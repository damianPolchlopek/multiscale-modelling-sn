package code.newElement;

import code.board.Board;
import code.dto.Pixel;
import javafx.scene.canvas.Canvas;

import java.util.Arrays;
import java.util.Random;
import java.util.stream.Stream;

public class Inclusions extends NewShape {

    private final String SQUARE_INCLUSION_NAME = "Square";

    private String inclusionType;
    private int inclusionsAmount;
    private int inclusionSize;

    public Inclusions(Canvas canvas,
                      Board board,
                      int xBoardDimension,
                      int yBoardDimension,
                      String inclusionType,
                      int inclusionsAmount,
                      int inclusionSize,
                      int clickedPrevSeedId) {

        super(canvas, board, xBoardDimension, yBoardDimension);
        this.inclusionType = inclusionType;
        this.inclusionsAmount = inclusionsAmount;
        this.inclusionSize = inclusionSize;
        this.clickedPrevSeedId = clickedPrevSeedId;
    }

    public void draw(){
        if (inclusionType.contains(SQUARE_INCLUSION_NAME)){
            addSquareInclusions(inclusionsAmount);
        }
        else{
            addCircleInclusions(inclusionsAmount);
        }
    }

    private Pixel determineCoordinate(){

        final boolean isAfterAlgorithm = Arrays.stream(board.getBoard()).flatMap(Stream::of)
                .allMatch((field) -> field.getId() != 0);

        Pixel pixel;

        if (inclusionType.equals(SQUARE_INCLUSION_NAME)){
            pixel = randomCoordinateForSquareInclusion();

            if (isAfterAlgorithm){
                while(!isCoordinateOnSeedBoundary(pixel))
                    pixel = randomCoordinateForSquareInclusion();
            }
        }
        else{
            pixel = randomCoordinateForCircleInclusion();

            if (isAfterAlgorithm){
                while(!isCoordinateOnSeedBoundary(pixel))
                    pixel = randomCoordinateForCircleInclusion();
            }
        }

        return pixel;
    }

    private Pixel randomCoordinateForSquareInclusion(){
        final Random random = new Random();
        int xPos, yPos;
        xPos = random.nextInt(xBoardDimension-inclusionSize+1);
        yPos = random.nextInt(yBoardDimension-inclusionSize+1);
        return new Pixel(xPos, yPos);
    }

    private Pixel randomCoordinateForCircleInclusion(){
        final Random random = new Random();
        int xPos, yPos;
        xPos = random.nextInt(xBoardDimension-2*inclusionSize) + inclusionSize;
        yPos = random.nextInt(yBoardDimension-2*inclusionSize) + inclusionSize;
        return new Pixel(xPos, yPos);
    }

    private void addCircleInclusions(final int iInclusionAmount) {
        for (int i = 0; i < iInclusionAmount; i++) {
            Pixel inclusionCoordinate = determineCoordinate();
            addCircleInclusion(inclusionCoordinate.getXPosition(),
                    inclusionCoordinate.getYPosition());
        }

        board.redraw();
    }

    private void addCircleInclusion(int x0, int y0) {
        int x = inclusionSize;
        int y = 0;
        int xChange = 1 - (inclusionSize << 1);
        int yChange = 0;
        int radiusError = 0;

        while (x >= y) {
            for (int i = x0 - x; i <= x0 + x; i++) {
                fillBlackPixelOnBoard(i, y0+y);
                fillBlackPixelOnBoard(i, y0-y);
            }

            for (int i = x0 - y; i <= x0 + y; i++) {
                fillBlackPixelOnBoard(i, y0+x);
                fillBlackPixelOnBoard(i, y0-x);
            }

            y++;
            radiusError += yChange;
            yChange += 2;
            if (((radiusError << 1) + xChange) > 0) {
                x--;
                radiusError += xChange;
                xChange += 2;
            }
        }
    }

    private void addSquareInclusions(final int iInclusionAmount) {
        for (int i = 0; i < iInclusionAmount; i++) {
            Pixel inclusionCoordinate = determineCoordinate();
            addSquareInclusion(inclusionCoordinate.getXPosition(),
                    inclusionCoordinate.getYPosition());
        }

        board.redraw();
    }

    private void addSquareInclusion(final int xRand, final int yRand) {
        final int yBoundary = yRand + inclusionSize;
        final int xBoundary = xRand + inclusionSize;

        for (int i = yRand; i < yBoundary; i++) {
            for (int j = xRand; j < xBoundary; j++) {
                fillBlackPixelOnBoard(j, i);
            }
        }
    }

}
