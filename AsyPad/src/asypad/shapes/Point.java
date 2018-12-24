package asypad.shapes;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import asypad.shapes.types.POINT_TYPE;

/**
 * Custom Point used for drawing in AsyPad.
 * @author Raymond Feng
 */
public class Point extends Shape
{
	/**
	 * Underlying dot that is drawn onto the screen.
	 */
	private javafx.scene.shape.Circle dot;

	/**
	 * x-coordinate of the point.
	 */
	private double x;

	/**
	 * y-coordinate of the point.
	 */
	private double y;

	/**
	 * Relative location of point relative to shape that it is on, only used for {@code POINT_TYPE.POINT_ON_SHAPE}
	 */
	private double relativeLocation; //used for points that are snapped to another shape.

	/**
	 * Identifier to distinguish between the 2 possible intersections of a point and a circle, 
	 * only used for {@ POINT_TYPE.INTERSECTION_POINT} that depend on a line and a circle.
	 */
	private boolean identifier; //used for intersection of line and circle or 2 circles.

	/**
	 * Constructs new Point at (x, y) with no label.
	 * @param x x-coordinate
	 * @param y y-coordinate
	 */
	public Point(double x, double y)
	{
		this(x, y, "");
	}

	/**
	 * Creates a new point that snaps onto the specified shape with no label.
	 * @param cx current x-coordinate
	 * @param cy current y-coordinate
	 * @param snap shape that this point will lie on.
	 */
	public Point(double cx, double cy, Shape snap)
	{
		this(cx, cy, snap, "");
	}

	/**
	 * Constructs an intersection point between lines l1 and l2 with no label.
	 * @param l1 first line
	 * @param l2 second line
	 */
	public Point(Line l1, Line l2)
	{
		this(l1, l2, "");
	}

	/**
	 * Constructs an intersection point between a line and a circle with no label. 
	 * Identifier = true represents the intersection point that is closer to the start of the line.
	 * @param l line
	 * @param c circle
	 * @param identifier which intersection point this will be
	 */
	public Point(Line l, Circle c, boolean identifier)
	{
		this(l, c, identifier, "");
	}

	/**
	 * Constructs the midpoint of 2 points with no label.
	 * @param p1 first point
	 * @param p2 second point
	 */
	public Point(Point p1, Point p2)
	{
		this(p1, p2, "");
	}

	/**
	 * Constructs new Point at (x, y).
	 * @param x x-coordinate
	 * @param y y-coordinate
	 * @param name name of point
	 */
	public Point(double x, double y, String name)
	{
		super();
		this.x = x;
		this.y = y;
		type = POINT_TYPE.POINT;
		relativeLocation = -1;
		identifier = false;
		dot = new javafx.scene.shape.Circle(x, y, StrokeWidth);
		dot.setStroke(Color.BLACK);
		dot.setStrokeWidth(StrokeWidth);
		label.setText(name);
		label.setLayoutX(x+StrokeWidth);
		label.setLayoutY(y+StrokeWidth);
	}

	/**
	 * Creates a new point that snaps onto the specified shape.
	 * @param cx current x-coordinate
	 * @param cy current y-coordinate
	 * @param snap shape that this point will lie on.
	 * @param name name of point
	 */
	public Point(double cx, double cy, Shape snap, String name)
	{
		super(snap);
		type = POINT_TYPE.POINT_ON_SHAPE;
		identifier = false;
		if(snap instanceof Line)
		{
			Line l = (Line) snap;
			x = Utility.footX(cx, cy, l);
			y = Utility.footY(cx, cy, l);
			relativeLocation = (x-l.getStartX())/(l.getEndX()-l.getStartX());
		}
		else if(snap instanceof Circle)
		{
			Circle c = (Circle) snap;
			x = c.getCenterX()+(cx-c.getCenterX())*c.getRadius()/Utility.dist(cx, cy, c.getCenterX(), c.getCenterY());
			y = c.getCenterY()+(cy-c.getCenterY())*c.getRadius()/Utility.dist(cx, cy, c.getCenterX(), c.getCenterY());
			relativeLocation = Math.atan((y-c.getCenterY())/(x-c.getCenterX()));
			if(x < c.getCenterX()) relativeLocation += Math.PI;
		}
		dot = new javafx.scene.shape.Circle(x, y, StrokeWidth);
		dot.setStroke(Color.BLACK);
		dot.setStrokeWidth(StrokeWidth);
		label.setText(name);
		label.setLayoutX(x+StrokeWidth);
		label.setLayoutY(y+StrokeWidth);
	}

