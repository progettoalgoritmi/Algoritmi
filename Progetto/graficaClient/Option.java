package graficaClient;

import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.*;

public class Option {
	JFrame f= new JFrame();
	public Option(){
		f.setTitle("Option");
		Dimension x= Toolkit.getDefaultToolkit().getScreenSize();
		f.setLocation(x.width/4,x.height/9);
		f.setSize(450, 600);
		f.setIconImage(Toolkit.getDefaultToolkit().getImage(".\\src\\graficaClient\\Images\\20133476748_8pzCx.jpg"));
		f.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		f.setVisible(true);
	}

}
