package asypad.ui.menus;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import asypad.shapes.Shape;
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
	 */
	public AsyPadMenuBar(AsyPadPane parent)
	{
		super();

		//uses system menu bar on mac
		final String os = System.getProperty("os.name");
		final KeyCombination.Modifier c;
		if (os != null && os.startsWith("Mac"))
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
		file.getItems().addAll(new MenuItem("Save as Asy File"), new MenuItem("Load Asy File"));
		KeyCodeCombination cs = new KeyCodeCombination(KeyCode.S, c);
		file.getItems().get(0).setAccelerator(cs);
		file.getItems().get(0).setOnAction(new EventHandler<ActionEvent>()
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

		file.getItems().get(1).setOnAction(new EventHandler<ActionEvent>()
		{
			public void handle(ActionEvent event)
			{

			}
		});

		//undo+redo
		Menu edit = new Menu("Edit");
		MenuItem undo = new MenuItem("Undo");
		KeyCodeCombination cz = new KeyCodeCombination(KeyCode.Z, c);
		undo.setAccelerator(cz);
		MenuItem redo = new MenuItem("Redo");
		KeyCodeCombination cy = new KeyCodeCombination(KeyCode.Y, c);
		redo.setAccelerator(cy);
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
		edit.getItems().addAll(undo, redo);

		//settings
		Menu settings = new Menu("Settings");
		settings.getItems().add(new MenuItem("Set Stroke Width"));
		settings.getItems().get(0).setOnAction(new EventHandler<ActionEvent>()
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
						parent.addCommand(new StrokeWidthCommand(Shape.StrokeWidth));
						setSW.close();
						parent.update();
					}
				});
				p.getChildren().addAll(label, sw, refresh);
				setSW.setScene(scene);
				setSW.show();
			}
		});

		Menu view = new Menu("View");
		MenuItem showHidden = new MenuItem("Show Hidden Shapes");
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
		view.getItems().add(showHidden);

		//help menu (about and credits)
		Menu help = new Menu("Help");
		Menu about = new Menu("About");
		about.getItems().addAll(new MenuItem("About"), new MenuItem("Credits"));
		help.getItems().add(about);

		//about the program (description)
		about.getItems().get(0).setOnAction(new EventHandler<ActionEvent>()
		{
			public void handle(ActionEvent event)
			{
				Stage a = new Stage();
				FlowPane p = new FlowPane();
				Scene scene = new Scene(p, 500, 100);
				Label ab = new Label("AsyPad allows you to draw diagrams\n and save them into Asymptote Files.");
				ab.setAlignment(Pos.CENTER);
				ab.setTextAlignment(TextAlignment.CENTER);
				ab.setStyle("-fx-font: 24 arial");
				ab.setPrefSize(500, 100);
				p.getChildren().add(ab);
				a.setScene(scene);
				a.show();
			}
		});

		//credits
		about.getItems().get(1).setOnAction(new EventHandler<ActionEvent>()
		{
			public void handle(ActionEvent event)
			{
				Stage cred = new Stage();
				FlowPane p = new FlowPane();
				Scene scene = new Scene(p, 500, 120);
				Label creds = new Label("AsyPad v0.1\nProgrammer: Raymond Feng\nHead Tester: Wenyi Feng");
				creds.setAlignment(Pos.CENTER);
				creds.setStyle("-fx-font: 24 arial");
				creds.setPrefSize(500, 120);
				p.getChildren().add(creds);
				cred.setScene(scene);
				cred.show();
			}
		});

		getMenus().addAll(file, edit, settings, view, help);
	}
}
