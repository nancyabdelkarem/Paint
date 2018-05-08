package eg.edu.alexu.csd.oop.draw.cs20.control;

import java.awt.Color;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import eg.edu.alexu.csd.oop.draw.Shape;
import eg.edu.alexu.csd.oop.draw.cs20.model.MyDrawingEngine;
import eg.edu.alexu.csd.oop.draw.cs20.model.Shapes;
import eg.edu.alexu.csd.oop.draw.cs20.model.shapes.Circle;
import eg.edu.alexu.csd.oop.draw.cs20.model.shapes.Ellipse;
import eg.edu.alexu.csd.oop.draw.cs20.model.shapes.LineSegment;
import eg.edu.alexu.csd.oop.draw.cs20.model.shapes.Rectangle;
import eg.edu.alexu.csd.oop.draw.cs20.model.shapes.Square;
import eg.edu.alexu.csd.oop.draw.cs20.model.shapes.Triangle;

public class Grid extends JPanel {
	MyDrawingEngine engine = new MyDrawingEngine();
	private static final long serialVersionUID = 1L;
	Graphics g;
	//vars for mouse actions
	private double x1, x2, x3, y1, y2, y3, z1, z2, dragFromX, dragFromY;
//	selected is for to know which shape is selected to draw it
//	shapeAction is a var which take the index of the shape in the array to make actions in it
	private int selected = 0, shapeAction = -1, manySel = 0;
	private Color outc, fillc;
	private boolean mouseClick, mouseMove, triangle;
//	this array list to put in it the shapes in the grid
	private ArrayList<Shape> data;
//	to save image
	private BufferedImage curGrid;
//	this for triangle
	private ArrayList<Point> points;
//	temp array to put arraylist to put the points in it to make actions in triangle
	private ArrayList<Point> points1;
//	to save image
	public static BufferedImage image;
	public static File imageDirectory = null;
//	to select many shapes
	private ArrayList<Integer> shapeActions;
	private boolean shapePressed = false;
	Shape sh = new Shapes();
	Shape sh1 = new LineSegment();
	Shape sh2 = new LineSegment();
	public int actionNum = 0;
//constractor to initialize the class
	public Grid() {
		points = new ArrayList<Point>();
		points1 = new ArrayList<Point>();
		shapeActions = new ArrayList<Integer>();
		mouseClick = false;
		mouseMove = false;
		triangle = false;
		MyListener();
		repaint();
	}
//this to draw the shape while dragging it
	private void whileDraw(Graphics2D g2d) {
		WhileDraw get = new WhileDraw();
		Shape x = get.whileDraw(selected, x1, y1, x2, y2, fillc, triangle, outc, mouseMove, points);
		x.draw(g2d);
	}

	public void paintComponent(Graphics g) {
		data = engine.Data();
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		curGrid = new BufferedImage(800, 600, BufferedImage.TYPE_INT_ARGB);
		Graphics2D i2d = curGrid.createGraphics();
		engine.refresh(g2d);
		engine.refresh(i2d);
		if (mouseClick || mouseMove)
			whileDraw(g2d);
	}

