import java.io.IOException;

import GraphicGame.FileNameFilter;
import GraphicGame.LoadingWindow;

public class Main {
	
	public static void main(String[] args) throws IOException{
		boolean graphicTest = false;
		boolean useBrutForce = false;
		int typeheuristic = 1;
<<<<<<< HEAD
		
		if (graphicTest) {
			LoadingWindow gui = new LoadingWindow(typeheuristic, useBrutForce);
			gui.setVisible(true);
			
		}
		else {
			CompareHeuristic c = new CompareHeuristic(0,4);
			c.run();
		}
=======
		LoadingWindow gui = new LoadingWindow(typeheuristic, useBrutForce);
		gui.setVisible(true);
>>>>>>> branch 'master' of https://github.com/gitpdg/rushhour
	}
}