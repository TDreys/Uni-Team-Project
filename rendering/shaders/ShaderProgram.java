package rendering.shaders;

import java.util.ArrayList;

import static org.lwjgl.opengl.GL20.*;

public class ShaderProgram {

    private int programID;

    public ShaderProgram(ArrayList<Shader> shaders)
    {
        this.programID = glCreateProgram();
        for (Shader shader:shaders)
        {
            glAttachShader(this.programID, shader.getShaderID());
        }
        glLinkProgram(this.programID);
        glValidateProgram(this.programID);
    }

    public int getProgramID() {
        return programID;
    }

    public void setProgramID(int programID) {
        this.programID = programID;
    }
}
