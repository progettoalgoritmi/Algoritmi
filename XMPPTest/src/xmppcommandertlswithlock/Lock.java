package xmppcommandertlswithlock;

public class Lock {
	private static boolean locked = false;
	
	public static synchronized void unlock() {
		locked = false;
	}
	
	public static synchronized void lock() {
		locked = true;
	}
	
	public static synchronized boolean getLockStatus() {
		return locked;
	}
}
