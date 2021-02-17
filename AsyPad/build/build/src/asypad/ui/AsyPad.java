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
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.File;
import java.util.Optional;

/**
 * AsyPad: A simple drawing tool that can convert diagrams into Asymptote code.
 * @author Raymond Feng
 * @version 2.0
 */
public class AsyPad extends Application
{
	/**
	 * Version number of AsyPad.
	 */
	public static final String VERSION = "2.0";

	/**
	 * Operating system the AsyPad is running on.
	 */
	public static final String OS = System.getProperty("os.name");
	
	/**
	 * Default width of application window.
	 */
	public static final double DEFAULT_WIDTH = 1000;
	
	/**
	 * Default height of application window.
	 */
	public static final double DEFAULT_HEIGHT = 700;

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
		if(args.length >= 2 && args[0].equals("conv"))
		{
			if(args.length == 2 && args[1].endsWith(".apad"))
			{
				AsyPadPane converter = new AsyPadPane();
				converter.loadApad(new File(args[1]));
				String latex = "\\begin{center}\n\\begin{asy}\n" + converter.toAsymptote() + "\n\\end{asy}\n\\end{center}";
				System.out.println(latex);
			}
			else throw new AsyPadException("Incorrect arguments.");
		}
		else
		{
			launch(args);
		}
		System.exit(0);
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

		//close confirmation
		primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>()
		{
			public void handle(WindowEvent event)
			{
				Alert alert = new Alert(AlertType.CONFIRMATION);
				alert.setTitle("Quit AsyPad");
				alert.setHeaderText("Are you sure you want to quit?");
				alert.setContentText("Unsaved changes will be lost.");

				Image icon = new Image("resources/AsyPad.icns");
				ImageView iconGraphic = new ImageView(icon);
				iconGraphic.setFitHeight(50);
				iconGraphic.setFitWidth(50);

				alert.setGraphic(iconGraphic);
				alert.getGraphic().setOnMousePressed(new EventHandler<MouseEvent>()
				{
					public void handle(MouseEvent event)
					{
						rootNode.showAsyPanel();
					}
				});
				alert.initOwner(primaryStage);
				Optional<ButtonType> res = alert.showAndWait();

				if(res.isPresent())
				{
					if(res.get().equals(ButtonType.CANCEL))
					{
						event.consume();
					}
				}
			}
		});

		Scene scene = new Scene(rootNode, DEFAULT_WIDTH, DEFAULT_HEIGHT);
		primaryStage.getIcons().add(new Image("resources/AsyPad.icns"));
		primaryStage.setTitle("AsyPad");
		primaryStage.setScene(scene);
		primaryStage.show();
	}
}