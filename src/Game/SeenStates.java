package Game;

public class SeenStates {
	//Classe de type arbre permettant de savoir si un état est déjà vu ou pas.
	//Cet arbre contient aussi le dernier mouvement fait pour arriver dans un état déjà vu.
	Move lastmove;
	Integer isExplored; //Indique si l'état a déjà été exporé afin de savoir si l'heuristique associée a déjà été calculée ou non.
	SeenStates[] children;
	Integer distance; //Donne la distance, qu'on connait pour l'instant, de cet état à l'état initialement
	Integer heuristic; //Donne l'heuristique associée à cet état si il a déjà été exploré.
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
		//Indique si un état a déjà été vu ou non, et renvoie si c'est le cas des informations sur cet état :
		//	- Sa distance à l'état initial actuellement connue
		//	- Est ce que l'état est déjà exploré ou non
		//	- La valeur de l'heuristic en cet état
		//Si l'état n'a pas déjà été vu on enregistre ces différentes informations ainsi que le dernier mouvement fait pour arriver dans cet état
		//Enfin si l'état a déjà été vu mais avec une distance plus grande on met alors à jour la distance et le dernier mouvement fait.
		//Dans le cas où on n'utilise pas les heuristics on s'intéresse uniquement à si l'état à déjà été vu ou non.
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
		if (tree.lastmove == null) { //Si on a pas encore vu cet état
			tree.lastmove = lastmove; //On enregistre le dernier mouvement fait
			if (!this.brutForce){
				tree.distance = e.distance; //On enregistre la distance actuelle et l'heuristique de cet état
				tree.heuristic = e.heuristic;
				res[0] = -1; //On indique qu'on n'avait pas encore vu cet état
				res[1] = 0;	//Qu'il n'avait pas non plus été déjà exploré
				res[2] = -1; //Et qu'on avait pas encore calculé l'heuristiqu
			}
			else{
				res[0] = -1; //Dans le cas bruteForce on indique juste qu'on avait pas encore vu l'état
			}
		}
		else {	//Si on a déjà vu l'état
			
			if (!this.brutForce){
				res[0] = tree.distance; //On renvoie la distance actuelle à l'état initial
				res[1] = tree.isExplored; //On indique si l'état a déjà été exploré ou non
				res[2] = tree.heuristic; //On renvoie l'heuristique pour ne pas avoir à la calculer deux fois
				if (tree.distance > e.distance){ //Si on a maintenant un chemin plus court pour y arriver
					tree.distance = e.distance; //On met à jour la distance
					tree.lastmove = lastmove; //On met à jour le dernier mouvement
				}
			}
			else{
				res[0] = 0; //Dans le cas bruteForce on indique juste qu'on a déjà vu l'état
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
					child[j] = new SeenStates(this.brutForce);
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
					child[j] = new SeenStates(this.brutForce);
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
