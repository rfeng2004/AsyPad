package asypad.ui.command;

import asypad.ui.AsyPadPane;

/**
 * This class represents a zooming action.
 * @author Raymond Feng
 */
public class ZoomCommand extends Command
{
	/**
	 * Coordinates of center of zoom.
	 */
	private double zx, zy;
	
	/**
	 * Zooming scale factor.
	 */
	private double factor;
	
	/**
	 * Creates a new ZoomCommand with the specified center and scale factor.
	 * @param zx x coordinate of center
	 * @param zy y coordinate of center
	 * @param factor scale factor
	 */
	public ZoomCommand(double zx, double zy, double factor)
	{
		this.zx = zx;
		this.zy = zy;
		this.factor = factor;
	}
	
	public void doAction(AsyPadPane target)
	{
		target.zoom(zx, zy, factor);
	}
	
	public String toString()
	{
		return "zoom(" + zx + ", " + zy + ", " + factor + ")";
	}
}
