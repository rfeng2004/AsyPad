package asypad.ui.command;

import asypad.shapes.*;
import asypad.ui.AsyPadPane;
import javafx.scene.paint.Color;

/**
 * This class represents the action of recoloring a shape.
 * @author Raymond Feng
 */
public class ColorCommand extends Command
{
	/**
	 * Point that will be moved by this command.
	 */
	private Shape shape;

	/**
	 * New color of the shape.
	 */
	private Color newColor;

	/**
	 * Creates a new ColorCommand.
	 * @param s Shape that will be recolored
	 * @param c new color of the Shape
	 */
	public ColorCommand(Shape s, Color c)
	{
		shape = s;
		newColor = c;
	}

	public void doAction(AsyPadPane target)
	{
		shape.setColor(newColor);
	}

	public String toString()
	{
		String rgb = Utility.hex(newColor);
		String s = "color(" + shape.getName() + ", " 
				+ rgb + ")\n";
		return s;
	}
}
