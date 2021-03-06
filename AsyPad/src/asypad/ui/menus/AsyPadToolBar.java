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
	 * @param parent the AsyPadPane that contains this AsyPadToolBar
	 */
	public AsyPadToolBar(AsyPadPane parent)
	{
		super();
		selectedTool = MOUSE.MOUSE;

		Image cursor = new Image("resources/cursor.png");
		ImageView mouseGraphic = new ImageView(cursor);
		mouseGraphic.setFitHeight(30);
		mouseGraphic.setFitWidth(30);

		ToolMenu mouse = new ToolMenu(mouseGraphic);

		Tool defaultMouse = new Tool(cursor, "Mouse", "Drag points or double click on point to configure.", MOUSE.MOUSE, mouse, this, parent);

		Image delete = new Image("resources/delete.png");
		Tool trash = new Tool(delete, "Delete", "Click on shapes to delete.", MOUSE.DELETE, mouse, this, parent);

		Image dragIcon = new Image("resources/drag.png");
		Tool drag = new Tool(dragIcon, "Drag Labels", "Use this tool to drag labels.", MOUSE.DRAG, mouse, this, parent);

		mouse.getItems().addAll(defaultMouse, trash, drag);
		mouse.setLastUsed(defaultMouse);


		Image p = new Image("resources/point.png");
		ImageView pointGraphic = new ImageView(p);
		pointGraphic.setFitHeight(30);
		pointGraphic.setFitWidth(30);
		
		ToolMenu point = new ToolMenu(pointGraphic);

		Image onShape = new Image("resources/onshape.png");
		Image intersection = new Image("resources/intersection.png");
		Image mid = new Image("resources/midpoint.png");

		Tool defaultPoint = new Tool(p, "Point", "Click to create a point.", POINT_TYPE.POINT, point, this, parent);
		Tool pointOnShape = new Tool(onShape, "Point on Shape", "Click on a shape to create a point that is locked onto that shape.", POINT_TYPE.POINT_ON_SHAPE, point, this, parent);
		Tool intersectionPoint = new Tool(intersection, "Intersection Point", "Click on the intersection of 2 shapes or select 2 shapes individually to intersect.", POINT_TYPE.INTERSECTION_POINT, point, this, parent);
		Tool midpoint = new Tool(mid, "Midpoint", "Select 2 points to create a midpoint.", POINT_TYPE.MIDPOINT, point, this, parent);

		point.getItems().addAll(defaultPoint, pointOnShape, intersectionPoint, midpoint);
		point.setLastUsed(defaultPoint);

		
		Image segment = new Image("resources/segment.png");
		ImageView lineGraphic = new ImageView(segment);
		lineGraphic.setFitHeight(30);
		lineGraphic.setFitWidth(30);

		ToolMenu line = new ToolMenu(lineGraphic);

		Image lineImage = new Image("resources/line.png");
		Image parallel = new Image("resources/parallel.png");
		Image perpendicular = new Image("resources/perpendicular.png");
		Image angleBisectorImage = new Image("resources/anglebisector.png");
		Image perpendicularBisectorImage = new Image("resources/perpendicularbisector.png");
		Image tangentLineImage = new Image("resources/tangentline.png");

		Tool defaultSegment = new Tool(segment, "Segment", "Select 2 points to create a segment.", LINE_TYPE.SEGMENT, line, this, parent);
		Tool defaultLine = new Tool(lineImage, "Line", "Select 2 points to create a line.", LINE_TYPE.LINE, line, this, parent);
		Tool parallelLine = new Tool(parallel, "Parallel Line", "Select a point and a line to create a parallel line.", LINE_TYPE.PARALLEL_LINE, line, this, parent);
		Tool perpendicularLine = new Tool(perpendicular, "Perpendicular Line", "Select a point and a line to create a perpendicular line.", LINE_TYPE.PERPENDICULAR_LINE, line, this, parent);
		Tool angleBisector = new Tool(angleBisectorImage, "Angle Bisector", "Select 3 points to create an angle bisector. The second point will be the vertex of the angle.", LINE_TYPE.ANGLE_BISECTOR, line, this, parent);
		Tool perpendicularBisector = new Tool(perpendicularBisectorImage, "Perpendicular Bisector", "Select 2 points to construct their perpendicular bisector", LINE_TYPE.PERPENDICULAR_BISECTOR, line, this, parent);
		Tool tangentLine = new Tool(tangentLineImage, "Tangent Line", "Select a point and a circle to construct a tangent line through the point.", LINE_TYPE.TANGENT_LINE, line, this, parent);

		line.getItems().addAll(defaultSegment, defaultLine, parallelLine, perpendicularLine, angleBisector, perpendicularBisector, tangentLine);
		line.setLastUsed(defaultSegment);

		
		Image c = new Image("resources/circle.png");
		ImageView circleGraphic = new ImageView(c);
		circleGraphic.setFitHeight(30);
		circleGraphic.setFitWidth(30);

		ToolMenu circle = new ToolMenu(circleGraphic);

		Image cc = new Image("resources/circumcircle.png");
		Image ic = new Image("resources/incircle.png");
		//Image tc = new Image("resources/tangentcircle.png");

		Tool defaultCircle = new Tool(c, "Circle", "Select the center, then a point on the circle.", CIRCLE_TYPE.CIRCLE, circle, this, parent);
		Tool circumcircle = new Tool(cc, "Circumircle", "Select 3 points to create a circumcircle.", CIRCLE_TYPE.CIRCUMCIRCLE, circle, this, parent);
		Tool incircle = new Tool(ic, "Incircle", "Select 3 points to create a incircle.", CIRCLE_TYPE.INCIRCLE, circle, this, parent);
		//Tool tangentcircle = new Tool(tc, "Tangent Circle", "Select 2 circles and a point on one of the circles to create a circle tangent to them at the point.", CIRCLE_TYPE.TANGENT_CIRCLE, circle, this, parent);

		circle.getItems().addAll(defaultCircle, circumcircle, incircle/*, tangentcircle*/);
		circle.setLastUsed(defaultCircle);

		getMenus().addAll(mouse, point, line, circle);
		setPrefSize(5000, 40);
		setLayoutX(0);
		setLayoutY(0);
	}

	/**
	 * Sets the selected tool.
	 * @param tool the new selected tool
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
