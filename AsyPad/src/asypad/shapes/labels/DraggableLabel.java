package asypad.shapes.labels;

import asypad.shapes.*;
import javafx.scene.control.Label;

/**
 * A draggable label used to label shapes in AsyPad.
 * @author Raymond Feng
 */
public class DraggableLabel extends Label
{
	/**
	 * This label's associated shape.
	 */
	private Shape shape;

	/**
	 * This label's direction from the shape.
	 */
	private double direction;

	/**
	 * Creates a new DraggableLabel associated with s at direction dir.
	 * @param s associated shape
	 * @param dir direction of label from shape
	 */
	public DraggableLabel(Shape s, double dir)
	{
		super();
		shape = s;
		direction = dir;
	}

	/**
	 * Sets the direction of the label, in radians.
	 * @param dir direction in radians
	 */
	public void setDirection(double dir)
	{
		direction = dir;
		refresh();
	}
	
	/**
	 * Return's the direction of the label.
	 * @return direction
	 */
	public double getDirection()
	{
		return direction;
	}

	/**
	 * Refreshes the position of the label.
	 */
	public void refresh()
	{
		if(shape instanceof Point)
		{
			Point p = (Point) shape;
			setLayoutX(p.getX()+5*Shape.StrokeWidth*Math.cos(direction)+4*getPrefWidth());
			setLayoutY(p.getY()-5*Shape.StrokeWidth*Math.sin(direction)+8*getPrefHeight());
		}
	}
}
