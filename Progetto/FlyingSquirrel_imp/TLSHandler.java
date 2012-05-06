package FlyingSquirrel_imp;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

public class TLSHandler {
	
	public SSLSocket createTLS(Connection c) throws KeyManagementException, NoSuchAlgorithmException, IOException, InterruptedException {

		SSLContext sslctxt = SSLContext.getInstance("TLS");
		sslctxt.init(null, new javax.net.ssl.TrustManager[] {new DummyTrustManager()}, new java.security.SecureRandom());
		SSLSocketFactory factory = (SSLSocketFactory) sslctxt.getSocketFactory();
		SSLSocket sslsock = (SSLSocket) factory.createSocket(c.getSocket(), c.getHostname(), c.getPort(), true);
		sslsock.setUseClientMode(true);
		sslsock.setSoTimeout(0);
		sslsock.setKeepAlive(true);
		sslsock.startHandshake();

		return sslsock;
	}
	
	public static boolean gotTLS(ArrayList<Character> buffer) throws InterruptedException {
		Lock.waitOnLocks();
		Lock.hardLock();
		char[] c = new char[buffer.size()];
		for (int i = 0; i < c.length; i++) {
			c[i] = buffer.get(i);
		}
		if (((String.valueOf(c)).indexOf("proceed") != -1) &&
			((String.valueOf(c)).indexOf("urn:ietf:params:xml:ns:xmpp-tls") != -1)) {
			Lock.hardUnlock();
			return true;
		}
		Lock.hardUnlock();
		return false;
	}

}
