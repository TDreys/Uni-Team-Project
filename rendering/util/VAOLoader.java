package rendering.util;

import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

public class VAOLoader {

    /**
     * create a opengl vao with the given data
     * @param vertices vertex data for the mesh
     * @param indices index data for the mesh
     * @return the vao object containing id for vao and vbo's
     */
    public static VAO createVAO(float[] vertices, int[] indices)
    {
        //create a vao and bind it
        int vaoID;
        vaoID = glGenVertexArrays();
        glBindVertexArray(vaoID);
        VAO vao = new VAO(vaoID);

        //create opengl buffers and add data to them
        int vboID = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vboID);
        glBufferData(GL_ARRAY_BUFFER, vertices, GL_STATIC_DRAW);
        glVertexAttribPointer(0, 3, GL_FLOAT, false,0,0);
        vao.addVBO(vboID);

        vboID = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, vboID);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, indices, GL_STATIC_DRAW);
        glVertexAttribPointer(1, 1, GL_INT, false,0,0);
        vao.addVBO(vboID);

        //unbind vao and vbo and return vao id
        glBindVertexArray(0);
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        return vao;
    }

    /**
     * create a opengl vao with the given data
     * @param vertices the vertex data for the mesh
     * @param textureCoords the texture coordinates for each vertex
     * @param indices the index data for the vertices
     * @return the vao object containing the vao id and vbo id's
     */
    public static VAO createVAO(float[] vertices,float[] textureCoords, int[] indices)
    {
        int vaoID;
        vaoID = glGenVertexArrays();
        glBindVertexArray(vaoID);
        VAO vao = new VAO(vaoID);

        int vboID = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vboID);
        glBufferData(GL_ARRAY_BUFFER, vertices, GL_STATIC_DRAW);
        glVertexAttribPointer(0, 3, GL_FLOAT, false,0,0);
        vao.addVBO(vboID);

        vboID = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, vboID);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, indices, GL_STATIC_DRAW);
        glVertexAttribPointer(1, 1, GL_INT, false,0,0);
        vao.addVBO(vboID);

        vboID = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vboID);
        glBufferData(GL_ARRAY_BUFFER, textureCoords, GL_STATIC_DRAW);
        glVertexAttribPointer(2, 2, GL_FLOAT, false,0,0);
        vao.addVBO(vboID);

        glBindVertexArray(0);
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        return vao;
    }

}
