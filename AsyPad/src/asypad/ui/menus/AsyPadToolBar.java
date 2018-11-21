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
	/**
	 * The currently selected tool. The default is {@code MOUSE.MOUSE}.
	 */
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
		
		Tool defaultMouse = new Tool(cursor, "Mouse", "Drag points or double click on point to configure.", MOUSE.MOUSE, mouse, this, parent);
		
		Image delete = new Image("resources/delete.png");
		Tool trash = new Tool(delete, "Delete", "Click on shapes to delete.", MOUSE.DELETE, mouse, this, parent);
		
		mouse.getItems().addAll(defaultMouse, trash);
		
		
		Menu point = new Menu();
		Image p = new Image("resources/point.png");
		ImageView pointGraphic = new ImageView(p);
		pointGraphic.setFitHeight(30);
		pointGraphic.setFitWidth(30);
		point.setGraphic(pointGraphic);
		
		Image onShape = new Image("resources/onshape.png");
		Image intersection = new Image("resources/intersection.png");
		Image mid = new Image("resources/midpoint.png");
		
		Tool defaultPoint = new Tool(p, "Point", "Click to create a point.", POINT_TYPE.POINT, point, this, parent);
		Tool pointOnShape = new Tool(onShape, "Point on Shape", "Click on a shape to create a point that is locked onto that shape.", POINT_TYPE.POINT_ON_SHAPE, point, this, parent);
		Tool intersectionPoint = new Tool(intersection, "Intersection Point", "Click on the intersection of 2 lines to create an intersection point.", POINT_TYPE.INTERSECTION_POINT, point, this, parent);
		Tool midpoint = new Tool(mid, "Midpoint", "Select 2 points to create a midpoint.", POINT_TYPE.MIDPOINT, point, this, parent);
		
		point.getItems().addAll(defaultPoint, pointOnShape, intersectionPoint, midpoint);

		Menu line = new Menu();
		Image segment = new Image("resources/segment.png");
		ImageView lineGraphic = new ImageView(segment);
		lineGraphic.setFitHeight(30);
		lineGraphic.setFitWidth(30);
		line.setGraphic(lineGraphic);
		
		Image lineImage = new Image("resources/line.png");
		Image parallel = new Image("resources/parallel.png");
		Image perpendicular = new Image("resources/perpendicular.png");
		Image angleBisectorImage = new Image("resources/anglebisector.png");
		Image perpendicularBisectorImage = new Image("resources/perpendicularbisector.png");
		
		Tool defaultSegment = new Tool(segment, "Segment", "Select 2 points to create a segment.", LINE_TYPE.SEGMENT, line, this, parent);
		Tool defaultLine = new Tool(lineImage, "Line", "Select 2 points to create a line.", LINE_TYPE.LINE, line, this, parent);
		Tool parallelLine = new Tool(parallel, "Parallel Line", "Select a point and a line to create a parallel line.", LINE_TYPE.PARALLEL_LINE, line, this, parent);
		Tool perpendicularLine = new Tool(perpendicular, "Perpendicular Line", "Select a point and a line to create a perpendicular line.", LINE_TYPE.PERPENDICULAR_LINE, line, this, parent);
		Tool angleBisector = new Tool(angleBisectorImage, "Angle Bisector", "Select 3 points to create an angle bisector. The second point will be the vertex of the angle.", LINE_TYPE.ANGLE_BISECTOR, line, this, parent);
		Tool perpendicularBisector = new Tool(perpendicularBisectorImage, "Perpendicular Bisector", "Select 2 points to construct their perpendicular bisector", LINE_TYPE.PERPENDICULAR_BISECTOR, line, this, parent);
		
		line.getItems().addAll(defaultSegment, defaultLine, parallelLine, perpendicularLine, angleBisector, perpendicularBisector);
		
		Menu circle = new Menu();
		Image c = new Image("resources/circle.png");
		ImageView circleGraphic = new ImageView(c);
		circleGraphic.setFitHeight(30);
		circleGraphic.setFitWidth(30);
		circle.setGraphic(circleGraphic);
		
		Image cc = new Image("resources/circumcircle.png");
		
		Tool defaultCircle = new Tool(c, "Circle", "Select the center, then a point on the circle.", CIRCLE_TYPE.CIRCLE, circle, this, parent);
		Tool circumcircle = new Tool(cc, "Circumircle", "Select 3 points to create a circumcircle.", CIRCLE_TYPE.CIRCUMCIRCLE, circle, this, parent);
		
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
