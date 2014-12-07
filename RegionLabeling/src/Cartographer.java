import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.locks.ReentrantLock;

import javax.swing.text.ChangedCharSetException;


public class Cartographer implements Runnable{
	private ExplicitElement currentLabel;
	private ExplicitElement potentialLabel;
	boolean madeChange;
	private int numRows;
	private int numCols;
	private enum neighbor { LEFT, RIGHT, UP, DOWN }

	int currRegion  = -1;
	int regions[][];
	ExplicitElement[][] labels;
	int row[];
	int rowNum;
	private boolean done;
	private CyclicBarrier barrier;


	public Cartographer(int[][] regions, ExplicitElement[][] labels, int[] row, int rowNum, CyclicBarrier barrier, boolean done, boolean changesMade){
		this.madeChange = changesMade;
		this.regions = regions;
		this.labels	 = labels;
		this.row     = row;
		this.rowNum  = rowNum;
		this.done = done;
		this.barrier = barrier;
		numCols = regions[0].length;
		numRows = regions.length;
	}

	public void run() {		
		while(!done) {
			for(int i = 0; i < rowNum; i++) {
				setRegionLabel(neighbor.UP,    i, rowNum, madeChange);
				System.out.println("UP done");
				setRegionLabel(neighbor.RIGHT, i, rowNum, madeChange);
				System.out.println("RIGHT done");
				setRegionLabel(neighbor.DOWN,  i, rowNum, madeChange);
				System.out.println("DOWN done");
				setRegionLabel(neighbor.LEFT,  i, rowNum, madeChange);
				System.out.println("LEFT done");
			}

			try {
				System.out.println("I'm at the barrier!");
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
	 * @param neighbor enum type to see who we should check
	 * @param i col rum
	 * @param j row num
	 * @param changeMade tells us if a thread made a change
	 */
	private void setRegionLabel(neighbor neighbor, int i, int j, boolean changeMade) {
		currentLabel = labels[j][i];
		currRegion   = regions[j][i];
		switch(neighbor) {
		case LEFT:
			// neighbor to our left?
			if(!((i - 1) < 0)) {
				if(currRegion == regions[j][i-1]) {
					potentialLabel = labels[j][i-1];
					potentialLabel.lock();
					setMax(potentialLabel, currentLabel, changeMade);
					potentialLabel.release();
				}
			} 
		case UP:
			// neighbor above us?
			if(!((j - 1) < 0)) {
				if(currRegion == regions[j-1][i]) {
					potentialLabel = labels[j-1][i];
					potentialLabel.lock();
					setMax(potentialLabel, currentLabel, changeMade);
					potentialLabel.release();
				}
			} 
			break;
		case RIGHT:
			// neighbor to our right?
			if((i - 1) < (numRows - 1)) {
				if(currRegion == regions[j][i+1]) {
					potentialLabel = labels[j][i+1];
					potentialLabel.lock();
					setMax(potentialLabel, currentLabel, changeMade);
					potentialLabel.release();
				}
			} 
			break;
		case DOWN:
			// neighbor below us?
			if((j + 1) < (numCols - 1)) {
				if(currRegion == regions[j+1][i]) {
					potentialLabel = labels[j+1][i];
					potentialLabel.lock();
					setMax(potentialLabel, currentLabel, changeMade);
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
			ExplicitElement currLabel, boolean changeMade) {
		if(currLabel.get() < potentialLabel.get()) {
			currLabel.set(potentialLabel.get());
			changeMade = true;
		}
	}

}
