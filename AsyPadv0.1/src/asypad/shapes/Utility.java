package asypad.shapes;

/**
 * Utility that contains useful functions.
 * @author Raymond Feng
 */

public class Utility
{
	/**
	 * Calculates the distance between 2 points.
	 * @param x1 x-coordinate of first point
	 * @param y1 y-coordinate of first point
	 * @param x2 x-coordinate of second point
	 * @param y2 y-coordinate of second point
	 * @return distance between the 2 points
	 */
	public static double dist(double x1, double y1, double x2, double y2)
	{
		return Math.sqrt((x1-x2)*(x1-x2)+(y1-y2)*(y1-y2));
	}

	/**
	 * Calculates the distance between 2 points.
	 * @param p1 first point
	 * @param p2 second point
	 * @return distance between the 2 points
	 */
	public static double dist(Point p1, Point p2)
	{
		return dist(p1.getX(), p1.getY(), p2.getX(), p2.getY());
	}

	/**
	 * Calculates the x-value that satisfies the equations y=m1*x+b1 and y=m2*x+b2.
	 * @param m1 slope of first equation
	 * @param b1 y-intercept of first equation
	 * @param m2 slope of second equation
	 * @param b2 y-intercept of second equation
	 * @return x-value that satisfies both equation
	 */

	public static double solveX(double m1, double b1, double m2, double b2)
	{
		if(m1 == m2) return 1/0.000000001;
		return (b2-b1)/(m1-m2);
	}

	/**
	 * Calculates the y-value that satisfies the equations y=m1*x+b1 and y=m2*x+b2.
	 * @param m1 slope of first equation
	 * @param b1 y-intercept of first equation
	 * @param m2 slope of second equation
	 * @param b2 y-intercept of second equation
	 * @return y-value that satisfies both equation
	 */

	public static double solveY(double m1, double b1, double m2, double b2)
	{
		return m1*solveX(m1, b1, m2, b2)+b1;
	}

	/**
	 * Checks if 2 lines intersect using the cross product method.
	 * @param l1 first line
	 * @param l2 second line
	 * @return if the 2 lines intersect
	 */

	public static boolean intersect(Line l1, Line l2)
	{
		double x1 = l1.getStartX();
		double y1 = l1.getStartY();
		double x2 = l1.getEndX();
		double y2 = l1.getEndY();
		double x3 = l2.getStartX();
		double y3 = l2.getStartY();
		double x4 = l2.getEndX();
		double y4 = l2.getEndY();
		double prod1 = (x1-x3)*(y4-y3)-(y1-y3)*(x4-x3);
		double prod2 = (x2-x3)*(y4-y3)-(y2-y3)*(x4-x3);
		double prod3 = (x3-x1)*(y2-y1)-(y3-y1)*(x2-x1);
		double prod4 = (x4-x1)*(y2-y1)-(y4-y1)*(x2-x1);
		//System.out.println(x1 + " " + x2 + " " + x3 + " " + x4);
		//System.out.println(prod1+" "+prod2+" "+prod3+" "+prod4);
		if(((prod1>=0&&prod2<=0)||(prod1<=0&&prod2>=0)) && ((prod3>=0&&prod4<=0)||(prod3<=0&&prod4>=0)))
		{
			return true;
		}
		return false;
	}

	/**
	 * The x-value of the point that lies on both lines.
	 * @param l1 first line
	 * @param l2 second line
	 * @return x-coordinate of intersection point
	 */
	//fix vertical line treatment
	public static double intersectX(Line l1, Line l2)
	{
		double m1 = (l1.getEndY()-l1.getStartY())/(l1.getEndX()-l1.getStartX());
		double b1 = l1.getStartY()-m1*l1.getStartX();
		double m2 = (l2.getEndY()-l2.getStartY())/(l2.getEndX()-l2.getStartX());
		double b2 = l2.getStartY()-m2*l2.getStartX();
		if(l1.getEndX()-l1.getStartX()!=0 && l2.getEndX()-l2.getStartX()!=0)
		{
			return solveX(m1, b1, m2, b2);
		}
		else
		{
			if(l1.getEndX()-l1.getStartX()==0 && l2.getEndX()-l2.getStartX()==0)
			{
				return 1/0.000000001;
			}
			if(l1.getEndX()-l1.getStartX()==0)
			{
				return l1.getStartX();
			}
			else
			{
				return l2.getStartX();
			}
		}
	}

	/**
	 * The y-value of the point that lies on both lines.
	 * @param l1 first line
	 * @param l2 second line
	 * @return y-coordinate of intersection point
	 */

