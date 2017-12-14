package GraphicGame;

import java.io.File;
import java.io.FilenameFilter;

public class FileNameFilter implements FilenameFilter{
	
	public boolean accept(File dir, String name){
		String d = name.substring(0, 4);
		return (d.equals("Game"));
	}

}
