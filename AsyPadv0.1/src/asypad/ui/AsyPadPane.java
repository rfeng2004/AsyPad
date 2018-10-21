package asypad.ui;

import java.util.ArrayList;

import javafx.event.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
//import javafx.scene.paint.Color;
import javafx.stage.Stage;
import asypad.shapes.*;
import asypad.shapes.types.*;
import asypad.shapes.types.SHAPE_TYPE.MOUSE;
import asypad.ui.menus.*;

/**
 * A special pane that interacts with the user and draws shapes.
 * @author Raymond Feng
 * @version 0.2
 */
public class AsyPadPane extends Pane
{
	private ArrayList<Shape> shapes;
	private int snappedIndex;
	private ArrayList<Shape> selectedShapes;
	private ArrayList<Shape> snappedShapes;
	private static final double snapForce = 5*Shape.StrokeWidth;
	private Label currentTool;
	private Label currentToolDescription;
	private javafx.scene.shape.Line currentLine;
	private javafx.scene.shape.Circle currentCircle;

	/**
	 * Creates an AsyPadPane layout.
	 */
	public AsyPadPane()
	{
		super();
		shapes = new ArrayList<Shape>();
		selectedShapes = new ArrayList<Shape>();
		snappedShapes = new ArrayList<Shape>();
		currentLine = new javafx.scene.shape.Line();
		currentCircle = new javafx.scene.shape.Circle();
		currentCircle.setFill(Color.TRANSPARENT);
		currentCircle.setStroke(Color.BLACK);

		AsyPadMenuBar menus = new AsyPadMenuBar(this);
		AsyPadToolBar tools = new AsyPadToolBar(this);
		tools.setLayoutY(menus.getPrefHeight());
		currentTool = new Label("Tool: Mouse");
		currentTool.setLayoutX(220);
		currentTool.setLayoutY(10 + menus.getPrefHeight());
		currentToolDescription = new Label("Drag points or double click on point to configure.");
		currentToolDescription.setLayoutX(420);
		currentToolDescription.setLayoutY(10 + menus.getPrefHeight());

		getChildren().addAll(menus, tools, currentTool, currentToolDescription);
		this.setOnMouseMoved(new EventHandler<MouseEvent>()
		{
			public void handle(MouseEvent event)
			{
				snappedIndex = -1;
				snappedShapes.clear();
				for(Shape s : shapes)
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
				if(snappedIndex == -1)
				{
					setCursor(Cursor.DEFAULT);
				}

				SHAPE_TYPE tool = tools.getSelectedTool();
				if(tool == LINE_TYPE.SEGMENT || tool == LINE_TYPE.LINE)
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
						if(tool == LINE_TYPE.SEGMENT)
						{
							setCurrentLine(p.getX(), p.getY(), x, y);
						}
						else
						{
							setCurrentLine(p.getX()-1000*(x-p.getX()), p.getY()-1000*(y-p.getY()), x+1000*(x-p.getX()), y+1000*(y-p.getY()));
						}
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
						setCurrentCircle(p.getX(), p.getY(), Utility.dist(p.getX(), p.getY(), x, y));
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
						Circle c = new Circle(new Point(x, y), p1, p2);
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
					if(snappedIndex != -1 && shapes.get(snappedIndex) instanceof Point && event.getClickCount() == 2)
					{
						Point p = (Point) shapes.get(snappedIndex);
						//configure point
						showConfigurePoint(p);
					}
				}
				if(tool == MOUSE.DELETE)
				{
					if(snappedIndex != -1)
					{
						shapes.get(snappedIndex).delete();
						update();
						snappedIndex = -1;
					}
				}
				else if(tool == POINT_TYPE.POINT)
				{
					ArrayList<Line> lines = new ArrayList<Line>();
					for(Shape s : snappedShapes)
					{
						if(s instanceof Line)
						{
							lines.add((Line) s);
						}
					}
					if(snappedIndex == -1)
					{
						addShape(new Point(event.getSceneX(), event.getSceneY()));
						snappedIndex = shapes.size()-1;
						setCursor(Cursor.HAND);
					}
					else if(!(shapes.get(snappedIndex) instanceof Point) && lines.size() < 2)
					{
						addShape(new Point(event.getSceneX(), event.getSceneY(), shapes.get(snappedIndex), ""));
						snappedIndex = shapes.size()-1;
						setCursor(Cursor.HAND);
					}
					else
					{
						if(lines.size() >= 2)
						{
							addShape(new Point(lines.get(0), lines.get(1), ""));
						}
					}
				}
				else if(tool == POINT_TYPE.POINT_ON_SHAPE)
				{
					if(snappedIndex != -1 && !(shapes.get(snappedIndex) instanceof Point))
					{
						addShape(new Point(event.getSceneX(), event.getSceneY(), shapes.get(snappedIndex), ""));
						snappedIndex = shapes.size()-1;
						setCursor(Cursor.HAND);
					}
				}
				else if(tool == POINT_TYPE.INTERSECTION_POINT)
				{
					ArrayList<Line> lines = new ArrayList<Line>();
					for(Shape s : snappedShapes)
					{
						if(s instanceof Line)
						{
							lines.add((Line) s);
						}
					}
					if(lines.size() >= 2)
					{
						addShape(new Point(lines.get(0), lines.get(1), ""));
					}
				}
				else if(tool == LINE_TYPE.SEGMENT || tool == LINE_TYPE.LINE)
				{
					if(snappedIndex != -1 && shapes.get(snappedIndex) instanceof Point) selectedShapes.add(shapes.get(snappedIndex));
					if(selectedShapes.size() == 2)
					{
						if(tool == LINE_TYPE.SEGMENT)
						{
							addShape(new Line((Point) selectedShapes.get(0), (Point) selectedShapes.get(1), true));
						}
						else 
						{
							addShape(new Line((Point) selectedShapes.get(0), (Point) selectedShapes.get(1), false));
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
								addShape(new Line((Point) selectedShapes.get(0), (Line) selectedShapes.get(1), true));
							}
							else 
							{
								addShape(new Line((Point) selectedShapes.get(0), (Line) selectedShapes.get(1), false));
							}
						}
						else 
						{
							if(tool == LINE_TYPE.PARALLEL_LINE)
							{
								addShape(new Line((Point) selectedShapes.get(1), (Line) selectedShapes.get(0), true));
							}
							else
							{
								addShape(new Line((Point) selectedShapes.get(1), (Line) selectedShapes.get(0), false));
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
						addShape(new Line((Point) selectedShapes.get(0), (Point) selectedShapes.get(1), (Point) selectedShapes.get(2)));
						resetSelectedShapes();
						selectedShapes.clear();
					}
				}
				else if(tool == LINE_TYPE.PERPENDICULAR_BISECTOR)
				{
					if(snappedIndex != -1 && shapes.get(snappedIndex) instanceof Point) selectedShapes.add(shapes.get(snappedIndex));
					if(selectedShapes.size() == 2)
					{
						addShape(new Line((Point) selectedShapes.get(0), (Point) selectedShapes.get(1)));
						resetSelectedShapes();
						selectedShapes.clear();
					}
				}
				else if(tool == CIRCLE_TYPE.CIRCLE)
				{
					if(snappedIndex != -1 && shapes.get(snappedIndex) instanceof Point) selectedShapes.add(shapes.get(snappedIndex));
					if(selectedShapes.size() == 2)
					{
						addShape(new Circle((Point) selectedShapes.get(0), (Point) selectedShapes.get(1)));
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
							addShape(new Circle(p1, p2, p3));
						}
						resetSelectedShapes();
						selectedShapes.clear();
						getChildren().remove(currentCircle);
					}
				}

				setSelectedShapes();
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
							((Point) s).setX(Math.max(0, event.getSceneX()));
							((Point) s).setY(Math.max(0, event.getSceneY()));
						}
						else if(s.getType() == POINT_TYPE.POINT_ON_SHAPE)
						{
							((Point) s).setRelativeLocation(event.getSceneX(), event.getSceneY());
						}
					}
				}
			}
		});
	}

	/**
	 * Adds a new shape to this pane.
	 * @param shape shape to add
	 */
	public void addShape(Shape shape)
	{
		shapes.add(shape);
		shape.draw(this);
		shape.getObject().toBack();
	}

	/**
	 * Adds new shapes to this pane.
	 * @param shapes shapes to add
	 */
	public void addShapes(Shape... shapes)
	{
		for(Shape s : shapes)
		{
			this.shapes.add(s);
			s.draw(this);
			s.getObject().toBack();
		}
	}

	/**
	 * Updates the AsyPadPane by 
	 * deleting all shapes with remove = true (this should be called each time delete() is called on a shape)
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
					if(getChildren().contains(s.getObject())) getChildren().remove(s.getObject());
					if(getChildren().contains(s.getLabel())) getChildren().remove(s.getLabel());
					break;
				}
			}
		}
		for(Shape s : shapes)
		{
			s.refresh();
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
		Scene renameScene = new Scene(flowPane, 500, 50);
		flowPane.getChildren().add(new Label("Rename Point " + p.getLabel().getText() + ":"));
		TextField name = new TextField();
		name.setPromptText("Enter new name of point");
		Button submit = new Button("Submit");
		Button cancel = new Button("Cancel");
		Button delete = new Button("Delete Point");
		flowPane.getChildren().addAll(name, submit, cancel, delete);
		cancel.requestFocus();
		submit.setOnAction(new EventHandler<ActionEvent>()
		{
			public void handle(ActionEvent event)
			{
				String pname = name.getText();
				if(pname.length() != 1) return;
				p.getLabel().setText(pname);
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
				shapes.get(snappedIndex).delete();
				update();
				snappedIndex = -1;
				rename.close();
			}
		});
		rename.setScene(renameScene);
		rename.show();
	}

	/**
	 * Updates the label that shows the current tool.
	 * @param tool new current tool
	 */
	public void updateTool(String tool)
	{
		currentTool.setText("Tool: " + tool);
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
	 * @param cx
	 * @param cy
	 * @param radius
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
			s.getObject().setStroke(Color.RED);
			if(s instanceof Point)
			{
				s.getObject().setFill(Color.RED);
			}
		}
	}

	/**
	 * Resets the stroke color of all selected shapes to black.
	 */
	private void resetSelectedShapes()
	{
		for(Shape s : selectedShapes)
		{
			s.getObject().setStroke(Color.BLACK);
			if(s instanceof Point)
			{
				s.getObject().setFill(Color.BLACK);
			}
		}
	}

	/**
	 * Converts the current state of the AsyPad into Asymptote code.
	 * @return Asymptote code representing the current state
	 */
	public String toAsymptote()
	{
		return null;
	}
}
