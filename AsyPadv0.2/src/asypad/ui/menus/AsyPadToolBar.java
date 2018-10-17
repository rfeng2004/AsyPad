package asypad.ui.menus;

import asypad.shapes.types.SHAPE_TYPE;
import asypad.shapes.types.SHAPE_TYPE.MOUSE;
import javafx.scene.control.*;
import javafx.scene.image.*;

/**
 * AsyPad toolbar contains all the tools for geometric construction on AsyPad.
 * @author Raymond Feng
 */
public class AsyPadToolBar extends MenuBar
{
	private SHAPE_TYPE selectedTool;
	/**
	 * Creates a new AsyPad toolbar.
	 */
	public AsyPadToolBar()
	{
		super();
		selectedTool = MOUSE.MOUSE;
		
		Menu mouse = new Menu();
		Image cursor = new Image("resources/cursor.png");
		ImageView mouseGraphic = new ImageView(cursor);
		mouseGraphic.setFitHeight(30);
		mouseGraphic.setFitWidth(30);
		mouse.setGraphic(mouseGraphic);
		
		Menu point = new Menu();
		Image p = new Image("resources/point.png");
		ImageView pointGraphic = new ImageView(p);
		pointGraphic.setFitHeight(30);
		pointGraphic.setFitWidth(30);
		point.setGraphic(pointGraphic);
		
		Menu line = new Menu();
		Image segment = new Image("resources/segment.png");
		ImageView lineGraphic = new ImageView(segment);
		lineGraphic.setFitHeight(30);
		lineGraphic.setFitWidth(30);
		line.setGraphic(lineGraphic);
		
		Menu circle = new Menu();
		Image c = new Image("resources/circle.png");
		ImageView circleGraphic = new ImageView(c);
		circleGraphic.setFitHeight(30);
		circleGraphic.setFitWidth(30);
		circle.setGraphic(circleGraphic);
		
		getMenus().addAll(mouse, point, line, circle);
		setPrefSize(5000, 40);
		setLayoutX(0);
		setLayoutY(0);
	}
	
	public SHAPE_TYPE getSelectedTool()
	{
		return selectedTool;
	}
}
