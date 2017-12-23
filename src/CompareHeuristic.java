import java.io.IOException;

import Game.Game;

public class CompareHeuristic {

	int min, max;
	String path = "Games/";
	
	public CompareHeuristic(int min, int max) {
		this.min = min;
		this.max = max;
	}
	
	public void run() {
		String fileName;
		Game game;
		for (int i=1; i<=40; i++) {
			System.out.println("");
			for (int type =min; type<=max; type++) {
				if (i<10) {
					fileName = "GameP0"+i;
				}
				else {
					fileName = "GameP"+i;
				}
				try {
					game = new Game(path + fileName + ".txt", type, false);
					int[] result = game.solveStat();
					System.out.println(fileName+"   heuristic "+type+"   "+result[0]+"ms   "+result[1]+" visited states   "+result[2]+" moves");
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
