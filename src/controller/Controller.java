package controller;


import board.Board;
import dto.Pixel;
import file.FileSchema;
import grainGrowthAlgorithms.GrainGrowth;
import grainGrowthAlgorithms.VonNeumann;
import helper.ColorFunctionality;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.util.Arrays;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.stream.Stream;



public class Controller extends ColorFunctionality implements Initializable {

    private Board board;

    private int clickedSeedId;

    @FXML
    private BorderPane borderPane;

    @FXML
    private TextField xSizeView;

    @FXML
    private TextField ySizeView;

    @FXML
    private TextField seedAmount;

    @FXML
    private TextField inclusionAmount;

    @FXML
    private TextField inclusionSize;

    @FXML
    private TextField simulationStepNumber;

    @FXML
    private ChoiceBox<String> inclusionType;

    @FXML
    private Canvas canvas;

    @FXML
    private Canvas clickedColor;



    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ObservableList<String> availableChoices = FXCollections.observableArrayList("Square", "Circle");
        inclusionType.setItems(availableChoices);
        inclusionType.getSelectionModel().select("Square");
    }

    private Pixel determineCoordinate(){
        final int xBoardDimension = parseTextFieldToInt(xSizeView);
        final int yBoardDimension = parseTextFieldToInt(ySizeView);
        final int incSize = parseTextFieldToInt(inclusionSize);
        final String incType = inclusionType.getValue();
        final Random random = new Random();

        final boolean isAfterAlgorithm = Arrays.stream(board.getBoard()).flatMap(Stream::of)
                .allMatch((field) -> field.getId() != 0);

        int xPos, yPos;
        if (incType.equals("Square")){
            xPos = random.nextInt(xBoardDimension-incSize+1);
            yPos = random.nextInt(yBoardDimension-incSize+1);

            if (isAfterAlgorithm){
                while(!isCoordinateOnSeedBoundary(xPos, yPos)){
                    xPos = random.nextInt(xBoardDimension-incSize+1);
                    yPos = random.nextInt(yBoardDimension-incSize+1);
                }
            }
        }
        else{
            xPos = random.nextInt(xBoardDimension-2*incSize) + incSize;
            yPos = random.nextInt(yBoardDimension-2*incSize) + incSize;

            if (isAfterAlgorithm){
                while(!isCoordinateOnSeedBoundary(xPos, yPos)){
                    xPos = random.nextInt(xBoardDimension-2*incSize) + incSize;
                    yPos = random.nextInt(yBoardDimension-2*incSize) + incSize;
                }
            }
        }

        return new Pixel(xPos,yPos);
    }

    //TODO: zrefaktoryzowac funkcje 'determineCoordinate' za pomoca ponizszej funkcji
