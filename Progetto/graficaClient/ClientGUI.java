package graficaClient;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import javax.swing.*;

import ClasseMix.*;

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
	String trl="trillo.jpg";
	String Icon="480192_640px.jpg";
	String online="online.jpg";
	String occupato="occupato.jpg";
	String invisibile="invisibile.jpg";
	String uomo="uomo.jpg";
	String donna="donna.jpg";
	String logo1="autenticatio_logo.png";
	String iconaaccount=online;
	ImageIcon accountIcona;
	TextArea legge;
	TextField jtf;
	TextField jtf1;
	TextArea scrive;
	JTabbedPane centro;
	JTabbedPane sud;
	SystemTray st;
	JMenu contact;
	TrayIcon ti;
	static int cont=0;
	public enum Lingua{Italiano, Francese, Inglese, Spagnolo, Tedesco, Calabrese}
		
	public ClientGUI(Lingua ln, String Ico){
		if(Ico!=null)
			Icon=Ico;
		f= new JFrame();
		
		try{
			if(SystemTray.isSupported()==true){
				st=SystemTray.getSystemTray();
			Image imgg = Toolkit.getDefaultToolkit().getImage(".\\src\\graficaClient\\Images\\"+logo1).getScaledInstance(15, 16,Image.SCALE_AREA_AVERAGING);
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
		contact= new JMenu("Contacts");
		mb3.add(file);
		mb3.add(contact);
		mb3.add(action);
		mb3.add(option);
		centro= new JTabbedPane(JTabbedPane.TOP);
		
//Listener esci
		JMenuItem esci= new JMenuItem("Exit");
		esci.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				RomeoGraphicsUtility.disconnetti();
			}
		});

//Listener close
		JMenuItem chiudi= new JMenuItem("Close");
		chiudi.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				f.setVisible(false);
			}
		});
		
//Listener account
		JMenuItem account= new JMenuItem("Set Account");
		account.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				Option op= new Option();
			}			
			});
		
		JMenu lingua= new JMenu("Select Language");
				
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
		JMenuItem pass= new JMenuItem("Change Password");
		
//Listener about
		JMenuItem about= new JMenuItem("About");
		about.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				RomeoGraphicsUtility.aboutProg();
			}
		});
		
//Listener Select Icon
		JMenuItem icona= new JMenuItem("Set Icon");
		icona.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				String name= RomeoGraphicsUtility.setIcon(f, Icon);
				accountIcona=RomeoGraphicsUtility.impostaIcona(name,255,255);
				Icon=name;
				}
			});
		
		JMenu stato= new JMenu("State");
		
		
		
//Listener Online
		JMenuItem lin= new JMenuItem("Online");
		lin.setIcon(RomeoGraphicsUtility.impostaIcona(online, 15, 15));
		lin.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				st.remove(ti);
				ti.setImage(Toolkit.getDefaultToolkit().getImage(".\\src\\graficaClient\\Images\\"+online).getScaledInstance(15, 16,Image.SCALE_AREA_AVERAGING));
				try {
					st.add(ti);
				} catch (AWTException e1) {
					e1.printStackTrace();
				}
			}	
		});
		
//Listener invisibile
		JMenuItem inv= new JMenuItem("Invisible");
		inv.setIcon(RomeoGraphicsUtility.impostaIcona(invisibile, 15, 15));
		inv.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				st.remove(ti);
				ti.setImage(Toolkit.getDefaultToolkit().getImage(".\\src\\graficaClient\\Images\\"+invisibile).getScaledInstance(15, 16,Image.SCALE_AREA_AVERAGING));
				try {
					st.add(ti);
				} catch (AWTException e1) {
					e1.printStackTrace();
				}
			}	
		});
		
