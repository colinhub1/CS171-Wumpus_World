# CS171-Wumpus_World
Wrote an AI that would traverse through a randomly generated 2d array dungeon (size varied), avoid dangerous tiles, find the tile with gold, and return to start

Worked on code with partner
*currently can't remember the numbers regarding how the dungeon operates (min/max size, points, duration)

User would start at [0,0] on stairs, in a 2d array dungeon with pitfall tiles, one Wumpus tile, and one gold tile.
The pitfall and wumpus tiles would kill the ai and terminate the program if stepped on and cost points*,
they gave an indication if adjacent to one.
The gold tile would give points*, the same number equivalent to dying I believe.
Traversing through the dungeon would cost one point per action taken. The possible actions included turning direction,
taking a step, picking up gold, and going up the stairs). Overall finding gold was always worth traversing through dungeon.
Attempting to kill the wumpus (one time opportunity per dungeon) costed points (15 I believe), but allowed the wumpus tile to be
traversed if successfully killed (a notification would go off if this was done).
If the ai stayed in the dungeon for too long, they would die after a large* amount of actions taken (also costed points).

The layout was randomly generated every time.
This included the size* of the dungeon as well as the number of pitfalls and the positons of pitfalls, wumpus, and gold.
The ai does not know the size or layout of the dungeon.
The ai had to return to the start with(out) gold received.

The goal was to run the ai on 1000 randomly generated dungeons and have a relatively high average score*,
equivalent to receiving the gold 95-100% of the time and never dying.

Our ai would travel north until no longer possible (detected threat or hit wall) followed by going east, west, and south.
It would save it's traversed steps in a stack. It would avoid going back the way it came (for example, if it was coming from the 
east, it would check if it could go north, west, and south). If it ever had to backtrack, it would remove the step(s) taken from 
the stack.
Since dangerous tiles gave indications all around, the ai was designed to interpret where dangers were if possible so it could
travel effectively and efficiently. Tiles were marked as safe to travel if they gave no indication of danger, and they would mark
the adjacent tiles as safe too.

In the end, our ai received an average score of 95% the desired average score. It succeeded in avoiding all dangers, and found
found gold 90-95% of the time (when possible, since occasionally unsolvable dungeons would be randomly generated).
Our reason for non-max score was due to the efficiency of travel and return to start (could have been shorter when returning to
start. In addition, we did not attempt to kill the Wumpus, so some dungeons that could have been solved by removing wumpus
were not.

The rest of the code necessary to run the program is omitted.
