package asypad.ui.command;

import asypad.shapes.Point;
import asypad.ui.AsyPadPane;

/**
 * This class represents a command where a label is repositioned by the user.
 * @author Raymond Feng
 */
public class DragCommand extends Command
{
	/**
	 * Point whose label will be moved by this command.
	 */
	private Point move;
	
	/**
	 * New direction of the label.
	 */
	private double direction;
	
	/**
	 * Creates a new DragCommand.
	 * @param move Point whose label will be moved by this command
	 * @param direction new direction of the label.
	 */
	public DragCommand(Point move, double direction)
	{
		this.move = move;
		this.direction = direction;
	}
	
	public void doAction(AsyPadPane target)
	{
		move.getLabel().setDirection(direction);
	}
	
	public String toString()
	{
		String s = "drag(" + move.getName() + ", " + direction + ")\n";
		return s;
	}
}
