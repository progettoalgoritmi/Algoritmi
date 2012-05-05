package graficaClient;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import ClasseMix.*;

public class AutenticationGUI {
	
	JFrame f=null;
	TextField username;
	JPasswordField pw;
	String Icon="480192_640px.jpg";
	ImageIcon accountIcona;
	JLabel icon;
	
	public AutenticationGUI(){
		f=new JFrame();
		f=RomeoGraphicsUtility.miniIcona(f);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setSize(350,550);
		Dimension x= Toolkit.getDefaultToolkit().getScreenSize();
		f.setLocation(x.width/3,x.height/4);
		f.setTitle("Autentication");
		f.setIconImage(Toolkit.getDefaultToolkit().getImage(".\\src\\graficaClient\\Images\\20133476748_8pzCx.jpg"));
		JPanel pbase= new JPanel();
		pbase.setLayout(new BorderLayout());
		JLabel user= new JLabel("Username");
		JLabel lpw= new JLabel("Password");
		JLabel errorAuth= new JLabel("Username o Password errati. Riprova");
		errorAuth.setForeground(Color.RED);
		errorAuth.setVisible(false);
		
//Username Listener
		username= new TextField();
		username.setMaximumSize(new Dimension(180,20));
		username.addKeyListener(new KeyListener(){
			public void keyPressed(KeyEvent e){
				if(e.getKeyCode()==KeyEvent.VK_ENTER)
					pw.requestFocus();
			}
			public void keyReleased(KeyEvent arg0){}
			public void keyTyped(KeyEvent arg0){}
		});
		
//Listener Password
		pw= new JPasswordField();
		pw.setMaximumSize(new Dimension(180,20));
		pw.addKeyListener(new KeyListener(){
			public void keyPressed(KeyEvent e){
				if(e.getKeyCode()==KeyEvent.VK_ENTER) System.out.println("Pressed Enter");
				//Qui poi va la parte di connessione
			}
			public void keyReleased(KeyEvent arg0){}
			public void keyTyped(KeyEvent arg0){}
		});
		
		Action connect= new AzioneBott("Connect");
		Action canc= new AzioneBott("Canc");
		
//Listener Canc
		JButton ko= new JButton(canc);
		ko.addMouseListener(new MouseListener(){
			public void mouseClicked(MouseEvent e) {
				username.setText(null);
				pw.setText(null);
			}
			public void mouseEntered(MouseEvent e) {}
			public void mouseExited(MouseEvent e) {}
			public void mousePressed(MouseEvent e) {}
			public void mouseReleased(MouseEvent e) {}
		});
		
//Connect Listener
		JButton ok= new JButton(connect);
		ok.addMouseListener(new MouseListener(){
			public void mouseClicked(MouseEvent e) {
				/*Guarda nel database se sono presenti user e pw:
				 * in caso affermativo genera un oggetto ClientGui 
				 * in caso negativo si riceve un messaggio di errore (possibilmente non con alert di java che fa cagare) dove si chiede di reinserire user e pw 
				 * connection(che non so in che classe sta) in caso di autenticazione fallita solleva una AuthenticationException
				 errorAuth.setVisible(false);
				 Un'altra cosa da fare è quella di settare a Icon il nome dell'icona che ha impostato l'utente
				 nell'ultima connessione perchè sennò riaprendo gli comparirà sempre il logo di defaul
				 try{
				 		connection(username.toString(),pw.toString());
				 }catch(AuthenticationException e){
				  		errorAuth.setVisible(true);
				 }
				 */
			}
			
			public void mouseEntered(MouseEvent e){}
			public void mouseExited(MouseEvent e){}
			public void mousePressed(MouseEvent e){}
			public void mouseReleased(MouseEvent e){}
		});
		
		JMenuBar mb= new JMenuBar();
		f.setJMenuBar(mb);
		JMenu file= new JMenu("File");

//Listener Icon
		JMenuItem imIcon= new JMenuItem("Set Icon");
		imIcon.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				String name= RomeoGraphicsUtility.setIcon(f);
				accountIcona=RomeoGraphicsUtility.impostaIcona(name,255,255);
				icon.setIcon(accountIcona);
				icon.repaint();
			}
		});
		
				JMenu options=new JMenu("Option");
				JMenuItem account= new JMenuItem("Set Account");
				JMenuItem lingua= new JMenuItem("Select Language");
				options.add(lingua);
				options.add(imIcon);
				options.add(account);
				
//Listener account
				account.addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent e) {
						Option op= new Option();
					}
				});
							
//Listener close
				JMenuItem close= new JMenuItem("Close");
				close.addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent e) {
						f.setVisible(false);
					}
				});
			
//Listener Connect
		JMenuItem Connect=new JMenuItem("Connect");
		Connect.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				// Qui va richiamata la funzione per connettersi 
			}});
				
//Listener canc
		JMenuItem Canc=new JMenuItem("Exit");
		Canc.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}});
		
		file.add(Connect);
		file.add(new JSeparator());
		file.add(options);
		file.add(new JSeparator());
		file.add(close);
		file.add(Canc);
		JMenu help= new JMenu("Help");
		JMenuItem reademe= new JMenuItem("Read Me!!!");
		
//Listener ReadMe
		reademe.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				RomeoGraphicsUtility.readMe();
			}
		});
		
		JMenuItem about= new JMenuItem("About");
		
//Listener about
		about.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				RomeoGraphicsUtility.aboutProg();
			}
		});
		
		help.add(reademe);
		help.add(about);
		mb.add(file);
		mb.add(help);
		icon= new JLabel(RomeoGraphicsUtility.impostaIcona(Icon));
		pbase.add(icon,BorderLayout.NORTH);				
		Box vet=Box.createVerticalBox();
		vet.add(Box.createVerticalStrut(80));	
		
//box username
		Box us= Box.createHorizontalBox();
		us.add(user);
		us.add(Box.createHorizontalStrut(20));
		us.add(username);
		vet.add(us);
		vet.add(Box.createVerticalStrut(10));
		
//box pw
		Box bpw= Box.createHorizontalBox();
		bpw.add(lpw);
		bpw.add(Box.createHorizontalStrut(20));
		bpw.add(pw);
		vet.add(bpw);
		
//box errore login
		Box boxError=Box.createHorizontalBox();
		boxError.add(errorAuth);
		vet.add(boxError);
		vet.add(Box.createVerticalStrut(10));
		
//box bottoni
		Box bot= Box.createHorizontalBox();
		bot.add(ok);
		bot.add(Box.createHorizontalStrut(15));
		bot.add(ko);
		vet.add(bot);
		pbase.add(vet,BorderLayout.CENTER);
		f.add(pbase);
		f.setVisible(true);
	}
	
	/**
	 * Questo metodo ci ridimensiona le immagini scelte per l'account
	 * in modo tale da non sfasare tutti gli altri componenti del frame
	 */

	@SuppressWarnings("serial")
	public class AzioneBott extends AbstractAction{
		public AzioneBott(String nome){
			putValue(Action.NAME,nome);
			putValue(Action.SHORT_DESCRIPTION,"Click button to "+nome);
			}
		public void actionPerformed(ActionEvent e) {}	
	}
	
	public static void main (String args[]){
		new AutenticationGUI();
	}
}