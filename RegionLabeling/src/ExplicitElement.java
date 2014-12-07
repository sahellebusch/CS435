import java.util.concurrent.locks.ReentrantLock;


public class ExplicitElement {
	private int element;
	private ReentrantLock lock;
	
	public ExplicitElement(int i) {
		element = i;
		lock = new ReentrantLock();
	}
	
	public int getAndLock() {
		lock.lock();
		return element;
	}
	
	public void setAndRelease(int i) {
		element = i;
		lock.unlock();
	}
	
	public void printElement() {
		System.out.println(element);
	}
	
}
