package asypad.shapes;

import javafx.scene.layout.Pane;
import asypad.shapes.types.LINE_TYPE;
/**
 * Custom line used for drawing in AsyPad.
 * @author Raymond Feng
 */
public class Line extends Shape
{
	private javafx.scene.shape.Line line;
	private double x1, y1, x2, y2;

	/**
	 * Constructs line through 2 points.
	 * @param p1 first point
	 * @param p2 second point
	 * @param isSegment if this line is a segment
	 */
	public Line(Point p1, Point p2, boolean isSegment)
	{
		super(p1, p2);
		if(isSegment)
		{
			type = LINE_TYPE.SEGMENT;
			x1 = p1.getX();
			y1 = p1.getY();
			x2 = p2.getX();
			y2 = p2.getY();
			label.setText("seg"+p1.getName()+p2.getName());
		}
		else 
		{
			type = LINE_TYPE.LINE;
			x1 = p1.getX()-INF*(p2.getX()-p1.getX());
			y1 = p1.getY()-INF*(p2.getY()-p1.getY());
			x2 = p2.getX()+INF*(p2.getX()-p1.getX());
			y2 = p2.getY()+INF*(p2.getY()-p1.getY());
			label.setText("line"+p1.getName()+p2.getName());
		}
		line = new javafx.scene.shape.Line(x1, y1, x2, y2);
		line.setStrokeWidth(StrokeWidth);
	}

	/**
	 * Constructs line through specified point that is parallel or perpendicular to specified line.
	 * @param p
	 * @param l
	 * @param isParallel
	 */
	public Line(Point p, Line l, boolean isParallel)
	{
		super(p, l);
		if(isParallel)
		{
			type = LINE_TYPE.PARALLEL_LINE;
			if(l.getType() != LINE_TYPE.SEGMENT)
			{
				x1 = p.getX()-(l.getEndX()-l.getStartX());
				y1 = p.getY()-(l.getEndY()-l.getStartY());
				x2 = p.getX()+(l.getEndX()-l.getStartX());
				y2 = p.getY()+(l.getEndY()-l.getStartY());
			}
			else
			{
				x1 = p.getX()-INF*(l.getEndX()-l.getStartX());
				y1 = p.getY()-INF*(l.getEndY()-l.getStartY());
				x2 = p.getX()+INF*(l.getEndX()-l.getStartX());
				y2 = p.getY()+INF*(l.getEndY()-l.getStartY());
			}
			label.setText("par"+p.getName()+l.getName());
		}
		else
		{
			type = LINE_TYPE.PERPENDICULAR_LINE;
			if(l.getType() != LINE_TYPE.SEGMENT)
			{
				x1 = p.getX()+(l.getEndY()-l.getStartY());
				y1 = p.getY()-(l.getEndX()-l.getStartX());
				x2 = p.getX()-(l.getEndY()-l.getStartY());
				y2 = p.getY()+(l.getEndX()-l.getStartX());
			}
			else
			{
				x1 = p.getX()+INF*(l.getEndY()-l.getStartY());
				y1 = p.getY()-INF*(l.getEndX()-l.getStartX());
				x2 = p.getX()-INF*(l.getEndY()-l.getStartY());
				y2 = p.getY()+INF*(l.getEndX()-l.getStartX());
			}
			label.setText("per"+p.getName()+l.getName());
		}
		line = new javafx.scene.shape.Line(x1, y1, x2, y2);
		line.setStrokeWidth(StrokeWidth);
	}

	/**
	 * Constructs the angle bisector of angle p1p2p3.
	 * @param p1 first point
	 * @param p2 second point (vertex of angle)
	 * @param p3 third point
	 */
	public Line(Point p1, Point p2, Point p3)
	{
		super(p1, p2, p3);
		type = LINE_TYPE.ANGLE_BISECTOR;
		double abx = Utility.angleBisectorX(p1, p2, p3);
		double aby = Utility.angleBisectorY(p1, p2, p3);
		x1 = p2.getX()-INF*(abx-p2.getX());
		y1 = p2.getY()-INF*(aby-p2.getY());
		x2 = abx+INF*(abx-p2.getX());
		y2 = aby+INF*(aby-p2.getY());
		label.setText("ab"+p1.getName()+p2.getName()+p3.getName());
		line = new javafx.scene.shape.Line(x1, y1, x2, y2);
		line.setStrokeWidth(StrokeWidth);
	}
	
