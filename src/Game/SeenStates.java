package Game;

public class SeenStates {
	//Classe de type arbre permettant de savoir si un �tat est d�j� vu ou pas.
	//Cet arbre contient aussi le dernier mouvement fait pour arriver dans un �tat d�j� vu.
	Move lastmove;
	Integer isExplored; //Indique si l'�tat a d�j� �t� expor� afin de savoir si l'heuristique associ�e a d�j� �t� calcul�e ou non.
	SeenStates[] children;
	Integer distance; //Donne la distance, qu'on connait pour l'instant, de cet �tat � l'�tat initialement
	Integer heuristic; //Donne l'heuristique associ�e � cet �tat si il a d�j� �t� explor�.
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
		//Indique si un �tat a d�j� �t� vu ou non, et renvoie si c'est le cas des informations sur cet �tat :
		//	- Sa distance � l'�tat initial actuellement connue
		//	- Est ce que l'�tat est d�j� explor� ou non
		//	- La valeur de l'heuristic en cet �tat
		//Si l'�tat n'a pas d�j� �t� vu on enregistre ces diff�rentes informations ainsi que le dernier mouvement fait pour arriver dans cet �tat
		//Enfin si l'�tat a d�j� �t� vu mais avec une distance plus grande on met alors � jour la distance et le dernier mouvement fait.
		//Dans le cas o� on n'utilise pas les heuristics on s'int�resse uniquement � si l'�tat � d�j� �t� vu ou non.
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
		if (tree.lastmove == null) { //Si on a pas encore vu cet �tat
			tree.lastmove = lastmove; //On enregistre le dernier mouvement fait
			if (!this.brutForce){
				tree.distance = e.distance; //On enregistre la distance actuelle et l'heuristique de cet �tat
				tree.heuristic = e.heuristic;
				res[0] = -1; //On indique qu'on n'avait pas encore vu cet �tat
				res[1] = 0;	//Qu'il n'avait pas non plus �t� d�j� explor�
				res[2] = -1; //Et qu'on avait pas encore calcul� l'heuristiqu
			}
			else{
				res[0] = -1; //Dans le cas bruteForce on indique juste qu'on avait pas encore vu l'�tat
			}
		}
		else {	//Si on a d�j� vu l'�tat
			
			if (!this.brutForce){
				res[0] = tree.distance; //On renvoie la distance actuelle � l'�tat initial
				res[1] = tree.isExplored; //On indique si l'�tat a d�j� �t� explor� ou non
				res[2] = tree.heuristic; //On renvoie l'heuristique pour ne pas avoir � la calculer deux fois
				if (tree.distance > e.distance){ //Si on a maintenant un chemin plus court pour y arriver
					tree.distance = e.distance; //On met � jour la distance
					tree.lastmove = lastmove; //On met � jour le dernier mouvement
				}
			}
			else{
				res[0] = 0; //Dans le cas bruteForce on indique juste qu'on a d�j� vu l'�tat
			}
		}
		return res;
	}
	
	public int isExplored(State e, int nbrVehicles, int size){
		//Indique si un �tat donn� a d�j� �t� explor� ou non
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
		//Marque un �tat comme explor�.
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
		//Calcule l'heuristique associ�e � un �tat
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
