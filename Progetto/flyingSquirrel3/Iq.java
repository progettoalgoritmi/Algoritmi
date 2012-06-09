package flyingSquirrel3;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.concurrent.CopyOnWriteArrayList;

public class Iq {
	
	private Connection c;
	private LinkedList<XMLNode> roster;
	
	public Iq(Connection c) {
		this.c = c;
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
	
	public LinkedList<XMLNode> rosterRequest() throws InterruptedException {
		String id = String.valueOf(c.hashCode()*23 + 1);
		String request = "<iq from='" + c.getUsername() + "@" + 
						c.getHostname() + c.getResource() + "' id='" + id + "' type='get'>" +
				"<query xmlns='jabber:iq:roster'/></iq>";
		c.send(request);
		while((roster = takeRoster()) == null);
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
	
	private LinkedList<XMLNode> takeRoster() {
		while(c.getLock().getLockStatus());
		c.getLock().lock();
		CopyOnWriteArrayList<XMLNode> cowal = new CopyOnWriteArrayList<XMLNode>(c.getNotProcessedEvents());
		Iterator<XMLNode> li = cowal.iterator();
		LinkedList<XMLNode> q = null;
		while(li.hasNext()) {
			XMLNode node = li.next();
			if (node.getRoot().equals("iq")) {
				ListIterator<XMLNode> li_q = node.getSubTags().listIterator();
				while(li_q.hasNext()) {
					XMLNode node_q = li_q.next();
					if (node_q.getRoot().equals("query") && node_q.getAttribute("xmlns").equals("jabber:iq:roster")) {
						ListIterator<XMLNode> items = node_q.getSubTags().listIterator();
						q = new LinkedList<XMLNode>();
						while(items.hasNext()) q.add(items.next()); 
						c.getNotProcessedEvents().remove(node);
					}
				}
			}
		}
		c.getLock().unlock();
		return q;
	}
	
	public LinkedList<XMLNode> getRoster() {
		return roster;
	}
}