	/**
	 * Constructs the perpendicular bisector of the 2 points.
	 * @param p1 first point
	 * @param p2 second point
	 */
	public Line(Point p1, Point p2)
	{
		super(p1, p2);
		type = LINE_TYPE.PERPENDICULAR_BISECTOR;
		double mx = (p1.getX()+p2.getX())/2;
		double my = (p1.getY()+p2.getY())/2;
		x1 = mx+INF*(p2.getY()-p1.getY());
		y1 = my-INF*(p2.getX()-p1.getX());
		x2 = mx-INF*(p2.getY()-p1.getY());
		y2 = my+INF*(p2.getX()-p1.getX());
		label.setText("pb"+p1.getName()+p2.getName());
		line = new javafx.scene.shape.Line(x1, y1, x2, y2);
		line.setStrokeWidth(StrokeWidth);
	}

	/**
	 * Gets start x.
	 * @return start x
	 */
	public double getStartX() 
	{
		return x1;
	}

	/**
	 * Sets start x.
	 * @param x1 new start x
	 */
	public void setStartX(double x1) 
	{
		this.x1 = x1;
		refresh();
	}

	/**
	 * Gets start y.
	 * @return start y
	 */
	public double getStartY() 
	{
		return y1;
	}

	/**
	 * Sets start y.
	 * @param y1 new start y
	 */
	public void setStartY(double y1) 
	{
		this.y1 = y1;
		refresh();
	}

	/**
	 * Gets end x.
	 * @return end x
	 */
	public double getEndX() 
	{
		return x2;
	}

	/**
	 * Sets end x.
	 * @param x2 new end x
	 */
	public void setEndX(double x2) 
	{
		this.x2 = x2;
		refresh();
	}

	/**
	 * Gets end y.
	 * @return end y
	 */
	public double getEndY() 
	{
		return y2;
	}

	/**
	 * Sets end y.
	 * @param y2 new end y
	 */
	public void setEndY(double y2) 
	{
		this.y2 = y2;
		refresh();
	}

	public void draw(Pane p)
	{
		p.getChildren().add(line);
	}

	public void refresh()
	{
		if(type == LINE_TYPE.SEGMENT)
		{
			Point p1 = (Point) dependencies.get(0);
			Point p2 = (Point) dependencies.get(1);
			x1 = p1.getX();
			y1 = p1.getY();
			x2 = p2.getX();
			y2 = p2.getY();
		}
		else if(type == LINE_TYPE.LINE)
		{
			Point p1 = (Point) dependencies.get(0);
			Point p2 = (Point) dependencies.get(1);
			x1 = p1.getX()-INF*(p2.getX()-p1.getX());
			y1 = p1.getY()-INF*(p2.getY()-p1.getY());
			x2 = p2.getX()+INF*(p2.getX()-p1.getX());
			y2 = p2.getY()+INF*(p2.getY()-p1.getY());
		}
		else if(type == LINE_TYPE.PARALLEL_LINE)
		{
			Point p = (Point) dependencies.get(0);
			Line l = (Line) dependencies.get(1);
			if(l.getType() != LINE_TYPE.SEGMENT)
			{
				x1 = p.getX()-(l.getEndX()-l.getStartX());
				y1 = p.getY()-(l.getEndY()-l.getStartY());
				x2 = p.getX()+(l.getEndX()-l.getStartX());
				y2 = p.getY()+(l.getEndY()-l.getStartY());
			}
			else
			{
				x1 = p.getX()-INF*(l.getEndX()-l.getStartX());
				y1 = p.getY()-INF*(l.getEndY()-l.getStartY());
				x2 = p.getX()+INF*(l.getEndX()-l.getStartX());
				y2 = p.getY()+INF*(l.getEndY()-l.getStartY());
			}
		}
		else if(type == LINE_TYPE.PERPENDICULAR_LINE)
		{
			Point p = (Point) dependencies.get(0);
			Line l = (Line) dependencies.get(1);
			if(l.getType() != LINE_TYPE.SEGMENT)
			{
				x1 = p.getX()+(l.getEndY()-l.getStartY());
				y1 = p.getY()-(l.getEndX()-l.getStartX());
				x2 = p.getX()-(l.getEndY()-l.getStartY());
				y2 = p.getY()+(l.getEndX()-l.getStartX());
			}
			else
			{
				x1 = p.getX()+INF*(l.getEndY()-l.getStartY());
				y1 = p.getY()-INF*(l.getEndX()-l.getStartX());
				x2 = p.getX()-INF*(l.getEndY()-l.getStartY());
				y2 = p.getY()+INF*(l.getEndX()-l.getStartX());
			}
		}
		else if(type == LINE_TYPE.ANGLE_BISECTOR)
		{
			Point p1 = (Point) dependencies.get(0);
			Point p2 = (Point) dependencies.get(1);
			Point p3 = (Point) dependencies.get(2);
			double abx = Utility.angleBisectorX(p1, p2, p3);
			double aby = Utility.angleBisectorY(p1, p2, p3);
			x1 = p2.getX()-INF*(abx-p2.getX());
			y1 = p2.getY()-INF*(aby-p2.getY());
			x2 = abx+INF*(abx-p2.getX());
			y2 = aby+INF*(aby-p2.getY());
		}
		else if(type == LINE_TYPE.PERPENDICULAR_BISECTOR)
		{
			Point p1 = (Point) dependencies.get(0);
			Point p2 = (Point) dependencies.get(1);
			double mx = (p1.getX()+p2.getX())/2;
			double my = (p1.getY()+p2.getY())/2;
			x1 = mx+INF*(p2.getY()-p1.getY());
			y1 = my-INF*(p2.getX()-p1.getX());
			x2 = mx-INF*(p2.getY()-p1.getY());
			y2 = my+INF*(p2.getX()-p1.getX());
		}
		//System.out.println(this);
		line.setStartX(x1);
		line.setStartY(y1);
		line.setEndX(x2);
		line.setEndY(y2);
		line.setStrokeWidth(StrokeWidth);
		for(Shape s : children)
		{
			s.refresh();
		}
	}

