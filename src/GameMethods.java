import java.lang.reflect.Array;
import java.util.ArrayList;


public class GameMethods {
    public static int decideGameState(int[][] board){
        if (calculateWin(1, board)){
            return 1;
        }
        if (calculateWin(2, board)){
            return 2;
        }
        if (isFull(board)){
            return 0;
        }
        return -1;
    }
    private static boolean isFull(int[][] board){
        for (int r=0;r< board.length;r++){
            for (int c=0;c<board[0].length;c++){
                if (board[r][c]==0){
                    return false;
                }
            }
        }
        return true;
    }
    private static boolean calculateWin(int num, int[][] board){
        return backSlashSearch(num, board)|| forwardSlashSearch(num, board) || horizontalSearch(num, board) || verticalSearch(num, board);

    }
    private static boolean backSlashSearch(int num, int[][] board){
       ArrayList<Integer> topList = new ArrayList<>();
        ArrayList<Integer> bottomList = new ArrayList<>();
        for (int i=board.length-4;i>=0;i--){
            for(int j=0;j<board[0].length-1-i;j++){
                topList.add(board[i+j][j]);
                bottomList.add(board[j][i+j+1]);
            }

            if (winningLine(topList, num)){
                return true;
            }
            if (winningLine(bottomList, num)){
                return true;
            }

            topList.clear();
            bottomList.clear();
        }
        return false;
    }
    private static boolean forwardSlashSearch(int num, int[][] board){
        ArrayList<Integer> topList = new ArrayList<>();
        ArrayList<Integer> bottomList = new ArrayList<>();
        for (int r=board.length-1;r>2;r--){
            for (int c=0;c<=r;c++){
                topList.add(board[r-c][c]);
                bottomList.add(board[board.length-1-r+c][board[0].length-1-c]);
            }
            if (winningLine(topList, num)){
                return true;
            }
            if (winningLine(bottomList, num)){
                return true;
            }
            topList.clear();
            bottomList.clear();
        }
        return false;
    }
    public static boolean verticalSearch(int num, int[][] board){
        for (int col=0;col<board[0].length;col++){
            for (int row=0;row<board.length-3;row++){
                if (board[row][col]==num && board[row+1][col]==num && board[row+2][col]==num && board[row+3][col]==num){
                    return true;
                }
            }
        }
        return false;
    }
    private static boolean horizontalSearch(int num, int[][] board){
        for (int row=0;row<board.length;row++){
            for (int col=0;col<board[0].length-3;col++){
                if (board[row][col]==num && board[row][col+1]==num && board[row][col+2]==num && board[row][col+3]==num){
                    return true;
                }
            }
        }
        return false;
    }
    private static boolean winningLine(ArrayList<Integer> list, int num){
        int count = 0;
        for (int i=0;i<list.size();i++){
            for (int j=i;j<list.size();j++){
                if (list.get(j)==num){
                    count++;
                }
                else{
                    count=0;
                    break;
                }
                if (count>3){
                    return true;
                }
            }
        }
        return false;
    }
}
