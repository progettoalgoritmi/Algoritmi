package FlyingSquirrel_imp;

public class Message {
	
	public static void sendMessage(Connection c, String destination, String message) throws InterruptedException {
		String message_request = "<message id='none' type='chat' to='" + destination + "@" + 
				c.getHostname() + "' from='" + c.getUsername() + "@" + c.getHostname() +
				"' xmlns='jabber:client'><active xmlns='http://jabber.org/protocol/chatstates'/>" +
				"<body>" + message + "</body><html xmlns='http://jabber.org/protocol/xhtml-im'>" +
				"<body xmlns='http://www.w3.org/1999/xhtml'><p>" + message + "</p></body>" +
				"</html></message>";
		c.send(message_request);
	}
}
