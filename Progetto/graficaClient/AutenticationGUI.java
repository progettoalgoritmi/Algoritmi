package graficaClient;

import graficaClient.ClientGUI.Lingua;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import ClasseMix.*;

public class AutenticationGUI {
	
	JFrame f=null;
	TextField username;
	JPasswordField pw;
	String Icon="480192_640px.jpg";
	String logo="autenticatio_logo.png";
	ImageIcon accountIcona;
	JLabel icon;
	Lingua ln;
	
	public AutenticationGUI(){
		f=new JFrame();
		RomeoGraphicsUtility.miniIcona(f, logo);
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
			@SuppressWarnings("deprecation")
			public void keyPressed(KeyEvent e){
				if(e.getKeyCode()==KeyEvent.VK_ENTER) System.out.println("Pressed Enter");
				RomeoGraphicsUtility.connetti(username.getText(), pw.getText(), ln, Icon);
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
			@SuppressWarnings("deprecation")
			public void mouseClicked(MouseEvent e) {
				RomeoGraphicsUtility.connetti(username.getText(), pw.getText(), ln, Icon);
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
				String name= RomeoGraphicsUtility.setIcon(f, Icon);
				accountIcona=RomeoGraphicsUtility.impostaIcona(name,255,255);
				Icon=name;
				icon.setIcon(accountIcona);
				icon.repaint();
			}
		});
		
				JMenu options=new JMenu("Option");
				JMenuItem account= new JMenuItem("Set Account");
				
//Listener account
				JMenu lingua= new JMenu("Select Language");
				account.addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent e) {
						Option op= new Option();
					}
				});
				
//Listener Italiano
				JMenuItem ita= new JMenuItem("Italiano");
				ita.addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent e){
						Lingua x= Lingua.Italiano;
						RomeoGraphicsUtility.impostaLingua(x);
					}
				});
				
//Listener inglese
				JMenuItem ing= new JMenuItem("English");
				ing.addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent e) {
						Lingua x= Lingua.Inglese;
						RomeoGraphicsUtility.impostaLingua(x);
					}
				});
				
//Listener francese
				JMenuItem fra= new JMenuItem("Francais");
				fra.addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent e) {
						Lingua x= Lingua.Francese;
						RomeoGraphicsUtility.impostaLingua(x);
					}
				});

//Listener Spagnolo
				JMenuItem spa= new JMenuItem("Espanol");
				spa.addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent e) {
						Lingua x= Lingua.Spagnolo;
						RomeoGraphicsUtility.impostaLingua(x);
					}
				});
				
//Listener tedesco
				JMenuItem ger= new JMenuItem("Deutsch");
				ger.addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent e) {
						Lingua x= Lingua.Tedesco;
						RomeoGraphicsUtility.impostaLingua(x);
					}
				});
				
//Listener calabrese
				JMenuItem cal= new JMenuItem("Calabrese");
				cal.addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent e) {
						Lingua x= Lingua.Calabrese;
						RomeoGraphicsUtility.impostaLingua(x);
					}
				});
				
				lingua.add(ita);
				lingua.add(ing);
				lingua.add(fra);
				lingua.add(spa);
				lingua.add(ger);
				lingua.add(cal);
				options.add(lingua);
				options.add(imIcon);
				options.add(account);
							
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
			@SuppressWarnings("deprecation")
			public void actionPerformed(ActionEvent e){
				RomeoGraphicsUtility.connetti(username.getText(), pw.getText(), ln, Icon);
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
		
//Listener ReadMe
		JMenuItem reademe= new JMenuItem("Read Me!!!");
		reademe.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				RomeoGraphicsUtility.readMe();
			}
		});
		
//Listener about
		JMenuItem about= new JMenuItem("About");
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