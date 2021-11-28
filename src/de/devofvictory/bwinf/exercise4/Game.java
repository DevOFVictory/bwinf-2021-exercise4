package de.devofvictory.bwinf.exercise4;

import java.util.ArrayList;
import java.util.List;

public class Game {

    private final List<GamePlayer> players;
    private GamePlan gamePlan;
    private int onTurn;

    private GamePlayer winner;

    public Game(int fieldAmount, int onTurn) {
        players = new ArrayList<>();
        gamePlan = new GamePlan(fieldAmount);
        this.onTurn = onTurn;
    }

    public int getOnTurn() {
        return onTurn;
    }

    public void nextPlayer() {
        if (this.onTurn == this.players.size() -1 ) {
            this.onTurn = 0;
        }else {
            this.onTurn ++;
        }
        Exercise4.logMessage(this.getPlayers().get(onTurn).getName() + " is now on turn.");
    }

    public void setWinner(GamePlayer winner) {
        this.winner = winner;
    }

    public GamePlayer getWinner() {
        return this.winner;
    }

    public boolean isRunning() {
        return winner == null;
    }

    public void joinPlayer(GamePlayer p) {
        players.add(p);
    }

    public List<GamePlayer> getPlayers() {
        return players;
    }


    public GamePlan getGamePlan() {
        return gamePlan;
    }

    public void setGamePlan(GamePlan gamePlan) {
        this.gamePlan = gamePlan;
    }

    public GameField getFieldById(int id) {
        for (GameField fields : gamePlan.getGameFields()) {
            if (fields.getId() == id) {
                return fields;
            }
        }
        return null;
    }

    public void moveFigure(GameFigure figure, GameField field) {
        if (!field.isOccupied() || (field.isOccupied() && field.getContent().getOwner() != figure.getOwner())) {
            GameFigure kickedFigure = field.getContent();

            if (kickedFigure == null) {
                figure.getField().setContent(null);
                field.setContent(figure);


            }else {
                field.getContent().getOwner().getHouse().add(kickedFigure);
                Exercise4.logMessage("Kicking figure of " + field.getContent().getOwner().getName());
                figure.getField().setContent(null);
                field.setContent(figure);

            }
            Exercise4.logMessage("Moved figure to " + field.getId());
        }else {
            Exercise4.logMessage("Target field is occupied. Turn expired.");
        }
    }

    public GameField getIdByFigure(GameFigure figure) {
        for (GameField figures : gamePlan.getGameFields()) {
            if (figures.getContent() != null && figures.getContent().equals(figure)) {
                return figures;
            }
        }
        return null;
    }
}