	public static double intersectY(Line l1, Line l2)
	{
		double m1 = (l1.getEndY()-l1.getStartY())/(l1.getEndX()-l1.getStartX());
		double b1 = l1.getStartY()-m1*l1.getStartX();
		double m2 = (l2.getEndY()-l2.getStartY())/(l2.getEndX()-l2.getStartX());
		double b2 = l2.getStartY()-m2*l2.getStartX();
		if(l1.getEndX()-l1.getStartX()!=0 && l2.getEndX()-l2.getStartX()!=0)
		{
			return solveY(m1, b1, m2, b2);
		}
		else
		{
			if(l1.getEndX()-l1.getStartX()==0 && l2.getEndX()-l2.getStartX()==0)
			{
				return 1/0.000000001;
			}
			if(l1.getEndX()-l1.getStartX()==0)
			{
				return m2*l1.getStartX()+b2;
			}
			else
			{
				return m1*l2.getStartX()+b1;
			}
		}
	}

	/**
	 * Calculates an intersection point between the line and the circle. If there is more than one intersection point,
	 * identifier = true means to return the x-coordinate of the intersection that is more counterclockwise on the circle.
	 * @param l line
	 * @param c circle
	 * @param identifier distinguishes between the possibly 2 different intersection points
	 * @return x-coordinate of the appropriate intersection
	 */
	public static double intersectX(Line l, Circle c, boolean identifier)
	{
		double x1 = l.getStartX()-c.getCenterX();
		double y1 = l.getStartY()-c.getCenterY();
		double x2 = l.getEndX()-c.getCenterX();
		double y2 = l.getEndY()-c.getCenterY();
		double dx = x2-x1;
		double dy = y2-y1;
		double dr = Math.sqrt(dx*dx+dy*dy);
		double D = x1*y2-x2*y1;
		double r = c.getRadius();
		double discriminant = r*r*dr*dr-D*D;
		if(discriminant < 0) return -1;
		else if(discriminant == 0)
		{
			return D*dy/(dr*dr)+c.getCenterX();
		}
		else
		{
			double ix1 = (D*dy+(Math.abs(dy)/dy)*dx*Math.sqrt(discriminant))/(dr*dr);
			double iy1 = (-D*dy+Math.abs(dy)*Math.sqrt(discriminant))/(dr*dr);
			double ix2 = (D*dy-(Math.abs(dy)/dy)*dx*Math.sqrt(discriminant))/(dr*dr);
			double iy2 = (-D*dy-Math.abs(dy)*Math.sqrt(discriminant))/(dr*dr);
			double theta1 = Math.atan((iy1-c.getCenterY())/(ix1-c.getCenterX()));
			if(ix1 < c.getCenterX()) theta1 += Math.PI;
			if((ix1*iy2-ix2*iy1) > 0)
			{
				if(identifier)
				{
					return ix2+c.getCenterX();
				}
				else
				{
					return ix1+c.getCenterX();
				}
			}
			else
			{
				if(identifier)
				{
					return ix1+c.getCenterX();
				}
				else
				{
					return ix2+c.getCenterX();
				}
			}
		}
	}

	/**
	 * Calculates an intersection point between the line and the circle. If there is more than one intersection point,
	 * identifier = true means to return the y-coordinate of the intersection that is more counterclockwise on the circle.
	 * @param l line
	 * @param c circle
	 * @param identifier distinguishes between the possibly 2 different intersection points
	 * @return y-coordinate of the appropriate intersection
	 */
	public static double intersectY(Line l, Circle c, boolean identifier)
	{
		double x1 = l.getStartX()-c.getCenterX();
		double y1 = l.getStartY()-c.getCenterY();
		double x2 = l.getEndX()-c.getCenterX();
		double y2 = l.getEndY()-c.getCenterY();
		double dx = x2-x1;
		double dy = y2-y1;
		double dr = Math.sqrt(dx*dx+dy*dy);
		double D = x1*y2-x2*y1;
		double r = c.getRadius();
		double discriminant = r*r*dr*dr-D*D;
		if(discriminant < 0) return -1;
		else if(discriminant == 0)
		{
			return -D*dx/(dr*dr)+c.getCenterY();
		}
		else
		{
			double ix1 = (D*dy+(Math.abs(dy)/dy)*dx*Math.sqrt(discriminant))/(dr*dr);
			double iy1 = (-D*dy+Math.abs(dy)*Math.sqrt(discriminant))/(dr*dr);
			double ix2 = (D*dy-(Math.abs(dy)/dy)*dx*Math.sqrt(discriminant))/(dr*dr);
			double iy2 = (-D*dy-Math.abs(dy)*Math.sqrt(discriminant))/(dr*dr);
			if((ix1*iy2-ix2*iy1) > 0)
			{
				if(identifier)
				{
					return iy2+c.getCenterY();
				}
				else
				{
					return iy1+c.getCenterY();
				}
			}
			else
			{
				if(identifier)
				{
					return iy1+c.getCenterY();
				}
				else
				{
					return iy2+c.getCenterY();
				}
			}
		}
	}