//Listener occ
		JMenuItem occ= new JMenuItem("Do not Disturbe");
		occ.setIcon(RomeoGraphicsUtility.impostaIcona(occupato, 15, 15));
		occ.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				st.remove(ti);
				ti.setImage(Toolkit.getDefaultToolkit().getImage(".\\src\\graficaClient\\Images\\"+occupato).getScaledInstance(15, 16,Image.SCALE_AREA_AVERAGING));
				try {
					st.add(ti);
				} catch (AWTException e1) {
					e1.printStackTrace();
				}
			}	
		});
		
//Listener New Contact
		JMenuItem cont1= new JMenuItem("New Contact");
		cont1.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				nuovoContatto();
			}
		});
		
		contact.add(cont1);
		contact.add(new JSeparator());
		
		JMenuItem aggcontact= new JMenuItem("New Account");
		
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
				String name=RomeoGraphicsUtility.setImage(f);
				RomeoGraphicsUtility.invioDati(name);
			}
		});
		
//Listener Send File
		JMenuItem send2= new JMenuItem("Send File");
		send2.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				String name=RomeoGraphicsUtility.setFile(f);
				RomeoGraphicsUtility.invioDati(name);
			}
		});
		
		action.add(send1);
		action.add(send2);		
		sud= new JTabbedPane(JTabbedPane.TOP);
		
//Listener scrivi
		scrive= new TextArea(null,0,0,TextArea.SCROLLBARS_VERTICAL_ONLY);
		scrive.setEditable(true);
		scrive.addKeyListener(new KeyListener(){
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode()== KeyEvent.VK_ENTER){
					RomeoGraphicsUtility.invio(scrive,legge);
				}
			}
			public void keyReleased(KeyEvent e) {}
			public void keyTyped(KeyEvent e) {}
			});

		Icon smile= RomeoGraphicsUtility.impostaIcona(s,30,30);
		Icon font= RomeoGraphicsUtility.impostaIcona(t,30,30);
		Icon write= RomeoGraphicsUtility.impostaIcona(w, 30,30);
		Icon rar= RomeoGraphicsUtility.impostaIcona(r, 30, 30);
		Icon img= RomeoGraphicsUtility.impostaIcona(g, 30, 30);
		Icon chat= RomeoGraphicsUtility.impostaIcona(k,30,30);
		Icon trill= RomeoGraphicsUtility.impostaIcona(trl, 30, 30);
		JPanel panelsmile= new JPanel();
		JPanel panelfont= new JPanel();
		JPanel panelwrite= new JPanel();
		JPanel panelrar= new JPanel();
		JPanel panelimg= new JPanel();
		JPanel paneltril= new JPanel();
		paneltril.setVisible(false);
		
		jtf1= new TextField();
		jtf1.setPreferredSize(new Dimension(350,20));
		JButton jtfb1= new JButton("Seach");
		JButton jtfbok1= new JButton("Ok");
		JLabel ind1= new JLabel("Insert");
		JLabel title1= new JLabel("Send a Image");
		Box orz9= Box.createHorizontalBox();
		orz9.add(ind1);
		orz9.add(Box.createHorizontalStrut(10));
		orz9.add(jtf1);
		Box orz3= Box.createHorizontalBox();
		orz3.add(jtfb1);
		orz3.add(Box.createHorizontalStrut(30));
		orz3.add(jtfbok1);
		Box orz11= Box.createVerticalBox();
		orz11.add(title1);
		orz11.add(Box.createVerticalStrut(30));
		orz11.add(orz9);
		orz11.add(Box.createVerticalStrut(15));
		orz11.add(orz3);

//Listener TextField
		jtf1.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				String x= jtf1.getText();
				RomeoGraphicsUtility.invioDati(x);
			}
		});

//Listener cerca
		jtfb1.addMouseListener(new MouseListener(){
			public void mouseClicked(MouseEvent e) {
				jtf1.setText(RomeoGraphicsUtility.setFile(f));
			}
			public void mouseEntered(MouseEvent e) {}
			public void mouseExited(MouseEvent e) {}
			public void mousePressed(MouseEvent e) {}
			public void mouseReleased(MouseEvent e) {}
		});

