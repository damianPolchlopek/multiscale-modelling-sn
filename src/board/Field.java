package board;

import java.awt.*;

public class Field {

    private int xPosition;
    private int yPosition;
    private int phase;
    private int id;
    private Color color;
    private boolean coloredPrevStep;


    public Field(int xPosition, int yPosition, int phase, int id, Color color) {
        this.xPosition = xPosition;
        this.yPosition = yPosition;
        this.phase = phase;
        this.id = id;
        this.color = color;
        this.coloredPrevStep = false;
    }

    public Field(int xPosition, int yPosition, int phase, int id) {
        this.xPosition = xPosition;
        this.yPosition = yPosition;
        this.phase = phase;
        this.id = id;
        this.coloredPrevStep = false;
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

    public int getyPosition() {
        return yPosition;
    }

    public int getPhase() {
        return phase;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public boolean isColoredPrevStep() {
        return coloredPrevStep;
    }

    public void setColoredPrevStep(boolean coloredPrevStep) {
        this.coloredPrevStep = coloredPrevStep;
    }

    @Override
    public String toString() {
        return "Field{" +
                "xPosition=" + xPosition +
                ", yPosition=" + yPosition +
                ", phase=" + phase +
                ", id=" + id +
                ", coloredPrevStep=" + coloredPrevStep +
                '}';
    }
}
