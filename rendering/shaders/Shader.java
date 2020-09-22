package rendering.shaders;

import static rendering.util.IOUtil.loadTextFileToString;
import static org.lwjgl.opengl.GL20.*;

public class Shader {

    private int shaderID;

    /**
     * compiles the given shader and stores the id
     * @param shaderType type of shader, see opengl docs for this
     * @param source  location of shader file
     */
    public Shader (int shaderType, String source)
    {
        shaderID = glCreateShader(shaderType);
        glShaderSource(shaderID, loadTextFileToString(source));
        glCompileShader(shaderID);

        if (glGetShaderi(shaderID, GL_COMPILE_STATUS) == GL_FALSE) {
            System.out.println(glGetShaderInfoLog(shaderID, 500));
            System.err.println("Could not compile shader");
            System.exit(-1);
        }
    }


    public int getShaderID() {
        return shaderID;
    }

    public void setShaderID(int shaderID) {
        this.shaderID = shaderID;
    }
}
