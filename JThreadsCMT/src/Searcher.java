public class Searcher implements Runnable {
	private int numIterations;
	private int value;


	public Searcher() {
		// empty constructor
	}

	public void run() {		
		printValue();
		System.out.println("searcher looking for " + value + " is exiting.");
	}
	
	public void printValue() {
		System.out.println(value);
	}
	
	public void setValueToFind(int i) {
		value = i;
	}

}
