/**
 * @author Sean
 * 
 * Class to print out a region labeled matrix
 */
public class FormattedLabelPrinter {
	private int[][] labels;
	private int numCols;
	private int numRows;
	private String temp;
	private int maxLength;

	public FormattedLabelPrinter(int[][] labels, int numCols, int numRows) {
		this.labels  = labels;
		this.numRows = numRows;
		this.numCols = numCols;
	} 

	/**
	 * The master print function of the class.  
	 * Prints the entire labels matrix.
	 */
	public void print() {
		maxLength = getMaxIntLength();
		for(int[] row: labels)
			writeLine(row);
	}
	
	/**
	 * Formats a line and prints it out
	 * 
	 * @param row the row to be printed
	 */
	private void writeLine(int[] row) {
		String line = "";
		String value;
		int spaces;
		for(int e: row) {
			value = String.valueOf(e);
			spaces = (maxLength + 1)- value.length();
			line = String.format("%-" + spaces + "s%s", " ", value);
			System.out.print(line);
		}
		System.out.println();
	}

	/**
	 * @return return the max integer length
	 */
	private int getMaxIntLength() {
		int max = 0;
		for(int i = 0; i < numCols; i++) {
			for(int k = 0; k < numRows; k++) {
				temp = String.valueOf(labels[i][k]);
				if(temp.length() > max)
					max = temp.length();
			}
		}
		return max;
	}
	
}