	/**
	 * Calculates distance from a point (x1, y1) to the line containing (lx1, ly1) and (lx2, ly2).
	 * @param lx1 x-coordinate of first point on line
	 * @param ly1 y-coordinate of first point on line
	 * @param lx2 x-coordinate of second point on line
	 * @param ly2 y-coordinate of second point on line
	 * @param x1 x-coordinate of point
	 * @param y1 y-coordinate of point
	 * @return distance from (x1, y1) to the line containing (lx1, ly1) and (lx2, ly2)
	 */

	public static double distToL(double lx1, double ly1, double lx2, double ly2, double x1, double y1)
	{
		return Math.abs((ly2-ly1)*x1-(lx2-lx1)*y1+lx2*ly1-ly2*lx1)/dist(lx1, ly1, lx2, ly2);
	}

	/**
	 * Calculates the distance from (x, y) to the line l.
	 * @param x x-coordinate of point
	 * @param y y-coordinate of point
	 * @param l line
	 * @return distance from point to line
	 */
	public static double distToL(double x, double y, Line l)
	{
		return distToL(l.getStartX(), l.getStartY(), l.getEndX(), l.getEndY(), x, y);
	}

	/**
	 * Finds the x-coordinate of the foot from (x, y) to line l.
	 * @param x x-coordinate
	 * @param y y-coordinate
	 * @param l line
	 * @return x-coordinate of the foot from point to line
	 */
	public static double footX(double x, double y, Line l)
	{
		double m1 = (l.getEndY()-l.getStartY())/(l.getEndX()-l.getStartX());
		double b1 = l.getStartY()-m1*l.getStartX();
		double m2 = -1/m1;
		double b2 = y-m2*x;
		if(m1 == 0) return x;
		return solveX(m1, b1, m2, b2);
	}

	/**
	 * Finds the y-coordinate of the foot from (x, y) to line l.
	 * @param x x-coordinate
	 * @param y y-coordinate
	 * @param l line
	 * @return y-coordinate of the foot from point to line
	 */
	public static double footY(double x, double y, Line l)
	{
		double m1 = (l.getEndY()-l.getStartY())/(l.getEndX()-l.getStartX());
		double b1 = l.getStartY()-m1*l.getStartX();
		double m2 = -1/m1;
		double b2 = y-m2*x;
		if(m1 == 0) return b1;
		return solveY(m1, b1, m2, b2);
	}

	/**
	 * Finds the x-coordinate of the circumcenter of 3 points.
	 * @param p1 first point
	 * @param p2 second point
	 * @param p3 third point
	 * @return x-coordinate of circumcenter
	 */
	public static double circumcenterX(Point p1, Point p2, Point p3)
	{
		double m1 = -1/((p1.getY()-p2.getY())/(p1.getX()-p2.getX()));
		double m2 = -1/((p1.getY()-p3.getY())/(p1.getX()-p3.getX()));
		double b1 = -m1*(p1.getX()+p2.getX())/2+(p1.getY()+p2.getY())/2;
		double b2 = -m2*(p1.getX()+p3.getX())/2+(p1.getY()+p3.getY())/2;
		if(p1.getY()-p2.getY() != 0 && p1.getY()-p3.getY() != 0)
		{
			return solveX(m1, b1, m2, b2);
		}
		else
		{
			if(p1.getY()-p2.getY() == 0 && p1.getY()-p3.getY() == 0)
			{
				return 1/0.000000001;
			}
			else if(p1.getY()-p2.getY() == 0)
			{
				return (p1.getX()+p2.getX())/2;
			}
			else
			{
				return (p1.getX()+p3.getX())/2;
			}
		}
	}

