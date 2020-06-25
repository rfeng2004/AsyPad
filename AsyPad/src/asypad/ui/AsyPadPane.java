package asypad.ui;
/*
 * TODO Add new tool: relative point, conic sections.
 * TODO Implement grid show and hide.
 * TODO Add user manual in help menu.
 * FIXME Tangent Circle Bugs
 * - Clicking on intersection point of tangent circle and original circle causes bugs.
 * - Add tangent circle asymptote and asypad conversions.
 */

import java.io.*;
import java.util.ArrayList;

import javafx.animation.AnimationTimer;
import javafx.event.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import asypad.shapes.*;
import asypad.shapes.types.*;
import asypad.shapes.types.SHAPE_TYPE.MOUSE;
import asypad.ui.command.*;
import asypad.ui.menus.*;

/**
 * A special drawing pane that interacts with the user and can be integrated into
 * other JavaFX applications.
 * @author Raymond Feng
 * @version 1.0
 */
public class AsyPadPane extends Pane
{
	/**
	 * ArrayList of all drawn Shapes.
	 */
	private ArrayList<Shape> shapes;

	/**
	 * Index pointing to the shape that the mouse is snapped to.
	 */
	private int snappedIndex;

	/**
	 * Whether the mouse has been dragging a shape.
	 */
	private boolean shapeDragged;

	/**
	 * ArrayList of user selected shapes.
	 */
	private ArrayList<Shape> selectedShapes;

	/**
	 * ArrayList of shapes that the mouse is snapped to.
	 */
	private ArrayList<Shape> snappedShapes;

	/**
	 * ArrayList of commands that stores previous states of the AsyPadPane for undo/redo.
	 */
	private ArrayList<Command> commands;

	/**
	 * The index that points to the position of the last command in the AsyPadPane.
	 */
	private int currentCommandIndex;

	/**
	 * Specifies the strength of the snapping system, i.e. how close the mouse has to be to snap to a shape.
	 */
	private static final double snapForce = 5*Shape.StrokeWidth;

	/**
	 * Label specifying the current tool.
	 */
	private Label currentTool;

	/**
	 * Label specifying the current tool description.
	 */
	private Label currentToolDescription;

	/**
	 * Temporary line that is used for "animation", i.e. the one the user sees when not all dependencies have been specified.
	 */
	private javafx.scene.shape.Line currentLine;

	/**
	 * Temporary circle that is used for "animation", i.e. the one the user sees when not all dependencies have been specified.
	 */
	private javafx.scene.shape.Circle currentCircle;

	/**
	 * Color of selected shapes.
	 */
	private Color selected;

	/**
	 * ColorPicker option which saves custom colors during the session.
	 */
	private ColorPicker colorPicker;

	/**
	 * Previous mouse location (tracked for dragging background).
	 */
	private double pmouseX, pmouseY;

	/**
	 * Total translated amount (accumulated over a single gesture).
	 */
	private double totalTranslateX, totalTranslateY;

	/**
	 * Total scrolled amount (accumulated over a single gesture).
	 */
	private double totalScroll;

