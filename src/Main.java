import java.awt.Color;
import java.io.IOException;

import javax.swing.JFrame;

import org.math.plot.Plot2DPanel;

import Game.Game;
import GraphicGame.LoadingWindow;

public class Main {
	
	public static void main(String[] args) throws IOException{
		
		boolean print_etat = false;
		
		boolean graphicTest = false; //si true, affiche l'animation graphique. Sinon, compare les heuristiques.
		
		//paramètres pour le graphicTest
		boolean useBruteForce = false; //si true, on n'utilise pas d'heuristiques
		int typeHeuristic = 5; //si on utilise une heuristique, on utilise celle-là
		
		//paramètres pour CompareHeuristics
		boolean SortedPrint = true; //si true, trie les abscisses. Sinon, garde l'ordre 1 à 40 des games.
		int SortedPrintComp = 0; //si on trie, on trie par rapports aux résulats de cette heuristique
		boolean print = true; //si true, affiche les résultats au fur et à mesure
		boolean timeGraph = false; //si true, fait le graphe du temps d'exécution. Sinon, fait le graphe du nombre d'états visités.
		int iter = 100; //nombre d'itérations pour moyenner le temps
		
		
		if (print_etat){
			Game g = new Game("Games/gameP02.txt", typeHeuristic, useBruteForce);
			int d = (g.solve()).size();
			int n = Integer.max(50, 2*d);
			double[] res = g.compte_etats(n);
			double[] x = new double[n+1];
			for (int i = 0; i <n+1; i++){
				x[i] = i;
			}
			Plot2DPanel plot = new Plot2DPanel();
			plot.addLinePlot("Nbr états", Color.red, x, res);
			
			JFrame window = new JFrame("Nombre d'états en fonction de la distance à l'état initial");
			window.setSize(2000,1000);
			window.setContentPane(plot);
			window.setVisible(true);
			
		}
		
		else {
			if (graphicTest) {
				LoadingWindow gui = new LoadingWindow(typeHeuristic, useBruteForce);
				gui.setVisible(true);
				
			}
			else {
				CompareHeuristic c = new CompareHeuristic(0,5);
				c.run(iter, SortedPrint, SortedPrintComp, print, timeGraph);
			}
		}
	}
}