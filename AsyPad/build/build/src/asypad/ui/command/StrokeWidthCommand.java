package asypad.ui.command;

import asypad.shapes.Shape;
import asypad.ui.AsyPadPane;

/**
 * This class represents a command where the Stroke Width is updated.
 * @author Raymond Feng
 */
public class StrokeWidthCommand extends Command
{
	/**
	 * The new Stroke Width specified by the user.
	 */
	private double newStrokeWidth;
	
	/**
	 * Creates a new StrokeWidthCommand.
	 * @param newStrokeWidth the new stroke width
	 */
	public StrokeWidthCommand(double newStrokeWidth)
	{
		this.newStrokeWidth = newStrokeWidth;
	}
	
	public void doAction(AsyPadPane target)
	{
		Shape.StrokeWidth = newStrokeWidth;
		target.update();
	}
}
