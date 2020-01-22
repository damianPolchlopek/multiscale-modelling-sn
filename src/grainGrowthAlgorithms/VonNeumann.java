package grainGrowthAlgorithms;

import board.Board;
import board.Field;

import java.util.HashMap;
import java.util.Random;

public class VonNeumann extends GrainGrowth {

    public VonNeumann(Board board, int seedAmount, int simulationStep) {
        super(board, seedAmount, simulationStep);
    }

    public void calculate(){


        boolean isAllBoardFilled = checkIfBoardFilled();
        int currentSimultionStep = 0;

        while (!isAllBoardFilled){

            if (simulationStep == currentSimultionStep)
                break;

            updateColoredPrevStepInNeighbour();

            //iteracja po boardzie
            for (int i = 0; i < board.getBoard().length; i++) {
                for (int j = 0; j < board.getBoard()[0].length; j++) {

                    Field field = board.getBoard()[i][j];

                    if (field.getId() == 0 &&
                            field.isColoredPrevStep() &&
                            field.getId() != 100 &&
                            field.getId() != 101 &&
                            field.getPhase() != 1) {

                        // algorytm sasiedztwa
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

                        boolean ifFourthRuleCompleted = fourthRule(xPos, yPos);
                        if (ifFourthRuleCompleted)
                            continue;

//                        HashMap<Integer, Integer> res = getMapForNeighborhood(j, i);
//                        System.out.println("x: " + j + ", y: " + i + ", most frequent id: " + getMostFrequentNeighbor(j, i) + ", amount: " + res.get(getMostFrequentNeighbor(j, i)));
                    }
                }
            }

            board.redraw();
            isAllBoardFilled = checkIfBoardFilled();
            currentSimultionStep++;

//            Thread t1 = new Thread(){
//                @Override
//                public void run() {
//                    super.run();
//                    board.redraw();
//                }
//            };
//            t1.start();

        }
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

    private int getMostFrequentNeighbor(final int currentXPosition, final int currentYPosition){
        HashMap<Integer, Integer> res = getMapForNeighborhood(currentXPosition, currentYPosition);
        return getMostFrequentKeyFromMap(res);
    }

    private void addElementToMap(HashMap<Integer, Integer> map, final int id, final int phase){
        if (phase == 1)
            System.out.println("[add element to hashmap]Phase: " + phase);

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
        int result = -1;
        for (Integer tmpValue : map.keySet()) {
            if (result < map.get(tmpValue))
                result = tmpValue;
        }

        return result;
    }

    private boolean firstRule(final int currentXPosition, final int currentYPosition){
        final int prevXPosition = currentXPosition - 1;
        final int prevYPosition = currentYPosition - 1;
        final int nextXPosition = currentXPosition + 1;
        final int nextYPosition = currentYPosition + 1;
        HashMap<Integer, Integer> neighborhood = new HashMap<>();

        //x..
        if (prevYPosition >= 0 && prevXPosition >= 0)
            addElementToMap(neighborhood,
                    board.getBoard()[prevYPosition][prevXPosition].getId(),
                    board.getBoard()[prevYPosition][prevXPosition].getPhase());

        //.x.
        if (prevYPosition >= 0 && currentXPosition >= 0)
            addElementToMap(neighborhood,
                    board.getBoard()[prevYPosition][currentXPosition].getId(),
                    board.getBoard()[prevYPosition][currentXPosition].getPhase());

        //..x
        if (prevYPosition >= 0 && nextXPosition < xBoardDimension)
            addElementToMap(neighborhood,
                    board.getBoard()[prevYPosition][nextXPosition].getId(),
                    board.getBoard()[prevYPosition][nextXPosition].getPhase());

        //x..
        if (currentYPosition >= 0 && prevXPosition >= 0)
            addElementToMap(neighborhood,
                    board.getBoard()[currentYPosition][prevXPosition].getId(),
                    board.getBoard()[currentYPosition][prevXPosition].getPhase());

        //..x
        if (currentYPosition >= 0 && nextXPosition < xBoardDimension)
            addElementToMap(neighborhood,
                    board.getBoard()[currentYPosition][nextXPosition].getId(),
                    board.getBoard()[currentYPosition][nextXPosition].getPhase());

        //x..
        if (nextYPosition < yBoardDimension && prevXPosition >= 0)
            addElementToMap(neighborhood,
                    board.getBoard()[nextYPosition][prevXPosition].getId(),
                    board.getBoard()[nextYPosition][prevXPosition].getPhase());

        //.x.
        if (nextYPosition < yBoardDimension && currentXPosition >= 0)
            addElementToMap(neighborhood,
                    board.getBoard()[nextYPosition][currentXPosition].getId(),
                    board.getBoard()[nextYPosition][currentXPosition].getPhase());

        //..x
        if (nextYPosition < yBoardDimension && nextXPosition < xBoardDimension)
            addElementToMap(neighborhood,
                    board.getBoard()[nextYPosition][nextXPosition].getId(),
                    board.getBoard()[nextYPosition][nextXPosition].getPhase());

        int mostFrequentValue = getMostFrequentKeyFromMap(neighborhood);
        if (mostFrequentValue >= 5){
            board.getBoard()[currentYPosition][currentXPosition].setId(mostFrequentValue);
            return true;
        }
        return false;
    }

    private boolean secondRule(final int currentXPosition, final int currentYPosition){
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

        int mostFrequentValue = getMostFrequentKeyFromMap(nearestNeighbor);
        if (mostFrequentValue >= 3){
            board.getBoard()[currentYPosition][currentXPosition].setId(mostFrequentValue);
            return true;
        }

       // System.out.println("Second rule - Most frequent nearest neighbourhood: " + nearestNeighbor.get(mostFrequentValue));
        return false;
    }

    private boolean thirdRule(final int currentXPosition, final int currentYPosition){
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

        int mostFrequentValue = getMostFrequentKeyFromMap(furtherNeighbor);
        if (mostFrequentValue >= 3){
            board.getBoard()[currentYPosition][currentXPosition].setId(mostFrequentValue);
            return true;
        }

//        System.out.println("Third rule - Most frequent furtherNeighbor neighbourhood: " + furtherNeighbor.get(mostFrequentValue));
        return false;
    }

    private boolean fourthRule(final int currentXPosition, final int currentYPosition){
        final int PROBABILITY = 50;
        final HashMap<Integer, Integer> neighborhood = getMapForNeighborhood(currentXPosition, currentYPosition);
        final int mostFrequentNeighbor = getMostFrequentNeighbor(currentXPosition, currentYPosition);


        Random r = new Random();
        int randomValue = r.nextInt(100);


//        if (mostFrequentNeighbor == -1){
//            System.out.println("[fourth rule] false");
//            return false;
//        }


        final int amountOfNeighbor = neighborhood.get(mostFrequentNeighbor);
        final int countedProbability = amountOfNeighbor * 100 / 8;

//        System.out.println("----------------------------------------");
//        System.out.println("x: " + currentXPosition + ", y: " + currentYPosition);
//        System.out.println("Most frequent: " + mostFrequentNeighbor);
//        System.out.println("Amount of neighbor: " + amountOfNeighbor);
//        System.out.println("Probability: " + countedProbability);
//        System.out.println("Random value: " + randomValue);

        if (randomValue >= PROBABILITY){
            board.getBoard()[currentYPosition][currentXPosition].setId(mostFrequentNeighbor);
            return true;
        }

        return false;
    }

}
