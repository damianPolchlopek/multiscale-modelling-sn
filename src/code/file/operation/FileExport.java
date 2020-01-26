package code.file.operation;

import code.board.Board;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.BorderPane;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

public class FileExport extends FileOperation {

    public FileExport(BorderPane borderPane, Canvas canvas, Board board, int xBoardDimension, int yBoardDimension) {
        super(borderPane, board, canvas, xBoardDimension, yBoardDimension);
    }

    public void export(){
        final File file = selectFile(FileOperationType.Export);
        final String fileName = file.getName();
        final String extension = fileName.substring(fileName.lastIndexOf("."));

        if (extension.contains(TXT_EXTENSION)){
            final String fileContent = createFileContent();
            saveTextToFile(fileContent, file);
        }
        else if (extension.contains(BMP_EXTENSION)){
            exportBitMap(file);
        }
    }

    private String createFileContent(){
        String boardContent = "";
        boardContent += fillFileHeader();
        boardContent += board.getBoardContent();
        return boardContent;
    }

    private String fillFileHeader(){
        return xBoardDimension + " " + yBoardDimension + System.lineSeparator();
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

    private void exportBitMap(File file) {
        final int SCALE = 5;
        final int xRealSize = xBoardDimension * SCALE;
        final int yRealSize = yBoardDimension * SCALE;

        WritableImage wim = new WritableImage(xRealSize, yRealSize);
        canvas.snapshot(null, wim);

        try {
            ImageIO.write(SwingFXUtils.fromFXImage(canvas.snapshot(null, wim), null), "png", file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
