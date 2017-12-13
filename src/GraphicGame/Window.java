package GraphicGame;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.LinkedList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import game.Game;
import game.Move;
import game.Vehicle;

public class Window extends JFrame {
	String file_name;
	Game game;
	JFrame window;
	LinkedList<Move> solution;
	private Panneau pan;
	private JPanel container = new JPanel();
	private Bouton boutonSolve = new Bouton("Solve");
	private Bouton boutonInit = new Bouton("Init");
	private Thread t;
	
	public Window(String file_name) throws IOException{
		super("Game");
		this.game = new Game(file_name);
		this.solution = null;
		this.file_name = file_name;
		
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
		b.add(boutonSolve);
		b.add(boutonInit);
		boutonInit.setEnabled(false);

		container.add(pan, BorderLayout.CENTER);
		container.add(b, BorderLayout.SOUTH);
		
		this.setContentPane(container);
		
		boutonSolve.addActionListener(new BoutonSolveListener());
		boutonInit.addActionListener(new BoutonInitListener());
		
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
			if (solution == null){
				LinkedList<Move> sol = game.solve();
				solution = sol;
				boutonSolve.setName("Solution");
			}
			else {
				t = new Thread(new PlayAnimation());
				t.start();
				boutonSolve.setEnabled(false);
				boutonInit.setEnabled(true);
			}
			
		}
	}
	
	class BoutonInitListener implements ActionListener  {
		
		public void actionPerformed(ActionEvent arg0) {

			solution = null;
			boutonSolve.setName("Solve");
			boutonSolve.setEnabled(true);
			boutonInit.setEnabled(false);
			
			try {
				game = new Game(file_name);
			}
			catch (IOException e){
				e.printStackTrace();
			}
			int[] pos = (game.initialState).pos;
			pan.init(pos);
			repaint();
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
	
	public static void main(String[] args) throws IOException{
		Window gui = new Window("file.txt");
		gui.setVisible(true);
	}
}
