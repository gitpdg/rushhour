package Game;

import java.util.Comparator;

public class ComparatorState implements Comparator<State> {
	//Comparateur sur des états : les états s1 et s2 sont comparés par la valeur de leur heuristique + leur distance
	public int compare(State s1, State s2){
		int h1 = s1.heuristic;
		int d1 = s1.distance;
		int h2 = s2.heuristic;
		int d2 = s2.distance;
		if (h1 + d1 < h2 + d2) return -1;
		if (h1 + d1 > h2 + d2) return 1;
		return 0;
		
	}
}
