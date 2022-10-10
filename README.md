**[Switch to german version](README-de.md)**
# 🎲 Exercise 4: Lucky dice

*DevOFVictory, 11/28/21 - Federal Computer Science Competition 2021*

---

# Table of content

1. Information
2. Ideas for solutions (+ Problems)
3. Programmatic implementation
4. Usage
5. Examples
6. Sourcecode
7. Application structure

# 1. Information

This project is my elaboration of Task 4 in the Federal Computer Science Competition 2021. The idea was to determine from a finite set of different dice which one of them is best suited for a complete game of “Don't get angry”. For this, I had to implement and simulate the entire game principle with all the rules, such as throwing out.
# 2. Ideas for solutions (+ Problems)

### Each player has their own game plan


💡 In my first idea, each player plays on their own schedule, as each player runs their own track, so to speak, and thus has their own goal and a separate start. The problem that came up with this idea is the interaction of the players with each other, i.e. the throwing out and skipping of the strangers. One approach to solving this problem would have been to synchronize the individual playing fields with each other. But this would have become a common cause of error, so I decided against this idea.



### There is a game plan for all players, where each field has its own ID


💡 Since the first idea, the problem with the difficult synchronization between occurred, I decided here to define a board for all the characters of the game. Each individual field is filled with a unique ID number, so that the fields are directly accessible.  But then the problem arose that each player has his own track and it is not generally possible to say that the track starts with the ID 0 and ends with the 39 because each player has to turn into his own house.



### Revision: A game plan, IDs for all fields, specification of each field


💡 The solution to the problem of the second idea and thus also the final solution is to continue declaring a playing field for all players, but to provide all fields including the target fields with a unique ID as a property of the object. In addition, the `Field` class still has the property, which player owns the respective field. Thus, the `GameFigure` class can implement a procedure `getTargetField()` which returns the `field` for a parameter `diced` where the figure lands after the move while skipping the foreign target fields.



# 3. Programmatic implementation

After turning away from the somewhat larger task of Python 3, I decided to use Java 16 as the programming language to tackle this project. The reasons for this are my many years of experience with Java, but mainly the possibility of strict object orientation. In my opinion, this is essential for the task, in order to have an overview of the individual game elements, such as the game board where the playing fields are located and of course objects such as the player who can throw and draw, as well as the game itself, in order to be able to define general methods centrally.

So I implemented the classes in the Java development environment * (IDE) * “IntelliJ Ultimate”

`Game.java`, `GameField.java`, `GameFigure.java`, `GamePlan.java` und `GamePlayer.java`.

For more detailed meaning, functions of each class and a class diagram, see the point “**6. Programme structure**”

# 4. Usage

The program can be activated by a command in which settings parameters such as the number of iterations of the game combinations and the path can be set.

```bash
java -jar Exercise4.java <boolean showDebug> <int iterations> <string pathToFile>
```

- showDebug - Specifies that all messages such as the complete game history should be displayed in the console. This is useful in order to be able to follow the game afterwards. The possible values are true and false
- iterations - Specifies how often each game combination should be played. The higher the number, the more accurate the result, but it also takes longer. The range of values for this parameter are all natural integers greater than 0.
- pathToFile - Specifies the path under which the file with the cubes to be tested is stored. A relative path is possible. This value is an arbitrary string.

### Example of a dice file:

```
6
6 1 2 3 4 5 6
6 1 1 1 6 6 6
4 1 2 3 4
10 0 1 2 3 4 5 6 7 8 9
12 1 2 3 4 5 6 7 8 9 10 11 12
20 1 2 3 4 5 6 7 8 9 10 11 12 13 14 15 16 17 18 19 20
```

- in the first row, the number n of the dice,
- in the other n rows:
    - first number the number m_i of the sides of the cube i and
    - in the other m_i numbers, the eye numbers of the dice sides

# 5. Examples

Used Command: `java -jar Exercise4.jar false 50 dice0.txt`

### dices0.txt - Took about 16,5 seconds

```
Here are the wins of each dice ordered by quantity:
1.) Dice 1 (41 Wins)
2.) Dice 4 (35 Wins)
3.) Dice 5 (31 Wins)
4.) Dice 6 (25 Wins)
5.) Dice 2 (9 Wins)
6.) Dice 3 (0 Wins)
```

