package graficaClient;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import javax.swing.*;
import javax.swing.filechooser.FileFilter;

public class ClientGUI {
	JFrame f;
	int count;
	String logo="20133476748_8pzCx.jpg";
	String s="images.jpg";
	String t="download.jpg";
	String w="penna.jpg";
	String r="file.jpg";
	String g="foto.jpg";
	String k="chat.jpg";
	String iconatipo=logo;
	ImageIcon accountIcona;
	TextArea legge;
	TextArea scrive;
	JTabbedPane centro;
	JTabbedPane sud;
	public enum Lingua{Italiano, Francese, Inglese, Spagnolo, Tedesco, Calabrese}
	public enum Contatto{}
	public enum Stato{}
	public enum Chiudi{}
	public enum Esci{}
	public enum Contatti{}
	public enum Action{}
	public enum Inviaimmagine{}
	public enum InviaFile{}
	public enum Opzioni{}
	public enum Setlingua{}
	public enum Seticona{}
	public enum Setpassw{}
	public enum Bottoneok{}
	public enum Bottoneko{}
	
	//Questa e' l'icona dell'account ogni volta che si chiude la connessione la facciamo aggiornare sul database
	
	public ClientGUI(){		
		//(String lingua, String ico){ questo si aggiunge alla fine quando poi gli passiamo i valori dal frame
		// precedenti nel caso in cui vengono impostati inizialmente
		f= new JFrame();
		MiniIcona();
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Dimension x= Toolkit.getDefaultToolkit().getScreenSize();
		f.setLocation(x.width/4,x.height/9);
		f.setSize(650,650);
		f.setTitle("Chat Client");
		f.setIconImage(Toolkit.getDefaultToolkit().getImage(".\\src\\graficaClient\\Images\\20133476748_8pzCx.jpg"));
		f.setLayout(new BorderLayout());
		JMenuBar mb3= new JMenuBar();
		f.setJMenuBar(mb3);
		JMenu file= new JMenu("File");
		JMenu option= new JMenu("Option");
		JMenu action= new JMenu("Action");
		JMenu contact= new JMenu("Contacts");
		mb3.add(file);
		mb3.add(contact);
		mb3.add(action);
		mb3.add(option);

//Listener esci
		JMenuItem esci= new JMenuItem("Exit");
		esci.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});

//Listener close
		JMenuItem chiudi= new JMenuItem("Close");
		chiudi.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				f.setVisible(false);
			}
		});
		
		JMenuItem account= new JMenuItem("Account");
		JMenu lingua= new JMenu("Select Language");
		JMenuItem ita= new JMenuItem("Italiano");
		
//Listener Italiano
		ita.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				Lingua x= Lingua.Italiano;
				impostaLingua(x);
			}
		});
		
//Listener inglese
		JMenuItem ing= new JMenuItem("English");
		ing.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				Lingua x= Lingua.Inglese;
				impostaLingua(x);
			}
		});
		
//Listener francese
		JMenuItem fra= new JMenuItem("Francais");
		fra.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				Lingua x= Lingua.Francese;
				impostaLingua(x);
			}
		});

//Listener Spagnolo
		JMenuItem spa= new JMenuItem("Espanol");
		spa.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				Lingua x= Lingua.Spagnolo;
				impostaLingua(x);
			}
		});
		
//Listener tedesco
		JMenuItem ger= new JMenuItem("Deutsch");
		ger.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				Lingua x= Lingua.Tedesco;
				impostaLingua(x);
			}
		});
		
//Listener calabrese
		JMenuItem cal= new JMenuItem("Calabrese");
		cal.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				Lingua x= Lingua.Calabrese;
				impostaLingua(x);
			}
		});
		
		lingua.add(ita);
		lingua.add(ing);
		lingua.add(fra);
		lingua.add(spa);
		lingua.add(ger);
		lingua.add(cal);
		JMenuItem pass= new JMenuItem("Change Password");
		JMenuItem about= new JMenuItem("About");
		
