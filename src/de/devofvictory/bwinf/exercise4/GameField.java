package de.devofvictory.bwinf.exercise4;

public class GameField {

    private int id;
    private GameFigure content;
    private GamePlayer limited;

    public GameField(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public GameFigure getContent() {
        return content;
    }

    public void setContent(GameFigure content) {
        this.content = content;
    }

    public GamePlayer getLimited() {
        return limited;
    }

    public void setLimited(GamePlayer limited) {
        this.limited = limited;
    }

    public boolean isOccupied() {
        return this.content != null;
    }

    @Override
    public String toString() {
        return "GameField{" +
                "id=" + id +
                ", content=" + content + '}';
    }
}
