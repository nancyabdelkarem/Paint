package eg.edu.alexu.csd.oop.draw.cs20.model;


import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.util.Map;

import eg.edu.alexu.csd.oop.draw.Shape;



public  class Shapes implements Shape{

	protected Point position;
    protected Map<String, Double> prop;
    protected Color color;
    protected Color fcolor;


	@Override
	public void setPosition(Point position) {
		this.position = position;

	}

	@Override
	public Point getPosition() {

		return position;
	}

	@Override
	public void setProperties(Map<String, Double> properties) {
		prop=properties;

	}

	@Override
	public Map<String, Double> getProperties() {

		return prop;
	}

	@Override
	public void setColor(Color color) {
		this.color=color;

	}

	@Override
	public Color getColor() {

		return color;
	}

	@Override
	public void setFillColor(Color color) {

		fcolor=color;
	}

	@Override
	public Color getFillColor() {

		return fcolor;
	}
	@Override
	public void draw(Graphics canvas) {
	}
	@Override
	public Object clone() throws CloneNotSupportedException{

      return super.clone();

	}

	
}
