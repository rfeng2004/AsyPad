package asypad.shapes;

import asypad.shapes.types.CIRCLE_TYPE;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

/**
 * Custom Circle used for drawing in AsyPad.
 * @author Raymond Feng
 */
public class Circle extends Shape
{
	/**
	 * Underlying circle that is drawn onto the screen.
	 */
	private javafx.scene.shape.Circle circle;

	/**
	 * x-coordinate of the center of the circle.
	 */
	private double x;

	/**
	 * y-coordinate of the center of the circle.
	 */
	private double y;

	/**
	 * Radius of the circle.
	 */
	private double radius;
	
	/**
	 * Identifier to distinguish between the 2 possible circles tangent to 2 circles and tangent to the first at a point, 
	 * only used for {@code LINE_TYPE.TANGENT_CIRCLE} that depend on 2 circles and a point.
	 */
	private boolean identifier;

	/**
	 * Constructs new Circle with center and a point on the circle.
	 * @param center center point
	 * @param on point on the circle
	 */
	public Circle(Point center, Point on)
	{
		super(center, on);
		x = center.getX();
		y = center.getY();
		radius = Utility.dist(center, on);
		type = CIRCLE_TYPE.CIRCLE;
		circle = new javafx.scene.shape.Circle(x, y, radius);
		circle.setFill(Color.TRANSPARENT);
		circle.setStrokeWidth(StrokeWidth);
		circle.setStroke(color);
		label.setText("circ" + center.getName() + SEPARATOR + on.getName());
	}

	/**
	 * Constructs a circle based on 3 points.
	 * @param p1 first point
	 * @param p2 second point
	 * @param p3 third point
	 * @param isCircumcircle if the circle is a circumcircle
	 */
	public Circle(Point p1, Point p2, Point p3, boolean isCircumcircle)
	{
		super(p1, p2, p3);
		if(isCircumcircle)
		{
			x = Utility.circumcenterX(p1, p2, p3);
			y = Utility.circumcenterY(p1, p2, p3);
			radius = Utility.dist(x, y, p1.getX(), p1.getY());
			type = CIRCLE_TYPE.CIRCUMCIRCLE;
			circle = new javafx.scene.shape.Circle(x, y, radius);
			circle.setFill(Color.TRANSPARENT);
			circle.setStrokeWidth(StrokeWidth);
			circle.setStroke(color);
			label.setText("cc" + p1.getName() + SEPARATOR + p2.getName() + SEPARATOR + p3.getName());
		}
		else
		{
			x = Utility.incenterX(p1, p2, p3);
			y = Utility.incenterY(p1, p2, p3);
			radius = Utility.distToL(p1.getX(), p1.getY(), p2.getX(), p2.getY(), x, y);
			type = CIRCLE_TYPE.INCIRCLE;
			circle = new javafx.scene.shape.Circle(x, y, radius);
			circle.setFill(Color.TRANSPARENT);
			circle.setStrokeWidth(StrokeWidth);
			circle.setStroke(color);
			label.setText("ic" + p1.getName() + SEPARATOR + p2.getName() + SEPARATOR + p3.getName());
		}
	}
	
	/**
	 * Constructs a circle tangent to two circles, tangent to the first circle at a given point. 
	 * identifier = true means it is internally tangent to one of the circles and externally tangent to another.
	 * @param c1 first circle
	 * @param c2 second circle
	 * @param p point
	 * @param identifier
	 * @param isCircumcircle if the circle is a circumcircle
	 */
	public Circle(Circle c1, Circle c2, Point p, boolean identifier)
	{
		super(c1, c2, p);
		x = Utility.tangentCircleX(c1, c2, p, identifier);
		y = Utility.tangentCircleY(c1, c2, p, identifier);
		radius = Utility.dist(x, y, c1.getCenterX(), c1.getCenterY()) - Utility.dist(x, y, p.getX(), p.getY());
		type = CIRCLE_TYPE.TANGENT_CIRCLE;
		circle = new javafx.scene.shape.Circle(x, y, radius);
		circle.setFill(Color.TRANSPARENT);
		circle.setStrokeWidth(StrokeWidth);
		circle.setStroke(color);
		label.setText("tc" + c1.getName() + SEPARATOR + c2.getName() + SEPARATOR + p.getName());
	}

	/**
	 * Returns the x-coordinate of the center.
	 * @return x-coordinate of center
	 */
	public double getCenterX()
	{
		return x;
	}

	/**
	 * Returns the y-coordinate of the center.
	 * @return y-coordinate of center
	 */
	public double getCenterY()
	{
		return y;
	}

	/**
	 * Returns the radius.
	 * @return radius
	 */
	public double getRadius()
	{
		return radius;
	}

	public void draw(Pane p)
	{
		p.getChildren().add(circle);
	}

