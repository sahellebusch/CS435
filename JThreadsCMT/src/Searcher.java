/**
 * @author Sean Hellebusch | sahellebusch@gmail.com
 * 
 * Thread to search for values of person1 and determine
 * if it is a common meeting time.  
 */
public class Searcher implements Runnable {
	private int value;
	private int[] person1Times;
	private int[] person2Times;
	private boolean found;


	/**
	 * @param times1 array of times for person 1
	 * @param times2 array of times for person 2
	 */
	public Searcher(int[] times1, int[] times2) {
		person1Times = times1;
		person2Times = times2;
	}

	public void run() {		
		found = findCommonMeetingTimes();
		if(found)
			System.out.println(value + " is a common meeting time.");
	}
	
	/**
	 * @param value
	 */
	public void setValueToFind(int value) {
		this.value = value;
	}
	
	/**
	 * @return if common meeting time is found, returns true
	 * 
	 */
	private boolean findCommonMeetingTimes() {
		for(int p1: person1Times) {
			if(value == p1) {
				for(int p2: person2Times){
					if(value == p2){
						return true;
					}
				}
			}
		}
		return false;
	}

}