# 6. Sourcecode

## Implementation of rules in source code

I`m going to insert the code snippet for each rule from the official german rulebook here under this section.

> *The first player to bring his four game pieces "home",
wins the game*
> 

```java
public boolean hasWon() {
        boolean won = true;

        for (GameField house : game.getGamePlan().getHouses(this)) {
            if (house.getContent() == null) {
                won = false;
                break;
            }
        }

        if (won) {
            Exercise4.logMessage(this.getName() + " won this game.");
        }

        return won;
    }
```

> *The player whose turn it is rolls the dice and advances his token by the number
by the number of dice rolled in the direction of the arrow.
Own and foreign stones can be skipped, but the occupied fields are counted.*
> 

> *Whoever hits a square with the last point of his number of dice that is
occupied by another player's piece, captures that piece and places his own
his own piece in its place.*
> 

```java
public void turn() {
        int diced = dice();

        if (diced != 6) {
            move(diced);

            if (!hasWon()) {
                this.game.nextPlayer();
            }else {
                this.game.setWinner(this);
            }

        }else {
            if (getStart().isOccupied()) {
                GameFigure occupyingFigure = getStart().getContent();
                
                if (occupyingFigure.getOwner().equals(this)) {
                    Exercise4.logMessage("Start is occupied by own figure. Moving this. ");
                    this.game.moveFigure(occupyingFigure, occupyingFigure.getTargetField(diced));
                }else {
                    Exercise4.logMessage("Start is occupied by other figure. Kicking this.");
                    getStart().getContent().getOwner().getHouse().add(occupyingFigure);

                    if (!this.getHouse().isEmpty()) {
                        GameFigure houseFigure = getHouse().get(0);

                        this.getStart().setContent(houseFigure);
                        getHouse().remove(houseFigure);
                    }else {
                        this.move(diced);
                    }
                }

            }else {
                if (!this.getHouse().isEmpty()) {
                    Exercise4.logMessage(this.getName() + " left his house.");
                    GameFigure houseFigure = getHouse().get(0);

                    this.getStart().setContent(houseFigure);
                    getHouse().remove(houseFigure);
                }else {
                    Exercise4.logMessage(this.getName() + " moved, because house is empty.");
                    move(diced);
                }
            }

            if (!hasWon()) {
                turn();
            }else {
                this.game.setWinner(this);
            }

        }

    }
```

> *If you have more than one piece on the track, you must move the frontmost
move with the frontmost piece that can be moved.*
> 

```java
public GameFigure getFirstGameFigure(List<GameFigure> includedFigures) {

        if (includedFigures.isEmpty()) {
            return null;
        }

        GameFigure firstFigure = includedFigures.get(0);

        if (!getStart().isOccupied()) {

            for (GameFigure figures : includedFigures) {
                if (getRaceIds().indexOf(figures.getField().getId()) > getRaceIds().indexOf(firstFigure.getField().getId())) {
                    firstFigure = figures;
                }
            }

            int currentId = firstFigure.getField().getId();
            int idIndex = getRaceIds().indexOf(currentId);

            GameField nextField = getRaceIds().size() > idIndex+1 ? this.game.getFieldById(getRaceIds().get(idIndex+1)) : null;

            if (nextField == null) {
                includedFigures.remove(firstFigure);
                return getFirstGameFigure(includedFigures);
            }else {
                if (nextField.isOccupied() && nextField.getContent().getOwner() == this) {
                    includedFigures.remove(firstFigure);
                    return getFirstGameFigure(includedFigures);
                }else {
                    return firstFigure;
                }
            }

        }else {
            return getStart().getContent();
        }
    }
