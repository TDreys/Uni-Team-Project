package interfaceui.elements;

import interfaceui.GUI;
import rendering.util.fontUtil.Font;

import java.awt.*;

public class Button extends Node {

    private String text = "";
    private int width,height;
    private Color color = new Color(255,255,255);
    private Font font = GUI.font;
    private boolean selected = false;
    public Action onClick;

    public Button (int width, int height, int x, int y ,Action onClick)
    {
        super(x,y);
        this.width = width;
        this.height = height;
        this.onClick = onClick;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Font getFont() {
        return font;
    }

    public void setFont(Font font) {
        this.font = font;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
