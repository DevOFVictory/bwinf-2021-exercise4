package de.devofvictory.bwinf.exercise4;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GamePlayer {

    private Game game;
    private String name;
    private int startFieldId;
    private int endFieldId;

    private List<GameFigure> house;

    private final List<Integer> dice;

    private final Random diceRandom;

    private List<Integer> raceIds;

    public GamePlayer(Game game, String name, int startFieldId, int endFieldId, List<Integer> dice) {
        this.name = name;
        this.game = game;
        this.startFieldId = startFieldId;
        this.endFieldId = endFieldId;
        this.dice = dice;
        this.diceRandom = new Random();

        this.house = new ArrayList<>();

        for (int i = 0; i<4; i++) {
            GameFigure figure = new GameFigure(game, this);
            house.add(figure);
        }

        List<Integer> raceIds = new ArrayList<>();

        int counter = 0;
        int currentId = startFieldId;

        while (counter < this.game.getGamePlan().getGameFields().size()) {
            raceIds.add(currentId);

            currentId ++;
            counter ++;

            if (currentId == this.game.getGamePlan().getGameFields().size()) {
                currentId = 0;
            }
        }
        this.raceIds = raceIds;
    }

    public List<Integer> getDice() {
        return dice;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public GameField getStart() {
        return game.getFieldById(startFieldId);
    }


    public List<GameFigure> getHouse() {
        return house;
    }


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

    public List<Integer> getRaceIds() {
        return raceIds;
    }

    public int getEndFieldId() {
        return endFieldId;
    }

    public List<GameFigure> getFiguresOnPlan(){

        List<GameFigure> onPlan = new ArrayList<>();

        for (GameField field : this.game.getGamePlan().getGameFields()) {
            if (field.getContent() != null && field.getContent().getOwner().equals(this)) {
                onPlan.add(field.getContent());
            }
        }
        return onPlan;
    }


    public int dice() {
        int diced = dice.get(diceRandom.nextInt(dice.size()));
        Exercise4.logMessage(this.getName() + " diced a " + diced);
        return diced;
    }

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

    public void move(int diced) {
        GameFigure figure = getFirstGameFigure(getFiguresOnPlan());

        if (figure == null) {
            Exercise4.logMessage("No figures on plan. Expired.");
            return;
        }
        GameField target = figure.getTargetField(diced);

        if (target == null || figure == null) {
            Exercise4.logMessage("Turn not possible. Expired.");
            return;
        }

        this.game.moveFigure(figure, target);
        Exercise4.logMessage("First figure of player '" + this.getName() + "' moved to field " + target.getId());
    }

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

    @Override
    public String toString() {
        return "GamePlayer{" +
                "name='" + name + '\'' +
                '}';
    }
}