	/**
	 * Creates an AsyPadPane layout.
	 */
	public AsyPadPane()
	{
		super();
		shapes = new ArrayList<Shape>();
		selectedShapes = new ArrayList<Shape>();
		snappedShapes = new ArrayList<Shape>();
		commands = new ArrayList<Command>();
		currentCommandIndex = -1;
		currentLine = new javafx.scene.shape.Line();
		currentCircle = new javafx.scene.shape.Circle();
		currentCircle.setFill(Color.TRANSPARENT);
		currentCircle.setStroke(Color.BLACK);
		selected = Color.RED;
		colorPicker = new ColorPicker();
		pmouseX = 0;
		pmouseY = 0;
		totalTranslateX = 0;
		totalTranslateY = 0;
		totalScroll = 0;

		AsyPadMenuBar menus = new AsyPadMenuBar(this);
		AsyPadToolBar tools = new AsyPadToolBar(this);
		tools.setLayoutY(menus.getPrefHeight());
		currentTool = new Label("Tool: Mouse");
		currentTool.setLayoutX(220);
		currentTool.setLayoutY(10 + menus.getPrefHeight());
		currentTool.setFocusTraversable(true);
		currentTool.requestFocus(); //needed for key listener to be valid on the pane.
		currentToolDescription = new Label("Drag points or double click on point to configure.");
		currentToolDescription.setLayoutX(420);
		currentToolDescription.setLayoutY(10 + menus.getPrefHeight());

		getChildren().addAll(menus, tools, currentTool, currentToolDescription);
		this.setOnKeyPressed(new EventHandler<KeyEvent>()
		{
			public void handle(KeyEvent event)
			{
				if(event.getCode() == KeyCode.ESCAPE)
				{
					resetSelectedShapes();
					selectedShapes.clear();
					if(getChildren().contains(currentLine))
					{
						getChildren().remove(currentLine);
					}
					if(getChildren().contains(currentCircle))
					{
						getChildren().remove(currentCircle);
					}
				}
				//add hotkeys?
			}
		});
		this.setOnMouseMoved(new EventHandler<MouseEvent>()
		{
			public void handle(MouseEvent event)
			{
				snappedIndex = -1;
				snappedShapes.clear();
				for(Shape s : shapes)
				{
					if(getChildren().contains(s.getObject()))
					{
						if(Utility.distToShape(event.getSceneX(), event.getSceneY(), s) < snapForce)
						{
							setCursor(Cursor.HAND);
							if(snappedIndex == -1 || !(shapes.get(snappedIndex) instanceof Point))
							{
								snappedIndex = shapes.indexOf(s);
							}
							if(!(s instanceof Point))
							{
								snappedShapes.add(s);
							}
						}
					}
				}
				if(snappedIndex == -1)
				{
					if(tools.getSelectedTool() == MOUSE.MOUSE)
					{
						setCursor(Cursor.OPEN_HAND);
					}
					else setCursor(Cursor.DEFAULT);
				}

				SHAPE_TYPE tool = tools.getSelectedTool();
				if(tool == LINE_TYPE.SEGMENT || tool == LINE_TYPE.LINE)
				{
					if(selectedShapes.size() == 1)
					{
						Line l;
						Point p = (Point) selectedShapes.get(0);
						double x = event.getSceneX();
						double y = event.getSceneY();
						if(snappedIndex != -1 && shapes.get(snappedIndex) instanceof Point)
						{
							Point point = (Point) shapes.get(snappedIndex);
							x = point.getX();
							y = point.getY();
						} 
						if(tool == LINE_TYPE.SEGMENT)
						{
							l = new Line(p, new Point(x, y), true);
						}
						else
						{
							l = new Line(p, new Point(x, y), false);
						}
						setCurrentLine(l.getStartX(), l.getStartY(), l.getEndX(), l.getEndY());
					}
				}
				else if(tool == LINE_TYPE.PARALLEL_LINE || tool == LINE_TYPE.PERPENDICULAR_LINE)
				{
					if(selectedShapes.size() == 1 && selectedShapes.get(0) instanceof Line)
					{
						Line l;
						double x = event.getSceneX();
						double y = event.getSceneY();
						if(snappedIndex != -1 && shapes.get(snappedIndex) instanceof Point)
						{
							Point point = (Point) shapes.get(snappedIndex);
							x = point.getX();
							y = point.getY();
						}
						if(tool == LINE_TYPE.PARALLEL_LINE)
						{
							l = new Line(new Point(x, y), (Line) selectedShapes.get(0), true);
						}
						else
						{
							l = new Line(new Point(x, y), (Line) selectedShapes.get(0), false);
						}
						setCurrentLine(l.getStartX(), l.getStartY(), l.getEndX(), l.getEndY());
					}
				}
				else if(tool == CIRCLE_TYPE.CIRCLE)
				{
					if(selectedShapes.size() == 1)
					{
						Point p = (Point) selectedShapes.get(0);
						double x = event.getSceneX();
						double y = event.getSceneY();
						if(snappedIndex != -1 && shapes.get(snappedIndex) instanceof Point)
						{
							Point point = (Point) shapes.get(snappedIndex);
							x = point.getX();
							y = point.getY();
						}
						Circle c = new Circle(p, new Point(x, y));
						setCurrentCircle(c.getCenterX(), c.getCenterY(), c.getRadius());
					}
				}
				else if(tool == CIRCLE_TYPE.CIRCUMCIRCLE)
				{
					if(selectedShapes.size() == 2)
					{
						Point p1 = (Point) selectedShapes.get(0);
						Point p2 = (Point) selectedShapes.get(1);
						double x = event.getSceneX();
						double y = event.getSceneY();
						if(snappedIndex != -1 && shapes.get(snappedIndex) instanceof Point)
						{
							Point point = (Point) shapes.get(snappedIndex);
							x = point.getX();
							y = point.getY();
							if(point == p1 || point == p2)
							{
								x = (p1.getX()+p2.getX())/2;
								y = (p1.getY()+p2.getY())/2;
							}
						}
						Circle c = new Circle(new Point(x, y), p1, p2, true);
						setCurrentCircle(c.getCenterX(), c.getCenterY(), c.getRadius());
					}
				}
				else if(tool == CIRCLE_TYPE.INCIRCLE)
				{
					if(selectedShapes.size() == 2)
					{
						Point p1 = (Point) selectedShapes.get(0);
						Point p2 = (Point) selectedShapes.get(1);
						double x = event.getSceneX();
						double y = event.getSceneY();
						if(snappedIndex != -1 && shapes.get(snappedIndex) instanceof Point)
						{
							Point point = (Point) shapes.get(snappedIndex);
							x = point.getX();
							y = point.getY();
							if(point == p1 || point == p2)
							{
								x = (p1.getX()+p2.getX())/2;
								y = (p1.getY()+p2.getY())/2;
							}
						}
						Circle c = new Circle(new Point(x, y), p1, p2, false);
						setCurrentCircle(c.getCenterX(), c.getCenterY(), c.getRadius());
					}
				}
			}
		});
		this.setOnMousePressed(new EventHandler<MouseEvent>()
		{
			public void handle(MouseEvent event)
			{
				SHAPE_TYPE tool = tools.getSelectedTool();
				if(tool == MOUSE.MOUSE || tool instanceof POINT_TYPE)
				{
					if(snappedIndex == -1 && tool == MOUSE.MOUSE)
					{
						setCursor(Cursor.CLOSED_HAND);
					}
					if(snappedIndex != -1 && (event.getClickCount() == 2 || event.isSecondaryButtonDown()))
					{
						showConfigureShape(shapes.get(snappedIndex));
					}
				}
				if(tool == MOUSE.DELETE)
				{
					if(snappedIndex != -1)
					{
						addCommand(new DeleteCommand(shapes.get(snappedIndex)));
						shapes.get(snappedIndex).delete();
						update();
						snappedIndex = -1;
					}
				}
				else if(tool == POINT_TYPE.POINT)
				{
					ArrayList<Line> lines = new ArrayList<Line>();
					ArrayList<Circle> circles = new ArrayList<Circle>();
					for(Shape s : snappedShapes)
					{
						if(s instanceof Line)
						{
							lines.add((Line) s);
						}
						else if(s instanceof Circle)
						{
							circles.add((Circle) s);
						}
					}
					if(snappedIndex == -1)
					{
						Point p = new Point(event.getSceneX(), event.getSceneY(), nextPointName(1));
						addShape(p);
						addCommand(new DrawCommand(p, p.getX(), p.getY(), p.getName()));
						//snappedIndex = shapes.size()-1;
						setCursor(Cursor.HAND);
					}
					else if(shapes.get(snappedIndex) instanceof Point) 
					{
						//dont draw a point over another point
						return;
					}
					else if(snappedShapes.size() == 1)
					{
						Point p = new Point(event.getSceneX(), event.getSceneY(), shapes.get(snappedIndex), nextPointName(1));
						addShape(p);
						addCommand(new DrawCommand(p, p.getX(), p.getY(), p.getName()));
						//snappedIndex = shapes.size()-1;
						setCursor(Cursor.HAND);
					}
					else
					{
						if(lines.size() >= 2)
						{
							Point p = new Point(lines.get(0), lines.get(1), nextPointName(1));
							addShape(p);
							addCommand(new DrawCommand(p, p.getX(), p.getY(), p.getName()));
						}
						else if(lines.size() == 1 && circles.size() >= 1)
						{
							Point p1 = new Point((Line) lines.get(0), (Circle) circles.get(0), false, nextPointName(1));
							Point p2 = new Point((Line) lines.get(0), (Circle) circles.get(0), true, nextPointName(1));
							if(Utility.distToShape(event.getSceneX(), event.getSceneY(), p1) < Utility.distToShape(event.getSceneX(), event.getSceneY(), p2))
							{
								addShape(p1);
								addCommand(new DrawCommand(p1, p1.getX(), p1.getY(), p1.getName()));
							}
							else
							{
								addShape(p2);
								addCommand(new DrawCommand(p2, p2.getX(), p2.getY(), p2.getName()));
							}
						}
						else if(circles.size() >= 2)
						{
							Point p1 = new Point((Circle) circles.get(0), (Circle) circles.get(1), false, nextPointName(1));
							Point p2 = new Point((Circle) circles.get(0), (Circle) circles.get(1), true, nextPointName(1));
							if(Utility.distToShape(event.getSceneX(), event.getSceneY(), p1) < Utility.distToShape(event.getSceneX(), event.getSceneY(), p2))
							{
								addShape(p1);
								//System.out.println(p1);
								addCommand(new DrawCommand(p1, p1.getX(), p1.getY(), p1.getName()));
							}
							else 
							{
								addShape(p2);
								//System.out.println(p2);
								addCommand(new DrawCommand(p2, p2.getX(), p2.getY(), p2.getName()));
							}
						}
					}
				}
				else if(tool == POINT_TYPE.POINT_ON_SHAPE)
				{
					if(snappedIndex != -1 && !(shapes.get(snappedIndex) instanceof Point))
					{
						Point p = new Point(event.getSceneX(), event.getSceneY(), shapes.get(snappedIndex), nextPointName(1));
						addShape(p);
						addCommand(new DrawCommand(p, p.getX(), p.getY(), p.getName()));
						//snappedIndex = shapes.size()-1;
						setCursor(Cursor.HAND);
					}
				}
				else if(tool == POINT_TYPE.INTERSECTION_POINT)
				{
					if(snappedIndex != -1 && shapes.get(snappedIndex) instanceof Point) return;
					ArrayList<Line> lines = new ArrayList<Line>();
					ArrayList<Circle> circles = new ArrayList<Circle>();
					for(Shape s : snappedShapes)
					{
						if(s instanceof Line)
						{
							lines.add((Line) s);
						}
						else if(s instanceof Circle)
						{
							circles.add((Circle) s);
						}
					}
					if(lines.size() >= 2)
					{
						Point p = new Point(lines.get(0), lines.get(1), nextPointName(1));
						addShape(p);
						addCommand(new DrawCommand(p, p.getX(), p.getY(), p.getName()));
					}
					else if(lines.size() == 1 && circles.size() >= 1)
					{
						Point p1 = new Point((Line) lines.get(0), (Circle) circles.get(0), false, nextPointName(1));
						Point p2 = new Point((Line) lines.get(0), (Circle) circles.get(0), true, nextPointName(1));
						if(Utility.distToShape(event.getSceneX(), event.getSceneY(), p1) < Utility.distToShape(event.getSceneX(), event.getSceneY(), p2))
						{
							addShape(p1);
							//System.out.println(p1);
							addCommand(new DrawCommand(p1, p1.getX(), p1.getY(), p1.getName()));
						}
						else 
						{
							addShape(p2);
							//System.out.println(p2);
							addCommand(new DrawCommand(p2, p2.getX(), p2.getY(), p2.getName()));
						}
					}
					else if(circles.size() >= 2)
					{
						Point p1 = new Point((Circle) circles.get(0), (Circle) circles.get(1), false, nextPointName(1));
						Point p2 = new Point((Circle) circles.get(0), (Circle) circles.get(1), true, nextPointName(1));
						if(Utility.distToShape(event.getSceneX(), event.getSceneY(), p1) < Utility.distToShape(event.getSceneX(), event.getSceneY(), p2))
						{
							addShape(p1);
							//System.out.println(p1);
							addCommand(new DrawCommand(p1, p1.getX(), p1.getY(), p1.getName()));
						}
						else 
						{
							addShape(p2);
							//System.out.println(p2);
							addCommand(new DrawCommand(p2, p2.getX(), p2.getY(), p2.getName()));
						}
					}
				}
				else if(tool == POINT_TYPE.MIDPOINT)
				{
					if(snappedIndex != -1 && shapes.get(snappedIndex) instanceof Point) selectedShapes.add(shapes.get(snappedIndex));
					if(selectedShapes.size() == 2)
					{
						if(selectedShapes.get(0) != selectedShapes.get(1))
						{
							Point p = new Point((Point) selectedShapes.get(0), (Point) selectedShapes.get(1), nextPointName(1));
							addShape(p);
							addCommand(new DrawCommand(p, p.getX(), p.getY(), p.getName()));
						}
						resetSelectedShapes();
						selectedShapes.clear();
					}
				}
				else if(tool == LINE_TYPE.SEGMENT || tool == LINE_TYPE.LINE)
				{
					if(snappedIndex != -1 && shapes.get(snappedIndex) instanceof Point) selectedShapes.add(shapes.get(snappedIndex));
					if(selectedShapes.size() == 2)
					{
						if(selectedShapes.get(0) != selectedShapes.get(1))
						{
							if(tool == LINE_TYPE.SEGMENT)
							{
								Line l = new Line((Point) selectedShapes.get(0), (Point) selectedShapes.get(1), true);
								addShape(l);
								addCommand(new DrawCommand(l));
							}
							else 
							{
								Line l = new Line((Point) selectedShapes.get(0), (Point) selectedShapes.get(1), false);
								addShape(l);
								addCommand(new DrawCommand(l));
							}
						}
						resetSelectedShapes();
						selectedShapes.clear();
						getChildren().remove(currentLine);
					}
				}
				else if(tool == LINE_TYPE.PARALLEL_LINE || tool == LINE_TYPE.PERPENDICULAR_LINE)
				{
					if(snappedIndex != -1 && shapes.get(snappedIndex) instanceof Point)
					{
						if(selectedShapes.size() == 0)
						{
							selectedShapes.add(shapes.get(snappedIndex));
						}
						else if(selectedShapes.size() == 1 && selectedShapes.get(0) instanceof Line)
						{
							selectedShapes.add(shapes.get(snappedIndex));
						}
					}
					else if(snappedIndex != -1 && shapes.get(snappedIndex) instanceof Line)
					{
						if(selectedShapes.size() == 0)
						{
							selectedShapes.add(shapes.get(snappedIndex));
						}
						else if(selectedShapes.size() == 1 && selectedShapes.get(0) instanceof Point)
						{
							selectedShapes.add(shapes.get(snappedIndex));
						}
					}
					if(selectedShapes.size() == 2)
					{
						if(selectedShapes.get(0) instanceof Point)
						{
							if(tool == LINE_TYPE.PARALLEL_LINE)
							{
								Line l = new Line((Point) selectedShapes.get(0), (Line) selectedShapes.get(1), true);
								addShape(l);
								addCommand(new DrawCommand(l));
							}
							else 
							{
								Line l = new Line((Point) selectedShapes.get(0), (Line) selectedShapes.get(1), false);
								addShape(l);
								addCommand(new DrawCommand(l));
							}
						}
						else 
						{
							if(tool == LINE_TYPE.PARALLEL_LINE)
							{
								Line l = new Line((Point) selectedShapes.get(1), (Line) selectedShapes.get(0), true);
								addShape(l);
								addCommand(new DrawCommand(l));
							}
							else
							{
								Line l = new Line((Point) selectedShapes.get(1), (Line) selectedShapes.get(0), false);
								addShape(l);
								addCommand(new DrawCommand(l));
							}
						}
						resetSelectedShapes();
						selectedShapes.clear();
						getChildren().remove(currentLine);
					}
				}
				else if(tool == LINE_TYPE.ANGLE_BISECTOR)
				{
					if(snappedIndex != -1 && shapes.get(snappedIndex) instanceof Point) selectedShapes.add(shapes.get(snappedIndex));
					if(selectedShapes.size() == 3)
					{
						Point p1 = (Point) selectedShapes.get(0);
						Point p2 = (Point) selectedShapes.get(1);
						Point p3 = (Point) selectedShapes.get(2);
						if(p1!=p2 && p2!=p3 && p3!=p1)
						{
							Line l = new Line(p1, p2, p3);
							addShape(l);
							addCommand(new DrawCommand(l));
						}
						resetSelectedShapes();
						selectedShapes.clear();
					}
				}
				else if(tool == LINE_TYPE.PERPENDICULAR_BISECTOR)
				{
					if(snappedIndex != -1 && shapes.get(snappedIndex) instanceof Point) selectedShapes.add(shapes.get(snappedIndex));
					if(selectedShapes.size() == 2)
					{
						if(selectedShapes.get(0) != selectedShapes.get(1))
						{
							Line l = new Line((Point) selectedShapes.get(0), (Point) selectedShapes.get(1));
							addShape(l);
							addCommand(new DrawCommand(l));
						}
						resetSelectedShapes();
						selectedShapes.clear();
					}
				}
				else if(tool == LINE_TYPE.TANGENT_LINE)
				{
					if(snappedIndex != -1 && shapes.get(snappedIndex) instanceof Point)
					{
						if(selectedShapes.size() == 0)
						{
							selectedShapes.add(shapes.get(snappedIndex));
						}
						else if(selectedShapes.size() == 1 && selectedShapes.get(0) instanceof Circle)
						{
							selectedShapes.add(shapes.get(snappedIndex));
						}
					}
					else if(snappedIndex != -1 && shapes.get(snappedIndex) instanceof Circle)
					{
						if(selectedShapes.size() == 0)
						{
							selectedShapes.add(shapes.get(snappedIndex));
						}
						else if(selectedShapes.size() == 1 && selectedShapes.get(0) instanceof Point)
						{
							selectedShapes.add(shapes.get(snappedIndex));
						}
					}
					if(selectedShapes.size() == 2)
					{
						Point p;
						Circle c;
						if(selectedShapes.get(0) instanceof Point)
						{
							p = (Point) selectedShapes.get(0);
							c = (Circle) selectedShapes.get(1);
						}
						else
						{
							p = (Point) selectedShapes.get(1);
							c = (Circle) selectedShapes.get(0);
						}
						double dist = Utility.dist(p.getX(), p.getY(), c.getCenterX(), c.getCenterY());
						if(Utility.equal(dist, c.getRadius()))
						{
							Line l = new Line(p, c, false);
							addShape(l);
							addCommand(new DrawCommand(l));
						}
						else if(dist > c.getRadius())
						{
							Point p1 = new Point(Utility.tangentX(p.getX(), p.getY(), c, false), Utility.tangentY(p.getX(), p.getY(), c, false));
							Point p2 = new Point(Utility.tangentX(p.getX(), p.getY(), c, true), Utility.tangentY(p.getX(), p.getY(), c, true));
							double mx = event.getSceneX();
							double my = event.getSceneY();
							if(Utility.dist(mx, my, p1.getX(), p1.getY()) < Utility.dist(mx, my, p2.getX(), p2.getY()))
							{
								Line l = new Line(p, c, false);
								addShape(l);
								addCommand(new DrawCommand(l));
							}
							else
							{
								Line l = new Line(p, c, true);
								addShape(l);
								addCommand(new DrawCommand(l));
							}
						}

						resetSelectedShapes();
						selectedShapes.clear();
					}
				}
				else if(tool == CIRCLE_TYPE.CIRCLE)
				{
					if(snappedIndex != -1 && shapes.get(snappedIndex) instanceof Point) selectedShapes.add(shapes.get(snappedIndex));
					if(selectedShapes.size() == 2)
					{
						if(selectedShapes.get(0) != selectedShapes.get(1))
						{
							Circle c = new Circle((Point) selectedShapes.get(0), (Point) selectedShapes.get(1));
							addShape(c);
							addCommand(new DrawCommand(c));
						}
						resetSelectedShapes();
						selectedShapes.clear();
						getChildren().remove(currentCircle);
					}
				}
				else if(tool == CIRCLE_TYPE.CIRCUMCIRCLE)
				{
					if(snappedIndex != -1 && shapes.get(snappedIndex) instanceof Point) selectedShapes.add(shapes.get(snappedIndex));
					if(selectedShapes.size() == 3)
					{
						Point p1 = (Point) selectedShapes.get(0);
						Point p2 = (Point) selectedShapes.get(1);
						Point p3 = (Point) selectedShapes.get(2);
						if(p1 != p2 && p2 != p3 && p1 != p3)
						{
							Circle c = new Circle(p1, p2, p3, true);
							addShape(c);
							addCommand(new DrawCommand(c));
						}
						resetSelectedShapes();
						selectedShapes.clear();
						getChildren().remove(currentCircle);
					}
				}
				else if(tool == CIRCLE_TYPE.INCIRCLE)
				{
					if(snappedIndex != -1 && shapes.get(snappedIndex) instanceof Point) selectedShapes.add(shapes.get(snappedIndex));
					if(selectedShapes.size() == 3)
					{
						Point p1 = (Point) selectedShapes.get(0);
						Point p2 = (Point) selectedShapes.get(1);
						Point p3 = (Point) selectedShapes.get(2);
						if(p1 != p2 && p2 != p3 && p1 != p3)
						{
							Circle c = new Circle(p1, p2, p3, false);
							addShape(c);
							addCommand(new DrawCommand(c));
						}
						resetSelectedShapes();
						selectedShapes.clear();
						getChildren().remove(currentCircle);
					}
				}
				/*else if(tool == CIRCLE_TYPE.TANGENT_CIRCLE)
				{
					if(snappedIndex != -1)
					{
						Shape selected = shapes.get(snappedIndex);
						if(selected instanceof Point)
						{
							int numPoints = 0;
							for(Shape s : selectedShapes)
							{
								if(s instanceof Point) numPoints++;
							}
							if(numPoints == 0) selectedShapes.add(selected);
						}
						else if(selected instanceof Circle)
						{
							int numCirc = 0;
							for(Shape s : selectedShapes)
							{
								if(s instanceof Circle) numCirc++;
							}
							if(numCirc <= 1) selectedShapes.add(selected);
						}
					}
					if(selectedShapes.size() == 3)
					{
						Circle c1 = new Circle(new Point(0, 0), new Point(0, 1)), c2 = new Circle(new Point(0, 0), new Point(0, 1));
						Point p = new Point(0, 0);

						if(selectedShapes.get(0) instanceof Circle && selectedShapes.get(1) instanceof Circle && selectedShapes.get(2) instanceof Point)
						{
							c1 = (Circle) selectedShapes.get(0);
							c2 = (Circle) selectedShapes.get(1);
							p = (Point) selectedShapes.get(2);
						}
						else if(selectedShapes.get(0) instanceof Circle && selectedShapes.get(1) instanceof Point && selectedShapes.get(2) instanceof Circle)
						{
							c1 = (Circle) selectedShapes.get(0);
							c2 = (Circle) selectedShapes.get(2);
							p = (Point) selectedShapes.get(1);
						}
						else if(selectedShapes.get(0) instanceof Point && selectedShapes.get(1) instanceof Circle && selectedShapes.get(2) instanceof Circle)
						{
							c1 = (Circle) selectedShapes.get(1);
							c2 = (Circle) selectedShapes.get(2);
							p = (Point) selectedShapes.get(0);
						}
						if(c1 != c2)
						{
							if(Utility.equal(Utility.distToShape(c1.getCenterX(), c1.getCenterY(), p), c1.getRadius()))
							{
								Circle c = new Circle(c1, c2, p, false);
								addShape(c);
								addCommand(new DrawCommand(c));
							}
							else if(Utility.equal(Utility.distToShape(c2.getCenterX(), c2.getCenterY(), p), c2.getRadius()))
							{
								Circle c = new Circle(c2, c1, p, false);
								addShape(c);
								addCommand(new DrawCommand(c));
							}
						}
						resetSelectedShapes();
						selectedShapes.clear();
						getChildren().remove(currentCircle);
					}
				}*/

				pmouseX = event.getSceneX();
				pmouseY = event.getSceneY();

				setSelectedShapes();
				clearGarbage();
			}
		});
		this.setOnMouseDragged(new EventHandler<MouseEvent>()
		{
			public void handle(MouseEvent event)
			{
				SHAPE_TYPE tool = tools.getSelectedTool();
				if(tool == MOUSE.MOUSE || tool instanceof POINT_TYPE)
				{
					if(snappedIndex != -1)
					{
						Shape s = shapes.get(snappedIndex);
						if(s.getType() == POINT_TYPE.POINT)
						{
							shapeDragged = true;
							((Point) s).setX(Math.max(0, event.getSceneX()));
							((Point) s).setY(Math.max(0, event.getSceneY()));
						}
						else if(s.getType() == POINT_TYPE.POINT_ON_SHAPE)
						{
							shapeDragged = true;
							((Point) s).setRelativeLocation(event.getSceneX(), event.getSceneY());
						}
					}
					else if(tool == MOUSE.MOUSE)
					{
						//setCursor(Cursor.CLOSED_HAND);
						double dx = event.getSceneX()-pmouseX;
						double dy = event.getSceneY()-pmouseY;
						totalTranslateX += dx;
						totalTranslateY += dy;
						translate(dx, dy);
					}
				}
				else if(tool == MOUSE.DRAG)
				{
					if(snappedIndex != -1 && shapes.get(snappedIndex) instanceof Point)
					{
						shapeDragged = true;
						Point p = (Point) shapes.get(snappedIndex);
						double cx = event.getSceneX();
						double cy = event.getSceneY();
						double direction = 0;
						double x = p.getX()+(cx-p.getX())/Utility.dist(cx, cy, p.getX(), p.getY());
						double y = p.getY()+(cy-p.getY())/Utility.dist(cx, cy, p.getX(), p.getY());
						direction = Math.atan((y-p.getY())/(x-p.getX()));
						if(x < p.getX()) direction += Math.PI;
						p.getLabel().setDirection(-direction);
					}
				}

				pmouseX = event.getSceneX();
				pmouseY = event.getSceneY();
			}
		});
		this.setOnMouseReleased(new EventHandler<MouseEvent>()
		{
			public void handle(MouseEvent event)
			{
				SHAPE_TYPE tool = tools.getSelectedTool();
				if(shapeDragged)
				{
					if(tool == MOUSE.MOUSE || tool instanceof POINT_TYPE)
					{
						Shape s = shapes.get(snappedIndex);
						if(s.getType() == POINT_TYPE.POINT)
						{
							double x = Math.max(0, event.getSceneX());
							double y = Math.max(0, event.getSceneY());
							((Point) s).setX(x);
							((Point) s).setY(y);
							addCommand(new MoveCommand((Point) s, x, y));
						}
						else if(s.getType() == POINT_TYPE.POINT_ON_SHAPE)
						{
							((Point) s).setRelativeLocation(event.getSceneX(), event.getSceneY());
							addCommand(new MoveCommand((Point) s, event.getSceneX(), event.getSceneY()));
						}
					}
					else if(tool == MOUSE.DRAG)
					{
						Point p = (Point) shapes.get(snappedIndex);
						double cx = event.getSceneX();
						double cy = event.getSceneY();
						double direction = 0;
						double x = p.getX()+(cx-p.getX())/Utility.dist(cx, cy, p.getX(), p.getY());
						double y = p.getY()+(cy-p.getY())/Utility.dist(cx, cy, p.getX(), p.getY());
						direction = Math.atan((y-p.getY())/(x-p.getX()));
						if(x < p.getX()) direction += Math.PI;
						p.getLabel().setDirection(-direction);
						addCommand(new DragCommand(p, -direction));
					}
				}
				if(getCursor() == Cursor.CLOSED_HAND)
				{
					setCursor(Cursor.OPEN_HAND);
					addCommand(new TranslateCommand(totalTranslateX, totalTranslateY));
					totalTranslateX = 0;
					totalTranslateY = 0;
				}
				shapeDragged = false;
			}
		});

		//handle zooming with zoom gesture or scrolling
		this.setOnZoom(new EventHandler<ZoomEvent>()
		{
			public void handle(ZoomEvent event)
			{
				zoom(event.getSceneX(), event.getSceneY(), event.getZoomFactor());
			}
		});
		this.setOnZoomFinished(new EventHandler<ZoomEvent>()
		{
			public void handle(ZoomEvent event)
			{
				addCommand(new ZoomCommand(event.getSceneX(), event.getSceneY(), event.getTotalZoomFactor()));
			}
		});
		this.setOnScroll(new EventHandler<ScrollEvent>()
		{
			private int framesMissed = 0;
			public void handle(ScrollEvent event)
			{
				framesMissed = 0;
				if(totalScroll == 0)
				{
					AnimationTimer update = new AnimationTimer()
					{
						public void handle(long now)
						{
							if(framesMissed >= 10) //missing greater than 10 frames means scroll is over
							{
								addCommand(new ZoomCommand(event.getSceneX(), event.getSceneY(), Math.pow(Math.E, totalScroll/1000)));
								totalScroll = 0;
								stop();
							}
							framesMissed++;
						}
					};
					update.start();
				}
				double factor = Math.pow(Math.E, event.getDeltaY()/1000);
				totalScroll += event.getDeltaY();
				//System.out.println(totalScroll);
				zoom(event.getSceneX(), event.getSceneY(), factor);
			}
		});
	}

