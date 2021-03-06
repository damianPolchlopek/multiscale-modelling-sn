package code.file.operation;

import code.board.Board;
import javafx.scene.canvas.Canvas;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

public abstract class FileOperation {

    private BorderPane borderPane;
    protected Board board;
    Canvas canvas;
    int xBoardDimension;
    int yBoardDimension;

    final String TXT_EXTENSION = "txt";
    final String BMP_EXTENSION = "bmp";

    FileOperation(BorderPane borderPane, Board board, Canvas canvas, int xBoardDimension, int yBoardDimension) {
        this.borderPane = borderPane;
        this.board = board;
        this.canvas = canvas;
        this.xBoardDimension = xBoardDimension;
        this.yBoardDimension = yBoardDimension;
    }

    enum FileOperationType{
        Import,
        Export
    }

    File selectFile(FileOperationType fileOperationType){
        Stage stage = (Stage) borderPane.getScene().getWindow();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save code.file");
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Text code.file", "*.txt"),
                new FileChooser.ExtensionFilter("BMP code.file", "*.bmp"));
        if (fileOperationType == FileOperationType.Export){
            return fileChooser.showSaveDialog(stage);
        }
        else{
            return fileChooser.showOpenDialog(stage);
        }
    }

}
