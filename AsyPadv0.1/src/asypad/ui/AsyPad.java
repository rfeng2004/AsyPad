package asypad.ui;

import asypad.shapes.*;
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
		Point p1 = new Point(300, 300);
		Point p2 = new Point(200, 200);
		Circle c = new Circle(p1, p2);
		Line l = new Line(p1, p2, false);
		Point p3 = new Point(l, c, true, "");
		System.out.println(p3);
		rootNode.addShapes(p1, p2, l, c, p3);
		Scene scene = new Scene(rootNode, 1000, 700);
		primaryStage.getIcons().add(new Image("resources/AsyPad.icns"));
		primaryStage.setTitle("AsyPad");
		primaryStage.setScene(scene);
		primaryStage.show();
	}
}
