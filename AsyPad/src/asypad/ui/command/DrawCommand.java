package asypad.ui.command;

import asypad.shapes.*;
import asypad.shapes.types.POINT_TYPE;
import asypad.ui.AsyPadPane;
import javafx.scene.paint.Color;

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
		if(shape instanceof Point)
		{
			Point p = (Point) shape;
			defaultX = p.getX();
			defaultY = p.getY();
			defaultName = p.getName();
		}
		else
		{
			defaultX = -1;
			defaultY = -1;
			defaultName = "";
		}
	}

	public void doAction(AsyPadPane target)
	{
		if(defaultName == "")
		{
			shape.setRemove(false);
			shape.setHidden(false);
			shape.setColor(Color.BLACK);
			target.addShape(shape, true);
		}
		else
		{
			Point p = (Point) shape;
			p.setX(defaultX);
			p.setY(defaultY);
			if(p.getType() == POINT_TYPE.POINT_ON_SHAPE)
			{
				//reset relative location for a point on shape
				p.setRelativeLocation(defaultX, defaultY);
			}
			p.setName(defaultName);
			p.setRemove(false);
			p.setHidden(false);
			p.setColor(Color.BLACK);
			p.getLabel().setDirection(-Math.PI/4);
			target.addShape(p, true);
		}
	}

	public String toString()
	{
		String s = "draw(" + shape.toString() + ")\n";
		return s;
	}
}
