package ExplosiveSheep;

import java.io.*;
import java.net.Socket;
import java.util.*;


	/**
	 * {@code ConnectionRunner} work as connection executor for each user try to connect.
	 * Used to start a thread to listen to the queries from the users.
	 */
public class ConnectionRunner extends Thread {
		private HashMap<Integer,ConnectionRunner> connectionList;
		private Socket connection;
		private StreamReaderThread sr;
		private ArrayList<Character> buff;
		private BufferedReader reader;
		private PrintWriter writer;
		private Lock warlock = new Lock();
		private String userState;
		private Window console;
		StreamReaderThread srt;
		boolean end=false;
		public ConnectionRunner(Socket socket,HashMap<Integer,ConnectionRunner> connectionList,Window console){
			connection=socket;
			this.connectionList=connectionList;
			this.console=console;
		}
		
		public void run() {
			//inizializzazione
			try {
				buff=new ArrayList<Character>(1000);
				reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
				srt=new StreamReaderThread(reader,buff,this.hashCode(),console,warlock);
				srt.start();
				writer = new PrintWriter(connection.getOutputStream());
			} catch (Exception e) {
				console.println("errore1:connection error");
			}
			console.println("no exception");
			//inizio autenticazione
			
						try {
							Thread.sleep(3000);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
			
			srt.clearBuffer();
			buff = srt.getBuffer();
			for(int i=0;i<buff.size();i++)System.out.print((srt.getBuffer().get(i)));
			System.out.println("Stampo cazzi.");
			System.out.println();
			//String xmlver=buffer
			int authType=1;//0=md5, 1=plain
			if(authType==0)console.println("MD5");else console.println("plain");
			switch(authType){
			case 0:
				
				
			break;
			case 1:
				
			break;
			}
			//voglio che l'utente mi dia il suo stato attuale salvato sul client
			userState="";
			while(!end){
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
						break;
				}
			}
		}
			
		
		
		/**
		 * Try to start a conversation with a user
		 * @param codeMaster quester hashcode
		 * @param codeSlave destination hashcode
		 * @throws UnsupportedOperationException conversation is already running or the destination is busy
		 */
		public void startConversation(int codeMaster, int codeSlave/*hash richiedente, hash destinatario*/){
			//if(((ConnectionRunner)(connectionList.get(codeSlave))).getUserState().equals("busy")) throw new UnsupportedOperationException("User busy"); //caso in cui l'utente è nello stato 'don't break my balls'
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
		
		public int getPort(){
			return connection.getPort();
		}
		
		public String getHost(){
			return connection.getInetAddress().getHostAddress();
		}
		
		public void send(String command) throws InterruptedException {
			warlock.waitOnLocks(100);
			warlock.hardLock();
			char[] c = command.toCharArray();
			for (int i = 0; i < c.length; i++) writer.write(c[i]);
			writer.flush();
			warlock.hardUnlock();
		}
}//Connection Runner END
