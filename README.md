# Mice VS Elephants
It is common knowledge that elephants are deathly afraid of mice. What is not so common knowledge is the reason why. Clearly it can't be that the elephant is afraid of stepping on the mouse as the mouse cannot, in any way, pose a risk to the substantially larger mammal in such an encounter. Some argue that the reason is that the elephant is afraid that the mouse will go inside its trunk. This is ridiculous because in one simple puff the elephant could eject the mouse for several hundred yards. In fact, this would seem the perfect way to deal with mice: when they approach, simply snort them up gently and then blow them out as far as you can. Multiple mice could be handled simultaneously. Heck, the elephants could set up competitions for distance or quantity.
The real reason is that mice have a strong taste for elephant flesh. Although there have been no visual recordings of mice attacking elephants as a tasty snack it does in fact happen. What's more surprising is that the mice have a very strong appetite for elephants. It only requires two mice to devour a single adult male elephant (clearly competitions involving quantity is not a good idea for the elephants). It's not a pretty sight either. Faced against the voracious feeding frenzy of a pair of mice the elephant has no hope of survival. A single mouse cannot take on an elephant and survive so they must coordinate their attack. The coordination is quite simple.

Using a multithreaded application written in Java you are to simulate mice hunting elephants in the wild.

# Specifications
You are to use three different thread functionss. One to represent elephant, one for mice and the original main thread.
The hunt occurs on a discrete, square grid.
The dimensions of the grid are passed as the first two command line arguments. 
```
C:/> java hunt 300 400 6 4 7
```
* results in a grid that is 300 squares wide and 400 squares tall. 
* The striking distance is set to 6 units. 
* There are 4 elephants and 7 mice at the start.

Animals can move (and be ''snot shot'' launched) in eight directions. The directions are the same as those that a king or queen can move in chess.
Adjacent squares in the diagonal direction are considered to be separated by a distance of 1 unit for purposes of movement.
* Mice are sneaky and fast. They make two moves for every move the elephant makes.
* An animal can only move to a square adjacent to the square it currently occupies.
* Elephants start in random squares.
* The mice start in random squares not already occupied by an elephant.
* Every time an animal moves itself it prints its 
```
movement to stdout in the format:
     Mouse 2 to 233 345
or
     Elephant 1 to 223 345
Adding a "Turn 23:" prefix 
or 
a " on Turn 23" suffix to the print statement is acceptable (even desirable, but not required).

```
- The hunt ends when elephants have been eaten. FYI... the mice die of starvation.

All threads must exit cleanly when the hunt ends.

# Distances:

- For movement and throwing all distances are discrete
squares, regardless of direction. Like kings and queens in chess.

- For measurements, such as ``farther'' and ``within'',
the distance is computed according to the standard formula:
```
LaTeX: sqrt((x1 - x2)^2+(y1 - y2)^2)
```

The game occurs on a global 2-dimensional array of squares. java.math.Random is a good random number generator. Be careful using them though as they may not be reentrant.

# Rules of Movement
Mice basically move randomly unless they aren't alone and within striking distance of the elephant in which case they attack.

- If the mouse is not within striking distance of the elephant then it moves to a random adjacent square.
- If the mouse is within the striking distance of the elephant then:
  - If the mouse is within the striking distance of the other mouse then the mouse is not alone and makes a move that brings it as close to the elephant as possible.
  - Otherwise, the mouse is alone and it freezes and stands still for fear of being launched with snot all over itself.
- The elephant is basically afraid of mice and seeks to maximize its closest distance to a mouse. But he's also irrational so his movement is always random.
  - If the elephant finds a single mouse on itself (occupies the same square as itself) then the elephant simply snorts it up and ejects it in a random direction a distance equal to twice the striking distance (not to exceed the edges of the board). After such an act the elephant makes his move normally.
  - If a mouse is within striking distance then it makes a random move that would increase the closest distance to any mouse. If no squares take it further away then the elephant stands still, trembling. In other words... any move that bring him closer to any mouse than he is currently would not be valid.
  - Otherwise, it makes a random move.
# Deliverables
Upload your source code files to canvas. If you are submitting as a team you should create a team group. Make sure all source code has a comment with all the author's name.
