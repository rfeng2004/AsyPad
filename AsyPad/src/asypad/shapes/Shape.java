package asypad.shapes;

import java.text.DecimalFormat;
import java.util.ArrayList;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import asypad.shapes.labels.DraggableLabel;
import asypad.shapes.types.SHAPE_TYPE;
import asypad.ui.AsyPadPane;
/**
 * Custom Shape class that is a superclass for all shapes drawn on AsyPad.
 * @author Raymond Feng
 */
public abstract class Shape
{
	/**
	 * Stroke width of shapes.
	 */
	public static double StrokeWidth = 3;

	/**
	 * Infinite that is used for drawing lines that should extend to infinity.
	 */
	public static final double INF = 10000;

	/**
	 * The formatter used to round to 2 decimal places for file i/o.
	 */
	public static final DecimalFormat FORMATTER = new DecimalFormat("#0.00");

	/**
	 * String used to separate dependencies in a shape's name;
	 */
	protected static final String SEPARATOR = "_";

	/**
	 * The shapes that this shape depends on.
	 */
	protected ArrayList<Shape> dependencies;

	/**
	 * The shapes that depend on this shape.
	 */
	protected ArrayList<Shape> children;

	/**
	 * If this shape should be removed.
	 */
	protected boolean remove;

	/**
	 * If this shape should be hidden.
	 */
	protected boolean hide;

	/**
	 * If this shape's code should be added to the generated Asymptote file.
	 */
	protected boolean inAsyCode;

	/**
	 * The shape's label.
	 */
	protected DraggableLabel label;

	/**
	 * The type of this shape.
	 */
	protected SHAPE_TYPE type;

	/**
	 * The dependency level of this shape.
	 */
	protected int level;
	
	/**
	 * The color of the shape.
	 */
	protected Color color;

