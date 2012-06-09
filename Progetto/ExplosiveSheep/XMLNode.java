package ExplosiveSheep;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;
import java.util.Stack;
import java.util.concurrent.CopyOnWriteArrayList;

public class XMLNode {
	
	private String root = null;
	private Stack<XMLNode> subtags = null;
	private HashMap<String, String> attributes;
	private String content;

	public XMLNode(String root) {
		this.root = root;
		this.attributes = new HashMap<String, String>(); 
		this.subtags = new Stack<XMLNode>();
		this.content = null;
	}
	
	public void pushSubtag(XMLNode node) {
		subtags.push(node);
	}

	public void addAttribute(String key, String value) {
		attributes.put(key, value);
	}
	
	public void addContent(String content) {
		this.content = content;
	}
	
	public String getAttribute(String key) {
		return attributes.get(key);
	}
	
	public HashMap<String, String> getAttributes() {
		return attributes;
	}
	
	public String getRoot() {
		return root;
	}
	
	public String getContent() {
		return content;
	}
	
	public Stack<XMLNode> getSubTags() {
		return subtags;
	}
	
	public void removeAttribute(String attribute) {
		attributes.remove(attribute);
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append('<');
		sb.append(root);
		CopyOnWriteArrayList<String> k = new CopyOnWriteArrayList<String>(attributes.keySet());
		CopyOnWriteArrayList<String> v = new CopyOnWriteArrayList<String>(attributes.values());
		Iterator<String> it_keys = k.iterator();
		Iterator<String> it_values = v.iterator();
		while (it_keys.hasNext() && it_values.hasNext()) {
			sb.append(' ' + it_keys.next());
			sb.append("='");
			sb.append(it_values.next() + "'");
		}
		if (root.equals("?xml")) {
			sb.append("?>");
			return sb.toString();
		}
		sb.append('>');
		if (content != null) sb.append(content);
		else {
			Iterator<XMLNode> it = subtags.iterator();
			while (it.hasNext()) sb.append(it.next());
		}
		sb.append("</" + root + '>');
		return sb.toString();
	}
}
