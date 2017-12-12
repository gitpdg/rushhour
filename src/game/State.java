package game;
import java.util.LinkedList;

public class State {
	int[] pos;
	boolean[][] isOccupied;


	public State(int[] pos, boolean[][] isOccupied) {
		this.pos = pos;
		this.isOccupied = isOccupied;
	}
	
	public State(State parent, Move m, Vehicle[] vehicles) {
		//this constructor is used to create the state obtained when performing a move m on a state parent.

		//initialized as parent
		int nbrVehicles = parent.pos.length;
		this.pos = new int[nbrVehicles];
		for (int k = 0; k < nbrVehicles; k++){
			this.pos[k] = parent.pos[k];
		}
		int size = (parent.isOccupied).length;
		boolean[][] t = new boolean[size][size];
		for (int k = 0; k < size; k++){
			for (int j = 0; j < size; j++) {
				t[k][j] = parent.isOccupied[k][j];
			}
		}
		this.isOccupied = t;

		//the vehicle that is to move is deleted from isOccupied
		for (int i=pos[m.id - 1]; i<=pos[m.id - 1]+vehicles[m.id - 1].length - 1; i++) {
			if (vehicles[m.id - 1].orientation=='h')
				isOccupied[i - 1][vehicles[m.id-1].fixedPos - 1] = false;
			else
				isOccupied[vehicles[m.id-1].fixedPos-1][i-1] = false;
		}

		//the vehicle is moved
		pos[m.id-1] += m.distance;

		//the vehicle that was moved is put back in isOccupied
		for (int i=pos[m.id-1]; i<=pos[m.id-1]+vehicles[m.id-1].length - 1; i++) {
			if (vehicles[m.id-1].orientation=='h'){
				isOccupied[i-1][vehicles[m.id-1].fixedPos-1] = true;
			}
			else
				isOccupied[vehicles[m.id-1].fixedPos-1][i-1] = true;
		}

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
		
		/*String d = "";
		for (int j = 0; j < isOccupied.length; j ++){
			for (int i = 0; i < isOccupied.length; i++){
				if (isOccupied[i][j]) {
					d += "1";
				}
				else {
					d += "0";
				}
			}
			d += "\n";
		}
		return(c + "\n" + d);*/
		return(c);
	}
	
	public void Previous(Move m){
		//Récupère l'état d'avant en ne changeant que pos, à utiliser que pour remonter à la fin !
		int id = m.id-1;
		this.pos[id] = this.pos[id] - m.distance;
	}

	public LinkedList<Move> possibleMoves(Vehicle[] vehicles, int size) {
		LinkedList<Move> nextMoves = new LinkedList<Move>();
		int distance;
		int n = pos.length;
		for (int id=0; id < n; id++) {
			if (vehicles[id].orientation=='h') {
				//moving left
				distance = -1;
				while (pos[id] - 1 + distance >= 0 && !isOccupied[pos[id] - 1 + distance][vehicles[id].fixedPos - 1]) {
					nextMoves.add(new Move(id+1, distance));
					distance -= 1;
				}
				//moving right
				distance = 1;
				while (pos[id] - 1 + vehicles[id].length - 1 + distance < size && !isOccupied[pos[id] - 1 + vehicles[id].length - 1 + distance][vehicles[id].fixedPos - 1]) {
					nextMoves.add(new Move(id+1, distance));
					distance += 1;
				}
					
			}
			else {
				//moving up
				distance = -1;
				while (pos[id] - 1 + distance >= 0 && !isOccupied[vehicles[id].fixedPos - 1][pos[id] - 1 + distance]) {
					nextMoves.add(new Move(id+1, distance));
					distance -= 1;
				}
				//moving down
				distance = 1;
				while (pos[id] - 1 + vehicles[id].length - 1 + distance < size && !isOccupied[vehicles[id].fixedPos - 1][pos[id] + vehicles[id].length - 1 + distance - 1]){
					nextMoves.add(new Move(id+1, distance));
					distance += 1;
				}
			}  				
		}
		return (nextMoves);
	}


	public Move getLastMove(SeenStates T, int nbrVehicles, int size) {
		//Attention à bien appeler cette méthode sur un état déja visité sinon ça va faire de la merde
		
		int[] pos = this.pos;
		SeenStates tree = T;
		for (int i = 0; i < nbrVehicles; i++) {
			SeenStates[] child = tree.children;
			int x = pos[i];
			tree = child[x-1];
		}
		return tree.lastmove;
	}
	
	/*
  	void print();
	 */
}
