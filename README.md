**[Switch to english version](README.md)**
# 🎲 Aufgabe 4: Würfelglück

*DevOFVictory, 28.11.21 - Bundeswettbewerb Informatik 2021*

---

# Inhalt

1. Informationen
2. Lösungsideen + (Probleme)
3. Programmatische Umsetzung
4. Benutzung
5. Beispiele
6. Quellcode
7. Programmstuktur

# 1. Informationen

Bei diesem Projekt handelt es sich um meiner Ausarbeitung der Aufgabe 4 im Bundeswettbewerb der Informatik 2021. Es ging darum, aus einer endlichen Menge an verschiedensten Wüfeln, zu bestimmen, welcher von ihnen am besten geeignet ist für ein komplettes Spiel "Mensch ärgere dich nicht". Dafür musste ich das komplette Spielprinzip mit allen Regeln, wie dem Rauswerfen programmatisch umsetzten und simulieren.

# 2. Lösungsideen (+Probleme)

### Jeder Spieler hat seinen eigenen Spielplan

<aside>
💡 Bei meiner ersten Idee, spielt jeder Spieler auf seinem eigenen Spielplan, da jeder Spieler sozusagen seine eigene Strecke läuft und damit auch sein eigenes Ziel und einen separierten Start hat. Das Problem, das sich bei dieser Idee herausstellte, ist die Interaktion der Spieler untereinander, also das Herauswerfen und überspringen der fremden Spielfiguren. Ein Ansatz zur Lösung dieses Problemes wäre es gewesen, die einzelnen Spielfelder miteinander zu synchronisieren. Dies wäre aber eine häufige Fehlerursache geworden, sodass ich mich gegen diese Idee entschieden habe.

</aside>

### Es gibt einen Spielplan für alle Spieler, bei dem jedes Feld eine eigene ID besitzt

<aside>
💡 Da sich bei der ersten Idee, das Problem mit der schwierigen Synkrosation zwischen auftrat, entschied ich mich hier, ein Spielbrett für alle alle Figuren des Spieles zu definieren. Dabei wird jedes einzelnes Feld mit einer einzigartigen ID Nummer bestückt, sodass die Felder direkt ansprechbar sind.  Hier stellte sich aber dann das Problem, dass jeder Spieler seine eigene Strecke hat und nicht allgemein gesagt werden kann, dass die Strecke mit der ID 0 beginnt und mit der 39 aufhört, da jeder Spieler in sein eigenes Haus einbiegen muss.

</aside>

### Überarbeitung: Ein Spielplan, IDs für alle Felder, Spezifikation der einzelnen Felder

<aside>
💡 Die Lösung für das Problem der zweiten Idee und damit auch der endgültige Lösungsansatz ist es nun, weiterhin ein Spielfeld für alle Spieler zu deklarieren, aber alle Felder inklusive der Zielfelder mit einer eindeutigen ID als Eigenschaft des Objektes zu versehen. Außerdem besitzt die `Field`-Klasse noch die Eigenschaft, welchem Spieler das jeweilige Feld gehört. Somit kann die `GameFigure` Klasse eine Prozedur `getTargetField()` implementieren, die für einen Paramater `diced` das `Field` zurückgibt, auf dem die Figur nach dem Zug landet, während sie die fremden Zielfelder überspringt.

</aside>

# 3. Programmatische Umsetzung

Nachdem ich der doch etwas umfangreicheren Aufgabe mich von Python 3 abgewendet habe, entschied ich mich für Java 16 als Programmiersprache, um dieses Projekt anzugehen. Die Gründe dafür sind meine mehrjährige Erfahrung mit Java aber hauptsächlich die Möglichkeit auf strikte Objektorientierung. Diese ist für die Aufgabe meiner Meinung nach essentiell, um den Überblick über die einzelnen Spielelemente, wie das Spielbrett, auf dem sich Spielfelder befinden und natütlich Objekte wie der Spieler, der würfen und ziehen kann, sowie das Spiel ansich, um allgemeine Methoden zentral definieren zu können.

Ich implementierte also in der Java Entwicklungsumgebung *(IDE)* "IntelliJ Ultimate" die Klassen

`Game.java`, `GameField.java`, `GameFigure.java`, `GamePlan.java` und `GamePlayer.java`.

Näherer Sinn, Funktionen der einzelnen Klassen und ein Class-Diagramm finden Sie unter dem Punkt "**6. Programmstuktur**"

# 4. Benutzung

Das Programm lässt sich durch einen Befehl aktivieren, in welchem auch Einstelungsparamter wie Anzahl der Iterationen der Spielkombinationen, und der Pfad festgelegt werden kann.

```bash
java -jar Exercise4.java <boolean showDebug> <int iterations> <string pathToFile>
```

- showDebug - Gibt an, ab alle Nachrichten wie der komplette Spielverlauf in der Console angezeigt werden sollen. Dies ist sinnvoll, um das Spiel im Nachhinein nachvollziehen zu können. Die möglichen Werte sind true und false
- iterations - Gibt an, wie oft jede Spielkombination gespielt werden soll. Je höher diese Zahl ist, desto genauer ist das Ergebnis, aber es dauert auch länger. Das Wertespektrum für diesen Parameter sind alle natürlichen, ganzen Zahlen größer als 0.
- pathToFile - Gibt an, unter welchem Pfad die Datei mit den zu testenden Würfeln gespeichert ist. Eine relative Pfadangabe ist möglich. Dieser Wert ist ein beliebiger String.

### Beispiel einer Würfeldatei:

