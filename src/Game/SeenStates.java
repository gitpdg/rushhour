package Game;

public class SeenStates {
	//Classe de type arbre permettant de savoir si un état est déjà vu ou pas.
	//Cet arbre contient aussi le dernier mouvement fait pour arriver dans un état déjà vu.
	Move lastmove;
	Integer isExplored; //Indique si l'état a déjà été exporé afin de savoir si l'heuristique associée a déjà été calculée ou non.
	SeenStates[] children;
	Integer distance; //Donne la distance, qu'on connait pour l'instant, de cet état à l'état initialement
	Integer heuristic; //Donne l'heuristique associée à cet état si il a déjà été exploré.
	
	public SeenStates(){
		this.lastmove = null;
		this.children = null;
		this.distance = null;
		this.isExplored = 0;
		this.heuristic = null;
	}
	
	public int[] add(State e, Move lastmove, int nbrVehicles, int size){
		//Indique si un état a déjà été vu ou non, et si ce n'est pas le cas le marque comme vu et enregistre
		//le dernier mouvement fait pour arriver dans cet état.
		int[] pos = e.pos;
		SeenStates tree = this;
		int[] res = new int[3];
		for (int i = 0; i < nbrVehicles; i++) {
			SeenStates[] child = tree.children;
			if (child == null) {
				child = new SeenStates[size];
				for (int j = 0; j < size; j++){
					child[j] = new SeenStates();
				}
				tree.children = child;
			}	
			tree = child[pos[i]-1];			
		}
		if (tree.lastmove == null) { //Si on a pas encore vu cet état
			tree.lastmove = lastmove;
			tree.distance = e.distance;
			tree.heuristic = e.heuristic;
			res[0] = -1;
			res[1] = 0;
			res[2] = -1;
		}
		else {
			res[0] = tree.distance;
			res[1] = tree.isExplored;
			res[2] = tree.heuristic;
			
			if (tree.distance > e.distance){
				tree.distance = e.distance;
				tree.lastmove = lastmove;
			}
		}
		return res;
	}
	
	public int isExplored(State e, int nbrVehicles, int size){
		//Indique si un état donné a déjà été exploré ou non
		int[] pos = e.pos;
		SeenStates tree = this;
		for (int i = 0; i < nbrVehicles; i++) {
			SeenStates[] child = tree.children;
			if (child == null) {
				child = new SeenStates[size];
				for (int j = 0; j < size; j++){
					child[j] = new SeenStates();
				}
				tree.children = child;
			}	
			tree = child[pos[i]-1];			
		}
		return(tree.isExplored);
	}
	
	public void explore(State e, int nbrVehicles, int size){
		//Marque un état comme exploré.
		int[] pos = e.pos;
		SeenStates tree = this;
		for (int i = 0; i < nbrVehicles; i++) {
			SeenStates[] child = tree.children;
			if (child == null) {
				child = new SeenStates[size];
				for (int j = 0; j < size; j++){
					child[j] = new SeenStates();
				}
				tree.children = child;
			}	
			tree = child[pos[i]-1];			
		}
		tree.isExplored = 1;
	}
	
	public void changedHeuristic(State e, int nbrVehicles, int size){
		//Calcule l'heuristique associée à un état
		int[] pos = e.pos;
		SeenStates tree = this;
		for (int i = 0; i < nbrVehicles; i++) {
			SeenStates[] child = tree.children;
			if (child == null) {
				child = new SeenStates[size];
				for (int j = 0; j < size; j++){
					child[j] = new SeenStates();
				}
				tree.children = child;
			}	
			tree = child[pos[i]-1];			
		}
		tree.heuristic = e.heuristic;
	}
	
/*
	public Integer add(State e, Move lastmove, int nbrVehicles, int size, State parent){
		int[] pos = e.pos;
		SeenStates tree = this;
		for (int i = 0; i < nbrVehicles; i++) {
			SeenStates[] child = tree.children;
			if (child == null) {
				child = new SeenStates[size];
				for (int j = 0; j < size; j++){
					child[j] = new SeenStates();
				}
				tree.children = child;
			}	
			tree = child[pos[i]-1];			
		}
		if (tree.lastmove == null) {
			tree.lastmove = lastmove;
			if (getDistance(parent)==null)
				tree.distance = 1;
			else
				tree.distance = this.getDistance(parent)+1;
			return(null);
		}
		else {
			return(tree.distance);
		}
	}

	public Integer getDistance(State s) {
		int[] pos = s.pos;
		SeenStates tree = this;
		for (int i=0; i<pos.length; i++) {
			SeenStates[] child = tree.children;
			tree = child[pos[i]-1];
		}
		return tree.distance;
	}
*/

	public String toString(){
		String c = "";
		if (this.lastmove == null){
			c += "null";
		}
		else {
			c += lastmove.toString();
		}
		c += ": \n";
		SeenStates[] child = this.children;
		if (child == null) {
			c += "fin";
		}
		else {
			for (int i = 0; i < child.length; i++) {
				if (child[i] == null) {
					c += "null /";
				}
				else {
					c += child[i].toString();
					c += "/";
				}
			}
		}
		return(c);
	}
}
