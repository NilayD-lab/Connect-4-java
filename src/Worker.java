import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Worker extends Thread{
    private int firstMove;
    private int depth;
    private int[][] board;
    private int[] columnLengths;
    private double value = -1;
    private Map<String, Double> map = new HashMap<>(100000);
    private int num = 0;
    public Worker(int move, int depth, int[][] board, int[] columnLengths){
        firstMove = move;
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
        System.out.println(firstMove + ": "+num);
    }
    public ArrayList<ArrayList<Double>> fill(){
        ArrayList<ArrayList<Double>> arr = new ArrayList<>();
        ArrayList<Double> temp;
        for (int i=0;i<7;i++){
            temp = new ArrayList<>();
            temp.add((double)i);
            temp.add((double)i);
            arr.add(temp);
        }
        return arr;
    }

    private double minimax(int move, double alpha, double beta, int depth, boolean player1, int[][] board, int[] columnLengths){
        if (map.get(hash(board))!=null){
            return map.get(hash(board));
        }
        if (num==4000000){
            System.out.println(firstMove+" 4 mil");
        }
        else if (num == 3350000){
            System.out.println(firstMove + " 3.35 mil");
        }
        else if (num == 3300000){
            System.out.println(firstMove + " 3.3 mil");
        }
        else if (num == 3250000){
            System.out.println(firstMove + " 3.25 mil");
        }
        else if (num == 3200000){
            System.out.println(firstMove + " 3.2 mil");
        }
        else if (num == 3100000){
            System.out.println(firstMove + " 3.1 mil");
        }
        else if (num == 3000000){
            System.out.println(firstMove + " 3 mil");
        }
        else if (num == 2000000){
            System.out.println(firstMove + " 2 mil");
        }

        num++;
        double maxVal;
        double minVal;
        double val;
        int gameState = GameMethods.decideGameState(board);
        if (gameState==1){
//            System.out.println("bruhhhh");
//            print(board);
            map.putIfAbsent(hash(board), 100.0+depth);
            return 100+depth;
        }
        if (gameState==2){
//            System.out.println("booootf");
//            print(board);
            map.putIfAbsent(hash(board), -100.0-depth);
            return -100-depth;
        }
        if (gameState==0){
            map.putIfAbsent(hash(board), 0.0);
            System.out.println("draww");
            return 0;
        }
        if (depth==0 || num > 4000000){
//            if (player1){
//                columnLengths[move]++;
//                board[columnLengths[move]][move] = 0;
//                double temp = Heuristics.heuristicValue(1, board, depth) - Heuristics.heuristicValue(2, board, depth);
//                board[columnLengths[move]][move] = 2;
//                columnLengths[move]--;
//                return temp;
//            }
            return Heuristics.heuristicValue(1, board, depth) -Heuristics.heuristicValue(2, board, depth);
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

                    }
                    else {
                        val = -100-depth+1;
                    }
                    map.putIfAbsent(hash(board), val);
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
                    }
                    else {
                        val = 100+depth-1;
                    }

                    map.putIfAbsent(hash(board), val);
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
    public static String hash(int[][] board){
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
