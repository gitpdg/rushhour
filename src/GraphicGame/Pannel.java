package GraphicGame;

import javax.swing.*;
import java.awt.*;
import game.*;
import java.util.Random;

public class Pannel extends JPanel {
	
	JPanel pan;
	int size;
	int nbrVehicles;
	Vehicle[] vehicles;
	int[] pos;
	
	public Pannel(int size, int nbrVehicles, Vehicle[] vehicles, int[] pos){
		super();
		this.size = size;
		this.nbrVehicles = nbrVehicles;
		this.vehicles = vehicles;
		this.pos = pos;
	}
	
	
	public void paintComponent(Graphics g){
		paintBackGround(g);
		Grille(g);
		paintVehicles(g);
	}
	
	public void paintBackGround(Graphics g){
		g.setColor(Color.LIGHT_GRAY);
		int x1 = this.getWidth();
		int y1 = this.getHeight();
		g.fillRect(1/10*x1/this.size, 1/10*y1/this.size, x1, y1);
	}
	
	public void Grille(Graphics g){
		g.setColor(Color.DARK_GRAY);
		int x1 = this.getWidth();
		int y1 = this.getHeight();
		int x = 0;
		int y = 0;
		for (int i = 0; i < this.size + 1; i++){
			g.drawLine(x, 0, x, y1);
			g.drawLine(0, y, x1, y);
			x += x1 / size;
			y += y1 / size;
		}
	}
	
	public Color randomColor(Random rand){
		float red = rand.nextFloat();
		float green = rand.nextFloat();
		float blue = rand.nextFloat();
		Color randomColor = new Color(red, green, blue);
		return (randomColor);
	}
	
	public void paintVehicle(Graphics g, Color c, Vehicle v, int position){
		g.setColor(c);
		int x1 = this.getWidth();
		int y1 = this.getHeight();
		float x2 = ((float) x1)/ this.size;
		float y2 = ((float) y1)/ this.size;
		System.out.println(x1);
		System.out.println(y1);
		System.out.println(1/5*x2);
		if (v.orientation == 'h') {
			int x = (int) ((position - 1)*x2 + 1f/10f*x2);
			int y = (int) ((v.fixedPos-1)*y2 + 1f/20f*y2) ;
			int width = (int) (v.length*x2 - 2f/10f*x2);
			int height = (int) ((18f/20f)*y2);
			g.fillRoundRect(x, y, width, height, 20, 20);
		}
		else {
			int x = (int) ((v.fixedPos - 1)*x2 + 1f/20f*x2);
			int y = (int) ((position-1)*y2 + 1f/10f*y2) ;
			int width = (int) ((18f/20f)*x2);
			int height = (int) (v.length*y2 - 2f/10f*y2);
			g.fillRoundRect(x, y, width, height, 20, 20);
		}
	}
	
	public void paintVehicles(Graphics g){
		Random rand = new Random();
		Color c;
		for (int i = 0; i < this.nbrVehicles; i++){
			if (i == 0){
				c = Color.red;
			}
			else {
				c = randomColor(rand);
			}
			Vehicle v = this.vehicles[i];
			int position = this.pos[i];
			paintVehicle(g, c, v, position);
		}
	}
	
	
}
