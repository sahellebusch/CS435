import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author Sean
 * 
 * Class that iterates and sets the labels for a given 
 * row of a regions matrix
 *
 */
public class Cartographer implements Runnable{
	private enum neighbor { LEFT, RIGHT, UP, DOWN }
	private int[][] labels;
	private AtomicBoolean madeChange;
	private AtomicBoolean done;
	private CyclicBarrier barrier;
	private int numRows;
	private int numCols;
	private int rowNum;
	private int regions[][];


	public Cartographer(int[][] regions, int[][] labels, int rowNum, 
			CyclicBarrier barrier, AtomicBoolean done, AtomicBoolean changesMade){

		this.madeChange = changesMade;
		this.regions    = regions;
		this.labels	    = labels;
		this.rowNum     = rowNum;
		this.done       = done;
		this.barrier    = barrier;
		numCols 		= regions[0].length;
		numRows 		= regions.length;
	}

	public void run() {		
		while(!done.get()) {

			changeLabels();

			try {
				barrier.await();
			} catch (InterruptedException e) {
				System.out.println("Error: Threads interrupted.");
				e.printStackTrace();
			} catch (BrokenBarrierException e) {
				System.out.println("Error: Barrier broken.");
				e.printStackTrace();
			}
		}
	}

	/**
	 * Check each neighbor and make necessary changes.
	 */
	private void changeLabels() {
		for(int i = 0; i < numCols; i++) {
			setRegionLabel(neighbor.UP,    i, rowNum);
			setRegionLabel(neighbor.RIGHT, i, rowNum);
			setRegionLabel(neighbor.DOWN,  i, rowNum);
			setRegionLabel(neighbor.LEFT,  i, rowNum);
		}
	}

	/**
	 * @param neighbor enum type to see which neighbor to should check
	 * @param i row number
	 * @param j position in the row
	 * @param madeChange tells us if a thread made a change
	 */
	private void setRegionLabel(neighbor neighbor, int i, int j) {
		int currRegion   = regions[j][i];

		switch(neighbor) {
		case LEFT:
			// neighbor to our left?
			if(!((i - 1) < 0)) {
				if(currRegion == regions[j][i-1]) {
					labels[j][i] = getMax(labels[j][i], labels[j][i-1]);
				}
			} 
		case UP:
			// neighbor above us?
			if(!((j - 1) < 0)) {
				if(currRegion == regions[j-1][i]) {
					labels[j][i] = getMax(labels[j][i], labels[j-1][i]);
				}
			} 
			break;
		case RIGHT:
			// neighbor to our right?
			if((i + 1) < (numCols)) {
				if(currRegion == regions[j][i+1]) {
					labels[j][i] = getMax(labels[j][i], labels[j][i+1]);
				}
			} 
			break;
		case DOWN:
			// neighbor below us?
			if((j + 1) < (numRows)) {
				if(currRegion == regions[j+1][i]) {
					labels[j][i] = getMax(labels[j][i], labels[j+1][i]);
				}
			}
			break;
		}
	}

	/**
	 * Function to determine the larger of two labels
	 * 
	 * @param current the current value of the label
	 * @param potential the potential value of the label
	 * @return the larger of the two labels
	 */
	private int getMax(int current, int potential) {
		if(current < potential) {
			current = potential;
			madeChange.set(true);
		}

		return current;
	}

}
