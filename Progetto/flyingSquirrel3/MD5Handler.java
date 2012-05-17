package flyingSquirrel3;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import FlyingSquirrel.Base64;

public class MD5Handler {
	
	public static String calculateResponse(int auth_type, String challenge, 
			Connection c, String username, String password) throws UnsupportedEncodingException, NoSuchAlgorithmException {
		System.out.println("Decoded challenge: " + challenge);
		int nonce_i = challenge.indexOf("nonce=\"");
		nonce_i += 7;
		String nonce_prep = challenge.substring(nonce_i);
		int nonce_e = nonce_prep.indexOf('\"');
		
		String nonce = nonce_prep.substring(0, nonce_e);
		String realm = c.getHostname();
		String digest_uri = "xmpp/" + c.getHostname();
		String nc = "00000001";
		String qop = "auth";
		String cnonce = String.valueOf(c.hashCode()*29 + nonce.hashCode());
		
		String result = digest(username, realm, password, qop, nc, nonce, cnonce, digest_uri);
		
		String decoded_response = "username=\"" + username //+ "@" + hostname 
									+ "\"," + "realm=\"" + c.getHostname() + "\"," +
									"nonce=\"" + nonce + "\"," + "qop=" + qop + "," +
									"cnonce=\"" + cnonce + "\"," + "nc=" + nc + "," +
									"digest-uri=\"xmpp/" + c.getHostname() + "\"," +
									"response=" + result + "," + "charset=utf-8";
		
		String encoded_response = new String(Base64.encodeString(decoded_response));
		
		String response = "<response xmlns='urn:ietf:params:xml:ns:xmpp-sasl'>" + encoded_response +
							"</response>";
		return response;
	}

	private static String digest(String username, String realm, String password, String qop, String nc,
			String nonce, String cnonce, String digest_uri) throws UnsupportedEncodingException, NoSuchAlgorithmException {
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
		return result;
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
