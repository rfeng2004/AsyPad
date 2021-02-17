package asypad.ui.command;

import asypad.shapes.Shape;
import asypad.ui.AsyPadPane;

/**
 * This class represents a command where the Stroke Width is updated.
 * @author Raymond Feng
 */
public class GlobalVariableCommand extends Command
{
	/**
	 * The global variable to update.
	 */
	private String varName;
	
	/**
	 * The new value specified by the user.
	 */
	private double newValue;
	
	/**
	 * Creates a new GlobalVariableCommand.
	 * @param newValue the new value
	 */
	public GlobalVariableCommand(String varName, double newValue)
	{
		this.varName = varName;
		this.newValue = newValue;
	}
	
	public void doAction(AsyPadPane target)
	{
		if(varName.equals("StrokeWidth"))
		{
			Shape.StrokeWidth = newValue;
		}
		else if(varName.equals("AsyUnitSize"))
		{
			AsyPadPane.AsyUnitSize = newValue;
		}
		target.update();
	}
	
	public String toString()
	{
		String s = "globalvar(" + varName + "=" + newValue + ")\n";
		return s;
	}
}
