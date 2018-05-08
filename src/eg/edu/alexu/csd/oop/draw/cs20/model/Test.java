package eg.edu.alexu.csd.oop.draw.cs20.model;

import java.awt.Color;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import eg.edu.alexu.csd.oop.draw.Shape;

import eg.edu.alexu.csd.oop.draw.cs20.model.shapes.Circle;
import eg.edu.alexu.csd.oop.draw.cs20.model.shapes.Ellipse;
import eg.edu.alexu.csd.oop.draw.cs20.model.shapes.LineSegment;
import eg.edu.alexu.csd.oop.draw.cs20.model.shapes.Rectangle;
import eg.edu.alexu.csd.oop.draw.cs20.model.shapes.Square;
import eg.edu.alexu.csd.oop.draw.cs20.model.shapes.Triangle;

public class Test  {

public static void main(String[] args) {
	MyDrawingEngine engine = new MyDrawingEngine();
	Circle circle = new Circle();
	engine.addShape(circle);
	final Point point = new Point();
	point.x=150;
	point.y=100;
	circle.setPosition(point);
	circle.setColor(Color.RED);
	circle.setFillColor(Color.RED);
	Rectangle rectangle=new Rectangle();
	rectangle.setPosition(point);
	rectangle.setColor(Color.RED);
	rectangle.setFillColor(Color.RED);
	engine.addShape(rectangle);
	engine.addShape(new Square());
	Shape [] shapes =engine.getShapes();
	for(Shape x : shapes) {
		System.out.println(x.getClass().getName());
	}System.out.println();
	engine.removeShape(rectangle);
	engine.removeShape(circle);
	shapes =engine.getShapes();
	for(Shape x : shapes) {
		System.out.println(x.getClass().getName());
	}System.out.println();
	engine.undo();
	engine.undo();
	shapes =engine.getShapes();
	for(Shape x : shapes) {
		System.out.println(x.getClass().getName());
	}System.out.println();
	engine.redo();
	shapes =engine.getShapes();
	for(Shape x : shapes) {
		System.out.println(x.getClass().getName());
		}System.out.println();
	engine.save("c:\\test\\Save.xml");
	LineSegment line = new LineSegment();
	System.out.println("hnanananan");
	System.out.println(line.getProperties().size());
	engine.addShape(line);
	Triangle triangle = new Triangle();
	engine.addShape(triangle);
	shapes =engine.getShapes();
	for(Shape x : shapes) {
		System.out.println(x.getClass().getName());
	}System.out.println();
	/*engine.load("c:\\test\\Save.xml");
	System.out.println("--------------");
	shapes =engine.getShapes();
	System.out.println();
	for(Shape x : shapes) {
		System.out.println(x.getClass().getSimpleName());
	}System.out.println();*/
	List<Class<? extends Shape>>support=new ArrayList<>();
	support=engine.getSupportedShapes();
	System.out.println(support.size());





}

}