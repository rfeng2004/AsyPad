package asypad.ui; //test

import java.util.ArrayList;
import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import asypad.shapes.*;
import asypad.shapes.types.*;

/**
 * A special pane that interacts with the user and draws shapes.
 * @author Raymond Feng
 * @version 0.2
 */
public class AsyPadPane extends Pane
{
	private ArrayList<Shape> shapes;
	private int snappedIndex;
	private static final int snapForce = 3;

	/**
	 * Creates an AsyPadPane layout.
	 */
	public AsyPadPane()
	{
		super();
		shapes = new ArrayList<Shape>();
		this.setOnMouseMoved(new EventHandler<MouseEvent>()
		{
			public void handle(MouseEvent event)
			{
				snappedIndex = -1;
				for(Shape s : shapes)
				{
					if(Utility.distToShape(event.getSceneX(), event.getSceneY(), s) < snapForce*Shape.StrokeWidth)
					{
						setCursor(Cursor.HAND);
						if(snappedIndex == -1 || !(shapes.get(snappedIndex) instanceof Point))
						{
							snappedIndex = shapes.indexOf(s);
						}
					}
				}
				if(snappedIndex == -1)
				{
					setCursor(Cursor.DEFAULT);
				}
				for(int i = 0; i < shapes.size(); i++)
				{
					if(i == snappedIndex)
					{
						//System.out.println(shapes.get(snappedIndex));
						shapes.get(i).getObject().setStroke(Color.RED);
						shapes.get(i).getObject().toFront();
					}
					else
					{
						shapes.get(i).getObject().setStroke(Color.BLACK);
					}
				}

			}
		});
		this.setOnMousePressed(new EventHandler<MouseEvent>()
		{
			public void handle(MouseEvent event)
			{
				if(snappedIndex == -1)
				{
					addShape(new Point(event.getSceneX(), event.getSceneY()));
					snappedIndex = shapes.size()-1;
					setCursor(Cursor.HAND);
				}
				else if(!(shapes.get(snappedIndex) instanceof Point))
				{
					addShape(new Point(event.getSceneX(), event.getSceneY(), shapes.get(snappedIndex), ""));
					snappedIndex = shapes.size()-1;
					setCursor(Cursor.HAND);
				}
			}
		});
		this.setOnMouseDragged(new EventHandler<MouseEvent>()
		{
			public void handle(MouseEvent event)
			{
				if(snappedIndex != -1)
				{
					Shape s = shapes.get(snappedIndex);
					if(s.getType() == POINT_TYPE.POINT)
					{
						((Point) s).setX(Math.max(0, event.getSceneX()));
						((Point) shapes.get(snappedIndex)).setY(Math.max(0, event.getSceneY()));
					}
					else if(s.getType() == POINT_TYPE.POINT_ON_OBJECT)
					{
						((Point) s).setRelativeLocation(event.getSceneX(), event.getSceneY());
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
		}
	}

	/**
	 * Updates the AsyPadPane by deleting all shapes with remove = true, this should be called each time delete() is called on a shape.
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
