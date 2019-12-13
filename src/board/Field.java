package board;

public class Field {

    private final int WIDTH_FIELD = 5;
    private final int HEIGHT_FIELD = 5;

    private int xBoardPosition;
    private int yBoardPosition;
    private int phase;
    private int id;

    public Field(int xPosition, int yPosition, int phase, int id) {
        this.xBoardPosition = convertLoadedXPositionToRealXPosition(xPosition);
        this.yBoardPosition = convertLoadedYPositionToRealYPosition(yPosition);
        this.phase = phase;
        this.id = id;
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
}