	/**
	 * Creates a new point that snaps onto the specified shape.
	 * @param snap shape that this point will lie on
	 * @param relativeLocation relative location of the point on the shape
	 * @param name name of point
	 */
	public Point(Shape snap, double relativeLocation, String name)
	{
		super(snap);
		type = POINT_TYPE.POINT_ON_SHAPE;
		identifier = false;
		this.relativeLocation = relativeLocation;
		if(snap instanceof Line)
		{
			Line l = (Line) snap;
			x = l.getStartX()+relativeLocation*(l.getEndX()-l.getStartX());
			y = l.getStartY()+relativeLocation*(l.getEndY()-l.getStartY());
		}
		else if(snap instanceof Circle)
		{
			Circle c = (Circle) snap;
			x = c.getCenterX()+c.getRadius()*Math.cos(relativeLocation);
			y = c.getCenterY()+c.getRadius()*Math.sin(relativeLocation);
		}
		dot = new javafx.scene.shape.Circle(x, y, StrokeWidth);
		dot.setStroke(Color.BLACK);
		dot.setStrokeWidth(StrokeWidth);
		label.setText(name);
		label.setLayoutX(x+StrokeWidth);
		label.setLayoutY(y+StrokeWidth);
	}

	/**
	 * Constructs an intersection point between lines l1 and l2.
	 * @param l1 first line
	 * @param l2 second line
	 * @param name name of point
	 */
	public Point(Line l1, Line l2, String name)
	{
		super(l1, l2);
		this.x = Utility.intersectX(l1, l2);
		this.y = Utility.intersectY(l1, l2);
		type = POINT_TYPE.INTERSECTION_POINT;
		relativeLocation = -1;
		identifier = false;
		dot = new javafx.scene.shape.Circle(x, y, StrokeWidth);
		dot.setStroke(Color.BLACK);
		dot.setStrokeWidth(StrokeWidth);
		label.setText(name);
		label.setLayoutX(x+StrokeWidth);
		label.setLayoutY(y+StrokeWidth);
	}

	/**
	 * Constructs an intersection point between a line and a circle. Identifier = true represents
	 * the intersection point that is closer to the start of the line.
	 * @param l line
	 * @param c circle
	 * @param identifier which intersection point this will be
	 * @param name
	 */
	public Point(Line l, Circle c, boolean identifier, String name)
	{
		super(l, c);
		type = POINT_TYPE.INTERSECTION_POINT;
		relativeLocation = -1;
		this.identifier = identifier;
		this.x = Utility.intersectX(l, c, identifier);
		this.y = Utility.intersectY(l, c, identifier);
		dot = new javafx.scene.shape.Circle(x, y, StrokeWidth);
		dot.setStroke(Color.BLACK);
		dot.setStrokeWidth(StrokeWidth);
		label.setText(name);
		label.setLayoutX(x+StrokeWidth);
		label.setLayoutY(y+StrokeWidth);
	}

	/**
	 * Constructs the midpoint of 2 points.
	 * @param p1 first point
	 * @param p2 second point
	 * @param name name of point
	 */
	public Point(Point p1, Point p2, String name)
	{
		super(p1, p2);
		this.x = (p1.getX()+p2.getX())/2;
		this.y = (p1.getY()+p2.getY())/2;
		type = POINT_TYPE.MIDPOINT;
		relativeLocation = -1;
		dot = new javafx.scene.shape.Circle(x, y, StrokeWidth);
		dot.setStroke(Color.BLACK);
		dot.setStrokeWidth(StrokeWidth);
		label.setText(name);
		label.setLayoutX(x+StrokeWidth);
		label.setLayoutY(y+StrokeWidth);
	}

