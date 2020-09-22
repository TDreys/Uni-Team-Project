package gamelogic.entities;

import input.InputState;

import java.util.Random;

public class AITank extends Player {

    private InputState inputState = new InputState();

    public InputState getInputState() {
        return inputState;
    }

    public void setInputState(InputState inputState) {
        this.inputState = inputState;
    }

    /**
     * creates a random input state for an ai
     * @return the random input state
     */
    public static InputState createAiInput()
    {
        InputState inputState = new InputState();
        Random random = new Random();
        if(random.nextFloat() > 0.3f)
        {
            inputState.aPressed = true;
        }
        if(random.nextFloat() > 0.3f)
        {
            inputState.wPressed = true;
        }
        if(random.nextFloat() > 0.3f)
        {
            inputState.sPressed = true;
        }
        if(random.nextFloat() > 0.3f)
        {
            inputState.dPressed = true;
        }
        if(random.nextFloat() > 0.8)
        {
            inputState.mLeftPressed = true;
        }
        return inputState;
    }
}
