package ExplosiveSheep;

import java.awt.TextArea;

import javax.swing.*;

@SuppressWarnings("serial")
public class Window extends JFrame{
	private TextArea ta;
	public Window(){
		ta=new TextArea(null,0,0,TextArea.SCROLLBARS_VERTICAL_ONLY);
		ta.setEditable(false);
		this.add(ta);
		this.setVisible(true);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setSize(500, 500);
	}
	
	public void print(String s){
		ta.append(s);
	}
	
	public void println(String s){
		ta.append(s+'\n');
	}
	
	public void print(char s){
		ta.append(""+s);
	}
	
	public void println(char s){
		ta.append(s+"\n");
	}
}
