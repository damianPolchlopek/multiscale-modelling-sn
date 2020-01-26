package file.operation;

import board.Board;
import board.Field;
import file.FileSchema;
import helper.ColorFunctionality;
import javafx.scene.canvas.Canvas;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Objects;

public class FileImport extends FileOperation {

    private ColorFunctionality colorFunctionality;

    public FileImport(BorderPane borderPane,
                      Board board,
                      Canvas canvas,
                      int xBoardDimension,
                      int yBoardDimension,
                      ColorFunctionality colorFunctionality) {

        super(borderPane, board, canvas, xBoardDimension, yBoardDimension);
        this.colorFunctionality = colorFunctionality;
    }

    public void importFile() throws IOException {
        final File file = selectFile(FileOperationType.Import);
        final String fileName = file.getName();
        final String extension = fileName.substring(fileName.lastIndexOf("."));

        if (extension.contains(TXT_EXTENSION)){
            importTextFile(file);
        }
        else if (extension.contains(BMP_EXTENSION)){
            importBMPFile(file);
        }
    }

    private void importTextFile(File file) throws IOException {
        String filePath = file.getPath();
        FileSchema fileSchema = new FileSchema(filePath);

        final int xBoardDimension = fileSchema.getxSize();
        final int yBoardDimension = fileSchema.getySize();
        board = new Board(xBoardDimension, yBoardDimension, canvas, colorFunctionality);

        fileSchema.getPointList()
                .forEach(field -> {
                    final int xRealPosition = field.getxPosition();
                    final int yRealPosition = field.getyPosition();
                    final java.awt.Color awtColor = colorFunctionality.getMatchedColorToId(field.getId());
                    final Color fxColor = colorFunctionality.convertAwtColorToFxColor(awtColor);

                    board.fillPixel(xRealPosition, yRealPosition, fxColor);

                    field.setColor(awtColor);
                    board.getBoard()[yRealPosition][xRealPosition] = field;
                });
    }

    private void importBMPFile(File file) throws IOException {
        BufferedImage bufferedImage = ImageIO.read(file);

        final int xRealBoardDimension = bufferedImage.getWidth();
        final int yRealBoardDimension = bufferedImage.getHeight();
        final int xBoardDimension = xRealBoardDimension/5;
        final int yBoardDimension = yRealBoardDimension/5;

        board = new Board(xBoardDimension, yBoardDimension, canvas, colorFunctionality);

        int tempId = 0;

        for (int y = 0; y < yRealBoardDimension; y++) {
            for (int x = 0; x < xRealBoardDimension; x++) {
                final int rgb=bufferedImage.getRGB(x,y);
                final int red = (rgb >> 16) & 0x000000FF;
                final int green = (rgb >>8 ) & 0x000000FF;
                final int blue = (rgb) & 0x000000FF;
                final double redDouble=red/255.0;
                final double greenDouble=green/255.0;
                final double blueDouble=blue/255.0;
                final Color color = Color.color(redDouble,greenDouble,blueDouble);

                if (y%5 == 0 && x%5 == 0){
                    final int yPos = y/5;
                    final int xPos = x/5;

                    final java.awt.Color awtColor = new java.awt.Color(red, green, blue);

                    if (!colorFunctionality.getUsedColor().containsValue(awtColor)){
                        tempId++;
                        colorFunctionality.getUsedColor().put(tempId, awtColor);
                    }

                    board.getBoard()[yPos][xPos] = new Field(xPos, yPos, 0,
                            getKeyByValue(colorFunctionality.getUsedColor(), awtColor),
                            awtColor);
                }

                board.getCanvas().getGraphicsContext2D().setFill(color);
                board.getCanvas().getGraphicsContext2D().fillRect(x,y,1,1);
            }
        }

    }

    private <T, E> T getKeyByValue(Map<T, E> map, E value) {
        for (Map.Entry<T, E> entry : map.entrySet()) {
            if (Objects.equals(value, entry.getValue())) {
                return entry.getKey();
            }
        }
        return null;
    }

}
