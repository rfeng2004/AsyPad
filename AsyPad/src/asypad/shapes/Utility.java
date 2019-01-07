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
		if(m1 == m2) return Double.POSITIVE_INFINITY;
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
		if(m1 == m2) return Double.POSITIVE_INFINITY;
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
	 * The x-value of the point that lies on both lines. If the intersection point does not
	 * lie in between the endpoints returns Double.POSITIVE_INFINITY.
	 * @param l1 first line
	 * @param l2 second line
	 * @return x-coordinate of intersection point
	 */
	public static double intersectX(Line l1, Line l2)
	{
		double m1 = (l1.getEndY()-l1.getStartY())/(l1.getEndX()-l1.getStartX());
		double b1 = l1.getStartY()-m1*l1.getStartX();
		double m2 = (l2.getEndY()-l2.getStartY())/(l2.getEndX()-l2.getStartX());
		double b2 = l2.getStartY()-m2*l2.getStartX();
		if(l1.getEndX()-l1.getStartX()!=0 && l2.getEndX()-l2.getStartX()!=0)
		{
			double x = solveX(m1, b1, m2, b2);
			if((l1.getStartX()-x)*(l1.getEndX()-x) < 0 && (l2.getStartX()-x)*(l2.getEndX()-x) < 0) return x;
			else return Double.POSITIVE_INFINITY;
		}
		else
		{
			if(l1.getEndX()-l1.getStartX()==0 && l2.getEndX()-l2.getStartX()==0)
			{
				return Double.POSITIVE_INFINITY;
			}
			if(l1.getEndX()-l1.getStartX()==0)
			{
				if((l2.getStartX()-l1.getStartX())*(l2.getEndX()-l1.getStartX()) < 0) return l1.getStartX();
				else return Double.POSITIVE_INFINITY;
			}
			else
			{
				if((l1.getStartX()-l2.getStartX())*(l1.getEndX()-l2.getStartX()) < 0) return l2.getStartX();
				else return Double.POSITIVE_INFINITY;
			}
		}
	}

	/**
	 * The y-value of the point that lies on both lines. If the intersection point does not
	 * lie in between the endpoints returns Double.POSITIVE_INFINITY.
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
			double y = solveY(m1, b1, m2, b2);
			if((l1.getStartY()-y)*(l1.getEndY()-y) < 0 && (l2.getStartY()-y)*(l2.getEndY()-y) < 0) return y;
			else return Double.POSITIVE_INFINITY;
		}
		else
		{
			if(l1.getEndX()-l1.getStartX()==0 && l2.getEndX()-l2.getStartX()==0)
			{
				return Double.POSITIVE_INFINITY;
			}
			if(l1.getEndX()-l1.getStartX()==0)
			{
				if((l2.getStartX()-l1.getStartX())*(l2.getEndX()-l1.getStartX()) < 0) return m2*l1.getStartX()+b2;
				else return Double.POSITIVE_INFINITY;
			}
			else
			{
				if((l1.getStartX()-l2.getStartX())*(l1.getEndX()-l2.getStartX()) < 0) return m1*l2.getStartX()+b1;
				else return Double.POSITIVE_INFINITY;
			}
		}
	}

	/**
	 * Calculates an intersection point between the line and the circle. If there is more than one intersection point,
	 * identifier = true means to return the x-coordinate of the intersection that is closer to the start of the line.
	 * If such an intersection point is non-existent returns Double.POSITIVE_INFINTY.
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
		if(discriminant < -0.001) return Double.POSITIVE_INFINITY;
		else if(discriminant < 0.001)
		{
			return D*dy/(dr*dr)+c.getCenterX();
		}
		else
		{
			double ix1 = (D*dy+signumstar(dy)*dx*Math.sqrt(discriminant))/(dr*dr)+c.getCenterX();
			double ix2 = (D*dy-signumstar(dy)*dx*Math.sqrt(discriminant))/(dr*dr)+c.getCenterX();
			if(Math.abs(ix1-l.getStartX()) > Math.abs(ix2-l.getStartX()))
			{
				if(identifier)
				{
					return ix2;
				}
				else
				{
					return ix1;
				}
			}
			else
			{
				if(identifier)
				{
					return ix1;
				}
				else
				{
					return ix2;
				}
			}
		}
	}

	/**
	 * Calculates an intersection point between the line and the circle. If there is more than one intersection point,
	 * identifier = true means to return the y-coordinate of the intersection that is closer to the start of the line.
	 * If such an intersection point is non-existent returns Double.POSITIVE_INFINTY.
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
		if(discriminant < -0.001) return Double.POSITIVE_INFINITY;
		else if(discriminant < 0.001)
		{
			return -D*dx/(dr*dr)+c.getCenterY();
		}
		else
		{
			double iy1 = (-D*dx+Math.abs(dy)*Math.sqrt(discriminant))/(dr*dr)+c.getCenterY();
			double iy2 = (-D*dx-Math.abs(dy)*Math.sqrt(discriminant))/(dr*dr)+c.getCenterY();
			if(Math.abs(iy1-l.getStartY()) > Math.abs(iy2-l.getStartY()))
			{
				if(identifier)
				{
					return iy2;
				}
				else
				{
					return iy1;
				}
			}
			else
			{
				if(identifier)
				{
					return iy1;
				}
				else
				{
					return iy2;
				}
			}
		}
	}
	
	/**
	 * Calculates an intersection point between the two circles. If there is more than one intersection point,
	 * identifier = true means to return the x-coordinate of the intersection that is more counterclockwise wrt the first circle.
	 * If such an intersection point is non-existent returns Double.POSITIVE_INFINTY.
	 * @param c1 circle 1
	 * @param c2 circle 2
	 * @param identifier distinguishes between the possibly 2 different intersection points
	 * @return x-coordinate of the appropriate intersection
	 */
	public static double intersectX(Circle c1, Circle c2, boolean identifier)
	{
		double dx = c2.getCenterX() - c1.getCenterX();
		double dy = c2.getCenterY() - c1.getCenterY();
		double d = dist(c1.getCenterX(), c1.getCenterY(), c2.getCenterX(), c2.getCenterY());
		if(equal(d, c1.getRadius() + c2.getRadius()))
		{
			return c1.getRadius() + dx * c1.getRadius() / d;
		}
		else if(d > c1.getRadius() + c2.getRadius())
		{
			return Double.POSITIVE_INFINITY;
		}
		
		double a = (c1.getRadius() * c1.getRadius() - c2.getRadius() * c2.getRadius() + d * d)/ (2 * d);
		double h = Math.sqrt(c1.getRadius() * c1.getRadius() - a * a);
				
		double x2 = c1.getCenterX() + dx * a / d + h * (identifier ? 1 : -1) * (dy / d);
		
		return x2;
	}
	
	/**
	 * Calculates an intersection point between the two circles. If there is more than one intersection point,
	 * identifier = true means to return the y-coordinate of the intersection that is more counterclockwise wrt the first circle.
	 * If such an intersection point is non-existent returns Double.POSITIVE_INFINTY.
	 * @param c1 circle 1
	 * @param c2 circle 2
	 * @param identifier distinguishes between the possibly 2 different intersection points
	 * @return y-coordinate of the appropriate intersection
	 */
	public static double intersectY(Circle c1, Circle c2, boolean identifier)
	{
		double dx = c2.getCenterX() - c1.getCenterX();
		double dy = c2.getCenterY() - c1.getCenterY();
		double d = dist(c1.getCenterX(), c1.getCenterY(), c2.getCenterX(), c2.getCenterY());
		if(equal(d, c1.getRadius() + c2.getRadius()))
		{
			return c1.getRadius() + dx * c1.getRadius() / d;
		}
		else if(d > c1.getRadius() + c2.getRadius())
		{
			return Double.POSITIVE_INFINITY;
		}
		
		double a = (c1.getRadius() * c1.getRadius() - c2.getRadius() * c2.getRadius() + d * d)/ (2 * d);
		double h = Math.sqrt(c1.getRadius() * c1.getRadius() - a * a);
		
		double y2 = c1.getCenterY() + dy * a / d - h * (identifier ? 1 : -1) * (dx / d);
		
		return y2;
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
		if(l.getStartX() == l.getEndX()) return l.getStartX();
		double m1 = (l.getEndY()-l.getStartY())/(l.getEndX()-l.getStartX());
		double b1 = l.getStartY()-m1*l.getStartX();
		if(m1 == 0) return x;
		double m2 = -1/m1;
		double b2 = y-m2*x;
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
		if(l.getStartX() == l.getEndX()) return y;
		double m1 = (l.getEndY()-l.getStartY())/(l.getEndX()-l.getStartX());
		double b1 = l.getStartY()-m1*l.getStartX();
		if(m1 == 0) return l.getStartY();
		double m2 = -1/m1;
		double b2 = y-m2*x;
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
				return Double.POSITIVE_INFINITY;
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
				return Double.POSITIVE_INFINITY;
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
	 * Finds the x-coordinate of the incenter of 3 points.
	 * @param p1 first point
	 * @param p2 second point
	 * @param p3 third point
	 * @return x-coordinate of incenter
	 */
	public static double incenterX(Point p1, Point p2, Point p3)
	{
		double a = dist(p2, p3), b = dist(p1, p3), c = dist(p1, p2);
		
		return (a * p1.getX() + b * p2.getX() + c * p3.getX()) / (a+b+c);
	}
	
	/**
	 * Finds the y-coordinate of the incenter of 3 points.
	 * @param p1 first point
	 * @param p2 second point
	 * @param p3 third point
	 * @return y-coordinate of incenter
	 */
	public static double incenterY(Point p1, Point p2, Point p3)
	{
		double a = dist(p2, p3), b = dist(p1, p3), c = dist(p1, p2);
		
		return (a * p1.getY() + b * p2.getY() + c * p3.getY()) / (a+b+c);
	}
	
	/**
	 * Finds the x-coordinate of the orthocenter of 3 points.
	 * @param p1 first point
	 * @param p2 second point
	 * @param p3 third point
	 * @return x-coordinate of orthocenter
	 */
	public static double orthocenterX(Point p1, Point p2, Point p3)
	{
		Line a1 = new Line(p1, new Point(footX(p1.getX(), p1.getY(), new Line(p2, p3)), footY(p1.getX(), p1.getY(), new Line(p2, p3))));
		Line a2 = new Line(p2, new Point(footX(p2.getX(), p2.getY(), new Line(p1, p3)), footY(p2.getX(), p2.getY(), new Line(p1, p3))));
		return intersectX(a1, a2);
	}
	
	/**
	 * Finds the y-coordinate of the orthocenter of 3 points.
	 * @param p1 first point
	 * @param p2 second point
	 * @param p3 third point
	 * @return y-coordinate of orthocenter
	 */
	public static double orthocenterY(Point p1, Point p2, Point p3)
	{
		Line a1 = new Line(p1, new Point(footX(p1.getX(), p1.getY(), new Line(p2, p3, false)), footY(p1.getX(), p1.getY(), new Line(p2, p3, false))), false);
		Line a2 = new Line(p2, new Point(footX(p2.getX(), p2.getY(), new Line(p1, p3, false)), footY(p2.getX(), p2.getY(), new Line(p1, p3, false))), false);
		return intersectY(a1, a2);
	}
	
	/**
	 * Finds the x-coordinate of the centroid of 3 points.
	 * @param p1 first point
	 * @param p2 second point
	 * @param p3 third point
	 * @return x-coordinate of centroid
	 */
	public static double centroidX(Point p1, Point p2, Point p3)
	{
		return (p1.getX()+p2.getX()+p3.getX())/3;
	}
	
	/**
	 * Finds the y-coordinate of the centroid of 3 points.
	 * @param p1 first point
	 * @param p2 second point
	 * @param p3 third point
	 * @return y-coordinate of centroid
	 */
	public static double centroidY(Point p1, Point p2, Point p3)
	{
		return (p1.getY()+p2.getY()+p3.getY())/3;
	}

	/**
	 * Calculates the x-coordinate of the point that lies on the angle bisector of angle p1p2p3,
	 * and is 1 unit away from p2.
	 * @param p1 first point
	 * @param p2 second point (vertex of angle)
	 * @param p3 third point
	 * @return x-coordinate of point on angle bisector, 1 unit away from p2.
	 */
	public static double angleBisectorX(Point p1, Point p2, Point p3)
	{
		double d1 = dist(p1, p2);
		double d3 = dist(p2, p3);
		double x1 = (d1-1)/d1*p2.getX()+1/d1*p1.getX();
		double y1 = (d1-1)/d1*p2.getY()+1/d1*p1.getY();
		double x3 = (d3-1)/d3*p2.getX()+1/d3*p3.getX();
		double y3 = (d3-1)/d3*p2.getY()+1/d3*p3.getY();
		double x2 = (x1+x3)/2;
		double y2 = (y1+y3)/2;
		double d2 = dist(p2.getX(), p2.getY(), x2, y2);
		x2 = (d2-1)/d2*p2.getX()+1/d2*x2;
		return x2;
	}

	/**
	 * Calculates the y-coordinate of the point that lies on the angle bisector of angle p1p2p3,
	 * and is 1 unit away from p2.
	 * @param p1 first point
	 * @param p2 second point (vertex of angle)
	 * @param p3 third point
	 * @return y-coordinate of point on angle bisector, 1 unit away from p2.
	 */
	public static double angleBisectorY(Point p1, Point p2, Point p3)
	{
		double d1 = dist(p1, p2);
		double d3 = dist(p2, p3);
		double x1 = (d1-1)/d1*p2.getX()+1/d1*p1.getX();
		double y1 = (d1-1)/d1*p2.getY()+1/d1*p1.getY();
		double x3 = (d3-1)/d3*p2.getX()+1/d3*p3.getX();
		double y3 = (d3-1)/d3*p2.getY()+1/d3*p3.getY();
		double x2 = (x1+x3)/2;
		double y2 = (y1+y3)/2;
		double d2 = dist(p2.getX(), p2.getY(), x2, y2);
		y2 = (d2-1)/d2*p2.getY()+1/d2*y2;
		return y2;
	}
	
	/**
	 * Finds the x-coordinate of the point such that the line formed by the point and the given point
	 * is tangent to the circle. identifier is used to find which point (true = point more counterclockwise wrt the first circle).
	 * If it is on the circle, finds the point 1 unit away and in the positive x direction from the given point.
	 * If it is inside the circle, returns Double.POSITIVE_INFINITY.
	 * @param x x-coordinate
	 * @param y y-coordinate
	 * @param c circle
	 * @param identifier distinguishes between the possibly 2 different points
	 * @return x coordinate of the point described above
	 */
	public static double tangentX(double x, double y, Circle c, boolean identifier)
	{
		if(equal(dist(c.getCenterX(), c.getCenterY(), x, y), c.getRadius()))
		{
			// translate the circle to be centered at the origin
			double translationX = x - c.getCenterX(), translationY = - y + c.getCenterY();
			
			// if it is a vertical line
			if(translationY == 0)
			{
				return x;
			}
			 
			double m = -translationX / translationY;
			
			return x + 1 / (m * m + 1);
		}
		else if(dist(c.getCenterX(), c.getCenterY(), x, y) < c.getRadius())
		{
			return Double.POSITIVE_INFINITY;
		}
		
		Circle dCirc = new Circle(new Point((x + c.getCenterX()) / 2, (y + c.getCenterY()) / 2), new Point(x, y));
		
		return intersectX(dCirc, c, identifier);
	}
	
	/**
	 * Finds the y-coordinate of the point such that the line formed by the point and the given point
	 * is tangent to the circle. identifier is used to find which point (true = point more counterclockwise wrt the first circle).
	 * If it is on the circle, finds the point 1 unit away and in the positive y direction from the given point.
	 * If it is a vertical line, returns 1 unit down.
	 * If it is inside the circle, returns Double.POSITIVE_INFINITY.
	 * @param x x-coordinate
	 * @param y y-coordinate
	 * @param c circle
	 * @param identifier distinguishes between the possibly 2 different points
	 * @return x coordinate of the point described above
	 */
	public static double tangentY(double x, double y, Circle c, boolean identifier)
	{
		if(equal(dist(c.getCenterX(), c.getCenterY(), x, y), c.getRadius()))
		{
			// translate the circle to be centered at the origin
			double translationX = x - c.getCenterX(), translationY = - y + c.getCenterY();
			
			// if it is a vertical line
			if(translationY == 0)
			{
				return y - 1;
			}
			 
			double m = -translationX / translationY;
			
			return y - m / (m * m + 1);
		}
		else if(dist(c.getCenterX(), c.getCenterY(), x, y) < c.getRadius())
		{
			return Double.POSITIVE_INFINITY;
		}
		
		Circle dCirc = new Circle(new Point((x + c.getCenterX()) / 2, (y + c.getCenterY()) / 2), new Point(x, y));
		
		return intersectY(dCirc, c, identifier);
	}
	
	/**
	 * Finds the x-coordinate of the center of the circle tangent to the two given non-intersecting circles going through the given point.
	 * identifier = true means it is internally tangent to one of the circles and externally tangent to another.
	 * If such a circle doesn't exist, returns Double.POSITIVE_INFINITY
	 * @param c1 first circle
	 * @param c2 second circle
	 * @param p point
	 * @param identifier distinguishes between the possibly 2 different circles
	 * @return x-coordinate of the described circle.
	 */
	public static double tangentCircleX(Circle c1, Circle c2, Point p, boolean identifier)
	{
		if(equal(distToShape(c2.getCenterX(), c2.getCenterY(), p), c2.getRadius()))
		{
			return tangentCircleX(c2, c1, p, identifier);
		}
		double dx = c1.getCenterX() - p.getX();
		double dy = c1.getCenterY() - p.getY();
		Point F = new Point(p.getX() + (identifier ? -1 : 1) * c2.getRadius() * dx / Math.sqrt(dx * dx + dy * dy), p.getY() + (identifier ? -1 : 1) * c2.getRadius() * dy / Math.sqrt(dx * dx + dy * dy));
		return intersectX(new Line(new Point(c1.getCenterX(), c1.getCenterY()), p, false), new Line(new Point(c2.getCenterX(), c2.getCenterY()), F));
	}
	
	/**
	 * Finds the y-coordinate of the center of the circle tangent to the two given non-intersecting circles going through the given point.
	 * identifier = true means it is internally tangent to one of the circles and externally tangent to another.
	 * If such a circle doesn't exist, returns Double.POSITIVE_INFINITY
	 * @param c1 first circle
	 * @param c2 second circle
	 * @param p point
	 * @param identifier distinguishes between the possibly 2 different circles
	 * @return y-coordinate of the described circle.
	 */
	public static double tangentCircleY(Circle c1, Circle c2, Point p, boolean identifier)
	{
		if(equal(distToShape(c2.getCenterX(), c2.getCenterY(), p), c2.getRadius()))
		{
			return tangentCircleY(c2, c1, p, identifier);
		}
		double dx = c1.getCenterX() - p.getX();
		double dy = c1.getCenterY() - p.getY();
		Point F = new Point(p.getX() + (identifier ? -1 : 1) * c2.getRadius() * dx / Math.sqrt(dx * dx + dy * dy), p.getY() + (identifier ? -1 : 1) * c2.getRadius() * dy / Math.sqrt(dx * dx + dy * dy));
		return intersectY(new Line(new Point(c1.getCenterX(), c1.getCenterY()), p, false), new Line(new Point(c2.getCenterX(), c2.getCenterY()), F));
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
	
	/**
	 * This is the equivalent of the sgn* function. Returns -1 if d < 0, 1 otherwise. (In particular, signumstar(0)=1)
	 * @param d double to take sgn* of
	 * @return sgn*(d)
	 */
	public static double signumstar(double d)
	{
		if(d < 0) return -1;
		return 1;
	}
	
	/**
	 * Whether the two doubles are equal (since rounding errors exist)
	 * @param d1 first double
	 * @param d2 second double
	 * @return whether |d1-d2| < 0.0001 = epsilon
	 */
	public static boolean equal(double d1, double d2)
	{
		double epsilon = 0.001;
		return Math.abs(d1 - d2) < epsilon;
	}	
}
