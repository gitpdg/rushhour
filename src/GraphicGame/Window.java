package GraphicGame;

import javax.swing.*;
import java.awt.*;
import game.Game;

import java.io.*;

public class Window extends JFrame{
	Game game;
	JFrame window;
	
	public Window(String file_name) throws IOException{
		super("Game");
		this.game = new Game(file_name);
		
		setSize(500,500);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		//Pannel pan = new Pannel();
		//pan.paintBackGround(g);
		//pan.Grille(getGraphics(), game.size);
		setContentPane(new Pannel());
		
		setVisible(true);
	}
	
	public static void main(String[] args) throws IOException{
		Window gui = new Window("file.txt");
		gui.setVisible(true);
	}
}
