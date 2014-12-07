import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.concurrent.locks.ReentrantLock;


public class Labeler implements Runnable{
	private  ReentrantLock n0Lock;
	private  ReentrantLock n1Lock;
	private  ReentrantLock n2Lock;
	private  ReentrantLock n3Lock;
	private  ReentrantLock n4Lock;
	
	int regions[][];
	int labels[][];
	int row[];

	public Labeler(int[][] regions, int[][] labels, int[] row){
		this.regions = regions;
		this.labels	 = labels;
		this.row     = row;
	}
	
	public Labeler() {
		// empty constructor
	}
	
	public void run() {
		for(int i = 0; i < regions.length; i++) {	
			System.out.println(Arrays.toString(regions[i]));
		}
		System.out.println();
		for(int i = 0; i < labels.length; i++) {	
			System.out.println(Arrays.toString(labels[i]));
		}
	}

	
}
