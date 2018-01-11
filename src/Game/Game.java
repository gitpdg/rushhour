package Game;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;

public class Game {

	public int size;
	public int nbrVehicles;
	public Vehicle[] vehicles;
	public State initialState;
	Heuristic heuristic;
	boolean brutForce;


	public Game(String file_name, int typeheuristic, boolean brutForce) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(file_name));
		String line;
		int size = Integer.valueOf(br.readLine());
		int nbrVehicles = Integer.valueOf(br.readLine());
		String[] line_split;
		Vehicle[] pos = new Vehicle[nbrVehicles];
		int[] posInit = new int[nbrVehicles];
		int[][] t = new int[size][size];
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				t[i][j] = 0;
			}
		}
		for (int k = 0; k < nbrVehicles; k++) {
			line = br.readLine();
			line_split = line.split(" ");
			int id = Integer.valueOf(line_split[0]);
			char orientation = line_split[1].charAt(0);
			int length = Integer.valueOf(line_split[2]);
			int x = Integer.valueOf(line_split[3]);
			int y = Integer.valueOf(line_split[4]);
			Vehicle v;
			if (orientation == 'h') {
				v = new Vehicle(id, length, orientation, y);
				pos[k] = v;
				posInit[k] = x;
				if ((x-1) + (length - 1) >= size) {
					System.out.println("Too big");
				}
				else {
					for (int l = x - 1; l < x - 1 + length; l++) {
						if (t[l][y-1] != 0) {
							System.out.println("None valid input");
						}
						else {
							t[l][y-1] = v.id;
						}
					}

				}
			}

			else {
				v = new Vehicle(id, length, orientation, x);
				pos[k] = v;
				posInit[k] = y;
				if ((y-1) + (length - 1) >= size) {
					System.out.println("Too big");
				}
				else {
					for (int l = y - 1; l < y - 1 + length; l++) {
						if (t[x-1][l] != 0) {
							System.out.println("None valid input");
						}
						else {
							t[x-1][l] = v.id;
						}
					}

				}
			}

		}
		br.close();

		this.size = size;
		this.nbrVehicles = nbrVehicles;
		this.vehicles = pos;
		this.initialState = new State(posInit, t);
		this.heuristic = new Heuristic(typeheuristic);
		this.brutForce = brutForce;
	}


	public LinkedList<Move> solve() {
		long startingTime = System.currentTimeMillis();

		LinkedList<Move> res = new LinkedList<Move>();
		int numberSeenStates = 0;
		State s = this.initialState;
		SeenStates seen = new SeenStates();
		seen.add(s, new Move(1, 0), this.nbrVehicles, this.size);
		boolean solved = false;

		if (this.brutForce) {
			Queue<State> q = new ArrayDeque<State>();
			q.add(s);

			while ((!q.isEmpty()) && !solved) {
				numberSeenStates += 1;
				s = q.remove();
				int posFirstVehicle = s.pos[0];
				int lengthFirstVehicle = (this.vehicles[0]).length;
				if (posFirstVehicle + lengthFirstVehicle - 1 == this.size) {
					solved = true;
				}
				else {
					LinkedList<Move> moves = s.possibleMoves(this.vehicles, this.size);
					for (Move m : moves){
						State s2 = new State(s, m, this.vehicles);
						if (seen.add(s2, m, this.nbrVehicles, this.size)[0] == -1){
							q.add(s2);
						}
					}
				}
			}
		}

		else {
			PriorityQueue<State> q = new PriorityQueue<State>(nbrVehicles, new ComparatorState());
			s.setheuristic(heuristic, vehicles, size);
			s.setdistance(0);
			q.add(s);
			
			while ((!q.isEmpty()) && !solved) {
				numberSeenStates += 1;
				s = q.poll();
				if (seen.isExplored(s, this.nbrVehicles, this.size) == 0){
					seen.explore(s, this.nbrVehicles, this.size);
					int posFirstVehicle = s.pos[0];
					int lengthFirstVehicle = (this.vehicles[0]).length;
					if (posFirstVehicle + lengthFirstVehicle - 1 == this.size) {
						solved = true;
					}
					else {
						LinkedList<Move> moves = s.possibleMoves(this.vehicles, this.size);
						for (Move m : moves){
							State s2 = new State(s, m, this.vehicles);
							s2.setdistance(s.distance + 1);
							int[] vu = seen.add(s2, m, this.nbrVehicles, this.size);
							
							if (vu[0] == -1){
								s2.setheuristic(heuristic, vehicles, size);
								seen.changedHeuristic(s2, this.nbrVehicles, this.size);
								q.add(s2);
							}
							else{
								if (vu[1] == 0){
									if (s.distance + 1 < vu[0]){
										s2.heuristic = vu[2];
										q.add(s2);
									}
								}
							}
						}
					}
				}
			}

		}

		LinkedList<Move> resInv = new LinkedList<Move>();

		if (solved) {
			Move lastmove = s.getLastMove(seen, this.nbrVehicles, this.size);
			s.Previous(lastmove);
			res.add(lastmove);
			while (!s.equal(this.initialState)){
				lastmove = s.getLastMove(seen, this.nbrVehicles, this.size);
				s.Previous(lastmove);
				res.add(lastmove);
			}


			Iterator<Move> ite = res.descendingIterator();
			while (ite.hasNext()) {
				resInv.add(ite.next());
			}
			System.out.println("Execution time : "+ (System.currentTimeMillis()-startingTime)+ "ms");
			System.out.println("Number of explored states : "+numberSeenStates);
			System.out.println("Length of solution : "+resInv.size());
		}
		else {
			resInv = null;
			System.out.println("Impossible");
		}

		return resInv;
	}

	public int[] solveStat() {
		//returns [execution time, number of visited states, length of solution]
		long startingTime = System.currentTimeMillis();

		LinkedList<Move> res = new LinkedList<Move>();
		int numberSeenStates = 0;
		State s = this.initialState;
		SeenStates seen = new SeenStates();
		seen.add(s, new Move(1, 0), this.nbrVehicles, this.size);
		boolean solved = false;

		if (this.brutForce) {
			Queue<State> q = new ArrayDeque<State>();
			q.add(s);

			while ((!q.isEmpty()) && !solved) {
				numberSeenStates += 1;
				s = q.remove();
				int posFirstVehicle = s.pos[0];
				int lengthFirstVehicle = (this.vehicles[0]).length;
				if (posFirstVehicle + lengthFirstVehicle - 1 == this.size) {
					solved = true;
				}
				else {
					LinkedList<Move> moves = s.possibleMoves(this.vehicles, this.size);
					for (Move m : moves){
						State s2 = new State(s, m, this.vehicles);
						if (seen.add(s2, m, this.nbrVehicles, this.size)[0] == -1){
							q.add(s2);
						}
					}
				}
			}
		}

		else {
			PriorityQueue<State> q = new PriorityQueue<State>(nbrVehicles, new ComparatorState());
			s.setheuristic(heuristic, vehicles, size);
			s.setdistance(0);
			q.add(s);
			

			while ((!q.isEmpty()) && !solved) {
				numberSeenStates += 1;
				s = q.poll();
				if (seen.isExplored(s, this.nbrVehicles, this.size) == 0){
					int posFirstVehicle = s.pos[0];
					int lengthFirstVehicle = (this.vehicles[0]).length;
					if (posFirstVehicle + lengthFirstVehicle - 1 == this.size) {
						solved = true;
					}
					else {
						LinkedList<Move> moves = s.possibleMoves(this.vehicles, this.size);
						for (Move m : moves){
							State s2 = new State(s, m, this.vehicles);
							s2.setdistance(s.distance + 1);
							int[] vu = seen.add(s2, m, this.nbrVehicles, this.size);
							
							if (vu[0] == -1){
								s2.setheuristic(heuristic, vehicles, size);
								seen.changedHeuristic(s2, this.nbrVehicles, this.size);
								q.add(s2);
							}
							else{
								if (vu[1] == 0){
									if (s.distance + 1 < vu[0]){
										s2.heuristic = vu[2];
										q.add(s2);
									}
								}
							}
						}
					}
				}
			}

		}

		LinkedList<Move> resInv = new LinkedList<Move>();

		if (solved) {
			Move lastmove = s.getLastMove(seen, this.nbrVehicles, this.size);
			s.Previous(lastmove);
			res.add(lastmove);
			while (!s.equal(this.initialState)){
				lastmove = s.getLastMove(seen, this.nbrVehicles, this.size);
				s.Previous(lastmove);
				res.add(lastmove);
			}


			Iterator<Move> ite = res.descendingIterator();
			while (ite.hasNext()) {
				resInv.add(ite.next());
			}
		}
		else {
			resInv = null;
			System.out.println("Impossible"); 
		}
		int[] result = new int[3];
		result[0] = ((Long) (System.currentTimeMillis()-startingTime)).intValue();
		result[1] = numberSeenStates;
		if (resInv == null)
			result[2] = -1;
		else
			result[2] = resInv.size();
		return result ;
	}
	

}