	/*
	 * this function to set a shape and convert from points to width,length,....
	 */
	private void SetShape() {
		if (selected == 1) {
			LineSegment line = new LineSegment();
			engine.addShape(line);
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
			selected = 0;
		} else if (selected == 2) {
			double f, s, r1, r2;
			f = 2 * x1 - x3;
			s = 2 * y1 - y3;
			r1 = Math.abs(x3 - x1) * 2;
			r2 = Math.abs(y3 - y1) * 2;
			if (x1 > x3)
				f -= r1;
			if (y1 > y3)
				s -= r2;
			Ellipse ellipse = new Ellipse();
			engine.addShape(ellipse);
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
			selected = 0;
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
			engine.addShape(circle);
			selected = 0;
		} else if (selected == 4) {
			double f, s;
			f = x1;
			if (x1 > x3)
				f -= Math.abs(x3 - x1);
			s = y1;
			if (y1 > y3)
				s -= Math.abs(y3 - y1);
			Rectangle rectangle = new Rectangle();
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
			engine.addShape(rectangle);
			selected = 0;
		} else if (selected == 5) {
			double r = Math.sqrt((x3 - x1) * (x3 - x1) + (y3 - y1) * (y3 - y1));
			r /= Math.sqrt(2);
			double f, s;
			f = x1;
			if (x1 > x3)
				f -= r;
			s = y1;
			if (y1 > y3)
				s -= r;
			Square square = new Square();
			Point point = new Point();
			point.setLocation(f, s);
			square.setPosition(point);
			square.setColor(outc);
			square.setFillColor(fillc);
			Map<String, Double> prop1;
			prop1 = new HashMap<>();
			prop1.put("Side", r);
			square.setProperties(prop1);
			engine.addShape(square);
			selected = 0;
		} else if (selected == 6 && points.size() == 2) {
			Point temp = new Point();
			temp.setLocation(x3, y3);
			points.add(temp);
			Triangle triangle = new Triangle();
			triangle.setPosition(points.get(0));
			triangle.setColor(outc);
			triangle.setFillColor(fillc);
			Map<String, Double> prop1;
			prop1 = new HashMap<>();
			prop1.put("x1", points.get(0).getX());
			prop1.put("y1", points.get(0).getY());
			prop1.put("x2", points.get(1).getX());
			prop1.put("y2", points.get(1).getY());
			prop1.put("x3", points.get(2).getX());
			prop1.put("y3", points.get(0).getY());
			triangle.setProperties(prop1);
			engine.addShape(triangle);
			for (int i = 0; i < points.size(); i++)
				points1.add(points.get(i));
			points.clear();
			selected = 0;
			mouseMove = false;
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void DrawPlugIn() {
		Class otherShape = engine.getSupportedShapes().get(0);
		Map<String, Double> prop;
		try {
			Constructor constructor = otherShape.getDeclaredConstructor();
			Shape shape = (Shape) constructor.newInstance();
			Double x = Double.parseDouble(JOptionPane.showInputDialog("x"));
			Double y = Double.parseDouble(JOptionPane.showInputDialog("y"));
			Point p = new Point();
			p.setLocation(x, y);
			shape.setPosition(p);
			shape.setColor(outc);
			shape.setFillColor(fillc);
			prop = shape.getProperties();
			Set keyset = prop.keySet();
			List keyList = new ArrayList(keyset);
			for (int i = 0; i < keyset.size(); i++) {
				Double ans = Double.parseDouble(JOptionPane.showInputDialog(keyList.get(i)));
				prop.put((String) keyList.get(i), ans);
			}
			engine.addShape(shape);
			repaint();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}

	void MyListener() {
		addMouseListener(new MouseAdapter() {

			@Override
			public void mousePressed(MouseEvent arg0) {
				x1 = arg0.getX();
				y1 = arg0.getY();
				if (SwingUtilities.isRightMouseButton(arg0)) {
					z1 = arg0.getX();
					z2 = arg0.getY();
				}
				if (selected == 0) {
					shapeAction = isClicked(x1, y1);
					if (shapeAction != -1) {
						shapePressed = true;
						data = engine.Data();
						sh = data.get(shapeAction);
						dragFromX = x1 - sh.getPosition().getX();
						dragFromY = y1 - sh.getPosition().getY();

					}

					repaint();
				}
				if (manySel == 11) {
					shapeActions.add(isClicked(x1, y1));
					repaint();

				}
				if (selected == 6 && mouseMove)
					return;
				mouseClick = true;
				triangle = true;
			}

			@Override
			public void mouseReleased(MouseEvent arg0) {
				x3 = arg0.getX();
				y3 = arg0.getY();
				mouseClick = false;
				shapePressed = false;
				if (selected != 0) {
					SetShape();
				}
				if (selected == 6) {
					Point p1 = new Point();
					p1.setLocation(x1, y1);
					points.add(p1);
					Point p2 = new Point();
					p2.setLocation(x3, y3);
					points.add(p2);
					mouseMove = true;
					triangle = false;
				}
				repaint();
			}
		});

		addMouseMotionListener(new MouseMotionAdapter() {
			public void mouseDragged(MouseEvent arg0) {
				x2 = arg0.getX();
				y2 = arg0.getY();
				if (actionNum == 1 && shapePressed) {
					if (sh.getProperties().containsKey("w")) {
						final Map<String, Double> newprop = new HashMap<>();
						newprop.put("z", x2 + (sh.getProperties().get("z") - sh.getPosition().getX()));
						newprop.put("w", y2 + (sh.getProperties().get("w") - sh.getPosition().getY()));
						sh.setProperties(newprop);
						Point p = new Point();
						p.setLocation(x2, y2);
						sh.setPosition(p);

					} else if (sh.getProperties().size() == 6) {
						final Point p = new Point();
						double dx, dy;
						dx = x2 - sh.getPosition().getX();
						dy = y2 - sh.getPosition().getY();
						p.setLocation(dx, dy);
						sh.setPosition(p);
						final Map<String, Double> newprop = new HashMap<>();
						newprop.put("x2", x2 - sh.getProperties().get("x2"));
						newprop.put("y2", y2 - sh.getProperties().get("y2"));
						newprop.put("x3", x2 - sh.getProperties().get("x3"));
						newprop.put("y3", y2 - sh.getProperties().get("y3"));
						newprop.put("x1", dx);
						newprop.put("y1", dy);
						sh.setProperties(newprop);
					} else {
						final Point p = new Point();
						double dx, dy;
						dx = x2 - dragFromX;
						dy = y2 - dragFromY;
						p.setLocation(dx, dy);
						sh.setPosition(p);
						repaint();

					}
				}
				if (actionNum == 2 && shapePressed) {
					resizeShape(sh, x2, y2);	
					repaint();
				}

				repaint();
			}

			public void mouseMoved(MouseEvent e) {
				if (mouseMove) {
					x2 = e.getX();
					y2 = e.getY();
					repaint();
				}
			}
		});
	}

	public boolean pointInLine(Shape sh, double x, double y) {

		if (sh.getPosition().getX() == sh.getProperties().get("z").doubleValue())
			return (y >= Math.min(sh.getPosition().getY(), sh.getProperties().get("w").doubleValue())
					&& (y <= Math.max(sh.getPosition().getY(), sh.getProperties().get("w").doubleValue())));
		if (sh.getPosition().getY() == sh.getProperties().get("w").doubleValue())
			return (x >= Math.min(sh.getPosition().getX(), sh.getProperties().get("z").doubleValue())
					&& (x <= Math.max(sh.getPosition().getX(), sh.getProperties().get("z").doubleValue())));
		double slope = (sh.getPosition().getY() - sh.getProperties().get("w").doubleValue())
				/ (sh.getPosition().getX() - sh.getProperties().get("z").doubleValue());
		double val = (slope * x) - (slope * sh.getProperties().get("z").doubleValue())
				+ sh.getProperties().get("w").doubleValue();
		return Math.abs(y - val) <= 8;
	}

	public boolean pointInEllipse(Shape sh, double x, double y) {
		double tempx, tempy, r1, r2;
		r1 = sh.getProperties().get("rectwidth").doubleValue() / 2;
		r2 = sh.getProperties().get("rectheight").doubleValue() / 2;
		tempx = r1 + sh.getPosition().getX();
		tempy = r2 + sh.getPosition().getY();
		double val1 = ((x - tempx) * (x - tempx)) / (r1 * r1);
		double val2 = ((y - tempy) * (y - tempy)) / (r2 * r2);
		return val1 + val2 <= 1;
	}

	public boolean pointInCircle(Shape sh, double x, double y) {
		double tempx, tempy, r1, r2;
		r1 = sh.getProperties().get("radius").doubleValue() / 2;
		r2 = sh.getProperties().get("radius").doubleValue() / 2;
		tempx = r1 + sh.getPosition().getX();
		tempy = r2 + sh.getPosition().getY();
		double val1 = ((x - tempx) * (x - tempx)) / (r1 * r1);
		double val2 = ((y - tempy) * (y - tempy)) / (r2 * r2);
		return val1 + val2 <= 1;

	}

	public boolean pointInRect(Shape sh, double x, double y) {
		double x1, x2, y1, y2;
		x1 = sh.getPosition().getX();
		y1 = sh.getPosition().getY();
		x2 = x1 + sh.getProperties().get("Width").doubleValue();
		y2 = y1 + sh.getProperties().get("Length").doubleValue();
		boolean v1, v2;
		v1 = (x >= x1 && x <= x2);
		v2 = (y >= y1 && y <= y2);
		return v1 && v2;
	}

	public boolean pointInSquare(Shape sh, double x, double y) {
		double x1, x2, y1, y2;
		x1 = sh.getPosition().getX();
		y1 = sh.getPosition().getY();
		x2 = x1 + sh.getProperties().get("Side").doubleValue();
		y2 = y1 + sh.getProperties().get("Side").doubleValue();
		boolean v1, v2;
		v1 = (x >= x1 && x <= x2);
		v2 = (y >= y1 && y <= y2);
		return v1 && v2;
	}

	public double area(double x1, double y1, double x2, double y2, double x3, double y3) {
		return Math.abs((x1 * (y2 - y3) + x2 * (y3 - y1) + x3 * (y1 - y2)) / 2.0);
	}

	public boolean pointInTriangle(Shape sh, double x, double y) {
		double x1, x2, x3, y1, y2, y3;
		x1 = points1.get(0).getX();
		x2 = points1.get(1).getX();
		x3 = points1.get(2).getX();
		y1 = points1.get(0).getY();
		y2 = points1.get(1).getY();
		y3 = points1.get(2).getY();
		double A = area(x1, y1, x2, y2, x3, y3);
		double A1 = area(x, y, x2, y2, x3, y3);
		double A2 = area(x1, y1, x, y, x3, y3);
		double A3 = area(x1, y1, x2, y2, x, y);
		points1.clear();
		return (A == A1 + A2 + A3);

	}

	public int isClicked(double x, double y) {
		data = engine.Data();
		for (int i = 0; i < data.size(); i++) {
			if (data.get(i).getProperties().containsKey("w")) {
				if (pointInLine(data.get(i), x, y))
					return i;
			} else if (data.get(i).getProperties().containsKey("rectwidth")) {
				if (pointInEllipse(data.get(i), x, y))
					return i;
			} else if (data.get(i).getProperties().containsKey("radius")) {
				if (pointInCircle(data.get(i), x, y))
					return i;
			} else if (data.get(i).getProperties().containsKey("Width")) {
				if (pointInRect(data.get(i), x, y))
					return i;
			} else if (data.get(i).getProperties().containsKey("Side")) {
				if (pointInSquare(data.get(i), x, y))
					return i;
			} else if (data.get(i).getProperties().size() == 6) {
				if (pointInTriangle(data.get(i), x, y))
					return i;
			}
		}
		return -1;
	}

	public void SaveFiles() {
		JFileChooser save = new JFileChooser();
		save.addChoosableFileFilter(new ImageFileFilter());
		save.addChoosableFileFilter(new XmlFileFilter());
		save.addChoosableFileFilter(new JsonFileFilter());
		int n = save.showSaveDialog(save);
		if (n == JFileChooser.APPROVE_OPTION) {
			String ext = "";
			String extension = save.getFileFilter().getDescription();
			if (extension.equals(".png,.PNG") || save.getSelectedFile().toString().endsWith(".png")) {
				ext = ".png";
				if (!save.getSelectedFile().toString().endsWith(ext)) {
					save.setSelectedFile(new File(save.getSelectedFile().toString() + ext));
				}
				try {
					BufferedImage image = getImg();
					File outputfile = save.getSelectedFile();
					ImageIO.write(image, "png", outputfile);
				} catch (IOException e) {
					e.printStackTrace();
				}
			} else if (extension.equals(".xml,.XML") || save.getSelectedFile().toString().endsWith(".xml")) {
				ext = ".xml";
				String path = save.getSelectedFile().toString();
				if (!save.getSelectedFile().toString().endsWith(ext)) {
					save.setSelectedFile(new File(save.getSelectedFile().toString() + ext));
					path = path + ".xml";
				}
				engine.save(path);

			} else if (extension.equals(".json,.JSON") || save.getSelectedFile().toString().endsWith(".json")) {
				ext = ".json";
				String path = save.getSelectedFile().toString();
				if (!save.getSelectedFile().toString().endsWith(ext)) {
					save.setSelectedFile(new File(save.getSelectedFile().toString() + ext));
					path = path + ".json";
				}
				engine.save(path);
			} else
				JOptionPane.showMessageDialog(null, "File Extension Invalid ! Please enter a valid File Extension");

		}
	}

	public void LoadFiles() {
		JFileChooser load = new JFileChooser();
		load.addChoosableFileFilter(new XmlFileFilter());
		load.addChoosableFileFilter(new JsonFileFilter());
		int n = load.showOpenDialog(null);
		if (n == JFileChooser.APPROVE_OPTION) {
			if (load.getSelectedFile().getName().endsWith(".xml")) {
				String path = load.getSelectedFile().toString();
				engine.load(path);
				repaint();

			} else if (load.getSelectedFile().getName().endsWith(".json")) {
				String path = load.getSelectedFile().toString();
				engine.load(path);
				repaint();
			} else
				JOptionPane.showMessageDialog(null, "File Extension Invalid ! Please enter a valid File Extension");
		}
	}

	public void LoadPlugIn() {
		JFileChooser loadPlug = new JFileChooser();
		loadPlug.addChoosableFileFilter(new JarFileFilter());
		int n = loadPlug.showOpenDialog(null);
		if (n == JFileChooser.APPROVE_OPTION) {
			if (loadPlug.getSelectedFile().getName().endsWith(".jar")) {
				String path = loadPlug.getSelectedFile().toString();
				String type = path.substring(path.lastIndexOf("\\") + 1, path.lastIndexOf("."));
				DynamicClassLoader dynamicLoading = new DynamicClassLoader(path, type);
				Class<?> classloaded = dynamicLoading.execute();
				engine.setSupportedShapes(classloaded);
			} else {
				JOptionPane.showMessageDialog(null, "File Extension Invalid ! Please enter a valid File Extension");
			}
		}
	}

	public void changfillcolor(Color c) {
		data = engine.Data();
		if (shapeAction == -1)
			throw new RuntimeException();
		Shape x = data.get(shapeAction);
		x.setFillColor(c);
		setFillColor(c);
		shapeAction = -1;
		repaint();
	}

	public void changoutcolor(Color c) {
		data = engine.Data();
		if (shapeAction == -1)
			throw new RuntimeException();
		Shape x = data.get(shapeAction);
		x.setColor(c);
		setOutColor(c);
		shapeAction = -1;
		repaint();
	}

	public void deleteShape() {
		data = engine.Data();
		if (manySel == 11) {
			for (int i = 0; i < shapeActions.size(); i++) {
				Shape y = data.get(i);

				engine.removeShape(y);
			}
			manySel = 0;
			for (int i = 0; i < shapeActions.size(); i++) {
				shapeActions.remove(i);
			}
		}
		if (shapeAction == -1)
			throw new RuntimeException();
		Shape x = data.get(shapeAction);
		engine.removeShape(x);
		shapeAction = -1;
		repaint();
	}

	public void copyShape() throws CloneNotSupportedException {
		if (shapeAction == -1)
			throw new RuntimeException();
		Shape x = data.get(shapeAction);
		Shape y = (Shape) x.clone();
		Point p = new Point();
		p.setLocation(z1, z2);
		y.setPosition(p);
		engine.addShape(y);
		shapeAction = -1;
		repaint();
	}

	public void setManysel() {
		manySel = 11;
	}

	public void setDrawMode(int x) {
		selected = x;
		shapeAction = -1;
	}

	public int getDrawMode() {
		return selected;
	}

	public void undo() {
		engine.undo();
		repaint();
	}

	public void redo() {
		engine.redo();
		repaint();
	}

	public void setOutColor(Color c) {
		outc = c;
	}

	public Color getoutColor() {
		return outc;
	}

	public void setFillColor(Color c) {
		fillc = c;
	}

	public Color getFillColor() {
		return fillc;
	}

	public BufferedImage getImg() {
		return curGrid;
	}

	public void resizeShape(Shape s, double x, double y) {
		if (sh.getProperties().containsKey("rectwidth")) {

			final Map<String, Double> newprop = new HashMap<>();
			newprop.put("rectwidth", x - sh.getPosition().getX());
			newprop.put("rectheight", y - sh.getPosition().getY());
			sh.setProperties(newprop);

		} else if (sh.getProperties().containsKey("radius")) {

			final Map<String, Double> newprop = new HashMap<>();
			newprop.put("radius", x - sh.getPosition().getX());
			newprop.put("radius", y - sh.getPosition().getY());
			sh.setProperties(newprop);			

		} else if (sh.getProperties().containsKey("Width")) {

			final Map<String, Double> newprop = new HashMap<>();
			newprop.put("Width", x - sh.getPosition().getX());
			newprop.put("Length", y - sh.getPosition().getY());
			sh.setProperties(newprop);
			

		} else if (sh.getProperties().containsKey("Side")) {

			final Map<String, Double> newprop = new HashMap<>();
			newprop.put("Side", x - sh.getPosition().getX());
			newprop.put("Side", y - sh.getPosition().getY());
			sh.setProperties(newprop);

		} else if (sh.getProperties().containsKey("w")) {
			Point p = new Point();
			p.setLocation(x, y);
			sh.setPosition(p);
		} else if (sh.getProperties().size() == 6) {
			final Map<String, Double> newprop = new HashMap<>();
			newprop.put("x3", x - sh.getPosition().getX());
			newprop.put("y3", y - sh.getPosition().getY());
			newprop.put("x1", sh.getProperties().get("x1"));
			newprop.put("y1", sh.getProperties().get("y1"));
			newprop.put("x2", sh.getProperties().get("x2"));
			newprop.put("y2", sh.getProperties().get("y2"));
			sh.setProperties(newprop);

		}

	}

}
