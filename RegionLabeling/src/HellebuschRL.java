import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Scanner;


public class HellebuschRL {

	public static void main(String[] args) {
		int rows = 0;
		int cols = 0;
		int regions[][] = null;
		int labels[][] = null;

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

		for(int i = 0; i < regions.length; i++) {	
			System.out.println(Arrays.toString(regions[i]));
		}
		System.out.println();
		for(int i = 0; i < labels.length; i++) {	
			System.out.println(Arrays.toString(labels[i]));
		}
	}


	/**
	 * @param rows number of rows
	 * @param cols number of columns
	 * @return init'd matrix of labels
	 */
	private static int[][] initLabelMatrix(int rows, int cols) {
		int temp[][] = new int[cols][rows];
		int val = 0;
		for(int i = 0; i < cols; i++) {
			for(int k = 0; k < rows; k++) {
				temp[i][k] = val;
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
