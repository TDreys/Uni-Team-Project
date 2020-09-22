package rendering;

import rendering.shaders.Shader;
import rendering.shaders.ShaderProgram;
import rendering.util.fontUtil.Text;
import rendering.util.VAO;
import rendering.util.VAOLoader;

import java.awt.*;
import java.util.ArrayList;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL20.GL_FRAGMENT_SHADER;
import static org.lwjgl.opengl.GL20.GL_VERTEX_SHADER;
import static org.lwjgl.opengl.GL30.glBindVertexArray;

public class BasicRenderer {

    private static int quadShaderProgramID;
    private static int textShaderProgramID;
    private static VAO quadVAO ;
    private static VAO borderVAO;
    private static final float[] quadVertices = {0.0f,0.0f,0f,0.0f,-1.0f,0f,1.0f,-1.0f,0f,1.0f,0.0f,0f};
    private static final float[] borderVertices = {0.0f,0.0f,0f,0.0f,-1.0f,0f,1.0f,-1.0f,0f,1.0f,0.0f,0f,0.0f,0.0f,0f,1.0f,0.0f,0f,0.0f,-1.0f,0f,1.0f,-1.0f,0f};
    private static final int[] quadIndices = {0,2,1,0,3,2};

    /**
     * create shaders for each of the required shaders and add them to a shader program
     * create the vaos for the quads and store the id's
     */
    public static void init()
    {
        //create shaders for a quad and add them to a program
        ArrayList<Shader> shaders = new ArrayList<>();
        Shader fragment = new Shader(GL_FRAGMENT_SHADER, "src/Rendering/Shaders/FragmentShader.glsl");
        Shader vertex = new Shader(GL_VERTEX_SHADER,"src/Rendering/Shaders/VertexShader.glsl");
        shaders.add(vertex);
        shaders.add(fragment);
        quadShaderProgramID = new ShaderProgram(shaders).getProgramID();

        ArrayList<Shader> shaders2 = new ArrayList<>();
        Shader fragment2 = new Shader(GL_FRAGMENT_SHADER, "src/Rendering/Shaders/TextFragmentShader.glsl");
        Shader vertex2 = new Shader(GL_VERTEX_SHADER,"src/Rendering/Shaders/TextVertexShader.glsl");
        shaders2.add(vertex2);
        shaders2.add(fragment2);
        textShaderProgramID = new ShaderProgram(shaders2).getProgramID();

        //create vaos for a quad and hollow quad
        quadVAO = VAOLoader.createVAO(quadVertices,quadIndices);
        borderVAO = VAOLoader.createVAO(borderVertices,quadIndices);
    }

    /**
     * delete vaos and shaders
     */
    public static void destroy()
    {
        quadVAO.destroy();
        borderVAO.destroy();
        glDeleteProgram(quadShaderProgramID);
    }

    /**
     * renders a line starting at x1,y1 and ending at x2,y2
     * @param x1 x coordinate of start point
     * @param y1 y coordinate of start point
     * @param x2 x coordinate of end point
     * @param y2 y coordinate of end point
     * @param width thickness of the line
     */
    public static void renderLine(float x1, float y1, float x2, float y2, float width)
    {
        glLineWidth(width);
        glEnable(GL_LINE_SMOOTH);
        glBegin(GL_LINES);
        glVertex2f((x1 / 1280 * 2) - 1, ( y1 / 720 * -2) + 1);
        glVertex2f( (x2 / 1280 * 2) - 1, (y2 / 720 * -2) + 1);
        glDisable(GL_LINE_SMOOTH);
        glEnd();
    }


    /**
     * render a quad with the given dimensions
     * with the top left corner being x,y
     * @param x x coordinate of top left corner
     * @param y y coordinate of top left corner
     * @param width width of the quad in pixels
     * @param height height of the quad in pixels
     * @param color color of the quad
     */
    public static void renderQuad(float x, float y, float width, float height, Color color)
    {
        glUseProgram(quadShaderProgramID);

        //bind attributes to uniform variables
        glUniform2f(glGetUniformLocation(quadShaderProgramID,"size"),width,height);
        glUniform2f(glGetUniformLocation(quadShaderProgramID,"screenPosition"),x,y);
        glUniform4f(glGetUniformLocation(quadShaderProgramID,"color"),color.getRed(),
                                                                            color.getGreen(),
                                                                            color.getBlue(),
                                                                            color.getAlpha());

        //bind attributes
        glBindVertexArray(quadVAO.getId());
        glEnableVertexAttribArray(0);

        //draw
        glDrawElements(GL_TRIANGLES,6,GL_UNSIGNED_INT,0);

        //unbind attributes
        glDisableVertexAttribArray(0);
        glBindVertexArray(0);
        glUseProgram(0);
    }

    /**
     * render a hollow quad with the given dimensions
     * with the top left corner being x,y
     * @param x x coordinate of top left corner
     * @param y y coordinate of top left corner
     * @param width width of the quad in pixels
     * @param height height of the quad in pixels
     * @param color color of the quad
     */
    public static void renderHollowQuad(float x, float y, float width, float height, Color color, float thickness)
    {
        glUseProgram(quadShaderProgramID);
        glEnable(GL_LINE_SMOOTH);
        //bind attributes to uniform variables
        glUniform2f(glGetUniformLocation(quadShaderProgramID,"size"),width,height);
        glUniform2f(glGetUniformLocation(quadShaderProgramID,"screenPosition"),x,y);
        glUniform4f(glGetUniformLocation(quadShaderProgramID,"color"),color.getRed(),
                                                                            color.getGreen(),
                                                                            color.getBlue(),
                                                                            color.getAlpha());
        //bind attributes
        glLineWidth(thickness);
        glBindVertexArray(borderVAO.getId());
        glEnableVertexAttribArray(0);

        //draw
        glDrawArrays(GL_LINES,0,GL_UNSIGNED_INT);

        //unbind attributes
        glDisable(GL_LINE_SMOOTH);
        glDisableVertexAttribArray(0);
        glBindVertexArray(0);
    }

    /**
     * render text with a given font with the
     * top left corner being x,y
     * @param x x coordinate of the top left corner
     * @param y y coordinate of the top left corner
     * @param text text object to render
     */
    public static void renderText(float x, float y, Text text)
    {
        y = y + (text.getFont().getMaxCharacterPixelAscent()/2);

        if(text.getString().length() != 0)
        {
            //enable textures and alpha channel
            glEnable(GL_TEXTURE_2D);
            glEnable(GL_BLEND);
            glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
            glUseProgram(textShaderProgramID);

            for(VAO vao:text.getCharacterGlyphs())
            {
                //bind attributes
                glBindVertexArray(vao.getId());
                glEnableVertexAttribArray(0);
                glEnableVertexAttribArray(2);

                //bind uniform variables and texture
                glUniform3f(glGetUniformLocation(textShaderProgramID,"color"),text.getFont().getColor().getRed(),
                                                                                    text.getFont().getColor().getGreen(),
                                                                                    text.getFont().getColor().getBlue());
                glUniform2f(glGetUniformLocation(textShaderProgramID,"screenPosition"),x*2,y*2);

                glBindTexture(GL_TEXTURE_2D,text.getFont().getFontTextureID());
                glActiveTexture(text.getFont().getFontTextureID());

                //draw
                glDrawElements(GL_QUADS,4,GL_UNSIGNED_INT,0);

                //disable attributes
                glDisableVertexAttribArray(0);
                glBindVertexArray(0);
                glBindTexture(GL_TEXTURE_2D,0);

            }
            glUseProgram(0);
        }
    }
}
