import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.plaf.metal.MetalButtonUI;
import javax.xml.crypto.Data;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeListener;
import java.io.File;

public class View extends JFrame {
    public static int size;
    public static int numBomb;
    //public int size = 10;
    public int numGames = 0;
    public int numWon = 0;
    public int numPercent =0;
    JLabel gamesWon;
    JLabel gamesPlayed;
    JLabel winPercentage;
    JLabel timer;
    JButton[][] boardButtons;
    Preference preference;
   // Stats stats;
    DatabaseHelper databaseHelper = new DatabaseHelper();

    JMenuItem menuItem1, menuItem2, menuItem3, menuItem4;

    public View(){
        super("MineSweeper Fun!");
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //dimension will be adjustable upon opening of the program
        setPreferredSize(new Dimension(800,800));
        preference = databaseHelper.getPreferences();
       // stats = databaseHelper.getStats();
        size = preference.dimensions;
        numBomb = preference.numBombs;
        setupUI();
        pack();
    }

    private void setupUI() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        //size = 10;
        //Main game board grid panel of buttons
        JPanel board = new JPanel();
        board.setLayout(new GridLayout(size,size));

        boardButtons = new JButton[size][size];
        for(int x = 0; x<size; x++){
            for (int y=0; y<size; y++){
                boardButtons[x][y] = new JButton("");
                boardButtons[x][y].setFont(new Font("Courier",Font.BOLD, 15));

                board.add(boardButtons[x][y]);
            }
        }

