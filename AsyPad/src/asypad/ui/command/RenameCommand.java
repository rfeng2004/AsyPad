package asypad.ui.command;

import asypad.shapes.Point;
import asypad.ui.AsyPadPane;

/**
 * This class represents a command where a point is renamed.
 * @author Raymond Feng
 */
public class RenameCommand extends Command
{
	/**
	 * The Point that is renamed.
	 */
	private Point rename;
	
	/**
	 * The new name of the Point.
	 */
	private String newName;
	
	/**
	 * Creates a new RenameCommand.
	 * @param rename Point to be renamed
	 * @param newName new name of the Point
	 */
	public RenameCommand(Point rename, String newName)
	{
		this.rename = rename;
		this.newName = newName;
	}

	public void doAction(AsyPadPane target) 
	{
		rename.setName(newName);
		rename.refreshName();
	}
	
	public String toString()
	{
		String s = "rename(" + rename.getName() + ", " + newName + ")\n";
		return s;
	}
}
