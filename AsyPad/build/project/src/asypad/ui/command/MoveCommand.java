package asypad.ui.command;

import asypad.shapes.Point;
import asypad.shapes.types.POINT_TYPE;
import asypad.ui.AsyPadPane;

/**
 * This class represents a command where a point is moved by the user.
 * @author Raymond Feng
 */
public class MoveCommand extends Command
{
	/**
	 * Point that will be moved by this command.
	 */
	private Point move;
	
	/**
	 * The x-coordinate that the point will be moved to.
	 */
	private double x;
	
	/**
	 * The y-coordinate that the point will be moved to.
	 */
	private double y;
	
	/**
	 * Creates a new MoveCommand.
	 * @param move Point that will be moved by this command
	 * @param x x-coordinate that the point will be moved to
	 * @param y y-coordinate that the point will be moved to
	 */
	public MoveCommand(Point move, double x, double y)
	{
		this.move = move;
		this.x = x;
		this.y = y;
	}
	
	public void doAction(AsyPadPane target)
	{
		if(move.getType() == POINT_TYPE.POINT)
		{
			move.setX(x);
			move.setY(y);
		}
		else if(move.getType() == POINT_TYPE.POINT_ON_SHAPE)
		{
			move.setRelativeLocation(x, y);
		}
	}
}
