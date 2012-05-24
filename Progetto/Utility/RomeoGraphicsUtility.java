package Utility;

import graficaClient.ClientGUI;
import graficaClient.ClientGUI.Lingua;
import FlyingSquirrel_imp.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

import javax.swing.*;

public class RomeoGraphicsUtility {
		
	public static ImageIcon impostaIcona(String x){
		Image im,ima;
		String image=".\\src\\graficaClient\\Images\\";
		image=image+x;
		ImageIcon icon= new ImageIcon(image);
		im=icon.getImage();
		ima=im.getScaledInstance(255,255,Image.SCALE_AREA_AVERAGING);
		return new ImageIcon(ima);
	}

	public static ImageIcon impostaIcona(String x, int l, int h){
		Image im,ima;
		String image=".\\src\\graficaClient\\Images\\";
		image=image+x;
		ImageIcon icon= new ImageIcon(image);
		im=icon.getImage();
		ima=im.getScaledInstance(l,h,Image.SCALE_AREA_AVERAGING);
		return new ImageIcon(ima);
	}
	
	public static JFrame miniIcona(JFrame k, String logo){
		final JFrame l=k;
		SystemTray st=null;
			final TrayIcon ti;
			try{
			if(SystemTray.isSupported()==true){
				st=SystemTray.getSystemTray();
			Image imgg = Toolkit.getDefaultToolkit().getImage(".\\src\\graficaClient\\Images\\"+logo).getScaledInstance(15, 16,Image.SCALE_AREA_AVERAGING);
			 ti= new TrayIcon(imgg);
				final JPopupMenu pm= new JPopupMenu();
				JMenuItem apri= new JMenuItem("Open");
		
	//Listener apri
				apri.addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent e) {
						l.setVisible(true);
					}
				});
				
				JMenuItem esci= new JMenuItem("Exit");
				
