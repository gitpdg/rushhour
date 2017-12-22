package Game;

public class Heuristic {

	int typeHeuristic; //determines which heuristic will be used.
	
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
		//constant zero
		return 0;
	}
	
	public int heuristic1(State state, Vehicle[] vehicles, int size){
		//counting the number of vehicles on the way of the red car
		int y = vehicles[0].fixedPos - 1; //y coordinate of the red car
		int count = 0;
		for (int x=state.pos[0]+vehicles[0].length-1; x < size ; x++) { //along the way
			if (state.isOccupied[x][y]>0)
				count+=1;
		}
		return count;
	}
	
	public int heuristic2(State state, Vehicle[] vehicles, int size){
		//TODO
		//counting one per vehicle on the way that can move out in one move and two for vehicles on the way that can't
		int yFixed = vehicles[0].fixedPos; //x coordinate of the red car
		int count = 0;
		int id, y;
		Vehicle v;
		boolean canMoveDown,canMoveUp;
		for (int x=state.pos[0]+vehicles[0].length; x < size ; x++) { //along the way
			if (state.isOccupied[x][yFixed]>0) {
				id = state.isOccupied[x][yFixed];
				v = vehicles[id+1];
				y = yFixed;
				
				canMoveDown = true;
				//can I move down ?
				if (v.length + yFixed + 1 < size) {
					for (y = state.pos[id]+v.length; y < yFixed+v.length+1; y++ ) {
						if (state.isOccupied[x][y]>0)
							canMoveDown=false;
					}
				}
				canMoveUp = true;
				//can I move up ?
				if (v.length <= yFixed) { //TODO
					for (y = state.pos[id]-1; y < yFixed+v.length+1; y-- ) { //TODO
						if (state.isOccupied[x][y]>0)
							canMoveUp=false;
					}
				}
			}
		}
		return count;
		
	}
}
