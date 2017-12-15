package GraphicGame;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.LinkedList;

import javax.swing.JFrame;
import javax.swing.JPanel;

import game.Game;
import game.Move;
import game.Vehicle;

public class GameWindow extends JFrame {
	String file_name;
	Game game;
	JFrame window;
	LinkedList<Move> solution;
	private GamePanel pan;
	private JPanel container = new JPanel();
	private Bouton bouton = new Bouton("Solve");
	private Bouton newGame = new Bouton("New Game");
	boolean reset;
	private Thread t;
	String path = "Games/";
	
	public GameWindow(String file_name, String windowName) throws IOException{
		super(windowName);
		this.game = new Game(path + file_name);
		this.solution = null;
		this.file_name = file_name;
		this.reset = false;
		
		setSize(750,750);
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
	
	public void movement(Move m, boolean last){
		pan.setMove(m);
		int distance = m.distance;
		if (distance >= 0) {
			if (last){
				distance += game.vehicles[m.id-1].length;
			}
			for (int d = 0; d < distance*(pan.getWidth()) / game.size; d++) {
				pan.setdistance(d);
				pan.repaint();
				try {
			        Thread.sleep(3);
			      } catch (InterruptedException e) {
			        e.printStackTrace();
			      }
			}
		}
		else {
			if (last){
				distance -= game.vehicles[m.id-1].length;
			}
			for (int d = 0; d > distance*(pan.getWidth()) / game.size; d--) {
				pan.setdistance(d);
				pan.repaint();
				try {
			        Thread.sleep(3);
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
				bouton.setName("Solve");
				
				try {
					game = new Game(path + file_name);
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
				if (solution == null){
					LinkedList<Move> sol = game.solve();
					solution = sol;
					bouton.setName("Solution");
				}
				else {
					t = new Thread(new PlayAnimation());
					t.start();
					bouton.setName("Reset");
					reset = true;
				}
			}
			
		}
	}
	
	class NewGameListener implements ActionListener{
		 public void actionPerformed(ActionEvent arg0){
			
			try {
				LoadingWindow gui = new LoadingWindow();
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
					movement(m, true);
				}
				else {
				movement(m, false);
				}
			}
		}
	}
}
