package asypad.ui.menus;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import asypad.shapes.Shape;
import asypad.ui.AsyPad;
import asypad.ui.AsyPadPane;
import asypad.ui.command.*;
import javafx.event.*;
import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.scene.text.TextAlignment;
import javafx.stage.*;
import javafx.stage.FileChooser.ExtensionFilter;

/**
 * AsyPad menu bar used in the AsyPad application.
 * @author Raymond Feng
 */
public class AsyPadMenuBar extends MenuBar
{
	/**
	 * Creates a new AsyPad menu bar with specified parent.
	 * @param parent the AsyPadPane that contains this AsyPadMenuBar
	 */
	public AsyPadMenuBar(AsyPadPane parent)
	{
		super();

		//uses system menu bar on mac
		final KeyCombination.Modifier c;
		if (AsyPad.OS != null && AsyPad.OS.startsWith("Mac"))
		{
			useSystemMenuBarProperty().set(true);
			c = KeyCodeCombination.META_DOWN;
		}
		else
		{
			c = KeyCodeCombination.CONTROL_DOWN;
			setPrefSize(5000, 30);
			setLayoutX(0);
			setLayoutY(0);
		}

		//file io
		Menu file = new Menu("File");
		MenuItem saveApad = new MenuItem("Save as AsyPad File");
		MenuItem saveAsy = new MenuItem("Save as Asymptote File");
		MenuItem loadApad = new MenuItem("Load AsyPad File");

		KeyCodeCombination cs = new KeyCodeCombination(KeyCode.S, c);
		saveApad.setAccelerator(cs);
		saveApad.setOnAction(new EventHandler<ActionEvent>()
		{
			public void handle(ActionEvent event)
			{
				FileChooser saver = new FileChooser();
				saver.getExtensionFilters().add(new ExtensionFilter("AsyPad Files", "*.apad"));
				saver.setTitle("Save Diagram as AsyPad File");
				File f = saver.showSaveDialog(new Stage());
				if(f == null) return;
				try(FileWriter fw = new FileWriter(f);
						BufferedWriter bw = new BufferedWriter(fw);)
				{
					bw.write(parent.toApad());
				}
				catch(IOException ioe)
				{
					ioe.printStackTrace();
				}
			}
		});

		KeyCodeCombination shiftcs = new KeyCodeCombination(KeyCode.S, c, KeyCodeCombination.SHIFT_DOWN);
		saveAsy.setAccelerator(shiftcs);
		saveAsy.setOnAction(new EventHandler<ActionEvent>()
		{
			public void handle(ActionEvent event)
			{
				FileChooser saver = new FileChooser();
				saver.getExtensionFilters().add(new ExtensionFilter("Asymptote Files", "*.asy"));
				saver.setTitle("Save Diagram as Asymptote File");
				File f = saver.showSaveDialog(new Stage());
				if(f == null) return;
				try(FileWriter fw = new FileWriter(f);
						BufferedWriter bw = new BufferedWriter(fw);)
				{
					bw.write(parent.toAsymptote());
				}
				catch(IOException ioe)
				{
					ioe.printStackTrace();
				}
			}
		});

		KeyCodeCombination co = new KeyCodeCombination(KeyCode.O, c);
		loadApad.setAccelerator(co);
		loadApad.setOnAction(new EventHandler<ActionEvent>()
		{
			public void handle(ActionEvent event)
			{
				FileChooser opener = new FileChooser();
				opener.getExtensionFilters().add(new ExtensionFilter("AsyPad Files", "*.apad"));
				opener.setTitle("Load Diagram from AsyPad File");
				File f = opener.showOpenDialog(new Stage());
				if(f == null) return;
				parent.loadApad(f);
			}
		});

		file.getItems().addAll(saveApad, saveAsy, loadApad);

		//undo+redo
		Menu edit = new Menu("Edit");
		MenuItem undo = new MenuItem("Undo");
		KeyCodeCombination cz = new KeyCodeCombination(KeyCode.Z, c);
		undo.setAccelerator(cz);

		MenuItem redo = new MenuItem("Redo");
		KeyCodeCombination cy = new KeyCodeCombination(KeyCode.Y, c);
		redo.setAccelerator(cy);

		//MenuItem clear = new MenuItem("Clear");
		undo.setOnAction(new EventHandler<ActionEvent>()
		{
			public void handle(ActionEvent event)
			{
				parent.undo();
			}
		});
		redo.setOnAction(new EventHandler<ActionEvent>()
		{
			public void handle(ActionEvent event)
			{
				parent.redo();
			}
		});
		/*clear.setOnAction(new EventHandler<ActionEvent>()
		{
			public void handle(ActionEvent event)
			{
				parent.clear();
			}
		});*/
		edit.getItems().addAll(undo, redo/*, clear*/);

		//settings
		Menu settings = new Menu("Settings");
		MenuItem setStrokeWidth = new MenuItem("Set Stroke Width");
		MenuItem setAsyUnitSize = new MenuItem("Set Asy Unit Size");
		setStrokeWidth.setOnAction(new EventHandler<ActionEvent>()
		{
			public void handle(ActionEvent event)
			{
				Stage setSW = new Stage();
				Pane p = new Pane();
				Scene scene = new Scene(p, 250, 80);
				Label label = new Label("Set the Stroke Width: " + Shape.StrokeWidth);
				label.setPrefSize(200, 20);
				label.setLayoutX(50);
				label.setLayoutY(0);
				label.setTextAlignment(TextAlignment.CENTER);
				Slider sw = new Slider();
				sw.setOrientation(Orientation.HORIZONTAL);
				sw.setShowTickMarks(true);
				sw.setMajorTickUnit(10);
				sw.setMax(50);
				sw.setMinorTickCount(0);
				sw.setShowTickLabels(false);
				sw.setPrefSize(250, 30);
				sw.setLayoutX(0);
				sw.setLayoutY(20);
				sw.setValue(Shape.StrokeWidth*10);
				sw.valueProperty().addListener((observable, oldValue, newValue)->
				{
					int j = newValue.intValue();
					label.setText("Set the Stroke Width: " + Double.toString((double) (j)/10));
				});
				Button refresh = new Button("Refresh");
				refresh.setPrefHeight(30);
				refresh.setLayoutX(90);
				refresh.setLayoutY(50);
				refresh.setOnAction(new EventHandler<ActionEvent>()
				{
					public void handle(ActionEvent event)
					{
						Shape.StrokeWidth = (double)((int)(sw.getValue()))/10;
						parent.addCommand(new GlobalVariableCommand("StrokeWidth", Shape.StrokeWidth));
						setSW.close();
						parent.update();
					}
				});
				p.getChildren().addAll(label, sw, refresh);
				setSW.setScene(scene);
				setSW.setAlwaysOnTop(true);
				setSW.setTitle("Set Stroke Width");
				setSW.show();
			}
		});
		setAsyUnitSize.setOnAction(new EventHandler<ActionEvent>()
		{
			public void handle(ActionEvent event)
			{
				Stage setSW = new Stage();
				Pane p = new Pane();
				Scene scene = new Scene(p, 250, 80);
				Label label = new Label("Set the unit size: " + AsyPadPane.AsyUnitSize);
				label.setPrefSize(200, 20);
				label.setLayoutX(50);
				label.setLayoutY(0);
				label.setTextAlignment(TextAlignment.CENTER);
				Slider sw = new Slider();
				sw.setOrientation(Orientation.HORIZONTAL);
				sw.setShowTickMarks(true);
				sw.setMajorTickUnit(10);
				sw.setMax(50);
				sw.setMinorTickCount(0);
				sw.setShowTickLabels(false);
				sw.setPrefSize(250, 30);
				sw.setLayoutX(0);
				sw.setLayoutY(20);
				sw.setValue(AsyPadPane.AsyUnitSize*10);
				sw.valueProperty().addListener((observable, oldValue, newValue)->
				{
					int j = newValue.intValue();
					label.setText("Set the unit size: " + Double.toString((double) (j)/10));
				});
				Button update = new Button("Update");
				update.setPrefHeight(30);
				update.setLayoutX(90);
				update.setLayoutY(50);
				update.setOnAction(new EventHandler<ActionEvent>()
				{
					public void handle(ActionEvent event)
					{
						AsyPadPane.AsyUnitSize = (double)((int)(sw.getValue()))/10;
						parent.addCommand(new GlobalVariableCommand("AsyUnitSize", AsyPadPane.AsyUnitSize));
						setSW.close();
						parent.update();
					}
				});
				p.getChildren().addAll(label, sw, update);
				setSW.setScene(scene);
				setSW.setAlwaysOnTop(true);
				setSW.setTitle("Set Asy Unit Size");
				setSW.show();
			}
		});
		settings.getItems().addAll(setStrokeWidth, setAsyUnitSize);

		Menu view = new Menu("View");
		MenuItem showHidden = new MenuItem("Show Hidden Shapes");
		MenuItem showAsyPanel = new MenuItem("Show Asy Panel");
		showHidden.setOnAction(new EventHandler<ActionEvent>()
		{
			public void handle(ActionEvent event)
			{
				for(Shape s : parent.getShapes())
				{
					s.setHidden(false);
				}
				parent.addCommand(new HideCommand());
				parent.update();
			}
		});
		showAsyPanel.setOnAction(new EventHandler<ActionEvent>()
		{
			public void handle(ActionEvent event)
			{
				parent.showAsyPanel();
			}
		});
		view.getItems().addAll(showHidden, showAsyPanel);

		//help menu (about and credits)
		Menu help = new Menu("Help");
		Menu about = new Menu("About");
		MenuItem aboutAsyPad = new MenuItem("About AsyPad");
		MenuItem credits = new MenuItem("Credits");

		//about the program (description)
		aboutAsyPad.setOnAction(new EventHandler<ActionEvent>()
		{
			public void handle(ActionEvent event)
			{
				Stage about = new Stage();
				FlowPane p = new FlowPane();
				Scene scene = new Scene(p, 500, 300);

				Label description = new Label("AsyPad allows you to draw diagrams\n and save them into Asymptote Files.");
				description.setAlignment(Pos.CENTER);
				description.setStyle("-fx-font: 24 arial");
				description.setPrefSize(500, 100);

				Label license = new Label("GNU General Public License Information");
				license.setAlignment(Pos.CENTER);
				license.setStyle("-fx-font: 14 arial");
				license.setPrefSize(500, 50);

				TextArea licenseInfo = new TextArea();
				licenseInfo.setPrefSize(500, 150);
				licenseInfo.setEditable(false);
				licenseInfo.setText("AsyPad: A simple drawing tool that can convert diagrams into Asymptote code.\n" + 
						"For more information visit: https://github.com/rfeng2004/AsyPad\n" + 
						"\n" + 
						"Copyright (C) 2018 Raymond Feng\n" + 
						"\n" + 
						"This program is free software: you can redistribute it and/or modify\n" + 
						"it under the terms of the GNU General Public License as published by\n" + 
						"the Free Software Foundation, either version 3 of the License, or\n" + 
						"(at your option) any later version.\n" + 
						"\n" + 
						"This program is distributed in the hope that it will be useful,\n" + 
						"but WITHOUT ANY WARRANTY; without even the implied warranty of\n" + 
						"MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the\n" + 
						"GNU General Public License for more details.\n" + 
						"\n" + 
						"You should have received a copy of the GNU General Public License\n" + 
						"along with this program.  If not, see https://www.gnu.org/licenses/.");
				p.getChildren().addAll(description, license, licenseInfo);
				about.setScene(scene);
				about.setAlwaysOnTop(true);
				about.setTitle("About AsyPad");
				about.show();
			}
		});

		//credits
		credits.setOnAction(new EventHandler<ActionEvent>()
		{
			public void handle(ActionEvent event)
			{
				Stage cred = new Stage();
				FlowPane p = new FlowPane();
				Scene scene = new Scene(p, 500, 120);
				Label creds = new Label("AsyPad v" + AsyPad.VERSION + "\nCreator: Raymond Feng\nCollaborators: Anthony Wang\nTester: Wenyi Feng");
				creds.setAlignment(Pos.CENTER);
				creds.setStyle("-fx-font: 24 arial");
				creds.setPrefSize(500, 120);
				p.getChildren().add(creds);
				cred.setScene(scene);
				cred.setAlwaysOnTop(true);
				cred.setTitle("Credits");
				cred.show();
			}
		});
		about.getItems().addAll(aboutAsyPad, credits);
		help.getItems().add(about);

		getMenus().addAll(file, edit, settings, view, help);
	}
}
