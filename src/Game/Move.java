package Game;

public class Move {
	//Classe repr�sentant un mouvement par, l'id du v�hicule se d�pla�ant et la distance � faire.
	public int id;
	public int distance;
	
	public Move(int id, int distance) {
		this.id = id;
		this.distance = distance;
	}
	
	public String toString(){
		return(Integer.toString(id) + "/" + Integer.toString(distance));
	}
}
