import java.util.concurrent.locks.ReentrantLock;


public class ExplicitElement {
	private int element;
	private ReentrantLock lock;
	
	public ExplicitElement(int i) {
		element = i;
		lock = new ReentrantLock();
	}
	
	public void printElement() {
		System.out.println(element);
	}
	
	public int get() {
		return element;
	}
	
	public void release() {
		lock.unlock();
	}
	
	public void lock() {
		lock.lock();
	}

	public void setAndRelease(int i) {
		element = i;
		lock.unlock();		
	}

	public void set(int i) {
		element = i;
	}
	
}
