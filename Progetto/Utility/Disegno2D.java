package Utility;

import java.awt.*;
import java.awt.event.*;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JPanel;

public class Disegno2D extends JPanel implements MouseListener, MouseMotionListener{

//Operazioni che si possono fare
 private final int  PEN_OP         = 1;
 private final int  LINE_OP        = 2;
 private final int  ERASER_OP      = 3;
 private final int  RECT_OP        = 5;
 private final int  OVAL_OP        = 6;
 private final int  FRECT_OP       = 7;
 private final int  FOVAL_OP       = 8;

//Cordinate del mouse attuali
 private int mousex                = 0;
 private int mousey                = 0;

//Cordinate del mouse precedenti
 private int prevx                 = 0;
 private int prevy                 = 0;

 //Variabile di selezionamento del tipo di operazione
 private boolean initialPen= true;
 private boolean initialLine= true;
 private boolean initialEraser= true;
 private boolean initialRect= true;
 private boolean initialOval= true;
 private boolean initialFRect= true;
 private boolean initialFOval= true;

//Variabili di dimensione
 private int  Orx= 0;
 private int  Ory= 0;
 private int  OrWidth= 0;
 private int  OrHeight= 0;
 private int  drawX= 0;
 private int  drawY= 0;
 private int  eraserLength= 5;
 
 //Variabile di operazione e di colore
 private int    opStatus= PEN_OP;
 private Color  mainColor= new Color(0,0,0);
 private Color  xorColor= new Color(255,255,255);
 
 //Menu di scelta del colore e dell'operazione
 private Choice colorMenu= new Choice();
 private Choice formaMenu= new Choice();
 private JButton ok= new JButton("Send");
 private JButton clear= new JButton("Clear");
 
