package Game;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;

public class Game {

	public int size; //Taille du plateau
	public int nbrVehicles; //Nombre de v�hicules
	public Vehicle[] vehicles; //Tableau d'informations sur chaque v�hicule, informations qui ne vont pas changer au cours de la partie
	public State initialState; //Repr�sente l'�tat initial
	Heuristic heuristic; //Donne l'heuristique utilis�e si il y a lieu
	boolean bruteForce; //Indique si on souhaite r�soudre avec une heuristique ou non


	public Game(String file_name, int typeheuristic, boolean brutForce) throws IOException {
		//Initialise une partie et v�rifie que la partie est correcte
		BufferedReader br = new BufferedReader(new FileReader(file_name));
		String line;
		int size = Integer.valueOf(br.readLine()); //On r�cup�re la taille dans le fichier
		int nbrVehicles = Integer.valueOf(br.readLine()); //On r�cup�re le nomre de v�hicules dans le fichier
		String[] line_split;
		Vehicle[] pos = new Vehicle[nbrVehicles];
		int[] posInit = new int[nbrVehicles];
		int[][] isOccupied = new int[size][size]; // isOccupied contient l'id de la voiture qui occupe la case, ou 0 si la case est vide.
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++)
				isOccupied[i][j] = 0;
		}
		
		for (int k = 0; k < nbrVehicles; k++) {//Pour chaque ligne
			line = br.readLine();
			line_split = line.split(" ");
			int id = Integer.valueOf(line_split[0]);
			char orientation = line_split[1].charAt(0);
			int length = Integer.valueOf(line_split[2]);
			int x = Integer.valueOf(line_split[3]);
			int y = Integer.valueOf(line_split[4]);
			Vehicle v;
			//On cr�er un nouveau v�hicule et on enregistre ses informations
			if (orientation == 'h') {
				v = new Vehicle(id, length, orientation, y);
				pos[k] = v;
				posInit[k] = x;
				if ((x-1) + (length - 1) >= size) { //Indique si une voiture sort du plateau ou non
					System.out.println("Car number "+id+" is too big");
				}
				else {
					for (int l = x - 1; l < x - 1 + length; l++) {
						if (isOccupied[l][y-1] != 0) //Indique si deux voitures sont en confrontation
							System.out.println("Car number "+id+" and car number "+ isOccupied[l][y-1]+" overlap");
						else
							isOccupied[l][y-1] = v.id;
					}
				}
			}

			else { //Idem si la voiture est verticale
				v = new Vehicle(id, length, orientation, x);
				pos[k] = v;
				posInit[k] = y;
				if ((y-1) + (length - 1) >= size) {
					System.out.println("Car number "+id+" is too big");
				}
				else {
					for (int l = y - 1; l < y - 1 + length; l++) {
						if (isOccupied[x-1][l] != 0)
							System.out.println("Car number "+id+" and car number "+ isOccupied[x-1][l-1]+" overlap");
						else
							isOccupied[x-1][l] = v.id;
					}

				}
			}

		}
		br.close();

		this.size = size;
		this.nbrVehicles = nbrVehicles;
		this.vehicles = pos;
		this.initialState = new State(posInit, isOccupied);
		this.heuristic = new Heuristic(typeheuristic);
		this.bruteForce = brutForce;
	}
	
	public double[] compte_etats(int n){
		double[] res = new double[n+1];
		for (int i = 0; i < n + 1; i++){
			res[i] = 0;
		}
		State s = this.initialState;
		int d = 0;
		SeenStates seen = new SeenStates(this.bruteForce);
		seen.add(s, new Move(1, 0), this.nbrVehicles, this.size);
		Queue<Couple> q = new ArrayDeque<Couple>();
		q.add(new Couple(d, s));
		while (!q.isEmpty()) {
			Couple c = q.remove();
			s = c.etat;
			d = c.distance;
			res[d] += 1;
			LinkedList<Move> moves = s.possibleMoves(this.vehicles, this.size); //On regarde tous les mouvements possibles
			
			for (Move m : moves){
				State s2 = new State(s, m, this.vehicles);
				if (seen.add(s2, m, this.nbrVehicles, this.size)[0] == -1){ //Si l'�tat s2 n'est pas d�j� vu on le marque comme vu (ce qui se fait dans add)
					q.add(new Couple(d+1, s2)); //Puis on l'ajoute � la file des �tats � visiter
				}
			}
		}
		return res;
	}


	public LinkedList<Move> solve() {
		//R�sout une partie en renvoyant la liste des mouvements � effectuer depuis l'�tat initial
		long startingTime = System.currentTimeMillis();
		LinkedList<Move> res = new LinkedList<Move>();
		int numberSeenStates = 0; //Pour connaitre le nombre d'�tats visit�s
		State s = this.initialState;
		SeenStates seen = new SeenStates(this.bruteForce);
		seen.add(s, new Move(1, 0), this.nbrVehicles, this.size); //On ajoute l'�tat initial comme �tat vu
		boolean solved = false;
		
		//Si on utilise la m�thode brute force on va utiliser un file pour faire un parcours en largeur de l'arbre des diff�rents �tats possibles
		//De cette mani�re si on arrive sur un �tat d�j� vu c'est qu'on a d�j� un chemin plus court menant � lui, on peut donc l'ignorer.
		if (this.bruteForce) {
			Queue<State> q = new ArrayDeque<State>();
			q.add(s);

			while ((!q.isEmpty()) && !solved) {
				numberSeenStates += 1;
				s = q.remove();
				//On teste si on est arriv� � la fin de la partie
				int posFirstVehicle = s.pos[0];
				int lengthFirstVehicle = (this.vehicles[0]).length;
				if (posFirstVehicle + lengthFirstVehicle - 1 == this.size) {
					solved = true;
				}
				else {
					LinkedList<Move> moves = s.possibleMoves(this.vehicles, this.size); //On regarde tous les mouvements possibles
					for (Move m : moves){
						State s2 = new State(s, m, this.vehicles);
						if (seen.add(s2, m, this.nbrVehicles, this.size)[0] == -1){ //Si l'�tat s2 n'est pas d�j� vu on le marque comme vu (ce qui se fait dans add)
							q.add(s2); //Puis on l'ajoute � la file des �tats � visiter
						}
					}
				}
			}
		}
		
		
		//Si on souhaite utiliser une heuristique, on va alors utiliser une file de priorit� afin de prendre les �tats par distance + heuristique croissante
		else {
			PriorityQueue<State> q = new PriorityQueue<State>(nbrVehicles, new ComparatorState());
			s.setheuristic(heuristic, vehicles, size);
			s.setdistance(0);
			q.add(s);
			
			while ((!q.isEmpty()) && !solved) {
				
				s = q.poll(); //On r�cup�re l'�tat le plus proche au sens distance + heuristique. Sa distance actuelle � l'�tat initial est alors sa distance minimale (cf preuve correction).
				if (seen.isExplored(s, this.nbrVehicles, this.size) == 0){ //Si l'�tat a d�j� �t� explor� c'est qu'on a d�j� trouv� sa distance minimale a l'�tat initial. On ne regarde donc que des �tats non explor�s.
					numberSeenStates += 1;
					seen.explore(s, this.nbrVehicles, this.size); //On marque cet �tat comme explor� puisqu'on a trouv� sa distance � l'�tat initial.
					//On teste si on est arriv� � la fin de la partie
					int posFirstVehicle = s.pos[0];
					int lengthFirstVehicle = (this.vehicles[0]).length;
					if (posFirstVehicle + lengthFirstVehicle - 1 == this.size) {
						solved = true;
					}
					else {
						LinkedList<Move> moves = s.possibleMoves(this.vehicles, this.size);
						for (Move m : moves){ //Pour chaque mouvement possible
							State s2 = new State(s, m, this.vehicles);
							s2.setdistance(s.distance + 1); //On connait un chemin de longueur s.distance + 1 � l'�tat initial.
							int[] vu = seen.add(s2, m, this.nbrVehicles, this.size);
							
							if (vu[0] == -1){ //Si l'�tat n'avait pas du tout �t� d�j� vu
								s2.setheuristic(heuristic, vehicles, size); //On calcule sont heuristique
								seen.changedHeuristic(s2, this.nbrVehicles, this.size); //Qu'on enregistre dans seen
								q.add(s2); //On ajoute s2 � la liste des �tats � explorer
							}
							else{ //Si on avait d�j� vu cet �tat
								if (vu[1] == 0){ //Si il n'est pas d�j� explor�
									if (s.distance + 1 < vu[0]){ //Et si ce nouveau chemin est plus court que l'ancien qu'on avait trouv�
										s2.heuristic = vu[2];
										q.add(s2); //On ajouter s2 � la liste des �tats � explorer. On n'a pas besoin de supprimer l'ancien �tat qu'on avait ajout� car le nouvel �tat sera dans tous les cas trait� avant.
									}
								}
							}
						}
					}
				}
			}

		}

		LinkedList<Move> resInv = new LinkedList<Move>();

		if (solved) { //Si on a r�ussi � r�soudre la partie, on remonte en partant de l'�tat final pour obtenir la liste des mouvements � faire
			Move lastmove = s.getLastMove(seen, this.nbrVehicles, this.size);
			s.Previous(lastmove);
			res.add(lastmove);
			while (!s.equal(this.initialState)){
				lastmove = s.getLastMove(seen, this.nbrVehicles, this.size);
				s.Previous(lastmove);
				res.add(lastmove);
			}


			Iterator<Move> ite = res.descendingIterator();
			while (ite.hasNext()) { //On inverse ensuite la liste pour l'avoir dans le bon sens.
				resInv.add(ite.next());
			}
			System.out.println("Execution time : "+ (System.currentTimeMillis()-startingTime)+ "ms");
			System.out.println("Number of explored states : "+numberSeenStates);
			System.out.println("Length of solution : "+resInv.size());
		}
		else {
			resInv = null;
			System.out.println("Impossible");
		}

		return resInv;
	}

	public int[] solveStat() {
		//M�me code que la m�thode solve mais cette fois on m�morise et on renvoie
		//[temps d'execution, nombre d'�tats visit�s, longueur de la solution]
		long startingTime = System.currentTimeMillis();

		LinkedList<Move> res = new LinkedList<Move>();
		int numberSeenStates = 0;
		State s = this.initialState;
		SeenStates seen = new SeenStates(this.bruteForce);
		seen.add(s, new Move(1, 0), this.nbrVehicles, this.size);
		boolean solved = false;

		if (this.bruteForce) {
			Queue<State> q = new ArrayDeque<State>();
			q.add(s);

			while ((!q.isEmpty()) && !solved) {
				numberSeenStates += 1;
				s = q.remove();
				int posFirstVehicle = s.pos[0];
				int lengthFirstVehicle = (this.vehicles[0]).length;
				if (posFirstVehicle + lengthFirstVehicle - 1 == this.size) {
					solved = true;
				}
				else {
					LinkedList<Move> moves = s.possibleMoves(this.vehicles, this.size);
					for (Move m : moves){
						State s2 = new State(s, m, this.vehicles);
						if (seen.add(s2, m, this.nbrVehicles, this.size)[0] == -1){
							q.add(s2);
						}
					}
				}
			}
		}

		else {
			PriorityQueue<State> q = new PriorityQueue<State>(nbrVehicles, new ComparatorState());
			s.setheuristic(heuristic, vehicles, size);
			s.setdistance(0);
			q.add(s);
			

			while ((!q.isEmpty()) && !solved) {
				
				s = q.poll();
				if (seen.isExplored(s, this.nbrVehicles, this.size) == 0){
					numberSeenStates += 1;
					int posFirstVehicle = s.pos[0];
					int lengthFirstVehicle = (this.vehicles[0]).length;
					if (posFirstVehicle + lengthFirstVehicle - 1 == this.size) {
						solved = true;
					}
					else {
						LinkedList<Move> moves = s.possibleMoves(this.vehicles, this.size);
						for (Move m : moves){
							State s2 = new State(s, m, this.vehicles);
							s2.setdistance(s.distance + 1);
							int[] vu = seen.add(s2, m, this.nbrVehicles, this.size);
							
							if (vu[0] == -1){
								s2.setheuristic(heuristic, vehicles, size);
								seen.changedHeuristic(s2, this.nbrVehicles, this.size);
								q.add(s2);
							}
							else{
								if (vu[1] == 0){
									if (s.distance + 1 < vu[0]){
										s2.heuristic = vu[2];
										q.add(s2);
									}
								}
							}
						}
					}
				}
			}

		}

		LinkedList<Move> resInv = new LinkedList<Move>();

		if (solved) {
			Move lastmove = s.getLastMove(seen, this.nbrVehicles, this.size);
			s.Previous(lastmove);
			res.add(lastmove);
			while (!s.equal(this.initialState)){
				lastmove = s.getLastMove(seen, this.nbrVehicles, this.size);
				s.Previous(lastmove);
				res.add(lastmove);
			}


			Iterator<Move> ite = res.descendingIterator();
			while (ite.hasNext()) {
				resInv.add(ite.next());
			}
		}
		else {
			resInv = null;
			System.out.println("Impossible"); 
		}
		int[] result = new int[3];
		result[0] = ((Long) (System.currentTimeMillis()-startingTime)).intValue();
		result[1] = numberSeenStates;
		if (resInv == null)
			result[2] = -1;
		else
			result[2] = resInv.size();
		return result ;
	}
	

}