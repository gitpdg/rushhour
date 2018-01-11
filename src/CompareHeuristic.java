import java.awt.Color;
import java.io.IOException;
import java.util.Random;

import javax.swing.JFrame;

import org.math.plot.Plot2DPanel;

import Game.Game;

public class CompareHeuristic {

	int min, max; //on compare toutes les heuristiques dont l'indice est entre min et max inclus
	String path = "Games/";

	public CompareHeuristic(int min, int max) {
		this.min = min;
		this.max = max;
	}
		
	public void run(int iter) {
		double[][] times = new double[max - min + 1][40]; //40 : nombre de games dans la comparaision
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
				times[type-min][i-1] = time/iter;
			}
		}
		printHeuristics(times);
	}
	
	public Color randomColor(Random rand){
		float red = rand.nextFloat();
		float green = rand.nextFloat();
		float blue = rand.nextFloat();
		Color randomColor = new Color(red, green, blue);
		return (randomColor);
	}
	
	public Color chooseColor(Color[] colors, int i){
		boolean find = false;
		Random rand = new Random();
		Color c = null;
		while (!find) {
			c = randomColor(rand);
			boolean used = false;
			for (int j = 0; j < i; j++){
				if (c.equals(colors[j])) used = true;
			}
			if (!used) find = true;
		}
		return c;
	}
	
	public void printHeuristics(double[][] times){
		System.out.println("printing plot...");
		Plot2DPanel plot = new Plot2DPanel();
		int n = times.length;
		int m = times[0].length;
		Color[] colors = new Color[n];
		
		double[] x = new double[m];
		for (int k = 0; k < m; k++){
			x[k] = k + 1;
		}
		Color c = Color.GREEN;
		for (int i = 0; i < n; i++){
			String name = "Heuristic" + min + i;
			double[] y = times[i];
			switch(i) {
			case 0: c = Color.RED;break;
			case 1: c = Color.BLUE;break;
			case 2: c = Color.GREEN;break;
			case 3: c = Color.ORANGE;break;
			case 4: c = Color.BLACK;break;
			default: c = chooseColor(colors, i);break;
			}
			colors[i] = c;
			plot.addLegend("EAST$");
			plot.addLinePlot(name, c, x, y);
		}
		
		JFrame window = new JFrame("Comparaison heuristics : temps en fonction des games");
		window.setSize(2000,1000);
		window.setContentPane(plot);
		window.setVisible(true);
	}
}