	/**
	 * Builds a Shape from the arguments.
	 * @param args arguments specifying the shape
	 * @param target target AsyPadPane that the Shape will be drawn in
	 * @return the Shape that was built
	 */
	public static Shape buildShape(String args, AsyPadPane target)
	{
		Shape s = null;
		if(args.substring(0, 5).equals("POINT"))
		{
			if(args.substring(14, 19).equals("POINT") && args.charAt(19) != '_')
			{
				int xi = args.indexOf('=', 26), yi = args.indexOf('=', xi+1);
				String name = args.substring(26, xi-3);
				String xs = args.substring(xi+2, yi-3);
				String ys = args.substring(yi+2, args.length());
				double x = Double.parseDouble(xs);
				double y = Double.parseDouble(ys);
				s = new Point(x, y, name);
			}
			else if(args.substring(14, 28).equals("POINT_ON_SHAPE"))
			{
				int d = args.lastIndexOf(':'), rl = args.lastIndexOf('=');
				String name = args.substring(35, d-11);
				String dependencyName = args.substring(d+2, rl-18);
				String rls = args.substring(rl+2, args.length());
				double relativeLocation = Double.parseDouble(rls);
				s = new Point(target.findShapeByName(dependencyName), relativeLocation, name);
			}
			else if(args.substring(14, 32).equals("INTERSECTION_POINT"))
			{
				int d1 = args.lastIndexOf(':'), d2 = args.lastIndexOf(','), id = args.indexOf('=', d2+1);
				String name = args.substring(39, d1-13);
				String d1Name = args.substring(d1+2, d2);
				String d2Name;
				if(id != -1)
				{
					d2Name = args.substring(d2+2, id-12);
				}
				else d2Name = args.substring(d2+2, args.length());
				Shape dependency1 = target.findShapeByName(d1Name);
				Shape dependency2 = target.findShapeByName(d2Name);
				if(dependency1 instanceof Line && dependency2 instanceof Line)
				{
					s = new Point((Line) dependency1, (Line) dependency2, name);
				}
				else if(dependency1 instanceof Line && dependency2 instanceof Circle)
				{
					boolean identifier = Boolean.parseBoolean(args.substring(id+2, args.length()));
					s = new Point((Line) dependency1, (Circle) dependency2, identifier, name);
				}
				else if(dependency1 instanceof Circle && dependency2 instanceof Circle)
				{
					boolean identifier = Boolean.parseBoolean(args.substring(id+2, args.length()));
					s = new Point((Circle) dependency1, (Circle) dependency2, identifier, name);
				}
			}
			else if(args.substring(14, 22).equals("MIDPOINT"))
			{
				int d1 = args.lastIndexOf(':'), d2 = args.lastIndexOf(',');
				String name = args.substring(29, d1-13);
				String d1Name = args.substring(d1+2, d2);
				String d2Name = args.substring(d2+2, args.length());
				Point dependency1 = (Point) target.findShapeByName(d1Name);
				Point dependency2 = (Point) target.findShapeByName(d2Name);
				s = new Point(dependency1, dependency2, name);
			}
		}
		else if(args.substring(0, 4).equals("LINE"))
		{
			if(args.substring(13, 20).equals("SEGMENT"))
			{
				int d1 = args.lastIndexOf(':'), d2 = args.lastIndexOf(',');
				String d1Name = args.substring(d1+2, d2);
				String d2Name = args.substring(d2+2, args.length());
				Point dependency1 = (Point) target.findShapeByName(d1Name);
				Point dependency2 = (Point) target.findShapeByName(d2Name);
				s = new Line(dependency1, dependency2, true);
			}
			else if(args.substring(13, 17).equals("LINE"))
			{
				int d1 = args.lastIndexOf(':'), d2 = args.lastIndexOf(',');
				String d1Name = args.substring(d1+2, d2);
				String d2Name = args.substring(d2+2, args.length());
				Point dependency1 = (Point) target.findShapeByName(d1Name);
				Point dependency2 = (Point) target.findShapeByName(d2Name);
				s = new Line(dependency1, dependency2, false);
			}
			else if(args.substring(13, 26).equals("PARALLEL_LINE"))
			{
				int d1 = args.lastIndexOf(':'), d2 = args.lastIndexOf(',');
				String d1Name = args.substring(d1+2, d2);
				String d2Name = args.substring(d2+2, args.length());
				Point dependency1 = (Point) target.findShapeByName(d1Name);
				Line dependency2 = (Line) target.findShapeByName(d2Name);
				s = new Line(dependency1, dependency2, true);
			}
			else if(args.substring(13, 31).equals("PERPENDICULAR_LINE"))
			{
				int d1 = args.lastIndexOf(':'), d2 = args.lastIndexOf(',');
				String d1Name = args.substring(d1+2, d2);
				String d2Name = args.substring(d2+2, args.length());
				Point dependency1 = (Point) target.findShapeByName(d1Name);
				Line dependency2 = (Line) target.findShapeByName(d2Name);
				s = new Line(dependency1, dependency2, false);
			}
			else if(args.substring(13, 27).equals("ANGLE_BISECTOR"))
			{
				int d1 = args.lastIndexOf(':'), d2 = args.indexOf(',', d1+1), d3 = args.indexOf(',', d2+1);
				String d1Name = args.substring(d1+2, d2);
				String d2Name = args.substring(d2+2, d3);
				String d3Name = args.substring(d3+2, args.length());
				Point dependency1 = (Point) target.findShapeByName(d1Name);
				Point dependency2 = (Point) target.findShapeByName(d2Name);
				Point dependency3 = (Point) target.findShapeByName(d3Name);
				s = new Line(dependency1, dependency2, dependency3);
			}
			else if(args.substring(13, 35).equals("PERPENDICULAR_BISECTOR"))
			{
				int d1 = args.lastIndexOf(':'), d2 = args.lastIndexOf(',');
				String d1Name = args.substring(d1+2, d2);
				String d2Name = args.substring(d2+2, args.length());
				Point dependency1 = (Point) target.findShapeByName(d1Name);
				Point dependency2 = (Point) target.findShapeByName(d2Name);
				s = new Line(dependency1, dependency2);
			}
			else if(args.substring(13, 25).equals("TANGENT_LINE"))
			{
				int d1 = args.lastIndexOf(':'), d2 = args.lastIndexOf(','), id = args.lastIndexOf('=');
				String d1Name = args.substring(d1+2, d2);
				String d2Name = args.substring(d2+2, id-12);
				boolean identifier = Boolean.parseBoolean(args.substring(id+2, args.length()));
				Point dependency1 = (Point) target.findShapeByName(d1Name);
				Circle dependency2 = (Circle) target.findShapeByName(d2Name);
				s = new Line(dependency1, dependency2, identifier);
			}
		}
		else if(args.substring(0, 6).equals("CIRCLE"))
		{
			if(args.substring(15, 21).equals("CIRCLE"))
			{
				int d1 = args.lastIndexOf(':'), d2 = args.lastIndexOf(',');
				String d1Name = args.substring(d1+2, d2);
				String d2Name = args.substring(d2+2, args.length());
				Point dependency1 = (Point) target.findShapeByName(d1Name);
				Point dependency2 = (Point) target.findShapeByName(d2Name);
				s = new Circle(dependency1, dependency2);
			}
			else if(args.substring(15, 27).equals("CIRCUMCIRCLE"))
			{
				int d1 = args.lastIndexOf(':'), d2 = args.indexOf(',', d1+1), d3 = args.indexOf(',', d2+1);
				String d1Name = args.substring(d1+2, d2);
				String d2Name = args.substring(d2+2, d3);
				String d3Name = args.substring(d3+2, args.length());
				Point dependency1 = (Point) target.findShapeByName(d1Name);
				Point dependency2 = (Point) target.findShapeByName(d2Name);
				Point dependency3 = (Point) target.findShapeByName(d3Name);
				s = new Circle(dependency1, dependency2, dependency3, true);
			}
			else if(args.substring(15, 23).equals("INCIRCLE"))
			{
				int d1 = args.lastIndexOf(':'), d2 = args.indexOf(',', d1+1), d3 = args.indexOf(',', d2+1);
				String d1Name = args.substring(d1+2, d2);
				String d2Name = args.substring(d2+2, d3);
				String d3Name = args.substring(d3+2, args.length());
				Point dependency1 = (Point) target.findShapeByName(d1Name);
				Point dependency2 = (Point) target.findShapeByName(d2Name);
				Point dependency3 = (Point) target.findShapeByName(d3Name);
				s = new Circle(dependency1, dependency2, dependency3, false);
			}
			/*else if(args.substring(15, 29).equals("TANGENT_CIRCLE"))
			{
				int d1 = args.lastIndexOf(':'), d2 = args.indexOf(',', d1+1), d3 = args.indexOf(',', d2+1), id = args.lastIndexOf('=');
				String d1Name = args.substring(d1 + 2, d2);
				String d2Name = args.substring(d2 + 2, d3);
				String d3Name = args.substring(d3 + 2, id - 12);
				boolean identifier = Boolean.parseBoolean(args.substring(id + 2, args.length()));
				Circle dependency1 = (Circle) target.findShapeByName(d1Name);
				Circle dependency2 = (Circle) target.findShapeByName(d2Name);
				Point dependency3 = (Point) target.findShapeByName(d3Name);
				s = new Circle(dependency1, dependency2, dependency3, identifier);
			}*/
		}
		return s;
	}

