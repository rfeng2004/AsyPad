package asypad.ui.menus;

import asypad.shapes.types.*;
import asypad.shapes.types.SHAPE_TYPE.MOUSE;
import asypad.ui.AsyPadPane;
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
	 * Creates a new AsyPad toolbar with specified parent.
	 */
	public AsyPadToolBar(AsyPadPane parent)
	{
		super();
		selectedTool = MOUSE.MOUSE;

		Menu mouse = new Menu();
		Image cursor = new Image("resources/cursor.png");
		ImageView mouseGraphic = new ImageView(cursor);
		mouseGraphic.setFitHeight(30);
		mouseGraphic.setFitWidth(30);
		mouse.setGraphic(mouseGraphic);
		
		Tool defaultMouse = new Tool(cursor, "Mouse", MOUSE.MOUSE, mouse, this, parent);
		
		Image delete = new Image("resources/delete.png");
		Tool trash = new Tool(delete, "Delete", MOUSE.DELETE, mouse, this, parent);
		
		mouse.getItems().addAll(defaultMouse, trash);
		
		
		Menu point = new Menu();
		Image p = new Image("resources/point.png");
		ImageView pointGraphic = new ImageView(p);
		pointGraphic.setFitHeight(30);
		pointGraphic.setFitWidth(30);
		point.setGraphic(pointGraphic);
		
		Tool defaultPoint = new Tool(p, "Point", POINT_TYPE.POINT, point, this, parent);
		Tool pointOnShape = new Tool(p, "Point on Shape", POINT_TYPE.POINT_ON_OBJECT, point, this, parent);
		Tool intersectionPoint = new Tool(p, "Intersection Point", POINT_TYPE.INTERSECTION_POINT, point, this, parent);
		
		point.getItems().addAll(defaultPoint, pointOnShape, intersectionPoint);

		Menu line = new Menu();
		Image segment = new Image("resources/segment.png");
		ImageView lineGraphic = new ImageView(segment);
		lineGraphic.setFitHeight(30);
		lineGraphic.setFitWidth(30);
		line.setGraphic(lineGraphic);
		
		Tool defaultSegment = new Tool(segment, "Segment", LINE_TYPE.SEGMENT, line, this, parent);
		Tool defaultLine = new Tool(segment, "Line", LINE_TYPE.LINE, line, this, parent);
		Tool parallelLine = new Tool(segment, "Parallel Line", LINE_TYPE.PARALLEL_LINE, line, this, parent);
		Tool perpendicularLine = new Tool(segment, "Perpendicular Line", LINE_TYPE.PERPENDICULAR_LINE, line, this, parent);
		Tool angleBisector = new Tool(segment, "Angle Bisector", LINE_TYPE.ANGLE_BISECTOR, line, this, parent);
		Tool perpendicularBisector = new Tool(segment, "Perpendicular Bisector", LINE_TYPE.PERPENDICULAR_BISECTOR, line, this, parent);
		
		line.getItems().addAll(defaultSegment, defaultLine, parallelLine, perpendicularLine, angleBisector, perpendicularBisector);
		
		Menu circle = new Menu();
		Image c = new Image("resources/circle.png");
		ImageView circleGraphic = new ImageView(c);
		circleGraphic.setFitHeight(30);
		circleGraphic.setFitWidth(30);
		circle.setGraphic(circleGraphic);
		
		Tool defaultCircle = new Tool(c, "Circle", CIRCLE_TYPE.CIRCLE, circle, this, parent);
		Tool circumcircle = new Tool(c, "Circumircle", CIRCLE_TYPE.CIRCUMCIRCLE, circle, this, parent);
		
		circle.getItems().addAll(defaultCircle, circumcircle);
		
		getMenus().addAll(mouse, point, line, circle);
		setPrefSize(5000, 40);
		setLayoutX(0);
		setLayoutY(0);
	}
	
	/**
	 * Sets the selected tool.
	 */
	public void setSelectedTool(SHAPE_TYPE tool)
	{
		selectedTool = tool;
	}

	/**
	 * Returns the current selected tool.
	 * @return selected tool
	 */
	public SHAPE_TYPE getSelectedTool()
	{
		return selectedTool;
	}
}
