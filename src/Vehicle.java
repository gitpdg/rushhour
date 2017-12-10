
public class Vehicle {

	int id;
	int length;
	char orientation;
	int x;
	int y;
	
	public Vehicle(int id, int length, char orientation, int x, int y) {
		this.id = id;
		this.length = length;
		this.orientation = orientation;
		this.x = x;
		this.y = y;
	}
	
	public int length() {
		return this.length;
	}
}
