import java.util.Locale;


/*
{
            {0, 0, 0, 0, 0, 0, 0},
            {2, 2, 2, 1, 2, 2, 2},
            {1, 1, 1, 2, 1, 1, 1},
            {2, 2, 1, 1, 1, 2, 1},
            {1, 1, 2, 2, 1, 1, 2},
            {2, 2, 1, 1, 2, 2, 1},
            {2, 2, 1, 1, 2, 1, 2}
    };
 */
import java.util.*;

/*
{
        {0, 1, 0, 1, 0, 1, 0},
        {1, 2, 0, 2, 0, 1, 0},
        {2, 2, 0, 1, 0, 2, 1},
        {1, 1, 0, 2, 0, 1, 2},
        {2, 2, 0, 1, 2, 2, 1},
        {2, 1, 1, 2, 2, 1, 2}
        };
        {0, -1, 4, -1, 3, -1, 1};

        {
        {0, 0, 0, 2, 0, 0, 0},
        {1, 0, 0, 2, 0, 0, 0},
        {2, 0, 0, 1, 0, 0, 0},
        {1, 0, 0, 2, 0, 0, 0},
        {1, 0, 0, 1, 0, 0, 0},
        {1, 0, 0, 2, 2, 0, 0}
        };
        {0, 5, 5, -1, 4, 5, 5};

        {
        {2, 2, 0, 2, 1, 0, 2},
        {1, 1, 0, 2, 2, 0, 2},
        {1, 2, 0, 1, 1, 0, 1},
        {2, 1, 1, 2, 1, 1, 2},
        {2, 2, 2, 1, 1, 1, 2},
        {1, 1, 1, 2, 2, 1, 2}
        };
        {-1, -1, 2, -1, -1, 2, -1};
 */
public class Thing {
    private static int[] columnLength =  {5,5, 5, 5,5, 5, 5};

    private static int[][] board = new int[6][7];


    private static int[] scores = {-5,-5,-5,-5,-5,-5,-5};
    private static Scanner sc = new Scanner(System.in);
    private static int turn=1;
    private static int depth = 14;
    private static int size = 6;
    private static Map<String, Integer> map = new HashMap<>(100000);
    public static void main(String[] args) {
        int answer;
        int bestMove = Integer.MAX_VALUE;
        int bestScore = Integer.MAX_VALUE;
        while (!generalSearch(1, board) && !generalSearch(2, board)){
            if (turn%2==1) {
                answer = sc.nextInt();
                board[columnLength[answer]][answer] = 1;
                columnLength[answer]--;
                print(board);
            }
            else{
                //map.clear();
                depth++;

//                if (size>numOfCols()){
//                    depth+=6;
//                    size--;
//                }
                bestScore = Integer.MAX_VALUE;
                bestMove = -10;
                for (int i=0;i<columnLength.length;i++){
                    if(columnLength[i]>=0){
                        board[columnLength[i]][i] = 2;
                        columnLength[i]--;
                        scores[i] = minimax(i,Integer.MIN_VALUE, Integer.MAX_VALUE, depth,true);
                        columnLength[i]++;
                        board[columnLength[i]][i] = 0;
                        if (scores[i]==0){
                            scores[i] = -1*colValue(i);
                        }
                    }
                }
                for (int i=0;i<scores.length;i++){
                    if (columnLength[i]>=0 && bestScore>scores[i]){
                        bestScore = scores[i];
                        bestMove = i;
                    }
                    else if (columnLength[i]>=0 && bestScore==scores[i]){
                        bestScore = scores[i];
                        bestMove = i;
                    }
                }
                System.out.println(turn);
                System.out.println(depth);
                board[columnLength[bestMove]][bestMove] = 2;
                columnLength[bestMove]--;
                print(board);
                for (int i=0;i<scores.length;i++){
                    System.out.print(scores[i]+" ");
                }
                System.out.println();

            }
            System.out.println();
            turn++;
        }

    }


    public static void print(int[][] arr){
        for (int i=0;i<arr.length;i++){
            for (int j=0;j<arr[i].length;j++){
                System.out.print(arr[i][j] + " ");
            }
            System.out.println();
        }
    }
    public static void print(int[] arr){
        for (int i: arr){
            System.out.print(i+" ");
        }
        System.out.println();
    }

