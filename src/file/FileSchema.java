package file;

import board.Field;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class FileSchema {

    private final String LINE_REGEX = "\\d+ \\d+ \\d+ \\d+";
    private final Pattern PATTERN = Pattern.compile(LINE_REGEX);

    private int xSize;
    private int ySize;
    private List<Field> pointList = new ArrayList<>();
    private String pathToFile;

    public FileSchema(String pathToFile) throws IOException {
        this.pathToFile = pathToFile;
        parseHeaderOfFile();
        addDataToPointList();
    }

    private void parseHeaderOfFile() throws IOException {
        final String firstLine = getFirstLineFile();
        final String[] numbers = firstLine.split(" ");
        this.xSize = Integer.parseInt(numbers[0]);
        this.ySize = Integer.parseInt(numbers[1]);
    }

    private String getFirstLineFile() throws IOException {
        BufferedReader Buff = new BufferedReader(new FileReader(this.pathToFile));
        return Buff.readLine();
    }

    private void addDataToPointList() throws IOException {
        Stream<String> stream = Files.lines(Paths.get(this.pathToFile));
        stream.forEach(line -> {
            Matcher m = PATTERN.matcher(line);
            if (m.find()){
                final String[] numbers = line.split(" ");
                final int xPos  = Integer.parseInt(numbers[0]);
                final int yPos  = Integer.parseInt(numbers[1]);
                final int phase = Integer.parseInt(numbers[2]);
                final int id    = Integer.parseInt(numbers[3]);
                pointList.add(new Field(xPos, yPos, phase, id));
            }
        });
    }

    public int getxSize() {
        return xSize;
    }

    public int getySize() {
        return ySize;
    }

    public List<Field> getPointList() {
        return pointList;
    }
}
