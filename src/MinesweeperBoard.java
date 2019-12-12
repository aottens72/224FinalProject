import java.util.Random;

class MinesweeperBoard {
    //View view;
    int size = View.size;
    int numBombs = View.numBomb;
    MinesweeperCell[][] board = new MinesweeperCell[size][size];

    public MinesweeperBoard(){
        for(int x = 0; x < size; x++){
            for(int y = 0; y < size; y++){
                board[x][y] = new MinesweeperCell(x, y);
            }
        }
        placeBombs();
        setTouching();
    }

    private void placeBombs(){
        Random random = new Random();
        int randX;
        int randY;
        for(int i = 0; i < numBombs; i++) {
            randX = random.nextInt(size);
            randY = random.nextInt(size);
            while(board[randX][randY].touching == -1) {
                randX = random.nextInt(size);
                randY = random.nextInt(size);
            }
            board[randX][randY].setBomb();
        }
    }

    private void setTouching(){
        for(int x = 0; x < size; x++){
            for(int y = 0; y < size; y++){
                board[x][y].setTouching(board);
            }
        }
    }

    public void print(){
        for(int x = 0; x < size; x++){
            for(int y = 0; y < size; y++){
                System.out.print(board[x][y].touching + "        ");
            }
            System.out.println();
        }
    }

    public static void main(String[] args) {
        MinesweeperBoard newBoard = new MinesweeperBoard();
        newBoard.print();
    }

    public String getNumber(int x, int y){
        return String.valueOf(board[x][y].touching);
    }
}

