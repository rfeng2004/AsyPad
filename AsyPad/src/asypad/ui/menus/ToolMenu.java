package asypad.ui.menus;

//import javafx.animation.AnimationTimer;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;

public class ToolMenu extends Menu
{
	/**
	 * Button which, when pressed, sets the saved tool.
	 */
	private Button toolButton;

	/**
	 * Last used Tool.
	 */
	private Tool lastUsed;

	/**
	 * Creates a ToolMenu using the specified graphic
	 * @param graphic the graphic
	 */
	public ToolMenu(Node graphic)
	{
		lastUsed = null;
		toolButton = new Button();
		toolButton.setPrefSize(30, 30);
		toolButton.setPadding(new Insets(0, 0, 0, 0));
		toolButton.setGraphic(graphic);
		toolButton.setOnAction(new EventHandler<ActionEvent>()
		{
			public void handle(ActionEvent event)
			{
				if(lastUsed != null)
				{
					lastUsed.fire();
				}
				/*new AnimationTimer()
				{
					private boolean isFirst = true;
					public void handle(long now)
					{
						if(isFirst)
						{
							isFirst = false;
							return;
						}
						hide();
						stop();
					}

				}.start();*/
			}
		});
		this.setGraphic(toolButton);
	}

	public void setImage(Node n)
	{
		toolButton.setGraphic(n);
	}

	public void setLastUsed(Tool t)
	{
		lastUsed = t;
	}
}
