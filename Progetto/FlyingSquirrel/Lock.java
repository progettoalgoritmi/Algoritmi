package FlyingSquirrel;

public class Lock {
	private static boolean locked = false;
	private static boolean hanged = false;
	
	public static synchronized void unlock() {
		locked = false;
	}
	
	public static synchronized void lock() {
		locked = true;
	}
	
	public static synchronized void hanged(boolean hanged) {
		Lock.hanged = hanged;
	}
	
	public static synchronized boolean getLockStatus() {
		return locked;
	}
	
	public static synchronized boolean isHanged() {
		return hanged;
	}
}
