package eg.edu.alexu.csd.oop.draw.cs20.control;

import java.io.File;

import java.net.URL;
import java.net.URLClassLoader;

import javax.swing.JLabel;
import javax.swing.JOptionPane;

public class DynamicClassLoader {
    
	private String path;
	private String type;
	private Class<?> loadedClass;
	
	
	public DynamicClassLoader(String path,String type){
		this.path=path;
		this.type=type;
		this.loadedClass=null;
	}
	
	public Class<?> execute(){
		File myJar=new File(path);
		URL myJarURL;
		try {
			myJarURL = myJar.toURI().toURL();
			ClassLoader loader = URLClassLoader.newInstance(new URL[] { myJarURL });
			loadedClass = loader.loadClass("eg.edu.alexu.csd.oop.draw.cs." + type);

		} catch (Exception e) {
			JLabel label = new JLabel("Please choose a valid jar file");
			JOptionPane.showMessageDialog(null, label);
		}
//		try {
//			jarFile = new JarFile(path);
//		} catch (IOException e2) {
//			// TODO Auto-generated catch block
//			e2.printStackTrace();
//		}
//		try {
//            jarFile = new JarFile(path);
//            URL[] urls = { new URL("jar:" + path+"!/") };
//			Enumeration<JarEntry> e = jarFile.entries();
//			URLClassLoader cl = URLClassLoader.newInstance(urls);
//
//			while (e.hasMoreElements()) {
//			    JarEntry je = e.nextElement();
//			    if(je.isDirectory() || !je.getName().endsWith(".class")){
//			    	System.out.println(je.getName());
//			        continue;
//			    }   
//			    // -6 because of .class
//			    String className = je.getName().substring(0,je.getName().length()-6);
//			    className = className.replace('/', '.');
//			    System.out.println(className);
//			     c = cl.loadClass(className);
//
//			}
//
//		} catch (Exception e1) {
//			JLabel label = new JLabel("Please choose a valid jar file");
//			JOptionPane.showMessageDialog(null, label);
//		}
		return loadedClass;
	}
	
}
