import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Worker extends Thread{
    private int firstMove;
    private int depth;
    private int[][] board;
    private int[] columnLengths;
    private double value = -1;
    private static Map<String, Double> map = new HashMap<>(100000);

    public Worker(int move, int depth, int[][] board, int[] columnLengths){
        firstMove = Integer.valueOf(move);
        this.depth = depth;
        this.board = copy(board);
        this.columnLengths = copy(columnLengths);

    }

    private static int[][] copy(int[][] arr){
        int[][] temp = new int[arr.length][arr[0].length];
        for (int r=0;r<arr.length;r++){
            for (int c=0;c<arr[0].length;c++){
                temp[r][c] = arr[r][c];
            }
        }
        return temp;
    }
    private static int[] copy(int[] arr){
        int[] temp = new int[arr.length];
        for (int i=0;i<arr.length;i++){
            temp[i] = arr[i];

        }
        return temp;
    }


    @Override
    public void run(){

        value = minimax(firstMove, Integer.MIN_VALUE, Integer.MAX_VALUE, depth, true, board, columnLengths);
        System.out.println(firstMove + ": "+value);

    }
    private double minimax(int move, double alpha, double beta, int depth, boolean player1, int[][] board, int[] columnLengths){
        if (map.get(hash(board))!=null){
            return map.get(hash(board));
        }
        double maxVal;
        double minVal;
        double val;
        if (GameMethods.decideGameState(board)==1){
            return 100+depth;
        }
        if (GameMethods.decideGameState(board)==2){
            return -100-depth;
        }
        if (GameMethods.decideGameState(board)==0){
            return 0;
        }
        if (depth==0){
            if (player1){
                columnLengths[move]++;
                board[columnLengths[move]][move] = 0;
                double temp = Heuristics.heuristicValue(1, board, depth) - Heuristics.heuristicValue(2, board, depth);
                board[columnLengths[move]][move] = 2;
                columnLengths[move]--;
                return temp;
            }
            return Heuristics.heuristicValue(1, board, depth) - Heuristics.heuristicValue(2, board, depth);
            //return 0;
        }
        if (player1){
            maxVal = Integer.MIN_VALUE;
            ArrayList<ArrayList<Double>> moves = Heuristics.orderMoves(1, columnLengths,board, depth);
            for (int i=0;i<moves.size();i++){
                int currentMove = (int)Math.floor(moves.get(i).get(0));
                if (columnLengths[currentMove]>=0){
                    board[columnLengths[currentMove]][currentMove] = 1;
                    columnLengths[currentMove]--;
                    if (!mistake(2, board, columnLengths)){
                        val = minimax(currentMove, alpha, beta, depth -1, false, board, columnLengths);
                        map.putIfAbsent(hash(board), val);
                    }
                    else {
                        val = -100-depth+1;
                    }

                    columnLengths[currentMove]++;
                    board[columnLengths[currentMove]][currentMove] = 0;
                    maxVal = Math.max(maxVal, val);
                    alpha = Math.max(alpha, val);
                    if (beta<=alpha){
                        break;
                    }
                }
            }
            return maxVal;
        }
        else{
            minVal = Integer.MAX_VALUE;
            ArrayList<ArrayList<Double>> moves = Heuristics.orderMoves(2, columnLengths,board, depth);
            for (int i=0;i<moves.size();i++){
                int currentMove = (int)Math.floor(moves.get(i).get(0));
                if (columnLengths[currentMove]>=0){
                    board[columnLengths[currentMove]][currentMove] = 2;
                    columnLengths[currentMove]--;
                    if (!mistake(1, board, columnLengths)){
                        val = minimax(currentMove, alpha, beta, depth-1, true, board, columnLengths);
                        map.putIfAbsent(hash(board), val);
                    }
                    else {
                        val = 100+depth-1;
                    }

                    columnLengths[currentMove]++;
                    board[columnLengths[currentMove]][currentMove]=0;
                    minVal = Math.min(minVal, val);
                    beta = Math.min(beta, val);
                    if (beta<=alpha){
                        break;
                    }
                }
            }
            return minVal;
        }
    }
    private String hash(int[][] board){
        String result = "";
        for (int [] r: board){
            for (int c: r){
                result+=c;
            }
        }
        return result;
    }
    private boolean mistake(int player, int[][] board, int[] columnLengths){
        for (int i=0;i<columnLengths.length;i++){
            if (columnLengths[i]>=0){
                board[columnLengths[i]][i] = player;
                if (GameMethods.decideGameState(board)== player){
                    board[columnLengths[i]][i] = 0;
                    return true;
                }

                board[columnLengths[i]][i] = 0;

            }
        }
        return false;
    }
    public void print(int[][] arr){
        for (int i=0;i<arr.length;i++){
            for (int j=0;j<arr[i].length;j++){
                System.out.print(arr[i][j] + " ");
            }
            System.out.println();
        }
    }
    public double getValue() {
        return value;
    }
}
