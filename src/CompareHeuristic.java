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
		

	public void run(int iter, boolean sorted, int sorte_comp, boolean print, boolean timeGraph) {
		//retourne un graphique du temps d'exécutions de diffférentes heuristiques pour les 40 parties.
		//si sorted=true, il trie les parties par temps d'exécution pour l'heuristique sorte_comp croissant
		//si print=true, il affiche ses résultats au fur et à mesure
		double[][] times = new double[max - min + 1][40]; //Stocke les temps moyens en fonctions de l'heuristique et de la partie. 40 : nombre de games dans la comparaision
		double[][] visited = new double[max - min + 1][40]; //Stocke le nombre d'états vus en fonctions de l'heuristique et de la partie. 40 : nombre de games dans la comparaision
		String fileName;
		Game game;
		int[] result = new int[3];
		int time;
		
		if (!timeGraph)
			iter=1; //rien ne sert de faire plusieurs itérations, on s'intéresse aux états visités et pas au temps
		
		for (int i=1; i<=40; i++) {
			System.out.println("");
			for (int type =min; type<=max; type++) {
				if (i<10) {
					fileName = "GameP0"+i;
				}
				else {
					fileName = "GameP"+i;
				}
				time = 0; //stocke le temps cumulé les itérations
				for (int j=0; j<iter;j++) {
					try {
						game = new Game(path + fileName + ".txt", type, false);
						result = game.solveStat();
						time += result[0];
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				if (print)
					System.out.println(fileName+"   heuristic "+type+"   average time : "+(time/iter)+"ms   "+result[1]+" visited states   "+result[2]+" moves");
				times[type-min][i-1] = time/iter;
				visited[type-min][i-1] = result[1];
			}
		}
		
		if (sorted){
			if (timeGraph)
				printHeuristicsSorted(times, sorte_comp, "Comparaison heuristics : temps en fonction des games");
			else
				printHeuristicsSorted(visited, sorte_comp, "Comparaison heuristics : nombre d'états visités en fonction des games");
		}
		else{
			if (timeGraph)
				printHeuristics(times, "Comparaison heuristics : temps en fonction des games");
			else
				printHeuristics(visited, "Comparaison heuristics : nombre d'états visités en fonction des games");
		}
		
		
		for (int type=min; type<=max;type+=1) {
			double sumTime = 0;
			double sumVisitedStates = 0;
			for (int i=0;i<times[0].length;i++) {
				sumTime += times[type][i];
				sumVisitedStates += visited[type][i];
			}
			System.out.println("Heuristic "+type+"   speed "+sumTime/sumVisitedStates);
		}
	}
	
	public Color randomColor(Random rand){ //renvoie une couleur aléatoire
		float red = rand.nextFloat();
		float green = rand.nextFloat();
		float blue = rand.nextFloat();
		Color randomColor = new Color(red, green, blue);
		return (randomColor);
	}
	
	public Color chooseColor(Color[] colors, int i){
		//Renvoie une couleur aléatoire qui n'est pas déjà contenue dans colors.
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
	

		public void sorteHeuristics(double[][] times, int comp){
		int n = times.length;
		int m = times[0].length;
		for (int i = 1; i < m; i++){
			int j = i;
			double value = times[comp][i];
			while ((j - 1 >= 0) && (times[comp][j-1] > value)){
				times[comp][j] = times[comp][j-1];
				j -= 1;
			}
			times[comp][j] = value;
			//Echange les temps des autres heuristics
			for (int k = 0; k < n; k++){
				if (k != comp){
					double aux = times[k][i];
					times[k][i] = times[k][j];
					times[k][j] = aux;		
				}
			}
		}
	}
	
	public void printHeuristicsSorted(double[][] times, int comp, String title){
		System.out.println("printing plot...");
		Plot2DPanel plot = new Plot2DPanel();
		sorteHeuristics(times, comp);
		int n = times.length;
		int m = times[0].length;
		Color[] colors = new Color[n];		
		
		double[] x = new double[m];
		for (int k = 0; k < m; k++){
			x[k] = k;
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
			case 5: c = Color.MAGENTA;break;
			default: c = chooseColor(colors, i);break;
			}
			colors[i] = c;
			plot.addLegend("SOUTH");
			plot.addLinePlot(name, c, x, y);
		}
		
		JFrame window = new JFrame(title);
		window.setSize(2000,1000);
		window.setContentPane(plot);
		window.setVisible(true);
	}
	

	public void printHeuristics(double[][] times, String title){
		//Affiche le temps mis par chaque heuristiques en fonction de la partie, en utilisant Plot2DPanel.
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
			case 5: c = Color.MAGENTA;break;
			default: c = chooseColor(colors, i);break;
			}
			colors[i] = c;
			plot.addLegend("SOUTH");
			plot.addLinePlot(name, c, x, y);
		}
		
		JFrame window = new JFrame(title);
		window.setSize(2000,1000);
		window.setContentPane(plot);
		window.setVisible(true);
	}
}