	//Listener esci
				esci.addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent e) {
						 l.setVisible(false);
					     SystemTray.getSystemTray().remove(ti);
					     System.exit(0);
					}
					});
				
				JMenuItem nascondi= new JMenuItem("Ghost");
				
	//Listener nascondi
				nascondi.addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent e) {
						 l.setVisible(false);
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
								if(l.isVisible()==true)
									l.setVisible(false);
								else
									l.setVisible(true);
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
		return l;
		}
	
	public static String setIcon(JFrame f, String precedente){
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
		return jf.getSelectedFile().getName();
		}
		return precedente ;
}
	
	public static String setFile(JFrame f){
		JFileChooser jf= new JFileChooser();
		jf.setMultiSelectionEnabled(false);
		jf.setCurrentDirectory(new File(""));
		jf.setDialogTitle("File");
		jf.setApproveButtonText("Select");
		int x=jf.showOpenDialog(f);
		if(x==JFileChooser.APPROVE_OPTION){
		return jf.getSelectedFile().getName();
		}
		return null;
}
	
	public static String setImage(JFrame f){
		JFileChooser jf= new JFileChooser();
		jf.setMultiSelectionEnabled(false);
		jf.setCurrentDirectory(new File(".\\src\\graficaClient\\Images\\"));
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
		return jf.getSelectedFile().getName();
		}
		return null;
}
		
	public static void readMe(){
		JFrame f= new JFrame();
		TextArea y= new TextArea(null,0,0, TextArea.SCROLLBARS_NONE);
		f.setTitle("ReadMe!!!");
		Dimension x= Toolkit.getDefaultToolkit().getScreenSize();
		f.setLocation(x.width/4,x.height/9);
		f.setSize(440, 300);
		f.setIconImage(Toolkit.getDefaultToolkit().getImage(".\\src\\graficaClient\\Images\\20133476748_8pzCx.jpg"));
		f.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		y.setText("");
		y.setPreferredSize(x);
		y.setVisible(true);
		y.setEditable(false);
		f.add(y);
		f.setBackground(Color.WHITE);
		f.setVisible(true);
		}

	public static void aboutProg(){
		JFrame f= new JFrame();
		Box oriz=Box.createVerticalBox();
		Box oriz1=Box.createHorizontalBox();
		Box oriz2=Box.createHorizontalBox();
		String mario="20133476748_8pzCx.jpg";
		JLabel mario1= new JLabel(RomeoGraphicsUtility.impostaIcona(mario, 100, 100));
		JLabel mario2= new JLabel("Romeo Mario");
		String antonio="antonio.jpg";
		JLabel antonio1= new JLabel(RomeoGraphicsUtility.impostaIcona(mario, 100, 100));
		JLabel antonio2= new JLabel("Antonio Limardi");
		String pepp="peppe.jpg";
		JLabel peppe1= new JLabel(RomeoGraphicsUtility.impostaIcona(mario, 100, 100));
		JLabel peppe2= new JLabel("Giueppe Vazzana");
		String ciccio="ciccio.jpg";
		JLabel ciccio1= new JLabel(RomeoGraphicsUtility.impostaIcona(mario, 100, 100));
		JLabel ciccio2= new JLabel("Francesco Scutella'");
		oriz1.add(mario1);
		oriz1.add(mario2);
		oriz1.add(Box.createHorizontalStrut(10));
		oriz1.add(antonio1);
		oriz1.add(antonio2);
		oriz.add(oriz1);
		oriz.add(Box.createVerticalStrut(25));
		oriz2.add(peppe1);
		oriz2.add(peppe2);
		oriz2.add(Box.createHorizontalStrut(10));
		oriz2.add(ciccio1);
		oriz2.add(ciccio2);
		oriz.add(oriz2);
		oriz.add(Box.createVerticalStrut(35));
		TextArea y= new TextArea(null,0,0, TextArea.SCROLLBARS_NONE);
		f.setTitle("About");
		Dimension x= Toolkit.getDefaultToolkit().getScreenSize();
		f.setLocation(x.width/4,x.height/9);
		f.setSize(450, 400);
		f.setIconImage(Toolkit.getDefaultToolkit().getImage(".\\src\\graficaClient\\Images\\20133476748_8pzCx.jpg"));
		f.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		y.setText("Questo programma e' un semplice sistema di messaggistica testuale \n" +
				" che vi permette di comunicare con altri utenti possessori dello stesso \n" +
				" o con altri sistemi che utilizzano gli stessi standard di comunicazione \n" +
				" Nato da una mente contorta e' stato sviluppato in collaborazione con \n Romeo Mario - Antonio Limardi - Giuseppe Vazzane - Francesco Scutella");
		y.setVisible(true);
		y.setEditable(false);
		oriz.add(y);
		f.add(oriz);
		f.setBackground(Color.WHITE);
		f.setVisible(true);
	}
	
	
	public static void invioDati (String x){
		
	}
	
	//Questi altri metodi vanno testati
	public static JMenu impostaLingua(Lingua x){
		switch (x){
		case Italiano:
			
		}
		return null;
	  }
	
	public static void invio(TextArea scrive, JTextArea legge){
		String x=scrive.getText();
		if(x.equals("\n"))
			return;
		// passo x all'oggetto per inviare il testo
		//String y= nome utente: a capo x
		String y= legge.getText();
		y=y+x+"\n";
		legge.setText(y);
		scrive.setText(null);
		scrive.setText("\n");
	}
	
	public static String connetti(String username, String pass, Lingua lingua, String icon, String hostname, int port, boolean withTLS, int auth_type, ArrayList<Character> buffer){
		if(username.length()<1||pass.length()<1)
			return "Username o Password invalidi";
		Connection connesso= new Connection(port, hostname, buffer);
		try {
			connesso.connect(auth_type, withTLS, username, pass);
		} catch (KeyManagementException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		ClientGUI x= new ClientGUI(lingua, icon, connesso);
		return "ok";
	}
	
}

