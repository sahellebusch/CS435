import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.concurrent.locks.ReentrantLock;


public class Cartographer implements Runnable{
	private ReentrantLock downLock;
	private ReentrantLock upLock;
	private ReentrantLock leftLock;
	private ReentrantLock rightLock;
	private boolean madeChange = false;

	int regions[][];
	ExplicitElement[][] labels;
	int row[];

	public Cartographer(int[][] regions, ExplicitElement[][] labels, int[] row){
		this.regions = regions;
		this.labels	 = labels;
		this.row     = row;
	}

	public void run() {		
//		for(int i = 0; i < 4; i++) {
//			for(int k = 0; k < 5; k++ )
//				labels[i][k].printElement();
//		}
		for(int i : row) {
			
		}
		
	}


}
