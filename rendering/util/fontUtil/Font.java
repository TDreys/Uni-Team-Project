package rendering.util.fontUtil;

import rendering.util.IOUtil;
import org.lwjgl.BufferUtils;
import org.lwjgl.stb.STBTTAlignedQuad;
import org.lwjgl.stb.STBTTBakedChar;
import org.lwjgl.stb.STBTTFontinfo;
import java.awt.*;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.stb.STBTruetype.*;

public class Font {

    private final int BITMAP_HEIGHT = 1024, BITMAP_WIDTH = 1024;
    private Color color = new Color(255,255,255);
    private ByteBuffer bitmap = BufferUtils.createByteBuffer(BITMAP_HEIGHT*BITMAP_WIDTH);
    private ByteBuffer characters = BufferUtils.createByteBuffer(2048);
    private STBTTBakedChar.Buffer charData = new STBTTBakedChar.Buffer(characters);
    private STBTTFontinfo fontinfo = STBTTFontinfo.create();
    private int fontTextureID;
    private float maxCharacterPixelAscent = 0;

    /**
     * this constructor creates a font object containing information about a font
     * it creates a bitmap and texture containing each character of that font
     * @param source location of the ttf file
     * @param fontHeight height for the font
     */
    public Font(String source, int fontHeight)
    {
        //create font bitmap
        ByteBuffer loadedFontData;
        loadedFontData = IOUtil.loadFileToByteBuffer(source);
        stbtt_InitFont(fontinfo,loadedFontData);
        stbtt_BakeFontBitmap(loadedFontData,fontHeight,bitmap,BITMAP_WIDTH,BITMAP_HEIGHT,32,charData);

        //create opengl texture for font
        fontTextureID = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, fontTextureID);
        glTexImage2D(GL_TEXTURE_2D, 0, GL_ALPHA, BITMAP_WIDTH, BITMAP_HEIGHT, 0, GL_ALPHA, GL_UNSIGNED_BYTE, bitmap);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        glBindTexture(GL_TEXTURE_2D,0);

        //get font size information
        FloatBuffer xbuffer = BufferUtils.createFloatBuffer(1);
        FloatBuffer ybuffer = BufferUtils.createFloatBuffer(1);
        for(int i = 32; i < 126;i++)
        {
            STBTTAlignedQuad charQuad = STBTTAlignedQuad.create();
            stbtt_GetBakedQuad(charData,BITMAP_WIDTH,BITMAP_HEIGHT,i-32,xbuffer,ybuffer,charQuad,true);

            if(-charQuad.y0()> maxCharacterPixelAscent)
            {
                maxCharacterPixelAscent = -charQuad.y0();
            }
        }
    }

    public int getBITMAP_HEIGHT() {
        return BITMAP_HEIGHT;
    }

    public int getBITMAP_WIDTH() {
        return BITMAP_WIDTH;
    }

    public ByteBuffer getBitmap() {
        return bitmap;
    }

    public void setBitmap(ByteBuffer bitmap) {
        this.bitmap = bitmap;
    }

    public ByteBuffer getCharacters() {
        return characters;
    }

    public void setCharacters(ByteBuffer characters) {
        this.characters = characters;
    }

    public STBTTBakedChar.Buffer getCharData() {
        return charData;
    }

    public void setCharData(STBTTBakedChar.Buffer charData) {
        this.charData = charData;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public int getFontTextureID() {
        return fontTextureID;
    }

    public void setFontTextureID(int fontTextureID) {
        this.fontTextureID = fontTextureID;
    }

    public STBTTFontinfo getFontinfo() {
        return fontinfo;
    }

    public void setFontinfo(STBTTFontinfo fontinfo) {
        this.fontinfo = fontinfo;
    }

    public float getMaxCharacterPixelAscent() {
        return maxCharacterPixelAscent;
    }

    public void setMaxCharacterPixelAscent(float maxCharacterPixelAscent) {
        this.maxCharacterPixelAscent = maxCharacterPixelAscent;
    }
}