//    private Pixel randomCoordinateForInclusion(){
//        final int xBoardDimension = parseTextFieldToInt(xSizeView);
//        final int yBoardDimension = parseTextFieldToInt(ySizeView);
//        final int incSize = parseTextFieldToInt(inclusionSize);
//        final String incType = inclusionType.getValue();
//        final Random random = new Random();
//
//        int xPos, yPos;
//        if (incType.equals("Square")){
//            xPos = random.nextInt(xBoardDimension-incSize+1);
//            yPos = random.nextInt(yBoardDimension-incSize+1);
//        }
//        else{
//            xPos = random.nextInt(xBoardDimension-2*incSize) + incSize;
//            yPos = random.nextInt(yBoardDimension-2*incSize) + incSize;
//        }
//
//        return new Pixel(xPos, yPos);
//    }

    private boolean isCoordinateOnSeedBoundary(final int xPos, final int yPos){
        final int xBoardDimension = parseTextFieldToInt(xSizeView);
        final int nextXPos = xPos >= xBoardDimension ? xPos : xPos + 1;

        final int yBoardDimension = parseTextFieldToInt(ySizeView);
        final int nextYPos = yPos >= yBoardDimension ? yPos : yPos + 1;

        // check if seed is clicked
        if (clickedSeedId > 0){
            return isPixelOnBoundarySelectedSeed(new Pixel(xPos, yPos), new Pixel(nextXPos, yPos)) ||
                    isPixelOnBoundarySelectedSeed(new Pixel(xPos, yPos), new Pixel(xPos, nextYPos));
        }
        else{
            return isTwoPixelsHasDifferentId(new Pixel(xPos, yPos), new Pixel(nextXPos, yPos)) ||
                    isTwoPixelsHasDifferentId(new Pixel(xPos, yPos), new Pixel(xPos, nextYPos));
        }
    }

    private boolean isPixelOnBoundarySelectedSeed(Pixel firstPixel, Pixel secondPixel){
        if (board.getBoard()[firstPixel.getyPosition()][firstPixel.getxPosition()].getId() == clickedSeedId ||
            board.getBoard()[secondPixel.getyPosition()][secondPixel.getxPosition()].getId() == clickedSeedId)

            return isTwoPixelsHasDifferentId(firstPixel, secondPixel);

        else
            return false;
    }

    private boolean isTwoPixelsHasDifferentId(Pixel firstPixel, Pixel secondPixel) {
        return (board.getBoard()[firstPixel.getyPosition()][firstPixel.getxPosition()].getId() !=
                board.getBoard()[secondPixel.getyPosition()][secondPixel.getxPosition()].getId())
                &&
                (board.getBoard()[firstPixel.getyPosition()][firstPixel.getxPosition()].getId() != 100 &&
                        board.getBoard()[secondPixel.getyPosition()][secondPixel.getxPosition()].getId() != 100);
    }

    private boolean isCoordinateOnSelectedSeedBoundary(Pixel firstPixel, Pixel secondPixel) {
        return (board.getBoard()[firstPixel.getyPosition()][firstPixel.getxPosition()].getId() != clickedSeedId &&
                board.getBoard()[secondPixel.getyPosition()][secondPixel.getxPosition()].getId() == clickedSeedId)
                ||
                (board.getBoard()[firstPixel.getyPosition()][firstPixel.getxPosition()].getId() == clickedSeedId &&
                board.getBoard()[secondPixel.getyPosition()][secondPixel.getxPosition()].getId() != clickedSeedId);
    }


    @FXML
    public void addInclusions(){
        final int iInclusionAmount = parseTextFieldToInt(inclusionAmount);
        final String sInclusionType = inclusionType.getSelectionModel().getSelectedItem();

        if (board == null) {
            final int xBoardDimension = parseTextFieldToInt(xSizeView);
            final int yBoardDimension = parseTextFieldToInt(ySizeView);
            board = new Board(xBoardDimension, yBoardDimension, canvas);
        }
        else{
            final int xBoardDimension = (int) (canvas.getWidth())/5;
            final int yBoardDimension = (int) (canvas.getHeight())/5;
            this.xSizeView.setText(Integer.toString(xBoardDimension));
            this.ySizeView.setText(Integer.toString(yBoardDimension));
        }

        if (sInclusionType.contains("Square")){
            addSquareInclusions(iInclusionAmount);
        }
        else{
            addCircleInclusions(iInclusionAmount);
        }
    }

    private void addCircleInclusions(int iInclusionAmount) {
        for (int i = 0; i < iInclusionAmount; i++) {
            Pixel inclusionCoordinate = determineCoordinate();
            addCircleInclusion(inclusionCoordinate.getxPosition(),
                                inclusionCoordinate.getyPosition());
        }

        board.redraw();
    }

    private void addCircleInclusion(int x0, int y0) {
        final int iInclusionSize = parseTextFieldToInt(inclusionSize);
        int x = iInclusionSize;
        int y = 0;
        int xChange = 1 - (iInclusionSize << 1);
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

    private void addSquareInclusions(int iInclusionAmount) {
        for (int i = 0; i < iInclusionAmount; i++) {
            Pixel inclusionCoordinate = determineCoordinate();
            addSquareInclusion(inclusionCoordinate.getxPosition(),
                                inclusionCoordinate.getyPosition());
        }

        board.redraw();
    }

    private void addSquareInclusion(int xRand, int yRand) {
        final int iInclusionSize = parseTextFieldToInt(inclusionSize);
        final int yBoundary = yRand + iInclusionSize;
        final int xBoundary = xRand + iInclusionSize;

        for (int i = yRand; i < yBoundary; i++) {
            for (int j = xRand; j < xBoundary; j++) {
                fillBlackPixelOnBoard(j, i);
            }
        }
    }

    private void fillBlackPixelOnBoard(int xPos, int yPos){
        board.getBoard()[yPos][xPos].setColor(java.awt.Color.BLACK);
        board.getBoard()[yPos][xPos].setId(100);
        board.getBoard()[yPos][xPos].setColoredPrevStep(true);
    }

    @FXML
    public void importFile() throws IOException {
        File file = selectFile(FileOperationType.Import);
        final String fileName = file.getName();
        final String extension = fileName.substring(fileName.lastIndexOf("."));
        final String TXT_EXTENSION = "txt";
        final String BMP_EXTENSION = "bmp";

        if (extension.contains(TXT_EXTENSION)){
            String filePath = file.getPath();
            FileSchema fileSchema = new FileSchema(filePath);

            final int xBoardDimension = fileSchema.getxSize();
            final int yBoardDimension = fileSchema.getySize();
            board = new Board(xBoardDimension, yBoardDimension, canvas);

            fileSchema.getPointList()
                    .forEach(field -> {
                        final int xRealPosition = field.getxPosition();
                        final int yRealPosition = field.getyPosition();
                        final java.awt.Color awtColor = getMatchedColorToId(field.getId());
                        final Color fxColor = convertAwtColorToFxColor(awtColor);

                        board.fillPixel(xRealPosition, yRealPosition, fxColor);

                        field.setColor(awtColor);
                        board.getBoard()[yRealPosition][xRealPosition] = field;
                    });


        }
        else if (extension.contains(BMP_EXTENSION)){
            BufferedImage bufferedImage = ImageIO.read(file);
            final int xBoardDimension =  bufferedImage.getWidth();
            final int yBoardDimension = bufferedImage.getHeight();

            board = new Board(xBoardDimension, yBoardDimension, canvas);

            for (int y = 0; y < yBoardDimension; y++) {
                for (int x = 0; x < xBoardDimension; x++) {
                    int rgb=bufferedImage.getRGB(x,y);
                    int red = (rgb >> 16) & 0x000000FF;
                    int green = (rgb >>8 ) & 0x000000FF;
                    int blue = (rgb) & 0x000000FF;
                    double redDouble=red/255.0;
                    double greenDouble=green/255.0;
                    double blueDouble=blue/255.0;
                    Color color=Color.color(redDouble,greenDouble,blueDouble);

                    board.getCanvas().getGraphicsContext2D().setFill(color);
                    board.getCanvas().getGraphicsContext2D().fillRect(x+200,y+400,1,1);
                }
            }
        }


    }

    @FXML
    public void generateBoard(){
        final int iSeedAmount = parseTextFieldToInt(seedAmount);
        final int xBoardDimension = parseTextFieldToInt(xSizeView);
        final int yBoardDimension = parseTextFieldToInt(ySizeView);
        int simulationStep;

        if (board == null)
            board = new Board(xBoardDimension, yBoardDimension, canvas);

        if (simulationStepNumber.getText().isEmpty())
            simulationStep = -1;
        else
            simulationStep = parseTextFieldToInt(simulationStepNumber);


        // inicjacja algorytmu
        GrainGrowth grainAlgorithm =
                new VonNeumann(board,
                            iSeedAmount,
                            simulationStep);

        // wylosuj ziarna
        grainAlgorithm.randomSeed();

        // przelicz algorytm
        grainAlgorithm.calculate();

    }

    @FXML
    public void exportFile(){
        final File file = selectFile(FileOperationType.Export);
        final String fileName = file.getName();
        final String extension = fileName.substring(fileName.lastIndexOf("."));
        final String TXT_EXTENSION = "txt";
        final String BMP_EXTENSION = "bmp";

        if (extension.contains(TXT_EXTENSION)){
            final String fileContent = createFileContent();
            saveTextToFile(fileContent, file);
        }
        else if (extension.contains(BMP_EXTENSION)){
            exportBitMap(file);
        }
    }

    private File selectFile(FileOperationType fileOperationType){
        Stage stage = (Stage) borderPane.getScene().getWindow();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save file");
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Text file", "*.txt"),
                new FileChooser.ExtensionFilter("BMP file", "*.bmp"));
        if (fileOperationType == FileOperationType.Export){
            return fileChooser.showSaveDialog(stage);
        }
        else{
            return fileChooser.showOpenDialog(stage);
        }
    }

    private void saveTextToFile(String content, File file) {
        try {
            PrintWriter writer;
            writer = new PrintWriter(file);
            writer.println(content);
            writer.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private String createFileContent(){
        String boardContent = "";
        boardContent += fillFileHeader();
        boardContent += board.getBoardContent();
        return boardContent;
    }

    private String fillFileHeader(){
        final String xSizeBoard = xSizeView.getText().trim();
        final String ySizeBoard = ySizeView.getText().trim();
        return xSizeBoard + " " + ySizeBoard + System.lineSeparator();
    }

    private void exportBitMap(File file) {
        final int xSize = parseTextFieldToInt(xSizeView);
        final int ySize = parseTextFieldToInt(ySizeView);
        final int SCALE = 5;
        final int xRealSize = xSize * SCALE;
        final int yRealSize = ySize * SCALE;

        WritableImage wim = new WritableImage(xRealSize, yRealSize);
        canvas.snapshot(null, wim);

        try {
            ImageIO.write(SwingFXUtils.fromFXImage(canvas.snapshot(null, wim), null), "png", file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void clearBoard(){
        final int xBoardDimension = parseTextFieldToInt(xSizeView);
        final int yBoardDimension = parseTextFieldToInt(ySizeView);

        board = new Board(xBoardDimension, yBoardDimension, canvas);

        board.redraw();
    }

    private int parseTextFieldToInt(TextField field){
        return Integer.valueOf(field.getText());
    }

    @FXML
    public void draw(){
        final int iSeedAmount = parseTextFieldToInt(seedAmount);
        final int xBoardDimension = parseTextFieldToInt(xSizeView);
        final int yBoardDimension = parseTextFieldToInt(ySizeView);
        int simulationStep;

        if (board == null)
            board = new Board(xBoardDimension, yBoardDimension, canvas);

        if (simulationStepNumber.getText().isEmpty())
            simulationStep = -1;
        else
            simulationStep = parseTextFieldToInt(simulationStepNumber);

        // inicjacja algorytmu
        GrainGrowth grainAlgorithm =
                new VonNeumann(board,
                        iSeedAmount,
                        simulationStep);

        // wylosuj ziarna
        board.getBoard()[2][2].setId(1);
        board.getBoard()[7][7].setId(2);

        // przelicz algorytm
        grainAlgorithm.calculate();
    }

    enum FileOperationType{
        Import,
        Export
    }

    // ----------------------
    @FXML
    public void drawBoundary(){
        final int xBoardDimension = parseTextFieldToInt(xSizeView);
        final int yBoardDimension = parseTextFieldToInt(ySizeView);

        // x = [0; max)
        // y = [0; max)
        for (int j = 0; j < xBoardDimension-1; j++) {
            for (int i = 0; i < yBoardDimension-1; i++) {
                if (isCoordinateOnSeedBoundary(j,i))
                    fillBlackPixelOnBoard(j,i);
            }
        }

        //y = max
        for (int i = 0; i < xBoardDimension-1; i++) {

            boolean isOtherPixel;
            if (clickedSeedId == -1)
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
            if (clickedSeedId == -1)
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

    @FXML
    public void clearSeed(){
        Arrays.stream(board.getBoard()).flatMap(Stream::of)
                .forEach(field -> {
                    if (field.getId() != 100)
                        field.setId(0);
                });

        board.redraw();
    }

    @FXML
    public void mouseClicked(MouseEvent event) {
        final int boardPosX = (int) (Math.ceil(event.getX() / 5) - 1);
        final int boardPosY = (int) (Math.ceil(event.getY() / 5) - 1);
        final int canvasClickedColorXDimension = (int) clickedColor.getWidth();
        final int canvasClickedColorYDimension = (int) clickedColor.getHeight();
        final int tmpClickedValue = board.getBoard()[boardPosY][boardPosX].getId();

        if (tmpClickedValue == clickedSeedId) {
            clickedSeedId = -1;
            clickedColor.getGraphicsContext2D().setFill(Color.WHITE);
        } else {
            clickedSeedId = tmpClickedValue;
            final Color canvasClickedColor = convertAwtColorToFxColor(board.getBoard()[boardPosY][boardPosX].getColor());
            clickedColor.getGraphicsContext2D().setFill(canvasClickedColor);
        }

        clickedColor.getGraphicsContext2D().fillRect(0, 0, canvasClickedColorXDimension,
                canvasClickedColorYDimension);
    }


}
