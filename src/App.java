
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

public class App {
    private static int[] columnLengths =  {5, 5, 5, 5, 5, 5, 5};
    private static int[] scores = {-5,-5,-5,-5,-5,-5,-5};
    private static Scanner sc = new Scanner(System.in);
    private static int[][] board = new int[6][7];
    static int[][] board2 = {
            {0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 2},
            {0, 0, 0, 0, 0, 0, 2},
            {0, 0, 0, 0, 0, 0, 2},
            {0, 0, 0, 0, 0, 0, 2}

    };
    private static int count = 1;
    private static int depth = 13;
    private static Worker[] workers = new Worker[7];
    private static ArrayList<Worker> workersBack = new ArrayList<>();
    private static Timer timer;
    public static void main(String[] args) throws Exception {

        game();
        System.out.println("sdjfnskdf");


    }
    public static void game(){
        System.out.println("turn: "+count);
        System.out.println("gameeee");
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
            for (int i=0;i<7;i++){
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
                    if (workersBack.size() == 7){
                        workersBack.clear();
                        System.out.println("lksdjf;a");
                        if (GameMethods.decideGameState(board) == -1){
                            System.out.println();
                            playBestMove();
                            count++;
                            game();
                        }
                        else {
                            System.out.println("done");
                        }
                        cancel();

                    }
                    for (int i=0;i<7;i++){
                        if (!workers[i].isAlive() && !workersBack.contains(workers[i])){
                            workersBack.add(workers[i]);
                            scores[i] = (int)workers[i].getValue();

                        }
                    }
                }
            },0,500);


        }


    }
    public static void playBestMove(){
        int bestScore = Integer.MAX_VALUE;
        int bestMove = -10;
        for (int i=0;i<scores.length;i++){
            if (columnLengths[i]>=0 && bestScore>scores[i]){
                bestScore = scores[i];
                bestMove = i;
            }
            else if (columnLengths[i]>0 && bestScore==scores[i]){
                bestScore = scores[i];
                bestMove = i;
            }
        }
        board[columnLengths[bestMove]][bestMove] = 2;
        columnLengths[bestMove]--;
        print(board);
        for (double i: scores){
            System.out.print(i + " ");
        }
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
    public static void print(int[] arr){
        for (int i: arr){
            System.out.print(i+" ");
        }
        System.out.println();
    }
}
