package board;

import helper.ColorFunctionality;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.Arrays;
import java.util.stream.Stream;

public class Board extends ColorFunctionality {

    private final int WIDTH_FIELD = 5;
    private final int HEIGHT_FIELD = 5;

    private Canvas canvas;
    private GraphicsContext graphicsContext;

    private int xSize;
    private int ySize;

    private Field[][] board;

    public Board(int xSize, int ySize, Canvas canvas) {
        this.xSize = xSize;
        this.ySize = ySize;
        this.canvas = canvas;
        this.graphicsContext = canvas.getGraphicsContext2D();
        this.board = new Field[ySize][xSize];

        canvas.setWidth(xSize*5);
        canvas.setHeight(ySize*5);

        initBoard();
    }

    public void fillPixel(int xPosition, int yPosition, Color color){
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
                board[i][j] =
                        new Field(j, i, 0, 0, java.awt.Color.WHITE);
            }
        }

    }

    public void matchColorToModifiedFields(){
        Arrays.stream(board).flatMap(Stream::of)
            .forEach(field ->{
                final java.awt.Color color = getMatchedColorToId(field.getId());
                field.setColor(color);
            });
    }

    public void redraw(){
        Arrays.stream(board).flatMap(Stream::of)
                .forEach(field -> fillPixel(field.getxPosition(),
                        field.getyPosition(),
                        convertAwtColorToFxColor(field.getColor())));
    }

    public String getBoardContent(){
        StringBuilder boardContent = new StringBuilder();
        Arrays.stream(board)
                .flatMap(Stream::of)
                .forEach((field -> boardContent.append(buildRowString(field))));

        return boardContent.toString();
    }

    private String buildRowString(Field field) {
        final String xPos = String.valueOf(field.getxPosition());
        final String yPos = String.valueOf(field.getyPosition());
        final String phase = String.valueOf(field.getPhase());
        final String id = String.valueOf(field.getId());
        return xPos + " " + yPos + " " + phase + " " + id + System.lineSeparator();
    }

    public Field[][] getBoard() {
        return board;
    }

}
