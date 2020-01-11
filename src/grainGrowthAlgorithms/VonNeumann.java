package grainGrowthAlgorithms;

import board.Board;
import board.Field;

import java.util.HashMap;

public class VonNeumann extends GrainGrowth {

    public VonNeumann(Board board, int seedAmount, int simulationStep) {
        super(board, seedAmount, simulationStep);
    }

    public void calculate(){

        // algorytm rozrostu
        boolean isAllBoardFilled = checkIfBoardFilled();
        int currentSimultionStep = 0;

        while (!isAllBoardFilled){

            if (simulationStep == currentSimultionStep)
                break;

            updateColoredPrevStepInNeighbour();

            //iteracja po boardzie i pierwsza iteracja
            for (int i = 0; i < board.getBoard().length; i++) {
                for (int j = 0; j < board.getBoard()[0].length; j++) {

                    Field field = board.getBoard()[i][j];
                    if (field.getId() == 0 && field.isColoredPrevStep() && field.getId() != 100) {
//
                        // algorytm sasiedztwa
                        int xPos = field.getxPosition();
                        int yPos = field.getyPosition();

//                        System.out.println("------------------------------------------");
                        boolean ifFirstRuleCompleted = firstRule(xPos, yPos);
//                        System.out.println("Rule 1: " + ifFirstRuleCompleted);
                        if (ifFirstRuleCompleted)
                            continue;

                        boolean ifSecondRuleCompleted = secondRule(xPos, yPos);
//                        System.out.println("Rule 2: " + ifSecondRuleCompleted);
                        if (ifSecondRuleCompleted)
                            continue;

                        boolean ifThirdRuleCompleted = thirdRule(xPos, yPos);
//                        System.out.println("Rule 3: " + ifThirdRuleCompleted);
                        if (ifThirdRuleCompleted)
                            continue;

                        boolean ifFourthRuleCompleted = fourthRule(xPos, yPos);
//                        System.out.println("Rule 4: " + ifFourthRuleCompleted);
                        if (ifFourthRuleCompleted)
                            continue;

//                        HashMap<Integer, Integer> res = getMapForNeighborhood(j, i);
//                        System.out.println("x: " + j + ", y: " + i + ", most frequent id: " + getMostFrequentNeighbor(j, i) + ", amount: " + res.get(getMostFrequentNeighbor(j, i)));
                    }
                }
            }

            //board.getCanvas().getGraphicsContext2D().restore();
            board.redraw();


//            try {
//////                Thread.sleep(1000);
////                TimeUnit.SECONDS.sleep(1);
////            } catch (InterruptedException e) {
////                e.printStackTrace();
////            }

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



//            new AnimationTimer() {
//                @Override public void handle(long currentNanoTime) {
//                    board.redraw();
//
//                    try {
//                        Thread.sleep(100);
//                    } catch (InterruptedException e) {
//                        // Do nothing
//                    }
//                }
//            }.start();


            System.out.println("Przerysowuje");
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
            addElementToMap(result, board.getBoard()[prevYPosition][prevXPosition].getId());

        //.x.
        if (prevYPosition >= 0 && currentXPosition >= 0)
            addElementToMap(result, board.getBoard()[prevYPosition][currentXPosition].getId());

        //..x
        if (prevYPosition >= 0 && nextXPosition < xBoardDimension)
            addElementToMap(result, board.getBoard()[prevYPosition][nextXPosition].getId());

        //x..
        if (currentYPosition >= 0 && prevXPosition >= 0)
            addElementToMap(result, board.getBoard()[currentYPosition][prevXPosition].getId());

        //..x
        if (currentYPosition >= 0 && nextXPosition < xBoardDimension)
            addElementToMap(result, board.getBoard()[currentYPosition][nextXPosition].getId());

        //x..
        if (nextYPosition < yBoardDimension && prevXPosition >= 0)
            addElementToMap(result, board.getBoard()[nextYPosition][prevXPosition].getId());

        //.x.
        if (nextYPosition < yBoardDimension && currentXPosition >= 0)
            addElementToMap(result, board.getBoard()[nextYPosition][currentXPosition].getId());

        //..x
        if (nextYPosition < yBoardDimension && nextXPosition < xBoardDimension)
            addElementToMap(result, board.getBoard()[nextYPosition][nextXPosition].getId());

        return result;
    }

    private int getMostFrequentNeighbor(final int currentXPosition, final int currentYPosition){
        HashMap<Integer, Integer> res = getMapForNeighborhood(currentXPosition, currentYPosition);
        return getMostFrequentKeyFromMap(res);
    }

    private void addElementToMap(HashMap<Integer, Integer> map, final int id){
        if (id == 0)
            return;

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
            addElementToMap(neighborhood, board.getBoard()[prevYPosition][prevXPosition].getId());

        //.x.
        if (prevYPosition >= 0 && currentXPosition >= 0)
            addElementToMap(neighborhood, board.getBoard()[prevYPosition][currentXPosition].getId());

        //..x
        if (prevYPosition >= 0 && nextXPosition < xBoardDimension)
            addElementToMap(neighborhood, board.getBoard()[prevYPosition][nextXPosition].getId());

        //x..
        if (currentYPosition >= 0 && prevXPosition >= 0)
            addElementToMap(neighborhood, board.getBoard()[currentYPosition][prevXPosition].getId());

        //..x
        if (currentYPosition >= 0 && nextXPosition < xBoardDimension)
            addElementToMap(neighborhood, board.getBoard()[currentYPosition][nextXPosition].getId());

        //x..
        if (nextYPosition < yBoardDimension && prevXPosition >= 0)
            addElementToMap(neighborhood, board.getBoard()[nextYPosition][prevXPosition].getId());

        //.x.
        if (nextYPosition < yBoardDimension && currentXPosition >= 0)
            addElementToMap(neighborhood, board.getBoard()[nextYPosition][currentXPosition].getId());

        //..x
        if (nextYPosition < yBoardDimension && nextXPosition < xBoardDimension)
            addElementToMap(neighborhood, board.getBoard()[nextYPosition][nextXPosition].getId());

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
            addElementToMap(nearestNeighbor, board.getBoard()[prevYPosition][currentXPosition].getId());

        //0 x..
        if (currentYPosition >= 0 && prevXPosition >= 0)
            addElementToMap(nearestNeighbor, board.getBoard()[currentYPosition][prevXPosition].getId());

        //0 ..x
        if (currentYPosition >= 0 && nextXPosition < xBoardDimension)
            addElementToMap(nearestNeighbor, board.getBoard()[currentYPosition][nextXPosition].getId());

        //+1 .x.
        if (nextYPosition < yBoardDimension && currentXPosition >= 0)
            addElementToMap(nearestNeighbor, board.getBoard()[nextYPosition][currentXPosition].getId());

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
            addElementToMap(furtherNeighbor, board.getBoard()[prevYPosition][prevXPosition].getId());

        //-1 ..x
        if (prevYPosition >= 0 && nextXPosition < xBoardDimension)
            addElementToMap(furtherNeighbor,  board.getBoard()[prevYPosition][nextXPosition].getId());

        //+1 x..
        if (nextYPosition < yBoardDimension && prevXPosition >= 0)
            addElementToMap(furtherNeighbor,  board.getBoard()[nextYPosition][prevXPosition].getId());

        //+1 ..x
        if (nextYPosition < yBoardDimension && nextXPosition < xBoardDimension)
            addElementToMap(furtherNeighbor,  board.getBoard()[nextYPosition][nextXPosition].getId());

        int mostFrequentValue = getMostFrequentKeyFromMap(furtherNeighbor);
        if (mostFrequentValue >= 3){
            board.getBoard()[currentYPosition][currentXPosition].setId(mostFrequentValue);
            return true;
        }

//        System.out.println("Third rule - Most frequent furtherNeighbor neighbourhood: " + furtherNeighbor.get(mostFrequentValue));
        return false;
    }

    private boolean fourthRule(final int currentXPosition, final int currentYPosition){
        final int PROBABILITY = 10;
        final HashMap<Integer, Integer> neighborhood = getMapForNeighborhood(currentXPosition, currentYPosition);
        final int mostFrequentNeighbor = getMostFrequentNeighbor(currentXPosition, currentYPosition);

        if (mostFrequentNeighbor == -1)
            return false;

        final int amountOfNeighbor = neighborhood.get(mostFrequentNeighbor);
        final int countedProbability = amountOfNeighbor * 100 / 8;

        if (countedProbability >= PROBABILITY){
            board.getBoard()[currentYPosition][currentXPosition].setId(mostFrequentNeighbor);
            return true;
        }

        return false;
    }

}
