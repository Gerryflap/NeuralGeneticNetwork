package tictactoe;

/**
 * Created by gerben on 2-12-15.
 */
public class Board {
    int[] board = new int[9];
    int player = 1;

    public Board(){

    }

    public Board(int[] board, int player) {
        if (board.length == 9) {
            this.board = board;
            this.player = player;
        }
    }

    public int getPlayerAt(int i) {
        if (i >= 0 && i < 9) {
            return board[i];
        } else {
            return -1;
        }
    }

    public int getPlayerAt(int x, int y){
        return getPlayerAt(x + y*3);
    }

    public void setPlayerAt(int i, int player) {
        if (i >= 0 && i < 9) {
            board[i] = player;
        } else {
            System.err.printf("Cannot set player at %d, array is too short!\n", i);
        }
    }

    public void setPlayerAt(int x, int y, int player){
        setPlayerAt(x + y*3, player);
    }


    public boolean doMove(int x, int y){
        return doMove(x + y*3);
    }

    public boolean doMove(int i) {
        if (getPlayerAt(i) == 0) {
            setPlayerAt(i, player);
            player = (player)%2 + 1;
            return true;
        } else {
            return false;
        }
    }

    public int getWinner() {
        if (board[0] != 0 && board[0] == board[4] && board[4] == board[8]) {
            return board[0];
        } else if (board[2] != 0 && board[2] == board[4] && board[4] == board[6]) {
            return board[2];
        }

        for (int x = 0; x < 3; x++) {
            if (getPlayerAt(x, 0) > 0 && getPlayerAt(x, 0) == getPlayerAt(x, 1) && getPlayerAt(x, 1) == getPlayerAt(x, 2)) {
                return getPlayerAt(x, 0);
            }
        }

        for (int y = 0; y < 3; y++) {
            if (getPlayerAt(0, y) > 0 && getPlayerAt(0, y) == getPlayerAt(1, y) && getPlayerAt(1, y) == getPlayerAt(2, y)) {
                return getPlayerAt(0, y);
            }
        }

        boolean isFull = false;
        for (int i = 0; i < board.length; i++) {
            isFull = board[i] > 0;
            if (!isFull) {
                break;
            }
        }

        if (isFull) {
            return 0;
        } else {
            return -1;
        }
    }

    public double[] getBoardForAI(int iMove) {
        double[] aiBoard = new double[18];
        for (int i = 0; i < board.length; i++) {
            if (board[i] != 0 || i == iMove) {
                if (board[i] == player || i == iMove) {
                    aiBoard[i] = 1;
                    aiBoard[i + 9] = 0;
                } else {
                    aiBoard[i] = 0;
                    aiBoard[i + 9] = 1;
                }
            } else {
                aiBoard[i] = 0;
                aiBoard[i + 9] = 0;
            }
        }
        return aiBoard;
    }

    public String toString() {
        String output = "";
        for (int y = 0; y < 3; y++) {
            for (int x = 0; x < 3; x++) {
                output += String.format("%d\t", getPlayerAt(x, y));
            }
            output += "\n";

        }
        return output;
    }

    public int getFirstValidMove() {
        for (int i = 0; i < board.length; i++) {
            if(board[i] == 0) {
                return i;
            }
        }
        return 0;
    }

    public boolean isFull() {
        for (int aBoard : board) {
            if (aBoard == 0) {
                return false;
            }
        }
        return true;
    }

    public int getBestMove(){
        int bestMove = -1;
        int winner = 0;
        int bestScore = -1;
        for (int i = 0; i < board.length; i++) {
            if (board[i] == 0) {
                board[i] = player;
                if(isFull()) {
                    board[i] = 0;
                    return i;
                }
                player = (player)%2+1;
                int enemyBest = getBestMove();
                board[enemyBest] = player;
                player = (player)%2+1;
                winner = getWinner();
                board[enemyBest] = 0;
                board[i] = 0;
                if (i == 4) {
                    return i;
                } else if (winner == player) {
                    bestScore = 1;
                    bestMove = i;
                } else if (winner > 0 && bestScore == -1) {
                    bestMove = i;
                } else if (winner == -1 && bestScore < 0) {
                    bestScore = -1;
                    bestMove = i;
                } else if (winner == 0 && bestScore < 0){
                    bestMove = i;
                    bestScore = 0;
                }

            }
        }
        return bestMove;
    }
}
