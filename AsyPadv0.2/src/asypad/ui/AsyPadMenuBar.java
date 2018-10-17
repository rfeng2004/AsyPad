package asypad.ui;

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
				Scene scene = new Scene(p, 250, 50);
				Label ab = new Label("AsyPad allows you to draw diagrams\n and save them into Asymptote Files.");
				ab.setAlignment(Pos.CENTER);
				ab.setTextAlignment(TextAlignment.CENTER);
				ab.setPrefSize(250, 50);
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
				Scene scene = new Scene(p, 250, 30);
				Label creds = new Label("AsyPad v0.2, created by Raymond Feng");
				creds.setAlignment(Pos.CENTER);
				creds.setPrefSize(250, 30);
				p.getChildren().add(creds);
				cred.setScene(scene);
				cred.show();
			}
		});
		getMenus().addAll(file, settings, help);
		setPrefSize(500, 30);
	}
}
