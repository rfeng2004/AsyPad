package asypad.ui.command;

import asypad.shapes.Shape;
import asypad.ui.AsyPadPane;

/**
 * This class represents a command where shapes are hidden/shown.
 * @author Raymond Feng
 */
public class HideCommand extends Command
{
	/**
	 * This hide command's target, if it exists.
	 */
	private Shape shape;
	
	/**
	 * Whether this hide command represents a "show all" command.
	 */
	private boolean showAll;
	
	/**
	 * Creates a HideCommand that hides a shape.
	 * @param shape shape to hide
	 */
	public HideCommand(Shape shape)
	{
		this.shape = shape;
		showAll = false;
	}

	/**
	 * Creates a HideCommand that shows all hidden shapes.
	 */
	public HideCommand()
	{
		showAll = true;
	}

	public void doAction(AsyPadPane target)
	{
		if(!showAll)
		{
			shape.setHidden(true);
		}
		else
		{
			for(Shape s : target.getShapes())
			{
				s.setHidden(false);
			}
			target.update();
		}
	}
	
	public String toString()
	{
		String s;
		if(!showAll)
		{
			s = "hide(" + shape.getName() + ")\n";
		}
		else
		{
			s = "hide(all)\n";
		}
		return s;
	}
}
