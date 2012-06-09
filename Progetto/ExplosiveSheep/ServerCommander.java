package ExplosiveSheep;

import java.net.*;
import java.util.*;

	public class ServerCommander implements Server{
		static public Window console=new Window();
		
	
	public static void main(String[] args) {
		ConnectionListener cl=new ConnectionListener(console);
		console.print("Server ");
		cl.start();
		console.println("on");
	}
		
}

