package asypad.ui.command;

import asypad.shapes.Shape;
import asypad.ui.AsyPadPane;

/**
 * This class represents a command where a shape is deleted.
 * @author Raymond Feng
 */
public class DeleteCommand extends Command
{
	/**
	 * Shape that is to be deleted.
	 */
	private Shape shape;
	
	/**
	 * Creates a new DeleteCommand.
	 * @param shape shape that is to be deleted
	 */
	public DeleteCommand(Shape shape)
	{
		this.shape = shape;
	}
	
	public void doAction(AsyPadPane target)
	{
		shape.delete();
		target.update();
	}
}
