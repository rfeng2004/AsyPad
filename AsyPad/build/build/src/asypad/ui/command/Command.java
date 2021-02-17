package asypad.ui.command;

import asypad.shapes.Point;
import asypad.shapes.Shape;
import asypad.ui.AsyPadPane;
import javafx.scene.paint.Color;

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
		//other than color, draw, drag, hide, and globalvar,
		//these are legacy command loads (deprecated as of v2)
		if(command.startsWith("color"))
		{
			int comma = command.indexOf(',');
			String name = command.substring(6, comma);
			String color = command.substring(comma+2, command.length()-1);
			Color c = Color.web(color);
			load = new ColorCommand(target.findShapeByName(name), c);
		}
		else if(command.startsWith("draw"))
		{
			String args = command.substring(5, command.length()-1);
			Shape s = Shape.buildShape(args, target);
			load = new DrawCommand(s);
		}
		else if(command.startsWith("delete")) //deprecated as of v2
		{
			String name = command.substring(7, command.length()-1);
			load = new DeleteCommand(target.findShapeByName(name));
		}
		else if(command.startsWith("drag"))
		{
			int comma = command.indexOf(',');
			String name = command.substring(5, comma);
			double dir = Double.parseDouble(command.substring(comma+2, command.length()-1));
			load = new DragCommand((Point) target.findShapeByName(name), dir);
		}
		else if(command.startsWith("hide"))
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
		else if(command.startsWith("move")) //deprecated as of v2
		{
			int comma1 = command.indexOf(','), comma2 = command.indexOf(',', comma1+1);
			String name = command.substring(5, comma1);
			String movex = command.substring(comma1+2, comma2);
			String movey = command.substring(comma2+2, command.length()-1);
			double x = Double.parseDouble(movex);
			double y = Double.parseDouble(movey);
			load = new MoveCommand((Point) target.findShapeByName(name), x, y);
		}
		else if(command.startsWith("rename")) //deprecated as of v2
		{
			int comma = command.indexOf(',');
			String name = command.substring(7, comma);
			String newName = command.substring(comma+2, command.length()-1);
			load = new RenameCommand((Point) target.findShapeByName(name), newName);
		}
		else if(command.startsWith("globalvar"))
		{
			int equal = command.indexOf('=');
			String varName = command.substring(10, equal);
			String newValueString = command.substring(equal+1, command.length()-1);
			double newValue = Double.parseDouble(newValueString);
			load = new GlobalVariableCommand(varName, newValue);
		}
		else if(command.startsWith("translate")) //deprecated as of v2
		{
			int comma = command.indexOf(',');
			double dx = Double.parseDouble(command.substring(10, comma));
			double dy = Double.parseDouble(command.substring(comma+2, command.length()-1));
			load = new TranslateCommand(dx, dy);
		}
		else if(command.startsWith("zoom")) //deprecated as of v2
		{
			int comma1 = command.indexOf(',');
			int comma2 = command.indexOf(',', comma1+1);
			double zx = Double.parseDouble(command.substring(5, comma1));
			double zy = Double.parseDouble(command.substring(comma1+2, comma2));
			double factor = Double.parseDouble(command.substring(comma2+2, command.length()-1));
			load = new ZoomCommand(zx, zy, factor);
		}
		else
		{
			//safety to avoid crashing
			return;
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
