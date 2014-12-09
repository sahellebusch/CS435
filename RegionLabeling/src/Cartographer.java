import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author Sean
 * 
 * Thread to take a row from
 *
 */
public class Cartographer implements Runnable{
	private enum neighbor { LEFT, RIGHT, UP, DOWN }
	private ExplicitElement currentLabel;
	private ExplicitElement potentialLabel;
	private ExplicitElement[][] labels;
	private AtomicBoolean madeChange;
	private AtomicBoolean done;
	private CyclicBarrier barrier;
	private int numRows;
	private int numCols;
	private int currRegion;
	private int rowNum;
	private int regions[][];
	


	public Cartographer(int[][] regions, ExplicitElement[][] labels, int rowNum, 
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
		currentLabel = labels[j][i];
		currRegion   = regions[j][i];
		
		switch(neighbor) {
		case LEFT:
			// neighbor to our left?
			if(!((i - 1) < 0)) {
				if(currRegion == regions[j][i-1]) {
					potentialLabel = labels[j][i-1];
					potentialLabel.lock();
					setMax(potentialLabel, currentLabel);
					potentialLabel.release();
				}
			} 
		case UP:
			// neighbor above us?
			if(!((j - 1) < 0)) {
				if(currRegion == regions[j-1][i]) {
					potentialLabel = labels[j-1][i];
					potentialLabel.lock();
					setMax(potentialLabel, currentLabel);
					potentialLabel.release();
				}
			} 
			break;
		case RIGHT:
			// neighbor to our right?
			if((i + 1) < (numCols)) {
				if(currRegion == regions[j][i+1]) {
					potentialLabel = labels[j][i+1];
					potentialLabel.lock();
					setMax(potentialLabel, currentLabel);
					potentialLabel.release();
				}
			} 
			break;
		case DOWN:
			// neighbor below us?
			if((j + 1) < (numRows)) {
				if(currRegion == regions[j+1][i]) {
					potentialLabel = labels[j+1][i];
					potentialLabel.lock();
					setMax(potentialLabel, currentLabel);
					potentialLabel.release();
				}
			}
			break;
		}
	}

	/**
	 * @param potentialLabel the potential label who's value we may take
	 * @param currLabel the current label
	 * @param changeMade tell us a thread made a change
	 */
	private void setMax(ExplicitElement potentialLabel,
			ExplicitElement currLabel) {
		if(currLabel.get() < potentialLabel.get()) {
			currLabel.set(potentialLabel.get());
			madeChange.set(true);
		}
	}
	
}
