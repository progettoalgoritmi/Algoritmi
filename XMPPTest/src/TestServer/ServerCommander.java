package TestServer;
/*costruito interamente in base alle domande inviate dal client
 *abilmente scopiazzato di qua e di l�*/
import java.io.*;
import java.net.*;
import java.util.Scanner;

/*
	public void conversazione() {
		String message = "";
		BufferedReader t = new BufferedReader(new InputStreamReader(System.in));
		try {
			out.println("Connected! write /logout to log out ");
			while(!message.equals("/logout")) {
				message = readBuffer(reader);
				System.out.println(message);
				if(!message.equals("/logout")) {
					message = t.readLine();
					out.println(" scrive: "+message);
				}
			} // while
		}
		catch(IOException e) {
			System.out.println("End conversation");
		}
	} 
}*/
	

	public class ServerCommander implements Server{
		static char[] s = new char[10000];
		private static Socket connection;
		private static BufferedReader reader;
		private static PrintStream writer;
	  	public ServerCommander() throws IOException{
	  		//TODO
	  	}
	  	
		public static void main(String[] args) throws UnknownHostException, IOException, InterruptedException{
			//ServerCommander server=new ServerCommander(); //si richiamer� poi server.qualcosa per dare i comandi
			try{
				ServerSocket socket = new ServerSocket(5222);
				System.out.println("Server on");
				connection=socket.accept();//in teoria dovrebbe esserci sempre un serverSocket,magari su un altro thread,in attesa di un nuovo client che si vuole connettere,TODO
				System.out.println("Connection estabilished with: "+connection.getInetAddress().getHostAddress()+"\nport: "+connection.getPort());
				reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
				System.out.println("created reader");
				writer = new PrintStream(connection.getOutputStream());
				System.out.println("created writer");
		  	}catch(Exception e){System.err.println(e);}//avevo scritto System.orr di orrori ma non me l'ha preso :(
		  	System.out.println("no exeption");
		  	String handshake=readBuffer(reader);
		  	System.out.println(handshake);
		  	//if(!handshake.matches("[<stream:stream to='][.]+[' xmlns='jabber:client' xmlns:stream='http://etherx.jabber.org/streams' version='][.]+['>]")) throw new IllegalArgumentException();
		  	Scanner sc=new Scanner(handshake);
		  	sc.delimiter();
		  	//TODO
		  	
		  	while(true);//test
		  	/*writer.close();
			reader.close();
			connection.close();*/
	}
		
	private static String readBuffer(BufferedReader in) throws IOException, InterruptedException {
		s=new char[10000];
		int x=in.read(s, 0, s.length);
		StringBuilder sb=new StringBuilder();
		for (int j = 0; s[j]!='>'&&j<s.length; j++) {//ci pensa lo string tokenizer a vedere se hanno scritto cavolate
			sb.append(s[j]);
		}
		sb.append('>');
		return sb.toString();
	}
}
