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
		this.isOccupied = parent.isOccupied;

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
		for (int id=0; id < pos.length; id++) {
			if (vehicles[id].orientation=='h') {
				//moving left
				distance = -1;
				while (pos[id] + distance >0 && !isOccupied[vehicles[id].fixedPos][pos[id] + distance])
					nextMoves.add(new Move(id, distance));
				//moving right
				distance = 1;
				while (pos[id] + vehicles[id].length + distance < size && !isOccupied[vehicles[id].fixedPos][pos[id] + vehicles[id].length + distance])
					nextMoves.add(new Move(id, distance));
			}
			else {
				//moving up
				distance = -1;
				while (pos[id] + distance >0 && !isOccupied[pos[id] + distance][vehicles[id].fixedPos])
					nextMoves.add(new Move(id, distance));
				//moving down
				distance = 1;
				while (pos[id] + vehicles[id].length + distance < size && !isOccupied[pos[id] + vehicles[id].length + distance][vehicles[id].fixedPos])
					nextMoves.add(new Move(id, distance));
			}  				
		}
		return (nextMoves);
	}

	/*
	Move getLastMove(SeenStates T);
  	void print();
	 */
}
