package GraphicGame;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.Random;

import javax.swing.JPanel;

import Game.Move;
import Game.Vehicle;

public class GamePanel extends JPanel {
	//Panel contenant l'affichage de la partie.
	static final long serialVersionUID = 2;
	
	int size;
	int nbrVehicles;
	Vehicle[] vehicles;
	int[] pos;
	Move move; //Mouvement en cours d'excécution
	int distancefaite;	//Distance à ajouter au véhicule qu'on est en train de déplacer
	Color[] colors = null;	//Tableau des couleurs de chaque véhicule
	boolean impossible = false;	//Est ce que la partie est possible ?
	
	public GamePanel(int size, int nbrVehicles, Vehicle[] vehicles, int[] pos){
		super();
		this.size = size;
		this.nbrVehicles = nbrVehicles;
		this.vehicles = vehicles;
		this.pos = pos;
		this.move = null;
		this.distancefaite = 0;
		this.impossible = false;		
	}
	
	
	public void paintComponent(Graphics g){
		if (impossible) { //On affiche "impossible" si le jeu n'a pas de solution
			paintBackGround(g);
			Grille(g);
			paintVehicles(g);
			Graphics2D g2d = (Graphics2D)g;
			Font font = new Font("Courier", Font.BOLD, 100);
			g2d.setFont(font);
		    g2d.setColor(Color.blue);
		    g2d.rotate(Math.toRadians(-20));
			g2d.drawString("IMPOSSIBLE", -this.getWidth()/8, 3*this.getHeight() / 4);
		}
		else {
			if (this.move == null) { //Initialisation de la partie, on trace tous les véhicules à leur position initial
				paintBackGround(g);
				Grille(g);
				if (this.colors == null) resetColor();
				paintVehicles(g);
			}
			else { //Dessine le mouvement d'un véhicule, on dessine chaque véhicule à sa position initial sauf celui qui bouge qu'on avance d'une distance distancefaite
				paintBackGround(g);
				Grille(g);
				paintVehiclesMovement(g);
			}
		}
	}
	
	public void setImpossible(boolean b){
		this.impossible = b;
	}
	
	
	public void init(int[] positions){
		this.setpos(positions);
		this.move = null;
		this.distancefaite = 0;
		this.impossible = false;
	}
	
	public void setMove(Move m){
		this.move = m;
	}
	
	public void setdistance(int d){
		this.distancefaite = d;
	}
	
	public int getpos(int i){
		return(this.pos[i]);
	}
	public void setpos(int i, int x){
		this.pos[i] = x;
	}
	public void setpos(int[] positions){
		for (int i = 0; i < pos.length; i++){
			this.pos[i] = positions[i];
		}
	}
	
	public void paintBackGround(Graphics g){
		//Peint le fond en gris
		g.setColor(Color.LIGHT_GRAY);
		int x1 = this.getWidth();
		int y1 = this.getHeight();
		g.fillRect(1/10*x1/this.size, 1/10*y1/this.size, x1, y1);			
	}
	
	public void Grille(Graphics g){
		//Affiche la grille, et notamment la porte de sortie. On suppose ici que la voiture rouge est horizontal pour afficher la porte de sortie.
		g.setColor(Color.black);
		int width = this.getWidth();
		int height = this.getHeight();
		int x1 = 98*width/100;
		int y1 = 98*height/100;
		int y2 = 0;
		int x0 = height/100;
		int y0 = width/100;
		int car1 = vehicles[0].fixedPos;
		g.fillRect(0, 0, width, height/100);
		g.fillRect(0, 0, width/100, height);
		int x = x0 + x1/size;
		int y = y0 + y1/size;
		for (int i = 1; i < this.size; i++){
			g.drawLine(x, 0, x, height);
			g.drawLine(0, y, width, y);
			if (i == car1 - 1) {
				y2 = y;
			}
			x += x1 / size;
			y += y1 / size;
		}
		g.fillRect(0, height - x0, width, x0);
		g.fillRect(width - y0, 0, y0, y2);
		y2 += y1/size;
		g.fillRect(width-y0, y2, y0, height-y2);
		
		
	}
	
	public Color randomColor(Random rand){
		//Renvoie une couleur aléatoire
		float red = rand.nextFloat();
		float green = rand.nextFloat();
		float blue = rand.nextFloat();
		Color randomColor = new Color(red, green, blue);
		return (randomColor);
	}
	
	public void paintVehicle(Graphics g, Color c, Vehicle v, int position, boolean movement){
		//Dessine un véhicule v sur la grille sous forme d'un rectangle de côtés arrondis et de couleur c.
		//Si mouvement = True, on trace le véhicule à sa position + distancefaite.
		g.setColor(c);
		int w = this.getWidth();
		int h = this.getHeight();
		int x1 = 98*w/100;
		int y1 = 98*h/100;
		int x0 = h/100;
		int y0 = w/100;
		float x2 = ((float) x1)/ this.size;
		float y2 = ((float) y1)/ this.size;
		if (v.orientation == 'h') {
			int x = x0 + (int) ((position - 1)*x2 + 1f/10f*x2);
			if (movement) {
				x += distancefaite;
			}
			int y = y0 + (int) ((v.fixedPos-1)*y2 + 1f/20f*y2) ;
			int width = (int) (v.length*x2 - 2f/10f*x2);
			int height = (int) ((18f/20f)*y2);
			g.fillRoundRect(x, y, width, height, 20, 20);
		}
		else {
			int x = x0 + (int) ((v.fixedPos - 1)*x2 + 1f/20f*x2);
			int y = y0 + (int) ((position-1)*y2 + 1f/10f*y2 ) ;
			if (movement) {
				y += distancefaite;
			}
			int width = (int) ((18f/20f)*x2);
			int height = (int) (v.length*y2 - 2f/10f*y2);
			g.fillRoundRect(x, y, width, height, 20, 20);
		}
	}
	
	public void resetColor(){
		//Initialise le tableau colors donnant une couleur pour chaque véhicule.
		Color[] colors = new Color[nbrVehicles];
		Random rand = new Random();
		for (int i = 0; i < this.nbrVehicles; i++){
			if (i == 0){
				colors[i] = Color.red; //On impose la couleur rouge au premier véhicule
			}
			else {
				colors[i] = randomColor(rand);
			}
		}
		this.colors = colors;
	}
	
	public void paintVehicles(Graphics g){
		//Dessine l'ensemble des véhicules
		for (int i = 0; i < this.nbrVehicles; i++){
			Vehicle v = this.vehicles[i];
			int position = this.pos[i];
			paintVehicle(g, colors[i], v, position, false);
		}
	}
	
	public void paintVehiclesMovement(Graphics g) {
		//Met à jour la grille de véhicule suite au mouvement d'un véhicule
		for (int i = 0; i < nbrVehicles; i++) {
			if (i == move.id - 1) {
				paintVehicle(g, colors[i], vehicles[i], pos[i], true);
			}
			else {
				paintVehicle(g, colors[i], vehicles[i], pos[i], false);
			}
		}
	}
	
	
	
}