	/**
	 * Default superclass constructor called by all shapes, initializes dependencies, children, and level.
	 * @param shapes dependencies
	 */
	public Shape(Shape... shapes)
	{
		dependencies = new ArrayList<Shape>();
		children = new ArrayList<Shape>();
		label = new DraggableLabel(this, -Math.PI/4); //default direction SE
		level = -1;
		for(Shape s : shapes)
		{
			dependencies.add(s);
			s.getChildren().add(this);
			level = Math.max(level, s.getLevel());
		}
		level++;
		remove = false;
		hide = false;
		inAsyCode = true;
		color = Color.BLACK;
	}

	/**
	 * Dependencies of this shape.
	 * @return dependencies
	 */
	public ArrayList<Shape> getDependencies()
	{
		return dependencies;
	}
	/**
	 * Children of this shape.
	 * @return children
	 */
	public ArrayList<Shape> getChildren()
	{
		return children;
	}

	/**
	 * Sets remove = true for this shape and all children.
	 */
	public void delete()
	{
		remove = true;
		for(Shape s : children)
		{
			s.delete();
		}
	}

	/**
	 * Sets the value of remove for this shape.
	 * @param remove new value of remove
	 */
	public void setRemove(boolean remove)
	{
		this.remove = remove;
	}

	/**
	 * Adds this shape and all children to Asymptote code.
	 */
	public void addToAsy()
	{
		inAsyCode = true;
		for(Shape s : children)
		{
			s.addToAsy();
		}
	}