    public static int minimax(int move, int alpha, int beta, int depth, boolean player1){
        if (map.get(arrToString(board))!=null){
            return map.get(arrToString(board));
        }

        int maxVal;
        int minVal;
        int eval;
        int bestMove;
        ArrayList<Integer> hValOfNodes = new ArrayList<>();
        boolean full = true;
        if (generalSearch(1, board)){
            map.putIfAbsent(arrToString(board), 100+depth);
            return 100+depth;
        }
        if (generalSearch(2, board)){
            map.putIfAbsent(arrToString(board), -100-depth);
            return -100-depth;
        }
        for (int r=0;r<board.length;r++){
            for (int c=0;c<board[r].length;c++){
                if (board[r][c]==0){
                    full = false;
                }
            }
        }
        if (full){
            return 50;
        }
        if (depth==0){
            return 0;
            //heuristicValue(move, player1, depth);
        }

        if (player1){
            maxVal = Integer.MIN_VALUE;
            for (int i=0;i<columnLength.length;i++){
                if (columnLength[i]>=0){
                    //hValOfNodes.add(heuristicValue(i, player1, depth));
                }
            }
//            while (!isAllNull(hValOfNodes)) {
//                bestMove = max(hValOfNodes);
//                if (columnLength[bestMove]>=0){
//                    board[columnLength[bestMove]][bestMove] = 1;
//                    columnLength[bestMove]--;
//                    eval = minimax(bestMove, alpha, beta, depth-1, false);
//                    columnLength[bestMove]++;
//                    board[columnLength[bestMove]][bestMove] = 0;
//                    maxVal = max(maxVal, eval);
//                    alpha = max(alpha, eval);
//                    if (beta<=alpha){
//                        break;
//                    }
//                }
//                hValOfNodes.set(bestMove, null);
//            }
            for (int i=0;i<columnLength.length;i++){
                if (columnLength[i]>=0){
                    board[columnLength[i]][i] = 1;
                    columnLength[i]--;
                    eval = minimax(i, alpha, beta, depth-1, false);
                    columnLength[i]++;
                    board[columnLength[i]][i] = 0;
                    maxVal = max(maxVal, eval);
                    alpha = max(alpha, eval);
                    if (beta<=alpha){
                        break;
                    }
                }
            }

            map.putIfAbsent(arrToString(board), maxVal);

            return maxVal;
        }
        else {
            minVal = Integer.MAX_VALUE;
            for (int i=0;i<columnLength.length;i++){
                if (columnLength[i]>=0){
                    //hValOfNodes.add(heuristicValue(i, player1, depth));
                }
            }
//            while (!isAllNull(hValOfNodes)) {
//                bestMove = min(hValOfNodes);
//                if (columnLength[bestMove]>=0){
//                    board[columnLength[bestMove]][bestMove] = 2;
//                    columnLength[bestMove]--;
//                    eval = minimax(bestMove, alpha, beta, depth-1, true);
//                    columnLength[bestMove]++;
//                    board[columnLength[bestMove]][bestMove] = 0;
//                    minVal = min(minVal, eval);
//                    beta  = min(beta, eval);
//                    if (beta<=alpha){
//                        break;
//                    }
//                }
//                hValOfNodes.set(bestMove, null);
//            }
            for (int i=0;i<columnLength.length;i++){
                if (columnLength[i]>=0){
                    board[columnLength[i]][i] = 2;
                    columnLength[i]--;
                    eval = minimax(i, alpha, beta, depth-1, true);
                    columnLength[i]++;
                    board[columnLength[i]][i] = 0;
                    minVal = min(minVal, eval);
                    beta  = min(beta, eval);
                    if (beta<=alpha){
                        break;
                    }
                }
            }
            map.putIfAbsent(arrToString(board), minVal);

            return minVal;
        }

    }

    public static int heuristicValue(int move, boolean isPlayer1, int depth){
        if (isPlayer1){
            for (int i=0;i<columnLength.length;i++){
                if (columnLength[i]>=0){
                    board[columnLength[i]][i] = 2;
                    if (generalSearch(2, board)) {
                        board[columnLength[i]][i] = 0;
                        return -100-depth;
                    }
                    board[columnLength[i]][i] = 0;
                }

            }

        }
        else {
            for (int i=0;i<columnLength.length;i++){
                if (columnLength[i]>=0){
                    board[columnLength[i]][i] = 1;
                    if (generalSearch(1, board)) {
                        board[columnLength[i]][i] = 0;
                        return 100+depth;
                    }
                    board[columnLength[i]][i] = 0;
                }
            }
        }
        if (isPlayer1){
            if (doubleWin(2, 1)>1){
                return 100+depth;
            }

        }
        else {
            if (doubleWin(1, 2)>1){
                return -100-depth;
            }
        }
        if (isPlayer1){
            if (mistake(2)) {
                return -100 - depth;
            }
        }
        else if (mistake(1)) {
            return 100+depth;
        }

        return 0;
    }

    public static int doubleWin(int player1, int player2){
        int count = 0;
        for (int i=0;i<columnLength.length;i++){
            count=0;
            if (columnLength[i]>=0){
                board[columnLength[i]][i] = player1;
                columnLength[i]--;
                for (int c=0;c<columnLength.length;c++){
                    if (columnLength[c]>=0) {
                        board[columnLength[c]][c] = player2;
                        if (generalSearch(player2, board)) {
                            count++;
                            if (count==2){
                                board[columnLength[c]][c] = 0;
                                columnLength[i]++;
                                board[columnLength[i]][i] = 0;
                                return count;
                            }
                        }
                        board[columnLength[c]][c] = 0;
                    }
                }
                columnLength[i]++;
                board[columnLength[i]][i] = 0;
            }
        }
        return count;
    }