//Listener Ok
		jtfbok1.addMouseListener(new MouseListener(){
			public void mouseClicked(MouseEvent e) {
				String x= jtf1.getText();
				RomeoGraphicsUtility.invioDati(x);
			}
			public void mouseEntered(MouseEvent e) {}
			public void mouseExited(MouseEvent e) {}
			public void mousePressed(MouseEvent e) {}
			public void mouseReleased(MouseEvent e) {}
		});
		panelimg.add(orz11);
				
		jtf= new TextField();
		jtf.setPreferredSize(new Dimension(350,20));
		JButton jtfb= new JButton("Seach");
		JButton jtfbok= new JButton("Ok");
		JLabel ind= new JLabel("Insert");
		JLabel title= new JLabel("Send a File");
		Box orz= Box.createHorizontalBox();
		orz.add(ind);
		orz.add(Box.createHorizontalStrut(10));
		orz.add(jtf);
		Box orz2= Box.createHorizontalBox();
		orz2.add(jtfb);
		orz2.add(Box.createHorizontalStrut(30));
		orz2.add(jtfbok);
		Box orz1= Box.createVerticalBox();
		orz1.add(title);
		orz1.add(Box.createVerticalStrut(30));
		orz1.add(orz);
		orz1.add(Box.createVerticalStrut(15));
		orz1.add(orz2);
		
//Listener TextField
		jtf.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				String x= jtf.getText();
				RomeoGraphicsUtility.invioDati(x);
			}
		});
		
//Listener cerca
		jtfb.addMouseListener(new MouseListener(){
			public void mouseClicked(MouseEvent e) {
				jtf.setText(RomeoGraphicsUtility.setFile(f));
			}
			public void mouseEntered(MouseEvent e) {}
			public void mouseExited(MouseEvent e) {}
			public void mousePressed(MouseEvent e) {}
			public void mouseReleased(MouseEvent e) {}
		});
		
//Listener Ok
		jtfbok.addMouseListener(new MouseListener(){
			public void mouseClicked(MouseEvent e) {
				String x= jtf.getText();
				RomeoGraphicsUtility.invioDati(x);
			}
			public void mouseEntered(MouseEvent e) {}
			public void mouseExited(MouseEvent e) {}
			public void mousePressed(MouseEvent e) {}
			public void mouseReleased(MouseEvent e) {}
		});
		panelrar.add(orz1);
				
		
		Box vet= Box.createVerticalBox();
		
