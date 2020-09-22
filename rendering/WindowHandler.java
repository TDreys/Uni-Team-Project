package rendering;

import input.InputHandler;
import org.lwjgl.glfw.GLFWCharCallbackI;
import org.lwjgl.glfw.GLFWCursorPosCallback;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWMouseButtonCallback;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.GLFW.glfwTerminate;
import static org.lwjgl.system.MemoryUtil.NULL;

public class WindowHandler {
    private long windowID;
    private final int WIDTH = 1280, HEIGHT = 720;

    public WindowHandler(final InputHandler inputHandler)
    {
        //setup window
        glfwInit();
        glfwWindowHint(GLFW_SAMPLES, 4);

        windowID = glfwCreateWindow(WIDTH,HEIGHT,"LastOfUs", NULL, NULL);

        if(windowID == NULL)
        {
            System.out.println("window creation failed");
        }

        glfwShowWindow(windowID);
        glfwMakeContextCurrent(windowID);

        //set callbacks for inputs
        glfwSetKeyCallback(windowID, new GLFWKeyCallback() {
            @Override
            public void invoke(long window, int key, int scancode, int action, int mods) {
                inputHandler.handleKeyBoardInput(key,action);
            }
        });

        glfwSetMouseButtonCallback(windowID, new GLFWMouseButtonCallback() {
            @Override
            public void invoke(long window, int button, int action, int mods) {
                inputHandler.handleMouseButtonInput(button, action);
            }
        });

        glfwSetCursorPosCallback(windowID, new GLFWCursorPosCallback() {
            @Override
            public void invoke(long window, double xpos, double ypos) {
                inputHandler.handleMouseMovementInput(xpos,ypos);
            }
        });

        glfwSetCharCallback(windowID, new GLFWCharCallbackI() {
            @Override
            public void invoke(long window, int codepoint) {
                inputHandler.handleTextInput(codepoint);
            }
        });
    }

    /**
     * update the window
     */
    public void update()
    {
        glfwSwapInterval(0);
        glfwSwapBuffers(windowID);
    }

    /**
     * destroy the window
     */
    public void destroy()
    {
        glfwDestroyWindow(windowID);
        glfwTerminate();
    }

    public long getWindowID() {
        return windowID;
    }

    public int getWIDTH() {
        return WIDTH;
    }

    public int getHEIGHT() {
        return HEIGHT;
    }
}
