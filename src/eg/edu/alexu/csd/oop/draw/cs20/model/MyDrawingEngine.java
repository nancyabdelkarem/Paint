package eg.edu.alexu.csd.oop.draw.cs20.model;

import java.awt.Color;


import java.io.FileReader;
import java.awt.Graphics;
import java.awt.Point;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import eg.edu.alexu.csd.oop.draw.DrawingEngine;
import eg.edu.alexu.csd.oop.draw.Shape;
import eg.edu.alexu.csd.oop.draw.cs20.model.shapes.Circle;
import eg.edu.alexu.csd.oop.draw.cs20.model.shapes.Ellipse;
import eg.edu.alexu.csd.oop.draw.cs20.model.shapes.LineSegment;
import eg.edu.alexu.csd.oop.draw.cs20.model.shapes.Rectangle;
import eg.edu.alexu.csd.oop.draw.cs20.model.shapes.Square;
import eg.edu.alexu.csd.oop.draw.cs20.model.shapes.Triangle;

public class MyDrawingEngine implements DrawingEngine {
	private Stack<ArrayList<Shape>> shapes = new Stack<ArrayList<Shape>>();
	private Stack<ArrayList<Shape>> urshapes = new Stack<ArrayList<Shape>>();
	private Stack<ArrayList<Shape>> loadedshapes = new Stack<ArrayList<Shape>>();
	private DocumentBuilderFactory documentBuilderFactory;
	private DocumentBuilder documentBuilder;
	private ArrayList<Shape> loadedArrayList;
	private Stack<ArrayList<Shape>> loadedStack;
	private File file;
	private List<Class<? extends Shape>> support = new ArrayList<>();
	private Color localFillColor;
	private Color localBordercolor;
	private Shape loadedShape;
	boolean loadCheck = false;

	public MyDrawingEngine() {
		shapes.push(new ArrayList<Shape>());
	}

	@Override
	public void refresh(Graphics canvas) {
		for (Shape sh : shapes.peek()) {
			sh.draw(canvas);
		}
	}

	@Override
	public void addShape(Shape shape) {
		if (shapes.size() <= 20) {
			shapes.push(new ArrayList<Shape>(shapes.peek()));
		} else {
			shapes.remove(0);
			shapes.push(new ArrayList<Shape>(shapes.peek()));
		}
		shapes.peek().add(shape);
	}

	@Override
	public void removeShape(Shape shape) {
		if (shapes.size() <= 20) {
			shapes.push(new ArrayList<Shape>(shapes.peek()));
		} else {
			shapes.remove(0);
			shapes.push(new ArrayList<Shape>(shapes.peek()));
		}
		shapes.peek().remove(shape);

	}

	@Override
	public void updateShape(Shape oldShape, Shape newShape) {
		if (shapes.size() <= 20) {
			shapes.push(new ArrayList<Shape>(shapes.peek()));
		} else {
			shapes.remove(0);
			shapes.push(new ArrayList<Shape>(shapes.peek()));
		}
		int index = shapes.peek().indexOf(oldShape);
		shapes.peek().set(index, newShape);

	}

	public ArrayList<Shape> Data() {
		return shapes.peek();
	}

