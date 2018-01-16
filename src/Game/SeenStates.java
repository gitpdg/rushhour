package Game;

public class SeenStates {
	
	Move lastmove;
	Integer isExplored;
	SeenStates[] children;
	Integer distance;
	Integer heuristic;
	boolean brutForce;
	
	public SeenStates(boolean brutForce){
		this.lastmove = null;
		this.children = null;
		this.distance = null;
		this.isExplored = 0;
		this.heuristic = null;
		this.brutForce = brutForce;
	}
	
	public int[] add(State e, Move lastmove, int nbrVehicles, int size){
		int[] pos = e.pos;
		SeenStates tree = this;
		int[] res = new int[3];
		for (int i = 0; i < nbrVehicles; i++) {
			SeenStates[] child = tree.children;
			if (child == null) {
				child = new SeenStates[size];
				for (int j = 0; j < size; j++){
					child[j] = new SeenStates(this.brutForce);
				}
				tree.children = child;
			}	
			tree = child[pos[i]-1];			
		}
		if (tree.lastmove == null) {
			tree.lastmove = lastmove;
			if (!this.brutForce){
				tree.distance = e.distance;
				tree.heuristic = e.heuristic;
				res[0] = -1;
				res[1] = 0;
				res[2] = -1;
			}
			else{
				res[0] = -1;
			}
		}
		else {
			
			if (!this.brutForce){
				res[0] = tree.distance;
				res[1] = tree.isExplored;
				res[2] = tree.heuristic;
				if (tree.distance > e.distance){
					tree.distance = e.distance;
					tree.lastmove = lastmove;
				}
			}
			else{
				res[0] = 0;
			}
		}
		return res;
	}
	
	public int isExplored(State e, int nbrVehicles, int size){
		int[] pos = e.pos;
		SeenStates tree = this;
		for (int i = 0; i < nbrVehicles; i++) {
			SeenStates[] child = tree.children;
			if (child == null) {
				child = new SeenStates[size];
				for (int j = 0; j < size; j++){
					child[j] = new SeenStates(this.brutForce);
				}
				tree.children = child;
			}	
			tree = child[pos[i]-1];			
		}
		return(tree.isExplored);
	}
	
	public void explore(State e, int nbrVehicles, int size){
		int[] pos = e.pos;
		SeenStates tree = this;
		for (int i = 0; i < nbrVehicles; i++) {
			SeenStates[] child = tree.children;
			if (child == null) {
				child = new SeenStates[size];
				for (int j = 0; j < size; j++){
					child[j] = new SeenStates(this.brutForce);
				}
				tree.children = child;
			}	
			tree = child[pos[i]-1];			
		}
		tree.isExplored = 1;
	}
	
	public void changedHeuristic(State e, int nbrVehicles, int size){
		int[] pos = e.pos;
		SeenStates tree = this;
		for (int i = 0; i < nbrVehicles; i++) {
			SeenStates[] child = tree.children;
			if (child == null) {
				child = new SeenStates[size];
				for (int j = 0; j < size; j++){
					child[j] = new SeenStates(this.brutForce);
				}
				tree.children = child;
			}	
			tree = child[pos[i]-1];			
		}
		tree.heuristic = e.heuristic;
	}

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
