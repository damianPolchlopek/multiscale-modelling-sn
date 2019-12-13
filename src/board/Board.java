package board;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

public class Board {

    private final int WIDTH_FIELD = 5;
    private final int HEIGHT_FIELD = 5;

    private Canvas canvas;
    private GraphicsContext graphicsContext;

    private int xSize;
    private int ySize;

    private int xCurrentPosition;
    private int yCurrentPosition;

    private Field[][] board;

    public Board(int xSize, int ySize, Canvas canvas) {
        this.xSize = xSize;
        this.ySize = ySize;
        this.canvas = canvas;
        this.graphicsContext = canvas.getGraphicsContext2D();
        this.xCurrentPosition = 0;
        this.yCurrentPosition = 0;
        this.board = new Field[ySize][xSize];

        canvas.setWidth(xSize*5);
        canvas.setHeight(ySize*5);
    }

    private int getNextXPosition(){
        return xCurrentPosition + WIDTH_FIELD;
    }

    private int getPrevXPosition(){
        return xCurrentPosition - WIDTH_FIELD;
    }

    private int getNextYPosition(){
        return yCurrentPosition + HEIGHT_FIELD;
    }

    private int getPrexYPosition(){
        return yCurrentPosition - HEIGHT_FIELD;
    }

    public void fillPixel(int xPosition, int yPosition, Paint color){
        final int xRealPosition = xPosition * WIDTH_FIELD;
        final int yRealPosition = yPosition * HEIGHT_FIELD;
        this.graphicsContext.setFill(color);
        this.graphicsContext.fillRect(xRealPosition, yRealPosition, WIDTH_FIELD, HEIGHT_FIELD);
    }

    public void initBoard(){
        this.graphicsContext.setFill(Color.RED);
        for (int i = 0; i < xSize*5; i++) {
            //poziome
            this.graphicsContext.fillRect(i, 0, 1,1);
            this.graphicsContext.fillRect(i, (xSize*5)-1, 1,1);

            // pionowe
            this.graphicsContext.fillRect(0, i, 1,1);
            this.graphicsContext.fillRect((ySize*5)-1, i, 1,1);
        }

        for (int i = 0; i < this.ySize; i++) {
            for (int j = 0; j < this.xSize; j++) {
                board[j][i] = new Field(j, i, 0, 0);
            }
        }

    }


    public Field[][] getBoard() {
        return board;
    }

    public void setBoard(Field[][] board) {
        this.board = board;
    }
}
