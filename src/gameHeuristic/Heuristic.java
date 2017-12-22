package gameHeuristic;

public class Heuristic {

	int typeHeuristic;
	
	public Heuristic(int typeHeuristic) {
		this.typeHeuristic = typeHeuristic;
	}

	public int value(State state, Vehicle[] vehicles, int size) {
		switch(typeHeuristic) {
		case 2: return heuristic2(state, vehicles, size);
		case 1: return heuristic1(state, vehicles, size);
		default: return heuristic0();
		}
	}
	
	public int heuristic0(){
		return 0;
	}
	
	public int heuristic1(State state, Vehicle[] vehicles, int size){
		//counting the number of vehicles on the way of the red car
		int y = vehicles[0].fixedPos; //x coordinate of the red car
		int count = 0;
		for (int x=state.pos[0]+vehicles[0].length; x < size ; x++) { //along the way
			if (state.isOccupied[x][y]>0)
				count+=1;
		}
		return count;
	}
	
	public int heuristic2(State state, Vehicle[] vehicles, int size){
		//counting one per vehicle on the way that can move out in one move and two for vehicles on the way that can't
		//TODO
		int y = vehicles[0].fixedPos; //x coordinate of the red car
		int count = 0;
		for (int x=state.pos[0]+vehicles[0].length; x < size ; x++) { //along the way
			if (state.isOccupied[x][y]>0)
				count+=1;
		}
		return count;
		
	}
}
