package asypad.ui.command;

import asypad.ui.AsyPadPane;

/**
 * This class stores a user command.
 * @author Raymond Feng
 */
public abstract class Command
{
	/**
	 * Carries out the command.
	 * @param target AsyPadPane where the command should be performed.
	 */
	public abstract void doAction(AsyPadPane target);
}
