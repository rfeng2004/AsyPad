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
	INCIRCLE,
	/**
	 * Circle tangent to two other circles. Either both externally or both internally tangent.
	 */
	TANGENT_CIRCLE;
}
