package inclusions;

import board.Board;
import dto.Pixel;
import javafx.scene.canvas.Canvas;

public class Boundary extends NewShape {

    public Boundary(Canvas canvas, Board board, int xBoardDimension, int yBoardDimension) {
        super(canvas, board, xBoardDimension, yBoardDimension);
    }

    public void draw(){
        // x = [0; max)
        // y = [0; max)
        for (int j = 0; j < xBoardDimension-1; j++) {
            for (int i = 0; i < yBoardDimension-1; i++) {
                if (isCoordinateOnSeedBoundary(new Pixel(j,i)))
                    fillBlackPixelOnBoard(j,i);
            }
        }

        //y = max
        for (int i = 0; i < xBoardDimension-1; i++) {

            boolean isOtherPixel;
            if (clickedPrevSeedId == -1)
                isOtherPixel = isTwoPixelsHasDifferentId(new Pixel(i, yBoardDimension-1),
                        new Pixel(i+1, yBoardDimension-1));
            else
                isOtherPixel = isCoordinateOnSelectedSeedBoundary(new Pixel(i, yBoardDimension-1),
                        new Pixel(i+1, yBoardDimension-1));

            if (isOtherPixel){
                fillBlackPixelOnBoard(i, yBoardDimension-1);
            }
        }

        //x = max
        for (int i = 0; i < yBoardDimension-1; i++) {

            boolean isOtherPixel;
            if (clickedPrevSeedId == -1)
                isOtherPixel = isTwoPixelsHasDifferentId(new Pixel(xBoardDimension-1, i),
                        new Pixel(xBoardDimension-1, i+1));
            else
                isOtherPixel = isCoordinateOnSelectedSeedBoundary(new Pixel(xBoardDimension-1, i),
                        new Pixel(xBoardDimension-1, i+1));

            if (isOtherPixel){
                fillBlackPixelOnBoard(xBoardDimension-1, i);
            }
        }

        board.redraw();
    }

    private boolean isCoordinateOnSelectedSeedBoundary(Pixel firstPixel, Pixel secondPixel) {
        return (board.getBoard()[firstPixel.getyPosition()][firstPixel.getxPosition()].getId() != clickedPrevSeedId &&
                board.getBoard()[secondPixel.getyPosition()][secondPixel.getxPosition()].getId() == clickedPrevSeedId)
                ||
                (board.getBoard()[firstPixel.getyPosition()][firstPixel.getxPosition()].getId() == clickedPrevSeedId &&
                        board.getBoard()[secondPixel.getyPosition()][secondPixel.getxPosition()].getId() != clickedPrevSeedId);
    }

}
