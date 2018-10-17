package asypad.ui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import asypad.shapes.*;

public class Test extends Application
{
	public static void main(String args[])
	{
		launch(args);
	}

	public void start(Stage primaryStage)
	{
		AsyPadPane rootNode = new AsyPadPane();
		Scene scene = new Scene(rootNode, 1000, 700);
		Point p1 = new Point(200, 200, "A");
		Point p2 = new Point(400, 320, "B");
		Point p3 = new Point(200, 400, "C");
		Point p4 = new Point(900, 430, "D");
		Line l1 = new Line(p1, p2, true);
		Line l2 = new Line(p3, p4, false);
		Line l3 = new Line(p1, l2, false);
		Point ip = new Point(l1, l2, "X");
		Circle  c = new Circle(p1, p2);
		Circle cc = new Circle(p1, p2, p3);
		Line ab = new Line(p1, ip, p4);
		Line pb = new Line(ip, p4);
		rootNode.addShapes(p1, p2, p3, p4, l1, l2, l3, ip, c, cc, ab, pb);
		rootNode.update();
		primaryStage.setTitle("AsyPad");
		primaryStage.setScene(scene);
		primaryStage.show();
	}
}
