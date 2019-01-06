# Adding a new Tool

There are many steps involved in this process. Be sure to complete each part to ensure that the new tool is not buggy.
- Make your new tool, i.e. a drawing tool, and assign a SHAPE_TYPE to this tool.
- Complete the toString and toAsymptote methods for this type of tool, if applicable. toString is for .apad file i/o and toAsymptote is the conversion of the shape into Asymptote code.
- Complete a section of Shape.buildShape corresponding to the tool, if applicable.
- If needed, add a new subclass of Command to represent this new tool's action.
- Finally, add the necessary components into AsyPadPane.java to enable the tool (don't forget to invoke AsyPadPane.addCommand).

Once these steps are complete, you should add your tool into the tool bar.
- Add the tool into AsyPadToolBar.java
- Add a placeholder icon, I will make one for it.
