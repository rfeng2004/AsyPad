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
		DELETE;
	};
}
