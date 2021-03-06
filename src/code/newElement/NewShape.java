package code.newElement;

import code.board.Board;
import code.dto.Pixel;
import javafx.scene.canvas.Canvas;

public abstract class NewShape {

    protected Board board;
    Canvas canvas;
    int xBoardDimension;
    int yBoardDimension;
    int clickedPrevSeedId;

    public abstract void draw();

    NewShape(Canvas canvas, Board board, int xBoardDimension, int yBoardDimension) {
        this.canvas = canvas;
        this.board = board;
        this.xBoardDimension = xBoardDimension;
        this.yBoardDimension = yBoardDimension;
    }

    boolean isTwoPixelsHasDifferentId(Pixel firstPixel, Pixel secondPixel) {
        return (board.getBoard()[firstPixel.getYPosition()][firstPixel.getXPosition()].getId() !=
                board.getBoard()[secondPixel.getYPosition()][secondPixel.getXPosition()].getId())
                &&
                (board.getBoard()[firstPixel.getYPosition()][firstPixel.getXPosition()].getId() != 100 &&
                        board.getBoard()[secondPixel.getYPosition()][secondPixel.getXPosition()].getId() != 100);
    }

    void fillBlackPixelOnBoard(int xPos, int yPos){
        board.getBoard()[yPos][xPos].setColor(java.awt.Color.BLACK);
        board.getBoard()[yPos][xPos].setId(100);
        board.getBoard()[yPos][xPos].setColoredPrevStep(true);
    }

    boolean isCoordinateOnSeedBoundary(final Pixel pixel){
        final int xPos = pixel.getXPosition();
        final int yPos = pixel.getYPosition();
        final int nextXPos = xPos >= xBoardDimension ? xPos : xPos + 1;
        final int nextYPos = yPos >= yBoardDimension ? yPos : yPos + 1;

        // check if seed is clicked
        if (clickedPrevSeedId > 0){
            return isPixelOnBoundarySelectedSeed(new Pixel(xPos, yPos), new Pixel(nextXPos, yPos)) ||
                    isPixelOnBoundarySelectedSeed(new Pixel(xPos, yPos), new Pixel(xPos, nextYPos));
        }
        else{
            return isTwoPixelsHasDifferentId(new Pixel(xPos, yPos), new Pixel(nextXPos, yPos)) ||
                    isTwoPixelsHasDifferentId(new Pixel(xPos, yPos), new Pixel(xPos, nextYPos));
        }
    }

    private boolean isPixelOnBoundarySelectedSeed(Pixel firstPixel, Pixel secondPixel){
        if (board.getBoard()[firstPixel.getYPosition()][firstPixel.getXPosition()].getId() == clickedPrevSeedId ||
                board.getBoard()[secondPixel.getYPosition()][secondPixel.getXPosition()].getId() == clickedPrevSeedId)

            return isTwoPixelsHasDifferentId(firstPixel, secondPixel);

        else
            return false;
    }

}
