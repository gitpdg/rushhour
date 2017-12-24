import java.io.IOException;

import Game.Game;

public class CompareHeuristic {

	int min, max;
	String path = "Games/";

	public CompareHeuristic(int min, int max) {
		this.min = min;
		this.max = max;
	}

	public void run(int iter) {
		String fileName;
		Game game;
		int[] result = new int[3];
		int time;
		for (int i=1; i<=40; i++) {
			System.out.println("");
			for (int type =min; type<=max; type++) {
				if (i<10) {
					fileName = "GameP0"+i;
				}
				else {
					fileName = "GameP"+i;
				}
				time = 0;
				for (int j=0; j<iter;j++) {
					try {
						game = new Game(path + fileName + ".txt", type, false);
						result = game.solveStat();
						time += result[0];
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				System.out.println(fileName+"   heuristic "+type+"   average time : "+(time/iter)+"ms   "+result[1]+" visited states   "+result[2]+" moves");
			}
		}
	}
}
