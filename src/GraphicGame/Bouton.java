package GraphicGame;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JButton;

public class Bouton extends JButton implements MouseListener {
	//Implémente l'affichage d'un Bouton, et notamment le changement d'affichage lors du passage ou du clic de la souris.
	static final long serialVersionUID = 1;
	private String name;
	private Color c;
	
	public Bouton(String str){
		super(str);
		this.name = str;
		this.addMouseListener(this);
		this.c = Color.white;
	}
	
	public void paintComponent(Graphics g){
		g.setColor(c);
		g.fillRect(0, 0, this.getWidth(), this.getHeight());
		g.setColor(Color.black);
		FontMetrics fm = g.getFontMetrics();
		int x = (getWidth()-fm.stringWidth(name))/2;
		int y = (getHeight()-fm.getHeight())/2 + fm.getAscent();
		//g.drawString(this.name, this.getWidth()/2 - (this.getWidth()/4), (this.getHeight()/2) + 5);
		g.drawString(this.name, x, y);
	}
	
	public void setName(String n){
		this.name = n;
	}
	
	public String getName(){
		return(this.name);
	}
	
	//Méthode appelée lors du clic de souris
	  public void mouseClicked(MouseEvent event) { }


	//Méthode appelée lors du survol de la souris
	public void mouseEntered(MouseEvent event) {
		this.c = Color.yellow;
	}


	//Méthode appelée lorsque la souris sort de la zone du bouton
	public void mouseExited(MouseEvent event) {
		this.c = Color.white;
	}


	//Méthode appelée lorsque l'on presse le bouton gauche de la souris
	public void mousePressed(MouseEvent event) {
		this.c = Color.red;
	}

	//Méthode appelée lorsque l'on relâche le clic de souris
	public void mouseReleased(MouseEvent event) {
		this.c = Color.white;
	}       

}
