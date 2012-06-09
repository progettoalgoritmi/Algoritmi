package flyingSquirrel3;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.EmptyStackException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Stack;

import javax.net.ssl.SSLSocket;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLStreamException;

import org.xml.sax.SAXException;

public class Connection {
	public static final int DIGESTMD5 = 0, PLAIN = 1;
	private Socket socket = null;
	private PrintWriter out = null;
	private StreamReaderThread in = null;
	private BufferedReader buffIn = null;
	private String hostname = null;
	private String username = null;
	private String resource = "/";
	private Integer port = null;
	private Thread reader = null;
	private Lock lock = null;
	private int timeout = 0;
	
	public Connection(Integer port, String hostname, char[] buffer, int timeout) throws XMLStreamException {
		this.lock = new Lock();
		this.timeout = timeout;
		try {
			this.port = port;
			this.hostname = hostname;
			socket = new Socket(hostname, port);
			socket.setSoTimeout(this.timeout);
			out = new PrintWriter(socket.getOutputStream());
			buffIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			in = new StreamReaderThread(buffIn, buffer, lock);
		} catch (UnknownHostException e) {
			System.err.println("Host not found: " + hostname + ".");
		} catch (IOException e) {
			System.err.println("Couldn't get I/O for " + hostname + ".");
		}
	}
	
	public void connect(int auth_type, boolean withTLS, String username, String password) 
			throws InterruptedException, NoSuchAlgorithmException, KeyManagementException, IOException, XMLStreamException {
		this.username = username;
		String xmlver = "<?xml version='1.0' encoding='UTF-8'?>";
		
		String handshake = "<stream:stream " +
				"to='" + hostname + "' " +
				"xmlns='jabber:client' " +
				"xmlns:stream='http://etherx.jabber.org/streams' " +
				"version='1.0'>";

		String auth = "<auth xmlns='urn:ietf:params:xml:ns:xmpp-sasl' mechanism='";
		reader = new Thread(in);
		reader.start();
		send(xmlver);
		send(handshake);
		String response = null;
		
		
		switch (auth_type) {
			case 0: 
					auth = auth + "DIGEST-MD5'/>";
					
					if (withTLS) {
						startTLS();
						send(handshake);
					} 
					
					send(auth);
					String lastChallenge = null;
					while ((lastChallenge = takeChallenge()) == null) Thread.sleep(100);
					String challenge = new String(Base64.decode(lastChallenge));
					
					response = calculateResponse(auth_type, challenge, username, password);
					
					send(response);
					
					while ((lastChallenge = takeChallenge()) == null) Thread.sleep(100);
					challenge = new String(Base64.decode(lastChallenge));
					System.out.println("Decoded challenge: " + challenge);
					
					String auth_final = "<response xmlns='urn:ietf:params:xml:ns:xmpp-sasl'/>";
					
					send(auth_final);
					
					break;
					
			case 1: if (withTLS) {
						startTLS();
						send(handshake);
					}
					response = calculateResponse(auth_type, null, username, password);
					send("<auth xmlns='urn:ietf:params:xml:ns:xmpp-sasl' mechanism='PLAIN'>" + 
							response + "</auth>");
					break;
			default: throw new IllegalArgumentException("Not supported or invalid argument.");
		}
		
		send(handshake);
		
		String bind_resource = "<iq type='set' id='bind_1'>" +
							"<bind xmlns='urn:ietf:params:xml:ns:xmpp-bind'/>" +
							"</iq>";
		
		send(bind_resource);
		
		while (resource.equals("/")) takeResource();
	}
	
	private String calculateResponse(int auth_type, String challenge, String username, String password) 
			throws NoSuchAlgorithmException, UnsupportedEncodingException {
		String response = null;
		switch (auth_type) {
		case 0:	response = MD5Handler.calculateResponse(auth_type, challenge, this, username, password);
				break;
		case 1: response = Base64.encodeString(username + "@" + hostname + "\u0000" 
												+ username + "\u0000" + password);
				break;
		default: throw new IllegalArgumentException("Not supported");
		}
		return response;
	}
	
	public void send(String command) throws InterruptedException {
		while(lock.getLockStatus());
		lock.lock();
		char[] c = command.toCharArray();
		for (int i = 0; i < c.length; i++) out.write(c[i]);
		out.flush();
		lock.unlock();
	}
	
	public void closeConnection() throws InterruptedException, IOException {
		send("</stream:stream>");
		reader.join();
		out.close();
		buffIn.close();
		socket.close();
	}
	
	public String getUsername() {
		return username;
	}
	
	public String getHostname() {
		return hostname;
	}
	
	public Integer getPort() {
		return port;
	}
	
