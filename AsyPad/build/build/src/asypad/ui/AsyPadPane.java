package asypad.ui;
/*
 * TODO Add new tool: tangents, relative point.
 * TODO Implement grid show and hide.
 * TODO Add user manual in help menu.
 * TODO Add undo/redo functionality.
 */

import java.util.ArrayList;
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
import asypad.ui.menus.*;

/**
 * A special pane that interacts with the user and draws shapes.
 * @author Raymond Feng
 * @version 0.1
 */
public class AsyPadPane extends Pane
{
	/**
	 * Arraylist of all drawn Shapes.
	 */
	private ArrayList<Shape> shapes;
	
	/**
	 * Index pointing to the shape that the mouse is snapped to.
	 */
	private int snappedIndex;
	
	/**
	 * Arraylist of user selected shapes.
	 */
	private ArrayList<Shape> selectedShapes;
	
	/**
	 * Arraylist of shapes that the mouse is snapped to.
	 */
	private ArrayList<Shape> snappedShapes;
	
	/**
	 * Specifies the strength of the snapping system, i.e. how close the mouse has to be to snap.
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
					setCursor(Cursor.DEFAULT);
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
					if(snappedIndex != -1 && event.getClickCount() == 2)
					{
						showConfigureShape(shapes.get(snappedIndex));
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
						addShape(new Point(event.getSceneX(), event.getSceneY(), nextPointName(1)));
						snappedIndex = shapes.size()-1;
						setCursor(Cursor.HAND);
					}
					else if(!(shapes.get(snappedIndex) instanceof Point) && snappedShapes.size() == 1)
					{
						addShape(new Point(event.getSceneX(), event.getSceneY(), shapes.get(snappedIndex), nextPointName(1)));
						snappedIndex = shapes.size()-1;
						setCursor(Cursor.HAND);
					}
					else
					{
						if(lines.size() >= 2)
						{
							addShape(new Point(lines.get(0), lines.get(1), nextPointName(1)));
						}
						else if(lines.size() == 1 && circles.size() >= 1)
						{
							Point p1 = new Point((Line) lines.get(0), (Circle) circles.get(0), false, nextPointName(1));
							Point p2 = new Point((Line) lines.get(0), (Circle) circles.get(0), true, nextPointName(1));
							if(Utility.distToShape(event.getSceneX(), event.getSceneY(), p1) < Utility.distToShape(event.getSceneX(), event.getSceneY(), p2))
							{
								addShape(p1);
							}
							else addShape(p2);
						}
					}
				}
				else if(tool == POINT_TYPE.POINT_ON_SHAPE)
				{
					if(snappedIndex != -1 && !(shapes.get(snappedIndex) instanceof Point))
					{
						addShape(new Point(event.getSceneX(), event.getSceneY(), shapes.get(snappedIndex), nextPointName(1)));
						snappedIndex = shapes.size()-1;
						setCursor(Cursor.HAND);
					}
				}
				else if(tool == POINT_TYPE.INTERSECTION_POINT)
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
					if(lines.size() >= 2)
					{
						addShape(new Point(lines.get(0), lines.get(1), nextPointName(1)));
					}
					else if(lines.size() == 1 && circles.size() >= 1)
					{
						Point p1 = new Point((Line) lines.get(0), (Circle) circles.get(0), false, nextPointName(1));
						Point p2 = new Point((Line) lines.get(0), (Circle) circles.get(0), true, nextPointName(1));
						if(Utility.distToShape(event.getSceneX(), event.getSceneY(), p1) < Utility.distToShape(event.getSceneX(), event.getSceneY(), p2))
						{
							addShape(p1);
							//System.out.println(p1);
						}
						else 
						{
							addShape(p2);
							//System.out.println(p2);
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
							addShape(new Point((Point) selectedShapes.get(0), (Point) selectedShapes.get(1), nextPointName(1)));
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
								addShape(new Line((Point) selectedShapes.get(0), (Point) selectedShapes.get(1), true));
							}
							else 
							{
								addShape(new Line((Point) selectedShapes.get(0), (Point) selectedShapes.get(1), false));
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
						Point p1 = (Point) selectedShapes.get(0);
						Point p2 = (Point) selectedShapes.get(1);
						Point p3 = (Point) selectedShapes.get(2);
						if(p1!=p2 && p2!=p3 && p3!=p1)
						{
							addShape(new Line(p1, p2, p3));
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
							addShape(new Line((Point) selectedShapes.get(0), (Point) selectedShapes.get(1)));
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
							addShape(new Circle((Point) selectedShapes.get(0), (Point) selectedShapes.get(1)));
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
		}
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
	 * Shows the configuration window for a point.
	 * @param p the point
	 */
	private void showConfigurePoint(Point p)
	{
		Stage rename = new Stage();
		FlowPane flowPane = new FlowPane();
		Scene renameScene = new Scene(flowPane, 580, 50);
		flowPane.getChildren().add(new Label("Rename Point " + p.getLabel().getText() + ":"));
		TextField name = new TextField();
		name.setPromptText("Enter new name of point");
		Button submit = new Button("Submit");
		Button cancel = new Button("Cancel");
		Button delete = new Button("Delete Point");
		Button hide = new Button("Hide Point");
		flowPane.getChildren().addAll(name, submit, cancel, delete, hide);
		cancel.requestFocus();
		submit.setOnAction(new EventHandler<ActionEvent>()
		{
			public void handle(ActionEvent event)
			{
				String pname = name.getText();
				if(!isValidPointName(pname)) return;
				p.getLabel().setText(pname);
				p.refreshName();
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
				p.setHidden(true);
				update();
				snappedIndex = -1;
				rename.close();
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
			Scene renameScene = new Scene(flowPane, 250, 30);
			Button cancel = new Button("Cancel");
			Button delete = new Button("Delete Shape");
			Button hide = new Button("Hide Shape");
			flowPane.getChildren().addAll(cancel, delete, hide);
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
					s.setHidden(true);
					update();
					snappedIndex = -1;
					configure.close();
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
		boolean isAllLettersOrNumbers = true;
		for(char c : pname.toCharArray())
		{
			if((c < '0' || c > '9') && (c < 'A' || c > 'Z')) isAllLettersOrNumbers = false;
		}
		
		if(isAllLettersOrNumbers) return true;
		else if(pname.length() == 2) //check if is a point prime
		{
			if(pname.charAt(0) < 'A' || pname.charAt(0) > 'Z') return false;
			if(pname.charAt(1) != '\'') return false;
			else return true;
		}
		else if(pname.length() == 3) //subscript names
		{
			if(pname.charAt(0) < 'A' || pname.charAt(0) > 'Z') return false;
			if(pname.charAt(1) != '_') return false;
			if((pname.charAt(2) < '0' || pname.charAt(2) > '9') && (pname.charAt(2) < 'a' || pname.charAt(2) > 'z')) return false;
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
		}
		String asy = "//Generated By AsyPadv0.1\n";
		asy+="import olympiad;\nimport markers;\nimport math;\nimport graph;\n";
		asy+="//change the unit size to fit your needs\n";
		asy+="unitsize(1cm);\n";
		int MAXLVL = 0;
		for(Shape s : shapes)
		{
			if(s.getLevel() > MAXLVL && s.isInAsyCode()) MAXLVL = s.getLevel();
		}
		for(int i = 0; i <= MAXLVL; i++)
		{
			asy += "//dependency level " + i + "\n";
			if(i == 0)
			{
				asy += "/* You can change the coordinates of these points of dependency level 0.\n";
				asy += "The drawing will retain the same relationships and qualities.\n";
				asy += "Please be aware that as a result of this some of the image may be clipped off. */\n";
			}
			if(i == 1)
			{
				asy += "//Do not change anything below, unless you are experienced in Asymptote.\n";
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
		asy+="//clip the drawing view\n";
		asy+="clip((" + xmin + ", " + ymin + ")--(" + xmin + ", " + ymax + ")--(" + xmax + ", " + ymax + ")--(" + xmax + ", " + ymin + ")--cycle);";
		return asy;
	}
}
