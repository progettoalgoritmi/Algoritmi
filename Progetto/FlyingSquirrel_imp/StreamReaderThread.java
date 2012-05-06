package FlyingSquirrel_imp;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;

public class StreamReaderThread implements Runnable {

	private BufferedReader in = null;
	private ArrayList<Character> buffer = null;
	private boolean end;

	public StreamReaderThread(BufferedReader in, ArrayList<Character> buffer) {
		this.in = in;
		this.buffer = buffer;
		this.end = false;
	}
	
	
	public boolean isEnded() {
		return this.end;
	}
	
	public ArrayList<Character> getBuffer() {
		return this.buffer;
	}
	
	public void forceEnd() {
		this.end = true;
	}
	
	public void clearBuffer() {
		buffer.clear();
	}
	
	@Override
	public void run() {
		boolean newLine = true;
		while(!Lock.getTerminationSignal()) {
			char buff;
			try {
				while(Lock.getSoftLockStatus());
				Lock.softLock();
				while(Lock.getHardLockStatus());
				buff = (char) in.read();
				Lock.softUnlock();
				buffer.add(buff);
				if (buff == 65535) break;
				else if (buff != '>') {
					if (newLine) {
						System.out.print("Server: ");
						newLine = false;
					}
					System.out.print(buff);
				}
				else {
					System.out.println(buff);
					if (TLSHandler.gotTLS(buffer)) break;
					newLine = true;
				}
			} catch (IOException e) {} 
			catch (InterruptedException e) {}
		}
	}
}
