package asypad.ui.command;

import asypad.shapes.Point;
import asypad.shapes.Shape;
import asypad.ui.AsyPadPane;

/**
 * This class stores a user command.
 * @author Raymond Feng
 */
public abstract class Command
{
	/**
	 * Executes a Command from a String and adds it to the target AsyPadPane. 
	 * This is used for file i/o.
	 * @param command string representation of command
	 * @param target target AsyPadPane that the command will be for
	 */
	public static void loadCommand(String command, AsyPadPane target)
	{
		Command load = null;
		if(command.substring(0, 4).equals("draw"))
		{
			String args = command.substring(5, command.length()-1);
			Shape s = Shape.buildShape(args, target);
			if(s instanceof Point)
			{
				Point p = (Point) s;
				load = new DrawCommand(p, p.getX(), p.getY(), p.getName());
			}
			else
			{
				load = new DrawCommand(s);
			}
		}
		else if(command.substring(0, 6).equals("delete"))
		{
			String name = command.substring(7, command.length()-1);
			load = new DeleteCommand(target.findShapeByName(name));
		}
		else if(command.substring(0, 4).equals("hide"))
		{
			String name = command.substring(5, command.length()-1);
			if(name.equals("all"))
			{
				load = new HideCommand();
			}
			else
			{
				load = new HideCommand(target.findShapeByName(name));
			}
		}
		else if(command.substring(0, 4).equals("move"))
		{
			int comma1 = 0, comma2 = 0;
			for(int i = 5; i < command.length(); i++)
			{
				if(command.charAt(i) == ',')
				{
					if(comma1 == 0) comma1 = i;
					else
					{
						comma2 = i;
						break;
					}
				}
			}
			String name = command.substring(5, comma1);
			String movex = command.substring(comma1+2, comma2);
			String movey = command.substring(comma2+2, command.length()-1);
			double x = Double.parseDouble(movex);
			double y = Double.parseDouble(movey);
			load = new MoveCommand((Point) target.findShapeByName(name), x, y);
		}
		else if(command.substring(0, 6).equals("rename"))
		{
			int comma = 0;
			for(int i = 7; i < command.length(); i++)
			{
				if(command.charAt(i) == ',')
				{
					comma = i;
					break;
				}
			}
			String name = command.substring(7, comma);
			String newName = command.substring(comma+2, command.length()-1);
			load = new RenameCommand((Point) target.findShapeByName(name), newName);
		}
		else if(command.substring(0, 5).equals("stroke"))
		{
			String newStrokeWidth = command.substring(6, command.length()-1);
			double stroke = Double.parseDouble(newStrokeWidth);
			load = new StrokeWidthCommand(stroke);
		}
		target.addCommand(load);
		load.doAction(target);
	}

	/**
	 * Carries out the command.
	 * @param target AsyPadPane where the command should be performed.
	 */
	public abstract void doAction(AsyPadPane target);
}
