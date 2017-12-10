import java.util.LinkedList;

public class Main {

	public static void main(String[] args) {
		System.out.println("Hello Benrenice");
		String path ="file.txt";
		Game game = new Game(path);
		LinkedList<Move> solution = game.solve();
		System.out.println(solution);
	}

}