```

> *As long as other pieces are waiting on the B-squares, no piece may remain on the A-square.
It must clear the square as soon as it has the opportunity to do so.
has the opportunity.*
> 

```java
if (!getStart().isOccupied()) {
	[...]
}else {
	return getStart().getContent();
}
```

> *The checkers placed on the B-squares can only be put into play with a "6".
into the game and thus be placed on the starting square A.*
> 

> *Whoever rolls a "6" has another free roll after his turn.
If he rolls a "6" again, he may roll again after the draw.
roll the dice.*
> 

> *If you get a "6", you must bring a new piece into play as long as there are
as long as there are still pieces on your own B-squares. The new
The new piece is then placed on the A square of your own color.*
> 

```java
int diced = dice();
if (diced != 6) {
	[...]
}else {
	[...]
	if (!this.getHouse().isEmpty()) {
	  Exercise4.logMessage(this.getName() + " left his house.");
		GameFigure houseFigure = getHouse().get(0);
		this.getStart().setContent(houseFigure);
		getHouse().remove(houseFigure);

	}else {
		Exercise4.logMessage(this.getName() + " moved, because house is empty.");
		move(diced);

  }
	turn();
}
```

> *If, on the other hand, a
If a foreign piece is on square A, it is captured.*
> 

> *If you roll a "6" and have no more pieces on the B squares,
may move one of his pieces six spaces further along the track and then roll the dice again.*
> 

```java
if (getStart().isOccupied() && !occupyingFigure.getOwner().equals(this)) {
	Exercise4.logMessage("Start is occupied by other figure. Kicking this.");
	getStart().getContent().getOwner().getHouse().add(occupyingFigure);
	if (!this.getHouse().isEmpty()) {
	    GameFigure houseFigure = getHouse().get(0);
	    this.getStart().setContent(houseFigure);
	    getHouse().remove(houseFigure);
	}else {
	    this.move(diced);
}
```

> *Fremde Zielfelder darf man nicht betreten.*
> 

```java
if ((field != null && field.getLimited() == null) || (field != null && field.getLimited().equals(this.getOwner()))) {
	[...]
}
```

## Simulation control

Here are important code parts that ensure that the files are read correctly and that the individual simulations are started.

### Generating the dice combinations

```java
for (int i=0; i<dices.size(); i++) {
	scores.put(i, 0);
	for (int j = i + 1; j < dices.size(); j++) {
	
	    List<String> combination = new ArrayList<>();
	    combination.add(dices.get(i));
	    combination.add(dices.get(j));
	
	    combinations.add(combination);
	}
}
```

### Creation of one single game simulation

```java
Game game = new Game(48, 0);

GamePlayer player1 = new GamePlayer(game, "Player 1 (Green)", 0, 47, dice1.subList(1, dice1.size()));
GamePlayer player2 = new GamePlayer(game, "Player 2 (Red)", 24, 23, dice2.subList(1, dice2.size()));

houses.put(44, player1);
houses.put(45, player1);
houses.put(46, player1);
houses.put(47, player1);

houses.put(20, player2);
houses.put(21, player2);
houses.put(22, player2);
houses.put(23, player2);

game.getGamePlan().setHouses(houses);
game.joinPlayer(player1);
game.joinPlayer(player2);
```

### Maximale Ausführungsdauer von 1,5 Sekunden

```java
long startedTime = System.currentTimeMillis();
while (game.isRunning()) {

  if (System.currentTimeMillis() - startedTime >= 1500) {
      logMessage("Simulation took too long. Skipped this game.");
      break;
  }
[...]
}
```

# 7. Applicationstructure

## Class Diagramm

![https://i.ibb.co/vPCZgsY/Package-exercise4.png](https://i.ibb.co/vPCZgsY/Package-exercise4.png)

Each class implements the `Game` object to access all game data from anywhere.

## Explanation of the individual classes

### `Exercise4.java`


💡 Here the whole simulation starts in the `main () ` method. It also does not include a function to output messages in a specific format.



### `Game.java`


💡 This class or object defines general functions of the gameplay, such as moving characters, winning status, or the player who is currently in the game.



### `GamePlayer.java`


💡 It stores players specific features and features, such as the individual move and the respective pieces that belong to the player.



### `GameFigure.java`


💡 The figures just mentioned are all objects of this type. For example, it implements the properties of the owner, as well as the field on which the figure stands.



### `GameField.java`


💡 This is the single field on which a figure may or may not stand. About this information, the property `content` gives information. A list of many of these objects can be found on the playing field.



### `GamePlan.java`


💡 This class represents the actual playing field. It contains a list of all places for squares and individual houses with the IDs of the players.