 //Pannelli di lavoro
 private Panel drawPanel= new Panel();
 private Panel statusPanel= new Panel();

//Metodo di inizializzazione dei pannelli di lavoro
 public Disegno2D(){
	 
// Layout Pagina
    setLayout(new BorderLayout());
    
//Item di colorMenu
    colorMenu.addItem("Black");
    colorMenu.addItem("Blue");
    colorMenu.addItem("Green");
    colorMenu.addItem("Red");
    colorMenu.addItem("Purple");
    colorMenu.addItem("Orange");
    colorMenu.addItem("Pink");
    colorMenu.addItem("Gray");
    colorMenu.addItem("Yellow");
    
//Item di formaMenu
    formaMenu.addItem("Pen");
    formaMenu.addItem("Line");
    formaMenu.addItem("Eraser");
    formaMenu.addItem("Rectangle");
    formaMenu.addItem("Oval");
    formaMenu.addItem("Filled Rectangle");
    formaMenu.addItem("Filled Oval");
    
//Listener colorMenu
    colorMenu.addItemListener(new ItemListener(){
		public void itemStateChanged(ItemEvent e) {
			if (e.getItem()=="Black")
			       mainColor = Color.black;

			    if (e.getItem()=="Blue")
			    	mainColor = Color.blue;

			    if (e.getItem()=="Green")
			    	mainColor = Color.green;

			    if (e.getItem()=="Red")
			    	mainColor = Color.red;

			    if (e.getItem()=="Purple")
			    	mainColor = Color.magenta;

			    if (e.getItem()=="Orange")
			    	mainColor = Color.orange;

			    if (e.getItem()=="Pink")
			    	mainColor = Color.pink;

			    if (e.getItem()=="Gray")
			    	mainColor = Color.gray;

			    if (e.getItem()=="Yellow")
			    	mainColor = Color.yellow;
		}
    	
    });
    
//Listener formaMenu
    formaMenu.addItemListener(new ItemListener(){
		public void itemStateChanged(ItemEvent e) {
			 if (e.getItem()=="Pen")
			       opStatus = PEN_OP;

			    if (e.getItem()=="Line")
			       opStatus = LINE_OP;

			    if (e.getItem()=="Eraser")
			       opStatus = ERASER_OP;

			    if (e.getItem()=="Rectangle")
			       opStatus = RECT_OP;

			    if (e.getItem()=="Oval")
			       opStatus = OVAL_OP;

			    if (e.getItem()=="Filled Rectangle")
			       opStatus = FRECT_OP;

			    if (e.getItem()=="Filled Oval")
			       opStatus = FOVAL_OP;
		}
    	
    });
    
//Listener Bottone Clear
    clear.addMouseListener(new MouseListener(){
		public void mouseClicked(MouseEvent e) {
			clearPanel(drawPanel);
		}
		public void mouseEntered(MouseEvent e) {}
		public void mouseExited(MouseEvent e) {}
		public void mousePressed(MouseEvent e) {}
		public void mouseReleased(MouseEvent e) {}
    });
    
//Listener Bottone Invia
    ok.addMouseListener(new MouseListener(){
    	public void mouseClicked(MouseEvent e) {
    		System.out.print("Inviato");
			clearPanel(drawPanel);
		}
		public void mouseEntered(MouseEvent e) {}
		public void mouseExited(MouseEvent e) {}
		public void mousePressed(MouseEvent e) {}
		public void mouseReleased(MouseEvent e) {}
    });
    
//Box dei bottoni
    Box bott= Box.createHorizontalBox();
    bott.add(ok);
    bott.add(Box.createHorizontalStrut(15));
    bott.add(clear);
    
//Inserimento dei pannelli e della box dei bottoni
    statusPanel.add(formaMenu);
    statusPanel.add(colorMenu);
    statusPanel.add(bott);
    drawPanel.setBackground(Color.white);
    add(statusPanel, "South");
    add(drawPanel, "Center");
    
//Listener Pannello di disegno
    drawPanel.addMouseMotionListener(this);
    drawPanel.addMouseListener(this);
    this.addMouseListener(this);
    this.addMouseMotionListener(this);
 }

//Metodo di reset del pannello di disegno
 public void clearPanel(Panel p)
 {
    Graphics g = p.getGraphics();
    g.setColor(p.getBackground());
    g.fillRect(0,0,p.getBounds().width,p.getBounds().height);
  }

//Metodo per l'utilizzo della matita per disegnare
 public void penOperation(MouseEvent e)
 {
    Graphics g  = drawPanel.getGraphics();
    g.setColor(mainColor);

    /*
      In initial state setup default values
      for mouse coordinates
    */
    if (initialPen)
    {
       setGraphicalDefaults(e);
       initialPen = false;
       g.drawLine(prevx,prevy,mousex,mousey);
    }

    /*
      Make sure that the mouse has actually
      moved from its previous position.
    */
    if (mouseHasMoved(e))
    {
       /*
          set mouse coordinates to
          current mouse position
       */
       mousex = e.getX();
       mousey = e.getY();

       /*
          draw a line from the previous mouse coordinates
          to the current mouse coordinates
       */
       g.drawLine(prevx,prevy,mousex,mousey);

       /*
          set the current mouse coordinates to
          previous mouse coordinates for next time
       */
       prevx = mousex;
       prevy = mousey;
    }
 }

//Metodo per disegnare una retta
 public void lineOperation(MouseEvent e)
 {
    Graphics g  = drawPanel.getGraphics();
    g.setColor(mainColor);

    /*
      In initial state setup default values
      for mouse coordinates
    */
    if (initialLine)
    {
       setGraphicalDefaults(e);
       g.setXORMode(xorColor);
       g.drawLine(Orx,Ory,mousex,mousey);
       initialLine=false;
    }

    /*
      Make sure that the mouse has actually
      moved from its previous position.
    */
    if (mouseHasMoved(e))
    {
       /*
         Delete previous line shadow
         by xor-ing the graphical object
       */
       g.setXORMode(xorColor);
       g.drawLine(Orx,Ory,mousex,mousey);

       /* Update new mouse coordinates */
       mousex = e.getX();
       mousey = e.getY();

       /* Draw line shadow */
       g.drawLine(Orx,Ory,mousex,mousey);
    }
 }

//Metodo per disegnare un rettangolo
 public void rectOperation(MouseEvent e)
 {
    Graphics g  = drawPanel.getGraphics();
    g.setColor(mainColor);

    /*
      In initial state setup default values
      for mouse coordinates
    */
    if (initialRect)
    {
       setGraphicalDefaults(e);
       initialRect = false;
    }

    /*
      Make sure that the mouse has actually
      moved from its previous position.
    */
    if (mouseHasMoved(e))
    {
       /*
         Delete previous rectangle shadow
         by xor-ing the graphical object
       */
       g.setXORMode(drawPanel.getBackground());
       g.drawRect(drawX,drawY,OrWidth,OrHeight);

       /* Update new mouse coordinates */
       mousex = e.getX();
       mousey = e.getY();

       /* Check new mouse coordinates for negative errors */
       setActualBoundry();

       /* Draw rectangle shadow */
       g.drawRect(drawX,drawY,OrWidth,OrHeight);

    }

 }

//Metodo per disegnare un ovale
 public void ovalOperation(MouseEvent e)
 {
    Graphics g  = drawPanel.getGraphics();
    g.setColor(mainColor);

    /*
      In initial state setup default values
      for mouse coordinates
    */
    if (initialOval)
    {
       setGraphicalDefaults(e);
       initialOval=false;
    }

    /*
      Make sure that the mouse has actually
      moved from its previous position.
    */
    if (mouseHasMoved(e))
    {
       /*
         Delete previous oval shadow
         by xor-ing the graphical object
       */
       g.setXORMode(xorColor);
       g.drawOval(drawX,drawY,OrWidth,OrHeight);

       /* Update new mouse coordinates */
       mousex = e.getX();
       mousey = e.getY();

       /* Check new mouse coordinates for negative errors */
       setActualBoundry();

       /* Draw oval shadow */
       g.drawOval(drawX,drawY,OrWidth,OrHeight);
    }
 }
 
//Metodo per disegnare un rattangolo pieno
public void frectOperation(MouseEvent e)
 {

    Graphics g  = drawPanel.getGraphics();
    g.setColor(mainColor);

    /*
      In initial state setup default values
      for mouse coordinates
    */
    if (initialFRect)
    {
       setGraphicalDefaults(e);
       initialFRect=false;
    }

    /*
      Make sure that the mouse has actually
      moved from its previous position.
    */
    if (mouseHasMoved(e))
    {
       /*
         Delete previous rectangle shadow
         by xor-ing the graphical object
       */
       g.setXORMode(xorColor);
       g.drawRect(drawX,drawY,OrWidth-1,OrHeight-1);

       /* Update new mouse coordinates */
       mousex = e.getX();
       mousey = e.getY();

       /* Check new mouse coordinates for negative errors */
       setActualBoundry();

       /* Draw rectangle shadow */
       g.drawRect(drawX,drawY,OrWidth-1,OrHeight-1);

    }

 }
 
//Metodo per disegnare un ovale pieno
public void fovalOperation(MouseEvent e)
 {
    Graphics g  = drawPanel.getGraphics();
    g.setColor(mainColor);

    /*
      In initial state setup default values
      for mouse coordinates
    */
    if (initialFOval)
    {
       setGraphicalDefaults(e);
       initialFOval = false;
    }

    /*
      Make sure that the mouse has actually
      moved from its previous position.
    */
    if (mouseHasMoved(e))
    {
       /*
         Delete previous oval shadow
         by xor-ing the graphical object
       */
       g.setXORMode(xorColor);
       g.drawOval(drawX,drawY,OrWidth,OrHeight);

       /* Update new mouse coordinates */
       mousex = e.getX();
       mousey = e.getY();

       /* Check new mouse coordinates for negative errors */
       setActualBoundry();

       /* Draw oval shadow */
       g.drawOval(drawX,drawY,OrWidth,OrHeight);
    }

 }

//Metodo per cancellare con la gomma
public void eraserOperation(MouseEvent e)
 {
    Graphics g  = drawPanel.getGraphics();

    /*
      In initial state setup default values
      for mouse coordinates
    */
    if (initialEraser)
    {
       setGraphicalDefaults(e);
       initialEraser = false;
       g.setColor(mainColor=Color.white);
       g.fillRect(mousex-eraserLength, mousey-eraserLength,eraserLength*2,eraserLength*2);
       g.setColor(Color.black);
       g.drawRect(mousex-eraserLength,mousey-eraserLength,eraserLength*2,eraserLength*2);
       prevx = mousex;
       prevy = mousey;
    }

    /*
      Make sure that the mouse has actually
      moved from its previous position.
    */
    if (mouseHasMoved(e))
    {
       g.setColor(mainColor=Color.white);
       g.drawRect(prevx-eraserLength, prevy-eraserLength,eraserLength*2,eraserLength*2);

       /* Get current mouse coordinates */
       mousex  = e.getX();
       mousey  = e.getY();

       /* Draw eraser block to panel */
       g.setColor(mainColor=Color.white);
       g.fillRect(mousex-eraserLength, mousey-eraserLength,eraserLength*2,eraserLength*2);
       g.setColor(Color.black);
       g.drawRect(mousex-eraserLength,mousey-eraserLength,eraserLength*2,eraserLength*2);
       prevx = mousex;
       prevy = mousey;
    }
 }

