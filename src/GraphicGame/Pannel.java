package GraphicGame;

import javax.swing.*;
import java.awt.*;

public class Pannel extends JPanel {
	
	public void paintComponent(Graphics g){
		paintBackGround(g);
		Grille(g, 6);
	}
	
	public void paintBackGround(Graphics g){
		g.setColor(Color.gray);
		int x1 = this.getWidth();
		int y1 = this.getHeight();
		g.fillRect(0, 0, x1, y1);
	}
	
	public void Grille(Graphics g, int size){
		g.setColor(Color.DARK_GRAY);
		int x1 = this.getWidth();
		int y1 = this.getHeight();
		int x = 0;
		int y = 0;
		for (int i = 0; i < size + 1; i++){
			g.drawLine(x, 0, x, y1);
			g.drawLine(0, y, x1, y);
			x += x1 / size;
			y += y1 / size;
		}
	}
	
	
	
}
