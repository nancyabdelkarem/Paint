package eg.edu.alexu.csd.oop.draw.cs20.model.shapes;

import java.awt.Graphics;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.BasicStroke;
import eg.edu.alexu.csd.oop.draw.cs20.model.Shapes;

public class Triangle extends Shapes{
	int[] xPoints ;
	int[] yPoints ;
    private Point point = new Point();
	    public Triangle() {
	    	prop = new HashMap<>();
		    prop.put("x1", 0.0);
		    prop.put("y1", 0.0);
		    prop.put("x2", 0.0);
		    prop.put("y2", 0.0);
		    prop.put("x3", 0.0);
		    prop.put("y3", 0.0);
		    
	    } 

	    
	@Override
	public void draw(Graphics canvas) {
		point.x=(int)prop.get("x1").intValue();
	    point.y=(int)prop.get("y1").intValue();
	    setPosition(point);
		 ((Graphics2D) canvas).setColor(getFillColor());
	    ((Graphics2D) canvas).fillPolygon(new int[] {getPosition().x,
	    		prop.get("x2").intValue()
	    		,prop.get("x3").intValue()
	    		}, new int[] {getPosition().y,
	    				prop.get("y2").intValue(),
	    				prop.get("y3").intValue()}, 3);

	        ((Graphics2D) canvas).setStroke(new BasicStroke(2));
	        ((Graphics2D) canvas).setColor(getColor());
	        ((Graphics2D) canvas).drawPolygon(new int[] {getPosition().x,
		    		prop.get("x2").intValue()
		    		,prop.get("x3").intValue()
		    		}, new int[] {getPosition().y,
		    				prop.get("y2").intValue(),
		    				prop.get("y3").intValue()} , 3);

	}
	@Override
	public Object clone() throws CloneNotSupportedException{
		 final Shapes r = new Triangle();
		    r.setColor(this.getColor());
			r.setFillColor(this.getFillColor());
			r.setPosition(this.getPosition());
			 final Map<String, Double> newprop = new HashMap<>();
		        for (final Iterator<Entry<String, Double>> iterator = prop.entrySet().iterator(); iterator.hasNext();) {
					final Entry<String, Double> s = iterator.next();
					newprop.put(s.getKey(), s.getValue());
				}
		        r.setProperties(newprop);
	        return r;
	}

}
