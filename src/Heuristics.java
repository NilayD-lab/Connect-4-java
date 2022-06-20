import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Heuristics {
    public static double[][] orderMoves(int player, int[] columnLengths, int[][] board, int branch){
        double[][] orderedMoves = new double[7][2];
        double temp;
        for (int i=0;i<columnLengths.length;i++){
            //temp = new ArrayList<>();
            if (columnLengths[i] >= 0){
                board[columnLengths[i]][i] = player;
                columnLengths[i]--;
                orderedMoves[i][0] = i;
                orderedMoves[i][1] = heuristicValue(player, board);
                columnLengths[i]++;
                board[columnLengths[i]][i] = 0;
            }
            else {
                orderedMoves[i][0] = i;
                orderedMoves[i][1] = -200.0;
            }
        }
        return orderByCenter(selectionSort(orderedMoves));
    }
    public static double[][] orderByCenter(double[][] arr){

        double[] max = new double[2];
        max[0] = -1;
        max[1] = 0;
        int maxIndex ;
        for (int i=0;i<arr.length;i++){
            maxIndex = -1;
            for (int j=i;j< arr.length;j++){
                if (arr[i][1] == arr[j][1] && colValue(arr[j][0]) > colValue(max[0])){
                    max = copy(arr[j]);
                    maxIndex = j;
                }
            }

            if (maxIndex>-1){
                arr[maxIndex] = copy(arr[i]);
                arr[i] = copy(max);
                max = new double[2];
                max[0] = -1;
                max[1] = 0;
            }

        }
        return arr;
    }
    public static double[][] selectionSort(double[][] arr){
        double[] max = new double[2];
        max[0] = -1;
        max[1] = -200;
        int maxIndex ;
        for (int i=0;i<arr.length;i++){
            maxIndex = -1;
            for (int j=i;j< arr.length;j++){
                if (arr[j][1] > max[1]){
                    max = copy(arr[j]);
                    maxIndex = j;
                }
            }
            if (maxIndex > -1){
                arr[maxIndex] = copy(arr[i]);
                arr[i] = copy(max);
                max = new double[2];
                max[0] = -1;
                max[1] = -200;
            }


        }
        return arr;
    }

    public static Double heuristicValue(int player, int[][] board){
        double val = heuristicBackSlashSearch(player, board) + heuristicForwardSlashSearch(player, board)
                + heuristicHorizontalSearch(player, board) + heuristicVerticalSearch(player, board);
        if (val<100000) {
            return val;
        }

        return 100.0;
    }
    public static double heuristicBackSlashSearch(int player, int[][] board){
        double val = 0;
        ArrayList<Integer> topList = new ArrayList<>();
        ArrayList<Integer> bottomList = new ArrayList<>();
        for (int i=board.length-4;i>=0;i--) {
            for (int j = 0; j < board[0].length - 1 - i; j++) {
                topList.add(board[i + j][j]);
                bottomList.add(board[j][i + j + 1]);
            }
            val += findConsecutives(player, topList) + findConsecutives(player, bottomList);
            topList.clear();
            bottomList.clear();
        }
        return val;
    }
    public static double heuristicForwardSlashSearch(int player, int[][] board) {
        double val = 0;
        ArrayList<Integer> topList = new ArrayList<>();
        ArrayList<Integer> bottomList = new ArrayList<>();
        for (int r = board.length - 1; r > 2; r--) {
            for (int c = 0; c <= r; c++) {
                topList.add(board[r - c][c]);
                bottomList.add(board[board.length - 1 - r + c][board[0].length - 1 - c]);
            }
            val += findConsecutives(player, topList) + findConsecutives(player, bottomList);
            topList.clear();
            bottomList.clear();
        }
        return val;
    }
    public static double heuristicVerticalSearch(int player, int[][] board){
        double val = 0;
        ArrayList<Integer> list = new ArrayList<>();
        for (int c = 0;c<board[0].length;c++){
            for (int r=0;r<board.length;r++){
                list.add(board[r][c]);
            }
            val += findConsecutives(player, list);
            list.clear();
        }
        return val;
    }
    public static double heuristicHorizontalSearch(int player, int[][] board){
        double val = 0;
        ArrayList<Integer> list = new ArrayList<>();
        for (int r = 0;r<board.length;r++){
            for (int c=0;c<board[0].length;c++){
                list.add(board[r][c]);
            }
            val += findConsecutives(player, list);
            list.clear();
        }
        return val;
    }

    public static double findConsecutives(int player, ArrayList<Integer> arr){
        boolean zeroFound = false;
        boolean pieceFound = false;
        double consecutiveNums = 0;
        double zeros = 0;
        double score = 0;
        for (int i=0;i<arr.size();i++){
            if (arr.get(i)==player){
                for (int z=i;z<arr.size();z++){
                    if (arr.get(z)==player){
                        pieceFound = true;
                        consecutiveNums +=1;
                    }
                    if (arr.get(z)!=player || z==arr.size()-1){
                        if (consecutiveNums>=4){
                            return Double.MAX_VALUE/2;
                        }
                        if (zeroFound || arr.get(z)==0){
                            score+=consecutiveNums * consecutiveNums;
                            zeroFound = true;
                        }
                        if (z==arr.size()-1 && arr.get(z) == player){
                            return score;
                        }
                        if (arr.get(z)!=0 && arr.get(z) != player){
                            pieceFound = false;
                        }
                        //System.out.println("poopoo: "+consecutiveNums);
                        consecutiveNums = 0;
                        i = z - 1;
                        break;
                    }
                }
            }
            if (arr.get(i)==0){
                for (int z=i;z<arr.size();z++){
                    if (arr.get(z)==0){
                        zeroFound = true;
                        zeros+=0.5;
                    }
                    if (arr.get(z)!=0 || z==arr.size()-1){
                        if (pieceFound || arr.get(z)==player){
                            score+=zeros;
                        }
                        if (z==arr.size()-1 && arr.get(z)==0){
                            return score;
                        }
                        if (arr.get(z)!=0 && arr.get(z)!=player){
                            zeroFound = false;
                        }
                        //System.out.println("peepee: " + zeros);
                        zeros = 0;
                        i = z-1;
                        break;
                    }
                }
            }
            if (arr.get(i)!=0 && arr.get(i)!=player){
                zeroFound = false;
                pieceFound = false;
            }
        }
        return score;
    }
    public static int colValue(double i) {
        int[] arr = {1, 2, 3, 4, 3, 2, 1};
        if (i<0 || i>6){
            return -1;
        }
        return arr[(int)i];
    }
    public static double[] copy(double[] arr){
        double[] newArr = new double[arr.length];
        for (int i=0;i< arr.length;i++){
            newArr[i] = arr[i];
        }
        return newArr;
    }

}