        panel.add(board, BorderLayout.CENTER);

        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BorderLayout());

        JPanel statsPanel = new JPanel();
        statsPanel.setLayout(new GridLayout(3,2));
        statsPanel.setBorder(BorderFactory.createTitledBorder("Session Stats"));

        JLabel GamesPlayed = new JLabel ("Games Played: ");
        statsPanel.add(GamesPlayed);

        gamesPlayed = new JLabel (" " + numGames);
        statsPanel.add(gamesPlayed);

        JLabel GamesWon = new JLabel("Games Won: ");
        statsPanel.add(GamesWon);

        gamesWon = new JLabel (" " + numWon);
        statsPanel.add(gamesWon);

        JLabel WinPercentage = new JLabel("Win Percentage: ");
        statsPanel.add(WinPercentage);

        winPercentage = new JLabel( " " + numPercent);
        statsPanel.add(winPercentage);

        // i put a random minesweeper logo in there - doesnt have to stay i was just messing with images lol
        try {
            BufferedImage MSIcon = ImageIO.read(new File("MSlogo.png"));
            JLabel picLabel = new JLabel(new ImageIcon(MSIcon));
            picLabel.setPreferredSize(new Dimension(250, 50));
            topPanel.add(picLabel, BorderLayout.WEST);
        } catch (Exception e) {
            e.printStackTrace();
        }
        topPanel.add(statsPanel, BorderLayout.EAST);

        timer = new JLabel("0");
        JPanel timerBoard = new JPanel(new FlowLayout());
        timerBoard.setBorder(BorderFactory.createTitledBorder("Time:"));
        timerBoard.add(timer);
        topPanel.add(timerBoard, BorderLayout.CENTER);

        panel.add(topPanel, BorderLayout.NORTH);

        getContentPane().add(panel);

        makeMenu();
    }

    private void makeMenu(){
        JMenuBar menuBar;
        JMenu menu;

        menuBar = new JMenuBar();

        menu = new JMenu("Menu ");
        menu.setMnemonic(KeyEvent.VK_A);
        menu.getAccessibleContext().setAccessibleDescription("The only menu with options");
        menuBar.add(menu);

        menuItem1 = new JMenuItem("Preferences", KeyEvent.VK_P);

        menuItem1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                miniView MiniView = new miniView();
            }

            class miniView extends JFrame {
                public miniView(){
                    super("Set game preferences");
                    setVisible(true);
                    setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);//JFrame.EXIT_ON_CLOSE);
                    setPreferredSize(new Dimension(300,300));
                    setupUI();
                    // pack();
                }
                public void setupUI(){
                    JPanel panel = new JPanel();
                    panel.setLayout(new BorderLayout());
                    int selectedSize;
                    JPanel slidersPanel = new JPanel();
                    slidersPanel.setLayout(new GridLayout(0,2));
                    //function for the selection of the size
                    final int slider_min = 5;
                    final int slider_max = 50;
                    final int slider_initial_size = preference.dimensions;
                    final int slider_initial_bombs = preference.numBombs;

                    JSlider sizeSlider = new JSlider(JSlider.VERTICAL, slider_min, slider_max, slider_initial_size);
                    sizeSlider.addChangeListener(new MySliderChangeListener());

                    sizeSlider.setMajorTickSpacing(5);
                    sizeSlider.setMinorTickSpacing(1);
                    sizeSlider.setPaintTicks(true);
                    sizeSlider.setPaintLabels(true);

                    JPanel sizePanel = new JPanel();
                    sizePanel.setLayout(new BorderLayout());
                    sizePanel.add(sizeSlider, BorderLayout.CENTER);

                    sizePanel.setBorder(BorderFactory.createTitledBorder("Dimensions of board"));
                    slidersPanel.add(sizePanel);

                    JSlider bombSlider = new JSlider(JSlider.VERTICAL, slider_min, slider_max, slider_initial_bombs);
                    bombSlider.addChangeListener(new MySliderChangeListener2());

                    bombSlider.setMajorTickSpacing(5);
                    bombSlider.setMinorTickSpacing(1);
                    bombSlider.setPaintTicks(true);
                    bombSlider.setPaintLabels(true);

                    JPanel bombPanel = new JPanel();
                    bombPanel.setLayout(new BorderLayout());
                    bombPanel.add(bombSlider, BorderLayout.CENTER);

                    bombPanel.setBorder(BorderFactory.createTitledBorder("Number of bombs"));
                    slidersPanel.add(bombPanel);

                    JPanel instructions = new JPanel();
                    instructions.setLayout(new BorderLayout());
                    JLabel preferencesPanel = new JLabel("Here you can change the mode of the game by " );
                    JLabel preferencePanel2  = new JLabel("decreasing the number of bombs and the size of the board ");
                    JLabel preferencePanel3 = new JLabel ("if you are looking for more of a challenge!");
                    instructions.add(preferencesPanel, BorderLayout.NORTH);
                    instructions.add(preferencePanel2, BorderLayout.CENTER);
                    instructions.add(preferencePanel3, BorderLayout.SOUTH);

                    JButton saveButton = new JButton("Save");
                    saveButton.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent actionEvent) {
                            //update the changes in the sliders to the actual view and preferences
                            //close old view
                            miniView.this.setVisible(false);
                            miniView.this.dispose();
                            View.this.setVisible(false);
                            View.this.dispose();

                            databaseHelper.updatePreferences(1, preference.dimensions, preference.numBombs);

                            Controller controller = new Controller();
                        }
                    });


                    panel.add(instructions, BorderLayout.NORTH);
                    panel.add(slidersPanel,BorderLayout.CENTER);
                    panel.add(saveButton, BorderLayout.SOUTH);

                    getContentPane().add(panel);
                    pack();
                }
            }
            // View updatedView = new View();
        });
        menu.add(menuItem1);

        menuItem2 = new JMenuItem("Stats", KeyEvent.VK_S);
        menuItem2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent){ StatsView statsView = new StatsView();}

            class StatsView extends JFrame{

                public StatsView(){
                    super("Statistics");
                    setVisible(true);
                    setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
                    setPreferredSize(new Dimension(300,300));
                    setupUI();
                }
                public void setupUI(){
                    JPanel Panel = new JPanel();
                    Panel.setLayout(new BorderLayout());

                    JPanel titlePanel = new JPanel();
                    JLabel title = new JLabel("Here are your player statistics:");
                    titlePanel.add(title);
                    Panel.add(titlePanel, BorderLayout.NORTH);

                    JPanel stats = new JPanel();
                    stats.setLayout(new GridLayout(4,2));
                    JLabel GP = new JLabel("Games Played: ");
                    stats.add(GP);
                    JLabel gp = new JLabel(Integer.toString(View.this.numGames));
                    stats.add(gp);

                    JLabel GW = new JLabel("Games Won: ");
                    stats.add(GW);
                    JLabel gw = new JLabel(Integer.toString(View.this.numWon));
                    stats.add(gw);

                    JLabel WP = new JLabel("Win Percentage: ");
                    stats.add(WP);
                    JLabel wp = new JLabel(Double.toString(View.this.numPercent));
                    stats.add(wp);

                    JLabel FT = new JLabel("Fastest Time: ");
                    stats.add(FT);
                    JLabel ft = new JLabel("999");
                    stats.add(ft);

                    Panel.add(stats, BorderLayout.CENTER);

                    JButton closeButton = new JButton("Close");
                    closeButton.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent actionEvent){
                            StatsView.this.dispose();
                        }
                    });


                    Panel.add(closeButton, BorderLayout.SOUTH);

                    getContentPane().add(Panel);
                    pack();
                }
            }
        });
        menu.add(menuItem2);

        menuItem3 = new JMenuItem("New Game", KeyEvent.VK_N);
        //add action listener
        menu.add(menuItem3);

//        menuItem4 = new JMenuItem("Reset Board", KeyEvent.VK_R);
//        menu.add(menuItem4);

        setJMenuBar(menuBar);
    }
    class MySliderChangeListener implements ChangeListener {
        @Override
        public void stateChanged(ChangeEvent e) {
            JSlider source = (JSlider) e.getSource();
            int value  = source.getValue();
            size = value;
            preference.dimensions = value;
            System.out.println(value);

        }
    }
    class MySliderChangeListener2 implements ChangeListener {
        @Override
        public void stateChanged(ChangeEvent e) {
            JSlider source = (JSlider) e.getSource();
            int value = source.getValue();
            numBomb = value;
            preference.numBombs = value;
            System.out.println(value);
        }
    }
}