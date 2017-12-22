package Game;

import java.util.Comparator;

public class ComparatorState implements Comparator<State> {
	
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
