package GraphicGame;

import java.io.File;
import java.io.FilenameFilter;

public class FileNameFilter implements FilenameFilter{
	//Teste si un nom de fichier commence par "Game"
	
	public boolean accept(File dir, String name){
		String d = name.substring(0, 4);
		return (d.equals("Game"));
	}

}
