package grainGrowthAlgorithms;

import board.Board;

import java.util.Arrays;
import java.util.Random;
import java.util.stream.Stream;

public abstract class GrainGrowth {

    Board board;
    int xBoardDimension;
    int yBoardDimension;
    private int seedAmount;

    GrainGrowth(Board board, int seedAmount, int xBoardDimension, int yBoardDimension) {
        this.board = board;
        this.seedAmount = seedAmount;
        this.xBoardDimension = xBoardDimension;
        this.yBoardDimension = yBoardDimension;
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
            board.getBoard()[xRand][yRand].setId(i+1); // id equals 0 is default value for field in board
        }
    }

    void updateColoredPrevStepField(){
        Arrays.stream(board.getBoard()).flatMap(Stream::of)
                .forEach(field ->{
                    if (field.getId() != 0) field.setColoredPrevStep(true);
                } );
    }

}
