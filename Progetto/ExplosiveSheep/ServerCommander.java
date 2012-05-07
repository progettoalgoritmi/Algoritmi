package TestServer;

import java.io.*;
import java.net.*;
import java.util.*;

	public class ServerCommander implements Server{
		static char[] s = new char[10000];
		static private HashMap<Integer,Thread> connectionList=new HashMap<Integer,Thread>();
		static private HashSet<Integer> conversationList=new HashSet<Integer>();
		private static Socket connection;
		
	  	public ServerCommander() {
	  		//TODO
	  	}
	  	
	  	
	/**
	 * {@code ConnectionListener} get a list of connected user and listen for user that tries to connect
	 */
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
					connectionList.put(cr.hashCode(),cr);
					connectionList.get(cr.hashCode()).start();
				}catch(Exception e){}
			}
		}
	}// Connection Listener END
	
	
	/**
	 * {@code ConnectionRunner} work as connection executor for each user try to connect.
	 * Used to start a thread to listen to the queries from the users.
	 */
	private static class ConnectionRunner extends Thread {
		private Socket connection;
		private BufferedReader reader;
		private PrintWriter writer;
		private Lock warlock;
		private String userState="online";
		boolean end=false;
		public ConnectionRunner(Socket socket){
			connection=socket;
		}
		
		public void run() {
			//inizializzazione
			try {
				reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
				System.out.println("created reader");
				writer = new PrintWriter(connection.getOutputStream());
				System.out.println("created writer");
			} catch (Exception e) {
				System.err.println("1:connection error");
			}
			System.out.println("no exception");
			
			//discussione sul tipo di autenticazione
			int authType=1;//0=md5, 1=tls
			if(authType==0)System.out.println("MD5");else System.out.println("TLS");
			switch(authType){
			case 0:
				try{
					String handshake=readBuffer(reader);
					System.out.println(handshake);
		  			/*StringTokenizer sc=new StringTokenizer(handshake,"'\"");
		  			if(!(sc.hasMoreTokens()&&sc.nextToken().equals("<stream:stream to=")))throw new IllegalArgumentException();
		  			sc.nextToken();//if(!sc.hasMoreTokens()||!sc.nextToken().matches("[/d]{0,3}[/.][/d]{0,3}[/.][/d]{0,3}"))throw new IllegalArgumentException();
		  			if(!sc.hasMoreTokens()||!sc.nextToken().equals(" xmlns="))throw new IllegalArgumentException();
		  			if(!sc.hasMoreTokens()||!sc.nextToken().matches("[j][a][b][b][e][r][:][^/d]*"))throw new IllegalArgumentException("Jabber: sintassi errata");
		  			if(!sc.hasMoreTokens()||!sc.nextToken().equals(" xmlns:stream="))throw new IllegalArgumentException();
		  			if(!sc.hasMoreTokens()||!sc.nextToken().equals("http://etherx.jabber.org/streams"))throw new IllegalArgumentException();
		  			if(!sc.hasMoreTokens()||!sc.nextToken().equals(" version="))throw new IllegalArgumentException();
		  			if(!sc.hasMoreTokens()||!sc.nextToken().equals("1.0"))throw new IllegalArgumentException();*/
		  			String handshake2="<stream:stream from='"+connection.hashCode()+"' xmlns='jabber:client' xmlns:stream='http://etherx.jabber.org/streams' version='1.0'>";
		  			writer.write(handshake2);
		  			writer.flush();
		  			String auth=readBuffer(reader);
		  			System.out.println(auth);
				}catch (IllegalArgumentException e){throw new IllegalArgumentException();
				}catch (Exception e) {}
			break;
			case 1:
				try{
					String xmlver=readBuffer(reader);
					System.out.println(xmlver);
					String handshake=readBuffer(reader);
					System.out.println(handshake);
					String starttls=readBuffer(reader);
					System.out.println(starttls);
					//String auth=readBuffer(reader);
					//System.out.println(auth);
				}catch(IOException e){System.err.println("IO");
				}catch(InterruptedException e){System.err.println("Interrupt");}
			break;
			}
			//voglio che l'utente mi dia il suo stato attuale salvato sul client
			userState="";
			while(!end){
				try {
					String challenge=readBuffer(reader);
				} catch (Exception e) {}
				int request=0;
				//scissione casi
				switch(request){
					case 1://inizio conversazione
						break;
					case 2://invio messaggio
						break;
					case 3://cambio stato
						String state="";//prendo stato da challenge
						setUserState(state);
				}
			}
			try {
				writer.close();
				reader.close();
				connection.close();
			} catch (Exception e) {System.err.println("errore chiusura connessione");System.exit(-1);}
		}
		
		/**
		 * Try to start a conversation with a user
		 * @param codeMaster quester hashcode
		 * @param codeSlave destination hashcode
		 * @throws UnsupportedOperationException conversation is already running or the destination is busy
		 */
		public void startConversation(int codeMaster, int codeSlave/*hash richiedente, hash destinatario*/){
			if(((ConnectionRunner)(connectionList.get(codeSlave))).getUserState().equals("busy")) throw new UnsupportedOperationException("User busy"); //caso in cui l'utente è nello stato 'don't break my balls'
			int aSquirrel=47;
			if(!conversationList.add(codeMaster*aSquirrel+codeSlave)) throw new UnsupportedOperationException("Conversation is already running");
		}
		
		/**
		 * Return the user state
		 * @return user state
		 */
		public String getUserState(){
			return userState;
		}
		
		/**
		 * Update the user state
		 * @param state new state
		 */
		public void setUserState(String state){
			userState=state;
		}

		@Override
		public int hashCode(){
			//TODO: ogni utente una sola connessione 
			return connection.hashCode();
		}
	}//Connection Runner END
	
	public static void main(String[] args) {
		ConnectionListener cl=new ConnectionListener();
		System.out.print("Server ");
		cl.start();
		System.out.println("on");
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
		StringBuilder sb=new StringBuilder(100);
		for (int j = 0; s[j]!='>'&&j<s.length-1; j++) {//ci pensa lo string tokenizer a vedere se hanno scritto cavolate
			sb.append(s[j]);
		}
		System.err.println("Indice i="+sb.length());
		sb.append('>');
		return sb.toString();
	}
}

