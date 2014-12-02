/**
 * @author Sean
 *
 * Program determines common meetings times among
 * three people using concurrency.  The times are
 * predefined in a text file.
 *  */

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class HellebuschCMT {

	public static void main(String[] args) {
		final int NUM_PERSONS = 3;
		int[][] persons = new int[NUM_PERSONS][];
		int numThreads;
		
		// check for correct number of arguments.
		if(args.length < 1) {
	        System.out.println("Error: missing argument.");
	        System.exit(1);
	    }
		
		initializePersonsArrays(args[0], NUM_PERSONS, persons);
		numThreads = persons[0].length;
		
		//create arrays to hold frames ad threads
		Searcher[] searcher = new Searcher[numThreads];
		Thread[] thread = new Thread[numThreads];
		
		// create objects that will run as threads
		for(int i = 0; i < numThreads; i++) {
			searcher[i] = new Searcher(persons[1], persons[2]);
		}

		//set searcher names and iterations
		for(int i = 0; i < numThreads; i++) {
			searcher[i].setValueToFind(persons[0][i]);
		}
		
		//create the threads
		for(int i = 0; i < numThreads; i++) {
			thread[i] = new Thread(searcher[i]);
		}
		
		//start the threads
		for(int i = 0; i < numThreads; i++) {
			thread[i].start();
		}
		
		//join the threads
		for(int i = 0; i < numThreads; i++) {
			try {
				thread[i].join();
			} catch(InterruptedException e) {
				System.out.println("Thread " + i + "failed to join.");
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
		String[] temp;
		try {
			Scanner scanner = new Scanner(new FileInputStream(arg));
			for(int k = 0; k < NUM_PERSONS; k++) {
				temp = scanner.nextLine().split(" ");
				persons[k] = new int[temp.length-1];
				for(int i = 1; i < temp.length; i++) {
					persons[k][i-1] = Integer.parseInt(temp[i]);
				}
			}
			
			scanner.close();
		} catch (FileNotFoundException e) {
			System.out.println("Error: File " + arg + " not found");
			e.printStackTrace();
		}
	}

}
