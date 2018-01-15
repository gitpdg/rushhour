package Game;

public class Heuristic {

	int typeHeuristic; //determine quelle heuristique utiliser.
	
	public Heuristic(int typeHeuristic) {
		this.typeHeuristic = typeHeuristic;
	}

	
	public int value(State state, Vehicle[] vehicles, int size) {
		switch(typeHeuristic) {
		case 5: return heuristic5(state, vehicles, size);
		case 4: return heuristic4(state, vehicles, size);
		case 3: return heuristic3(state, vehicles, size);
		case 2: return heuristic2(state, vehicles, size);
		case 1: return heuristic1(state, vehicles, size);
		default: return heuristic0();
		}
	}
	
	
	public int heuristic0(){
		//constant zero
		return 0;
	}
	
	public int heuristic1(State state, Vehicle[] vehicles, int size){
		//compte le nombre de véhicule devant la voiture rouge
		int y = vehicles[0].fixedPos - 1; //y-coordonnée de la voiture rouge
		int count = 0;
		for (int x=state.pos[0]-1+vehicles[0].length; x < size ; x++) { //Sur le chemin
			if (state.isOccupied[x][y]>0)
				count+=1;
		}
		return count;
	}
	
	public int heuristic2(State state, Vehicle[] vehicles, int size){
		//return 0 si on est à la solution, et sinon 1+nombre de véhicule devant la voiture rouge, on ajoute ensuite 1 si au moins un de ces véhicules ne peut pas s'écarter en un seul mouvement.
		if (state.pos[0]-1 + vehicles[0].length  == size)
			return 0;

		int yFixed = vehicles[0].fixedPos -1; //y-coordonnée de la voiture rouge
		int count = 1;
		int id, y;
		Vehicle v;
		boolean canMoveDown,canMoveUp;
		boolean blocked = false;
		for (int x=state.pos[0]-1 + vehicles[0].length; x < size ; x++) { //Sur le chemin de la voiture rouge
			if (state.isOccupied[x][yFixed]>0) {
				id = state.isOccupied[x][yFixed];
				v = vehicles[id-1];
				y = yFixed;
				if (!blocked) { //Pas besoin de vérifier si une voiture est déjà bloquée.
					canMoveDown = true;
					//Peut-on aller vers le bas ?
					if (v.length + yFixed + 1 < size) {
						for (y = state.pos[id-1]-1 + v.length; y < yFixed + 1 + v.length; y++ ) {
							if (state.isOccupied[x][y]>0)
								canMoveDown=false;
						}
					}
					canMoveUp = true;
					//Peut-on aller vers le haut ?
					if (v.length <= yFixed) {
						for (y = state.pos[id-1]-1; y >= yFixed - v.length; y-- ) {
							if (state.isOccupied[x][y]>0)
								canMoveUp=false;
						}
					}
					if (!canMoveDown && !canMoveUp)
						blocked = true;
				}
			}
		}
		if (blocked)
			count += 1;
		return count;
		
	}
	
	public int heuristic3(State state, Vehicle[] vehicles, int size){
		//return 0 si on est à la solution, 1 sinon.
		if (state.pos[0]-1 + vehicles[0].length  == size)
			return 0;
		return 1;
	}
	
	public int heuristic4(State state, Vehicle[] vehicles, int size){
		//combine les strategies des heuristiques 1 et 3 : 0 si on est à la solution, 1+nombre de voiture sur le chemin sinon.
		if (state.pos[0]-1 + vehicles[0].length  == size)
			return 0;		

		int y = vehicles[0].fixedPos - 1; //y-coordonnée de la voiture rouge
		int count = 1;
		for (int x=state.pos[0]+vehicles[0].length-1; x < size ; x++) { //Sur le chemin de la voiture rouge
			if (state.isOccupied[x][y]>0)
				count+=1;
		}
		return count;
	}
	
	public int heuristic5(State state, Vehicle[] vehicles, int size){
		//return 0 si on est à la solution
		//Sinon, return 1 + nombre de voiture sur le chemin + 1/longueur(i) pour chaque voiture i empêchant une voiture sur le chemin de la voiture rouge de s'écarter en un coup.
		//On prend ensuite l'entier arrondi au supérieur.
		if (state.pos[0]-1 + vehicles[0].length  == size)
			return 0;	
		
		int y = vehicles[0].fixedPos - 1;
		float count = 1;
		for (int x = state.pos[0] + vehicles[0].length - 1; x < size; x++){ //Sur le chemin de la voiture rouge
			int id1 = state.isOccupied[x][y];
			if (id1 > 0){
				count += 1;
				Vehicle v = vehicles[id1-1];
				int l = v.length;
				//Peut-on s'écarter par le haut ?
				float count_up = Float.MAX_VALUE;
				int to_move_up = l - (state.pos[id1-1] - 1 - y);  
				if (to_move_up <= state.pos[id1-1] - 1){
					count_up = 0;
					for (int y2 = state.pos[id1-1] - 2; y2 >= state.pos[id1-1] - 1 - to_move_up; y2--){ //Sur le chemin vers le haut de la voiture id1
						int id2 = state.isOccupied[x][y2];
						if (id2 > 0){
							count_up += 1./vehicles[id2-1].length;
						}
					}
				}
				
				//Peut-on s'écarter par le bas ?
				float count_down = Float.MAX_VALUE;
				int to_move_down = state.pos[id1-1] - 1 - y + 1;
				if (state.pos[id1-1] - 1 + v.length - 1 + to_move_down < size){
					count_down = 0;
					for (int y2 = state.pos[id1-1] - 1 + v.length - 1 + 1; y2 <= state.pos[id1-1] - 1 + v.length - 1 + to_move_down; y2++){//Sur le chemin vers le bas de la voiture id1
						int id2 = state.isOccupied[x][y2];
						if (id2 > 0){
							count_down += 1./vehicles[id2-1].length;
						}
					}
				}
				
				count += Float.min(count_down, count_up);
			}
		}
		return ((int) count + 1);
	}
}