 public boolean mouseHasMoved(MouseEvent e)
 {
    return (mousex != e.getX() || mousey != e.getY());
 }

 public void setActualBoundry()
 {
       /*
         If the any of the current mouse coordinates
         are smaller than the origin coordinates, meaning
         if drag occured in a negative manner, where either
         the x-shift occured from right and/or y-shift occured
         from bottom to top.
       */
       if (mousex < Orx || mousey < Ory)
       {
          /*
            if the current mouse x coordinate is smaller
            than the origin x coordinate,
            equate the drawX to be the difference between
            the current width and the origin x coordinate.
          */
          if (mousex < Orx)
          {
             OrWidth = Orx - mousex;
             drawX   = Orx - OrWidth;
          }
          else
          {
             drawX    = Orx;
             OrWidth  = mousex - Orx;

          }
          /*
            if the current mouse y coordinate is smaller
            than the origin y coordinate,
            equate the drawY to be the difference between
            the current height and the origin y coordinate.
          */
          if (mousey < Ory)
          {
             OrHeight = Ory - mousey;
             drawY    = Ory - OrHeight;
          }
          else
          {
             drawY    = Ory;
             OrHeight = mousey - Ory;
          }
       }
       /*
         Else if drag was done in a positive manner meaning
         x-shift occured from left to right and or y-shift occured
         from top to bottom
       */
       else
       {
          drawX    = Orx;
          drawY    = Ory;
          OrWidth  = mousex - Orx;
          OrHeight = mousey - Ory;
       }
 }
 
