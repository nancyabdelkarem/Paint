package eg.edu.alexu.csd.oop.draw.cs20.model.shapes;

import java.awt.BasicStroke;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import eg.edu.alexu.csd.oop.draw.cs20.model.Shapes;

public class Square extends Shapes{

    public Square() {
        prop = new HashMap<>();
        prop.put("Side", 0.0);
    }
    
	@Override
	public void draw(Graphics canvas) {
		((Graphics2D) canvas).setColor(getFillColor());
		
        ((Graphics2D) canvas).fillRect((int) getPosition().getX(),
                (int) getPosition().getY(),
                (int) prop.get("Side").intValue(),
                (int) prop.get("Side").intValue());

        ((Graphics2D) canvas).setStroke(new BasicStroke(2));
        ((Graphics2D) canvas).setColor(getColor());
        ((Graphics2D) canvas).drawRect((int) getPosition().getX(),
                (int) getPosition().getY(),
                (int) prop.get("Side").intValue(),
                (int) prop.get("Side").intValue());
		
	}
	@Override
	public Object clone() throws CloneNotSupportedException{
		   Shapes r = new Square();
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
