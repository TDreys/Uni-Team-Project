package input;

import java.io.Serializable;

public class InputState implements Serializable {

    public int playerID;
    public boolean wPressed = false, sPressed = false, aPressed = false, dPressed = false, mLeftPressed = false;
    public double mousex = 0,mousey = 0;

}
