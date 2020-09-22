package input;

import program.Main;

import static org.lwjgl.glfw.GLFW.*;
import static program.Main.State.*;

public class InputHandler {

    public InputState state;

    public InputHandler()
    {
        this.state = new InputState();
    }

    /**
     * this method is called to handle keyboard inputs,
     * called with glfw callbacks
     * @param key the key that was pressed
     * @param action the action for the given key, check glfw docs for values
     */
    public void handleKeyBoardInput(int key, int action)
    {
        if(Main.state == PLAYING)
        {
            if(key == GLFW_KEY_W && action == GLFW_PRESS)
            {
                state.wPressed = true;
            }
            if(key == GLFW_KEY_A && action == GLFW_PRESS)
            {
                state.aPressed = true;
            }
            if(key == GLFW_KEY_S && action == GLFW_PRESS)
            {
                state.sPressed = true;
            }
            if(key == GLFW_KEY_D && action == GLFW_PRESS)
            {
                state.dPressed = true;
            }
            
            if(key == GLFW_KEY_W && action == GLFW_RELEASE)
            {
                state.wPressed = false;
            }
            if(key == GLFW_KEY_A && action == GLFW_RELEASE)
            {
                state.aPressed = false;
            }
            if(key == GLFW_KEY_S && action == GLFW_RELEASE)
            {
                state.sPressed = false;

            }
            if(key == GLFW_KEY_D && action == GLFW_RELEASE)
            {
                state.dPressed = false;
            }
        }

        if(Main.state == JOIN_GAME)
        {
            if(key == GLFW_KEY_BACKSPACE && action == GLFW_PRESS)
            {
                Main.gui.handleTextInput(-1);
            }
        }

        if(key == GLFW_KEY_ESCAPE && action == GLFW_PRESS)
        {
            if(Main.state == PLAYING)
            {
                Main.state = Main.State.PAUSED;
            }
        }
        
    }

    /**
     * this method is called to handle mouse button inputs,
     * called with glfw callbacks
     * @param button the button that was pressed
     * @param action the action for the given key, check glfw docs for values
     */
    public void handleMouseButtonInput(int button, int action)
    {
        if(button == GLFW_MOUSE_BUTTON_LEFT && action == GLFW_PRESS)
        {
            if(Main.state == PLAYING)
            {
                state.mLeftPressed = true;       
            }
            else
            {
                Main.gui.handleMouseClickInput(state.mousex,state.mousey);
            }
        }
        if(button == GLFW_MOUSE_BUTTON_LEFT && action == GLFW_RELEASE)
        {
            if(Main.state == PLAYING)
            {
                state.mLeftPressed = false;
            }
        }
    }

    /**
     * this method is called whenever the mouse is moved
     * called with glfw callbacks
     * @param x x coordinate of the mouse in pixels
     * @param y y coordinate of the mouse in pixels
     */
    public void handleMouseMovementInput(double x, double y)
    {
        state.mousex = x;
        state.mousey = y;
    }

    /**
     * this method is called when any keyboard input is made
     * it will send the input to the gui to handle
     * @param codePoint the key that was pressed as a unicode character
     */
    public void handleTextInput(int codePoint)
    {
        if (Main.state == Main.State.JOIN_GAME)
        {
            Main.gui.handleTextInput(codePoint);
        }
    }

}