	public void refresh()
	{
		if(type == CIRCLE_TYPE.CIRCLE)
		{
			Point center = (Point) dependencies.get(0);
			Point on = (Point) dependencies.get(1);
			x = center.getX();
			y = center.getY();
			radius = Utility.dist(center, on);
		}
		else if(type == CIRCLE_TYPE.CIRCUMCIRCLE)
		{
			Point p1 = (Point) dependencies.get(0);
			Point p2 = (Point) dependencies.get(1);
			Point p3 = (Point) dependencies.get(2);
			x = Utility.circumcenterX(p1, p2, p3);
			y = Utility.circumcenterY(p1, p2, p3);
			radius = Utility.dist(x, y, p1.getX(), p1.getY());
		}
		else if(type == CIRCLE_TYPE.INCIRCLE)
		{
			Point p1 = (Point) dependencies.get(0);
			Point p2 = (Point) dependencies.get(1);
			Point p3 = (Point) dependencies.get(2);
			x = Utility.incenterX(p1, p2, p3);
			y = Utility.incenterY(p1, p2, p3);
			radius = Utility.distToL(p1.getX(), p1.getY(), p2.getX(), p2.getY(), x, y);
		}
		else if(type == CIRCLE_TYPE.TANGENT_CIRCLE)
		{
			Circle c1 = (Circle) dependencies.get(0);
			Circle c2 = (Circle) dependencies.get(1);
			Point p = (Point) dependencies.get(2);

			x = Utility.tangentCircleX(c1, c2, p, identifier);
			y = Utility.tangentCircleY(c1, c2, p, identifier);
			radius = Utility.dist(x, y, c1.getCenterX(), c1.getCenterY()) - Utility.dist(x, y, p.getX(), p.getY());
		}
		circle.setCenterX(x);
		circle.setCenterY(y);
		circle.setRadius(radius);
		circle.setStrokeWidth(StrokeWidth);
		circle.setStroke(color);
		for(Shape s : children)
		{
			s.refresh();
		}
	}

	public void refreshName()
	{
		String d1 = dependencies.get(0).getName();
		String d2 = dependencies.get(1).getName();
		if(type == CIRCLE_TYPE.CIRCLE)
		{
			label.setText("circ"+d1+SEPARATOR+d2);
		}
		else if(type == CIRCLE_TYPE.CIRCUMCIRCLE)
		{
			label.setText("cc"+d1+SEPARATOR+d2+SEPARATOR+dependencies.get(2).getName());
		}
		else if(type == CIRCLE_TYPE.INCIRCLE)
		{
			label.setText("ic"+d1+SEPARATOR+d2+SEPARATOR+dependencies.get(2).getName());
		}
		else if(type == CIRCLE_TYPE.TANGENT_CIRCLE)
		{
			label.setText("tc"+d1+SEPARATOR+d2+SEPARATOR+dependencies.get(2).getName());
		}
		for(Shape s : children) s.refreshName();
	}

	public javafx.scene.shape.Circle getObject()
	{
		return circle;
	}

	public String toString()
	{
		String s = "";
		if(type == CIRCLE_TYPE.CIRCLE)
		{
			s = "CIRCLE: type = " + type + " dependencies: " + dependencies.get(0).getName()
					+ ", " + dependencies.get(1).getName();
		}
		else if(type == CIRCLE_TYPE.CIRCUMCIRCLE || type == CIRCLE_TYPE.INCIRCLE)
		{
			s = "CIRCLE: type = " + type + " dependencies: " + dependencies.get(0).getName()
					+ ", " + dependencies.get(1).getName() + ", " + dependencies.get(2).getName();
		}
		else if(type == CIRCLE_TYPE.TANGENT_CIRCLE)
		{
			s = "CIRCLE: type = " + type + " dependencies: " + dependencies.get(0).getName()
					+ ", " + dependencies.get(1).getName() + ", " + dependencies.get(2).getName()
					+ " identifier = " + identifier;
		}
		return s;
	}

	public String toAsymptote()
	{
		if(!inAsyCode) return "";
		String n = getName();
		if(type == CIRCLE_TYPE.CIRCLE)
		{
			String p1 = dependencies.get(0).getName();
			String p2 = dependencies.get(1).getName();
			String s = "path " + n + " = Circle(" + p1 + ", abs(" + p1 + "-" + p2 + ")); ";
			if(!hide) s+="draw(" + n + ");\n";
			else s+="\n";
			return s;
		}
		else if(type == CIRCLE_TYPE.CIRCUMCIRCLE)
		{
			String p1 = dependencies.get(0).getName();
			String p2 = dependencies.get(1).getName();
			String p3 = dependencies.get(2).getName();
			String s = "path " + n + " = circumcircle(" + p1 + ", " + p2 + ", " + p3 + "); ";
			if(!hide) s+="draw(" + n + ");\n";
			else s+="\n";
			return s;
		}
		else if(type == CIRCLE_TYPE.INCIRCLE)
		{
			String p1 = dependencies.get(0).getName();
			String p2 = dependencies.get(1).getName();
			String p3 = dependencies.get(2).getName();
			String s = "path " + n + " = incircle(" + p1 + ", " + p2 + ", " + p3 + "); ";
			if(!hide) s+="draw(" + n + ");\n";
			else s+="\n";
			return s;
		}
		else if(type == CIRCLE_TYPE.TANGENT_CIRCLE)
		{
			String p1 = dependencies.get(0).getName();
			String p2 = dependencies.get(1).getName();
			String p3 = dependencies.get(2).getName();
			String s = "path " + n + " = incircle(" + p1 + ", " + p2 + ", " + p3 + "); ";
			if(!hide) s+="draw(" + n + ");\n";
			else s+="\n";
			return s;
		}
		return null;
	}

}
