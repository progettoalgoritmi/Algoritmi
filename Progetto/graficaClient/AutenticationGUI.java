package graficaClient;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import javax.swing.*;

public class AutenticationGUI {
	
	JFrame f=null;
	TextField username;
	JPasswordField pw;
	String Icon="480192_640px.jpg";
	ImageIcon accountIcona;
	
	public AutenticationGUI(){
		f=new JFrame();
		MiniIcona();
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
		username= new TextField();
		username.setMaximumSize(new Dimension(180,20));
		
//Username Listener
		username.addKeyListener(new KeyListener(){
			public void keyPressed(KeyEvent e){
				if(e.getKeyCode()==KeyEvent.VK_ENTER)
					pw.requestFocus();
			}
			public void keyReleased(KeyEvent arg0){}
			public void keyTyped(KeyEvent arg0){}
		});
		
		pw= new JPasswordField();
		pw.setMaximumSize(new Dimension(180,20));
		
//Pw Listener
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
		JButton ok= new JButton(connect);
		JButton ko= new JButton(canc);
		
//Canc Listener
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
		JMenuItem imIcon= new JMenuItem("Icon");

//Listener Icon
		imIcon.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				JFileChooser jf= new JFileChooser();
				ExtensionFileFilter filtro= new ExtensionFileFilter();
				filtro.addExtension("jpg", false);
				filtro.addExtension("gif",false);
				filtro.addExtension("jpeg", false);
				filtro.setDescription("Image File");
				jf.setDialogTitle("Image");
				jf.setApproveButtonText("Select");
				jf.setFileFilter(filtro);
				jf.setMultiSelectionEnabled(false);
				jf.setCurrentDirectory(new File(".\\src\\graficaClient\\Images\\"));				
				int x=jf.showOpenDialog(f);
				if(x==JFileChooser.APPROVE_OPTION){
				String name=jf.getSelectedFile().getName();
				System.out.print(name);
				//accountIcona=AutenticationGUI.impostaIcona(name); questo si applica alla fine
				}
			}
		});
		
				JMenuItem options=new JMenuItem("Option");
				JMenuItem close= new JMenuItem("Close");
				
//Listener close
				close.addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent e) {
						f.setVisible(false);
					}
				});
			
		JMenuItem Connect=new JMenuItem("Connect");
		Connect.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				// Qui va richiamata la funzione per connettersi 
			}});
		
		JMenuItem Canc=new JMenuItem("Exit");
		
//Listener canc
		Canc.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}});
		
		
		file.add(Connect);
		file.add(new JSeparator());
		file.add(imIcon);
		file.add(options);
		file.add(new JSeparator());
		file.add(close);
		file.add(Canc);
		JMenu help= new JMenu("Help");
		JMenuItem reademe= new JMenuItem("Read Me!!!");
		JMenuItem about= new JMenuItem("About");
		help.add(reademe);
		help.add(about);
		mb.add(file);
		mb.add(help);
		JLabel icon= new JLabel(AutenticationGUI.impostaIcona(Icon));
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
	private static ImageIcon impostaIcona(String x){
		Image im,ima;
		String image=".\\src\\graficaClient\\Images\\";
		image=image+x;
		ImageIcon icon= new ImageIcon(image);
		im=icon.getImage();
		ima=im.getScaledInstance(255,255,Image.SCALE_AREA_AVERAGING);
		return new ImageIcon(ima);
	}

	private static void ImpostazioniDefaul(String lingua, String icona){}

	@SuppressWarnings("serial")
	public class AzioneBott extends AbstractAction{
		public AzioneBott(String nome){
			putValue(Action.NAME,nome);
			putValue(Action.SHORT_DESCRIPTION,"Click button to "+nome);
			}
		public void actionPerformed(ActionEvent e) {}	
	}
	
	private void MiniIcona(){
		SystemTray st=null;
		final TrayIcon ti;
		try{
		if(SystemTray.isSupported()==true){
			st=SystemTray.getSystemTray();
		Image imgg = Toolkit.getDefaultToolkit().getImage(".\\src\\graficaClient\\Images\\autenticatio_logo.png").getScaledInstance(15, 16,Image.SCALE_AREA_AVERAGING);
		 ti= new TrayIcon(imgg);
			final JPopupMenu pm= new JPopupMenu();
			JMenuItem apri= new JMenuItem("Open");
//Listener apri
			apri.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e) {
					f.setVisible(true);
				}
			});
			
			JMenuItem esci= new JMenuItem("Exit");
//Listener esci
			esci.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e) {
					 f.setVisible(false);
				     SystemTray.getSystemTray().remove(ti);
				     System.exit(0);
				}
				});
			
			JMenuItem nascondi= new JMenuItem("Ghost");
//Listener ghost
			nascondi.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e) {
					 f.setVisible(false);
				}
				});
			
			pm.add(apri);
			pm.add(nascondi);
			pm.add(new JSeparator());
			pm.add(esci);
			st.add(ti);
//Listener TrayIcon
			ti.addMouseListener(new MouseListener(){
					public void mouseClicked(MouseEvent e) {
						if(e.getButton()==1){
							if(f.isVisible()==true)
								f.setVisible(false);
							else
								f.setVisible(true);
						}else
							if(e.getButton()==3){
								pm.show(e.getComponent(), e.getX(), e.getY());
						      	}
					}
					public void mouseEntered(MouseEvent e) {}
					public void mouseExited(MouseEvent e) {}
					public void mousePressed(MouseEvent e) {}
					public void mouseReleased(MouseEvent e) {}
					});
			
		}
		}
		catch (AWTException e1) {
			System.err.print("Don't supported by OperatioSistem ");
		}
		}
		
	public static void main (String args[]){
		new AutenticationGUI();
	}
}