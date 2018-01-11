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

import Game.Game;
import Game.Move;
import Game.Vehicle;


public class GameWindow extends JFrame {
	//Affiche l'intégralité de la fenêtre contenant la partie et les boutons.
	static final long serialVersionUID = 3;
	
	String file_name;
	Game game;
	LinkedList<Move> solution; //Solution de la partie
	boolean solved; //Est-ce-que la solution de la partie a déjà été calculée ?
	private GamePanel pan; //Panel contenant la grille de la partie
	private JPanel container = new JPanel(); //Panel général contenant le Panel de la grille et les boutons
	private Bouton bouton = new Bouton("Solve"); //Bouton Solve/Solution/Reset
	private Bouton newGame = new Bouton("New Game"); //Bouton pour changer de partie
	boolean reset; //Est-ce-que la bouton "bouton" est en position reset ?
	private Thread t;
	String path = "Games/"; //Chemin d'accès où récupérer les parties
	int typeheuristic; //Heuristic à utiliser pour la résolution
	boolean brutForce; //Est-ce-qu'on utilise une heuristic ?
	
	public GameWindow(String file_name, String windowName, int type, boolean brutForce) throws IOException{
		//Initialise la fenêtre, avec les différents Panel, Bouton et leur agencement.
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
		//Affiche un mouvement en faisant avancer petit à petit la voiture concernée et en redessinant la grille à chaque fois.
		//Si c'est le dernier mouvement (i.e. last = True), on avance de la longueur de la voiture en plus pour faire "sortir" la voiture de l'écran.
		pan.setMove(m); 
		int timeFreeze = 2; //Temps entre deux avancement, plus timeFreeze est petit plus la voiture va se déplacer vite
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
		//Implémente l'action du Bouton "bouton".
		public void actionPerformed(ActionEvent arg0) {
			if (reset) { //Si il faut réinitialiser la partie
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
				if (!solved){ //Si on a pas encore calculé la solution, on la calcule
					LinkedList<Move> sol = game.solve();
					
					solution = sol;
					solved = true;
					bouton.setName("Solution");
				}
				else { //Si la solution est calculée, on l'affiche
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
		//Implémente le bouton "New Game" : on ferme la fenêtre actuelle et on réouvre la fenêtre de chargement de partie.
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
		//Implémente l'affichage de la solution en déplaçant chacune leur tour chaque voiture de "solution".
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
