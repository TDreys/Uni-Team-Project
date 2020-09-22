package rendering.util;

import java.util.ArrayList;

import static org.lwjgl.opengl.GL15.glDeleteBuffers;
import static org.lwjgl.opengl.GL30.glDeleteVertexArrays;

public class VAO {

    int id;
    ArrayList<Integer> vboIDs = new ArrayList<>();

    public VAO(int id){
        this.id = id;
    }

    public int getId() {
        return id;
    }


    public void addVBO(int id)
    {
        vboIDs.add(id);
    }

    public void destroy()
    {
        for(Integer id:vboIDs)
        {
            glDeleteBuffers(id);
        }
        glDeleteVertexArrays(id);
    }
}