	@Override
	public Shape[] getShapes() {
		return shapes.peek().toArray(new Shape[0]);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<Class<? extends Shape>> getSupportedShapes() {
		Class<? extends Shape> shape;
		try {
			shape = (Class) Class.forName("eg.edu.alexu.csd.oop.draw.cs20.model.shapes.Circle");
			support.add(shape);
			shape = (Class) Class.forName("eg.edu.alexu.csd.oop.draw.cs20.model.shapes.Ellipse");
			support.add(shape);
			shape = (Class) Class.forName("eg.edu.alexu.csd.oop.draw.cs20.model.shapes.LineSegment");
			support.add(shape);
			shape = (Class) Class.forName("eg.edu.alexu.csd.oop.draw.cs20.model.shapes.Triangle");
			support.add(shape);
			shape = (Class) Class.forName("eg.edu.alexu.csd.oop.draw.cs20.model.shapes.Rectangle");
			support.add(shape);
			shape = (Class) Class.forName("eg.edu.alexu.csd.oop.draw.cs20.model.shapes.Square");
			support.add(shape);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return support;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void setSupportedShapes(Class value) {
		support.add(value);
	}

	@Override
	public void undo() {
		if (shapes.size() > 1 && loadCheck == false) {
			urshapes.push(shapes.pop());
		}
	}

	@Override
	public void redo() {
		if (!urshapes.empty()) {
			shapes.push(urshapes.pop());
		}
	}

	@Override
	public void save(String path) {
		if (path.toLowerCase().contains(".xml")) {
			SaveXML(shapes, path);
		} else if (path.toUpperCase().contains(".JSON")) {
			jsonsave(path);
		}
	}

	@SuppressWarnings({ "unchecked" })
	public void jsonsave(String path) {
		final JSONArray list = new JSONArray();
		for (final Shape sh : shapes.peek()) {
			final JSONObject obj = new JSONObject();
			if (sh.getProperties() != null) {
				final String json = sh.getProperties().toString();
				obj.put("properities", json);
			}
			if (sh.getPosition() != null) {
				final JSONArray position = new JSONArray();
				position.add((sh.getPosition().getX()));
				position.add((sh.getPosition().getY()));
				obj.put("position point", position);

			}
			if (sh.getColor() != null) {
				final Color awtColor = sh.getColor();

				final int r = awtColor.getRed();
				final int g = awtColor.getGreen();
				final int b = awtColor.getBlue();
				final JSONArray color = new JSONArray();
				color.add(r);
				color.add(g);
				color.add(b);
				obj.put("color", color);
			}
			if (sh.getFillColor() != null) {
				final Color awtColor1 = sh.getFillColor();
				final int r1 = awtColor1.getRed();
				final int g1 = awtColor1.getGreen();
				final int b1 = awtColor1.getBlue();
				final JSONArray fillcolor = new JSONArray();
				fillcolor.add(r1);
				fillcolor.add(g1);
				fillcolor.add(b1);

				obj.put("fillcolor", fillcolor);
			}
			obj.put("type", sh.toString());

			list.add(obj);

		}

		try (FileWriter file = new FileWriter(path)) {
			file.write(list.toJSONString());
		} catch (final IOException e) {

			e.printStackTrace();
		}

	}

	public void SaveXML(Stack<ArrayList<Shape>> shapesOnPanel, String filePath) {
		Document dom;
		documentBuilderFactory = DocumentBuilderFactory.newInstance();
		try {
			documentBuilder = documentBuilderFactory.newDocumentBuilder();
			dom = documentBuilder.newDocument();
			Element rootElement = dom.createElement("Stackshapes");
			dom.appendChild(rootElement);
			for (int i = 1; i < shapesOnPanel.size(); i++) {
				Element arrshape = dom.createElement("arrshape");
				rootElement.appendChild(arrshape);

				arrshape.setAttribute("id", String.valueOf(i));

				ArrayList<Shape> shapes = new ArrayList<Shape>();
				shapes = shapesOnPanel.get(i);

				for (int j = 0; j < shapes.size(); j++) {
					Element shape = dom.createElement("shape");
					shape.setAttribute("type", shapes.get(j).getClass().getSimpleName());
					arrshape.appendChild(shape);

					if (shapes.get(j).getPosition() != null) {
						Element positionx = dom.createElement("positionX");
						positionx.appendChild(dom.createTextNode(String.valueOf(shapes.get(j).getPosition().x)));
						shape.appendChild(positionx);
						Element positiony = dom.createElement("positionY");
						positiony.appendChild(dom.createTextNode(String.valueOf(shapes.get(j).getPosition().y)));
						shape.appendChild(positiony);

					}

					if (shapes.get(j).getProperties() != null) {
						Element Properties = dom.createElement("Properties");
						Properties.appendChild(dom.createTextNode(String.valueOf(shapes.get(j).getProperties())));
						shape.appendChild(Properties);
					}

					if (shapes.get(j).getColor() != null) {
						Element borderColor = dom.createElement("borderColor");
						borderColor.appendChild(dom.createTextNode(String.valueOf(shapes.get(j).getColor().getRGB())));
						shape.appendChild(borderColor);
					}

					if (shapes.get(j).getFillColor() != null) {
						Element fillColor = dom.createElement("fillColor");
						fillColor
								.appendChild(dom.createTextNode(String.valueOf(shapes.get(j).getFillColor().getRGB())));
						shape.appendChild(fillColor);
					}
				}
			}
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(dom);
			StreamResult result = new StreamResult(new File(filePath));
			transformer.transform(source, result);
		} catch (ParserConfigurationException pce) {
			pce.printStackTrace();
		} catch (TransformerException tfe) {
			tfe.printStackTrace();
		}
	}

	@Override
	public void load(String path) {
		loadCheck = true;
		if (path.toLowerCase().contains(".xml")) {
			loadedshapes = LoadXML(path);
			shapes.clear();
			shapes = loadedshapes;
		} else if (path.toUpperCase().contains(".JSON")) {
			try {
				jsonload(path);
			} catch (org.json.simple.parser.ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}

	public void jsonload(String path) throws org.json.simple.parser.ParseException {
		shapes.clear();

		shapes.push(new ArrayList<Shape>());

		final JSONParser parser = new JSONParser();
		JSONArray jsonArray = null;
		try {
			jsonArray = (JSONArray) JSONParser.parse(new FileReader(path));
		} catch (final FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (final IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		for (final Object o : jsonArray) {
			final JSONObject obj = (JSONObject) o;
			Shape shape = new Shapes();
			final String s = (String) obj.get("type");

			if (s.contains(".Square")) {
				shape = new Square();
				shapes.peek().add(shape);

			} else if (s.contains(".LineSegment")) {
				shape = new LineSegment();
				shapes.peek().add(shape);
				addShape(shape);
			} else if (s.contains(".Triangle")) {
				shape = new Triangle();
				shapes.peek().add(shape); // addShape(shape);

			}

			else if (s.contains(".Rectangle")) {
				shape = new Rectangle();
				shapes.peek().add(shape);
				addShape(shape);
			} else if (s.contains(".Ellipse")) {
				shape = new Ellipse();
				shapes.peek().add(shape);
				addShape(shape);

			} else if (s.contains(".Circle")) {
				shape = new Circle();
				shapes.peek().add(shape);
				addShape(shape);

			}

			final JSONArray position = (JSONArray) obj.get("position point");
			if (position != null) {
				final double[] point = new double[2];
				int i = 0;
				for (final Object p : position) {
					point[i] = Double.parseDouble(p.toString());
					i++;
				}
				final Point p = new Point();
				p.x = (int) point[0];
				p.y = (int) point[1];
				shape.setPosition(p);
			}

			final JSONArray colors = (JSONArray) obj.get("color");
			if (colors != null) {
				final int[] col = new int[3];
				int z = 0;
				for (final Object c : colors) {
					col[z] = Integer.parseInt(c.toString());
					z++;
				}
				shape.setColor(new Color(col[0], col[1], col[2]));
			}
			final JSONArray fcolors = (JSONArray) obj.get("fillcolor");
			if (fcolors != null) {
				final int[] fcol = new int[3];
				int y = 0;
				for (final Object fc : fcolors) {
					fcol[y] = Integer.parseInt(fc.toString());
					y++;
				}

				shape.setFillColor(new Color(fcol[0], fcol[1], fcol[2]));
			}

			String properities = (String) obj.get("properities");
			if (properities != null) {

				properities = properities.replace('{', ' ');
				properities = properities.replace('}', ' ');

				final String[] tokens = properities.split(",");
				final Map<String, Double> map = new HashMap<>();

				for (int x = 0; x < tokens.length; x++) {

					final String[] strings = tokens[x].split("=");
					if (strings.length == 2) {
						strings[0] = strings[0].replaceAll("\\s+", "");
						map.put(strings[0], Double.parseDouble(strings[1]));
					}
				}
				shape.setProperties(map);
			}

			shapes.peek().add(shape);
		}

	}

	public Stack<ArrayList<Shape>> LoadXML(String path) {
		loadedArrayList = new ArrayList<>();
		loadedStack = new Stack<ArrayList<Shape>>();
		file = new File(path);
		try {
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document dom = dBuilder.parse(file);

			NodeList nList = dom.getElementsByTagName("arrshape");
			for (int i = 0; i < nList.getLength(); i++) {
				Node nNode = nList.item(i);
				if (nNode.getNodeType() == Node.ELEMENT_NODE) {
					Element nElement = (Element) nNode;
					NodeList mList = nElement.getElementsByTagName("shape");
					for (int j = 0; j < mList.getLength(); j++) {
						Node mNode = mList.item(j);
						if (mNode.getNodeType() == Node.ELEMENT_NODE) {
							Element mElement = (Element) mNode;
							String Type = mElement.getAttribute("type");
							if (Type.equals("Circle")) {
								loadedShape = new Circle();
							} else if (Type.equals("Ellipse")) {
								loadedShape = new Ellipse();
							} else if (Type.equals("Rectangle")) {
								loadedShape = new Rectangle();
							} else if (Type.equals("Square")) {
								loadedShape = new Square();
							} else if (Type.equals("Triangle")) {
								loadedShape = new Triangle();
							} else if (Type.equals("LineSegment")) {
								loadedShape = new LineSegment();
							}
							if (mElement.getElementsByTagName("positionX").item(0) != null
									&& mElement.getElementsByTagName("positionY").item(0) != null) {
								Point point = new Point();
								point.x = Integer
										.parseInt(mElement.getElementsByTagName("positionX").item(0).getTextContent());
								point.y = Integer
										.parseInt(mElement.getElementsByTagName("positionY").item(0).getTextContent());
								loadedShape.setPosition(point);
							}
							if (mElement.getElementsByTagName("Properties").item(0) != null) {
								String properities = mElement.getElementsByTagName("Properties").item(0)
										.getTextContent();
								properities = properities.replace('{', ' ');
								properities = properities.replace('}', ' ');

								final String[] tokens = properities.split(",");
								final Map<String, Double> map = new HashMap<>();

								for (int x = 0; x < tokens.length; x++) {
									final String[] strings = tokens[x].split("=");
									if (strings.length == 2) {
										strings[0] = strings[0].replaceAll("\\s+", "");
										map.put(strings[0], Double.parseDouble(strings[1]));
									}
								}
								loadedShape.setProperties(map);
							}
							if (mElement.getElementsByTagName("fillColor").item(0) != null) {
								localFillColor = new Color(Integer
										.parseInt(mElement.getElementsByTagName("fillColor").item(0).getTextContent()));
								loadedShape.setFillColor(localFillColor);
							}
							if (mElement.getElementsByTagName("borderColor").item(0) != null) {
								localBordercolor = new Color(Integer.parseInt(
										mElement.getElementsByTagName("borderColor").item(0).getTextContent()));
								loadedShape.setColor(localBordercolor);
							}
						}
						if (j == 0)
							loadedArrayList = new ArrayList<>();
						loadedArrayList.add(loadedShape);

					}
				}
				loadedStack.push(loadedArrayList);
			}

		} catch (Exception e) {
			e.printStackTrace();

		}
		return loadedStack;
	}

}
