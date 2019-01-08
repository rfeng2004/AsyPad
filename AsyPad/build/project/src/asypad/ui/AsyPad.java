/*
 * AsyPad: A simple drawing tool that can convert diagrams into Asymptote code.
 * For more information visit: https://github.com/rfeng2004/AsyPad
 * 
 * Copyright (C) 2018 Raymond Feng
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see https://www.gnu.org/licenses/.
 */
package asypad.ui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import java.io.File;

/**
 * AsyPad: A simple drawing tool that can convert diagrams into Asymptote code.
 * @author Raymond Feng
 * @version 1.0
 */
public class AsyPad extends Application
{
	/**
	 * Version number of AsyPad.
	 */
	public static final String VERSION = "1.2";

	/**
	 * Operating system the AsyPad is running on.
	 */
	public static final String OS = System.getProperty("os.name");

	/**
	 * The main component of the AsyPad Application.
	 */
	private AsyPadPane rootNode;

	/**
	 * Creates a new AsyPad Application that can handle .apad file opening (for Mac).
	 */
	public AsyPad()
	{
		rootNode = new AsyPadPane();
		if(OS != null && OS.startsWith("Mac"))
		{
			com.sun.glass.ui.Application glassApp = com.sun.glass.ui.Application.GetApplication();
			glassApp.setEventHandler(new com.sun.glass.ui.Application.EventHandler()
			{
				public void handleOpenFilesAction(com.sun.glass.ui.Application app, long time, String[] filenames)
				{
					super.handleOpenFilesAction(app, time, filenames);
					if(filenames[0].endsWith(".apad"))
					{
						rootNode.loadApad(new File(filenames[0]));
					}
				}
			});
		}
	}

	public static void main(String args[])
	{
		launch(args);
	}

	public void start(Stage primaryStage)
	{
		//file handling for windows
		if(OS != null && OS.startsWith("Windows"))
		{
			String filename = getParameters().getRaw().toString();
			filename = filename.substring(1, filename.length()-1); //trim off the brackets
			if(filename.endsWith(".apad"))
			{
				rootNode.loadApad(new File(filename));
			}
		}
		
		Scene scene = new Scene(rootNode, 1000, 700);
		primaryStage.getIcons().add(new Image("resources/AsyPad.icns"));
		primaryStage.setTitle("AsyPad");
		primaryStage.setScene(scene);
		primaryStage.show();
	}
}