package gamelogic.entities;

import java.io.Serializable;

public class BlastAnimation implements Serializable {

    float centerX,centerY;
    int maxSize = 30;
    float progress = 0;
    float frameCount = 50;

    public BlastAnimation(float centerX, float centerY) {
        this.centerX = centerX;
        this.centerY = centerY;
    }

    /**
     * give the height of a sine wave with the given progress
     * @return
     */
    public float getProgressFactor()
    {
        return (float)Math.sin(progress*Math.PI);
    }

    /**
     * steps the progress by one frame
     */
    public void step()
    {
        float stepAmount = 1/frameCount;
        this.progress += stepAmount;
    }

    public float getCenterX() {
        return centerX;
    }

    public void setCenterX(float centerX) {
        this.centerX = centerX;
    }

    public float getCenterY() {
        return centerY;
    }

    public void setCenterY(float centerY) {
        this.centerY = centerY;
    }

    public int getMaxSize() {
        return maxSize;
    }

    public void setMaxSize(int maxSize) {
        this.maxSize = maxSize;
    }

    public float getProgress() {
        return progress;
    }
}
