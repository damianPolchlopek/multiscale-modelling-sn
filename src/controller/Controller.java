package controller;


import board.Board;
import helper.ColorFunctionality;
import file.FileSchema;
import grainGrowthAlgorithms.GrainGrowth;
import grainGrowthAlgorithms.VonNeumann;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
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


public class Controller extends ColorFunctionality {

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
    private Canvas canvas;


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
        final int seedAmount = getSeedAmount();
        final int xBoardDimension = getXBoardDimension();
        final int yBoardDimension = getYBoardDimension();

        // stworz board
        board = new Board(xBoardDimension, yBoardDimension, canvas);

        // inicjacja algorytmu
        GrainGrowth grainAlgorithm =
                new VonNeumann(board,
                                seedAmount,
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
    public void clearBoard(){
        board = new Board(100, 100, canvas);
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
        final int xSize = getXBoardDimension();
        final int ySize = getYBoardDimension();
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
    public void drawBoard(){
        final int xBoardDimension = getXBoardDimension();
        final int yBoardDimension = getYBoardDimension();
        board = new Board(xBoardDimension, yBoardDimension, canvas);
        board.initBoard();
        board.fillPixel(2,3, Color.GREEN);
        board.fillPixel(7,7, Color.RED);

        board.getBoard()[3][2].setId(1);
        board.getBoard()[7][7].setId(2);


        final int seedAmount = getSeedAmount();

        // inicjacja algorytmu
        GrainGrowth grainAlgorithm =
                new VonNeumann(board,
                        seedAmount,
                        xBoardDimension,
                        yBoardDimension);

        // przelicz algorytm
        grainAlgorithm.calculate();

        // mapowanie koloru do id
        board.matchColorToModifiedFields();

        //rysowanie pikseli
        board.redraw();


//        final int seedAmount = getSeedAmount();
//        VonNeumann gainAlgoruthm = new VonNeumann(board, seedAmount, xBoardDimension, yBoardDimension);
//        gainAlgoruthm.calculate();
//
//        // mapowanie koloru do id
//        Arrays.stream(board.getBoard()).flatMap(Stream::of)
//                .forEach(field ->{
//                    final java.awt.Color color = getMatchedColorToId(field.getId());
//                    field.setColor(color);
//                });
//
//
//        //rysowanie pikseli
//        redraw();
//
//                Arrays.stream(board.getBoard()).flatMap(Stream::of)
//                .forEach(field -> board.fillPixel(
//                                                    field.getyPosition(),
//                        field.getxPosition(),
//                                                    convertAwtColorToFxColor(field.getColor())));

    }

    private int getXBoardDimension(){
        return Integer.valueOf(xSizeView.getText());
    }

    private int getYBoardDimension(){
        return Integer.valueOf(ySizeView.getText());
    }

    private int getSeedAmount(){
        return Integer.valueOf(seedAmount.getText());
    }

    enum FileOperationType{
        Import,
        Export
    }

}
