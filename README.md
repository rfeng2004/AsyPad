# AsyPad
A simple drawing tool that can convert diagrams into Asymptote code.

# Shapes (as of v1.1)
Points: Free point, Point locked onto another shape, Intersection Point, Midpoint

Lines: Segment, Line, Parallel Line, Perpendicular Line, Angle Bisector, Perpendicular Bisector

Circles: Center and Point on circle, Circumcircle

# Valid Point Names
Rename a point by double clicking on it.
The following are valid point names:
- The name is made only of capital letters and numbers, and starts with a capital letter.
- The name is a capital letter, followed by an underscore, followed by a number or lowercase letter.

# Keyboard Shortcuts
Press Esc when you want to cancel drawing a shape.

# Undo/Redo Functionality
Use ⌘Z for undoing the last command, if there is one.
Use ⌘Y for redoing the command that was undone, if there is one.
Functions that can be undoed/redoed: Drawing a shape, Deleting a shape, Dragging a Label, Moving a shape, Hiding a shape, Renaming a point, Setting the Stroke Width.

# Internal Structure
Each shape has a set of dependencies and children. A shape's dependencies are the shapes that it needs to define itself. For example, a midpoint will need 2 points as its dependencies. A shape's children are the shapes that depend on it. This is important because when deleting a shape you need to also delete its children, since they are not defined anymore, and then delete the children's children, etc. 

# File I/O
Diagrams can be saved into a .apad file, which can be reopened later by AsyPad. They can also be saved into .asy files containing the Asymptote code for the diagram. The Asymptote code generated by AsyPad is unique because it is organized, and furthermore you can edit the coordinates of the points with dependency level 0 and the diagram will retain all the same properties. You can find sample files in the AsyPad Library folder. The APMO13 .asy files are for comparison of the code generated by Geogebra and by AsyPad.
