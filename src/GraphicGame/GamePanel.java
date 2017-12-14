package GraphicGame;

import javax.swing.*;
import java.awt.*;
import game.*;
import java.util.Random;

public class GamePanel extends JPanel {
	
	JPanel pan;
	int size;
	int nbrVehicles;
	Vehicle[] vehicles;
	int[] pos;
	Move move;
	int distancefaite;
	Color[] colors = null;
	
	public GamePanel(int size, int nbrVehicles, Vehicle[] vehicles, int[] pos){
		super();
		this.size = size;
		this.nbrVehicles = nbrVehicles;
		this.vehicles = vehicles;
		this.pos = pos;
		this.move = null;
		distancefaite = 0;
	}
	
	
	public void paintComponent(Graphics g){
		if (this.move == null) {
			paintBackGround(g);
			Grille(g);
			this.colors = paintVehicles(g);
		}
		else {
			paintBackGround(g);
			Grille(g);
			paintVehiclesMovement(g);
		}
	}
	
	public void init(int[] positions){
		this.setpos(positions);
		this.move = null;
		this.colors = null;
		this.distancefaite = 0;
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
		g.setColor(Color.LIGHT_GRAY);
		int x1 = this.getWidth();
		int y1 = this.getHeight();
		g.fillRect(1/10*x1/this.size, 1/10*y1/this.size, x1, y1);			
	}
	
	public void Grille(Graphics g){
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
		float red = rand.nextFloat();
		float green = rand.nextFloat();
		float blue = rand.nextFloat();
		Color randomColor = new Color(red, green, blue);
		return (randomColor);
	}
	
	public void paintVehicle(Graphics g, Color c, Vehicle v, int position, boolean movement){
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
	
	public Color[] paintVehicles(Graphics g){
		Color[] colors = new Color[nbrVehicles];
		Random rand = new Random();
		for (int i = 0; i < this.nbrVehicles; i++){
			if (i == 0){
				colors[i] = Color.red;
			}
			else {
				colors[i] = randomColor(rand);
			}
			Vehicle v = this.vehicles[i];
			int position = this.pos[i];
			paintVehicle(g, colors[i], v, position, false);
		}
		return(colors);
	}
	
	public void paintVehiclesMovement(Graphics g) {
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
