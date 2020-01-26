package code.controller;


import code.board.Board;
import code.file.operation.FileExport;
import code.file.operation.FileImport;
import code.grainGrowthAlgorithms.GrainGrowth;
import code.helper.ColorFunctionality;
import code.inclusions.Boundary;
import code.inclusions.Inclusions;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;


public class Controller implements Initializable {

    private ColorFunctionality colorFunctionality = new ColorFunctionality();

    private int xBoardDimension;
    private int yBoardDimension;
    private int nucleonsAmount;
    private int inclusionsAmount;
    private int inclusionSize;
    private String inclusionType;

    private Board board;
    private int clickedPrevSeedId;
    private List<Integer> clickedSeeds = new ArrayList<>();

    @FXML
    private BorderPane borderPane;

    @FXML
    private TextField xSizeView;

    @FXML
    private TextField ySizeView;

    @FXML
    private TextField nucleonsAmountView;

    @FXML
    private TextField inclusionsAmountView;

    @FXML
    private TextField inclusionSizeView;

    @FXML
    private TextField simulationStepNumber;

    @FXML
    private ChoiceBox<String> inclusionTypeView;

    @FXML
    private Canvas canvas;

    @FXML
    private Canvas clickedColor;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        final String SQUARE_INCLUSION_NAME = "Square";
        final String CIRCLE_INCLUSION_NAME = "Circle";

        ObservableList<String> availableChoices = FXCollections.observableArrayList(SQUARE_INCLUSION_NAME,
                CIRCLE_INCLUSION_NAME);
        inclusionTypeView.setItems(availableChoices);
        inclusionTypeView.getSelectionModel().select(SQUARE_INCLUSION_NAME);
    }

    @FXML
    public void xSizeListener(){
        this.xBoardDimension = parseTextFieldToInt(this.xSizeView);
    }

    @FXML
    public void ySizeListener(){
        this.yBoardDimension = parseTextFieldToInt(this.ySizeView);
    }

    @FXML
    public void nucleonAmountListener(){
        this.nucleonsAmount = parseTextFieldToInt(this.nucleonsAmountView);
    }

    @FXML
    public void inclusionAmountListener(){
        this.inclusionsAmount = parseTextFieldToInt(this.inclusionsAmountView);
    }

    @FXML
    public void inclusionSizeListener(){
        this.inclusionSize = parseTextFieldToInt(this.inclusionSizeView);
    }

    @FXML
    public void inclusionTypeListener(){
        this.inclusionType = inclusionTypeView.getValue();
    }

    private int parseTextFieldToInt(TextField field){
        return field.getText().isEmpty() ? 0 : Integer.valueOf(field.getText());
    }

    // --------------------------------------------------------------

    @FXML
    public void addInclusions(){
        Inclusions inclusions = new Inclusions(canvas,
                board,
                xBoardDimension,
                yBoardDimension,
                inclusionType,
                inclusionsAmount,
                inclusionSize,
                clickedPrevSeedId);

        inclusions.draw();
    }

    @FXML
    public void importFile() throws IOException {
        FileImport fileImport = new FileImport(borderPane,
                board,
                canvas,
                xBoardDimension,
                yBoardDimension,
                colorFunctionality);

        fileImport.importFile();
    }

    @FXML
    public void generateBoard(){
        int simulationStep;

        if (board == null)
            board = new Board(xBoardDimension, yBoardDimension, canvas, colorFunctionality);

        if (simulationStepNumber.getText().isEmpty())
            simulationStep = -1;
        else
            simulationStep = parseTextFieldToInt(simulationStepNumber);

        GrainGrowth grainAlgorithm =
                new GrainGrowth(board,
                        nucleonsAmount,
                        simulationStep);

        grainAlgorithm.calculate();
    }

    @FXML
    public void exportFile(){
        FileExport fileExport = new FileExport(borderPane,
                canvas,
                board,
                xBoardDimension,
                yBoardDimension);

        fileExport.export();
    }

    @FXML
    public void clearBoard(){
        board = new Board(xBoardDimension, yBoardDimension, canvas, colorFunctionality);
        clickedSeeds.clear();
        clickedPrevSeedId = -1;

        board.redraw();
    }

    @FXML
    public void drawBoundary(){
        Boundary boundary = new Boundary(canvas, board, xBoardDimension, yBoardDimension);
        boundary.draw();
    }

    @FXML
    public void clearSeed(){
        board.clearSeeds();
        board.redraw();
    }

    @FXML
    public void mouseClicked(MouseEvent event) {
        final int boardPosX = (int) (Math.ceil(event.getX() / 5) - 1);
        final int boardPosY = (int) (Math.ceil(event.getY() / 5) - 1);
        final int canvasClickedColorXDimension = (int) clickedColor.getWidth();
        final int canvasClickedColorYDimension = (int) clickedColor.getHeight();
        final int tmpClickedValue = board.getBoard()[boardPosY][boardPosX].getId();

        //dodawanie do listy
        if (clickedSeeds.contains(tmpClickedValue)){
            clickedSeeds.remove((Integer) tmpClickedValue);
        }
        else{
            if (tmpClickedValue > 0)
                clickedSeeds.add(tmpClickedValue);
        }

        // aktualizacja kliknietego canvasu
        if (tmpClickedValue == clickedPrevSeedId) {
            clickedPrevSeedId = -1;
            clickedColor.getGraphicsContext2D().setFill(Color.WHITE);
        } else {
            clickedPrevSeedId = tmpClickedValue;
            final Color canvasClickedColor = colorFunctionality.convertAwtColorToFxColor(board.getBoard()[boardPosY][boardPosX].getColor());
            clickedColor.getGraphicsContext2D().setFill(canvasClickedColor);
        }

        clickedColor.getGraphicsContext2D().fillRect(0, 0, canvasClickedColorXDimension,
                canvasClickedColorYDimension);
    }

    @FXML
    public void clearSeedWithoutPhase(){
        board.setClickedSeeds(clickedSeeds);
        board.clearSeedWithoutPhase();
        board.redraw();
    }

    @FXML
    public void clearSeedWithoutSubstructure(){
        board.setClickedSeeds(clickedSeeds);
        board.clearSeedWithoutSubstructure();
        board.redraw();
    }

}
