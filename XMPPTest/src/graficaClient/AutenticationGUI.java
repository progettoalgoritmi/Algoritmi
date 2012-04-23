package graficaClient;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

public class AutenticationGUI {
	
	public JFrame f=null;
	TextField username;
	JPasswordField pw;
	ImageIcon icona=new ImageIcon(".\\src\\graficaClient\\Images\\20133476748_8pzCx.jpg");
	String logo=".\\src\\graficaClient\\Images\\20133476748_8pzCx.jpg";
	
	public AutenticationGUI(){
		f=new JFrame();
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setSize(350,550);
		Dimension x= Toolkit.getDefaultToolkit().getScreenSize();
		f.setLocation(x.width/3, x.height/4);
		f.setTitle("Autentication");
		f.setIconImage(Toolkit.getDefaultToolkit().getImage(logo));
		JPanel pbase= new JPanel();
		pbase.setLayout(new BorderLayout());
		JLabel user= new JLabel("Username");
		JLabel lpw= new JLabel("Password");
		username= new TextField();
		username.setMaximumSize(new Dimension(180,20));
		pw= new JPasswordField();
		pw.setMaximumSize(new Dimension(180,20));
		
		Action connect= new AzioneBott("Connect");
		Action canc= new AzioneBott("Canc");
		JButton ok= new JButton(connect);
		JButton ko= new JButton(canc);
		
		JMenuBar mb= new JMenuBar();
		f.setJMenuBar(mb);
		JMenu file= new JMenu("File");
		JMenu help= new JMenu("Help");
		mb.add(file);
		mb.add(help);
		JLabel icon= new JLabel(icona);
		pbase.add(icon,BorderLayout.NORTH);				
		Box vet=Box.createVerticalBox();
		vet.add(Box.createVerticalStrut(80));		
		Box us= Box.createHorizontalBox();
		us.add(user);
		us.add(Box.createHorizontalStrut(20));
		us.add(username);
		vet.add(us);
		vet.add(Box.createVerticalStrut(10));
		Box bpw= Box.createHorizontalBox();
		bpw.add(lpw);
		bpw.add(Box.createHorizontalStrut(20));
		bpw.add(pw);
		vet.add(bpw);
		vet.add(Box.createVerticalStrut(10));
		Box bot= Box.createHorizontalBox();
		bot.add(ok);
		bot.add(Box.createHorizontalStrut(15));
		bot.add(ko);
		vet.add(bot);
		pbase.add(vet,BorderLayout.CENTER);
		f.add(pbase);
		f.setVisible(true);
	}
	
	private void ImpostaIcona(String x){
		int larghezza;
		int altezza;
		String image=".\\src\\graficaClient\\Images\\";
		image=image+x;
		ImageIcon prov= new ImageIcon (image);
		larghezza=prov.getIconWidth();
		altezza=prov.getIconHeight();
		if(larghezza>225&&altezza>225){
			//dobbiamo ridimensionare l'immagine
			
		}
	}

	public class AzioneBott extends AbstractAction{
		public AzioneBott(String nome){
			putValue(Action.NAME,nome);
			putValue(Action.SHORT_DESCRIPTION,"Click button to "+nome);
			}
		
		public void actionPerformed(ActionEvent e) {
			System.out.print(Action.NAME);
			
				username.setText(null);
				pw.setText(null);
			/**
			 * qui va la parte della connessione con else
			 * che per� non ho ancora fatto
			 */
		}
		
}
	
	public static void main (String args[]){
		AutenticationGUI s= new AutenticationGUI();
	}
}