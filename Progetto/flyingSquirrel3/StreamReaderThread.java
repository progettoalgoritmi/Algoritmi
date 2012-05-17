package flyingSquirrel3;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

public class StreamReaderThread implements Runnable {

	private BufferedReader in = null;
	private char[] buffer;
	private boolean end;
	private Lock lock;
	private int original_buffer_size;
	private int counter;
	private boolean timeout;
	private Stack<XMLNode> stack;
	private Stack<XMLNode> toBeProcessed;

	public StreamReaderThread(BufferedReader in, char[] buffer, Lock lock) {
		this.in = in;
		this.buffer = buffer;
		this.end = false;
		this.lock = lock;
		this.original_buffer_size = buffer.length;
		this.counter = 0;
		this.timeout = false;
		this.stack = new Stack<XMLNode>();
		this.toBeProcessed = new Stack<XMLNode>();
	}
	
	public int getCounter() {
		return counter;
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
	
	protected void clearBuffer() {
		buffer = new char[original_buffer_size];
		counter = 0;
	}
	
	public void safeClearBuffer(int begin, int end) {
		while(lock.getLockStatus());
		lock.lock();
		char[] temp = new char[buffer.length - (end - begin)];
		System.arraycopy(buffer, 0, temp, 0, begin);
		System.arraycopy(buffer, end + 1, temp, begin, temp.length - begin - 1);
		buffer = temp;
		counter = temp.length - 1;
		lock.unlock();
	}
	
	public int getBufferSize() {
		return buffer.length;
	}
	
	public boolean getTimeout() {
		return timeout;
	}
	
	public Stack<XMLNode> getNotProcessedEvents() {
		return toBeProcessed;
	}
	
	public Stack<XMLNode> getIncompleteEvents() {
		return stack;
	}
	
	@Override
	public void run() {
		boolean newLine = true;
		while(true) {
			char buff;
			try {
				while(lock.getLockStatus());
				lock.lock();
				buff = (char) in.read();
				timeout = false;
				lock.unlock();
				if (counter >= buffer.length) {
					char[] temp = new char[buffer.length*2];
					System.arraycopy(buffer, 0, temp, 0, buffer.length);
					buffer = temp;
				}
				buffer[counter] = buff;
				counter++;
				if (buff == 65535) break;
				else if (buff != '>') {
					if (newLine) {
						//System.out.print("Server: ");
						newLine = false;
					}
					//System.out.print(buff);
				}
				else {
					//System.out.println(buff);
					while(lock.getLockStatus());
					lock.lock();
					Parser p = new Parser(buffer, stack, toBeProcessed);
					p.parse();
					lock.unlock();
					if (TLSHandler.gotTLS(toBeProcessed, lock)) break;
					clearBuffer();
					newLine = true;
				}
			} catch (SocketTimeoutException e) {
				lock.unlock();
				timeout = true;
				continue; } 
			catch (InterruptedException e) {} 
			catch (IOException e) {}
		}
	}
}
