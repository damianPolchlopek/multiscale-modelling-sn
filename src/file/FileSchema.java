package file;

import java.util.List;

public class FileMapping {

    private int xSize;
    private int ySize;
    private List<Row> rowList;

    public FileMapping(int xSize, int ySize, List<Row> rowList) {
        this.xSize = xSize;
        this.ySize = ySize;
        this.rowList = rowList;
    }
}
