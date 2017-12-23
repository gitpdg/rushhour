package Game;

public class SeenStates {
	
	Move lastmove;
	SeenStates[] children;
	Integer distance;
	
	public SeenStates(){
		this.lastmove = null;
		this.children = null;
		this.distance = null;
	}
	
	public boolean add(State e, Move lastmove, int nbrVehicles, int size){
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
			return(false);
		}
		else {
			return(true);
		}
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
