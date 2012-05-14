package ExplosiveSheep;

import java.net.*;
import java.util.*;

	public class ServerCommander implements Server{
		static public Window console=new Window();
		static private HashMap<Integer,ConnectionRunner> connectionList=new HashMap<Integer,ConnectionRunner>();
		static private HashSet<Integer> conversationList=new HashSet<Integer>();
	  	
	  	
	/**
	 * {@code ConnectionListener} get a list of connected user and listen for user that tries to connect
	 */
	private static class ConnectionListener extends Thread {
		private Socket connection;
		
		@Override
		public void run() {
			while(true){
				try{
					ServerSocket socket = new ServerSocket(5222);
					connection=socket.accept();
					socket=null;
					System.gc();
					console.println("Connection estabilished with: "+connection.getInetAddress().getHostAddress()+"\nport: "+connection.getPort());
					ConnectionRunner cr=new ConnectionRunner(connection,connectionList,console);
					connectionList.put(cr.hashCode(),cr);
					connectionList.get(cr.hashCode()).start();
				}catch(Exception e){}
			}
		}
	}// Connection Listener END
	
	
	public static void main(String[] args) {
		ConnectionListener cl=new ConnectionListener();
		console.print("Server ");
		cl.start();
		console.println("on");
	}
		
}

