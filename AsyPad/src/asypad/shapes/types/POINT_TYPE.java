package asypad.shapes.types;

/**
 * Types of points.
 * @author Raymond Feng
 */
public enum POINT_TYPE implements SHAPE_TYPE
{
	/**
	 * A point specified by x and y coordinate.
	 */
	POINT,
	/**
	 * A point that is locked onto a specific shape.
	 */
	POINT_ON_SHAPE,
	/**
	 * A point specified by the intersection of 2 given shapes.
	 */
	INTERSECTION_POINT,
	/**
	 * The midpoint of 2 given points.
	 */
	MIDPOINT,
	/**
	 * Point set relative to 2 given points by a user supplied ratio.
	 */
	RELATIVE_POINT;
}
