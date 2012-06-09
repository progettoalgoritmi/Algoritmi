package flyingSquirrel3;

import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.concurrent.CopyOnWriteArrayList;

// import java.util.LinkedList;

public class Message {
	
	private LinkedList<MessageBox> lastMessages = new LinkedList<MessageBox>();
	private Connection c;
	
	public Message(Connection c) {
		this.c = c;
	}
	
	public void sendMessage(String destination, String message) throws InterruptedException {
		/*XMLNode message_node = new XMLNode("message");
		message_node.addAttribute("id", "none");
		message_node.addAttribute("type", "chat");
		message_node.addAttribute("to", destination + "@" + c.getHostname());
		message_node.addAttribute("from", c.getUsername() + "@" + c.getHostname() + c.getResource());
		message_node.addAttribute("xmlns", "jabber:client");
		XMLNode active = new XMLNode("active");
		active.addAttribute("xmlns", "http://jabber.org/protocol/chatstates");
		message_node.pushSubtag(active);
		XMLNode body = new XMLNode("body");
		body.addContent(message);
		message_node.pushSubtag(body);
		XMLNode html = new XMLNode("html");
		html.addAttribute("xmlns", "http://jabber.org/protocol/xhtml-im");
		XMLNode html_body = new XMLNode("body");
		html_body.addAttribute("xmlns", "http://www.w3.org/1999/xhtml");
		XMLNode p_content = new XMLNode("p");
		p_content.addContent(message);
		html_body.pushSubtag(p_content);
		html.pushSubtag(html_body);
		message_node.pushSubtag(html);
		String message_request = message_node.toString();*/
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
	
	public LinkedList<MessageBox> getMessages() throws InterruptedException {
		while(c.getLock().getLockStatus());
		c.getLock().lock();
		LinkedList<MessageBox> q = new LinkedList<MessageBox>();
		CopyOnWriteArrayList<XMLNode> cowal = new CopyOnWriteArrayList<XMLNode>(c.getNotProcessedEvents());
		Iterator<XMLNode> li = cowal.iterator();
		while(li.hasNext()) {
			XMLNode node = li.next();
			ListIterator<XMLNode> li_m = node.getSubTags().listIterator();
			if (node.getRoot().equals("message")) {
				while(li_m.hasNext()) {
					XMLNode node_m = li_m.next();
					if (node_m.getRoot().equals("body")) {
						q.add(new MessageBox(node.getAttribute("from"), node_m.getContent()));
						lastMessages = q;
						c.getNotProcessedEvents().remove(node);
					}
				}
			}
		}
		c.getLock().unlock();
		return q;
	}
	
	public LinkedList<MessageBox> getLastMessages() {
		return new LinkedList<MessageBox>(lastMessages);
	}
	
	public void clearLastMessages() {
		lastMessages.clear();
	}
}
