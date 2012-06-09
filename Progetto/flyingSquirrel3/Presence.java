package flyingSquirrel3;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Stack;
import java.util.concurrent.CopyOnWriteArrayList;

public class Presence {
	
	private Connection c;
	private HashMap<String, String> presenceMap = new HashMap<String, String>();
	private boolean presenceChanged = false;
	
	public final static String ONLINE = "chat", AWAY = "away", DONOTDISTURB = "dnd";
	
	public Presence(Connection c) {
		this.c = c;
	}

	public void setStatus(String status, String status_message) throws InterruptedException {
		String showme = "<presence xmlns='jabber:client' from='" + c.getUsername() + "@" + c.getHostname() +
				c.getResource() + "'><show>"+ status +"</show><status>"+ status_message + "</status></presence>";
		c.send(showme);
	}
	
	public HashMap<String, String> getPresences() throws InterruptedException {
		while(c.getLock().getLockStatus());
		c.getLock().lock();
		presenceChanged = false;
		CopyOnWriteArrayList<XMLNode> cowal = new CopyOnWriteArrayList<XMLNode>(c.getNotProcessedEvents());
		Iterator<XMLNode> li = cowal.iterator();
		while (li.hasNext()) {
			XMLNode node = li.next();
			if (node.getRoot().equals("presence")) {
				String from = node.getAttribute("from");
				if (from.contains("/")) from = from.substring(0, from.indexOf('/'));
				if (from.contains(c.getUsername() + "@" + c.getHostname())) {
					c.getNotProcessedEvents().remove(node);
					continue;
				}
				String type = node.getAttribute("type");
				if (type == null) {
					Stack<XMLNode> subtags = node.getSubTags();
					Iterator<XMLNode> i = subtags.iterator();
					while (i.hasNext()) {
						XMLNode sub = i.next();
						if (sub.getRoot().equals("priority")) {
							type = "available";
							break;
						}
						if (sub.getRoot().equals("show")) {
							type = sub.getContent();
							break;
						}
					}
				}
				presenceMap.put(from, type);
				presenceChanged = true;
				c.getNotProcessedEvents().remove(node);
			}
		}
		c.getLock().unlock();
		return presenceMap;
	}
	
	public HashMap<String, String> getLastPresences() {
		return presenceMap;
	}
	
	public boolean presencesHasChanged() {
		return presenceChanged;
	}
	
}
