package flyingSquirrel3;

import java.util.LinkedList;

public class Iq {
	
	private Connection c;
	private LinkedList<String>[] roster = new LinkedList[4];
	private boolean rosterGot;
	private int j;
	
	public Iq(Connection c) {
		this.c = c;
		this.rosterGot = false;
	}
	
	public void setRoster(String username, String hostname, String name, String group) 
			throws InterruptedException {
		String id = String.valueOf(c.hashCode()*23 + 3);
		String request = "<iq from='" + c.getUsername() + "@" + c.getHostname() + c.getResource() +
				"' id='" + id + "' type='set'><query xmlns='jabber:iq:roster'>" +
				"<item jid='" + username + "@" + hostname + "' name='" + name + "'>" +
				"<group>" + group + "</group></item></query></iq>";
		c.send(request);
	}
	
	public LinkedList<String>[] rosterRequest() throws InterruptedException {
		String id = String.valueOf(c.hashCode()*23 + 1);
		String request = "<iq from='" + c.getUsername() + "@" + 
						c.getHostname() + c.getResource() + "' id='" + id + "' type='get'>" +
				"<query xmlns='jabber:iq:roster'/></iq>";
		j = c.getStreamBufferCounter();
		c.send(request);
		while(!rosterGot) roster = takeRoster();
		rosterGot = false;
		return roster;
	}
	
	public void getUserInfo(String user) throws InterruptedException {
		String id = String.valueOf(c.hashCode()*23 + 2);
		String request = "<iq type='get' " + "from='" + c.getUsername() + "@" + 
						c.getHostname() + c.getResource() + "' to='" + user + "@" + c.getHostname() + 
						"' id='" + id + "'>" +
						"<query xmlns='http://jabber.org/protocol/disco#info'/>" +
						"</iq>";
		c.send(request);
	}
	
	private LinkedList<String>[] takeRoster() {
		while(c.getLock().getLockStatus());
		c.getLock().lock();
		LinkedList<String>[] q = new LinkedList[4];
		for(int i = 0; i < 4; i++) {
			q[i] = new LinkedList<String>();
		}
		char[] ch = c.getBuffer();
		int i;
		if ((i = String.valueOf(ch).indexOf("<iq", j)) != -1) {
			if(String.valueOf(ch).indexOf("/>", i) != -1) {
				rosterGot = true;
				c.getLock().unlock();
				return q;
			}
		}
		if (String.valueOf(ch).indexOf("</iq>", j) == -1) {
			c.getLock().unlock();
			return q;
		}
		if (String.valueOf(ch).indexOf("<item ", j) == -1) {
			rosterGot = true;
			c.getLock().unlock();
			return q;
		}
		// There are items...
		populateRoster("jid=", '\'', '\"', ch, q, 0);
		populateRoster("name=", '\'', '\"', ch, q, 1);
		populateRoster("<group", '<', '<', ch, q, 2);
		populateRoster("subscription=", '\'', '\"', ch, q, 3);
		rosterGot = true;
		c.getLock().unlock();
		return q;
	}
	
	private void populateRoster(String string, char d1, char d2, char[] ch, LinkedList<String>[] q, int index) {
		StringBuilder sb = new StringBuilder();
		int i = j;
		int previous = i;
		while ((i = String.valueOf(ch).indexOf(string, i)) != -1) {
			i = i + string.length() + 1;
			previous = i;
			while((ch[i] != d1) && (ch[i] != d2)) {
				sb.append(ch[i]);
				i++;
			}
			if (previous == i) q[index].add(null);
			else q[index].add(sb.toString());
		}
		if (previous == i) q[index].add(null);
	}
	
	public LinkedList<String>[] getRoster() {
		return roster;
	}
}
