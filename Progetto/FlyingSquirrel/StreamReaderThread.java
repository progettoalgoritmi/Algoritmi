package FlyingSquirrel;

import java.io.BufferedReader;
import java.io.IOException;

public class StreamReaderThread implements Runnable {

	private BufferedReader in = null;
	private char[] buffer = null;
	private int bufsize;
	private boolean end;
	private String lastChallenge;
	private boolean lastChallengeUsed = false;
	
	public StreamReaderThread(BufferedReader in) {
		this.in = in;
		this.buffer = new char[10000];
		this.bufsize = 10000;
		this.end = false;
		this.lastChallenge = null;
		this.lastChallengeUsed = false;
	}
	
	public StreamReaderThread(BufferedReader in, int size) {
		this.in = in;
		this.buffer = new char[size];
		this.bufsize = size;
		this.end = false;
		this.lastChallenge = null;
		this.lastChallengeUsed = false;
	}
	
	
	public boolean isEnded() {
		return this.end;
	}
	
	public char[] getBuffer() {
		return this.buffer;
	}
	
	public void forceEnd() {
		this.end = true;
	}
	
	private void takeChallengeOrTLS() {
		if ((String.valueOf(buffer)).indexOf("</challenge>") != -1) {
			StringBuilder sb = new StringBuilder();
			int j = String.valueOf(buffer).indexOf("challenge");
			while (buffer[j] != '>') j++;
			j++;
			while (buffer[j] != '<') {
				sb.append(buffer[j]);
				j++;
			}
			String challenge = new String(sb.toString());
			lastChallenge = challenge;
			lastChallengeUsed = false;
		}
		if (((String.valueOf(buffer)).indexOf("proceed") != -1) &&
				((String.valueOf(buffer)).indexOf("urn:ietf:params:xml:ns:xmpp-tls") != -1)) {
			this.end = true;
		}
		if ((String.valueOf(buffer)).indexOf("/stream:stream") != -1) this.end = true; //TODO: buffer is cyclic, fix this
		if (lastChallengeUsed) lastChallenge = null;
	}
	
	public void clearBuffer() {
		for (int i = 0; i < this.bufsize; i++) buffer[i] = '\0';
	}
	
	public String getChallenge() {
		return this.lastChallenge;
	}
	
	public boolean isLastChallengeUsed() {
		return lastChallengeUsed;
	}
	
	public void setLastChallengeUsed() {
		this.lastChallengeUsed = true;
	}
	
	@Override
	public void run() {
		boolean newLine = true;
		int i = 0;
		while(!end) {
			char buff;
			try {
				while(Lock.getLockStatus());
				Lock.lock();
				Lock.hanged(true);
				buff = (char) in.read();
				Lock.hanged(false);
				if (buff == 65535) end = true;
				else if (buff != '>') {
					if (newLine) {
						System.out.print("Server: ");
						newLine = false;
					}
					buffer[i] = buff;
					i = (i + 1)%bufsize;
					System.out.print(buff);
				}
				else {
					buffer[i] = buff;
					i = (i + 1)%bufsize;
					System.out.println(buff);
					takeChallengeOrTLS();
					newLine = true;
				}
				Lock.unlock();
			} catch (IOException e) {}
		}
	}
}