 public void setGraphicalDefaults(MouseEvent e)
 {
    mousex   = e.getX();
    mousey   = e.getY();
    prevx    = e.getX();
    prevy    = e.getY();
    Orx      = e.getX();
    Ory      = e.getY();
    drawX    = e.getX();
    drawY    = e.getY();
    OrWidth  = 0;
    OrHeight = 0;
 }

 public void mouseDragged(MouseEvent e)
 {
    updateMouseCoordinates(e);

    switch (opStatus)
    {
       /* If opStatus is PEN_OP  then call penOperation method */
       case PEN_OP   : penOperation(e);
                       break;

       /* If opStatus is LINE_OP then call lineOperation method */
       case LINE_OP  : lineOperation(e);
                       break;

       /* If opStatus is RECt_OP  then call rectOperation method */
       case RECT_OP  : rectOperation(e);
                       break;

       /* If opStatus is OVAL_OP then call ovalOperation method */
       case OVAL_OP  : ovalOperation(e);
                       break;

       /* If opStatus is FRECT_OP  then call frectOperation method */
       case FRECT_OP : frectOperation(e);
                       break;

       /* If opStatus is FOVAL_OP then call fovalOperation method */
       case FOVAL_OP : fovalOperation(e);
                       break;

       /* If opStatus is ERASER_OP then call eraserOperation method */
       case ERASER_OP: eraserOperation(e);
                       break;
    }
 }

