package grainGrowthAlgorithms;

import board.Board;

import java.util.Arrays;
import java.util.stream.Stream;

public class VonNeumann extends GrainGrowth {

    public VonNeumann(Board board, int seedAmount, int xBoardDimension, int yBoardDimension) {
        super(board, seedAmount, xBoardDimension, yBoardDimension);
    }

    public void calculate(){

        // algorytm rozrostu
        boolean isAllBoardFilled = checkIfBoardFilled();
        while (!isAllBoardFilled){

            // ustawienie coloredPrevStep na true
            updateColoredPrevStepField();

            //iteracja po boardzie i pierwsza iteracja
            Arrays.stream(board.getBoard()).flatMap(Stream::of)
                    .forEach(field ->{
                        if (field.isColoredPrevStep() && field.getId() != INCLUSION_COLOR){
                            // algorytm sasiedztwa
                            int xPos = field.getxPosition();
                            int yPos = field.getyPosition();
                            int fieldId = field.getId();

                            // gora
                            if (yPos-1 >= 0)
                                if (board.getBoard()[yPos-1][xPos].getId() == 0) {
                                    board.getBoard()[yPos-1][xPos].setId(fieldId);
                                }
                            // prawo
                            if (xPos+1 < xBoardDimension)
                                if (board.getBoard()[yPos][xPos+1].getId() == 0) {
                                    board.getBoard()[yPos][xPos+1].setId(fieldId);
                                }
                            // dol
                            if (yPos+1 < yBoardDimension)
                                if (board.getBoard()[yPos+1][xPos].getId() == 0) {
                                    board.getBoard()[yPos+1][xPos].setId(fieldId);
                                }
                            // lewo
                            if (xPos-1 >= 0)
                                if (board.getBoard()[yPos][xPos-1].getId() == 0) {
                                    board.getBoard()[yPos][xPos-1].setId(fieldId);
                                }
                        }
                    });

            isAllBoardFilled = checkIfBoardFilled();
        }
    }

}
