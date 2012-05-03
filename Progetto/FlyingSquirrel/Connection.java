package FlyingSquirrel;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

public class Connection {
	public static final int DIGESTMD5 = 0, PLAIN = 1;
	private Socket socket = null;
	private PrintWriter out = null;
	private StreamReaderThread in = null;
	private BufferedReader buffIn = null;
	private String hostname = null;
	private String username = null;
	private Integer port = null;
	
	public Connection(Integer port, String hostname) {
		try {
			this.port = port;
			this.hostname = hostname;
			socket = new Socket(hostname, port);
			out = new PrintWriter(socket.getOutputStream());
			buffIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			in = new StreamReaderThread(buffIn);
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
		switch (auth_type) {
			case 0: Thread reader = new Thread(in);
					reader.start();

					auth = auth + "DIGEST-MD5'/>";

					send(xmlver);

					send(handshake);

					if (withTLS) {
						String starttls = "<starttls xmlns='urn:ietf:params:xml:ns:xmpp-tls'/>";
						send(starttls);

						Thread.sleep(250); //Sleep 'til reader finishes... TODO: must be fixed.

						SSLContext sslctxt = SSLContext.getInstance("TLS");
						sslctxt.init(null, new javax.net.ssl.TrustManager[] {new DummyTrustManager()}, new java.security.SecureRandom());
						SSLSocketFactory factory = (SSLSocketFactory) sslctxt.getSocketFactory();
						SSLSocket sslsock = (SSLSocket) factory.createSocket(socket, hostname, port, true);
						sslsock.setUseClientMode(true);
						sslsock.setSoTimeout(0);
						sslsock.setKeepAlive(true);
						sslsock.startHandshake();

						out = new PrintWriter(sslsock.getOutputStream());
						buffIn = new BufferedReader(new InputStreamReader(sslsock.getInputStream()));
						in = new StreamReaderThread(buffIn);
						reader = new Thread(in);
						reader.start();

						send(handshake);
					} 
					
					send(auth);
					
					while ((in.getChallenge() == null) || in.isLastChallengeUsed()) Thread.sleep(100);
					String challenge = new String(Base64.decode(in.getChallenge()));
					in.setLastChallengeUsed();
					in.clearBuffer();
					
					String response = calculateResponse(auth_type, challenge, username, password);
					
					send(response);
					
					while ((in.getChallenge() == null) || in.isLastChallengeUsed()) Thread.sleep(100);
					challenge = new String(Base64.decode(in.getChallenge()));
					in.setLastChallengeUsed();
					in.clearBuffer();
					System.out.println("Decoded challenge: " + challenge);
					
					String auth_final = "<response xmlns='urn:ietf:params:xml:ns:xmpp-sasl'/>";
					
					send(auth_final);
					
					send(handshake);
					
					String bind_resource = "<iq type='set' id='bind_1'>" +
										"<bind xmlns='urn:ietf:params:xml:ns:xmpp-bind'/>" +
										"</iq>";
					
					send(bind_resource);
					break;
					
			case 1: break;
			default: throw new IllegalArgumentException("Not supported or invalid argument.");
		}
	}
	
	public void send(String command) throws InterruptedException {
		while(Lock.getLockStatus()) {
			if (Lock.isHanged()) Thread.sleep(200);
			if (Lock.isHanged()) Lock.unlock();
		}
		Lock.lock();
		out.write(command);
		out.flush();
		Lock.unlock();
	}
	
	private String calculateResponse(int auth_type, String challenge, String username, String password) 
			throws NoSuchAlgorithmException, UnsupportedEncodingException {
		String response = null;
		switch (auth_type) {
		case 0:
		System.out.println("Decoded challenge: " + challenge);
		int nonce_i = challenge.indexOf("nonce=\"");
		nonce_i += 7;
		String nonce_prep = challenge.substring(nonce_i);
		int nonce_e = nonce_prep.indexOf('\"');
		
		String nonce = nonce_prep.substring(0, nonce_e);
		String realm = hostname;
		String digest_uri = "xmpp/" + hostname;
		String nc = "00000001";
		String qop = "auth";
		String cnonce = "d93000000";
		
		// MD5-Digest algo implementation
		
		MessageDigest md = MessageDigest.getInstance("MD5");
		String x = username + ":" + realm + ":" + password;
		md.update(x.getBytes("UTF-8"));
		byte[] y = md.digest();
		md.reset();
		String a1 = ":" + nonce + ":" + cnonce;
		md.update(y);
		md.update(a1.getBytes("UTF-8"));
		byte[] ha1 = md.digest();
		md.reset();
		String a2 = "AUTHENTICATE:" + digest_uri;
		md.update(a2.getBytes("UTF-8"));
		byte[] ha2 = md.digest();
		md.reset();
		String ha1s = md5hex(ha1);
		String ha2s = md5hex(ha2);
		String kd = ha1s + ":" + nonce + ":" + nc + ":" + cnonce + ":" + qop + ":" + ha2s;
		md.update(kd.getBytes("UTF-8"));
		String result = md5hex(md.digest());
		md.reset();
		
		// End of MD5-Digest SASL algo
		
		String decoded_response = "username=\"" + username //+ "@" + hostname 
									+ "\"," + "realm=\"" + hostname + "\"," +
									"nonce=\"" + nonce + "\"," + "qop=auth," +
									"cnonce=\"d93000000\"," + "nc=00000001," +
									"digest-uri=\"xmpp/" + hostname + "\"," +
									"response=" + result + "," + "charset=utf-8";
		
		String encoded_response = new String(Base64.encodeString(decoded_response));
		
		response = "<response xmlns='urn:ietf:params:xml:ns:xmpp-sasl'>" + encoded_response +
							"</response>";
		break;
		case 1: throw new IllegalArgumentException("Not supported");
		default: throw new IllegalArgumentException("Not supported");
		}
		return response;
	}
	
	private String md5hex(byte[] str) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < str.length; i++) {
			sb.append(Integer.toHexString((0x000000ff & str[i]) | 0xffffff00).substring(6));
		}
		String result = new String(sb);
		return result;
	}
	
	public String getUsername() {
		return username;
	}
	
	public String getHostname() {
		return hostname;
	}
	
	public static void main(String[] args) throws KeyManagementException, NoSuchAlgorithmException, InterruptedException, IOException {
		Connection c = new Connection(5222, "localhost");
		c.connect(Connection.DIGESTMD5, false, "user", "user");
		String showme = "<presence xmlns='jabber:client' from='" + c.getUsername() + "@" + c.getHostname() +
				"'><show>chat</show><status>Up and running.</status></presence>";
		c.send(showme);
		c.send("</stream:stream>");
	}
}