	/**
	 * Returns the shapes in this pane.
	 * @return shapes
	 */
	public ArrayList<Shape> getShapes()
	{
		return shapes;
	}

	/**
	 * Adds a new shape to this pane, checking whether a shape with the same name already exists.
	 * @param shape shape to add
	 */
	public void addShape(Shape shape)
	{
		if(isDuplicateName(shape.getName())) return;
		shapes.add(shape);
		shape.draw(this);
		shape.getObject().toBack();
		shape.getLabel().toBack();
	}

	/**
	 * Adds new shapes to this pane, checking whether a shape with the same name as each of shapes already exists.
	 * @param shapes shapes to add
	 */
	public void addShapes(Shape... shapes)
	{
		for(Shape s : shapes)
		{
			if(isDuplicateName(s.getName())) continue;
			this.shapes.add(s);
			s.draw(this);
			s.getObject().toBack();
			s.getLabel().toBack();
		}
	}

	/**
	 * Searches the current shapes in the AsyPadPane for a shape with the given name.
	 * @param name name of shape to search for
	 * @return the shape with the given name, or null if it is not found
	 */
	public Shape findShapeByName(String name)
	{
		for(Shape s : shapes)
		{
			if(s.getName().equals(name)) return s;
		}
		return null;
	}

	/**
	 * Clears the AsyPadPane by deleting all shapes and resetting the Stroke Width to the default.
	 */
	public void clear()
	{
		for(Shape s : shapes) s.delete();
		update();
		Shape.StrokeWidth = 3;
	}

