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
		f.setLocation(x.width/3,x.height/4);//da me non si vede centrato @peppe TODO
		f.setTitle("Autentication");
		f.setIconImage(Toolkit.getDefaultToolkit().getImage(logo));
		JPanel pbase= new JPanel();
		pbase.setLayout(new BorderLayout());
		JLabel user= new JLabel("Username");
		JLabel lpw= new JLabel("Password");
		JLabel errorAuth= new JLabel("Username o Password errati. Riprova");
		errorAuth.setForeground(Color.RED);
		errorAuth.setVisible(false);
		username= new TextField();
		username.setMaximumSize(new Dimension(180,20));
		//Username Listener
		username.addKeyListener(new KeyListener(){
			public void keyPressed(KeyEvent arg0){
				if(arg0.getKeyCode()==KeyEvent.VK_ENTER) //se premi enter passa al frame della pw; io sbaglio questa cosa sempre per cui la implemento @peppe
					pw.requestFocus();
			}
			public void keyReleased(KeyEvent arg0){}public void keyTyped(KeyEvent arg0){}
		});//username listener
		
		pw= new JPasswordField();
		pw.setMaximumSize(new Dimension(180,20));
		//Pw Listener
		pw.addKeyListener(new KeyListener(){
			public void keyPressed(KeyEvent arg0){
				if(arg0.getKeyCode()==KeyEvent.VK_ENTER) System.out.println("Pressed Enter");
				//vedi "ok" listener 
			}
			public void keyReleased(KeyEvent arg0){}public void keyTyped(KeyEvent arg0){}
		});//Pw listener
		
		Action connect= new AzioneBott("Connect");
		Action canc= new AzioneBott("Canc");
		JButton ok= new JButton(connect);
		JButton ko= new JButton(canc);
		
		//Canc Listener
		ko.addMouseListener(new MouseListener(){
			public void mouseClicked(MouseEvent e) {
				System.exit(0);
			}
			public void mouseEntered(MouseEvent e) {}
			public void mouseExited(MouseEvent e) {}
			public void mousePressed(MouseEvent e) {}
			public void mouseReleased(MouseEvent e) {}
		});//canc listener
		//Connect Listener
		ok.addMouseListener(new MouseListener(){
			public void mouseClicked(MouseEvent e) {
				/*Guarda nel database se sono presenti user e pw:
				 * in caso affermativo genera un oggetto ClientGui 
				 * in caso negativo si riceve un messaggio di errore (possibilmente non con alert di java che fa cagare) dove si chiede di reinserire user e pw 
				 * connection(che non so in che classe sta) in caso di autenticazione fallita solleva una AuthenticationException
				 errorAuth.setVisible(false);
				 try{
				 		connection(username.toString(),pw.toString());
				 }catch(AuthenticationException e){
				  		errorAuth.setVisible(true);
				 }
				 */
				// TODO
			}
			public void mouseEntered(MouseEvent e) {}public void mouseExited(MouseEvent e) {}public void mousePressed(MouseEvent e) {}public void mouseReleased(MouseEvent e) {}
		});//connect listener
		
		JMenuBar mb= new JMenuBar();
		f.setJMenuBar(mb);
		JMenu file= new JMenu("File");
		JMenuItem options=new JMenuItem("Option");//deve settare true la visibilit� di Options
		file.add(new JSeparator());
		file.add(options);
		JMenu help= new JMenu("Help");
		mb.add(file);
		mb.add(help);
		JLabel icon= new JLabel(icona);
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
	 * Completo
	 */
	private ImageIcon impostaIcona(String x){
		ImageIcon icona;
		Image im,ima;
		String image=".\\src\\graficaClient\\Images\\";
		//image=image+x; questa parte poi si sblocca quando creo il metodo per impostarlo esternamente
		ImageIcon icon= new ImageIcon(x);
		im=icon.getImage();
		ima=im.getScaledInstance(255,255,Image.SCALE_AREA_AVERAGING);
		return icona= new ImageIcon(ima);
	}

	@SuppressWarnings("serial")
	public class AzioneBott extends AbstractAction{
		public AzioneBott(String nome){
			putValue(Action.NAME,nome);
			putValue(Action.SHORT_DESCRIPTION,"Click button to "+nome);
			}
		
		public void actionPerformed(ActionEvent e) {
			System.out.print(Action.NAME);//TEST
			
				username.setText(null);
				pw.setText(null);
			/**
			 * qui va la parte della connessione con else
			 * che per� non ho ancora fatto
			 */
		}
		
	}
	
	public static void main (String args[]){
		new AutenticationGUI();
	}
}