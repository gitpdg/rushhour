import java.io.IOException;

import GraphicGame.LoadingWindow;

public class Main {
	
	public static void main(String[] args) throws IOException{
		
		boolean graphicTest = false; //si true, affiche l'animation graphique. Sinon, compare les heuristiques.
		
		//param�tres pour le graphicTest
		boolean useBrutForce = false; //si true, on n'utilise pas d'heuristiques
		int typeheuristic = 5; //si on utilise une heuristique, on utilise celle-l�
		
		//param�tres pour CompareHeuristics
		boolean SortedPrint = false; //si true, trie les abscisses. Sinon, garde l'ordre 1 � 40 des games.
		int SortedPrintComp = 0; //si on trie, on trie par rapports aux r�sulats de cette heuristique
		boolean print = false; //si true, affiche les r�sultats au fur et � mesure
		boolean timeGraph = true; //si true, fait le graphe du temps d'ex�cution. Sinon, fait le graphe du nombre d'�tats visit�s.
		int iter = 10; //nombre d'it�rations pour moyenner le temps
		
		if (graphicTest) {
			LoadingWindow gui = new LoadingWindow(typeheuristic, useBrutForce);
			gui.setVisible(true);
			
		}
		else {
			CompareHeuristic c = new CompareHeuristic(0,5);
			c.run(iter, SortedPrint, SortedPrintComp, print, timeGraph);
		}
	}
}