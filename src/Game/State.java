package Game;
import java.util.LinkedList;

public class State {
	//Classe repr�sentant un �tat par :
	//	- la liste des positions non fixe des v�hicules
	//	- un tableau de taille size*size tel que isOccupied[i][j] est l'id du v�hicule occupant la case (i, j) et -1 si la case est libre
	//	- la valeur de l'heuristic en cet �tat
	// 	- la distance, pour l'instant connu, de cet �tat � l'�tat initial.
	public int[] pos;
	int[][] isOccupied;
	int distance;
	int heuristic;


	public State(int[] pos, int[][] isOccupied) {
		this.pos = pos;
		this.isOccupied = isOccupied;
		this.heuristic = -1;
		this.distance = -1;
	}
	
	public State(State parent, Move m, Vehicle[] vehicles) {
		//Constructeur cr�ant l'�tat obtenu suite au mouvement m sur l'�tat parent.

		//initialise l'�tat comme son parent
		int nbrVehicles = parent.pos.length;
		this.pos = new int[nbrVehicles];
		for (int k = 0; k < nbrVehicles; k++){
			this.pos[k] = parent.pos[k];
		}
		int size = (parent.isOccupied).length;
		int[][] t = new int[size][size];
		for (int k = 0; k < size; k++){
			for (int j = 0; j < size; j++) {
				t[k][j] = parent.isOccupied[k][j];
			}
		}
		this.isOccupied = t;

		//le v�hicule qui va se d�placer est supprim� de isOccupied
		for (int i=pos[m.id - 1]; i<=pos[m.id - 1]+vehicles[m.id - 1].length - 1; i++) {
			if (vehicles[m.id - 1].orientation=='h')
				isOccupied[i - 1][vehicles[m.id-1].fixedPos - 1] = 0;
			else
				isOccupied[vehicles[m.id-1].fixedPos-1][i-1] = 0;
		}

		//La position du v�hicule est mise � jour suite � son mouvement
		pos[m.id-1] += m.distance;

		//Le v�hicule qui vient de se d�placer est remis dans isOccupied
		for (int i=pos[m.id-1]; i<=pos[m.id-1]+vehicles[m.id-1].length - 1; i++) {
			if (vehicles[m.id-1].orientation=='h'){
				isOccupied[i-1][vehicles[m.id-1].fixedPos-1] = m.id;
			}
			else
				isOccupied[vehicles[m.id-1].fixedPos-1][i-1] = m.id;
		}

	}
	
	public void setheuristic(Heuristic h, Vehicle[] v, int size){
		this.heuristic = h.value(this, v, size);
	}
	
	public void setdistance(int d){
		this.distance = d;
	}
	
	
	public boolean equal(State s2) {
		int[] pos1 = this.pos;
		int[] pos2 = s2.pos;
		int n = pos1.length;
		for (int i = 0; i < n; i++) {
			if (pos1[i] != pos2[i]) {
				return(false);
			}
		}
		return(true);
	}
	
	public String toString(){
		String c = "";
		for (int i = 0; i < pos.length; i++){
			c = c + Integer.toString(pos[i]) + "/";
		}
		
		String d = "";
		for (int j = 0; j < isOccupied.length; j ++){
			for (int i = 0; i < isOccupied.length; i++){
				d += isOccupied[i][j];
			}
			d += "\n";
		}
		return(c + "\n" + d);
	}
	
	public void Previous(Move m){
		//R�cup�re l'�tat pr�c�dent le mouvement m, et ce en ne changeant que pos, � utiliser que pour remonter � la fin.
		int id = m.id-1;
		this.pos[id] = this.pos[id] - m.distance;
	}

	public LinkedList<Move> possibleMoves(Vehicle[] vehicles, int size) {
		//Renvoie la liste des mouvements possibles � partir d'un �tat.
		LinkedList<Move> nextMoves = new LinkedList<Move>();
		int distance;
		int n = pos.length;
		for (int id=0; id < n; id++) {
			if (vehicles[id].orientation=='h') {
				//Mouvement � gauche
				distance = -1;
				while (pos[id] - 1 + distance >= 0 && (isOccupied[pos[id] - 1 + distance][vehicles[id].fixedPos - 1] == 0)) {
					nextMoves.add(new Move(id+1, distance));
					distance -= 1;
				}
				//Mouvement � droite
				distance = 1;
				while (pos[id] - 1 + vehicles[id].length - 1 + distance < size && (isOccupied[pos[id] - 1 + vehicles[id].length - 1 + distance][vehicles[id].fixedPos - 1] == 0)) {
					nextMoves.add(new Move(id+1, distance));
					distance += 1;
				}
					
			}
			else {
				//Mouvement en haut
				distance = -1;
				while (pos[id] - 1 + distance >= 0 && (isOccupied[vehicles[id].fixedPos - 1][pos[id] - 1 + distance] == 0)) {
					nextMoves.add(new Move(id+1, distance));
					distance -= 1;
				}
				//Mouvement en bas
				distance = 1;
				while (pos[id] - 1 + vehicles[id].length - 1 + distance < size && (isOccupied[vehicles[id].fixedPos - 1][pos[id] + vehicles[id].length - 1 + distance - 1] == 0)){
					nextMoves.add(new Move(id+1, distance));
					distance += 1;
				}
			}  				
		}
		return (nextMoves);
	}


	public Move getLastMove(SeenStates T, int nbrVehicles, int size) {
		//R�cup�re le dernier mouvement fait pour arriver dans l'�tat actuel.
		//Cette m�thode ne doit �tre utilis�e que sur un �tat d�j� visit�.		
		int[] pos = this.pos;
		SeenStates tree = T;
		for (int i = 0; i < nbrVehicles; i++) {
			SeenStates[] child = tree.children;
			int x = pos[i];
			tree = child[x-1];
		}
		return tree.lastmove;
	}
}
