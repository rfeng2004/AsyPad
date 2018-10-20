package asypad.ui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class AsyPad extends Application
{
	public static void main(String args[])
	{
		launch(args);
	}

	public void start(Stage primaryStage)
	{
		AsyPadPane rootNode = new AsyPadPane();
		Scene scene = new Scene(rootNode, 1000, 700);
		primaryStage.getIcons().add(new Image("resources/icon.icns"));
		primaryStage.setTitle("AsyPad");
		primaryStage.setScene(scene);
		primaryStage.show();
	}
}
