package eg.edu.alexu.csd.oop.draw.cs20.control;

import java.io.File;

import javax.swing.filechooser.FileFilter;

public class JsonFileFilter extends FileFilter{

	@Override
	public boolean accept(File f) {
		if(f.isDirectory()){
		return false;
		}
		String s=f.getName();
		return s.endsWith(".json")||s.endsWith(".JSON");
	}

	@Override
	public String getDescription() {
		return ".json,.JSON";
	}

}