	/**
	 * Gets x-coordinate.
	 * @return x-coordinate
	 */
	public double getX()
	{
		return x;
	}

	/**
	 * Sets x-coordinate.
	 * @param x new x-coordinate
	 */
	public void setX(double x)
	{
		this.x = x;
		refresh();
	}

	/**
	 * Gets y-coordinate.
	 * @return y-coordinate
	 */
	public double getY()
	{
		return y;
	}

	/**
	 * Sets y-coordinate.
	 * @param y new y-coordinate
	 */
	public void setY(double y)
	{
		this.y = y;
		refresh();
	}

	/**
	 * Sets the relative location of this point to the shape that it is on
	 * based on the current location of the mouse.
	 * @param cx current x-coordinate
	 * @param cy current y-coordinate
	 */
	public void setRelativeLocation(double cx, double cy)
	{
		if(type != POINT_TYPE.POINT_ON_SHAPE) return;
		if(dependencies.get(0) instanceof Line)
		{
			//set relative location
			Line l = (Line) dependencies.get(0);
			relativeLocation = (Utility.footX(cx, cy, l)-l.getStartX())/(l.getEndX()-l.getStartX());
			if(relativeLocation < 0) relativeLocation = 0;
			if(relativeLocation > 1) relativeLocation = 1;
		}
		else if(dependencies.get(0) instanceof Circle)
		{
			//set relative location
			Circle c = (Circle) dependencies.get(0);
			double x = c.getCenterX()+(cx-c.getCenterX())*c.getRadius()/Utility.dist(cx, cy, c.getCenterX(), c.getCenterY());
			double y = c.getCenterY()+(cy-c.getCenterY())*c.getRadius()/Utility.dist(cx, cy, c.getCenterX(), c.getCenterY());
			relativeLocation = Math.atan((y-c.getCenterY())/(x-c.getCenterX()));
			if(x < c.getCenterX()) relativeLocation += Math.PI;
		}
		refresh();
	}

	public void draw(Pane p)
	{
		p.getChildren().add(dot);
		p.getChildren().add(label);
	}

	public void refresh()
	{
		if(type == POINT_TYPE.POINT_ON_SHAPE)
		{
			Shape snap = dependencies.get(0);
			if(snap instanceof Line)
			{
				Line l = (Line) snap;
				x = l.getStartX()+relativeLocation*(l.getEndX()-l.getStartX());
				y = l.getStartY()+relativeLocation*(l.getEndY()-l.getStartY());
			}
			else if(snap instanceof Circle)
			{
				Circle c = (Circle) snap;
				x = c.getCenterX()+c.getRadius()*Math.cos(relativeLocation);
				y = c.getCenterY()+c.getRadius()*Math.sin(relativeLocation);
			}
		}
		else if(type == POINT_TYPE.INTERSECTION_POINT)
		{
			if(dependencies.get(0) instanceof Line && dependencies.get(1) instanceof Line)
			{
				Line l1 = (Line) dependencies.get(0);
				Line l2 = (Line) dependencies.get(1);
				x = Utility.intersectX(l1, l2);
				y = Utility.intersectY(l1, l2);
			}
			else if(dependencies.get(0) instanceof Line && dependencies.get(1) instanceof Circle)
			{
				Line l = (Line) dependencies.get(0);
				Circle c = (Circle) dependencies.get(1);
				x = Utility.intersectX(l, c, identifier);
				y = Utility.intersectY(l, c, identifier);

			}
		}
		else if(type == POINT_TYPE.MIDPOINT)
		{
			Point p1 = (Point) dependencies.get(0);
			Point p2 = (Point) dependencies.get(1);
			x = (p1.getX()+p2.getX())/2;
			y = (p1.getY()+p2.getY())/2;
		}
		dot.setCenterX(x);
		dot.setCenterY(y);
		dot.setRadius(StrokeWidth);
		dot.setStrokeWidth(StrokeWidth);
		label.setLayoutX(x+StrokeWidth);
		label.setLayoutY(y+StrokeWidth);
		for(Shape s : children)
		{
			s.refresh();
		}
	}

