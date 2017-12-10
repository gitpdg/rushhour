
public class SeenStates {
	
	Move lastmove;
	SeenStates[] children;
	
	public SeenStates(){
		this.lastmove = null;
		this.children = null;
	}
	
	public boolean add(State e, Move lastmove, int nbrVehicles, int size){
		int[] pos = e.pos;
		SeenStates tree = this;
		for (int i = 0; i < nbrVehicles-1; i++) {
			SeenStates[] child = tree.children;
			if (child == null) {
				child = new SeenStates[size];
				for (int j = 0; j < size; j++){
					child[j] = new SeenStates();
				}
				tree.children = child;
			}
			int x = pos[i];
			tree = child[x];			
		}
		if (tree.lastmove == null) {
			tree.lastmove = lastmove;
			return(false);
		}
		else {
			return(true);
		}
	}
}
