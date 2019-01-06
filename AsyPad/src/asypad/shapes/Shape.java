package asypad.shapes;

import java.text.DecimalFormat;
import java.util.ArrayList;

import javafx.scene.layout.Pane;
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
	public static final double INF = 2000;
	
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
				int xi = 0, yi = 0;
				for(int i = 26; i < args.length(); i++)
				{
					if(args.charAt(i) == '=')
					{
						if(xi == 0) xi = i;
						else yi = i;
					}
				}
				String name = args.substring(26, xi-3);
				String xs = args.substring(xi+2, yi-3);
				String ys = args.substring(yi+2, args.length());
				double x = Double.parseDouble(xs);
				double y = Double.parseDouble(ys);
				s = new Point(x, y, name);
			}
			else if(args.substring(14, 28).equals("POINT_ON_SHAPE"))
			{
				int d = 0, rl = 0;
				for(int i = 35; i < args.length(); i++)
				{
					if(args.charAt(i) == ':')
					{
						d = i;
					}
					if(args.charAt(i) == '=')
					{
						rl = i;
					}
				}
				String name = args.substring(35, d-11);
				String dependencyName = args.substring(d+2, rl-18);
				String rls = args.substring(rl+2, args.length());
				double relativeLocation = Double.parseDouble(rls);
				s = new Point(target.findShapeByName(dependencyName), relativeLocation, name);
			}
			else if(args.substring(14, 32).equals("INTERSECTION_POINT"))
			{
				int d1 = 0, d2 = 0, id = 0;
				for(int i = 39; i < args.length(); i++)
				{
					if(args.charAt(i) == ':')
					{
						d1 = i;
					}
					if(args.charAt(i) == ',')
					{
						d2 = i;
					}
					if(args.charAt(i) == '=')
					{
						id = i;
					}
				}
				String name = args.substring(39, d1-13);
				String d1Name = args.substring(d1+2, d2);
				String d2Name;
				if(id != 0)
				{
					d2Name = args.substring(d2+2, id-12);
				}
				else d2Name = args.substring(d2+2, args.length());
				Line dependency1 = (Line) target.findShapeByName(d1Name);
				Shape dependency2 = target.findShapeByName(d2Name);
				if(dependency2 instanceof Line)
				{
					s = new Point(dependency1, (Line) dependency2, name);
				}
				else
				{
					boolean identifier = Boolean.parseBoolean(args.substring(id+2, args.length()));
					s = new Point(dependency1, (Circle) dependency2, identifier, name);
				}
			}
			else if(args.substring(14, 22).equals("MIDPOINT"))
			{
				int d1 = 0, d2 = 0;
				for(int i = 39; i < args.length(); i++)
				{
					if(args.charAt(i) == ':')
					{
						d1 = i;
					}
					if(args.charAt(i) == ',')
					{
						d2 = i;
					}
				}
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
				int d1 = 0, d2 = 0;
				for(int i = 20; i < args.length(); i++)
				{
					if(args.charAt(i) == ':')
					{
						d1 = i;
					}
					if(args.charAt(i) == ',')
					{
						d2 = i;
					}
				}
				String d1Name = args.substring(d1+2, d2);
				String d2Name = args.substring(d2+2, args.length());
				Point dependency1 = (Point) target.findShapeByName(d1Name);
				Point dependency2 = (Point) target.findShapeByName(d2Name);
				s = new Line(dependency1, dependency2, true);
			}
			else if(args.substring(13, 17).equals("LINE"))
			{
				int d1 = 0, d2 = 0;
				for(int i = 17; i < args.length(); i++)
				{
					if(args.charAt(i) == ':')
					{
						d1 = i;
					}
					if(args.charAt(i) == ',')
					{
						d2 = i;
					}
				}
				String d1Name = args.substring(d1+2, d2);
				String d2Name = args.substring(d2+2, args.length());
				Point dependency1 = (Point) target.findShapeByName(d1Name);
				Point dependency2 = (Point) target.findShapeByName(d2Name);
				s = new Line(dependency1, dependency2, false);
			}
			else if(args.substring(13, 26).equals("PARALLEL_LINE"))
			{
				int d1 = 0, d2 = 0;
				for(int i = 26; i < args.length(); i++)
				{
					if(args.charAt(i) == ':')
					{
						d1 = i;
					}
					if(args.charAt(i) == ',')
					{
						d2 = i;
					}
				}
				String d1Name = args.substring(d1+2, d2);
				String d2Name = args.substring(d2+2, args.length());
				Point dependency1 = (Point) target.findShapeByName(d1Name);
				Line dependency2 = (Line) target.findShapeByName(d2Name);
				s = new Line(dependency1, dependency2, true);
			}
			else if(args.substring(13, 31).equals("PERPENDICULAR_LINE"))
			{
				int d1 = 0, d2 = 0;
				for(int i = 31; i < args.length(); i++)
				{
					if(args.charAt(i) == ':')
					{
						d1 = i;
					}
					if(args.charAt(i) == ',')
					{
						d2 = i;
					}
				}
				String d1Name = args.substring(d1+2, d2);
				String d2Name = args.substring(d2+2, args.length());
				Point dependency1 = (Point) target.findShapeByName(d1Name);
				Line dependency2 = (Line) target.findShapeByName(d2Name);
				s = new Line(dependency1, dependency2, false);
			}
			else if(args.substring(13, 27).equals("ANGLE_BISECTOR"))
			{
				int d1 = 0, d2 = 0, d3 = 0;
				for(int i = 20; i < args.length(); i++)
				{
					if(args.charAt(i) == ':')
					{
						d1 = i;
					}
					if(args.charAt(i) == ',')
					{
						if(d2 == 0) d2 = i;
						else d3 = i;
					}
				}
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
				int d1 = 0, d2 = 0;
				for(int i = 35; i < args.length(); i++)
				{
					if(args.charAt(i) == ':')
					{
						d1 = i;
					}
					if(args.charAt(i) == ',')
					{
						d2 = i;
					}
				}
				String d1Name = args.substring(d1+2, d2);
				String d2Name = args.substring(d2+2, args.length());
				Point dependency1 = (Point) target.findShapeByName(d1Name);
				Point dependency2 = (Point) target.findShapeByName(d2Name);
				s = new Line(dependency1, dependency2);
			}
			else if(args.substring(13, 25).equals("TANGENT_LINE"))
			{
				int d1 = 0, d2 = 0;
				for(int i = 25; i < args.length(); i++)
				{
					if(args.charAt(i) == ':')
					{
						d1 = i;
					}
					if(args.charAt(i) == ',')
					{
						d2 = i;
					}
				}
				String d1Name = args.substring(d1+2, d2);
				String d2Name = args.substring(d2+2, args.length());
				Point dependency1 = (Point) target.findShapeByName(d1Name);
				Point dependency2 = (Point) target.findShapeByName(d2Name);
				s = new Line(dependency1, dependency2);
			}
		}
		else if(args.substring(0, 6).equals("CIRCLE"))
		{
			if(args.substring(15, 21).equals("CIRCLE"))
			{
				int d1 = 0, d2 = 0;
				for(int i = 21; i < args.length(); i++)
				{
					if(args.charAt(i) == ':')
					{
						d1 = i;
					}
					if(args.charAt(i) == ',')
					{
						d2 = i;
					}
				}
				String d1Name = args.substring(d1+2, d2);
				String d2Name = args.substring(d2+2, args.length());
				Point dependency1 = (Point) target.findShapeByName(d1Name);
				Point dependency2 = (Point) target.findShapeByName(d2Name);
				s = new Circle(dependency1, dependency2);
			}
			else if(args.substring(15, 27).equals("CIRCUMCIRCLE"))
			{
				int d1 = 0, d2 = 0, d3 = 0;
				for(int i = 27; i < args.length(); i++)
				{
					if(args.charAt(i) == ':')
					{
						d1 = i;
					}
					if(args.charAt(i) == ',')
					{
						if(d2 == 0) d2 = i;
						else d3 = i;
					}
				}
				String d1Name = args.substring(d1+2, d2);
				String d2Name = args.substring(d2+2, d3);
				String d3Name = args.substring(d3+2, args.length());
				Point dependency1 = (Point) target.findShapeByName(d1Name);
				Point dependency2 = (Point) target.findShapeByName(d2Name);
				Point dependency3 = (Point) target.findShapeByName(d3Name);
				s = new Circle(dependency1, dependency2, dependency3, 1);
			}
			else if(args.substring(15, 23).equals("INCIRCLE"))
			{
				int d1 = 0, d2 = 0, d3 = 0;
				for(int i = 23; i < args.length(); i++)
				{
					if(args.charAt(i) == ':')
					{
						d1 = i;
					}
					if(args.charAt(i) == ',')
					{
						if(d2 == 0) d2 = i;
						else d3 = i;
					}
				}
				String d1Name = args.substring(d1+2, d2);
				String d2Name = args.substring(d2+2, d3);
				String d3Name = args.substring(d3+2, args.length());
				Point dependency1 = (Point) target.findShapeByName(d1Name);
				Point dependency2 = (Point) target.findShapeByName(d2Name);
				Point dependency3 = (Point) target.findShapeByName(d3Name);
				s = new Circle(dependency1, dependency2, dependency3, 2);
			}
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
