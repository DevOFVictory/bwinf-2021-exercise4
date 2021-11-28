package de.devofvictory.bwinf.exercise4;

public class GameFigure {

    private GamePlayer owner;
    private Game game;

    public GameFigure(Game game, GamePlayer owner) {
        this.game = game;
        this.owner = owner;
    }

    public GamePlayer getOwner() {
        return owner;
    }

    public GameField getField() {
        return this.game.getIdByFigure(this);
    }

    public GameField getTargetField(int diced) {
        int targetId = this.game.getIdByFigure(this).getId();

        int counter = 0;

        while (counter != diced) {
            GameField field = this.game.getFieldById(targetId+1);
            if ((field != null && field.getLimited() == null) || (field != null && field.getLimited().equals(this.getOwner()))) {
                targetId++;
                counter++;

                if (targetId == this.getOwner().getEndFieldId() && counter < diced) {
                    Exercise4.logMessage("With diced number, the figure cant fit into its home.");
                    return null;
                }

                if (targetId >= this.game.getGamePlan().getGameFields().size() -1 && counter < diced) {
                    targetId = 0;
                    Exercise4.logMessage("Figure is at beginning of game plan.");
                }

            }else {
                targetId ++;
                if (targetId >= this.game.getGamePlan().getGameFields().size() -1) {
                    targetId = 0;
                    Exercise4.logMessage("Figure is at beginning of game plan.");
                }
            }
        }
        return this.game.getFieldById(targetId);
    }

    public boolean isInEnd() {
        return this.getField().getLimited() != null && this.getField().getLimited().equals(this.getOwner());
    }

    @Override
    public String toString() {
        return "GameFigure{" +
                "owner=" + owner + ", " +
                "fieldId=" + getField().getId() +
                '}';
    }
}
