package game;
import java.util.LinkedList;
import java.io.*;

public class Main {

	public static void main(String[] args) throws IOException {
		String path = "file.txt";
		Game game = new Game(path);
		LinkedList<Move> solution = game.solve();
		System.out.println(solution.toString());
		System.out.println("fin !!!");
	}

}
