package TestServer;
/*costruito interamente in base alle domande inviate dal client
 *abilmente scopiazzato di qua e di l�*/
import java.io.*;
import java.net.*;
import java.util.*;

	public class ServerCommander implements Server{
		static char[] s = new char[10000];
		static private HashMap<Integer,Thread> ConnectionList=new HashMap<Integer,Thread>();
		private static Socket connection;
	  	public ServerCommander() {
	  		//TODO
	  	}
	  	
//Connection Listener
	private static class ConnectionListener extends Thread {
		public ConnectionListener(){}

		@Override
		public void run() {
			while(true){
				try{
					ServerSocket socket = new ServerSocket(5222);
					connection=socket.accept();
					System.out.println("Connection estabilished with: "+connection.getInetAddress().getHostAddress()+"\nport: "+connection.getPort());
					ConnectionRunner cr=new ConnectionRunner(connection);
					ConnectionList.put(cr.hashCode(),cr);
					ConnectionList.get(cr.hashCode()).start();
				}catch(Exception e){}
			}
		}
	}
	
	private static class ConnectionRunner extends Thread {
		private Socket connection;
		private BufferedReader reader;
		private PrintWriter writer;
		boolean end=false;
		public ConnectionRunner(Socket socket){
			connection=socket;
		}
		
		@Override
		public void run() {
			try {
				reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
				System.out.println("created reader");
				writer = new PrintWriter(connection.getOutputStream());
				System.out.println("created writer");
				System.out.println("no exeption");
		  		String handshake=readBuffer(reader);
		  		System.out.println(handshake);
		  		StringTokenizer sc=new StringTokenizer(handshake,"'\"");
		  		if(!(sc.hasMoreTokens()&&sc.nextToken().equals("<stream:stream to=")))throw new IllegalArgumentException();
		  		sc.nextToken();//if(!sc.hasMoreTokens()||!sc.nextToken().matches("[/d]{0,3}[/.][/d]{0,3}[/.][/d]{0,3}"))throw new IllegalArgumentException();
		  		if(!sc.hasMoreTokens()||!sc.nextToken().equals(" xmlns="))throw new IllegalArgumentException();
		  		if(!sc.hasMoreTokens()||!sc.nextToken().matches("[j][a][b][b][e][r][:][^/d]*"))throw new IllegalArgumentException();
		  		if(!sc.hasMoreTokens()||!sc.nextToken().equals(" xmlns:stream="))throw new IllegalArgumentException();
		  		if(!sc.hasMoreTokens()||!sc.nextToken().equals("http://etherx.jabber.org/streams"))throw new IllegalArgumentException();
		  		if(!sc.hasMoreTokens()||!sc.nextToken().equals(" version="))throw new IllegalArgumentException();
		  		if(!sc.hasMoreTokens()||!sc.nextToken().equals("1.0"))throw new IllegalArgumentException();
		  		String handshake2="<stream:stream from='"+connection.hashCode()+"' xmlns='jabber:client' xmlns:stream='http://etherx.jabber.org/streams' version='1.0'>";
		  		writer.write(handshake2);
		  		writer.flush();
		  		String auth=readBuffer(reader);
		  		System.out.println(auth);
			}catch (IllegalArgumentException e){throw new IllegalArgumentException();}
			catch (Exception e) {}
			while(!end){
				try {
					String lettura=readBuffer(reader);
					
				} catch (Exception e) {}
			}
		}
		
		@Override
		public int hashCode(){
			//TODO: ogni utente una sola connessione 
			return connection.hashCode();
		}
	}
	  	
		public static void main(String[] args) {
			ConnectionListener cl=new ConnectionListener();
		  	while(true);//test
		  	/*writer.close();
			reader.close();
			connection.close();*/
	}
		
	public int getPort(){
		return connection.getPort();
	}
	
	public String getHost(){
		return connection.getInetAddress().getHostAddress();
	}
		
	private static String readBuffer(BufferedReader in) throws IOException, InterruptedException {
		s=new char[10000];
		in.read(s, 0, s.length);
		StringBuilder sb=new StringBuilder();
		for (int j = 0; s[j]!='>'&&j<s.length; j++) {//ci pensa lo string tokenizer a vedere se hanno scritto cavolate
			sb.append(s[j]);
		}
		sb.append('>');
		return sb.toString();
	}
}
