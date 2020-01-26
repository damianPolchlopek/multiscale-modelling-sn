package code.board;

import code.helper.ColorFunctionality;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class Board {

    private final int WIDTH_FIELD = 5;
    private final int HEIGHT_FIELD = 5;

    private Canvas canvas;
    private GraphicsContext graphicsContext;
    private ColorFunctionality colorFunctionality;
    private List<Integer> clickedSeeds = new ArrayList<>();

    private int xSize;
    private int ySize;

    private Field[][] board;

    public Board(int xSize, int ySize, Canvas canvas, ColorFunctionality colorFunctionality) {
        this.xSize = xSize;
        this.ySize = ySize;
        this.canvas = canvas;
        this.graphicsContext = canvas.getGraphicsContext2D();
        this.board = new Field[ySize][xSize];
        this.colorFunctionality = colorFunctionality;

        canvas.setWidth(xSize*WIDTH_FIELD);
        canvas.setHeight(ySize*HEIGHT_FIELD);

        initBoard();
    }

    public void fillPixel(int xPosition, int yPosition, Color color){
        final int xRealPosition = xPosition * WIDTH_FIELD;
        final int yRealPosition = yPosition * HEIGHT_FIELD;
        this.graphicsContext.setFill(color);
        this.graphicsContext.fillRect(xRealPosition, yRealPosition, WIDTH_FIELD, HEIGHT_FIELD);
    }

    private void initBoard(){
        this.graphicsContext.setFill(Color.RED);
        for (int i = 0; i < this.ySize; i++) {
            for (int j = 0; j < this.xSize; j++) {
                board[i][j] =
                        new Field(j, i, 0, 0, java.awt.Color.WHITE);
            }
        }
    }

    private void updateColorInBoardFields(){
        java.awt.Color color;
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                int id = board[i][j].getId();
                color = colorFunctionality.getColor(id);
                board[i][j].setColor(color);
            }
        }
    }

    public void redraw(){

        Arrays.stream(board).flatMap(Stream::of)
                .forEach(field -> {

                    final java.awt.Color color = colorFunctionality.getMatchedColorToId(field.getId());

                    fillPixel(field.getxPosition(),
                            field.getyPosition(),
                            colorFunctionality.convertAwtColorToFxColor(color));

                });

        updateColorInBoardFields();
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

    public void clearSeeds() {
        Arrays.stream(board).flatMap(Stream::of)
                .forEach(field -> {
                    if (field.getId() != 100)
                        field.setId(0);
                });
    }

    public void clearSeedWithoutPhase(){

        Arrays.stream(board).flatMap(Stream::of)
                .forEach(field -> {
                    if (clickedSeeds.contains(field.getId()))
                        field.setPhase(1);
                    field.setId(101);
                });

        Arrays.stream(board).flatMap(Stream::of)
                .forEach(field -> {
                    if (field.getPhase() != 1){
                        field.setId(0);
                        field.setColoredPrevStep(false);
                    }
                });

    }

    public void clearSeedWithoutSubstructure(){

        Arrays.stream(board).flatMap(Stream::of)
                .forEach(field -> {
                    if (clickedSeeds.contains(field.getId()))
                        field.setPhase(1);
                });

        Arrays.stream(board).flatMap(Stream::of)
                .forEach(field -> {
                    if (field.getPhase() != 1){
                        field.setId(0);
                        field.setColoredPrevStep(false);
                    }
                });

    }



    public Field[][] getBoard() {
        return board;
    }

    public Canvas getCanvas() {
        return canvas;
    }

    public void setClickedSeeds(List<Integer> clickedSeeds) {
        this.clickedSeeds = clickedSeeds;
    }

}