//Listener Send
		JButton invio= new JButton("Send");
		invio.addMouseListener(new MouseListener(){
			public void mouseClicked(MouseEvent e) {
				RomeoGraphicsUtility.invio(scrive,legge);
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
		vet.add(Box.createVerticalStrut(15));
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
		sud.add(paneltril);
		sud.setIconAt(2,trill);
		sud.add(panelfont);
		sud.setIconAt(3, font);
		sud.add(panelwrite);
		sud.setIconAt(4, write);
		sud.add(panelrar);
		sud.setIconAt(5, rar);
		sud.add(panelimg);
		sud.setIconAt(6, img);		
		f.add(sud, BorderLayout.SOUTH);
		f.add(centro,BorderLayout.CENTER);
		scrive.setVisible(true);
		f.setVisible(true);
	}
		
	public void nuovoContatto(){
		final JFrame pl= new JFrame();
		pl.setTitle("Add Contact");
		Dimension x= Toolkit.getDefaultToolkit().getScreenSize();
		pl.setLocation(x.width/4,x.height/9);
		pl.setSize(250, 300);
		pl.setIconImage(Toolkit.getDefaultToolkit().getImage(".\\src\\graficaClient\\Images\\20133476748_8pzCx.jpg"));
		pl.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		Box vet= Box.createVerticalBox();
		Box oz= Box.createHorizontalBox();
		Box oz1= Box.createHorizontalBox();
		Box oz2= Box.createHorizontalBox();
		final JTextField fd= new JTextField();
		JLabel lb= new JLabel("Username");
		fd.setMaximumSize(new Dimension(350,20));
		ButtonGroup group= new ButtonGroup();
		final JRadioButton men= new JRadioButton("Men", false);
		final JRadioButton woman= new JRadioButton("Woman", false);
		group.add(men);
		group.add(woman);
		oz1.add(men);
		oz1.add(Box.createHorizontalStrut(20));
		oz1.add(woman);
		oz.add(lb);
		oz.add(Box.createHorizontalStrut(10));
		oz.add(fd);
		vet.add(oz);
		vet.add(Box.createVerticalStrut(20));
		vet.add(oz1);
		JButton ok= new JButton("Ok");
		JButton ko= new JButton("Canc");
		
//Mouse Listener ok
		ok.addMouseListener(new MouseListener(){
			public void mouseClicked(MouseEvent e) {
				final String nome=fd.getText();
				if(nome.length()!=0){
				String sesso = null;
				JMenuItem ct=null;
				if(men.isSelected()){
					sesso="uomo.jpg";
				}else if(woman.isSelected())
					sesso="donna.jpg";
				else if(men.isSelected()==false&&woman.isSelected()==false)
					sesso="uomo.jpg";
				
				final Icon icn= RomeoGraphicsUtility.impostaIcona(sesso, 15, 15);
				ct= new JMenuItem(nome, icn);
				
//Listener contatto
				ct.addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent e){
						Box oz= Box.createHorizontalBox();
						Box vet1= Box.createVerticalBox();
						legge= new TextArea(null,0,0,TextArea.SCROLLBARS_VERTICAL_ONLY);
						legge.setEditable(false);
						oz.add(legge);
						oz.add(Box.createHorizontalStrut(10));
						JLabel icon= new JLabel(RomeoGraphicsUtility.impostaIcona(iconatipo, 145, 145));
				
//Listener icona nome
						icon.addMouseListener(new MouseListener(){
							public void mouseClicked(MouseEvent e) {
								if(e.getButton()==2){
									
								}	
							}
							public void mouseEntered(MouseEvent e) {}
							public void mouseExited(MouseEvent e) {}
							public void mousePressed(MouseEvent e) {}
							public void mouseReleased(MouseEvent e) {}
						});
						
						vet1.add(icon);
						vet1.add(Box.createVerticalGlue());
						oz.add(vet1);
						centro.add(oz);
						centro.setTitleAt(cont, nome);
						centro.setIconAt(cont, icn);
						cont++;
						legge.setVisible(true);
						centro.repaint();
					}
				});
				
				contact.add(ct);
				contact.repaint();
				pl.setVisible(false);
				}
			}
			public void mouseEntered(MouseEvent e) {}
			public void mouseExited(MouseEvent e) {}
			public void mousePressed(MouseEvent e) {}
			public void mouseReleased(MouseEvent e) {}
		});
		
		ko.addMouseListener(new MouseListener(){
			public void mouseClicked(MouseEvent e) {
				pl.setVisible(false);
			}
			public void mouseEntered(MouseEvent e) {}
			public void mouseExited(MouseEvent e) {}
			public void mousePressed(MouseEvent e) {}
			public void mouseReleased(MouseEvent e) {}
		});
		ko.addKeyListener(new KeyListener(){
			public void keyPressed(KeyEvent e) {
				if(e.getKeyChar()==KeyEvent.VK_ENTER)
				pl.setVisible(false);
			}
			public void keyReleased(KeyEvent e) {}
			public void keyTyped(KeyEvent e) {}
		});
		
		oz2.add(ok);
		oz2.add(Box.createHorizontalStrut(10));
		oz2.add(ko);
		vet.add(Box.createVerticalStrut(10));
		vet.add(oz2);
		pl.add(vet);
		pl.setVisible(true);
	}	
	
public static void main(String args[]){
	ClientGUI p=new ClientGUI(null, null);

}
}