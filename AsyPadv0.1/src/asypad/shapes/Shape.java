package asypad.shapes;

import java.util.ArrayList;

import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import asypad.shapes.types.SHAPE_TYPE;
/**
 * Custom Shape class that is a superclass for all shapes drawn on AsyPad.
 * @author Raymond Feng
 */
public abstract class Shape
{
	/**
	 * Stroke width of shapes.
	 */
	public static double StrokeWidth = 3;
	/**
	 * The shapes that this shape depends on.
	 */
	protected ArrayList<Shape> dependencies;
	/**
	 * The shapes that depend on this shape.
	 */
	protected ArrayList<Shape> children;
	/**
	 * If this shape should be removed.
	 */
	protected boolean remove;
	/**
	 * The shape's label.
	 */
	protected Label label;
	/**
	 * The type of this shape.
	 */
	protected SHAPE_TYPE type;
	/**
	 * The dependency level of this shape.
	 */
	protected int level;
	
	/**
	 * Default superclass constructor called by all shapes, initializes dependencies, children, and level.
	 * @param shapes dependencies
	 */
	public Shape(Shape... shapes)
	{
		dependencies = new ArrayList<Shape>();
		children = new ArrayList<Shape>();
		level = -1;
		for(Shape s : shapes)
		{
			dependencies.add(s);
			s.getChildren().add(this);
			level = Math.max(level, s.getLevel());
		}
		level++;
		remove = false;
	}
	
	/**
	 * Dependencies of this shape.
	 * @return dependencies
	 */
	public ArrayList<Shape> getDependencies()
	{
		return dependencies;
	}
	/**
	 * Children of this shape.
	 * @return children
	 */
	public ArrayList<Shape> getChildren()
	{
		return children;
	}

	/**
	 * Sets remove = true for this shape and all children.
	 */
	public void delete()
	{
		remove = true;
		for(Shape s : children)
		{
			s.delete();
		}
	}
	
	/**
	 * If this shape should be removed.
	 * @return remove
	 */
	public boolean remove()
	{
		return remove;
	}
	
	/**
	 * Renames the point.
	 * @param name new name
	 */
	public void setName(String name)
	{
		label.setText(name);
	}
	
	/**
	 * Returns the object's label.
	 * @return the object label
	 */
	public Label getLabel()
	{
		return label;
	}
	
	/**
	 * Returns this shape's type.
	 * @return type
	 */
	public SHAPE_TYPE getType()
	{
		return type;
	}
	
	/**
	 * Returns this shape's level of dependency.
	 * @return level of dependency
	 */
	public int getLevel()
	{
		return level;
	}

	/**
	 * Draws this shape onto the specified pane.
	 * @param p pane that shape is to be drawn on
	 */
	public abstract void draw(Pane p);
	
	/**
	 * Refreshes shape.
	 */
	public abstract void refresh();
	
	/**
	 * Returns the underlying shape that is drawn onto the screen.
	 * @return the underlying shape
	 */
	public abstract javafx.scene.shape.Shape getObject();
	
	/**
	 * String representation of the shape.
	 * @return string representation
	 */
	public abstract String toString();
}
