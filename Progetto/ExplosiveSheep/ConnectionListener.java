package ExplosiveSheep;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.HashSet;

/**
 * {@code ConnectionListener} get a list of connected user and listen for user that tries to connect
 */
public class ConnectionListener extends Thread {
	static public Window console;
	ServerSocket socket = null;
	Socket connection = null;
	static private HashMap<Integer,ConnectionRunner> connectionList=new HashMap<Integer,ConnectionRunner>();
	static private HashSet<Integer> conversationList=new HashSet<Integer>();
	
	public ConnectionListener(Window console) {
		this.console = console;
	}
	
	@Override
	public void run() {
		while(true){
			try{
				ServerSocket socket = new ServerSocket(5222);
				Socket connection=socket.accept();
				socket = null;
				System.gc();
				console.println("Connection estabilished with: "+connection.getInetAddress().getHostAddress()+"\nport: "+connection.getPort());
				ConnectionRunner cr=new ConnectionRunner(connection,connectionList,console);
				connectionList.put(cr.hashCode(),cr);
				connectionList.get(cr.hashCode()).start();
			}catch(Exception e){e.printStackTrace();}
		}
	}
}// Connection Listener END
