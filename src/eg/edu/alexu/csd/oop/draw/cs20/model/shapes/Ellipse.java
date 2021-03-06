package eg.edu.alexu.csd.oop.draw.cs20.model.shapes;

import eg.edu.alexu.csd.oop.draw.cs20.model.Shapes;

import java.awt.BasicStroke;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.awt.geom.Ellipse2D;
public class Ellipse extends Shapes{
	public Ellipse(){
		prop = new HashMap<>();
	    prop.put("rectwidth", 0.0);
	    prop.put("rectheight", 0.0);

	}
	
	@Override
	public void draw(Graphics canvas) {
		((Graphics2D) canvas).setColor(getFillColor());
        ((Graphics2D) canvas).fill(new Ellipse2D.Double((int) position.getX(),
                (int) position.getY(),
                prop.get("rectwidth").intValue(),
                prop.get("rectheight").intValue()));

        ((Graphics2D) canvas).setStroke(new BasicStroke(2));
        ((Graphics2D) canvas).setColor(getColor());
        ((Graphics2D) canvas).draw(new Ellipse2D.Double((int) position.getX(),
        		(int) position.getY(),
                prop.get("rectwidth").intValue(),
                prop.get("rectheight").intValue()));

	}
	@Override
	public Object clone() throws CloneNotSupportedException{
		final Shapes r = new Ellipse();
        r.setColor(color);
        r.setFillColor(fcolor);
        r.setPosition(position);
        final Map<String, Double> newprop = new HashMap<>();
        for (final Iterator<Entry<String, Double>> iterator = prop.entrySet().iterator(); iterator.hasNext();) {
			final Entry<String, Double> s = iterator.next();
			newprop.put(s.getKey(), s.getValue());
		}
        r.setProperties(newprop);
        return r;

	}

}
