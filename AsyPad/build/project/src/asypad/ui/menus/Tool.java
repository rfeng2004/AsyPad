package asypad.ui.menus;

import asypad.shapes.types.SHAPE_TYPE;
import asypad.ui.AsyPadPane;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.*;
import javafx.scene.image.*;

/**
 * Tools that are used in the AsyPad toolbar.
 * @author Raymond Feng
 */
public class Tool extends MenuItem
{
	/**
	 * Creates a new Tool.
	 * @param image graphic to be used for this tool
	 * @param name name of this tool
	 * @param description description of this tool
	 * @param type internal identification of this tool
	 * @param parent parent menu
	 * @param bar toolbar that this is on
	 * @param pane pane that contains the toolbar
	 */
	public Tool(Image image, String name, String description, SHAPE_TYPE type, ToolMenu parent, AsyPadToolBar bar, AsyPadPane pane)
	{
		super();
		setText(name);
		ImageView graphic = new ImageView(image);
		graphic.setFitHeight(30);
		graphic.setFitWidth(30);
		setGraphic(graphic);
		setOnAction(new EventHandler<ActionEvent>()
		{
			public void handle(ActionEvent event)
			{
				bar.setSelectedTool(type);
				ImageView d = new ImageView(image);
				d.setFitHeight(30);
				d.setFitWidth(30);
				parent.setImage(d);
				parent.setLastUsed((Tool)(event.getSource()));
				pane.updateTool(name);
				pane.updateToolDescription(description);
			}
		});
	}
}