 public void mouseReleased(MouseEvent e)
 {
    /* Update current mouse coordinates to screen */
    updateMouseCoordinates(e);

    switch (opStatus)
    {
       /* If opStatus is PEN_OP  then call releasedPen method */
       case PEN_OP    : releasedPen();
                        break;

       /* If opStatus is LINE_OP then call releasedLine method */
       case LINE_OP   : releasedLine();
                        break;

       /* If opStatus is RECT_OP  then call releasedRect method */
       case RECT_OP   : releasedRect();
                        break;

       /* If opStatus is OVAL_OP then call releasedOval method */
       case OVAL_OP   : releasedOval();
                        break;

       /* If opStatus is FRECT_OP  then call releasedFrect method */
       case FRECT_OP  : releasedFRect();
                        break;

       /* If opStatus is FOVAL_OP then call releasedFoval method */
       case FOVAL_OP  : releasedFOval();
                        break;

       /* If opStatus is ERASER_OP then call releasedEraser method */
       case ERASER_OP : releasedEraser();
                        break;
    }
 }

 public void mouseEntered(MouseEvent e)
 {
    updateMouseCoordinates(e);
 }

 public void releasedPen()
 {
    initialPen = true;
 }

 public void releasedLine()
 {
    if ((Math.abs(Orx - mousex) + Math.abs(Ory - mousey)) != 0)
    {
       System.out.println("Line has been released....");
       initialLine = true;
       Graphics g  = drawPanel.getGraphics();
       g.setColor(mainColor);
       g.drawLine(Orx,Ory,mousex,mousey);
    }
 }

 public void releasedEraser()
 {
    initialEraser = true;
    Graphics g  = drawPanel.getGraphics();
    g.setColor(mainColor=Color.white);
    g.drawRect(mousex-eraserLength,mousey-eraserLength,eraserLength*2,eraserLength*2);
 }
 
 public void releasedRect()
 {
    initialRect = true;
    Graphics g  = drawPanel.getGraphics();
    g.setColor(mainColor);
    g.drawRect(drawX,drawY,OrWidth,OrHeight);
 }

 public void releasedOval()
 {
    initialOval = true;
    Graphics g  = drawPanel.getGraphics();
    g.setColor(mainColor);
    g.drawOval(drawX,drawY,OrWidth,OrHeight);
 }

 public void releasedFRect()
 {
    initialFRect = true;
    Graphics g  = drawPanel.getGraphics();
    g.setColor(mainColor);
    g.fillRect(drawX,drawY,OrWidth,OrHeight);
 }

 public void releasedFOval()
 {
    initialFOval = true;
    Graphics g  = drawPanel.getGraphics();
    g.setColor(mainColor);
    g.fillOval(drawX - 1,drawY - 1,OrWidth + 2,OrHeight + 2);
 }

 public void updateMouseCoordinates(MouseEvent e){
	String xCoor ="";
    String yCoor ="";

    if (e.getX() < 0) xCoor = "0";
    else
    {
       xCoor = String.valueOf(e.getX());
    }

    if (e.getY() < 0) xCoor = "0";
    else
    {
       yCoor = String.valueOf(e.getY());
    }
 }
 
public void mouseClicked(MouseEvent e)
 {
    updateMouseCoordinates(e);
}
 
public void mouseExited(MouseEvent e)
 {
    updateMouseCoordinates(e);
 }

 public void mouseMoved(MouseEvent e)
 {
    updateMouseCoordinates(e);
 }

 public void mousePressed(MouseEvent e)
 {
    updateMouseCoordinates(e);
 }

public Panel getPanelDraw(){
	return drawPanel;
}
}

