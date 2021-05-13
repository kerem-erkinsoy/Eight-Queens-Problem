package eightqueensproblem;

/**
 *
 * @author Kerem Erkinsoy
 */

public class Initializer {
    // The number indicating how many queens it will consist of
    private final static int NUM_SQUARE = 8; 
    // The number indicating how many times the problem will be solved
    private final static int NUM_PROCESS = 25; 
    
    public static void main(String[] args) {
        HillClimbing bc = new HillClimbing(NUM_PROCESS, NUM_SQUARE);
        bc.solveProblem();
    }
}