	/**
	 * Finds the y-coordinate of the circumcenter of 3 points.
	 * @param p1 first point
	 * @param p2 second point
	 * @param p3 third point
	 * @return y-coordinate of circumcenter
	 */
	public static double circumcenterY(Point p1, Point p2, Point p3)
	{
		double m1 = -1/((p1.getY()-p2.getY())/(p1.getX()-p2.getX()));
		double m2 = -1/((p1.getY()-p3.getY())/(p1.getX()-p3.getX()));
		double b1 = -m1*(p1.getX()+p2.getX())/2+(p1.getY()+p2.getY())/2;
		double b2 = -m2*(p1.getX()+p3.getX())/2+(p1.getY()+p3.getY())/2;
		if(p1.getY()-p2.getY() != 0 && p1.getY()-p3.getY() != 0)
		{
			return solveY(m1, b1, m2, b2);
		}
		else
		{
			if(p1.getY()-p2.getY() == 0 && p1.getY()-p3.getY() == 0)
			{
				return 1/0.000000001;
			}
			else if(p1.getY()-p2.getY() == 0)
			{
				return m2*circumcenterX(p1, p2, p3)+b2;
			}
			else
			{
				return m1*circumcenterX(p1, p2, p3)+b1;
			}
		}
	}

	/**
	 * Calculates the x-coordinate of the point that lies on the angle bisector of angle p1p2p3,
	 * and is 10 pixels away from p2.
	 * @param p1 first point
	 * @param p2 second point (vertex of angle)
	 * @param p3 third point
	 * @return x-coordinate of point on angle bisector, 10 pixels away from p2.
	 */
	public static double angleBisectorX(Point p1, Point p2, Point p3)
	{
		double d1 = dist(p1, p2);
		double d3 = dist(p2, p3);
		double x1 = (d1-10)/d1*p2.getX()+10/d1*p1.getX();
		double y1 = (d1-10)/d1*p2.getY()+10/d1*p1.getY();
		double x3 = (d3-10)/d3*p2.getX()+10/d3*p3.getX();
		double y3 = (d3-10)/d3*p2.getY()+10/d3*p3.getY();
		double x2 = (x1+x3)/2;
		double y2 = (y1+y3)/2;
		double d2 = dist(p2.getX(), p2.getY(), x2, y2);
		x2 = (d2-10)/d2*p2.getX()+10/d2*x2;
		return x2;
	}

	/**
	 * Calculates the y-coordinate of the point that lies on the angle bisector of angle p1p2p3,
	 * and is 10 pixels away from p2.
	 * @param p1 first point
	 * @param p2 second point (vertex of angle)
	 * @param p3 third point
	 * @return y-coordinate of point on angle bisector, 10 pixels away from p2.
	 */
	public static double angleBisectorY(Point p1, Point p2, Point p3)
	{
		double d1 = dist(p1, p2);
		double d3 = dist(p2, p3);
		double x1 = (d1-10)/d1*p2.getX()+10/d1*p1.getX();
		double y1 = (d1-10)/d1*p2.getY()+10/d1*p1.getY();
		double x3 = (d3-10)/d3*p2.getX()+10/d3*p3.getX();
		double y3 = (d3-10)/d3*p2.getY()+10/d3*p3.getY();
		double x2 = (x1+x3)/2;
		double y2 = (y1+y3)/2;
		double d2 = dist(p2.getX(), p2.getY(), x2, y2);
		y2 = (d2-10)/d2*p2.getY()+10/d2*y2;
		return y2;
	}

	/**
	 * Finds distance from (x, y) to shape s.
	 * @param x x-coordinate
	 * @param y y-coordinate
	 * @param s target shape
	 * @return distance from (x y) to s
	 */
	public static double distToShape(double x, double y, Shape s)
	{
		if(s instanceof Point)
		{
			return dist(x, y, ((Point) s).getX(), ((Point) s).getY());
		}
		else if(s instanceof Line)
		{
			if((((Line) s).getStartX()-footX(x, y, (Line) s))*(((Line) s).getEndX()-footX(x, y, (Line) s)) <= 0)
			{
				return distToL(((Line) s).getStartX(), ((Line) s).getStartY(), ((Line) s).getEndX(), ((Line) s).getEndY(), x, y);
			}
			else return Math.min(dist(x, y, ((Line) s).getStartX(), ((Line) s).getStartY()), dist(x, y, ((Line) s).getEndX(), ((Line) s).getEndY()));
		}
		else if(s instanceof Circle)
		{
			return Math.abs(((Circle) s).getRadius()-dist(x, y, ((Circle) s).getCenterX(), ((Circle) s).getCenterY()));
		}
		return -1;
	}
}
