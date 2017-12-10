import java.io.*;
// hello BereniceCourant

public class Game {
	
	int size;
	int nbr_vehicles;
	Vehicle[] vehicles;
	

	public Game(String file_name) throws FileNotFoundException, IOException {
		File etat = new File(file_name);		
		BufferedReader br = new BufferedReader(new FileReader(etat));
		String line;
		int size = br.read();
		int nbr_vehicles = br.read();
		String[] line_split;
		Vehicle[] pos = new Vehicle[nbr_vehicles];
		boolean[][] t = new boolean[size][size];
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				t[i][j] = false;
			}
		}
		for (int k = 0; k < nbr_vehicles; k++) {
			line = br.readLine();
			line_split = line.split(" ");
			int id = Integer.valueOf(line_split[0]);
			char orientation = line_split[1].charAt(0);
			int length = Integer.valueOf(line_split[2]);
			int x = Integer.valueOf(line_split[3]);
			int y = Integer.valueOf(line_split[4]);
			Vehicle v = new Vehicle(id, length, orientation, x, y);
			pos[k] = v;
			if (orientation == 'h') {
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
		this.nbr_vehicles = nbr_vehicles;
		this.vehicles = pos;
	}

	
}
