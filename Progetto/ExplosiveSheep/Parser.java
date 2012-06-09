package ExplosiveSheep;

import java.util.Stack;

public class Parser {
	
	private char[] buffer;
	private Stack<XMLNode> stack, toBeProcessed;
	
	public Parser(char[] buffer, Stack<XMLNode> stack, Stack<XMLNode> toBeProcessed) {
		this.buffer = buffer;
		this.stack = stack;
		this.toBeProcessed = toBeProcessed;
	}

	public void parse() {
		String str = String.valueOf(buffer);
		boolean root = (str.charAt(0) == '<');
		boolean closure = (str.charAt(1) == '/');
		if (root) {
			if (closure) {
				String closing_tag = split(str)[0].substring(1);
				if (closing_tag.equals(stack.peek().getRoot())) {
					XMLNode next = stack.pop();
					if (!stack.isEmpty() && !stack.peek().getRoot().equals("stream:stream") &&
							!stack.peek().getRoot().equals("?xml"))
						stack.peek().pushSubtag(next);
					else toBeProcessed.push(next);
				}
				else throw new RuntimeException("Requested closing tag '</" + closing_tag + ">' while expected '</"
												+ stack.peek().getRoot() + ">' closing tag.");
			} else {
				String[] split = split(str); 
				if (split[0].matches("^[a-zA-Z_][a-zA-Z0-9_\\-:\\.]*/$")) {
					XMLNode next = new XMLNode(split[0].substring(0, split[0].length() - 1));
					if (!stack.isEmpty() && !stack.peek().getRoot().equals("stream:stream") &&
							!stack.peek().getRoot().equals("?xml"))
						stack.peek().pushSubtag(next);
					else toBeProcessed.push(next);
					return;
				}
				if ((!split[0].matches("^[a-zA-Z_][a-zA-Z0-9_\\-:\\.]*$")) && (!split[0].equals("?xml")))
					throw new RuntimeException("'" + split[0] + "' not expected.");
				XMLNode node = new XMLNode(split[0]);
				stack.push(node);
				int i = 1;
				while (i < split.length) {
					if (split[i].equals("?")) {
						if(!split[0].equals("?xml")) throw new RuntimeException("'?' closing character allowed " +
																				" only for '?xml' root.");
						else toBeProcessed.push(stack.pop());
						break;
					}
					if (split[i].equals("/")) {
						XMLNode next = stack.pop();
						if (!stack.isEmpty() && !stack.peek().getRoot().equals("stream:stream") &&
								!stack.peek().getRoot().equals("?xml"))
							stack.peek().pushSubtag(next);
						else toBeProcessed.push(next);
						break;
					}
					if (!split[i].matches("^[a-zA-Z_][a-zA-Z0-9_\\-:\\.]*=$")) 
						throw new RuntimeException("'" + split[i].substring(0, split[i].length() - 1) 
																			+ "' not expected as attribute.");
					node.addAttribute(split[i].substring(0, split[i].length() - 1), split[i + 1]);
					i += 2;
				}
				if (node.getRoot().equals("stream:stream")) toBeProcessed.push(node); 
			}
		}
		else {
			String data = str.substring(0, str.indexOf('<'));
			String closing_tag = str.substring(str.indexOf("</") + "</".length(), str.indexOf('>'));
			stack.peek().addContent(data);
			if (closing_tag.equals(stack.peek().getRoot())) {
				XMLNode next = stack.pop();
				if (!stack.isEmpty() && !stack.peek().getRoot().equals("stream:stream") &&
						!stack.peek().getRoot().equals("?xml"))
					stack.peek().pushSubtag(next);
				else toBeProcessed.push(next);
			} else if (closing_tag.equals("stream:stream") && 
					(toBeProcessed.isEmpty() || !toBeProcessed.peek().getRoot().equals("stream:stream")))
				throw new RuntimeException("Stream abnormally interrupted.");
			else throw new RuntimeException("Requested closing tag '</" + closing_tag + ">' while expected '</"
											+ stack.peek().getRoot() + ">' closing tag.");
		}
	}
	
	private static String[] split(String string) {
		String[] temp_split = string.split("<|>|\"|'| ");
		int counter = 0;
		for (String x : temp_split) {
			if (!x.matches("\\s*?") && !x.matches("\\x00*")) counter++;
		}
		String[] split = new String[counter];
		int i = 0;
		for (String x : temp_split) {
			if (!x.matches("\\s*?") && !x.matches("\\x00*")) {
				split[i] = x;
				i++;
			}
		}
		return split;
	}
}
