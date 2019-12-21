package board;


import java.awt.*;

public class Field {

    private final int WIDTH_FIELD = 5;
    private final int HEIGHT_FIELD = 5;

    private int xBoardPosition;
    private int yBoardPosition;
    private int xPosition;
    private int yPosition;
    private int phase;
    private int id;
    private Color color;

    public Field(int xPosition, int yPosition, int phase, int id, Color color) {
        this.xPosition = xPosition;
        this.yPosition = yPosition;
        this.xBoardPosition = convertLoadedXPositionToRealXPosition(xPosition);
        this.yBoardPosition = convertLoadedYPositionToRealYPosition(yPosition);
        this.phase = phase;
        this.id = id;
        this.color = color;
    }

    private int convertLoadedXPositionToRealXPosition(final int xPosition){
        return xPosition * WIDTH_FIELD;
    }

    private int convertLoadedYPositionToRealYPosition(final int yPosition){
        return yPosition * HEIGHT_FIELD;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getxPosition() {
        return xPosition;
    }

    public void setxPosition(int xPosition) {
        this.xPosition = xPosition;
    }

    public int getyPosition() {
        return yPosition;
    }

    public void setyPosition(int yPosition) {
        this.yPosition = yPosition;
    }

    public int getPhase() {
        return phase;
    }

    public void setPhase(int phase) {
        this.phase = phase;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }
}
