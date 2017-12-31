package GraphicGame;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.LinkedList;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import Game.Game;
import Game.Move;
import Game.Vehicle;


public class GameWindow extends JFrame {
	String file_name;
	Game game;
	LinkedList<Move> solution;
	boolean solved;
	private GamePanel pan;
	private JPanel container = new JPanel();
	private Bouton bouton = new Bouton("Solve");
	private Bouton newGame = new Bouton("New Game");
	boolean reset;
	private Thread t;
	String path = "Games/";
	int typeheuristic;
	boolean brutForce;
	
	public GameWindow(String file_name, String windowName, int type, boolean brutForce) throws IOException{
		super(windowName);
		this.game = new Game(path + file_name, type, brutForce);
		this.typeheuristic = type;
		this.brutForce = brutForce;
		this.solution = null;
		this.file_name = file_name;
		this.reset = false;
		this.solved = false;
		
		
		setSize(700,700);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		container.setBackground(Color.white);
		container.setLayout(new BorderLayout());
		
		int size = game.size;
		int nbrVehicles = game.nbrVehicles;
		Vehicle[] vehicles = game.vehicles;
		int[] pos = (game.initialState).pos;
		this.pan = new GamePanel(size, nbrVehicles, vehicles, pos);
		
		JPanel b = new JPanel();
		FlowLayout g = new FlowLayout();
		b.setLayout(g);
		b.add(bouton);
		b.add(newGame);

		container.add(pan, BorderLayout.CENTER);
		container.add(b, BorderLayout.SOUTH);
		
		setContentPane(container);
		
		bouton.addActionListener(new BoutonSolveListener());
		newGame.addActionListener(new NewGameListener());
		
		setVisible(true);
	}
	
	public void movement(Move m, boolean last, char orientation){
		pan.setMove(m);
		int timeFreeze = 3;
		int distance = m.distance;
		int t = 0;
		if (orientation == 'h'){
			t = pan.getWidth();
		}
		else t = pan.getHeight();
		if (distance >= 0) {
			if (last){
				distance += game.vehicles[m.id-1].length;
			}
			for (int d = 0; d < distance*(98*t/100) / game.size; d++) {
				pan.setdistance(d);
				pan.repaint();
				try {
			        Thread.sleep(timeFreeze);
			      } catch (InterruptedException e) {
			        e.printStackTrace();
			      }
			}
		}
		else {
			if (last){
				distance -= game.vehicles[m.id-1].length;
			}
			for (int d = 0; d > distance*(98*t/100) / game.size; d--) {
				pan.setdistance(d);
				pan.repaint();
				try {
			        Thread.sleep(timeFreeze);
			      } catch (InterruptedException e) {
			        e.printStackTrace();
			      }
			}
		}
		pan.setpos(m.id-1, pan.getpos(m.id-1) + distance);
		
	}
	
	class BoutonSolveListener implements ActionListener{
		
		public void actionPerformed(ActionEvent arg0) {
			if (reset) {
				solution = null;
				solved = false;
				bouton.setName("Solve");
				
				try {
					game = new Game(path + file_name, typeheuristic, brutForce);
				}
				catch (IOException e){
					e.printStackTrace();
				}
				int[] pos = (game.initialState).pos;
				pan.init(pos);
				setContentPane(container);
				reset = false;
			}
			else {
				if (!solved){
					LinkedList<Move> sol = game.solve();
					
					int n = sol.size();
					for (int i = 0; i < n; i++) {
						
					}
					solution = sol;
					solved = true;
					bouton.setName("Solution");
				}
				else {
					if (solution != null) {
						t = new Thread(new PlayAnimation());
						t.start();
					}
					else {
						pan.setImpossible(true);
						pan.repaint();
					}
					bouton.setName("Reset");
					reset = true;
				}
			}
			
		}
	}
	
	class NewGameListener implements ActionListener{
		 public void actionPerformed(ActionEvent arg0){
			
			try {
				LoadingWindow gui = new LoadingWindow(typeheuristic, brutForce);
				gui.setVisible(true);
				setVisible(false);
			 }
			catch (IOException e){
				e.printStackTrace();
			}
			
		 }
	}
	
	class PlayAnimation implements Runnable{
		
		public void run(){
			while (!solution.isEmpty()){
				Move m = solution.remove();
				if (solution.isEmpty()){
					movement(m, true, game.vehicles[m.id-1].orientation);
				}
				else {
				movement(m, false, game.vehicles[m.id-1].orientation);
				}
			}
		}
	}
}
