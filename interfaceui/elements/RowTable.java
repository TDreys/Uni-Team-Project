package interfaceui.elements;

import interfaceui.GUI;
import rendering.util.fontUtil.Font;

import java.util.ArrayList;

public class RowTable extends Node{

    private int maxRowCount;
    private ArrayList<String> rowTexts = new ArrayList<>();
    private int width,height;
    private Font font = GUI.font;

    public RowTable(int width, int height, int x, int y, int maxRowCount)
    {
        super(x,y);
        this.width = width;
        this.height = height;
        this.maxRowCount = maxRowCount;
    }

    /**
     * adds a row with a given string to the table
     * it checks if the table is full before adding the string
     * @param text
     */
    public void addRowLabel(String text)
    {
        if(rowTexts.size() < maxRowCount)
        {
            rowTexts.add(text);
        }
    }

    public ArrayList<String> getRowTexts() {
        return rowTexts;
    }

    public int getMaxRowCount() {
        return maxRowCount;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public Font getFont() {
        return font;
    }
}