	/**
	 * Adds a new command to the AsyPadPane.
	 * @param c the new command
	 */
	public void addCommand(Command c)
	{
		while(commands.size()-1 > currentCommandIndex)
		{
			commands.remove(commands.size()-1);
		}
		commands.add(c);
		currentCommandIndex++;
	}

	/**
	 * Undoes a command in the AsyPadPane.
	 */
	public void undo()
	{
		resetSelectedShapes();
		selectedShapes.clear();
		getChildren().remove(currentLine);
		getChildren().remove(currentCircle);

		if(currentCommandIndex > -1) currentCommandIndex--;
		clear();
		for(int i = 0; i <= currentCommandIndex; i++)
		{
			commands.get(i).doAction(this);
		}
		update();
	}

	/**
	 * Redoes a command in the AsyPadPane.
	 */
	public void redo()
	{
		resetSelectedShapes();
		selectedShapes.clear();
		getChildren().remove(currentLine);
		getChildren().remove(currentCircle);

		if(currentCommandIndex < commands.size()-1)
		{
			currentCommandIndex++;
			commands.get(currentCommandIndex).doAction(this);
		}
		update();
	}

	/**
	 * Updates the AsyPadPane by 
	 * deleting all shapes with {@code remove == true} (this should be called each time {@code delete()} is called on a shape),
	 * hiding all shapes with {@code hidden == true} and showing shapes with {@code hidden == false} (should be called after each {@code setHidden()} call),
	 * and refreshing all shapes.
	 */
	public void update()
	{
		int MAX = shapes.size();
		for(int i = 0; i < MAX; i++)
		{
			for(Shape s : shapes)
			{
				if(s.remove())
				{
					shapes.remove(s);
					getChildren().remove(s.getObject());
					getChildren().remove(s.getLabel());
					break;
				}
				else if(s.isHidden())
				{
					getChildren().remove(s.getObject());
					getChildren().remove(s.getLabel());
				}
				else
				{
					if(!getChildren().contains(s.getObject()))
					{
						getChildren().add(s.getObject());
						s.getObject().toBack();
					}
					if(!getChildren().contains(s.getLabel()) && s.getLabel() != null)
					{
						getChildren().add(s.getLabel());
						s.getLabel().toBack();
					}
				}
			}
		}
		for(Shape s : shapes)
		{
			s.refresh();
		}
	}

