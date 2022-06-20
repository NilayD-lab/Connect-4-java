
import com.sun.tools.jconsole.JConsoleContext;
import jdk.swing.interop.SwingInterOpUtils;

import java.util.*;

public class App {
    private static int[] columnLengths =  {5, 5, 5, 5, 5, 5, 5};
    private static double[] scores = {-57,-57,-57,-57,-57,-57,-57};
    private static Scanner sc = new Scanner(System.in);
    private static int[][] board = new int[6][7];
    static int[][] board2 = {
            {0, 0, 0, 0, 0, 0, 0},
            {0, 2, 0, 0, 0, 0, 0},
            {0, 1, 0, 0, 2, 0, 0},
            {0, 1, 0, 2, 1, 0, 0},
            {0, 2, 1, 1, 2, 0, 0},
            {1, 2, 1, 2, 1, 2, 2}

    };
    private static int count = 0;
    private static int depth = 14;
    private static Worker[] workers = new Worker[7];
    private static ArrayList<Worker> workersBack = new ArrayList<>();
    private static Timer timer;
    private static int numOfWorkers = 7;
    public static void main(String[] args) throws Exception {
        ArrayList<Integer> arr = new ArrayList<>();
        arr.add(0);
        arr.add(0);
        arr.add(0);
        arr.add(0);
        arr.add(0);
        arr.add(0);
        System.out.println(Heuristics.heuristicValue(2, board2));
        //System.out.println(Heuristics.findConsecutives(2, arr));
        //System.out.println(Heuristics.heuristicVerticalSearch(2, board2));
//        if (count%2==1){
//            board[columnLengths[3]][3] = 2;
//            columnLengths[3]--;
//            count++;
//            print(board);
//        }

       //game();
        System.out.println(7 & 15);

    }
    public static void game(){
        if (count%5==0){
            depth++;
        }
        if (GameMethods.decideGameState(board)!=-1){
            System.out.println("wooooo");
            timer.cancel();
            return;
        }
        System.out.println("turn: "+count);
        System.out.println("depth: "+depth);
        int answer;
        if (count%2==0){
            System.out.println("Player 1 turn");
            answer = sc.nextInt();
            board[columnLengths[answer]][answer] = 1;
            columnLengths[answer]--;
            print(board);
            System.out.println();
            count++;
            game();
        }
        else {
            for (int i=0;i<numOfWorkers;i++){
                if (columnLengths[i]>=0) {
                    board[columnLengths[i]][i] = 2;
                    columnLengths[i]--;
                    workers[i] = new Worker(i, depth, board, columnLengths);
                    workers[i].start();
                    columnLengths[i]++;
                    board[columnLengths[i]][i] = 0;
                }
                else {
                    scores[i] = Integer.MAX_VALUE;
                }
            }
            timer = new Timer();
            timer.scheduleAtFixedRate(new TimerTask(){
                @Override
                public void run(){
                    if (workersBack.size() == numOfWorkers){
                        workersBack.clear();
                        System.out.println("lksdjf;a");
                        if (GameMethods.decideGameState(board) == -1){
                            System.out.println("jumbo");
                            playBestMove();
                            count++;
                            game();
                        }
                        else {
                            print(board);
                            System.out.println(GameMethods.forwardSlashSearch(2, board));
                            System.out.println(GameMethods.backSlashSearch(2, board));
                            System.out.println("done ");
                        }
                        cancel();

                    }
                    for (int i=0;i<numOfWorkers;i++){
                        if (!workers[i].isAlive() && !workersBack.contains(workers[i])){
                            workersBack.add(workers[i]);
                            scores[i] = workers[i].getValue();

                        }
                    }
                }
            },0,500);


        }


    }
    public static void playBestMove(){
        int bestMove;
        double[][] arr = new double[7][2];
        for (int i=0;i<scores.length;i++){
            arr[i][0]= i;
            if (columnLengths[i]>=0){
                arr[i][1] = scores[i];
            }
            else {
                arr[i][1] = Integer.MAX_VALUE;
            }
        }
        System.out.println(arr);
        //System.out.println("selection");
        //System.out.println(Heuristics.selectionSort(arr));
        bestMove = (int)Heuristics.orderByCenter(reverse(Heuristics.selectionSort(arr)))[0][0];
        System.out.println("best move: "+bestMove );
        System.out.println("cols");
        print(columnLengths);
        System.out.println();
        board[columnLengths[bestMove]][bestMove] = 2;
        columnLengths[bestMove]--;
        print(board);
        print(scores);
        System.out.println();
    }
    public static void print(int[][] arr){
        for (int i=0;i<arr.length;i++){
            for (int j=0;j<arr[i].length;j++){
                System.out.print(arr[i][j] + " ");
            }
            System.out.println();
        }
    }
    public static void print(double[][] arr){
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
    public static void print(double[] arr){
        for (double i: arr){
            System.out.print(i+" ");
        }
        System.out.println();
    }

    public static double[][] reverse(double[][] arr)
    {
        double[][] newArr = new double[arr.length][arr[0].length];
        int count = 0;
        for (int i = arr.length - 1; i >= 0; i--) {

            newArr[count] = Heuristics.copy(arr[i]);
            count++;
        }
        return newArr;
    }

    // Iterate through all the elements and print
    public void printElements(ArrayList<Integer> alist)
    {
        for (int i = 0; i < alist.size(); i++) {
            System.out.print(alist.get(i) + " ");
        }
    }

    public static Worker[] getWorkers() {
        return workers;
    }
}
