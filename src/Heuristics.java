import java.lang.reflect.Array;
import java.util.ArrayList;

public class Heuristics {
    public static ArrayList<ArrayList<Double>> orderMoves(int player, int[] columnLengths, int[][] board, int depth){
        ArrayList<ArrayList<Double>> orderedMoves = new ArrayList<>();
        ArrayList<Double> temp;
        for (int i=0;i<columnLengths.length;i++){
            temp = new ArrayList<>();
            if (columnLengths[i] >= 0){
                board[columnLengths[i]][i] = player;
                columnLengths[i]--;
                temp.add((double)i);
                temp.add(player==1? heuristicValue(1, board, depth): heuristicValue(2, board, depth));
                orderedMoves.add(temp);
                columnLengths[i]++;
                board[columnLengths[i]][i] = 0;
            }
            else {
                temp.add((double)i);
                temp.add(-200.0);
                orderedMoves.add(temp);
            }
        }
        return orderByCenter(selectionSort(orderedMoves));
    }
    public static ArrayList<ArrayList<Double>> orderByCenter(ArrayList<ArrayList<Double>> arr){

        ArrayList<Double> max = new ArrayList<>();
        max.add(-1.0);
        max.add(0.0);
        int maxIndex ;
        for (int i=0;i<arr.size();i++){
            maxIndex = -1;
            for (int j=i;j< arr.size();j++){
                if (arr.get(i).get(1).equals(arr.get(j).get(1)) && colValue(arr.get(j).get(0)) > colValue(arr.get(i).get(0))){
                    max = new ArrayList<>(arr.get(j));
                    maxIndex = j;
                }
            }
            if (maxIndex>-1){
                arr.set(maxIndex, new ArrayList<>(arr.get(i)));
                arr.set(i, new ArrayList<>(max));
                max = new ArrayList<>();
                max.add(-1.0);
                max.add(0.0);
            }

        }
        return arr;
    }
    private static ArrayList<ArrayList<Double>> selectionSort(ArrayList<ArrayList<Double>> arr){
        ArrayList<Double> max = new ArrayList<>();
        max.add(-1.0);
        max.add(Double.MIN_VALUE);
        int maxIndex ;
        for (int i=0;i<arr.size();i++){
            maxIndex = -1;
            for (int j=i;j< arr.size();j++){
                if (arr.get(j).get(1) > max.get(1)){
                    max = new ArrayList<>(arr.get(j));
                    maxIndex = j;
                }
            }
            if (maxIndex>-1){
                arr.set(maxIndex, new ArrayList<>(arr.get(i)));
                arr.set(i, new ArrayList<>(max));
                max = new ArrayList<>();
                max.add(-1.0);
                max.add(Double.MIN_VALUE);
            }

        }
        return arr;
    }

    public static double heuristicValue(int player, int[][] board, int depth){
        double val = heuristicBackSlashSearch(player, board) + heuristicForwardSlashSearch(player, board)
                + heuristicHorizontalSearch(player, board) + heuristicVerticalSearch(player, board);
        if (val<1000) {
            return val;
        }

        return 100+depth;
    }
    private static double heuristicBackSlashSearch(int player, int[][] board){
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
    private static double heuristicForwardSlashSearch(int player, int[][] board) {
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
    private static double heuristicVerticalSearch(int player, int[][] board){
        int val = 0;
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
    private static double heuristicHorizontalSearch(int player, int[][] board){
        int val = 0;
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

    private static double findConsecutives(int player, ArrayList<Integer> arr){
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
                        zeros+=.5;
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

}