	/**
	 * Removes this shape and all children from Asymptote code.
	 */
	public void removeFromAsy()
	{
		inAsyCode = false;
		for(Shape s : children)
		{
			s.removeFromAsy();
		}
	}

	/**
	 * Sets whether the shape should be hidden.
	 * @param hidden if the shape should be hidden
	 */
	public void setHidden(boolean hidden)
	{
		hide = hidden;
	}

	/**
	 * If this shape should be removed.
	 * @return remove
	 */
	public boolean remove()
	{
		return remove;
	}

	/**
	 * If this shape should be hidden.
	 * @return hide
	 */
	public boolean isHidden()
	{
		return hide;
	}

	/**
	 * If this shape is in the Asymptote code.
	 * @return inAsyCode
	 */
	public boolean isInAsyCode()
	{
		return inAsyCode;
	}

	/**
	 * Renames the shape.
	 * @param name new name
	 */
	public void setName(String name)
	{
		label.setText(name);
	}
	
	/**
	 * Sets the color of the shape
	 * @param c new color of shape
	 */
	public void setColor(Color c)
	{
		color = c;
		refresh();
	}
	
	/**
	 * Returns the color of the shape.
	 * @return color of shape
	 */
	public Color getColor()
	{
		return color;
	}

	/**
	 * Returns the name of the shape.
	 * @return the name of the shape
	 */
	public String getName()
	{
		return label.getText();
	}

	/**
	 * Returns the shapes's label.
	 * @return the shape label
	 */
	public DraggableLabel getLabel()
	{
		return label;
	}

	/**
	 * Returns this shape's type.
	 * @return type
	 */
	public SHAPE_TYPE getType()
	{
		return type;
	}

	/**
	 * Returns this shape's level of dependency.
	 * @return level of dependency
	 */
	public int getLevel()
	{
		return level;
	}

	/**
	 * Draws this shape onto the specified pane.
	 * @param p pane that shape is to be drawn on
	 */
	public abstract void draw(Pane p);

	/**
	 * Refreshes shape and all of its children.
	 */
	public abstract void refresh();

	/**
	 * Refreshes the name of this shape and all of its children.
	 */
	public abstract void refreshName();

	/**
	 * Returns the underlying shape that is drawn onto the screen.
	 * @return the underlying shape
	 */
	public abstract javafx.scene.shape.Shape getObject();

	/**
	 * String representation of the shape. Is used in .apad file i/o.
	 * @return string representation
	 */
	public abstract String toString();

	/**
	 * Converts this shape into Asymptote code.
	 * @return Asymptote representation.
	 */
	public abstract String toAsymptote();
}
