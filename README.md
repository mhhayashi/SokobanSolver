# SokobanSolver

Simple BFS based Sokoban solver that can solve easy puzzles. 

Created using Intellij IDEA CE and Java 17.

To run:

Use Jar from out/artifacts

java -jar -Xmx2G javaSolver.jar {Puzzle File Path} {# Threads (optional)}

The solver runs puzzle files using the standard encoding where the file contains only the puzzle and no comments. 

Good puzzles include the Microban set created by David Skinner: http://www.abelmartin.com/rj/sokobanJS/Skinner/David%20W.%20Skinner%20-%20Sokoban.htm

To solve above puzzles, copy the board only into a new file and run the solver on that file. 
