package asypad.ui.command;

import asypad.shapes.*;
import asypad.ui.AsyPadPane;

/**
 * This class represents a command where the user draws a shape onto the screen.
 * @author Raymond Feng
 */
public class DrawCommand extends Command
{
	/**
	 * Shape that this command will draw.
	 */
	private Shape shape;
	private double defaultX;
	private double defaultY;
	private String defaultName;

	/**
	 * Creates a new DrawCommand.
	 * @param shape shape that the command will draw
	 */
	public DrawCommand(Shape shape)
	{
		this.shape = shape;
		defaultX = -1;
		defaultY = -1;
		defaultName = "";
	}

	/**
	 * Creates a new DrawCommand specialized for a point.
	 * @param point point to be drawn
	 * @param x default x-coordinate
	 * @param y default y-coordinate
	 * @param name default name
	 */
	public DrawCommand(Point point, double x, double y, String name)
	{
		this.shape = point;
		defaultX = x;
		defaultY = y;
		defaultName = name;
	}

	public void doAction(AsyPadPane target)
	{
		if(defaultName == "")
		{
			shape.setRemove(false);
			shape.setHidden(false);
			target.addShape(shape);
		}
		else
		{
			Point p = (Point) shape;
			p.setX(defaultX);
			p.setY(defaultY);
			p.setName(defaultName);
			p.setRemove(false);
			p.setHidden(false);
			target.addShape(p);
		}
	}
	
	public String toString()
	{
		String s = "draw(" + shape.toString() + ")\n";
		return s;
	}
}
