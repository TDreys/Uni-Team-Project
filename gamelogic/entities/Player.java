package gamelogic.entities;

import java.io.Serializable;

public class Player implements Serializable{

    private long lastFireTime = System.currentTimeMillis();
    private long lastHitTime = System.currentTimeMillis();
    private int playerID = -1;
    private String playerName;
    private int HP = 3;
    private int maxHP = 3;
    private int ammoCount = 20;
    private float speed = 1f;
    private float projectileSpeed = 5f;
    private float timeBetweenShots = 1000;
    private float cannonAngle = 0;
    private float xPosition = 0, yPosition = 0;
    private int width = 30, height = 30;

    public int getPlayerID() {
        return playerID;
    }

    public void setPlayerID(int playerID) {
        this.playerID = playerID;
    }

    public int getAmmoCount() {
        return ammoCount;
    }

    public void setAmmoCount(int ammoCount) {
        this.ammoCount = ammoCount;
    }
    
    public float getPlayerSpeed() {
        return speed;
    }

    public void setPlayerSpeed(float speed) {
        this.speed = speed;
    }
    
    public float getProjectileSpeed() {
        return projectileSpeed;
    }

    public void setProjectileSpeed(float projectileSpeed) {
        this.projectileSpeed = projectileSpeed;
    }
    
    public int getHP() {
        return HP;
    }

    public void setHP(int HP) {
        this.HP = HP;
    }

    public float getCannonAngle() {
        return cannonAngle;
    }

    public void setCannonAngle(float cannonAngle) {
        this.cannonAngle = cannonAngle;
    }

    public float getxPosition() {
        return xPosition;
    }

    public boolean isDead() {
    	return HP<=0;
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

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public int getMaxHP() {
        return maxHP;
    }

    public void setMaxHP(int maxHP) {
        this.maxHP = maxHP;
    }

    public long getLastFireTime() {
        return lastFireTime;
    }

    public void setLastFireTime(long lastFireTime) {
        this.lastFireTime = lastFireTime;
    }

    public long getLastHitTime() {
        return lastHitTime;
    }

    public void setLastHitTime(long lastHitTime) {
        this.lastHitTime = lastHitTime;
    }

    public float getTimeBetweenShots() {
        return timeBetweenShots;
    }

    public void setTimeBetweenShots(float timeBetweenShots) {
        this.timeBetweenShots = timeBetweenShots;
    }
}
