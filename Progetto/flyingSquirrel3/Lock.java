package flyingSquirrel3;

public class Lock {
	private boolean lock;
	
	public Lock() {
		this.lock = false;
	}
	
	public void lock() {
		this.lock = true;
	}
	
	public void unlock() {
		this.lock = false;
	}
	
	public boolean getLockStatus() {
		return lock;
	}
}
