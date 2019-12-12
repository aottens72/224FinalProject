import javax.swing.*;

class MinesweeperCell extends JButton {
    public int touching = 0; //represents number of bombs touching cell, if -1 cell contains bomb
    int x;
    int y;

    public MinesweeperCell(int paramX, int paramY){
        x = paramX;
        y = paramY;
    }

    @Override
    public void setEnabled(boolean enabled) {
        if (this.getText() == Integer.toString(1)) {
            setText("<html><center><font color=\"111111\">Manage<br>Users</font></center></html>");
        } else if (getText() == Integer.toString(2)) {
            setText("<html><center><font color=\"222222\">Manage<br>Users</font></center></html>");
        } else if (getText() == Integer.toString(3)) {
            setText("<html><center><font color=\"333333\">Manage<br>Users</font></center></html>");
        } else if (getText() == Integer.toString(4)) {
            setText("<html><center><font color=\"444444\">Manage<br>Users</font></center></html>");
        } else if (getText() == Integer.toString(5)) {
            setText("<html><center><font color=\"555555\">Manage<br>Users</font></center></html>");
        } else if (getText() == Integer.toString(6)) {
            setText("<html><center><font color=\"666666\">Manage<br>Users</font></center></html>");
        } else if (getText() == Integer.toString(7)) {
            setText("<html><center><font color=\"777777\">Manage<br>Users</font></center></html>");
        } else if (getText() == Integer.toString(8)) {
            setText("<html><center><font color=\"888888\">Manage<br>Users</font></center></html>");
        }

        super.setEnabled(enabled);
        repaint();
    }

    protected void setBomb(){
        touching = -1;
    }

    protected void setTouching(MinesweeperCell[][] board){
        if(touching == -1){
            return;
        }
        if(x == 0 && y == 0){
            checkSurroundingTopLeft(board);
        }
        else if(x == 0 && y == View.size-1){
            checkSurroundingBottomLeft(board);
        }
        else if(x == View.size-1 && y == 0){
            checkSurroundingTopRight(board);
        }
        else if(x == View.size-1 && y == View.size-1){
            checkSurroundingBottomRight(board);
        }
        else if(x == 0){
            checkSurroundingLeftEdge(board);
        }
        else if(x == View.size-1){
            checkSurroundingRightEdge(board);
        }
        else if(y == 0){
            checkSurroundingTopEdge(board);
        }
        else if(y == View.size-1){
            checkSurroundingBottomEdge(board);
        }
        else{
            checkSurrounding(board);
        }
    }

    private void checkSurroundingTopLeft(MinesweeperCell[][] board){
        if(board[x + 1][y].touching == -1){
            touching++;
        }
        if(board[x + 1][y + 1].touching == -1){
            touching++;
        }
        if(board[x][y + 1].touching == -1){
            touching++;
        }
    }

    private void checkSurroundingBottomLeft(MinesweeperCell[][] board){
        if(board[x][y - 1].touching == -1){
            touching++;
        }
        if(board[x + 1][y - 1].touching == -1){
            touching++;
        }
        if(board[x + 1][y].touching == -1){
            touching++;
        }
    }

    private void checkSurroundingTopRight(MinesweeperCell[][] board){
        if(board[x -1][y].touching == -1){
            touching++;
        }
        if(board[x -1][y + 1].touching == -1){
            touching++;
        }
        if(board[x][y +1].touching == -1){
            touching++;
        }
    }

    private void checkSurroundingBottomRight(MinesweeperCell[][] board){
        if(board[x][y - 1].touching == -1){
            touching++;
        }
        if(board[x - 1][y - 1].touching == -1){
            touching++;
        }
        if(board[x - 1][y].touching == -1){
            touching++;
        }
    }

    private void checkSurroundingLeftEdge(MinesweeperCell[][] board){
        if(board[x][y + 1].touching == -1){
            touching++;
        }
        if(board[x + 1][y + 1].touching == -1){
            touching++;
        }
        if(board[x + 1][y].touching == -1){
            touching++;
        }
        if(board[x + 1][y - 1].touching == -1){
            touching++;
        }
        if(board[x][y - 1].touching == -1){
            touching++;
        }
    }

    private void checkSurroundingRightEdge(MinesweeperCell[][] board){
        if(board[x][y+1].touching == -1){
            touching++;
        }
        if(board[x - 1][y +1].touching == -1){
            touching++;
        }
        if(board[x -1][y].touching == -1){
            touching++;
        }
        if(board[x - 1][y -1].touching == -1){
            touching++;
        }
        if(board[x][y - 1].touching == -1){
            touching++;
        }
    }

    private void checkSurroundingTopEdge(MinesweeperCell[][] board){
        if(board[x-1][y].touching == -1){
            touching++;
        }
        if(board[x-1][y+1].touching == -1){
            touching++;
        }
        if(board[x][y+1].touching == -1){
            touching++;
        }
        if(board[x+1][y+1].touching == -1){
            touching++;
        }
        if(board[x+1][y].touching == -1){
            touching++;
        }
    }

    private void checkSurroundingBottomEdge(MinesweeperCell[][] board){
        if(board[x-1][y].touching == -1){
            touching++;
        }
        if(board[x-1][y-1].touching == -1){
            touching++;
        }
        if(board[x][y-1].touching == -1){
            touching++;
        }
        if(board[x+1][y-1].touching == -1){
            touching++;
        }
        if(board[x+1][y].touching == -1){
            touching++;
        }
    }

    private void checkSurrounding(MinesweeperCell[][] board){
        if(board[x +1][y].touching == -1){
            touching++;
        }
        if(board[x+1][y+1].touching == -1){
            touching++;
        }
        if(board[x][y+1].touching == -1){
            touching++;
        }
        if(board[x-1][y+1].touching == -1){
            touching++;
        }
        if(board[x-1][y].touching == -1){
            touching++;
        }
        if(board[x-1][y-1].touching == -1){
            touching++;
        }
        if(board[x][y-1].touching == -1){
            touching++;
        }
        if(board[x+1][y-1].touching == -1){
            touching++;
        }
    }
}

