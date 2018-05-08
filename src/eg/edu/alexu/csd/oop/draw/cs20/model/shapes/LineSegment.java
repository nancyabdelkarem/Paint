package eg.edu.alexu.csd.oop.draw.cs20.model.shapes;

import eg.edu.alexu.csd.oop.draw.cs20.model.Shapes;

import java.awt.BasicStroke;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
public class LineSegment extends Shapes {
	

	public LineSegment() {
		prop = new HashMap<>();
        prop.put("z", 0.0);
        prop.put("w", 0.0);
	}
	

	@Override
	public void draw(Graphics canvas) {
		((Graphics2D) canvas).setColor(getFillColor());
        ((Graphics2D) canvas).setStroke(new BasicStroke(2));
        ((Graphics2D) canvas).setColor(getColor());
        ((Graphics2D) canvas).drawLine((int) position.getX(),
                (int) position.getY(),prop.get("z").intValue()
                ,prop.get("w").intValue() );

	}
	@Override
	public Object clone() throws CloneNotSupportedException{
		   Shapes r = new LineSegment();
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
