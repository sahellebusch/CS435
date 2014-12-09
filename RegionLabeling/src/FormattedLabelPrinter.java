public class FormattedLabelPrinter {
	ExplicitElement[][] labels;
	String line;
	int numCols;
	int numRows;
	String temp;
	int maxLength;

	public FormattedLabelPrinter(ExplicitElement[][] labels, int numCols, int numRows) {
		this.labels  = labels;
		this.numRows = numRows;
		this.numCols = numCols;
	} 

	public void print() {
		maxLength = getMaxIntLength();
		for(ExplicitElement[] row: labels)
			writeLine(row);
	}
	
	private void writeLine(ExplicitElement[] row) {
		String line = "";
		String value;
		int spaces;
		for(ExplicitElement e: row) {
			value = String.valueOf(e.get());
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
				temp = String.valueOf(labels[i][k].get());
				if(temp.length() > max)
					max = temp.length();
			}
		}
		return max;
	}
	
}
