package asypad.shapes.types;

/**
 * Types of shapes.
 * @author Raymond Feng
 */
public interface SHAPE_TYPE 
{
	/**
	 * Types of mouses.
	 * @author Raymond Feng
	 */
	enum MOUSE implements SHAPE_TYPE
	{
		/**
		 * This is the default selected tool.
		 */
		MOUSE,
		/**
		 * This tool allows the user to delete shapes from the screen.
		 */
		DELETE,
		/**
		 * This tool allows the user to drag the labels of points.
		 */
		DRAG;
	};
}
