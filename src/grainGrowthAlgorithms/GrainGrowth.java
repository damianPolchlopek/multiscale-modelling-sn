package grainGrowthAlgorithms;

import board.Board;
import board.Field;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Random;
import java.util.stream.Stream;

public class GrainGrowth {

    private final int INCLUSION_ID   = 100;
    private final int DUAL_PHASE_ID  = 101;
    private final int WHITE_FIELD_ID = 0;

    private Board board;
    private int xBoardDimension;
    private int yBoardDimension;
    private int simulationStep;
    private int seedAmount;

    public GrainGrowth(Board board, int seedAmount, int simulationStep) {
        this.board = board;
        this.seedAmount = seedAmount;
        this.xBoardDimension = board.getBoard()[0].length;
        this.yBoardDimension = board.getBoard().length;
        this.simulationStep = simulationStep;
    }

    public void calculate(){
        randomSeed();

        boolean isAllBoardFilled = checkIfBoardFilled();
        int currentSimulationStep = 0;

        while (!isAllBoardFilled){

            if (simulationStep == currentSimulationStep)
                break;

            updateColoredPrevStepInNeighbour();

            //iteracja po boardzie
            for (int i = 0; i < board.getBoard().length; i++) {
                for (int j = 0; j < board.getBoard()[0].length; j++) {

                    Field field = board.getBoard()[i][j];

                    if (checkIfFieldIsAppropriateToGrainGrowthAlgorithm(field)) {

                        int xPos = field.getxPosition();
                        int yPos = field.getyPosition();

                        boolean ifFirstRuleCompleted = firstRule(xPos, yPos);
                        if (ifFirstRuleCompleted)
                            continue;

                        boolean ifSecondRuleCompleted = secondRule(xPos, yPos);
                        if (ifSecondRuleCompleted)
                            continue;

                        boolean ifThirdRuleCompleted = thirdRule(xPos, yPos);
                        if (ifThirdRuleCompleted)
                            continue;

                        fourthRule(xPos, yPos);
                    }
                }
            }

            board.redraw();
            isAllBoardFilled = checkIfBoardFilled();
            currentSimulationStep++;
        }
    }

    private void randomSeed(){
        if (isSubtractedButtonClicked()){
            for (int i = seedAmount; i < 2*seedAmount; i++) {
                i = randomFieldOnBoard(i);
            }
        }
        else {
            for (int i = 0; i < seedAmount; i++) {
                i = randomFieldOnBoard(i);
            }
        }
    }

    private int randomFieldOnBoard(int i) {
        int xRand, yRand;
        Random random = new Random();
        xRand = random.nextInt(xBoardDimension);
        yRand = random.nextInt(yBoardDimension);

        if (board.getBoard()[yRand][xRand].getId() == 0)
            board.getBoard()[yRand][xRand].setId(i+1);
        else
            i--;

        return i;
    }

    private boolean isSubtractedButtonClicked(){
        return Arrays.stream(board.getBoard()).flatMap(Stream::of)
                .anyMatch((field) -> field.getPhase() == 1);
    }

    private boolean checkIfBoardFilled() {
        return Arrays.stream(board.getBoard()).flatMap(Stream::of)
                .allMatch((field) -> field.getId() != 0);
    }

