package flyingSquirrel3;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.Stack;

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
	
	public static boolean gotTLS(Stack<XMLNode> toBeProcessed, Lock lock) throws InterruptedException {
		while(lock.getLockStatus());
		lock.lock();
		if (!toBeProcessed.isEmpty()) {
			if (toBeProcessed.peek().getRoot().equals("proceed") &&
					toBeProcessed.peek().getAttribute("xmlns").equals("urn:ietf:params:xml:ns:xmpp-tls")) {
				lock.unlock();
				return true;
			}
		}
		lock.unlock();
		return false;
	}

}