	/**
	 * Updates the label that shows the current tool.
	 * @param tool new current tool
	 */
	public void updateTool(String tool)
	{
		currentTool.setText("Tool: " + tool);
		currentTool.requestFocus(); //needed for key listener to be valid on the pane.
		resetSelectedShapes();
		selectedShapes.clear();
		if(getChildren().contains(currentLine))
		{
			getChildren().remove(currentLine);
		}
		if(getChildren().contains(currentCircle))
		{
			getChildren().remove(currentCircle);
		}
	}

	/**
	 * Sets the description of the current tool.
	 * @param description description of tool.
	 */
	public void updateToolDescription(String description)
	{
		currentToolDescription.setText(description);
	}

	/**
	 * Shows a live updating Asymptote Panel.
	 */
	public void showAsyPanel()
	{
		Stage asyPanel = new Stage();
		FlowPane p = new FlowPane();
		Scene scene = new Scene(p, 500, 600);

		TextArea asymptote = new TextArea();
		asymptote.setPrefSize(500, 600);
		asymptote.setEditable(false);
		asymptote.setText(toAsymptote());

		AnimationTimer update = new AnimationTimer()
		{
			private long lastUpdate = 0;
			public void handle(long now)
			{
				if(now-lastUpdate > 1e9)
				{
					lastUpdate = now;
					double vScroll = asymptote.getScrollTop();
					double hScroll = asymptote.getScrollLeft();
					if(!toAsymptote().equals(asymptote.getText()))
					{
						asymptote.setText(toAsymptote());
					}
					asymptote.setScrollTop(vScroll);
					asymptote.setScrollLeft(hScroll);
				}
			}
		};
		update.start();

		p.getChildren().add(asymptote);
		asyPanel.setScene(scene);
		asyPanel.setAlwaysOnTop(true);
		asyPanel.setTitle("Live Updating Asymptote Panel");
		asyPanel.setResizable(false);
		asyPanel.show();
	}

