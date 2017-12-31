import java.io.IOException;

import GraphicGame.LoadingWindow;

public class Main {
	
	public static void main(String[] args) throws IOException{
		boolean graphicTest = true;
		boolean useBrutForce = false;
		int typeheuristic = 5;
		
		if (graphicTest) {
			LoadingWindow gui = new LoadingWindow(typeheuristic, useBrutForce);
			gui.setVisible(true);
			
		}
		else {
			CompareHeuristic c = new CompareHeuristic(0,5);
			c.run(500);
		}
	}
}