package FlyingSquirrel_imp;

public class Lock {
	private static boolean softLock = false;
	private static boolean hardLock = false;
	private static boolean terminate = false;
	
	public static synchronized void softUnlock() {
		softLock = false;
	}
	
	public static synchronized void softLock() {
		softLock = true;
	}
	
	public static synchronized void hardUnlock() {
		hardLock = false;
	}
	
	public static synchronized void hardLock() {
		hardLock = true;
	}
	
	public static synchronized void terminate() {
		terminate = true;
	}
	
	public static synchronized void unTerminate() {
		terminate = false;
	}
	
	public static synchronized boolean getSoftLockStatus() {
		return softLock;
	}
	
	public static synchronized boolean getHardLockStatus() {
		return hardLock;
	}
	
	public static synchronized boolean getTerminationSignal() {
		return terminate;
	}
	
	public static synchronized void waitOnLocks() throws InterruptedException {
		while(Lock.getSoftLockStatus()) {
			if (Lock.getSoftLockStatus()) Thread.sleep(100);
			if (Lock.getSoftLockStatus()) Lock.softUnlock();
		}
		while(Lock.getHardLockStatus());
	}
}
