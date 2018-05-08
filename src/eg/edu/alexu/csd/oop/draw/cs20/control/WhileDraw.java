package eg.edu.alexu.csd.oop.draw.cs20.control;

import java.awt.Color;


import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import eg.edu.alexu.csd.oop.draw.Shape;
import eg.edu.alexu.csd.oop.draw.cs20.model.shapes.Circle;
import eg.edu.alexu.csd.oop.draw.cs20.model.shapes.Ellipse;
import eg.edu.alexu.csd.oop.draw.cs20.model.shapes.LineSegment;
import eg.edu.alexu.csd.oop.draw.cs20.model.shapes.Rectangle;
import eg.edu.alexu.csd.oop.draw.cs20.model.shapes.Square;
import eg.edu.alexu.csd.oop.draw.cs20.model.shapes.Triangle;

public class WhileDraw {
	 public Shape whileDraw(final int selected, final double x1, final double y1,
	            final double x3, final double y3, final Color fillc, final boolean triangle,
	            final Color outc,final boolean mouseMove,final ArrayList<Point> points) {
		 if(selected==1){
				LineSegment line = new LineSegment();
				 Point point = new Point();
	    		point.setLocation(x1, y1);
	    		line.setPosition(point);
	    		Map<String, Double> prop1;
	    		prop1 = new HashMap<>();
	    	    prop1.put("z", x3);
	    	    prop1.put("w", y3);
	    	    line.setProperties(prop1);
	    	    line.setColor(outc);
	    	    line.setFillColor(fillc);
				return line;
			}else if (selected == 2) {
	            double f, s, r1, r2;
	            f = 2 * x1 - x3;
	            s = 2 * y1 - y3;
	            r1 = Math.abs(x3 - x1) * 2;
	            r2 = Math.abs(y3 - y1) * 2;
	            if (x1 > x3)
	                f -= r1;
	            if (y1 > y3)
	                s -= r2;
	            Ellipse ellipse= new Ellipse();
	            ellipse.setColor(outc);
	            ellipse.setFillColor(fillc);
	            Point point = new Point();
	    		point.setLocation(f, s);
	    		ellipse.setPosition(point);
	    		Map<String, Double> prop1;
	    		prop1 = new HashMap<>();
			    prop1.put("rectwidth", r1);
			    prop1.put("rectheight", r2);
			    ellipse.setProperties(prop1);
	            return ellipse;
	        } else if (selected == 3) {
	            double r = Math.sqrt(Math.pow(x3 - x1, 2) + Math.pow(y3 - y1, 2));
	            Circle circle = new Circle();
	            circle.setColor(outc);
	            circle.setFillColor(fillc);
	            Point point = new Point();
	    		point.setLocation(x1 - r, y1 - r);
	    		circle.setPosition(point);
	    		Map<String, Double> prop1;
	    		prop1 = new HashMap<>();
			    prop1.put("radius", r * 2.0);
			    circle.setProperties(prop1);
	        	return circle;
	        }else if (selected == 4) {
	            double f, s;
	            f = x1;
	            if (x1 > x3)
	                f -= Math.abs(x3 - x1);
	            s = y1;
	            if (y1 > y3)
	                s -= Math.abs(y3 - y1);
	            Rectangle rectangle=new Rectangle();
	            Point point = new Point();
	    		point.setLocation(f, s);
	        	rectangle.setPosition(point);
	        	rectangle.setColor(outc);
	        	rectangle.setFillColor(fillc);
	        	Map<String, Double> prop1;
	    		prop1 = new HashMap<>();
			    prop1.put("Width", Math.abs(x3 - x1));
			    prop1.put("Length", Math.abs(y3 - y1));
			    rectangle.setProperties(prop1);
	        	return rectangle;
	        }else if (selected == 5) {
	            double r = Math.sqrt((x3 - x1) * (x3 - x1) + (y3 - y1) * (y3 - y1));
	            r /= Math.sqrt(2);
	            double f, s;
	            f = x1;
	            if (x1 > x3)
	                f -= r;
	            s = y1;
	            if (y1 > y3)
	                s -= r;
	            Square square=new Square();
	            Point point = new Point();
	    		point.setLocation(f, s);
	    		square.setPosition(point);
	    		square.setColor(outc);
	    		square.setFillColor(fillc);
	        	Map<String, Double> prop1;
	    		prop1 = new HashMap<>();
			    prop1.put("Side", r);
			    square.setProperties(prop1);
	        	return square;
	        } else if (selected == 6 && triangle) {
	        	LineSegment line = new LineSegment();
				 Point point = new Point();
	    		point.setLocation(x1, y1);
	    		line.setPosition(point);
	    		Map<String, Double> prop1;
	    		prop1 = new HashMap<>();
	    	    prop1.put("z", x3);
	    	    prop1.put("w", y3);
	    	    line.setProperties(prop1);
	    	    line.setColor(outc);
	    	    line.setFillColor(fillc);
				return line;
	        } else if (selected == 6 && mouseMove) {
	        	Point temp = new Point();
	            temp.setLocation(x3, y3);
	            points.add(temp);
	            Triangle triangl=new Triangle();
	            triangl.setPosition(points.get(0));
	            triangl.setColor(outc);
	            triangl.setFillColor(fillc);
	            Map<String, Double> prop1;
	    		prop1 = new HashMap<>();
	    	    prop1.put("x1", points.get(0).getX());
	    	    prop1.put("y1", points.get(0).getY());
	    	    prop1.put("x2", points.get(1).getX());
	    	    prop1.put("y2", points.get(1).getY());
	    	    prop1.put("x3", points.get(2).getX());
	    	    prop1.put("y3", points.get(0).getY());
	    	    triangl.setProperties(prop1);
	            points.remove(2);
	            return triangl;
	        }
		 LineSegment line = new LineSegment();
		 Point point = new Point();
		point.setLocation(0, 0);
		line.setPosition(point);
		Map<String, Double> prop1;
		prop1 = new HashMap<>();
	    prop1.put("z", 0.0);
	    prop1.put("w", 0.0);
	    line.setProperties(prop1);
	    line.setColor(outc);
	    line.setFillColor(fillc);
		return line;
		 
	 }
}
