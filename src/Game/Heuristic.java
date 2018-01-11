package Game;

public class Heuristic {

	int typeHeuristic; //determines which heuristic will be used.
	
	public Heuristic(int typeHeuristic) {
		this.typeHeuristic = typeHeuristic;
	}

	
	public int value(State state, Vehicle[] vehicles, int size) {
		switch(typeHeuristic) {
		case 5: return heuristic5(state, vehicles, size);
		case 4: return heuristic4(state, vehicles, size);
		case 3: return heuristic3(state, vehicles, size);
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
		int y = vehicles[0].fixedPos - 1; //y-coordinate of the red car
		int count = 0;
		for (int x=state.pos[0]-1+vehicles[0].length; x < size ; x++) { //along the way
			if (state.isOccupied[x][y]>0)
				count+=1;
		}
		return count;
	}
	
	public int heuristic2(State state, Vehicle[] vehicles, int size){
		//returns 0 if we are at the solution, otherwise 1+number of vehicles on the way, and adding one if at least one of these vehicles can't get out in one move
		if (state.pos[0]-1 + vehicles[0].length  == size)
			return 0;

		int yFixed = vehicles[0].fixedPos -1; //y-coordinate of the red car
		int count = 1;
		int id, y;
		Vehicle v;
		boolean canMoveDown,canMoveUp;
		boolean blocked = false;
		for (int x=state.pos[0]-1 + vehicles[0].length; x < size ; x++) { //along the way of the red car
			if (state.isOccupied[x][yFixed]>0) {
				id = state.isOccupied[x][yFixed];
				v = vehicles[id-1];
				y = yFixed;
				if (!blocked) { //no need to check again if one car is already blocked
					canMoveDown = true;
					//can I move down ?
					if (v.length + yFixed + 1 < size) {
						for (y = state.pos[id-1]-1 + v.length; y < yFixed + 1 + v.length; y++ ) {
							if (state.isOccupied[x][y]>0)
								canMoveDown=false;
						}
					}
					canMoveUp = true;
					//can I move up ?
					if (v.length <= yFixed) {
						for (y = state.pos[id-1]-1; y >= yFixed - v.length; y-- ) {
							if (state.isOccupied[x][y]>0)
								canMoveUp=false;
						}
					}
					if (!canMoveDown && !canMoveUp)
						blocked = true;
				}
			}
		}
		if (blocked)
			count += 1;
		return count;
		
	}
	
	public int heuristic3(State state, Vehicle[] vehicles, int size){
		//return 0 if we are at the solution, 1 otherwise
		if (state.pos[0]-1 + vehicles[0].length  == size)
			return 0;
		return 1;
	}
	
	public int heuristic4(State state, Vehicle[] vehicles, int size){
		//combines the strategies of heuristic 1 and 3 : 0 if we are at the solution, 1+number of cars in the way otherwise
		if (state.pos[0]-1 + vehicles[0].length  == size)
			return 0;		

		int y = vehicles[0].fixedPos - 1; //y coordinate of the red car
		int count = 1;
		for (int x=state.pos[0]+vehicles[0].length-1; x < size ; x++) { //along the way
			if (state.isOccupied[x][y]>0)
				count+=1;
		}
		return count;
	}
	
	public int heuristic5(State state, Vehicle[] vehicles, int size){
		if (state.pos[0]-1 + vehicles[0].length  == size)
			return 0;	
		
		int y = vehicles[0].fixedPos - 1;
		float count = 1;
		for (int x = state.pos[0] + vehicles[0].length - 1; x < size; x++){
			int id1 = state.isOccupied[x][y];
			if (id1 > 0){
				count += 1;
				Vehicle v = vehicles[id1-1];
				int l = v.length;
				float count_up = Float.MAX_VALUE;
				int to_move_up = l - (state.pos[id1-1] - 1 - y);  
				if (to_move_up <= state.pos[id1-1] - 1){
					count_up = 0;
					for (int y2 = state.pos[id1-1] - 2; y2 >= state.pos[id1-1] - 1 - to_move_up; y2--){
						int id2 = state.isOccupied[x][y2];
						if (id2 > 0){
							count_up += 1./vehicles[id2-1].length;
						}
					}
				}
				
				float count_down = Float.MAX_VALUE;
				int to_move_down = state.pos[id1-1] - 1 - y + 1;
				if (state.pos[id1-1] - 1 + v.length - 1 + to_move_down < size){
					count_down = 0;
					for (int y2 = state.pos[id1-1] - 1 + v.length - 1 + 1; y2 <= state.pos[id1-1] - 1 + v.length - 1 + to_move_down; y2++){
						int id2 = state.isOccupied[x][y2];
						if (id2 > 0){
							count_down += 1./vehicles[id2-1].length;
						}
					}
				}
				
				count += Float.min(count_down, count_up);
			}
		}
		return ((int) count + 1);
	}
}
