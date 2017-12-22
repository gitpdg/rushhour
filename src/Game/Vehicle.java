package Game;

public class Vehicle {

	public int id;
	public int length;
	public char orientation;
	public int fixedPos;
	
	public Vehicle(int id, int length, char orientation, int fixedPos) {
		this.id = id;
		this.length = length;
		this.orientation = orientation;
		this.fixedPos = fixedPos;
	}
	
	@Override
	public String toString(){
		String c = Integer.toString(id) + "/" + Integer.toString(length) + "/" + orientation + "/" + Integer.toString(fixedPos);
		return(c);
	}

}
