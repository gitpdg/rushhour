
public class Move {
	
	int id;
	int distance;
	
	public Move(int id, int distance) {
		this.id = id;
		this.distance = distance;
	}
	
	public String toString(){
		return(Integer.toString(id) + "/" + Integer.toString(distance));
	}
}
