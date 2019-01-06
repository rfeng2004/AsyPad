package asypad.shapes.types;

/**
 * Types of lines.
 * @author Raymond Feng
 */
public enum LINE_TYPE implements SHAPE_TYPE
{
	/**
	 * Segment through 2 given points.
	 */
	SEGMENT,
	/**
	 * Line through 2 given points.
	 */
	LINE,
	/**
	 * Line through a given point parallel to a given line.
	 */
	PARALLEL_LINE,
	/**
	 * Line through a given point and perpendicular to a given line.
	 */
	PERPENDICULAR_LINE,
	/**
	 * Angle bisector of an angle specified by 3 given points.
	 */
	ANGLE_BISECTOR,
	/**
	 * Perpendicular bisector of the segment specified by 2 given points.
	 */
	PERPENDICULAR_BISECTOR,
	/**
	 * Line tangent to a circle at a point.
	 */
	TANGENT_LINE,
}