	/**
	 * Translates the entire figure by the given amount.
	 * @param dx translation in x direction
	 * @param dy translation in y direction
	 */
	public void translate(double dx, double dy)
	{
		for(Shape s : shapes)
		{
			//move all dependency level 0 shapes by dx and dy, all children will follow
			if(s.getLevel() == 0)
			{
				Point p = (Point) s;
				p.setX(p.getX()+dx);
				p.setY(p.getY()+dy);
			}
		}
		update();
	}

	/**
	 * Zooms by the specified factor with respect to the specified location
	 * @param zx x-coordinate of zoom
	 * @param zy y-coordinate of zoom
	 * @param factor zoom factor
	 */
	public void zoom(double zx, double zy, double factor)
	{
		for(Shape s : shapes)
		{
			//zoom all dependency level 0 shapes by dx and dy, all children will follow
			if(s.getLevel() == 0)
			{
				Point p = (Point) s;
				p.setX(zx+factor*(p.getX()-zx));
				p.setY(zy+factor*(p.getY()-zy));
			}
		}
		update();
	}

	/**
	 * Clears out the garbage shapes that were used for calculation but are not needed anymore.
	 */
	private void clearGarbage()
	{
		for(Shape s : shapes)
		{
			for(int i = 0; i < s.getChildren().size(); i++)
			{
				if(!shapes.contains(s.getChildren().get(i)))
				{
					s.getChildren().remove(i);
					i--;
				}
			}
		}
	}

