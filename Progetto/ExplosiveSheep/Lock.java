package ExplosiveSheep;

public class Lock {
	private boolean softLock;
	private boolean hardLock; 
	private boolean terminate; 
	
	public Lock(){
	softLock = false;
	hardLock = false;
	terminate = false;
	}
	
	public synchronized void softUnlock() {
		softLock = false;
	}
	
	public synchronized void softLock() {
		softLock = true;
	}
	
	public synchronized void hardUnlock() {
		hardLock = false;
	}
	
	public synchronized void hardLock() {
		hardLock = true;
	}
	
	public synchronized void terminate() {
		terminate = true;
	}
	
	public synchronized void unTerminate() {
		terminate = false;
	}
	
	public synchronized boolean getSoftLockStatus() {
		return softLock;
	}
	
	public synchronized boolean getHardLockStatus() {
		return hardLock;
	}
	
	public synchronized boolean getTerminationSignal() {
		return terminate;
	}
	
	public synchronized void waitOnLocks(int timer) throws InterruptedException {
		while(getSoftLockStatus()) {
			if (getSoftLockStatus()) Thread.sleep(timer);
			if (getSoftLockStatus()) softUnlock();
		}
		while(getHardLockStatus());
	}
}
