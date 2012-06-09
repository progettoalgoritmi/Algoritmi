package flyingSquirrel3;

public class MessageBox {
	private String from;
	private String message;
	
	public MessageBox(String from, String message) {
		this.from = from;
		this.message = message;
	}
	
	public String getFrom() {
		return from;
	}
	
	public String getMessage() {
		return message;
	}
	
	@Override
	public String toString() {
		return "From: " + from + "\nMessage: " + message;
	}
}
