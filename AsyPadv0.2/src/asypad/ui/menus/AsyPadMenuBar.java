package asypad.ui.menus;

import javafx.event.*;
import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.TextAlignment;
import javafx.stage.*;

/**
 * AsyPad menu bar used in the AsyPad application.
 * @author Raymond Feng
 */
public class AsyPadMenuBar extends MenuBar
{
	/**
	 * Creates a new AsyPad menu bar.
	 */
	public AsyPadMenuBar()
	{
		super();

		//uses system menu bar on mac
		final String os = System.getProperty("os.name");
		if (os != null && os.startsWith("Mac"))
		{
			useSystemMenuBarProperty().set(true);
		}
		else
		{
			setPrefSize(5000, 30);
			setLayoutX(0);
			setLayoutY(0);
		}

		//file io
		Menu file = new Menu("File");
		file.getItems().addAll(new MenuItem("Save as Asy File"), new MenuItem("Load Asy File"));
		file.getItems().get(0).setOnAction(new EventHandler<ActionEvent>()
		{
			public void handle(ActionEvent event)
			{

			}
		});

		file.getItems().get(1).setOnAction(new EventHandler<ActionEvent>()
		{
			public void handle(ActionEvent event)
			{

			}
		});

		//settings
		Menu settings = new Menu("Settings");
		settings.getItems().add(new MenuItem("Set Stroke Width"));
		settings.getItems().get(0).setOnAction(new EventHandler<ActionEvent>()
		{
			public void handle(ActionEvent event)
			{

			}
		});

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
				Scene scene = new Scene(p, 500, 60);
				Label creds = new Label("AsyPad v0.2, created by Raymond Feng");
				creds.setAlignment(Pos.CENTER);
				creds.setStyle("-fx-font: 24 arial");
				creds.setPrefSize(500, 60);
				p.getChildren().add(creds);
				cred.setScene(scene);
				cred.show();
			}
		});

		getMenus().addAll(file, settings, help);
	}
}
