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
		this.pos = parent.pos;
		int size = parent.isOccupied.length;
		boolean[][] t = new boolean[size][size];
		for (int k = 0; k < size; k++){
			for (int j = 0; j < size; k++) {
				t[k][j] = parent.isOccupied[k][j];
			}
		}
		this.isOccupied = t;

		//the vehicle that is to move is deleted from isOccupied
		for (int i=pos[m.id]; i<pos[m.id]+vehicles[m.id].length; i++) {
			if (vehicles[m.id].orientation=='h')
				isOccupied[vehicles[m.id].fixedPos][i] = false;
			else
				isOccupied[i][vehicles[m.id].fixedPos] = false;
		}

		//the vehicle is moved
		pos[m.id] += m.distance;

		//the vehicle that was moved is put back in isOccupied
		for (int i=pos[m.id]; i<pos[m.id]+vehicles[m.id].length; i++) {
			if (vehicles[m.id].orientation=='h')
				isOccupied[vehicles[m.id].fixedPos][i] = true;
			else
				isOccupied[i][vehicles[m.id].fixedPos] = true;
		}

	}

	public LinkedList<Move> possibleMoves(Vehicle[] vehicles, int size) {
		LinkedList<Move> nextMoves = new LinkedList<Move>();
		int distance;
		int n = pos.length;
		for (int id=0; id < n; id++) {
			if (vehicles[id].orientation=='h') {
				//moving left
				distance = -1;
				while (pos[id] + distance >0 && !isOccupied[vehicles[id].fixedPos][pos[id] + distance])
					nextMoves.add(new Move(id, distance));
				//moving right
				distance = 1;
				while (pos[id] + vehicles[id].length - 1 + distance < size && !isOccupied[vehicles[id].fixedPos][pos[id] + vehicles[id].length + distance])
					nextMoves.add(new Move(id, distance));
			}
			else {
				//moving up
				distance = -1;
				while (pos[id] + distance >0 && !isOccupied[pos[id] + distance][vehicles[id].fixedPos])
					nextMoves.add(new Move(id, distance));
				//moving down
				distance = 1;
				while (pos[id] + vehicles[id].length - 1 + distance < size && !isOccupied[pos[id] + vehicles[id].length + distance][vehicles[id].fixedPos])
					nextMoves.add(new Move(id, distance));
			}  				
		}
		return (nextMoves);
	}


	public Move getLastMove(SeenStates T, int nbrVehicles, int size) {
		//Attention à bien appeler cette méthode sur un état déja visité sinon ça va faire de la merde
		int[] pos = this.pos;
		SeenStates tree = T;
		for (int i = 0; i < nbrVehicles-1; i++) {
			SeenStates[] child = tree.children;
			int x = pos[i];
			tree = child[x];			
		}
		return tree.lastmove;
	}
	
	
	
	/*
  	void print();
	 */
}