	public char[] getBuffer() {
		return in.getBuffer();
	}
	
	private void clearBuffer() {
		in.clearBuffer();
	}
	
	public void safeClearBuffer(int begin, int end) {
		in.safeClearBuffer(begin, end);
	}
	
	public Socket getSocket() {
		return socket;
	}
	
	public String getResource() {
		return resource;
	}
	
	public Lock getLock() {
		return lock;
	}
	
	public boolean getTimeout() {
		return in.getTimeout();
	}
	
	public int getStreamBufferCounter() {
		return in.getCounter();
	}
	
	public Stack<XMLNode> getNotProcessedEvents() {
		return in.getNotProcessedEvents();
	}
	
	private void takeResource() throws InterruptedException {
		while(lock.getLockStatus());
		lock.lock();
		try {
			XMLNode node = getNotProcessedEvents().peek().getSubTags().peek().getSubTags().peek();
			if (node.getRoot().equals("jid")) {
				String content = node.getContent();
				this.resource = content.substring(content.indexOf('/'));
			}
		} catch (EmptyStackException e) {}
		lock.unlock();
	}
	
	private String takeChallenge() throws InterruptedException, IOException {
		while(lock.getLockStatus());
		lock.lock();
		Stack<XMLNode> toBeProcessed = getNotProcessedEvents();
		if (!toBeProcessed.isEmpty() && toBeProcessed.peek().getRoot().equals("challenge")) {
			String challenge = toBeProcessed.pop().getContent();
			lock.unlock();
			return challenge;
		}
		if (!toBeProcessed.isEmpty() && toBeProcessed.peek().getRoot().equals("failure")) {
			Stack<XMLNode> failure = toBeProcessed.pop().getSubTags();
			String cause = "";
			if (failure != null)
				if (failure.peek() != null) cause = failure.pop().getRoot();
			closeConnection();
			throw new RuntimeException("Authentication failure. Cause: " + (cause.equals("") ? "unknown." : cause + "."));
		}
		lock.unlock();
		return null;
	}
	
	private void startTLS() throws InterruptedException, KeyManagementException, NoSuchAlgorithmException, IOException, XMLStreamException {
		String starttls = "<starttls xmlns='urn:ietf:params:xml:ns:xmpp-tls'/>";
		send(starttls);
		while(!TLSHandler.gotTLS(getNotProcessedEvents(), lock));
		reader.join();
		while(lock.getLockStatus());
		lock.lock();
		TLSHandler handler = new TLSHandler();
		SSLSocket sslsock = handler.createTLS(this);
		sslsock.setSoTimeout(this.timeout);
		out = new PrintWriter(sslsock.getOutputStream());
		buffIn = new BufferedReader(new InputStreamReader(sslsock.getInputStream()));
		clearBuffer();
		char[] buffer = getBuffer();
		in = new StreamReaderThread(buffIn, buffer, lock);
		reader = new Thread(in);
		lock.unlock();
		reader.start();
	}
	
	public Stack<XMLNode> getIncompleteEvents() {
		return in.getIncompleteEvents();
	}
	
	public static void main(String[] args) throws KeyManagementException, NoSuchAlgorithmException, InterruptedException, IOException, ParserConfigurationException, SAXException, XMLStreamException {
 		Connection c = new Connection(5222, "localhost", new char[1], 1);
		c.connect(Connection.DIGESTMD5, true, "user", "user");
		Iq iq = new Iq(c);
		Presence p = new Presence(c);
		Message msg = new Message(c);
		/*iq.setRoster("antonio", c.getHostname(), "Antonio", "Self");
		LinkedList<String>[] llr = iq.rosterRequest();
		for (int i = 0; i < llr.length; i++) System.err.println(llr[i]);*/
		p.setStatus(Presence.DONOTDISTURB, "Trying to get things work.");
		LinkedList<XMLNode> roster = iq.rosterRequest();
		System.out.println(roster);
		while(true) {
			msg.getMessages();
			p.getPresences();
			if (p.presencesHasChanged()) System.out.println(p.getLastPresences());
			if (msg.getLastMessages().equals(new LinkedList<String>())) continue;
			else System.out.println(msg.getLastMessages().getFirst());
			Iterator<MessageBox> it = msg.getLastMessages().iterator();
			boolean exit = false;
			while (it.hasNext()) {
				if (it.next().getMessage().equals("exit")) exit = true;
				break;
			}
			if (exit) break;
			msg.clearLastMessages();
		}
		iq.getUserInfo("antonio");
		c.closeConnection();
		/* Implementare thread controlla presenze e ricezione trilli in MessageBox con booleana buzz. Fondere sendBuzz e sendMessage. */
	}
	
}