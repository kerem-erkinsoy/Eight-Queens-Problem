package eightqueensproblem;

/**
 *
 * @author Kerem Erkinsoy
 */

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class HillClimbing {
    
    private double [] totalProcessTime ;
    private int [] totalRandomRestart ;
    private int [] totalDeltaX;
    private int[] chessBoard;
    private long startTime;
    private int iteration;
    private int randomRestart;
    private int processLength;
    private int squareNum;  
    private static int delta = 0;
    private int allDeltaX = 0; 
    private int allRR = 0;
    private double allProcessTime = 0;
    
    
    
    public HillClimbing(int processLength, int squareNum){
        this.squareNum = squareNum;
        this.processLength = processLength;
        totalProcessTime = new double [processLength];
        totalRandomRestart = new int [processLength];
        totalDeltaX = new int [processLength];
    }
    /**
     * 
     * This method is the main method required to solve the problem
     * 
     */
    public void solveProblem(){
        for (int i = 0; i < processLength; i++) {
            randomRestart = 0;
            startTime = System.nanoTime();
            
            while (true) {
                iteration=0;
                delta=0;
                chessBoard = new int[squareNum];
                generateRandomBoard(chessBoard);
                printBoard(chessBoard);
                climbAlgorithm();
                
                if(boardScore(chessBoard)==0){
                    calculateResults(i);
                    break;
                }
                randomRestart++;
                allRR+=randomRestart;
                
                System.out.println("Random restart atılıyor...");
                System.out.println("Random Restart Sayısı: "+randomRestart);
            }
        }
        printFinalResult();
    }
    
    /**
     * This method is designed to create a random board.
     * @param chessBoard 
     */
    
    private  void generateRandomBoard(int[] chessBoard) {
        Random random = new Random();
        for (int j = 0; j < squareNum; j++) {
            int rand = random.nextInt(squareNum);
            // A loop to prevent it from being in the same column
            while(j>0 && chessBoard[j-1]==rand) rand=random.nextInt(8);
            chessBoard[j] = rand;
        }
    }
    
    /**
     * This method was written to print the 
     * final version of the board onto the console.
     * @param board 
     */
    private  void printBoard(int[] board){
        System.out.println("******************");
        for (int i = 0; i < squareNum; i++) {
            for (int j = 0; j < squareNum; j++) {
                if (j  != board[i]) System.out.print(" | ");
                else System.out.print(" Q ");
            }
            System.out.println("");
        }
        System.out.println("******************");
    }
    
    /**
     * This algorithm is based on the pseudocode which is given at added link
     * 
     * @link https://en.wikipedia.org/wiki/Hill_climbing
     * 
     */
    private void climbAlgorithm() {
        while (true) {                    
            System.out.println(iteration + ". iteration: ");
            List<int[]> successors = generateSuccesors(chessBoard);
            int[] bestSuccessor = selectBestNode(successors);
            if (boardScore(bestSuccessor) >= boardScore(chessBoard)) {
                break;
            }
            changeSuccessorToBoard(bestSuccessor, chessBoard);
            printBoard(chessBoard);
            System.out.println("h value: "+boardScore(bestSuccessor));
            iteration++;
        }
    }
    
    
    /**
     * Calculation of the moves that all queens can make in the columns where they are located.
     * @param arr (board)
     * @return successors
     */
    private List<int[]> generateSuccesors(int [] arr){
       List<int[]> successors = new ArrayList<int[]>();
        for (int i = 0; i < squareNum; i++) {
            for (int j = 0; j < squareNum; j++) {
                if(arr[i]!=j){
                    int[] state = generateCloneArray(arr);
                    state[i] = j;
                    successors.add(state);
               }
           }
       }
       return successors;
    }
    
    /**
     * 
     * @param array
     * @return the clone of the array that comes as a parameter.
     */
    
    private  int [] generateCloneArray(int [] arr){
        int [] clone = new int[squareNum];
        System.arraycopy(arr, 0, clone, 0, squareNum);
        return clone;
    } 
    
    
    /**
     * 
     * @param successors
     * @return the node with the least number of attacks.
     */
    
    private  int [] selectBestNode(List<int[]> successors){
        int [] newSuccessor = successors.get(0);
        int min = boardScore(newSuccessor);
        int bestSucccessorsScore=0;
        for (int i = 0; i < successors.size(); i++) {
            int successorsScore = boardScore(successors.get(i));
            if(successorsScore < min){
                min = successorsScore;
                bestSucccessorsScore = i;
            }
        }
        return successors.get(bestSucccessorsScore);
    }
    
    /***
     * 
     * @param state The state for which the number of attacks will be calculated.
     * @return number of attacks on the board
     */
    private  int boardScore(int [] state){
        int score = 0;
        for (int i = 0; i < state.length-1; i++) {
            for (int j = i+1; j < state.length; j++) {
                 if(state[i] == state[j]) score++;
                 else if(i + state[i] == j + state[j]) score++;
                 else if(i - state[i] == j - state[j]) score++;
            }
        }
        return score;
    }
    /**
     * The board layout is updated and the total delta x is calculated at this time. 
     * @param successor
     * @param chessBoard 
     */
    
    private  void changeSuccessorToBoard(int[] successor,int[] chessBoard){
        for (int i=0; i < squareNum; i++){
            delta +=  Math.abs(successor[i]-chessBoard[i]);
        }
        System.arraycopy(successor, 0, chessBoard, 0, squareNum);
    }
    
    
    /**
     * After the problem is solved, the results are calculated. 
     * @param index that represents what process it is in
     */
    
    private void calculateResults(int index) {
        System.out.println("Toplam yerdeğiştirme miktarı: "+delta);
        System.out.println("Toplam random restart sayısı: "+randomRestart+ "\n");
        totalDeltaX[index]=delta;
        allDeltaX+=delta;
        totalRandomRestart[index]+=randomRestart;
        long endTime = System.nanoTime();
        double totalTime = TimeUnit.NANOSECONDS.toMillis(endTime-startTime);
        totalProcessTime[index] = totalTime;
        allProcessTime+=totalTime;
    }
    
    /**
     * Print all results and averages after 
     * the total number of processes has been completed.
     */
    private void printFinalResult() {
        System.out.println("\t\t\t" +"Random Restart: " + "\t\t\t" + "Total Delta in a process: " + "\t\t" + "Process Times(milisecond): ");
        for (int i = 0; i < processLength; i++) {
            System.out.println(i + 1 + "\t\t\t" + totalRandomRestart[i] + "\t\t\t\t\t" + totalDeltaX[i] + "\t\t\t\t\t" + totalProcessTime[i]);
        }
        System.out.println("**************************************");
        System.out.println("Average:" + "\t" + allDeltaX / processLength + "\t\t\t\t\t" + allRR / processLength + "\t\t\t\t" + allProcessTime / processLength);
    }
    

    

    
}
    

