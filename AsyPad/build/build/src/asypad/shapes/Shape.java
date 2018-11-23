package asypad.shapes;

import java.text.DecimalFormat;
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
	 * Infinite that is used for drawing lines that should extend to infinity.
	 */
	public static final double INF = 2000;
	
	/**
	 * The formatter used to round to 2 decimal places for file i/o.
	 */
	public static final DecimalFormat FORMATTER = new DecimalFormat("#0.00");
	
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
	 * If this shape should be hidden.
	 */
	protected boolean hide;
	
	/**
	 * If this shape's code should be added to the generated Asymptote file.
	 */
	protected boolean inAsyCode;
	
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
		label = new Label();
		level = -1;
		for(Shape s : shapes)
		{
			dependencies.add(s);
			s.getChildren().add(this);
			level = Math.max(level, s.getLevel());
		}
		level++;
		remove = false;
		hide = false;
		inAsyCode = true;
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
	 * Adds this shape and all children to Asymptote code.
	 */
	public void addToAsy()
	{
		inAsyCode = true;
		for(Shape s : children)
		{
			s.addToAsy();
		}
	}
	
	/**
	 * Removes this shape and all children from Asymptote code.
	 */
	public void removeFromAsy()
	{
		inAsyCode = false;
		for(Shape s : children)
		{
			s.removeFromAsy();
		}
	}
	
	/**
	 * Sets whether the shape should be hidden.
	 * @param hidden if the shape should be hidden
	 */
	public void setHidden(boolean hidden)
	{
		hide = hidden;
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
	 * If this shape should be hidden.
	 * @return hide
	 */
	public boolean isHidden()
	{
		return hide;
	}
	
	/**
	 * If this shape is in the Asymptote code.
	 * @return inAsyCode
	 */
	public boolean isInAsyCode()
	{
		return inAsyCode;
	}
	
	/**
	 * Renames the shape.
	 * @param name new name
	 */
	public void setName(String name)
	{
		label.setText(name);
	}
	
	/**
	 * Returns the name of the shape.
	 * @return the name of the shape
	 */
	public String getName()
	{
		return label.getText();
	}
	
	/**
	 * Returns the shapes's label.
	 * @return the shape label
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
	 * Refreshes shape and all of its children.
	 */
	public abstract void refresh();
	
	/**
	 * Refreshes the name of this shape and all of its children.
	 */
	public abstract void refreshName();
	
	/**
	 * Returns the underlying shape that is drawn onto the screen.
	 * @return the underlying shape
	 */
	public abstract javafx.scene.shape.Shape getObject();
	
	/**
	 * String representation of the shape. Should only be used for debugging purposes.
	 * @return string representation
	 */
	public abstract String toString();
	
	/**
	 * Converts this shape into Asymptote code.
	 * @return Asymptote representation.
	 */
	public abstract String toAsymptote();
}
