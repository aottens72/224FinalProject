import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

public class Controller {

    class MySliderChangeListener implements ChangeListener {
        @Override
        public void stateChanged(ChangeEvent e) {
            JSlider source = (JSlider) e.getSource();
            int value  = source.getValue();
            // System.out.println("Selected: " + value);
        }
    }

    View view = new View();
    MinesweeperBoard board = new MinesweeperBoard();
    int time = 0;
    int num_clicks = 0;
    Timer timer;
    DatabaseHelper databaseHelper = new DatabaseHelper();

    public Controller(){
        addListeners();
        timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                time++;
                view.timer.setText(Integer.toString(time));
            }
        });
    }

    public void addListeners(){
        for(int x=0; x<View.size; x++){
            for(int y=0; y<View.size; y++){
                final int X = x;
                final int Y = y;

                // changed to mouse event so that we can tell left and right click ( dont need action listener )
                view.boardButtons[x][y].addMouseListener(new MouseListener() {

                    @Override
                    public void mouseClicked(MouseEvent e) {

                    }

                    @Override
                    public void mousePressed(MouseEvent e) {

                        //update timer and stats
                        if(num_clicks == 0){
                            timer.start();
                            view.numGames++;
                            view.gamesPlayed.setText(" " + view.numGames);
                        }
                        num_clicks++;

                        // if left or middle button normal functionality
                        if(SwingUtilities.isLeftMouseButton(e) || SwingUtilities.isMiddleMouseButton(e)) {
                            if(view.boardButtons[X][Y].getIcon() != null) {
                                return;
                            }
                            if (board.board[X][Y].touching == -1) {
                                hitBomb(board.board, X, Y);
                                return;
                            }
                            view.boardButtons[X][Y].setText(board.getNumber(X, Y));//String.valueOf(board.board[X][Y].touching));
                            if (board.board[X][Y].touching == 0) {
                                checkSurrounding(board.board, X, Y);
                                checkPerimeter(board.board);
                                checkPerimeter(board.board);
                            }
                            view.boardButtons[X][Y].setEnabled(false);

                            // if right button flag
                        } else if (SwingUtilities.isRightMouseButton(e)) {
                            if(view.boardButtons[X][Y].isEnabled() == false) {
                                return;
                            }
                            if(view.boardButtons[X][Y].getIcon() != null) {
                                view.boardButtons[X][Y].setEnabled(true);
                                view.boardButtons[X][Y].setText("");
                                view.boardButtons[X][Y].setIcon(null);
                                return;
                            }
                            displayIcon(X, Y, "flagIcon.png");
                        }

                        checkForWin(board.board);
                        view.repaint();
                    }

                    @Override
                    public void mouseReleased(MouseEvent e) {

                    }

                    @Override
                    public void mouseEntered(MouseEvent e) {

                    }

                    @Override
                    public void mouseExited(MouseEvent e) {

                    }
                });
            }
        }
        view.menuItem3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                timer.stop();
                resetBoard();
            }
        });
    }

    // i made a function for everything it does when it hits the bomb and
    // put it all in there so its less cluttered and easier to understand
    private void hitBomb(MinesweeperCell[][] board, int x, int y) {
        timer.stop();
        showBombs(board);
        //set image of button to bomb
        //play explosion sound
        //dialog box to notify you lose, ask if want to play again
        //update stats (model)

        //update stats
        //still needs to be formatted correctly
        view.winPercentage.setText(" " + String.format("%.2f", (view.numWon/(double) view.numGames)*100) + "%");
        Stats stats = databaseHelper.getStats();
        stats.gamesPlayed += view.numGames;
        stats.gamesWon += view.numWon;
        stats.winPercentage = ((view.numWon/(double) view.numGames)*100);
        databaseHelper.updateStats(1, stats.gamesPlayed, stats.gamesWon, stats.winPercentage, stats.fastestTime);


        // makes bomb sound
        try {
            String soundName = "Bomb.wav";
            AudioInputStream bombSound = AudioSystem.getAudioInputStream(new File(soundName).getAbsoluteFile());
            Clip clip = AudioSystem.getClip();
            clip.open(bombSound);
            clip.start();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // puts bomb icon on button
        displayIcon(x, y, "bombIcon.png");

        int choice = JOptionPane.showConfirmDialog(view, "You have hit a bomb and lost :( Do you" +
                " want to play again?", "Play Again?", JOptionPane.YES_NO_OPTION);
        if (choice == JOptionPane.NO_OPTION) {
            System.exit(0);
        }

        if (choice == JOptionPane.YES_OPTION) {
            resetBoard();
        }
    }

    // resets the board (when playing again)
    private void resetBoard() {
        for (int a = 0; a < View.size; a++) {
            for (int b = 0; b < View.size; b++) {
                view.boardButtons[a][b].setIcon(null);
                view.boardButtons[a][b].setEnabled(true);
                view.boardButtons[a][b].setText("");
            }
        }
        board = new MinesweeperBoard();
        view.timer.setText("0");
        num_clicks = 0;
        time = 0;
        view = new View();
        return;
    }

    // checks the perimeter of all of the 0's so that can make numbers visible
    private void checkPerimeter(MinesweeperCell[][] board) {
        try {
            for (int a = 0; a < View.size; a++) {
                for (int b = 0; b < View.size; b++) {
                    if (board[a][b].touching == 0 && view.boardButtons[a][b].isEnabled() == false) {
                        // i added "view.boardButtons[a + 1][b].getIcon() == null" to the if statement so that if theres a flag it will not
                        // put the number over top of it (which looks really odd), just leaves it flagged
                        // doesnt exactly work perfectly yet... we workin on it
                        if ((a + 1 < View.size) && view.boardButtons[a + 1][b].isEnabled() && view.boardButtons[a + 1][b].getIcon() == null) {
                            view.boardButtons[a + 1][b].setEnabled(false);
                            board[a + 1][b].setEnabled(false);
                            view.boardButtons[a + 1][b].setText(Integer.toString(board[a + 1][b].touching));
                            //UIManager.put(Button, Color.RED);
                        }

                        if ((a + 1 < View.size) && (b + 1 < View.size) && view.boardButtons[a + 1][b + 1].isEnabled() && board[a + 1][b + 1].touching > 0 && view.boardButtons[a + 1][b + 1].getIcon() == null) {
                            view.boardButtons[a + 1][b + 1].setEnabled(false);
                            board[a + 1][b + 1].setEnabled(false);
                            view.boardButtons[a + 1][b + 1].setText(Integer.toString(board[a + 1][b + 1].touching));
                        }

                        if ((b + 1 < View.size) && view.boardButtons[a][b + 1].isEnabled() && view.boardButtons[a][b + 1].getIcon() == null) {
                            view.boardButtons[a][b + 1].setEnabled(false);
                            board[a][b + 1].setEnabled(false);
                            view.boardButtons[a][b + 1].setText(Integer.toString(board[a][b + 1].touching));
                        }

                        if ((a > 0) && (b + 1 < View.size) && view.boardButtons[a - 1][b + 1].isEnabled() && board[a - 1][b + 1].touching > 0 && view.boardButtons[a - 1][b + 1].getIcon() == null) {
                            view.boardButtons[a - 1][b + 1].setEnabled(false);
                            board[a - 1][b + 1].setEnabled(false);
                            view.boardButtons[a - 1][b + 1].setText(Integer.toString(board[a - 1][b + 1].touching));
                        }

                        if ((a > 0) && view.boardButtons[a - 1][b].isEnabled() && view.boardButtons[a - 1][b].getIcon() == null) {
                            view.boardButtons[a - 1][b].setEnabled(false);
                            board[a - 1][b].setEnabled(false);
                            view.boardButtons[a - 1][b].setText(Integer.toString(board[a - 1][b].touching));
                        }

                        if ((a > 0) && (b > 0) && view.boardButtons[a - 1][b - 1].isEnabled() && board[a - 1][b - 1].touching > 0 && view.boardButtons[a - 1][b - 1].getIcon() == null) {
                            view.boardButtons[a - 1][b - 1].setEnabled(false);
                            board[a - 1][b - 1].setEnabled(false);
                            view.boardButtons[a - 1][b - 1].setText(Integer.toString(board[a - 1][b - 1].touching));
                        }

                        if ((b > 0) && view.boardButtons[a][b - 1].isEnabled() && board[a][b - 1].touching > 0 && view.boardButtons[a][b - 1].getIcon() == null) {
                            view.boardButtons[a][b - 1].setEnabled(false);
                            board[a][b - 1].setEnabled(false);
                            view.boardButtons[a][b - 1].setText(Integer.toString(board[a][b - 1].touching));
                        }

                        if ((a + 1 < View.size) && (b > 0) && view.boardButtons[a + 1][b - 1].isEnabled() && board[a + 1][b - 1].touching > 0 && view.boardButtons[a + 1][b - 1].getIcon() == null) {
                            view.boardButtons[a + 1][b - 1].setEnabled(false);
                            board[a + 1][b - 1].setEnabled(false);
                            view.boardButtons[a + 1][b - 1].setText(Integer.toString(board[a + 1][b - 1].touching));
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // recursively reveals all the 0's touching the button user clicked (if a 0)
    private void checkSurrounding(MinesweeperCell[][] board, int x, int y) {
        try {

            if(y < 0 || y >= View.size) {
                return;
            }

            view.boardButtons[x][y].setText("");

            if (x + 1 < View.size && board[x + 1][y].touching == 0 && view.boardButtons[x + 1][y].isEnabled() && view.boardButtons[x + 1][y].getIcon() == null) {
                view.boardButtons[x + 1][y].setEnabled(false);
                checkSurrounding(board,x + 1, y);
            }

            if ((y + 1 < View.size) && board[x][y + 1].touching == 0 && view.boardButtons[x][y + 1].isEnabled() && view.boardButtons[x][y + 1].getIcon() == null) {
                view.boardButtons[x][y + 1].setEnabled(false);
                checkSurrounding(board, x, y + 1);
            }

            if ((x > 0) && board[x - 1][y].touching == 0 && view.boardButtons[x - 1][y].isEnabled() && view.boardButtons[x - 1][y].getIcon() == null) {
                view.boardButtons[x - 1][y].setEnabled(false);
                checkSurrounding(board,x - 1, y);
            }

            if ((y > 0) && board[x][y - 1].touching == 0 && view.boardButtons[x][y - 1].isEnabled() && view.boardButtons[x][y - 1].getIcon() == null) {
                view.boardButtons[x][y - 1].setEnabled(false);
                checkSurrounding(board, x , y - 1);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // checks if the user has won by counting the number of flags that actually cover bombs and then displays win message
    private void checkForWin (MinesweeperCell[][] board) {
        int countFlags = 0;

        for(int x = 0; x < View.size; x++) {
            for(int y = 0; y < View.size; y++) {
                if(view.boardButtons[x][y].getIcon() != null) {
                    if(board[x][y].touching == -1) {
                        countFlags++;
                    }
                }
            }
        }

        if(countFlags == View.numBomb) {
            timer.stop();
            showBombs(board);

            //update stats
            view.numWon++;
            view.gamesWon.setText(" " + view.numWon);
            view.winPercentage.setText(" " + String.format("%.2f", (view.numWon/(double) view.numGames)*100) + "%");
            Stats stats = databaseHelper.getStats();
            stats.gamesPlayed += view.numGames;
            stats.gamesWon += view.numWon;
            stats.winPercentage = ((view.numWon/(double) view.numGames)*100);
            if(time < stats.fastestTime){
                stats.fastestTime = time;
            }
            databaseHelper.updateStats(1, stats.gamesPlayed, stats.gamesWon, stats.winPercentage, stats.fastestTime);
            int choice = JOptionPane.showConfirmDialog(view, "You won! Do you" +
                    " want to play again?", "Play Again?", JOptionPane.YES_NO_OPTION);
            if (choice == JOptionPane.NO_OPTION) {
                System.exit(0);
            }

            if (choice == JOptionPane.YES_OPTION) {
                resetBoard();
            }
        } else {
            return;
        }
    }

    private void showBombs(MinesweeperCell[][] board) {
        try {
            for(int x = 0; x < View.size; x++) {
                for(int y = 0; y < View.size; y++) {
                    if(board[x][y].touching == -1 && board[x][y].getIcon() != null) {
                        displayIcon(x, y, "bombWrongIcon.png");
                    } else if(board[x][y].touching == -1 && board[x][y].getIcon() == null) {
                        displayIcon(x, y,"bombIcon.png");
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void displayIcon(int x, int y, String iconPath) {
        // puts bomb icon on button
        try {
            BufferedImage img = ImageIO.read(new File(iconPath));
            view.boardButtons[x][y].setText("");
            view.boardButtons[x][y].setIcon(new ImageIcon(img));
        } catch (Exception a) {
            a.toString();
        }
    }
}