package controller;


import board.Board;
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
import java.util.Random;
import java.util.ResourceBundle;


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

    @FXML
    public void addInclusions(){
        final int iInclusionAmount = parseTextFieldToInt(inclusionAmount);
        final int iInclusionSize = parseTextFieldToInt(inclusionSize);
        final String sInclusionType = inclusionType.getSelectionModel().getSelectedItem();

        if (board == null) {
            final int xBoardDimension = parseTextFieldToInt(xSizeView);
            final int yBoardDimension = parseTextFieldToInt(ySizeView);
            board = new Board(xBoardDimension, yBoardDimension, canvas);
        }

        if (sInclusionType.contains("Square")){
            addSquareInclusions(iInclusionAmount, iInclusionSize);
        }
        else{
            addCircleInclusions(iInclusionAmount, iInclusionSize);
        }

    }

    private void addCircleInclusions(int iInclusionAmount, int iInclusionSize) {
        final int xBoardDimension = parseTextFieldToInt(xSizeView);
        final int yBoardDimension = parseTextFieldToInt(ySizeView);

        Random random = new Random();
        int yRand, xRand;
        for (int i = 0; i < iInclusionAmount; i++) {
            xRand = random.nextInt(xBoardDimension);
            yRand = random.nextInt(yBoardDimension);

            addCircleInclusion(xRand, yRand, iInclusionSize);
        }

        board.redraw();
    }

    private void addCircleInclusion(int x0, int y0, int iInclusionSize) {
        int x = iInclusionSize;
        int y = 0;
        int xChange = 1 - (iInclusionSize << 1);
        int yChange = 0;
        int radiusError = 0;

        int firstBoundary, tmpFirstBoundary;
        int secondBoundary, tmpSecondBoundary;
        int thirdBoundry;

        final int minimalBoundryValue = 0;
        final int maximumBoundryHight = parseTextFieldToInt(ySizeView) - 1;
        final int maximumBoundryWidth = parseTextFieldToInt(xSizeView) - 1;

        int tmpI;
        while (x >= y)
        {
            System.out.println("While");
            tmpI = x0 - x;
            tmpI = tmpI >= 0 ? tmpI : 0;

            for (int i = tmpI; i <= x0 + x; i++)
            {
                tmpFirstBoundary = y0 + y;
                firstBoundary = tmpFirstBoundary < maximumBoundryWidth ? tmpFirstBoundary : maximumBoundryWidth;

                tmpSecondBoundary = y0 - y;
                secondBoundary = tmpSecondBoundary > minimalBoundryValue ? tmpSecondBoundary : minimalBoundryValue;
                secondBoundary = secondBoundary < maximumBoundryWidth ? secondBoundary : maximumBoundryWidth;

                thirdBoundry = i <= maximumBoundryHight ? i : maximumBoundryHight;

                System.out.println("firstBoundry: " + firstBoundary + ", secondBoundry: " + secondBoundary + ", thirdBoundry: " + thirdBoundry);

                board.getBoard()[thirdBoundry][firstBoundary].setColor(java.awt.Color.BLACK);
                board.getBoard()[thirdBoundry][firstBoundary].setId(100);

                board.getBoard()[thirdBoundry][secondBoundary].setColor(java.awt.Color.BLACK);
                board.getBoard()[thirdBoundry][secondBoundary].setId(100);

//                board.fillPixel(i, y0 + y, Color.BLACK);
//                board.fillPixel(i, y0 - y, Color.BLACK);
            }


            tmpI = x0 - y;
            tmpI = tmpI >= 0 ? tmpI : 0;
            for (int i = tmpI; i <= x0 + y; i++)
            {
//                System.out.println("yyyyy x: " + i + ", y: " + (y0 + x));

                tmpFirstBoundary = y0 + x;
                firstBoundary = tmpFirstBoundary < maximumBoundryWidth ? tmpFirstBoundary : maximumBoundryWidth;

                tmpSecondBoundary = y0 - x;
                secondBoundary = tmpSecondBoundary > minimalBoundryValue ? tmpSecondBoundary : minimalBoundryValue;
                secondBoundary = secondBoundary < maximumBoundryWidth ? secondBoundary : maximumBoundryWidth;

                thirdBoundry = i <= maximumBoundryHight ? i : maximumBoundryHight;

                board.getBoard()[thirdBoundry][firstBoundary].setColor(java.awt.Color.BLACK);
                board.getBoard()[thirdBoundry][firstBoundary].setId(100);

                board.getBoard()[thirdBoundry][secondBoundary].setColor(java.awt.Color.BLACK);
                board.getBoard()[thirdBoundry][secondBoundary].setId(100);
            }

            y++;
            radiusError += yChange;
            yChange += 2;
            if (((radiusError << 1) + xChange) > 0)
            {
                x--;
                radiusError += xChange;
                xChange += 2;
            }
        }


    }

    private void addSquareInclusions(int iInclusionAmount, int iInclusionSize) {
        final int xBoardDimension = parseTextFieldToInt(xSizeView);
        final int yBoardDimension = parseTextFieldToInt(ySizeView);

        Random random = new Random();
        int yRand, xRand;
        int yBoundary, tmpYBoundary;
        int xBoundary, tmpXBoundary;
        for (int i = 0; i < iInclusionAmount; i++) {
            xRand = random.nextInt(xBoardDimension);
            yRand = random.nextInt(yBoardDimension);

            tmpYBoundary = yRand + iInclusionSize;
            yBoundary = tmpYBoundary <= yBoardDimension ? tmpYBoundary : yBoardDimension;

            tmpXBoundary = xRand + iInclusionSize;
            xBoundary = tmpXBoundary <= xBoardDimension ? tmpXBoundary : xBoardDimension;

            addSquareInclusion(xRand, yRand, xBoundary, yBoundary);
        }

        board.redraw();
    }

    private void addSquareInclusion(int xRand, int yRand, int xBoundary, int yBoundary) {
        for (int i = yRand; i < yBoundary; i++) {
            for (int j = xRand; j < xBoundary; j++) {
                board.getBoard()[i][j].setColor(java.awt.Color.BLACK);
                board.getBoard()[i][j].setId(100);
            }
        }
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

        // stworz board
        if (board == null) {
            board = new Board(xBoardDimension, yBoardDimension, canvas);
        }

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