```
6
6 1 2 3 4 5 6
6 1 1 1 6 6 6
4 1 2 3 4
10 0 1 2 3 4 5 6 7 8 9
12 1 2 3 4 5 6 7 8 9 10 11 12
20 1 2 3 4 5 6 7 8 9 10 11 12 13 14 15 16 17 18 19 20
```

- in der ersten Zeile die Anzahl n der Würfel,
- in den weiteren n Zeilen jeweils:
    - als erste Zahl die Anzahl m_i der Seiten des Würfels i und
    - in den weiteren m_i Zahlen jeweils die Augenzahlen der
    Würfelseiten

# 5. Beispiel

Genutzer Befehl: `java -jar Exercise4.jar false 50 dice0.txt`

### dices0.txt - Dauerte ungefähr 16,5 Sekunden

```
Here are the wins of each dice ordered by quantity:
1.) Dice 1 (41 Wins)
2.) Dice 4 (35 Wins)
3.) Dice 5 (31 Wins)
4.) Dice 6 (25 Wins)
5.) Dice 2 (9 Wins)
6.) Dice 3 (0 Wins)
```

# 6. Quellcode

## Umsetzung von Regelwerk im Quellcode

Ich werde unter diesem Abschnitt für jede Regel aus dem offiziellen Regelwerk den dafür zuständigen Code Schnipsel hier einfügen.

> *Wer seine vier Spielsteine als erster „nach Hause“ gebracht hat,
gewinnt das Spiel*
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

> *Der Spieler, der an der Reihe ist, würfelt und setzt seinen Spielstein
um die gewürfelte Augenzahl in Pfeilrichtung auf der Laufbahn vor.
Eigene und fremde Steine können übersprungen werden, die besetzten Felder werden aber mitgezählt.*
> 

> *Wer mit dem letzten Punkt seiner Augenzahl auf ein Feld trifft, das
von einer fremden Spielfigur besetzt ist, schlägt diese Figur und setzt
seinen eigenen Stein auf ihren Platz.*
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

> *Wer mehrere Spielsteine auf der Laufbahn stehen hat, muss mit dem
vordersten Stein ziehen, der gezogen werden kann.*
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

> *So lange noch weitere Steine auf den B-Feldern auf ihren Spieleinsatz warten, darf keine eigene Figur auf dem A-Feld stehen bleiben.
Sie muss das Feld frei machen, sobald sie die Möglichkeit dazu
hat.*
> 

```java
if (!getStart().isOccupied()) {
	[...]
}else {
	return getStart().getContent();
}
```

> *Die Steine, die auf den B-Feldern stehen, können nur mit einer „6“
ins Spiel gebracht und damit auf das Anfangsfeld A gesetzt werden.*
> 

> *Wer eine „6“ würfelt, hat nach seinem Zug einen weiteren Wurf frei.
Erzielt er dabei wieder eine „6“, darf er erneut nach dem Ziehen
würfeln.*
> 

> *Bei einer „6“ muss man einen neuen Stein ins Spiel bringen, so
lange noch Spielfiguren auf den eigenen B-Feldern stehen. Der neue
Stein wird dann auf das Feld A der eigenen Farbe gestellt.*
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

> *Steht dagegen eine
fremde Figur auf dem Feld A, wird sie geschlagen.*
> 

> *Wer eine „6“ würfelt und keinen Stein mehr auf den B-Feldern hat,
darf mit einer seiner Figuren auf der Laufbahn sechs Felder weiterziehen und dann noch einmal würfeln.*
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

## Simulationssteuerung

Hier sind wichtige Code Teile, die daür sorgen, dass die Dateien korrekt ausgelesen werden und die einzelnen Simulationen gestartet werden.

### Auswahl der Spielkombinationen

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

### Erstellung einer Spielsimulation

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

# 7. Programmstruktur

## Class Diagramm

![https://i.ibb.co/vPCZgsY/Package-exercise4.png](https://i.ibb.co/vPCZgsY/Package-exercise4.png)

Jede Klasse implementiert das `Game` Objekt, um auf alle Daten des Spiels von überall zugreifen zu können.

## Erklärung zu den einzelnen Klassen

### `Exercise4.java`

<aside>
💡 Hier startet die ganze Simulation in der `main()` Methode. Außerdem enthält sie nich eine Funktion, um Nachrichten in einem bestimmten Format auszugeben.

</aside>

### `Game.java`

<aside>
💡 Über diese Klasse bzw. dieses Objekt werden allgemeine Funktionen des Spielablaufs definiert, wie zum Beispiel das Bewegen von Figuren, der Gewinnerstatus oder der Spieler, der momentan an der Reihe ist.

</aside>

### `GamePlayer.java`

<aside>
💡 Hier sind Spielerspezifische Funktionen und Eigenschaften gespeichert, wie z.B. der einzelne Zug und die jeweiligen Figuren, die zu dem Spieler gehören.

</aside>

### `GameFigure.java`

<aside>
💡 Die eben genannten Figuren sind alles Objekte dieses Types. Sie implementiert z.B. die Eigenschaften des Besitzers, sowie das Feld, auf dem die Figur steht.

</aside>

### `GameField.java`

<aside>
💡 Das ist das einzelne Feld, auf dem eine Figur stehen kann, oder auch nicht stehen kann. Über diese Info gibt die Eigenschaft `content` Info. Eine Liste mit vielen dieser Objekte befindet sich auf dem Spielfeld.

</aside>

### `GamePlan.java`

<aside>
💡 Diese Klasse stellt das eigentliche Spielfeld dar. Es enthält eine Liste aller Plätze für Felder und die einzelnen Häuser mit den IDs der Spieler.

</aside>