	/**
	 * Shows the configuration window for a point.
	 * @param p the point
	 */
	private void showConfigurePoint(Point p)
	{
		Stage rename = new Stage();
		FlowPane flowPane = new FlowPane();
		Scene renameScene = new Scene(flowPane, 580, 60);
		flowPane.getChildren().add(new Label("Rename Point " + p.getLabel().getText() + ":"));
		TextField name = new TextField();
		name.setPromptText("Enter new name of point");
		Button submit = new Button("Submit");
		Button cancel = new Button("Cancel");
		Button delete = new Button("Delete Point");
		Button hide = new Button("Hide Point");
		Label chooseColor = new Label("Set Point Color: ");
		flowPane.getChildren().addAll(name, submit, cancel, delete, hide, chooseColor, colorPicker);
		cancel.requestFocus();
		submit.setOnAction(new EventHandler<ActionEvent>()
		{
			public void handle(ActionEvent event)
			{
				String pname = name.getText();
				if(!isValidPointName(pname)) return;
				p.getLabel().setText(pname);
				p.refreshName();
				addCommand(new RenameCommand(p, pname));
				rename.close();
			}
		});
		cancel.setOnAction(new EventHandler<ActionEvent>()
		{
			public void handle(ActionEvent event)
			{
				rename.close();
			}
		});
		delete.setOnAction(new EventHandler<ActionEvent>()
		{
			public void handle(ActionEvent event)
			{
				addCommand(new DeleteCommand(p));
				p.delete();
				update();
				snappedIndex = -1;
				rename.close();
			}
		});
		hide.setOnAction(new EventHandler<ActionEvent>()
		{
			public void handle(ActionEvent event)
			{
				addCommand(new HideCommand(p));
				p.setHidden(true);
				update();
				snappedIndex = -1;
				rename.close();
			}
		});
		colorPicker.setValue(p.getColor());
		colorPicker.setOnAction(new EventHandler<ActionEvent>()
		{
			public void handle(ActionEvent e)
			{
				Color c = colorPicker.getValue();
				p.setColor(c);
				addCommand(new ColorCommand(p, c));
			}
		});
		rename.setScene(renameScene);
		rename.setAlwaysOnTop(true);
		rename.initStyle(StageStyle.UTILITY);
		rename.setTitle("Point Options");
		rename.show();
	}

	/**
	 * Shows configuration window for the specified shape.
	 * @param s shape to show configuration for
	 */
	private void showConfigureShape(Shape s)
	{
		if(s instanceof Point)
		{
			showConfigurePoint((Point) s);
			return;
		}
		else
		{
			Stage configure = new Stage();
			FlowPane flowPane = new FlowPane();
			Scene renameScene = new Scene(flowPane, 250, 60);
			Button cancel = new Button("Cancel");
			Button delete = new Button("Delete Shape");
			Button hide = new Button("Hide Shape");
			Label chooseColor = new Label("Set Shape Color: ");
			flowPane.getChildren().addAll(cancel, delete, hide, chooseColor, colorPicker);
			cancel.requestFocus();
			cancel.setOnAction(new EventHandler<ActionEvent>()
			{
				public void handle(ActionEvent event)
				{
					configure.close();
				}
			});
			delete.setOnAction(new EventHandler<ActionEvent>()
			{
				public void handle(ActionEvent event)
				{
					addCommand(new DeleteCommand(s));
					s.delete();
					update();
					snappedIndex = -1;
					configure.close();
				}
			});
			hide.setOnAction(new EventHandler<ActionEvent>()
			{
				public void handle(ActionEvent event)
				{
					addCommand(new HideCommand(s));
					s.setHidden(true);
					update();
					snappedIndex = -1;
					configure.close();
				}
			});
			colorPicker.setValue(s.getColor());
			colorPicker.setOnAction(new EventHandler<ActionEvent>()
			{
				public void handle(ActionEvent e)
				{
					Color c = colorPicker.getValue();
					s.setColor(c);
					addCommand(new ColorCommand(s, c));
				}
			});
			configure.setScene(renameScene);
			configure.setAlwaysOnTop(true);
			configure.initStyle(StageStyle.UTILITY);
			configure.setTitle("Shape Options");
			configure.show();
		}
	}

	/**
	 * Updates the current line.
	 * @param x1 start x-coordinate
	 * @param y1 start y-coordinate
	 * @param x2 end x-coordinate
	 * @param y2 end y-coordinate
	 */
	private void setCurrentLine(double x1, double y1, double x2, double y2)
	{
		currentLine.setStartX(x1);
		currentLine.setStartY(y1);
		currentLine.setEndX(x2);
		currentLine.setEndY(y2);

		currentLine.setStrokeWidth(Shape.StrokeWidth);
		if(!getChildren().contains(currentLine))
		{
			getChildren().add(currentLine);
			currentLine.toBack();
		}
	}

	/**
	 * Updates the current circle.
	 * @param cx x-coordinate of center
	 * @param cy y-coordinate of center
	 * @param radius radius of circle
	 */
	private void setCurrentCircle(double cx, double cy, double radius)
	{
		currentCircle.setCenterX(cx);
		currentCircle.setCenterY(cy);
		currentCircle.setRadius(radius);

		currentCircle.setStrokeWidth(Shape.StrokeWidth);
		if(!getChildren().contains(currentCircle))
		{
			getChildren().add(currentCircle);
			currentCircle.toBack();
		}
	}

	/**
	 * Sets the stroke color of all selected shapes to red.
	 */
	private void setSelectedShapes()
	{
		for(Shape s : selectedShapes)
		{
			s.getObject().setStroke(selected);
			if(s instanceof Point)
			{
				s.getObject().setFill(selected);
			}
		}
	}

