package controller;


import board.Board;
import dto.Pixel;
import helper.ColorFunctionality;
import file.FileSchema;
import grainGrowthAlgorithms.GrainGrowth;
import grainGrowthAlgorithms.VonNeumann;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
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
    private ChoiceBox<String> inclusionType;

    @FXML
    private Canvas canvas;


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
                while(!isCoordinateOnSeedBoundry(xPos, yPos)){
                    xPos = random.nextInt(xBoardDimension-incSize+1);
                    yPos = random.nextInt(yBoardDimension-incSize+1);
                }
            }
        }
        else{
            xPos = random.nextInt(xBoardDimension-2*incSize) + incSize;
            yPos = random.nextInt(yBoardDimension-2*incSize) + incSize;

            if (isAfterAlgorithm){
                while(!isCoordinateOnSeedBoundry(xPos, yPos)){
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

    private boolean isCoordinateOnSeedBoundry(final int xPos, final int yPos){
        final int xBoardDimension = parseTextFieldToInt(xSizeView);
        final int nextXPos = xPos == xBoardDimension ? xPos : xPos + 1;
        final int prevXPos = xPos == 0 ? xPos : xPos - 1;

        final int yBoardDimension = parseTextFieldToInt(ySizeView);
        final int nextYPos = yPos == yBoardDimension ? yPos : yPos + 1;
        final int prevYPos = yPos == 0 ? yPos : yPos - 1;

        return isTwoPixelsHasDifferentId(new Pixel(xPos, yPos), new Pixel(nextXPos, yPos)) ||
                isTwoPixelsHasDifferentId(new Pixel(xPos, yPos), new Pixel(prevXPos, yPos)) ||
                isTwoPixelsHasDifferentId(new Pixel(xPos, yPos), new Pixel(xPos, nextYPos)) ||
                isTwoPixelsHasDifferentId(new Pixel(xPos, yPos), new Pixel(xPos, prevYPos));
    }

    private boolean isTwoPixelsHasDifferentId(Pixel firstPixel, Pixel secondPixel){
        return ( board.getBoard()[firstPixel.getyPosition()][firstPixel.getxPosition()].getId() !=
                 board.getBoard()[secondPixel.getyPosition()][secondPixel.getxPosition()].getId() )
                &&
                ( board.getBoard()[firstPixel.getyPosition()][firstPixel.getxPosition()].getId() != 100 &&
                  board.getBoard()[secondPixel.getyPosition()][secondPixel.getxPosition()].getId() != 100 );
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
        String filePath = file.getPath();
        FileSchema fileSchema = new FileSchema(filePath);

        final int xBoardDimension = fileSchema.getxSize();
        final int yBoardDimension = fileSchema.getySize();
        board = new Board(xBoardDimension, yBoardDimension, canvas);

        fileSchema.getPointList()
            .forEach(field -> {
                final int xRealPosition = field.getxPosition();
                final int yRealPosition = field.getyPosition();
                final java.awt.Color awtColor = field.getColor();
                final Color fxColor = convertAwtColorToFxColor(awtColor);

                board.fillPixel(xRealPosition, yRealPosition, fxColor);
            });
    }

    @FXML
    public void generateBoard(){
        final int iSeedAmount = parseTextFieldToInt(seedAmount);
        final int xBoardDimension = parseTextFieldToInt(xSizeView);
        final int yBoardDimension = parseTextFieldToInt(ySizeView);

        if (board == null)
            board = new Board(xBoardDimension, yBoardDimension, canvas);

        // inicjacja algorytmu
        GrainGrowth grainAlgorithm =
                new VonNeumann(board,
                            iSeedAmount,
                            xBoardDimension,
                            yBoardDimension);

        // wylosuj ziarna
        grainAlgorithm.randomSeed();

        // przelicz algorytm
        grainAlgorithm.calculate();

        // mapowanie koloru do id
        board.matchColorToModifiedFields();

        //rysowanie pikseli
        board.redraw();
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

    enum FileOperationType{
        Import,
        Export
    }

}