	public javafx.scene.shape.Line getObject()
	{
		return line;
	}

	public String toString()
	{
		String s = "LINE: type = " + type + " startx = " + x1 + " starty = " + y1 + " endx = " + x2 + " endy = " + y2;
		return s;
	}
	
	public String toAsymptote()
	{
		if(!inAsyCode) return "";
		String n = getName();
		if(type == LINE_TYPE.SEGMENT)
		{
			String p1 = dependencies.get(0).getName();
			String p2 = dependencies.get(1).getName();
			String s = "path " + n + " = " + p1 + "--" + p2 + "; ";
			if(!hide) s+="draw(" + n + ");\n";
			else s+="\n";
			return s;
		}
		else if(type == LINE_TYPE.LINE)
		{
			String p1 = dependencies.get(0).getName();
			String p2 = dependencies.get(1).getName();
			String s = "path " + n + " = (" + p1 + "-" + INF + "*(" + p2 + "-" + p1 + "))--(" + p2 + "+" + INF + "*(" + p2 + "-" + p1 + ")); ";
			if(!hide) s+="draw(" + n + ");\n";
			else s+="\n";
			return s;
		}
		else if(type == LINE_TYPE.PARALLEL_LINE)
		{
			String p = dependencies.get(0).getName();
			String l = dependencies.get(1).getName();
			String s = "path " + n + " = (" + p + "-" + INF + "*dir(" + l + "))--" + "(" + p + "+" + INF + "*dir(" + l + ")); ";
			if(!hide) s+="draw(" + n + ");\n";
			else s+="\n";
			return s;
		}
		else if(type == LINE_TYPE.PERPENDICULAR_LINE)
		{
			String p = dependencies.get(0).getName();
			String l = dependencies.get(1).getName();
			String s = "path " + n + " = (" + p + "-" + INF + "*(dir(" + l + ").y, -dir(" + l + ").x))--" + "(" + p + "+" + INF + "*(dir(" + l + ").y, -dir(" + l + ").x)); ";
			if(!hide) s+="draw(" + n + ");\n";
			else s+="\n";
			return s;
		}
		else if(type == LINE_TYPE.ANGLE_BISECTOR)
		{
			String p1 = dependencies.get(0).getName();
			String p2 = dependencies.get(1).getName();
			String p3 = dependencies.get(2).getName();
			String bisectorpoint = "(bisectorpoint(" + p1 + ", " + p2 + ", " + p3 + ")-" + p2 + ")";
			String s = "path " + n + " = (" + p2 + "-" + INF + "*" + bisectorpoint + ")--(" + p2 + "+" + INF + "*" + bisectorpoint + "); ";
			if(!hide) s+="draw(" + n + ");\n";
			else s+="\n";
			return s;
		}
		else if(type == LINE_TYPE.PERPENDICULAR_BISECTOR)
		{
			String p1 = dependencies.get(0).getName();
			String p2 = dependencies.get(1).getName();
			String p = "(" + p1 + "+" + p2 + ")/2";
			String s = "path " + n + " = (" + p + "-" + INF + "*unit(((" + p2 + "-" + p1 + ").y, -(" + p2 + "-" + p1 + ").x)))--(" + p + "+" + INF + "*unit(((" + p2 + "-" + p1 + ").y, -(" + p2 + "-" + p1 + ").x))); ";
			if(!hide) s+="draw(" + n + ");\n";
			else s+="\n";
			return s;
		}
		return null;
	}
}
