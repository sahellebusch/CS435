import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.concurrent.CyclicBarrier;


public class HellebuschRL {



	public static void main(String[] args) {
		boolean changesMade = true;
		boolean done = false;
		int rows = 0;
		int cols = 0;
		int regions[][] = null;
		ExplicitElement labels[][] = null;
		FormattedLabelPrinter writer;

		if(args.length != 1) {
			System.out.println("Error: incorrect number of arguments.");
			System.out.println("Usage: java HellebuschRL <file>.");
			System.exit(1);
		}

		try {
			Scanner scanner = new Scanner(new FileInputStream(args[0]));
			cols = scanner.nextInt();
			rows = scanner.nextInt();
			regions = getRegionsMatrix(scanner, rows, cols);
			scanner.close();
		} catch (FileNotFoundException e) {
			System.out.println("Error: File " + args[0] + " could not be opened.");
			e.printStackTrace();
		}

		labels = initLabelMatrix(rows, cols);
		
		//init labelers
		Cartographer[] labeler = new Cartographer[cols];
		CyclicBarrier barrier = new CyclicBarrier(cols);
		
		for(int i = 0; i < cols; i++) {
			labeler[i] = new Cartographer(regions, labels, regions[i], i, barrier , done, changesMade);
		}

		//init threads
		Thread[] thread = new Thread[cols];
		for(int i = 0; i < cols; i++) {
			thread[i] = new Thread(labeler[i]);
		}

		for(Thread t : thread){
			t.start();
		}

		//join the threads
		for(int i = 0; i < cols; i++) {
			try {
				thread[i].join();
			} catch(InterruptedException e) {
				System.out.println("Thread " + i + "failed to join.");
			}
		}
		
		
		writer = new FormattedLabelPrinter(labels, cols, rows);
		writer.print();
		
	}	
	
	/**
	 * @param rows number of rows
	 * @param cols number of columns
	 * @return init'd matrix of labels
	 */
	private static ExplicitElement[][] initLabelMatrix(int rows, int cols) {
		ExplicitElement[][] temp = new ExplicitElement[cols][rows];
		int val = 0;
		for(int i = 0; i < cols; i++) {
			for(int k = 0; k < rows; k++) {
				temp[i][k] = new ExplicitElement(val);
				val++;
			}
		}
		return temp;
	}

	/**
	 * @param scanner scanner that has file open
	 * @param x width of the regions matrix
	 * @param y depth of the regions matrix
	 * @return the init'd regions matrix
	 */
	private static int[][] getRegionsMatrix(Scanner scanner, int x, int y) {
		int temp[][] = new int[y][x];
		String[] line;
		scanner.nextLine();
		for(int i = 0; i < y ; i++) {
			line = scanner.nextLine().split(" ");
			for(int k = 0; k < x; k++) {
				temp[i][k] = Integer.parseInt(line[k]);
			}
		}
		return temp;
	}
}
