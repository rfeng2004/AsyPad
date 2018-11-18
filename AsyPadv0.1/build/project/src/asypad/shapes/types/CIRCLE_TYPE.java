package asypad.shapes.types;

/**
 * Types of circles.
 * @author Raymond Feng
 */
public enum CIRCLE_TYPE implements SHAPE_TYPE
{
	/**
	 * Circle specified by center and a point on the circle.
	 */
	CIRCLE,
	/**
	 * Circumcircle of the triangle formed by three given points.
	 */
	CIRCUMCIRCLE,
	/**
	 * Incircle of the triangle formed by three given points.
	 */
	INCIRCLE;
}
