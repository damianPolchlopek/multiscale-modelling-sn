package controller;


import board.Board;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;


public class Controller {

    private Board board;

    @FXML
    private RadioButton halfHundred;

    @FXML
    private RadioButton oneHundred;

    @FXML
    private RadioButton oneAndHalfHundred;

    @FXML
    private Canvas canvas;



    @FXML
    public void updateImportField(){
        System.out.println("Importuje Plik");
    }

    @FXML
    public void updateExportField(){
        System.out.println("Exportuje plik");
    }

    @FXML
    public void drawBoard(){
        System.out.println(" -- Draw Board -- ");

//        final int boardDimension = getBoardDimension();
        final int boardDimension = 9;
        board = new Board(boardDimension, boardDimension, canvas);
        board.initBoard();
        board.fillPixel(2,3, Color.GREEN);
        board.fillPixel(7,7, Color.RED);

        board.getBoard()[3][2].setId(1);
        board.getBoard()[7][7].setId(2);
    }

    private int getBoardDimension(){
        if (halfHundred.isSelected()){
            return 50;
        }
        else if(oneHundred.isSelected()){
            return 100;
        }
        else{
            return 150;
        }
    }

    private void drawBlackRectangle(){

    }

}
