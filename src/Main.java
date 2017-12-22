import java.io.IOException;

import GraphicGame.FileNameFilter;
import GraphicGame.LoadingWindow;

public class Main {
	
	public static void main(String[] args) throws IOException{
		boolean useBrutForce = false;
		int typeheuristic = 4;
		LoadingWindow gui = new LoadingWindow(typeheuristic, useBrutForce);
		gui.setVisible(true);
	}
}
