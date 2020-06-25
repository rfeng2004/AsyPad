package asypad.ui.command;

import asypad.ui.AsyPadPane;

/**
 * This class represents a translation.
 * @author Raymond Feng
 */
public class TranslateCommand extends Command
{
	/**
	 * Translation amounts in x and y directions.
	 */
	private double dx, dy;
	
	/**
	 * Creates a new TranslateCommand with the specified translations in the x and y directions.
	 * @param dx amount in x direction
	 * @param dy amount in y direction
	 */
	public TranslateCommand(double dx, double dy)
	{
		this.dx = dx;
		this.dy = dy;
	}
	
	public void doAction(AsyPadPane target)
	{
		target.translate(dx, dy);
	}
	
	public String toString()
	{
		return "translate(" + dx + ", " + dy + ")";
	}
}
