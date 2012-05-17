package flyingSquirrel3;

import java.util.HashMap;
import java.util.Stack;

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
}
