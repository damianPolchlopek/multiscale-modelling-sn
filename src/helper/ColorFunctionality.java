package helper;

import java.awt.*;
import java.util.HashMap;
import java.util.Random;

public class ColorFunctionality {

    private HashMap<Integer, Color> usedColor = new HashMap<>();

    public ColorFunctionality() {
        usedColor.put(0, Color.WHITE);
        usedColor.put(100, Color.BLACK);
    }

    private java.awt.Color getRandomColor(){
        Random rand = new Random();
        float r = rand.nextFloat();
        float g = rand.nextFloat();
        float b = rand.nextFloat();
        return new java.awt.Color(r, g, b);
    }

    protected java.awt.Color getMatchedColorToId(int fieldId){
        final boolean isIdExist = usedColor.containsKey(fieldId);
        java.awt.Color color;
        if (isIdExist){
            color = usedColor.get(fieldId);
        }
        else {
            color = getRandomColor();
            usedColor.put(fieldId, color);
        }
        return color;
    }

    protected javafx.scene.paint.Color convertAwtColorToFxColor(java.awt.Color awtColor){
        int r = awtColor.getRed();
        int g = awtColor.getGreen();
        int b = awtColor.getBlue();
        int a = awtColor.getAlpha();
        double opacity = a / 255.0;
        return javafx.scene.paint.Color.rgb(r, g, b, opacity);
    }

    protected Color getColor(int id){
        return usedColor.get(id);
    }

}
