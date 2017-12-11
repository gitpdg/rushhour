import java.io.*;
import java.util.*;

public class Game {
	
	int size;
	int nbrVehicles;
	Vehicle[] vehicles;
	State initialState;
	

	public Game(String file_name) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(file_name));
		String line;
		int size = Integer.valueOf(br.readLine());
		int nbrVehicles = Integer.valueOf(br.readLine());
		String[] line_split;
		Vehicle[] pos = new Vehicle[nbrVehicles];
		int[] posInit = new int[nbrVehicles];
		boolean[][] t = new boolean[size][size];
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				t[i][j] = false;
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
						if (t[l][y-1]) {
							System.out.println("None valid input");
						}
						else {
							t[l][y-1] = true;
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
						if (t[x-1][l]) {
							System.out.println("None valid input");
						}
						else {
							t[x-1][l] = true;
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
	}
	
	
	public LinkedList<Move> solve() {
		LinkedList<Move> res = new LinkedList<Move>();
		Queue<State> q = new ArrayDeque<State>();
		SeenStates seen = new SeenStates();
		State s = this.initialState;
		q.add(s);
		seen.add(s, new Move(1, 0), this.nbrVehicles, this.size);
		boolean solved = false;
		while ((!q.isEmpty()) && !solved) {
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
					if (!seen.add(s2, m, this.nbrVehicles, this.size)){
						q.add(s2);
					}
				}
				System.out.println();
			}
		}
		
		Move lastmove = s.getLastMove(seen, this.nbrVehicles, this.size);
		s.Previous(lastmove);
		res.add(lastmove);
		while (!s.equal(this.initialState)){
			lastmove = s.getLastMove(seen, this.nbrVehicles, this.size);
			s.Previous(lastmove);
			res.add(lastmove);
		}
		return res;
	}

	
}
