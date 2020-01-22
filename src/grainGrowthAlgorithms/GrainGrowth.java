package grainGrowthAlgorithms;

import board.Board;
import board.Field;

import java.util.Arrays;
import java.util.Random;
import java.util.stream.Stream;

public abstract class GrainGrowth {

    protected final int INCLUSION_ID = 100;
    protected final int DUAL_PHASE_ID = 101;
    protected final int WHITE_FIELD_ID = 0;

    Board board;
    int xBoardDimension;
    int yBoardDimension;
    int simulationStep;
    private int seedAmount;

    GrainGrowth(Board board, int seedAmount, int simulationStep) {
        this.board = board;
        this.seedAmount = seedAmount;
        this.xBoardDimension = board.getBoard()[0].length;
        this.yBoardDimension = board.getBoard().length;
        this.simulationStep = simulationStep;
    }

    public abstract void calculate();

    boolean checkIfBoardFilled() {
        return Arrays.stream(board.getBoard()).flatMap(Stream::of)
                .allMatch((field) -> field.getId() != 0);
    }

    boolean isSubstractedButtonIsClicked(){
        return Arrays.stream(board.getBoard()).flatMap(Stream::of)
                .anyMatch((field) -> field.getPhase() == 1);
    }

    public void randomSeed(){
        Random random = new Random();
        int xRand;
        int yRand;

        if (isSubstractedButtonIsClicked()){
            for (int i = seedAmount; i < 2*seedAmount; i++) {
                xRand = random.nextInt(xBoardDimension);
                yRand = random.nextInt(yBoardDimension);

                if (board.getBoard()[yRand][xRand].getId() == 0)
                    board.getBoard()[yRand][xRand].setId(i+1);
                else
                    i--;
            }
        }
        else {
            for (int i = 0; i < seedAmount; i++) {
                xRand = random.nextInt(xBoardDimension);
                yRand = random.nextInt(yBoardDimension);

                if (board.getBoard()[yRand][xRand].getId() == 0)
                    board.getBoard()[yRand][xRand].setId(i+1);
                else
                    i--;
            }
        }


    }

    void updateColoredPrevStepInNeighbour() {

        for (int i = 0; i < board.getBoard().length; i++) {
            for (int j = 0; j < board.getBoard()[0].length; j++) {

                Field field = board.getBoard()[i][j];
                int currentXPosition = field.getxPosition();
                int currentYPosition = field.getyPosition();

                if (field.getId() != WHITE_FIELD_ID &&
                    field.getId() != INCLUSION_ID &&
                    field.getId() != DUAL_PHASE_ID) {

                    final int prevXPosition = currentXPosition - 1;
                    final int prevYPosition = currentYPosition - 1;
                    final int nextXPosition = currentXPosition + 1;
                    final int nextYPosition = currentYPosition + 1;

                    //x..
                    if (prevYPosition >= 0 && prevXPosition >= 0)
                        updatePrevColorField(prevXPosition, prevYPosition);

                    //.x.
                    if (prevYPosition >= 0 && currentXPosition >= 0)
                        updatePrevColorField(currentXPosition, prevYPosition);

                    //..x
                    if (prevYPosition >= 0 && nextXPosition < xBoardDimension)
                        updatePrevColorField(nextXPosition, prevYPosition);

                    //x..
                    if (currentYPosition >= 0 && prevXPosition >= 0)
                        updatePrevColorField(prevXPosition, currentYPosition);

                    //..x
                    if (currentYPosition >= 0 && nextXPosition < xBoardDimension)
                        updatePrevColorField(nextXPosition, currentYPosition);

                    //x..
                    if (nextYPosition < yBoardDimension && prevXPosition >= 0)
                        updatePrevColorField(prevXPosition, nextYPosition);

                    //.x.
                    if (nextYPosition < yBoardDimension && currentXPosition >= 0)
                        updatePrevColorField(currentXPosition, nextYPosition);

                    //..x
                    if (nextYPosition < yBoardDimension && nextXPosition < xBoardDimension)
                        updatePrevColorField(nextXPosition, nextYPosition);


//                    // gora
//                    if (yPos - 1 >= 0)
//                        if (board.getBoard()[yPos - 1][xPos].getId() == 0) {
//                            board.getBoard()[yPos - 1][xPos].setColoredPrevStep(true);
//                        }
//
//
//                    // prawo
//                    if (xPos + 1 < xBoardDimension)
//                        if (board.getBoard()[yPos][xPos + 1].getId() == 0) {
//                            board.getBoard()[yPos][xPos + 1].setColoredPrevStep(true);
//                        }
//
//
//                    // dol
//                    if (yPos + 1 < yBoardDimension)
//                        if (board.getBoard()[yPos + 1][xPos].getId() == 0) {
//                            board.getBoard()[yPos + 1][xPos].setColoredPrevStep(true);
//                        }
//
//
//                    // lewo
//                    if (xPos - 1 >= 0)
//                        if (board.getBoard()[yPos][xPos - 1].getId() == 0) {
//                            board.getBoard()[yPos][xPos - 1].setColoredPrevStep(true);
//                        }


                }
            }

        }
    }

    void updatePrevColorField(final int xPos, final int yPos){
        if (board.getBoard()[yPos][xPos].getId() == 0) {
            board.getBoard()[yPos][xPos].setColoredPrevStep(true);
        }
    }


}