    public static boolean mistake(int player2){
        int count = 0;
        for (int i=0;i<columnLength.length;i++){
            if (columnLength[i]>=0){
                board[columnLength[i]][i] = player2;
                columnLength[i]--;
                for (int c=0;c<columnLength.length;c++){
                    if (columnLength[c]>=0) {
                        board[columnLength[c]][c] = player2;
                        if (generalSearch(player2, board)) {
                            count++;
                        }
                        board[columnLength[c]][c] = 0;
                    }
                }
                columnLength[i]++;
                board[columnLength[i]][i] = 0;
            }
        }
        return count>2;
    }
    public static int max(ArrayList<Integer> arr){
        int maxVal = Integer.MIN_VALUE;
        int maxIndex = -1;
        for (int i=0;i<arr.size();i++){
            if (arr.get(i)!=null && arr.get(i)>maxVal){
                maxVal = arr.get(i);
                maxIndex = i;
            }
        }
        return maxIndex;
    }
    public static int min(ArrayList<Integer> arr){
        int minValue = Integer.MAX_VALUE;
        int minIndex = -1;
        for (int i=0;i<arr.size();i++){
            if (arr.get(i)!=null && arr.get(i)<minValue){
                minValue = arr.get(i);
                minIndex = i;
            }
        }
        return minIndex;
    }


    public static String arrToString(int[][] arr){
        String bean = "";
        for (int [] r: arr){
            for (int c: r){
                bean+=c;
            }
        }
        return bean;
    }
    public static boolean isAllNull(ArrayList<Integer> arr){
        for (Integer i: arr){
            if (i!=null){
                return false;
            }
        }
        return true;
    }
    public static int numOfCols(){
        int count = 0;
        for (int i=0;i<columnLength.length;i++){
            if (columnLength[i]>=0){
                count++;
            }
        }
        return count;
    }














    public static int colValue(int i){
        if (columnLength[i]!=-1){
            switch (i){
                case 0: case 6: return 1;
                case 1: case 5: return 2;
                case 2: case 4: return 3;
                case 3: return 4;
                default: return -1;
            }

        }
        return 0;
    }

    public static int max(int a, int b){
        if (a>b){
            return a;
        }
        else {
            return b;
        }
    }
    public static int min(int a, int b){
        if (a<b){
            return a;
        }
        else {
            return b;
        }
    }

    public static boolean generalSearch(int num, int[][] board){
        return backSlashSearch(num, board) || fowardSlashSearch(num, board) || horizontalSearch(num, board) || verticalSearch(num, board);
    }
    public static boolean backSlashSearch(int num, int[][] board){
        ArrayList<Integer> topList = new ArrayList<>();
        ArrayList<Integer> bottomList = new ArrayList<>();
        for (int i=board.length-4;i>=0;i--){
            for(int j=0;j<board[0].length-1-i;j++){
                topList.add(board[i+j][j]);
                bottomList.add(board[j][i+j+1]);
            }
            for (int z=0;z<topList.size()-3;z++){
                if(topList.get(z)==num && topList.get(z+1)==num && topList.get(z+2)==num && topList.get(z+3)==num){
                    return true;
                }
            }
            for (int z=0;z<bottomList.size()-3;z++){

                if(bottomList.get(z)==num && bottomList.get(z+1)==num && bottomList.get(z+2)==num && bottomList.get(z+3)==num){
                    return true;
                }
            }
            topList.clear();
            bottomList.clear();
        }
        return false;
    }
    public static boolean fowardSlashSearch(int num, int[][] board){
        ArrayList<Integer> topList = new ArrayList<>();
        ArrayList<Integer> bottomList = new ArrayList<>();
        for (int r=board.length-1;r>2;r--){
            for (int c=0;c<=r;c++){
                topList.add(board[r-c][c]);
                bottomList.add(board[board.length-1-r+c][board[0].length-1-c]);
            }
            for (int z=0;z<topList.size()-3;z++){
                if(topList.get(z)==num && topList.get(z+1)==num && topList.get(z+2)==num && topList.get(z+3)==num){
                    return true;
                }
                if(bottomList.get(z)==num && bottomList.get(z+1)==num && bottomList.get(z+2)==num && bottomList.get(z+3)==num){
                    return true;
                }
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
    public static boolean horizontalSearch(int num, int[][] board){
        for (int row=0;row<board.length;row++){
            for (int col=0;col<board[0].length-3;col++){
                if (board[row][col]==num && board[row][col+1]==num && board[row][col+2]==num && board[row][col+3]==num){
                    return true;
                }
            }
        }
        return false;
    }




}