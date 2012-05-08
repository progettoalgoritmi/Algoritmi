package ClasseMix;

import graficaClient.AutenticationGUI;
import graficaClient.ClientGUI;
import graficaClient.ClientGUI.Lingua;

import java.awt.*;
import java.awt.event.*;
import java.io.File;

import javax.swing.*;
import javax.swing.text.Caret;

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
	
//Questi altri metodi vanno testati
	public static JMenu impostaLingua(Lingua x){
		switch (x){
		case Italiano:
			
		}
		return null;
	  }
	
	public static JFrame readMe(){
		JFrame f= new JFrame();
		TextArea y= new TextArea(null,0,0, TextArea.SCROLLBARS_NONE);
		f.setTitle("ReadMe!!!");
		Dimension x= Toolkit.getDefaultToolkit().getScreenSize();
		f.setLocation(x.width/4,x.height/9);
		f.setSize(250, 300);
		f.setIconImage(Toolkit.getDefaultToolkit().getImage(".\\src\\graficaClient\\Images\\20133476748_8pzCx.jpg"));
		f.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		y.setBackground(Color.WHITE);
		y.setText("");
		y.setSize(x);
		y.setVisible(true);
		y.setEditable(false);
		f.setVisible(true);
		return f;
		}

	public static JFrame aboutProg(){
		JFrame f= new JFrame();
		TextArea y= new TextArea(null,0,0, TextArea.SCROLLBARS_NONE);
		f.setTitle("About");
		Dimension x= Toolkit.getDefaultToolkit().getScreenSize();
		f.setLocation(x.width/4,x.height/9);
		f.setSize(350, 400);
		f.setIconImage(Toolkit.getDefaultToolkit().getImage(".\\src\\graficaClient\\Images\\20133476748_8pzCx.jpg"));
		f.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		y.setText("");
		y.setSize(x);
		y.setVisible(true);
		y.setEditable(false);
		y.setBackground(Color.WHITE);
		f.setVisible(true);
		return f;
	}
	
	
	public static void invioDati (String x){
		
	}
	
	public static void invio(TextArea scrive, TextArea legge){
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
	
	public static void connetti(String username, String pass, Lingua lingua, String icon){
		if(username==null||pass==null)
			System.err.print("Username o Password invalidi");
		
		ClientGUI x= new ClientGUI(lingua, icon);
	}
	
	public static void disconnetti(){
		
		AutenticationGUI x= new AutenticationGUI();
	}
}

