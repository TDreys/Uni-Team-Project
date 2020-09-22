package rendering.util.fontUtil;

import rendering.util.VAO;
import rendering.util.VAOLoader;
import org.lwjgl.BufferUtils;
import org.lwjgl.stb.STBTTAlignedQuad;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import static org.lwjgl.stb.STBTruetype.stbtt_GetBakedQuad;

public class Text {

    private Font font;
    private String string;
    private float textWidth;
    private float textHeight;
    private ArrayList<VAO> characterGlyphs = new ArrayList<>();

    /**
     * create a text object with a given string and font
     * also records information about the size and shape of the text and text characters
     * this constructor creates a list of vaos for each character in the string
     * @param string the string to create text for
     * @param font the font to render the text with
     */
    public Text(String string, Font font)
    {
        this.font = font;
        this.string = string;
        this.textHeight = font.getMaxCharacterPixelAscent();
        char[] characters = string.toCharArray();
        FloatBuffer xbuffer = BufferUtils.createFloatBuffer(1);
        FloatBuffer ybuffer = BufferUtils.createFloatBuffer(1);
        for(int i = 0; i < characters.length; i++) {
            STBTTAlignedQuad charQuad = STBTTAlignedQuad.create();
            stbtt_GetBakedQuad(font.getCharData(), font.getBITMAP_WIDTH(), font.getBITMAP_HEIGHT(), characters[i] - 32, xbuffer, ybuffer, charQuad, true);

            float[] quadVertices1 = {
                    (charQuad.x0()),(charQuad.y0()),0f,
                    (charQuad.x1()),(charQuad.y0()),0f,
                    (charQuad.x1()),(charQuad.y1()),0f,
                    (charQuad.x0()),(charQuad.y1()),0f};

            int[] quadIndices1 = {0,1,2,3};

            float[] textureCoords = {
                    charQuad.s0(),charQuad.t0(),
                    charQuad.s1(),charQuad.t0(),
                    charQuad.s1(),charQuad.t1(),
                    charQuad.s0(),charQuad.t1()};

            VAO vao = VAOLoader.createVAO(quadVertices1,textureCoords,quadIndices1);
            characterGlyphs.add(vao);

            if(i == characters.length-1)
            {
                textWidth = charQuad.x1();
            }
        }

    }

    public void destroy()
    {
        for(VAO vao:characterGlyphs)
        {
            vao.destroy();
        }
    }

    public Font getFont() {
        return font;
    }

    public String getString() {
        return string;
    }

    public void setString(String string) {
        this.string = string;
    }

    public float getTextWidth() {
        return textWidth;
    }


    public float getTextHeight() {
        return textHeight;
    }


    public ArrayList<VAO> getCharacterGlyphs() {
        return characterGlyphs;
    }

}
