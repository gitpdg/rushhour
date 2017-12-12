package GraphicGame;

import java.util.LinkedList;
import javax.swing.*;
import java.awt.*;
import game.*;

import java.io.*;

public class Window extends JFrame{
	Game game;
	JFrame window;
	private Pannel pan;
	
	public Window(String file_name) throws IOException{
		super("Game");
		this.game = new Game(file_name);
		
		setSize(750,750);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		int size = game.size;
		int nbrVehicles = game.nbrVehicles;
		Vehicle[] vehicles = game.vehicles;
		int[] pos = (game.initialState).pos;
		this.pan = new Pannel(size, nbrVehicles, vehicles, pos);
		setContentPane(pan);
		
		setVisible(true);
	}
	
	public void movement(Move m){
		pan.setMove(m);
		int distance = m.distance;
		if (distance >= 0) {
			for (int d = 0; d <= distance*(pan.getWidth()) / game.size; d++) {
				pan.setdistance(d);
				pan.repaint();
				try {
			        Thread.sleep(5);
			      } catch (InterruptedException e) {
			        e.printStackTrace();
			      }
			}
		}
		else {
			for (int d = 0; d >= distance*(pan.getWidth()) / game.size; d--) {
				pan.setdistance(d);
				pan.repaint();
				try {
			        Thread.sleep(5);
			      } catch (InterruptedException e) {
			        e.printStackTrace();
			      }
			}
		}
		pan.setpos(m.id-1, pan.getpos(m.id-1) + distance);
		
	}
	
	public static void main(String[] args) throws IOException{
		Window gui = new Window("file.txt");
		gui.setVisible(true);
		LinkedList<Move> sol = (gui.game).solve();
		try {
	        Thread.sleep(1000);
	      } catch (InterruptedException e) {
	        e.printStackTrace();
	      }
		while (!sol.isEmpty()){
			Move m = sol.remove();
			gui.movement(m);
		}
	}
}
