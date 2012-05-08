package FlyingSquirrel_imp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

import javax.net.ssl.SSLSocket;

import FlyingSquirrel.Base64;

public class Connection {
	public static final int DIGESTMD5 = 0, PLAIN = 1;
	private Socket socket = null;
	private PrintWriter out = null;
	private StreamReaderThread in = null;
	private BufferedReader buffIn = null;
	private String hostname = null;
	private String username = null;
	private Integer port = null;
	private ArrayList<Character> buffer = null;
	private Thread reader = null;
	
	public Connection(Integer port, String hostname, ArrayList<Character> buffer) {
		try {
			this.port = port;
			this.hostname = hostname;
			this.buffer = buffer;
			socket = new Socket(hostname, port);
			out = new PrintWriter(socket.getOutputStream());
			buffIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			in = new StreamReaderThread(buffIn, buffer);
		} catch (UnknownHostException e) {
			System.err.println("Host not found: " + hostname + ".");
		} catch (IOException e) {
			System.err.println("Couldn't get I/O for " + hostname + ".");
		}
	}
	
	public void connect(int auth_type, boolean withTLS, String username, String password) 
			throws InterruptedException, NoSuchAlgorithmException, KeyManagementException, IOException {
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
		Lock.waitOnLocks();
		Lock.hardLock();
		char[] c = command.toCharArray();
		for (int i = 0; i < c.length; i++) out.write(c[i]);
		out.flush();
		Lock.hardUnlock();
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
	
	public ArrayList<Character> getBuffer() {
		return buffer;
	}
	
	public Socket getSocket() {
		return socket;
	}
	
	private String takeChallenge() throws InterruptedException {
		Lock.waitOnLocks();
		Lock.hardLock();
		char[] c = new char[buffer.size()];
		for (int i = 0; i < c.length; i++) {
			c[i] = buffer.get(i);
		}
		if (String.valueOf(c).indexOf("</challenge>") != -1) {
			StringBuilder sb = new StringBuilder();
			int j =  String.valueOf(c).indexOf("challenge"); 
			while (c[j] != '>') j++;
			j++;
			while (c[j] != '<') {
				sb.append(buffer.get(j));
				j++;
			}
			String challenge = new String(sb.toString());
			in.clearBuffer();
			Lock.hardUnlock();
			return challenge;
		}
		Lock.hardUnlock();
		return null;
	}
	
	private void startTLS() throws InterruptedException, KeyManagementException, NoSuchAlgorithmException, IOException {
		String starttls = "<starttls xmlns='urn:ietf:params:xml:ns:xmpp-tls'/>";
		send(starttls);
		while(!TLSHandler.gotTLS(buffer)) {
			Thread.sleep(100);
		}
		reader.join();
		Lock.waitOnLocks();
		Lock.hardLock();
		TLSHandler handler = new TLSHandler();
		SSLSocket sslsock = handler.createTLS(this);
		out = new PrintWriter(sslsock.getOutputStream());
		buffIn = new BufferedReader(new InputStreamReader(sslsock.getInputStream()));
		buffer.clear();
		in = new StreamReaderThread(buffIn, buffer);
		reader = new Thread(in);
		Lock.hardUnlock();
		reader.start();
	}
	
	public static void main(String[] args) throws KeyManagementException, NoSuchAlgorithmException, InterruptedException, IOException {
		ArrayList<Character> buffer = new ArrayList<Character>(1000);
 		Connection c = new Connection(5222, "localhost", buffer);
		c.connect(Connection.DIGESTMD5, true, "user", "user");
		String showme = "<presence xmlns='jabber:client' from='" + c.getUsername() + "@" + c.getHostname() +
				"'><show>chat</show><status>Up and running.</status></presence>";
		c.send(showme);
		Message.sendMessage(c, "antonio", "hahahah!");
		c.closeConnection();
	}
}
