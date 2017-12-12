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

public class Window extends JFrame{
	Game game;
	JFrame window;
	LinkedList<Move> solution;
	private Panneau pan;
	private JPanel container = new JPanel();
	private Bouton boutonSolution = new Bouton("Solution");
	private Bouton boutonSolve = new Bouton("Solve");
	private Thread t;
	
	public Window(String file_name) throws IOException{
		super("Game");
		this.game = new Game(file_name);
		this.solution = null;
		
		setSize(750,750);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		container.setBackground(Color.white);
		container.setLayout(new BorderLayout());
		
		int size = game.size;
		int nbrVehicles = game.nbrVehicles;
		Vehicle[] vehicles = game.vehicles;
		int[] pos = (game.initialState).pos;
		this.pan = new Panneau(size, nbrVehicles, vehicles, pos);
		setContentPane(pan);
		
		JPanel b = new JPanel();
		//GridLayout g = new GridLayout(1, 2, 5, 5);
		FlowLayout g = new FlowLayout();
		b.setLayout(g);
		b.add(boutonSolution);
		b.add(boutonSolve);
		container.add(pan, BorderLayout.CENTER);
		container.add(b, BorderLayout.SOUTH);
		this.setContentPane(container);
		
		boutonSolution.addActionListener(new BoutonSolutionListener());
		boutonSolution.setEnabled(false);
		
		boutonSolve.addActionListener(new BoutonSolveListener());
		
		setVisible(true);
	}
	
	public void movement(Move m){
		pan.setMove(m);
		int distance = m.distance;
		if (distance >= 0) {
			for (int d = 0; d < distance*(pan.getWidth()) / game.size; d++) {
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
			for (int d = 0; d > distance*(pan.getWidth()) / game.size; d--) {
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
	
	class BoutonSolveListener implements ActionListener{
		
		public void actionPerformed(ActionEvent arg0) {
			LinkedList<Move> sol = game.solve();
			solution = sol;
			boutonSolution.setEnabled(true);
			boutonSolve.setEnabled(false);
		}
	}
	
	class BoutonSolutionListener implements ActionListener{
		
		public void actionPerformed(ActionEvent arg0) {
			t = new Thread(new PlayAnimation());
			t.start();
			boutonSolution.setEnabled(false);
		}
	}
	
	class PlayAnimation implements Runnable{
		
		public void run(){
			while (!solution.isEmpty()){
				Move m = solution.remove();
				movement(m);
			}
		}
	}
	
	
	public static void main(String[] args) throws IOException{
		Window gui = new Window("file.txt");
		gui.setVisible(true);
		/*LinkedList<Move> sol = gui.game.solve();
		gui.solution = sol;
		while (!gui.solution.isEmpty()){
			Move m = gui.solution.remove();
			gui.movement(m);
		}*/
		/*LinkedList<Move> sol = (gui.game).solve();
		try {
	        Thread.sleep(1000);
	      } catch (InterruptedException e) {
	        e.printStackTrace();
	      }
		while (!sol.isEmpty()){
			Move m = sol.remove();
			gui.movement(m);
		}*/
	}
}
