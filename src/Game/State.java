package Game;
import java.util.LinkedList;

public class State {
	//Classe représentant un état par :
	//	- la liste des positions non fixe des véhicules
	//	- un tableau de taille size*size tel que isOccupied[i][j] est l'id du véhicule occupant la case (i, j) et -1 si la case est libre
	//	- la valeur de l'heuristic en cet état
	// 	- la distance, pour l'instant connu, de cet état à l'état initial.
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
		//Constructeur créant l'état obtenu suite au mouvement m sur l'état parent.

		//initialise l'état comme son parent
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

		//le véhicule qui va se déplacer est supprimé de isOccupied
		for (int i=pos[m.id - 1]; i<=pos[m.id - 1]+vehicles[m.id - 1].length - 1; i++) {
			if (vehicles[m.id - 1].orientation=='h')
				isOccupied[i - 1][vehicles[m.id-1].fixedPos - 1] = 0;
			else
				isOccupied[vehicles[m.id-1].fixedPos-1][i-1] = 0;
		}

		//La position du véhicule est mise à jour suite à son mouvement
		pos[m.id-1] += m.distance;

		//Le véhicule qui vient de se déplacer est remis dans isOccupied
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
		//Récupère l'état précédent le mouvement m, et ce en ne changeant que pos, à utiliser que pour remonter à la fin.
		int id = m.id-1;
		this.pos[id] = this.pos[id] - m.distance;
	}

	public LinkedList<Move> possibleMoves(Vehicle[] vehicles, int size) {
		//Renvoie la liste des mouvements possibles à partir d'un état.
		LinkedList<Move> nextMoves = new LinkedList<Move>();
		int distance;
		int n = pos.length;
		for (int id=0; id < n; id++) {
			if (vehicles[id].orientation=='h') {
				//Mouvement à gauche
				distance = -1;
				while (pos[id] - 1 + distance >= 0 && (isOccupied[pos[id] - 1 + distance][vehicles[id].fixedPos - 1] == 0)) {
					nextMoves.add(new Move(id+1, distance));
					distance -= 1;
				}
				//Mouvement à droite
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
		//Récupère le dernier mouvement fait pour arriver dans l'état actuel.
		//Cette méthode ne doit être utilisée que sur un état déjà visité.		
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
