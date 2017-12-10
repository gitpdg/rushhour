public class State{
	int size;
	int nbr_vehicles;
	int[] pos;
	boolean[][] is_occupied;
	
	public State(int size, int nbr_vehicles, int[] pos) {
		this.size=size;
		this.nbr_vehicles=nbr_vehicles;
		this.pos = pos;
		
	}
}
