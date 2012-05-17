package flyingSquirrel3;

public class Presence {
	
	private Connection c;
	
	public final static String ONLINE = "chat", AWAY = "away", DONOTDISTURB = "dnd";
	
	public Presence(Connection c) {
		this.c = c;
	}

	public void setStatus(String status, String status_message) throws InterruptedException {
		String showme = "<presence xmlns='jabber:client' from='" + c.getUsername() + "@" + c.getHostname() +
				c.getResource() + "'><show>"+ status +"</show><status>"+ status_message + "</status></presence>";
		c.send(showme);
	}
}
