package graficaClient;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Toolkit;

import javax.swing.*;

public class Option {
	JFrame f= new JFrame();
	int port;
	boolean withTLS;
	int auth_type;
	String hostname;
	public Option(int x, int y){
		f.setTitle("Option");
		f.setLocation(x,y);
		f.setSize(450, 600);
		f.setIconImage(Toolkit.getDefaultToolkit().getImage(".\\src\\graficaClient\\Images\\20133476748_8pzCx.jpg"));
		f.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		f.setVisible(true);
		JButton ok= new JButton("Ok");
		JButton ko= new JButton("Canc");
		Box vet= Box.createVerticalBox();
	}
}