	public void refreshName()
	{
		for(Shape s : children) s.refreshName();
	}

	public javafx.scene.shape.Circle getObject()
	{
		return dot;
	}

	public String toString()
	{
		String s = "";
		if(type == POINT_TYPE.POINT)
		{
			s = "POINT: type = " + type + " name: " + getName() + " x = " + x + " y = " + y;
		}
		else if(type == POINT_TYPE.POINT_ON_SHAPE)
		{
			s = "POINT: type = " + type + " name: " + getName() + " dependency: " + dependencies.get(0).getName() 
					+ " relativeLocation = " + relativeLocation;
		}
		else if(type == POINT_TYPE.INTERSECTION_POINT || type == POINT_TYPE.MIDPOINT)
		{
			if(type == POINT_TYPE.INTERSECTION_POINT && dependencies.get(1) instanceof Circle)
			{
				s = "POINT: type = " + type + " name: " + getName() + " dependencies: " + dependencies.get(0).getName()
						+ ", " + dependencies.get(1).getName() + " identifier = " + identifier;
			}
			else
			{
				s = "POINT: type = " + type + " name: " + getName() + " dependencies: " + dependencies.get(0).getName()
						+ ", " + dependencies.get(1).getName();
			}
		}
		return s;
	}

	public String toAsymptote()
	{
		if(!inAsyCode) return "";
		String n = getName();
		String n2 = getName();
		if(n.endsWith("'"))
		{
			n = n.charAt(0)+"prime";
		}
		if(type == POINT_TYPE.POINT)
		{
			String s = "pair " + n + " = (" + FORMATTER.format(x/100) + ", " + FORMATTER.format((INF-y)/100) + "); ";
			if(!hide) s += "dot(" + n + "); label(\"$" + n2 + "$\", " + n + ", SE);\n";
			else s+="\n";
			return s;
		}
		else if(type == POINT_TYPE.INTERSECTION_POINT)
		{
			if(dependencies.get(1) instanceof Line)
			{
				String l1 = dependencies.get(0).getName();
				String l2 = dependencies.get(1).getName();
				String s = "pair " + n + " = intersectionpoint(" + l1 + ", " + l2 + "); ";
				if(!hide) s += "dot(" + n + "); label(\"$" + n2 + "$\", " + n + ", SE);\n";
				else s+="\n";
				return s;
			}
			else if(dependencies.get(1) instanceof Circle)
			{
				String l = dependencies.get(0).getName();
				String c = dependencies.get(1).getName();
				int a = 1;
				if(identifier) a = 0;
				String s = "pair " + n + " = intersectionpoints(" + l + ", " + c + ")[" + a + "]; ";
				if(!hide) s += "dot(" + n + "); label(\"$" + n2 + "$\", " + n + ", SE);\n";
				else s+="\n";
				return s;
			}
		}
		else if(type == POINT_TYPE.POINT_ON_SHAPE)
		{
			if(dependencies.get(0) instanceof Line)
			{
				String l = dependencies.get(0).getName();
				String s = "pair " + n + " = relpoint(" + l + ", " + relativeLocation + "); ";
				if(!hide) s += "dot(" + n + "); label(\"$" + n2 + "$\", " + n + ", SE);\n";
				else s+="\n";
				return s;
			}
			else if(dependencies.get(0) instanceof Circle)
			{
				String c = dependencies.get(0).getName();
				String s = "pair " + n + " = relpoint(" + c + ", " + -relativeLocation/(2*Math.PI) + "); ";
				if(!hide) s += "dot(" + n + "); label(\"$" + n2 + "$\", " + n + ", SE);\n";
				else s+="\n";
				return s;
			}
		}
		else if(type == POINT_TYPE.MIDPOINT)
		{
			String p1 = dependencies.get(0).getName();
			String p2 = dependencies.get(1).getName();
			String s = "pair " + n + " = (" + p1 + " + " + p2 + ")/2; ";
			if(!hide) s += "dot(" + n + "); label(\"$" + n2 + "$\", " + n + ", SE);\n";
			else s+="\n";
			return s;
		}
		return null;
	}
}
