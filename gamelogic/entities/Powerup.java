package gamelogic.entities;

import java.io.Serializable;

public class Powerup implements Serializable{

    public enum Type {FIRE_RATE, AMMO, SPEEDUP, SHOT_SPEEDUP}

    private Type type;
    private float xPosition = 0, yPosition = 0, width = 15, height = 15;

    public Type getType() {
        return type;
    }

    public Powerup(Type type, float xPosition, float yPosition) {
		super();
		this.type = type;
		this.xPosition = xPosition;
		this.yPosition = yPosition;
	}

	public void setType(Type type) {
        this.type = type;
    }

    public float getxPosition() {
        return xPosition;
    }

    public void setxPosition(float xPosition) {
        this.xPosition = xPosition;
    }

    public float getyPosition() {
        return yPosition;
    }

    public void setyPosition(float yPosition) {
        this.yPosition = yPosition;
    }

    public float getWidth() {
        return width;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }
}