    private void updateColoredPrevStepInNeighbour() {

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

                }
            }

        }
    }

    private boolean checkIfFieldIsAppropriateToGrainGrowthAlgorithm(Field field){
        return field.getId() == WHITE_FIELD_ID &&
                field.getId() != INCLUSION_ID &&
                field.getId() != DUAL_PHASE_ID &&
                field.isColoredPrevStep() &&
                field.getPhase() != 1;
    }

    private void updatePrevColorField(final int xPos, final int yPos){
        if (board.getBoard()[yPos][xPos].getId() == 0) {
            board.getBoard()[yPos][xPos].setColoredPrevStep(true);
        }
    }

    private void addElementToMap(HashMap<Integer, Integer> map, final int id, final int phase){
        if (id == WHITE_FIELD_ID ||
                id == INCLUSION_ID ||
                id == DUAL_PHASE_ID ||
                phase == 1){
            return;
        }

        int count = map.getOrDefault(id, 0);
        map.put(id, count + 1);
    }

    private int getMostFrequentKeyFromMap(HashMap<Integer, Integer> map){
        int key = -1;
        int maxValue = -1;
        for (Integer tmpKey : map.keySet()) {
            if (maxValue < map.get(tmpKey)){
                key =tmpKey;
                maxValue = map.get(tmpKey);
            }
        }

        return key;
    }

    private boolean firstRule(final int currentXPosition, final int currentYPosition){
        HashMap<Integer, Integer> neighborhood
                = getMapForNeighborhood(currentXPosition, currentYPosition);

        return checkNeighborhood(currentXPosition, currentYPosition, neighborhood, 5);
    }

    private HashMap<Integer, Integer> getMapForNeighborhood(final int currentXPosition, final int currentYPosition){
        final int prevXPosition = currentXPosition - 1;
        final int prevYPosition = currentYPosition - 1;
        final int nextXPosition = currentXPosition + 1;
        final int nextYPosition = currentYPosition + 1;
        HashMap<Integer, Integer> result = new HashMap<>();

        //x..
        if (prevYPosition >= 0 && prevXPosition >= 0)
            addElementToMap(result,
                    board.getBoard()[prevYPosition][prevXPosition].getId(),
                    board.getBoard()[prevYPosition][prevXPosition].getPhase());

        //.x.
        if (prevYPosition >= 0 && currentXPosition >= 0)
            addElementToMap(result,
                    board.getBoard()[prevYPosition][currentXPosition].getId(),
                    board.getBoard()[prevYPosition][currentXPosition].getPhase());

        //..x
        if (prevYPosition >= 0 && nextXPosition < xBoardDimension)
            addElementToMap(result,
                    board.getBoard()[prevYPosition][nextXPosition].getId(),
                    board.getBoard()[prevYPosition][nextXPosition].getPhase());

        //x..
        if (currentYPosition >= 0 && prevXPosition >= 0)
            addElementToMap(result,
                    board.getBoard()[currentYPosition][prevXPosition].getId(),
                    board.getBoard()[currentYPosition][prevXPosition].getPhase());

        //..x
        if (currentYPosition >= 0 && nextXPosition < xBoardDimension)
            addElementToMap(result,
                    board.getBoard()[currentYPosition][nextXPosition].getId(),
                    board.getBoard()[currentYPosition][nextXPosition].getPhase());

        //x..
        if (nextYPosition < yBoardDimension && prevXPosition >= 0)
            addElementToMap(result,
                    board.getBoard()[nextYPosition][prevXPosition].getId(),
                    board.getBoard()[nextYPosition][prevXPosition].getPhase());

        //.x.
        if (nextYPosition < yBoardDimension && currentXPosition >= 0)
            addElementToMap(result,
                    board.getBoard()[nextYPosition][currentXPosition].getId(),
                    board.getBoard()[nextYPosition][currentXPosition].getPhase());

        //..x
        if (nextYPosition < yBoardDimension && nextXPosition < xBoardDimension)
            addElementToMap(result,
                    board.getBoard()[nextYPosition][nextXPosition].getId(),
                    board.getBoard()[nextYPosition][nextXPosition].getPhase());

        return result;
    }

    private boolean secondRule(final int currentXPosition, final int currentYPosition){
        HashMap<Integer, Integer> nearestNeighborhood
                = getMapForNearestNeighborhood(currentXPosition, currentYPosition);

        return checkNeighborhood(currentXPosition, currentYPosition, nearestNeighborhood, 3);
    }

    private HashMap<Integer, Integer> getMapForNearestNeighborhood(final int currentXPosition, final int currentYPosition){
        final int prevXPosition = currentXPosition - 1;
        final int prevYPosition = currentYPosition - 1;
        final int nextXPosition = currentXPosition + 1;
        final int nextYPosition = currentYPosition + 1;
        HashMap<Integer, Integer> nearestNeighbor = new HashMap<>();

        //-1 .x.
        if (prevYPosition >= 0 && currentXPosition >= 0)
            addElementToMap(nearestNeighbor,
                    board.getBoard()[prevYPosition][currentXPosition].getId(),
                    board.getBoard()[prevYPosition][currentXPosition].getPhase());

        //0 x..
        if (currentYPosition >= 0 && prevXPosition >= 0)
            addElementToMap(nearestNeighbor,
                    board.getBoard()[currentYPosition][prevXPosition].getId(),
                    board.getBoard()[currentYPosition][prevXPosition].getPhase());

        //0 ..x
        if (currentYPosition >= 0 && nextXPosition < xBoardDimension)
            addElementToMap(nearestNeighbor,
                    board.getBoard()[currentYPosition][nextXPosition].getId(),
                    board.getBoard()[currentYPosition][nextXPosition].getPhase());

        //+1 .x.
        if (nextYPosition < yBoardDimension && currentXPosition >= 0)
            addElementToMap(nearestNeighbor,
                    board.getBoard()[nextYPosition][currentXPosition].getId(),
                    board.getBoard()[nextYPosition][currentXPosition].getPhase());



        return nearestNeighbor;
    }

    private boolean thirdRule(final int currentXPosition, final int currentYPosition){
        HashMap<Integer, Integer> furtherNeighborhood
                = getMapForFurtherNeighborhood(currentXPosition, currentYPosition);

        return checkNeighborhood(currentXPosition, currentYPosition, furtherNeighborhood, 3);
    }

    private HashMap<Integer, Integer> getMapForFurtherNeighborhood(final int currentXPosition, final int currentYPosition){
        final int prevXPosition = currentXPosition - 1;
        final int prevYPosition = currentYPosition - 1;
        final int nextXPosition = currentXPosition + 1;
        final int nextYPosition = currentYPosition + 1;
        HashMap<Integer, Integer> furtherNeighbor = new HashMap<>();

        //-1 x..
        if (prevYPosition >= 0 && prevXPosition >= 0)
            addElementToMap(furtherNeighbor,
                    board.getBoard()[prevYPosition][prevXPosition].getId(),
                    board.getBoard()[prevYPosition][prevXPosition].getPhase());

        //-1 ..x
        if (prevYPosition >= 0 && nextXPosition < xBoardDimension)
            addElementToMap(furtherNeighbor,
                    board.getBoard()[prevYPosition][nextXPosition].getId(),
                    board.getBoard()[prevYPosition][nextXPosition].getPhase());

        //+1 x..
        if (nextYPosition < yBoardDimension && prevXPosition >= 0)
            addElementToMap(furtherNeighbor,
                    board.getBoard()[nextYPosition][prevXPosition].getId(),
                    board.getBoard()[nextYPosition][prevXPosition].getPhase());

        //+1 ..x
        if (nextYPosition < yBoardDimension && nextXPosition < xBoardDimension)
            addElementToMap(furtherNeighbor,
                    board.getBoard()[nextYPosition][nextXPosition].getId(),
                    board.getBoard()[nextYPosition][nextXPosition].getPhase());

        return furtherNeighbor;
    }

    private boolean checkNeighborhood(int currentXPosition, int currentYPosition, HashMap<Integer, Integer> furtherNeighbor, int requirement) {
        int mostFrequentKey = getMostFrequentKeyFromMap(furtherNeighbor);

        if (checkRequirementForNeighborhood(furtherNeighbor, requirement)){
            board.getBoard()[currentYPosition][currentXPosition].setId(mostFrequentKey);
            return true;
        }

        return false;
    }

    private boolean checkRequirementForNeighborhood(final HashMap<Integer, Integer> neighbor, final int requirement){

        final int mostFrequentKey = getMostFrequentKeyFromMap(neighbor);
        if (mostFrequentKey == -1)
            return false;

        final int mostFrequentValue = neighbor.get(mostFrequentKey);
        return mostFrequentValue >= requirement;
    }

    private void fourthRule(final int currentXPosition, final int currentYPosition){
        HashMap<Integer, Integer> neighbourhood = getMapForNeighborhood(currentXPosition, currentYPosition);
        final int mostFrequentKey = getMostFrequentKeyFromMap(neighbourhood);

        // jesli nie ma ani jednego normalnego id w sasiedztwie to funkcja powyzej zwroci -1
        // a to znaczy ze nie chcemy updatowac fielda dlatego konczymy
        if (mostFrequentKey == -1)
            return;

        final Random r = new Random();
        final int randomValue = r.nextInt(100);
        final int PROBABILITY = 50;

        if (randomValue >= PROBABILITY){
            board.getBoard()[currentYPosition][currentXPosition].setId(mostFrequentKey);
        }

    }

}
