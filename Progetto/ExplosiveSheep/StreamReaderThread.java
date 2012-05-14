package ExplosiveSheep;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class StreamReaderThread extends Thread {

	private BufferedReader in = null;
	private ArrayList<Character> buffer = null;
	private int hash;
	private Window console;
	private boolean end;
	private Lock lock;

	public StreamReaderThread(BufferedReader in, ArrayList<Character> buffer,int hashCode,Window console,Lock lock) {
		this.in = in;
		this.buffer = buffer;
		this.end = false;
		hash=hashCode;
		this.console=console;
		this.lock=lock;
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
		try {
			lock.waitOnLocks(100);
		} catch (InterruptedException e) {}
		lock.hardLock();
		int i;
		for(i=0;(i<buffer.size())&&(!buffer.get(i).equals('>'));i++);
		if((i<buffer.size())&&(buffer.get(i).equals('>'))){
			List<Character> buffTemp= buffer.subList(i, buffer.size());
			buffer.clear();
			buffer.addAll(buffTemp);
		}
		lock.hardUnlock();
	}
	
	@Override
	public void run() {
		boolean newLine = true;
		while(!lock.getTerminationSignal()) {
			char buff;
			try {
				while(lock.getSoftLockStatus());
				lock.softLock();
				while(lock.getHardLockStatus());
				buff = (char) in.read();
				lock.softUnlock();
				buffer.add(buff);
				if (buff == 65535) break;
				else if (buff != '>') {
					if (newLine) {
						console.print("Client "+hash+": ");
						newLine = false;
					}
					console.print(buff);
				}
				else {
					console.println(buff);
					//if (TLSHandler.gotTLS(buffer)) break;
					newLine = true;
				}
			} catch (IOException e) {} 
			catch (Exception e) {}
		}
	}
}
