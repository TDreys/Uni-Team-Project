package interfaceui.elements;

import interfaceui.GUI;
import rendering.util.fontUtil.Font;

public class Label extends Node {

    private String text;
    private Font font = GUI.font;

    public Label (String text, int x, int y)
    {
        super(x,y);
        this.text = text;
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
}
