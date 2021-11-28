package de.devofvictory.bwinf.exercise4;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GamePlan {

    private List<GameField> gameFields;

    public GamePlan(int fieldAmount) {

        gameFields = new ArrayList<>();

        for (int i = 0; i<fieldAmount; i++) {
            GameField field = new GameField(i);
            gameFields.add(field);
        }
    }

    public void setHouses(HashMap<Integer, GamePlayer> houses) {
        for (int id : houses.keySet()) {
            gameFields.get(id).setLimited(houses.get(id));
        }
    }

    public List<GameField> getHouses(GamePlayer player) {
        List<GameField> houses = new ArrayList<>();
        for (GameField fields : this.getGameFields()) {
            if (fields.getLimited() != null && fields.getLimited().equals(player)) {
                houses.add(fields);
            }
        }
        return houses;
    }

    public List<GameField> getGameFields() {
        return gameFields;
    }

    public void setGameFields(List<GameField> gameFields) {
        this.gameFields = gameFields;
    }
}
