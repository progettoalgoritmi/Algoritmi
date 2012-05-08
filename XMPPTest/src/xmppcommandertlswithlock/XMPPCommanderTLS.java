package xmppcommandertlswithlock;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

import xmppcommander.Base64;

public class XMPPCommanderTLS {
	
	public static void main(String[] args) throws IOException, InterruptedException, NoSuchAlgorithmException, KeyManagementException {
		Socket socket = null;
		PrintWriter out = null;
		StreamReaderThread in = null;
		BufferedReader buffIn = null;
		String hostname = "localhost";
		int port = 5222;

		try {
			socket = new Socket(hostname, port);
			out = new PrintWriter(socket.getOutputStream());
			buffIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			in = new StreamReaderThread(buffIn);
		} catch (UnknownHostException e) {
			System.err.println("Host not found: " + hostname + ".");
		} catch (IOException e) {
			System.err.println("Couldn't get I/O for " + hostname + ".");
		}

		Thread reader = new Thread(in);
		reader.start();
		
		String xmlver = "<?xml version='1.0' encoding='UTF-8'?>";
		
		String handshake = "<stream:stream " +
				"to='" + hostname + "' " +
				"xmlns='jabber:client' " +
				"xmlns:stream='http://etherx.jabber.org/streams' " +
				"version='1.0'>";
		
		String starttls = "<starttls xmlns='urn:ietf:params:xml:ns:xmpp-tls'/>";

		String auth = "<auth xmlns='urn:ietf:params:xml:ns:xmpp-sasl' mechanism='DIGEST-MD5'/>";
	
		while(Lock.getLockStatus());
		Lock.lock();
		out.println(xmlver);
		out.flush();
		Lock.unlock();
		
		while(Lock.getLockStatus());
		Lock.lock();
		out.println(handshake);
		out.flush();
		Lock.unlock();
		
		while(Lock.getLockStatus());
		Lock.lock();
		out.println(starttls);
		out.flush();
		Lock.unlock();
		
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
		
		while(Lock.getLockStatus());
		Lock.lock();
		out.println(handshake);
		out.flush();
		Lock.unlock();
		
		while(Lock.getLockStatus());
		Lock.lock();
		out.println(auth);
		out.flush();
		Lock.unlock();
		
		while ((in.getChallenge() == null) || in.isLastChallengeUsed()) Thread.sleep(100);
		String challenge = new String(Base64.decode(in.getChallenge()));
		in.setLastChallengeUsed();
		in.clearBuffer();
		System.out.println("Decoded challenge: " + challenge);
		int nonce_i = challenge.indexOf("nonce=\"");
		nonce_i += 7;
		String nonce_prep = challenge.substring(nonce_i);
		int nonce_e = nonce_prep.indexOf('\"');
		
		String nonce = nonce_prep.substring(0, nonce_e);
		String username = "user";
		String realm = hostname;
		String password = "user";
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
		
		String response = "<response xmlns='urn:ietf:params:xml:ns:xmpp-sasl'>" + encoded_response +
							"</response>";
		
		while(Lock.getLockStatus());
		Lock.lock();
		out.println(response);
		out.flush();
		Lock.unlock();
		
		while ((in.getChallenge() == null) || in.isLastChallengeUsed()) Thread.sleep(100);
		challenge = new String(Base64.decode(in.getChallenge()));
		in.setLastChallengeUsed();
		in.clearBuffer();
		System.out.println("Decoded challenge: " + challenge);
		
		String auth_final = "<response xmlns='urn:ietf:params:xml:ns:xmpp-sasl'/>";
		
		while(Lock.getLockStatus());
		Lock.lock();
		out.println(auth_final);
		out.flush();
		Lock.unlock();
		
		while(Lock.getLockStatus());
		Lock.lock();
		out.println(handshake);
		out.flush();
		Lock.unlock();
		
		String bind_resource = "<iq type='set' id='bind_1'>" +
							"<bind xmlns='urn:ietf:params:xml:ns:xmpp-bind'/>" +
							"</iq>";
		
		while(Lock.getLockStatus());
		Lock.lock();
		out.println(bind_resource);
		out.flush();
		Lock.unlock();
		
		String showme = "<presence xmlns='jabber:client' from='" + username + "@" + hostname +
						"'><show>chat</show><status>Up and running.</status></presence>";
		
		while(Lock.getLockStatus());
		Lock.lock();
		out.println(showme);
		out.flush();
		Lock.unlock();
		
		while (String.valueOf(in.getBuffer()).indexOf("/bind") == -1) Thread.sleep(100);

		BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
		String userInput;
		
		boolean quit = false;
		
		while (!in.isEnded() && !quit) {
			String[] arrayInput = new String[100];
			int i = 0;
			
			while (!in.isEnded()) {
				userInput = stdIn.readLine();
				if (userInput.equalsIgnoreCase("send")) break;
				if (userInput.indexOf("/m") == 0) {
					int messageStart = 0;
					try {
						messageStart = userInput.indexOf(" ", 3);
						String rcp = userInput.substring(3, messageStart);
						String msg = userInput.substring(messageStart + 1);
						String message_request = "<message id='none' type='chat' to='" + rcp + "@" + 
												hostname + "' from='" + username + "@" + hostname +
												"'><body>" + msg + "</body></message>";
						arrayInput[0] = message_request;
						i = 1;
						break;
					} catch (StringIndexOutOfBoundsException e) {
						System.err.println("Syntax error!\nUsage: /m username message");
						i = 0;
					} 
				}
				if (userInput.indexOf("/b") == 0) {
					try {
						String rcp = userInput.substring(3);
						String message_request = "<message id='none' type='chat' to='" + rcp + "@" + 
												hostname + "' from='" + username + "@" + hostname +
												"'><attention xmlns='urn:xmpp:attention:0'/></message>";
						arrayInput[0] = message_request;
						i = 1;
						break;
					} catch (StringIndexOutOfBoundsException e) {
						System.err.println("Syntax error!\nUsage: /b username");
						i = 0;
					} 
				}
				if (userInput.equalsIgnoreCase("sendMessage")) {
					System.out.print("to: ");
					String rcp = stdIn.readLine();
					System.out.print("message: ");
					String msg = stdIn.readLine();
					String message_request = "<message id='none' type='chat' to='" + rcp + "@" + 
											hostname + "' from='" + username + "@" + hostname +
											"'><body>" + msg + "</body></message>";
					arrayInput[0] = message_request;
					i = 1;
					break;
				}
				if (userInput.equalsIgnoreCase("sendBuzz")) {
					System.out.print("to: ");
					String rcp = stdIn.readLine();
					String message_request = "<message id='none' type='chat' to='" + rcp + "@" + 
											hostname + "' from='" + username + "@" + hostname +
											"'><attention xmlns='urn:xmpp:attention:0'/></message>";
					arrayInput[0] = message_request;
					i = 1;
					break;
				}
				if (userInput.equalsIgnoreCase("quit")) {
					arrayInput[0] = "</stream:stream>";
					i = 1;
					quit = true;
					break;
				}
				arrayInput[i] = userInput;
				i++;
			}

			for (int j = 0; j < i; j++) {
				while(Lock.getLockStatus());
				Lock.lock();
				out.write(arrayInput[j]);
				out.flush();
				Lock.unlock();
			}
		}
		
		reader.join();
		out.close();
		buffIn.close();
		stdIn.close();
		socket.close();
	}
	
	private static String md5hex(byte[] str) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < str.length; i++) {
			sb.append(Integer.toHexString((0x000000ff & str[i]) | 0xffffff00).substring(6));
		}
		String result = new String(sb);
		return result;
	}
}