//Listener Select Icon
		JMenuItem icona= new JMenuItem("Select Icon");
		icona.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				JFileChooser jf= new JFileChooser();
				jf.setMultiSelectionEnabled(false);
				jf.setCurrentDirectory(new File(".\\src\\graficaClient\\Images\\"));
				ExtensionFileFilter filtro= new ExtensionFileFilter();
				filtro.addExtension("jpg", false);
				filtro.addExtension("gif",false);
				filtro.addExtension("jpeg", false);
				filtro.setDescription("Image File");
				jf.setDialogTitle("Icon");
				jf.setApproveButtonText("Select");
				jf.setFileFilter(filtro);
				
				int x=jf.showOpenDialog(f);
				if(x==JFileChooser.APPROVE_OPTION){
				String name=jf.getSelectedFile().getName();
				System.out.print(name);
				//accountIcona=RomeoGraphicsUtility.impostaIcona(name, 255, 255); questo si applica alla fine
				}
			}
		});
		
		JMenu stato= new JMenu("State");
		JMenuItem lin= new JMenuItem("Online");
		JMenuItem inv= new JMenuItem("Invisible");
		JMenuItem occ= new JMenuItem("Don't break my balls");
		JMenuItem aggcontact= new JMenuItem("New Contact");
		stato.add(lin);
		stato.add(inv);
		stato.add(occ);
		option.add(about);
		option.add(new JSeparator());
		option.add(account);
		option.add(pass);
		option.add(lingua);
		option.add(icona);
		file.add(aggcontact);
		file.add(stato);
		file.add(new JSeparator());
		file.add(chiudi);
		file.add(esci);
		
//Listener Send Image
		JMenuItem send1= new JMenuItem("Send Image");
		send1.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				JFileChooser jf= new JFileChooser();
				jf.setMultiSelectionEnabled(false);
				jf.setCurrentDirectory(new File(""));
				ExtensionFileFilter filtro= new ExtensionFileFilter();
				filtro.addExtension("jpg", false);
				filtro.addExtension("gif",false);
				filtro.addExtension("jpeg", false);
				filtro.setDescription("Image File");
				jf.setDialogTitle("Image");
				jf.setApproveButtonText("Select");
				jf.setFileFilter(filtro);
				int x=jf.showOpenDialog(f);
				if(x==JFileChooser.APPROVE_OPTION){
				String name=jf.getSelectedFile().getName();
				System.out.print(name);
				//Qui poi va la parte dell'invio dell'immagine
				}
			}
		});
		
//Listener Send File
		JMenuItem send2= new JMenuItem("Send File");
		send2.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				JFileChooser jf= new JFileChooser();
				jf.setMultiSelectionEnabled(false);
				jf.setCurrentDirectory(new File(""));
				jf.setDialogTitle("Image");
				jf.setApproveButtonText("Select");
				int x=jf.showOpenDialog(f);
				if(x==JFileChooser.APPROVE_OPTION){
				String name=jf.getSelectedFile().getName();
				System.out.print(name);
				//Qui invece va la parte dell'invio del file di qualsiasi tipo
				}
			}
		});
		
		action.add(send1);
		action.add(send2);		
		sud= new JTabbedPane(JTabbedPane.TOP);
		scrive= new TextArea(null,0,0,TextArea.SCROLLBARS_VERTICAL_ONLY);
		scrive.setEditable(true);
	
//Listener scrivi
		scrive.addKeyListener(new KeyListener(){
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode()== KeyEvent.VK_ENTER){
					invio();
				}
			}
			public void keyReleased(KeyEvent e) {}
			public void keyTyped(KeyEvent e) {}
			});

		Icon smile= ClientGUI.impostaIcona(s,30,30);
		Icon font= ClientGUI.impostaIcona(t,30,30);
		Icon write= ClientGUI.impostaIcona(w, 30,30);
		Icon rar= ClientGUI.impostaIcona(r, 30, 30);
		Icon img= ClientGUI.impostaIcona(g, 30, 30);
		Icon chat= ClientGUI.impostaIcona(k,30,30);
		JPanel panelsmile= new JPanel();
		JPanel panelfont= new JPanel();
		JPanel panelwrite= new JPanel();
		JPanel panelrar= new JPanel();
		JPanel panelimg= new JPanel();
		Box vet= Box.createVerticalBox();
		
