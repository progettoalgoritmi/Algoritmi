package flyingSquirrel3;

import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.concurrent.CopyOnWriteArrayList;

// import java.util.LinkedList;

public class Message {
	
	private LinkedList<String> lastMessages = new LinkedList<String>();
	private Connection c;
	
	public Message(Connection c) {
		this.c = c;
	}
	
	public void sendMessage(String destination, String message) throws InterruptedException {
		String message_request = "<message id='none' type='chat' to='" + destination + "@" + 
				c.getHostname() + "' from='" + c.getUsername() + "@" + c.getHostname() + c.getResource() +
				"' xmlns='jabber:client'><active xmlns='http://jabber.org/protocol/chatstates'/>" +
				"<body>" + message + "</body><html xmlns='http://jabber.org/protocol/xhtml-im'>" +
				"<body xmlns='http://www.w3.org/1999/xhtml'><p>" + message + "</p></body>" +
				"</html></message>";
		c.send(message_request);
	}
	
	public void sendBuzz(String destination) throws InterruptedException {
		String message_request = "<message id='none' type='chat' to='" + destination + "@" + 
				c.getHostname() + "' from='" + c.getUsername() + "@" + c.getHostname() + c.getResource() +
				"'><attention xmlns='urn:xmpp:attention:0'/></message>";
		c.send(message_request);
	}
	
	public LinkedList<String> getMessagesThroughStack() throws InterruptedException {
		while(c.getLock().getLockStatus());
		c.getLock().lock();
		LinkedList<String> q = new LinkedList<String>();
		CopyOnWriteArrayList<XMLNode> cowal = new CopyOnWriteArrayList<XMLNode>(c.getNotProcessedEvents());
		Iterator<XMLNode> li = cowal.iterator();
		while(li.hasNext()) {
			XMLNode node = li.next();
			ListIterator<XMLNode> li_m = node.getSubTags().listIterator();
			if (node.getRoot().equals("message")) {
				while(li_m.hasNext()) {
					XMLNode node_m = li_m.next();
					if (node_m.getRoot().equals("body")) {
						q.add(node.getAttribute("from"));
						q.add(node_m.getContent());
						lastMessages = q;
						c.getNotProcessedEvents().remove(node);
					}
				}
			}
		}
		c.getLock().unlock();
		return q;
	}
	
	public LinkedList<String> getMessages() {
		while(c.getLock().getLockStatus());
		c.getLock().lock();
		LinkedList<String> q = new LinkedList<String>();
		char[] ch = c.getBuffer();
		int i;
		while ((i = String.valueOf(ch).indexOf("<message")) != -1) {
			StringBuilder sb_from = new StringBuilder();
			StringBuilder sb_message = new StringBuilder();
			int begin = i;
			int j = String.valueOf(ch).indexOf("from=", i);
			int k = String.valueOf(ch).indexOf("<body>", i);
			if ((j > -1) && (k > -1)) {
				i = j + "from='".length();
				while ((i < ch.length) && (ch[i] != '\"') && (ch[i] != '\'')) {
					sb_from.append(ch[i]);
					i++;
				}
				if (i >= ch.length) break;
				i = k + "<body>".length();
				while ((i < ch.length) && (ch[i] != '<')) {
					sb_message.append(ch[i]);
					i++;
				}
				if (i >= ch.length) break;
				int end = i + "/body>".length();
				q.add(sb_from.toString());
				q.add(sb_message.toString());
				lastMessages = q;
				c.safeClearBuffer(begin, end);
				ch = c.getBuffer();
			}
			else break;
		}
		c.getLock().unlock();
		return q;
	}
	
	public LinkedList<String> getLastMessages() {
		return new LinkedList<String>(lastMessages);
	}
	
	public void clearLastMessages() {
		lastMessages.clear();
	}
}
