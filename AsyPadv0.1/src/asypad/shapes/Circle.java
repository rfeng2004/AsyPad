package asypad.shapes;

import asypad.shapes.types.CIRCLE_TYPE;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

public class Circle extends Shape
{
	private javafx.scene.shape.Circle circle;
	private double x, y, radius;

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
		circle.setStroke(Color.BLACK);
		label.setText("circ" + center.getName() + on.getName());
	}

	/**
	 * Constructs the circumcircle of 3 points.
	 * @param p1 first point
	 * @param p2 second point
	 * @param p3 third point
	 */
	public Circle(Point p1, Point p2, Point p3)
	{
		super(p1, p2, p3);
		x = Utility.circumcenterX(p1, p2, p3);
		y = Utility.circumcenterY(p1, p2, p3);
		radius = Utility.dist(x, y, p1.getX(), p1.getY());
		type = CIRCLE_TYPE.CIRCUMCIRCLE;
		circle = new javafx.scene.shape.Circle(x, y, radius);
		circle.setFill(Color.TRANSPARENT);
		circle.setStrokeWidth(StrokeWidth);
		circle.setStroke(Color.BLACK);
		label.setText("cc" + p1.getName() + p2.getName() + p3.getName());
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
		circle.setCenterX(x);
		circle.setCenterY(y);
		circle.setRadius(radius);
		circle.setStrokeWidth(StrokeWidth);
		for(Shape s : children)
		{
			s.refresh();
		}
	}

	public javafx.scene.shape.Circle getObject()
	{
		return circle;
	}

	public String toString()
	{
		String s = "Circle: type = " + type + " x = " + x + " y = " + y + " radius = " + radius;
		return s;
	}
	
	public String toAsymptote()
	{
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
		return null;
	}

}