	/**
	 * Resets the stroke color of all selected shapes to their original color.
	 */
	private void resetSelectedShapes()
	{
		for(Shape s : selectedShapes)
		{
			s.getObject().setStroke(s.getColor());
			if(s instanceof Point)
			{
				s.getObject().setFill(s.getColor());
			}
		}
	}

	/**
	 * Returns the next available point name. The default point names are {@code A, B,..., Z, AA, AB,..., AZ, BA,..., AAA,...}
	 * @param length length of name to search
	 * @return next available point name
	 */
	private String nextPointName(int length)
	{
		for(int i = 0; i < Math.pow(27, length); i++)
		{
			int k = i;
			String name = "";
			for(int j = 0; j < length; j++)
			{
				if(k%27==0)
				{
					continue;
				}
				name=(char)('A'+k%27-1)+name;
				k/=27;
			}
			if(name.equals("")) continue;
			boolean broken = false;
			for(Shape s : shapes)
			{
				if(s.getName().equals(name))
				{
					broken = true;
					break;
				}
			}
			if(!broken) return name;
		}
		return nextPointName(length+1);
	}

	/**
	 * Checks whether the string is a valid point name.
	 * @param pname
	 * @return if the string is a valid point name.
	 */
	private boolean isValidPointName(String pname)
	{
		if(isDuplicateName(pname)) return false; //check for duplicate name
		if(pname.length() == 0) return false; //no empty name
		if(pname.charAt(0) < 'A' || pname.charAt(0) > 'Z') return false; //must start with capital letter
		boolean isAllLettersOrNumbers = true;
		for(char c : pname.toCharArray())
		{
			if((c < '0' || c > '9') && (c < 'A' || c > 'Z')) isAllLettersOrNumbers = false;
		}

		if(isAllLettersOrNumbers) return true;
		else if(pname.length() == 2) //check for a point prime
		{
			if(pname.charAt(1) != '\'') return false;
			else return true;
		}
		else if(pname.length() == 3) //subscript names
		{
			if(pname.charAt(1) != '_') return false;
			if((pname.charAt(2) < '0' || pname.charAt(2) > '9') && (pname.charAt(2) < 'a' || pname.charAt(2) > 'z') && (pname.charAt(2) < 'A' || pname.charAt(2) > 'Z')) return false;
			else return true;
		}
		else return false;
	}

	/**
	 * Checks if the given name is shared by a shape that is already drawn.
	 * @param name name to check
	 * @return if the name already exists
	 */
	private boolean isDuplicateName(String name)
	{
		for(Shape s : shapes)
		{
			if(s.getName().equals(name)) return true;
		}
		return false;
	}

	/**
	 * Loads an AsyPad file into the AsyPad.
	 * @param apad file to be loaded
	 */
	public void loadApad(File apad)
	{
		clear();
		commands.clear();
		currentCommandIndex = -1;
		try(FileReader fr = new FileReader(apad);
				BufferedReader br = new BufferedReader(fr);)
		{
			String currentLine;
			while((currentLine = br.readLine()) != null)
			{
				Command.loadCommand(currentLine, this);
			}
		}
		catch(IOException ioe)
		{
			ioe.printStackTrace();
		}
		update();
		updateToolDescription("Loaded diagram from " + apad.getAbsolutePath());
	}

	/**
	 * Converts the current state of the AsyPad into .apad code for file i/o.
	 * @return .apad code for current state of AsyPad
	 */
	public String toApad()
	{
		String apad = "";
		clear();
		for(int i = 0; i <= currentCommandIndex; i++)
		{
			//renaming a point makes the command's generated string buggy so for rename
			//commands we execute it after adding it to the apad string.
			if(!(commands.get(i) instanceof RenameCommand)) commands.get(i).doAction(this);
			apad += commands.get(i).toString();
			if(commands.get(i) instanceof RenameCommand) commands.get(i).doAction(this);
		}
		update();
		return apad;
	}

	/**
	 * Converts the current state of the AsyPad into Asymptote code.
	 * @return Asymptote code representing the current state
	 */
	public String toAsymptote()
	{
		for(Shape s : shapes) s.addToAsy(); //adds all shapes to asymptote code
		for(Shape s : shapes)
		{
			if(s instanceof Point)
			{
				//removes all shapes that have undefined coordinates or depend on it.
				if(((Point)s).getX() == Double.POSITIVE_INFINITY) s.removeFromAsy();
			}
			if(s instanceof Line && s.getType() == LINE_TYPE.TANGENT_LINE)
			{
				Point p = (Point) s.getDependencies().get(0);
				Circle c = (Circle) s.getDependencies().get(1);
				double dist = Utility.dist(p.getX(), p.getY(), c.getCenterX(), c.getCenterY());
				if(!Utility.equal(dist, c.getRadius()) && dist < c.getRadius())
				{
					//remove non-existent tangent lines
					s.removeFromAsy();
				}
			}
		}
		String asy = "// Generated By AsyPadv" + AsyPad.VERSION + "\n";
		asy+="import olympiad;\nimport markers;\nimport math;\nimport graph;\n";
		asy+="// change the unit size to fit your needs\n";
		asy+="unitsize(1cm);\n";

		ArrayList<String> colors = new ArrayList<String>();
		for(Shape s : shapes)
		{
			if(s.isInAsyCode())
			{
				String c = Utility.hex(s.getColor());
				if(!colors.contains(c))
				{
					colors.add(c);
				}
			}
		}
		asy+="// colored pens\n";
		for(String color : colors)
		{
			asy+="pen c" + color + " = rgb(\"" + color + "\");\n";
		}

		int MAXLVL = 0;
		for(Shape s : shapes)
		{
			if(s.getLevel() > MAXLVL && s.isInAsyCode()) MAXLVL = s.getLevel();
		}
		for(int i = 0; i <= MAXLVL; i++)
		{
			asy += "// dependency level " + i + "\n";
			if(i == 0)
			{
				asy += "/* You can change the coordinates of these points of dependency level 0.\n "
						+ "The drawing will retain the same relationships and qualities.\n "
						+ "Please be aware that as a result of this some of the image may be clipped off. */\n";
			}
			if(i == 1)
			{
				asy += "// Do not change anything below, unless you are experienced in Asymptote.\n";
			}
			for(Shape s : shapes)
			{
				if(s.getLevel() == i)
				{
					asy += s.toAsymptote();
				}
			}
		}
		double xmin = 0;
		double xmax = getWidth()/100;
		double ymin = (Shape.INF-getHeight())/100;
		double ymax = Shape.INF/100;
		asy+="// clip the drawing view\n";
		asy+="clip((" + xmin + ", " + ymin + ")--(" + xmin + ", " + ymax + ")--(" + xmax + ", " + ymax + ")--(" + xmax + ", " + ymin + ")--cycle);";
		return asy;
	}
}
