public class Searcher implements Runnable {
	private int numIterations;
	private String name;


	public Searcher() {
		// empty constructor
	}

	public void run() {
		System.out.println(name + " run() has been called.");
		for(int i = 0; i < numIterations; i++) {
			System.out.println(name + " is on iteration " + i);
		}

		System.out.println(name + " is exiting.");
	}

	public int getNumIterations() {
		return numIterations;
	}

	public void setNumIterations(int numIterations) {
		this.numIterations = numIterations;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
