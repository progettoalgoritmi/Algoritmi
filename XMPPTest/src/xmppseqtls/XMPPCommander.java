// Sì, lo so, il codice è una vera m***a... del resto non è che una shell di test e debug...

package xmppseqtls;
import java.io.*;
import java.net.*;
import java.security.KeyManagementException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

import xmppcommandertls.DummyTrustManager;

public class XMPPCommander {
	static char[] c = new char[10000];
	public static void main(String[] args) throws IOException, NoSuchAlgorithmException, InterruptedException, KeyManagementException {

		Socket socket = null;
		PrintWriter out = null;
		BufferedReader in = null;
		String hostname = "localhost";
		int port = 5222;

		try {
			socket = new Socket(hostname, port);
			out = new PrintWriter(socket.getOutputStream());
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()), c.length);
		} catch (UnknownHostException e) {
			System.err.println("Don't know about host: " + hostname + ".");
			System.exit(1);
		} catch (IOException e) {
			System.err.println("Couldn't get I/O for "
					+ "the connection to: " + hostname + ".");
			System.exit(1);
		}
		
		System.out.println("Connection estabilished.");
		
		String handshake = "<stream:stream " +
						"to='" + hostname + "' " +
						"xmlns='jabber:client' " +
						"xmlns:stream='http://etherx.jabber.org/streams' " +
						"version='1.0'>";
		
		String starttls = "<starttls xmlns='urn:ietf:params:xml:ns:xmpp-tls'/>";
		
		String auth = "<auth xmlns='urn:ietf:params:xml:ns:xmpp-sasl' mechanism='DIGEST-MD5'/>";
		
		out.write(handshake);
		out.flush();
		
		printBuffer(in);
		
		out.write(starttls);
		out.flush();
		
		while(in.read() != '>');
		
		SSLContext sslctxt = SSLContext.getInstance("TLS");
		sslctxt.init(null, new javax.net.ssl.TrustManager[] {new DummyTrustManager()}, new java.security.SecureRandom());
		SSLSocketFactory factory = (SSLSocketFactory) sslctxt.getSocketFactory();
		SSLSocket sslsock = (SSLSocket) factory.createSocket(socket, hostname, port, true);
		sslsock.setUseClientMode(true);
		sslsock.setSoTimeout(0);
		sslsock.setKeepAlive(true);
		sslsock.startHandshake();
		
		out = new PrintWriter(sslsock.getOutputStream());
		in = new BufferedReader(new InputStreamReader(sslsock.getInputStream()));
		
		out.write(handshake);
		out.flush();
		
		printBuffer(in);
		
		out.write(auth);
		out.flush();
		
		printBuffer(in);
		
		String challenge = new String(Base64.decode(getChallenge(c, in)));
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
		
		out.write(response);
		out.flush();
		
		printBuffer(in);
		
		challenge = new String(Base64.decode(getChallenge(c, in)));
		System.out.println("Decoded challenge: " + challenge);
		
		String auth_final = "<response xmlns='urn:ietf:params:xml:ns:xmpp-sasl'/>";
		
		out.write(auth_final);
		out.flush();
		
		printBuffer(in);
		
		System.out.println("Authenticated! ;)");
		
		out.write(handshake);
		out.flush();
		
		printBuffer(in);
		
		String bind_resource = "<iq type='set' id='bind_1'>" +
							"<bind xmlns='urn:ietf:params:xml:ns:xmpp-bind'/>" +
							"</iq>";
		
		out.write(bind_resource);
		out.flush();
		
		printBuffer(in);
		
		while (String.valueOf(c).indexOf("/bind") == -1) printBuffer(in);
		System.out.println("Binding done. Starting shell...");
		
		BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
		String userInput;
		
		boolean flag = true;
		while(flag) {
			String[] arrayInput = new String[100];
			int i = 0;
			
			if (String.valueOf(c).contains("</stream:stream>")) { // End if stream is closed
				flag = false;
				break;
			}
			
			System.out.print(username + "@" + hostname + "$ ");
			while (!(userInput = stdIn.readLine()).equals("EOF")) {
				arrayInput[i] = userInput;
				i++;
				System.out.print(username + "@" + hostname + "$ ");
			}

			if ((arrayInput[0] == null) || arrayInput[0].equals("CLOSE")) {
				out.write("</stream:stream>");
				out.flush();
				printBuffer(in);
				flag = false;
				break;
			}

			for (int j = 0; j < i; j++) {
				out.write(arrayInput[j]);
				if (arrayInput[j].contains("</stream:stream>")) {
					out.flush();
					printBuffer(in);
					flag = false;
					break;
				}
			}

			if (flag) {
				out.flush();
				System.out.println("Sent.");
				printBuffer(in);
			}

		}

		out.close();
		in.close();
		stdIn.close();
		socket.close();
	}
	
	private static void printBuffer(BufferedReader in) throws IOException, InterruptedException {
		for (int j = 0; j < c.length; j++) c[j] = '\0';
		in.read(c, 0, c.length);
		System.out.print("Server: ");
		for (int j = 0; j < c.length; j++) {
			System.out.print(c[j]);
		}

		System.out.println();
	}
	
	private static String getChallenge(char[] c, BufferedReader in) throws IOException, InterruptedException {
		while ((String.valueOf(c)).indexOf("challenge") == -1) printBuffer(in);
		StringBuilder sb = new StringBuilder();
		int j = 0;
		while (c[j] != '>') j++;
		j++;
		while (c[j] != '<') {
			sb.append(c[j]);
			j++;
		}
		String challenge = new String(sb.toString());
		return challenge;
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