import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

/**
 * @author Sean
 *
 */
public class HellebuschCMT {

	public static void main(String[] args) {
		final int NUM_PERSONS = 3;
		int[][] persons = new int[NUM_PERSONS][];
		
		// check for correct number of arguments.
		if(args.length < 1) {
	        System.out.println("Error: missing argument.");
	        System.exit(1);
	    }
		
		initializePersonsArrays(args[0], NUM_PERSONS, persons);
		
		//create arrays to hold frames ad threads
		Searcher[] searcher = new Searcher[NUM_PERSONS];
		Thread[] thread = new Thread[NUM_PERSONS];
		
		// create objects that will run as threads
		for(int i = 0; i < NUM_PERSONS; i++) {
			searcher[i] = new Searcher();
		}
		
		//set searcher names and iterations
		for(int i = 0; i < NUM_PERSONS; i++) {
			searcher[i].setName("Searcher" + i);
			searcher[i].setNumIterations(persons[i].length);
		}
		
		//create the threads
		for(int i = 0; i < NUM_PERSONS; i++) {
			thread[i] = new Thread(searcher[i]);
		}
		
		for(int i = 0; i < NUM_PERSONS; i++) {
			System.out.println("Starting thread " + searcher[i].getName());
			thread[i].start();
		}
		
		for(int i = 0; i < NUM_PERSONS; i++) {
			try {
				thread[i].join();
			} catch(InterruptedException e) {
				System.out.println("Thread " + searcher[i].getName() + "failed to join.");
			}
		}
		
	}

	/**
	 * Function to initialize the persons arrays.
	 * 
	 * @param arg the text file provided by the user
	 * @param NUM_PERSONS number of persons with meeting times
	 * @param persons 2D array to contain individual persons times
	 */
	private static void initializePersonsArrays(String arg,
			final int NUM_PERSONS, int[][] persons) {
		String[] temp_s;
		try {
			Scanner scanner = new Scanner(new FileInputStream(arg));
			for(int k = 0; k < NUM_PERSONS; k++) {
				temp_s = scanner.nextLine().split(" ");
				persons[k] = new int[temp_s.length-1];
				for(int i = 1; i < temp_s.length; i++) {
					persons[k][i-1] = Integer.parseInt(temp_s[i]);
				}
			}
			
			scanner.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
