package asypad.shapes;

import javafx.scene.layout.Pane;
import asypad.shapes.types.LINE_TYPE;

/**
 * Custom Line used for drawing in AsyPad.
 * @author Raymond Feng
 */
public class Line extends Shape
{
	/**
	 * Underlying line that is drawn to the screen.
	 */
	private javafx.scene.shape.Line line;

	/**
	 * x-coordinate of the start point of the line.
	 */
	private double x1;

	/**
	 * y-coordinate of the start point of the line.
	 */
	private double y1;

	/**
	 * x-coordinate of the end point of the line.
	 */
	private double x2;

	/**
	 * y-coordinate of the end point of the line.
	 */
	private double y2;

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
			double dx = p2.getX()-p1.getX();
			double dy = p2.getY()-p1.getY();
			double dirx = dx/Math.sqrt(dx*dx+dy*dy);
			double diry = dy/Math.sqrt(dx*dx+dy*dy);
			x1 = p1.getX()-INF*dirx;
			y1 = p1.getY()-INF*diry;
			x2 = p2.getX()+INF*dirx;
			y2 = p2.getY()+INF*diry;
			label.setText("line"+p1.getName()+p2.getName());
		}
		line = new javafx.scene.shape.Line(x1, y1, x2, y2);
		line.setStrokeWidth(StrokeWidth);
	}

	/**
	 * Constructs line through specified point that is parallel or perpendicular to specified line.
	 * @param p point that line should go through
	 * @param l line that will be used as reference
	 * @param isParallel {@code true} for a parallel line, {@code false} for a perpendicular line
	 */
	public Line(Point p, Line l, boolean isParallel)
	{
		super(p, l);
		if(isParallel)
		{
			type = LINE_TYPE.PARALLEL_LINE;
			double dx = l.getEndX()-l.getStartX();
			double dy = l.getEndY()-l.getStartY();
			double dirx = dx/Math.sqrt(dx*dx+dy*dy);
			double diry = dy/Math.sqrt(dx*dx+dy*dy);
			x1 = p.getX()-INF*dirx;
			y1 = p.getY()-INF*diry;
			x2 = p.getX()+INF*dirx;
			y2 = p.getY()+INF*diry;
			label.setText("par"+p.getName()+l.getName());
		}
		else
		{
			type = LINE_TYPE.PERPENDICULAR_LINE;
			double dx = l.getEndX()-l.getStartX();
			double dy = l.getEndY()-l.getStartY();
			double dirx = dx/Math.sqrt(dx*dx+dy*dy);
			double diry = dy/Math.sqrt(dx*dx+dy*dy);
			x1 = p.getX()+INF*diry;
			y1 = p.getY()-INF*dirx;
			x2 = p.getX()-INF*diry;
			y2 = p.getY()+INF*dirx;
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
		double dx = p2.getX()-p1.getX();
		double dy = p2.getY()-p1.getY();
		double dirx = dx/Math.sqrt(dx*dx+dy*dy);
		double diry = dy/Math.sqrt(dx*dx+dy*dy);
		x1 = mx+INF*diry;
		y1 = my-INF*dirx;
		x2 = mx-INF*diry;
		y2 = my+INF*dirx;
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
			double dx = p2.getX()-p1.getX();
			double dy = p2.getY()-p1.getY();
			double dirx = dx/Math.sqrt(dx*dx+dy*dy);
			double diry = dy/Math.sqrt(dx*dx+dy*dy);
			x1 = p1.getX()-INF*dirx;
			y1 = p1.getY()-INF*diry;
			x2 = p2.getX()+INF*dirx;
			y2 = p2.getY()+INF*diry;
		}
		else if(type == LINE_TYPE.PARALLEL_LINE)
		{
			Point p = (Point) dependencies.get(0);
			Line l = (Line) dependencies.get(1);
			double dx = l.getEndX()-l.getStartX();
			double dy = l.getEndY()-l.getStartY();
			double dirx = dx/Math.sqrt(dx*dx+dy*dy);
			double diry = dy/Math.sqrt(dx*dx+dy*dy);
			x1 = p.getX()-INF*dirx;
			y1 = p.getY()-INF*diry;
			x2 = p.getX()+INF*dirx;
			y2 = p.getY()+INF*diry;
		}
		else if(type == LINE_TYPE.PERPENDICULAR_LINE)
		{
			Point p = (Point) dependencies.get(0);
			Line l = (Line) dependencies.get(1);
			double dx = l.getEndX()-l.getStartX();
			double dy = l.getEndY()-l.getStartY();
			double dirx = dx/Math.sqrt(dx*dx+dy*dy);
			double diry = dy/Math.sqrt(dx*dx+dy*dy);
			x1 = p.getX()+INF*diry;
			y1 = p.getY()-INF*dirx;
			x2 = p.getX()-INF*diry;
			y2 = p.getY()+INF*dirx;
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
			double dx = p2.getX()-p1.getX();
			double dy = p2.getY()-p1.getY();
			double dirx = dx/Math.sqrt(dx*dx+dy*dy);
			double diry = dy/Math.sqrt(dx*dx+dy*dy);
			x1 = mx+INF*diry;
			y1 = my-INF*dirx;
			x2 = mx-INF*diry;
			y2 = my+INF*dirx;
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

	public void refreshName()
	{
		String d1 = dependencies.get(0).getName();
		String d2 = dependencies.get(1).getName();
		if(type == LINE_TYPE.SEGMENT)
		{
			label.setText("seg"+d1+d2);
		}
		else if(type == LINE_TYPE.LINE)
		{
			label.setText("line"+d1+d2);
		}
		else if(type == LINE_TYPE.PARALLEL_LINE)
		{
			label.setText("par"+d1+d2);
		}
		else if(type == LINE_TYPE.PERPENDICULAR_LINE)
		{
			label.setText("per"+d1+d2);
		}
		else if(type == LINE_TYPE.ANGLE_BISECTOR)
		{
			label.setText("ab"+d1+d2+dependencies.get(2).getName());
		}
		else if(type == LINE_TYPE.PERPENDICULAR_BISECTOR)
		{
			label.setText("pb"+d1+d2);
		}
		for(Shape s : children) s.refreshName();
	}

	public javafx.scene.shape.Line getObject()
	{
		return line;
	}

	public String toString()
	{
		String s = "";
		if(type == LINE_TYPE.SEGMENT || type == LINE_TYPE.LINE 
				|| type == LINE_TYPE.PARALLEL_LINE || type == LINE_TYPE.PERPENDICULAR_LINE
				|| type == LINE_TYPE.PERPENDICULAR_BISECTOR)
		{
			s = "LINE: type = " + type + " dependencies: " + dependencies.get(0).getName()
					+ ", " + dependencies.get(1).getName();
		}
		else if(type == LINE_TYPE.ANGLE_BISECTOR)
		{
			s = "LINE: type = " + type + " dependencies: " + dependencies.get(0).getName()
					+ ", " + dependencies.get(1).getName() + ", " + dependencies.get(2).getName();
		}
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
			String s = "path " + n + " = (" + p1 + "-" + INF/100 + "*unit(" + p2 + "-" + p1 + "))--(" + p2 + "+" + INF/100 + "*unit(" + p2 + "-" + p1 + ")); ";
			if(!hide) s+="draw(" + n + ");\n";
			else s+="\n";
			return s;
		}
		else if(type == LINE_TYPE.PARALLEL_LINE)
		{
			String p = dependencies.get(0).getName();
			String l = dependencies.get(1).getName();
			String s = "path " + n + " = (" + p + "-" + INF/100 + "*dir(" + l + "))--(" + p + "+" + INF/100 + "*dir(" + l + ")); ";
			if(!hide) s+="draw(" + n + ");\n";
			else s+="\n";
			return s;
		}
		else if(type == LINE_TYPE.PERPENDICULAR_LINE)
		{
			String p = dependencies.get(0).getName();
			String l = dependencies.get(1).getName();
			String s = "path " + n + " = (" + p + "-" + INF/100 + "*(dir(" + l + ").y, -dir(" + l + ").x))--(" + p + "+" + INF/100 + "*(dir(" + l + ").y, -dir(" + l + ").x)); ";
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
			String s = "path " + n + " = (" + p2 + "-" + INF/100 + "*" + bisectorpoint + ")--(" + p2 + "+" + INF/100 + "*" + bisectorpoint + "); ";
			if(!hide) s+="draw(" + n + ");\n";
			else s+="\n";
			return s;
		}
		else if(type == LINE_TYPE.PERPENDICULAR_BISECTOR)
		{
			String p1 = dependencies.get(0).getName();
			String p2 = dependencies.get(1).getName();
			String p = "(" + p1 + "+" + p2 + ")/2";
			String s = "path " + n + " = (" + p + "-" + INF/100 + "*unit(((" + p2 + "-" + p1 + ").y, -(" + p2 + "-" + p1 + ").x)))--(" + p + "+" + INF/100 + "*unit(((" + p2 + "-" + p1 + ").y, -(" + p2 + "-" + p1 + ").x))); ";
			if(!hide) s+="draw(" + n + ");\n";
			else s+="\n";
			return s;
		}
		return null;
	}
}