//Listener Send
		JButton invio= new JButton("Send");
		invio.addMouseListener(new MouseListener(){
			public void mouseClicked(MouseEvent e) {
				invio();
				scrive.requestFocus();
			}
			public void mouseEntered(MouseEvent e) {}
			public void mouseExited(MouseEvent e) {}
			public void mousePressed(MouseEvent e) {}
			public void mouseReleased(MouseEvent e) {}
			
		});
		
//Listener Canc
		JButton canc= new JButton("Canc");
		canc.addMouseListener(new MouseListener(){
			public void mouseClicked(MouseEvent e) {
				scrive.setText(null);
			}
			public void mouseEntered(MouseEvent e) {}
			public void mouseExited(MouseEvent e) {}
			public void mousePressed(MouseEvent e) {}
			public void mouseReleased(MouseEvent e) {}
			
		});
				
		vet.add(invio);
		vet.add(Box.createVerticalStrut(20));
		vet.add(canc);
		Box ozz= Box.createHorizontalBox();
		ozz.add(scrive);
		ozz.add(Box.createHorizontalStrut(10));
		ozz.add(vet);
		sud.add(ozz);
		sud.setTitleAt(0,"Chat");
		sud.setIconAt(0, chat);
		sud.add(panelsmile);
		sud.setIconAt(1, smile);
		sud.add(panelfont);
		sud.setIconAt(2, font);
		sud.add(panelwrite);
		sud.setIconAt(3, write);
		sud.add(panelrar);
		sud.setIconAt(4, rar);
		sud.add(panelimg);
		sud.setIconAt(5, img);
		centro= new JTabbedPane(JTabbedPane.TOP);	
		Box oz= Box.createHorizontalBox();
		Box vet1= Box.createVerticalBox();
		legge= new TextArea(null,0,0,TextArea.SCROLLBARS_VERTICAL_ONLY);
		legge.setEditable(false);
		oz.add(legge);
		oz.add(Box.createHorizontalStrut(10));
		JLabel icon= new JLabel(ClientGUI.impostaIcona(iconatipo, 145, 145));
		vet1.add(icon);
		vet1.add(Box.createVerticalGlue());
		oz.add(vet1);
		centro.add(oz);
		f.add(sud, BorderLayout.SOUTH);
		f.add(centro,BorderLayout.CENTER);
		legge.setVisible(true);
		scrive.setVisible(true);
		f.setVisible(true);
	}
	
	private static ImageIcon impostaIcona(String x, int l, int h){
		Image im,ima;
		String image=".\\src\\graficaClient\\Images\\";
		image=image+x;
		ImageIcon icon= new ImageIcon(image);
		im=icon.getImage();
		ima=im.getScaledInstance(l,h,Image.SCALE_AREA_AVERAGING);
		return new ImageIcon(ima);
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
			
//Listener nascondi
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
	
	private void invio(){
		String x=scrive.getText();
		if(x.equals("\n"))
			return;
		// passo x all'oggetto per inviare il testo
		//String y= nome utente: a capo x
		String y= legge.getText();
		y=y+x+"\n";
		legge.setText(y);
		scrive.setText(null);
	}
	
	private void nomeTray(){
		//String n= oggetto della connessione.getNome();
		//faccio na variabile che viene incrementata ogni volta che si clicca per una nuova chat
		//e viene decrementata quando si chiude
		//centro.setTitleAt(count, n);
	}
	
	private void impostaLingua(Lingua x){
		switch (x){
		case Italiano:
			
		}
	  }
	
public static void main(String args[]){
	ClientGUI p=new ClientGUI();

}
}