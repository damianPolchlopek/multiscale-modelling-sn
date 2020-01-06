package grainGrowthAlgorithms;

import board.Board;
import board.Field;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Random;
import java.util.stream.Stream;

public abstract class GrainGrowth {

    final int INCLUSION_COLOR = 100;

    Board board;
    int xBoardDimension;
    int yBoardDimension;
    int simulationStep;
    private int seedAmount;
    HashMap<Integer, Integer> pixelNeighborhood;

    GrainGrowth(Board board, int seedAmount, int simulationStep) {
        this.board = board;
        this.seedAmount = seedAmount;
        this.xBoardDimension = board.getBoard()[0].length;
        this.yBoardDimension = board.getBoard().length;
        this.simulationStep = simulationStep;
        this.pixelNeighborhood = new HashMap<>();
    }

    public abstract void calculate();


    boolean checkIfBoardFilled() {
        return Arrays.stream(board.getBoard()).flatMap(Stream::of)
                .allMatch((field) -> field.getId() != 0);
    }

    public void randomSeed(){
        Random random = new Random();
        int xRand;
        int yRand;
        for (int i = 0; i < seedAmount; i++) {
            xRand = random.nextInt(xBoardDimension);
            yRand = random.nextInt(yBoardDimension);
            //TODO: dodac warunek czy nie wylosowano inclusiona
            board.getBoard()[yRand][xRand].setId(i+1); // id equals 0 is default value for field in board
        }
    }

    void updateColoredPrevStepField(){
        Arrays.stream(board.getBoard()).flatMap(Stream::of)
                .forEach(field ->{
                    if (field.getId() != 0) field.setColoredPrevStep(true);
                } );
    }


    void updateColoredPrevStepInNeighbour() {

        for (int i = 0; i < board.getBoard().length; i++) {
            for (int j = 0; j < board.getBoard()[0].length; j++) {

                Field field = board.getBoard()[i][j];
                int xPos = field.getxPosition();
                int yPos = field.getyPosition();

                if (field.getId() != 0) {


                    // gora
                    if (yPos - 1 >= 0)
                        if (board.getBoard()[yPos - 1][xPos].getId() == 0) {
                            board.getBoard()[yPos - 1][xPos].setColoredPrevStep(true);
                        }
                    // prawo
                    if (xPos + 1 < xBoardDimension)
                        if (board.getBoard()[yPos][xPos + 1].getId() == 0) {
                            board.getBoard()[yPos][xPos + 1].setColoredPrevStep(true);
                        }
                    // dol
                    if (yPos + 1 < yBoardDimension)
                        if (board.getBoard()[yPos + 1][xPos].getId() == 0) {
                            board.getBoard()[yPos + 1][xPos].setColoredPrevStep(true);
                        }
                    // lewo
                    if (xPos - 1 >= 0)
                        if (board.getBoard()[yPos][xPos - 1].getId() == 0) {
                            board.getBoard()[yPos][xPos - 1].setColoredPrevStep(true);


                        }


                }
            }


        }
    }


}
