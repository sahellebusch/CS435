import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.concurrent.locks.ReentrantLock;


public class Cartographer implements Runnable{
	int downRegion  = -1;
	int upRegion    = -1;
	int leftRegion  = -1;
	int rightRegion = -1;
	int currRegion  = -1;
	int regions[][];
	ExplicitElement[][] labels;
	int row[];
	int rowNum;
	private ExplicitElement currentLabel;
	private ExplicitElement rightLabel;
	private ExplicitElement leftLabel;
	private ExplicitElement downLabel;
	private ExplicitElement upLabel;
	private boolean madeChange;
	private int numRows;
	private int numCols;
	
	public Cartographer(int[][] regions, ExplicitElement[][] labels, int[] row, int rowNum){
		this.regions = regions;
		this.labels	 = labels;
		this.row     = row;
		this.rowNum  = rowNum;
		numCols = regions[0].length;
		numRows = regions.length;
	}

	public void run() {		
//		for(int i = 0; i < 4; i++) {
//			for(int k = 0; k < 5; k++ )
//				labels[i][k].printElement();
//		}

		for(int colNum = 0; colNum < row.length; colNum++) {
			setLabels(colNum, rowNum);
		}
//		setLabels(1, rowNum);
		
		
	}

	private void setLabels(int i, int j) {
		// lock all the neighbors if they exist, get the region number
		madeChange = false;
		currentLabel = labels[j][i];
		currRegion = regions[j][i];
		System.out.println("currRegion = " + currRegion);
		// check if neighbor right
		if(!((i + 1) > (numCols - 1))) {
			rightLabel = labels[j][i+1];
			rightLabel.lock();
			System.out.println("I've got right lock");
//			rightLabel.release();
			rightRegion = regions[j][i+1];
			System.out.println("rightRegion = " + rightRegion);
		} else rightRegion = -1;
		// check if neighbor left
		if(!((i - 1) < 0)) {
			leftLabel = labels[j][i-1];
			leftLabel.lock();
			System.out.println("I've got the left lock");
//			leftLabel.release();
			leftRegion = regions[j][i-1];
		} else leftRegion = -1;
		// check if neighbor down
		if(!((j - 1) < 0)) {
			downLabel = labels[j-1][i];
			downLabel.lock();
			System.out.println("I've got the down lock");
//			downLabel.release();
			downRegion = regions[j-1][i];
		} else downRegion = -1;
		// check if neighbor up
		if(!((j + 1) > (numRows - 1))) {
			upLabel = labels[j+1][i];
			upLabel.lock();
			System.out.println("I've got the up lock");
//			upLabel.relsease();
			upRegion = regions[j+1][i];
		} else upRegion = -1;
		
		checkAndSetLabels();
	}

	private void checkAndSetLabels() {
		if(upRegion == currRegion) {
			if(upLabel.get() > currentLabel.get()) {
				currentLabel.set(upLabel.get());
				madeChange = true;
			} 
		} 
		upLabel.release();
		if(downRegion == currRegion) {
			if(downLabel.get() > currentLabel.get()) {
				currentLabel.set(downLabel.get());
				madeChange = true;
			} 
		} 
		downLabel.release();
		if(leftRegion == currRegion) {
			System.out.println("leftRegion == currRegion");
			if(leftLabel.get() > currentLabel.get()) {
				currentLabel.set(leftLabel.get());
				madeChange = true;
			}
		} 
		leftLabel.release();
		if(downRegion == currRegion) {
			System.out.println("downRegion == currRegion");
			if(downLabel.get() > currentLabel.get()) {
				currentLabel.set(downLabel.get());
				madeChange = true;
			}
		} 
		downLabel.release();
		
//		currentLabel.release();
	}

}
