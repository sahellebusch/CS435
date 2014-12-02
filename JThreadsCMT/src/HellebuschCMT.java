import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Scanner;


public class HellebuschCMT {

	public static void main(String[] args) throws FileNotFoundException {
		final int NUM_THREADS = 5;
		final int NUM_ITERATIONS = 10;
		
		System.out.println("Main start");
		
		//create arrays to hold frames ad threads
		Searcher[] searcher = new Searcher[NUM_THREADS];
		Thread[] thread = new Thread[NUM_THREADS];
		
		// create objects that will run as threads
		for(int i = 0; i < NUM_THREADS; i++) {
			searcher[i] = new Searcher();
		}
		
		//set searcher names and iterations
		for(int i = 0; i < NUM_THREADS; i++) {
			searcher[i].setName("T" + i);
			searcher[i].setNumIterations(NUM_ITERATIONS);
		}
		
		//create the threads
		for(int i = 0; i < NUM_THREADS; i++) {
			thread[i] = new Thread(searcher[i]);
		}
		
		for(int i = 0; i < NUM_THREADS; i++) {
			System.out.println("Starting thread " + searcher[i].getName());
			thread[i].start();
			
			System.out.println("Thread " + searcher[i].getName() + " started.");
		}
		
		for(int i = 0; i < NUM_THREADS; i++) {
			try {
				thread[i].join();
				System.out.println("Joining thread " + searcher[i].getName());
			} catch(InterruptedException e) {
				System.out.println("Thread " + searcher[i].getName() + "failed to join.");
			}
		}
		
		
		System.out.println("Main exiting.");
	}

}
