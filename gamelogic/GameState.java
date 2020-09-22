package gamelogic;

import java.awt.*;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Random;

import audio.Buffer;
import audio.Source;
import database.DatabaseConnetor;
import gamelogic.entities.*;
import input.InputState;
import networking.ClientInfo;
import networking.Server;

public class GameState implements Serializable{

    private ArrayList<Player> players = new ArrayList<>();
    private ArrayList<AITank> aiTanks = new ArrayList<>();
    private ArrayList<Projectile> liveProjectiles = new ArrayList<>();
    private ArrayList<Powerup> availablePowerups = new ArrayList<>();
    private ArrayList<BlastAnimation> blastAnimations = new ArrayList<>();
    private Map map = new Map();
    private boolean finished = false;
    private String winMessage = "";
    private boolean loginState = false;
    private int gameCountdown = 3;
    private static long lastAiSpawnTime;
    public static int score = 0; 
    public static String playerName = ""; 
//    DatabaseConnetor dc = new DatabaseConnetor();
    		

    /**
     * this method takes in an input state and changes the gamestate
     * according to the inputs provided. if the player for the inputs is dead then this method
     * does not change the gamestate
     * the changes made are movement from WASD keys, projectile firing ,and cannon angle
     * this method also plays corresponding sound effects (if any)
     * @param inputState the inputs from a player
     * @return the updated gamestate
     */
    public void stepInputs(InputState inputState)
    {
        Player currentPlayer = this.getPlayers().get(inputState.playerID);

        if (!currentPlayer.isDead())
        {
            if(inputState.wPressed)
            {
                if(!checkWallCollision(currentPlayer,0,-currentPlayer.getPlayerSpeed()))
                {
                    currentPlayer.setyPosition(currentPlayer.getyPosition() - currentPlayer.getPlayerSpeed());
                    if(!Source.movSou.isPlaying()) {
                        Source.movSou.play(Buffer.movementBuffer);
                    }
                }
            }
            if(inputState.aPressed)
            {
                if(!checkWallCollision(currentPlayer,-currentPlayer.getPlayerSpeed(), 0))
                {
                    currentPlayer.setxPosition(currentPlayer.getxPosition() - currentPlayer.getPlayerSpeed());
                    if(!Source.movSou.isPlaying()) {
                        Source.movSou.play(Buffer.movementBuffer);
                    }
                }
            }
            if(inputState.sPressed)
            {
                if(!checkWallCollision(currentPlayer,0, currentPlayer.getPlayerSpeed()))
                {
                    currentPlayer.setyPosition(currentPlayer.getyPosition() + currentPlayer.getPlayerSpeed());
                    if(!Source.movSou.isPlaying()) {
                        Source.movSou.play(Buffer.movementBuffer);
                    }
                }
            }
            if(inputState.dPressed)
            {
                if(!checkWallCollision(currentPlayer,currentPlayer.getPlayerSpeed(), 0))
                {
                    currentPlayer.setxPosition(currentPlayer.getxPosition() + currentPlayer.getPlayerSpeed());
                    if(!Source.movSou.isPlaying()) {
                        Source.movSou.play(Buffer.movementBuffer);
                    }
                }
            }
            if(!inputState.dPressed && !inputState.wPressed && !inputState.aPressed && !inputState.sPressed)
            {
                if(Source.movSou.isPlaying()) {
                    Source.movSou.pause();
                }
            }
        }

        if(inputState.mLeftPressed && System.currentTimeMillis() - currentPlayer.getLastFireTime() > currentPlayer.getTimeBetweenShots() && currentPlayer.getAmmoCount() > 0)
        {
            Projectile newProjectile = new Projectile();
            newProjectile.setxPosition(currentPlayer.getxPosition() + currentPlayer.getWidth()/2);
            newProjectile.setyPosition(currentPlayer.getyPosition() + currentPlayer.getHeight()/2);
            newProjectile.setxVelocity(currentPlayer.getProjectileSpeed()  * (float) Math.sin(Math.toRadians(currentPlayer.getCannonAngle())));
            newProjectile.setyVelocity(currentPlayer.getProjectileSpeed() * (float) -Math.cos(Math.toRadians(currentPlayer.getCannonAngle())));
            newProjectile.setPlayerID(currentPlayer.getPlayerID());
            this.getLiveProjectiles().add(newProjectile);
            currentPlayer.setLastFireTime(System.currentTimeMillis());
            currentPlayer.setAmmoCount(currentPlayer.getAmmoCount() - 1);
            Source.fireSou.play(Buffer.fireBuffer);
        }


        currentPlayer.setCannonAngle((float)Math.toDegrees(Math.atan2(
                inputState.mousex-(currentPlayer.getxPosition()+currentPlayer.getWidth()/2),
                (currentPlayer.getyPosition()+currentPlayer.getHeight()/2)-inputState.mousey)));
    }

    /**
     * this method is used to see if a given movement would cause the player to intersect a wall
     * @param currentPlayer the player to check
     * @param xMove the x value of the movement
     * @param yMove the y value of the movement
     * @return true if the player would intersect the wall, false if not
     */
    private boolean checkWallCollision(Player currentPlayer,float xMove,float yMove)
    {
        boolean collided = false;

        for(int x = 0; x <map.gethCount(); x++)
        {
            for(int y = 0; y < map.getvCount(); y++)
            {
                if(map.getMaze()[x][y])
                {
                    if (checkCollision(currentPlayer.getxPosition()+xMove,currentPlayer.getWidth(),currentPlayer.getyPosition()+yMove,currentPlayer.getHeight(),
                            x*1280f/map.gethCount(),1280f/map.gethCount(),y*720f/map.getvCount(),720f/map.getvCount()))
                    {
                        collided = true;
                    }
                }
            }
        }

        return collided;
    }

    /**
     * checks to see if two rectangles are intersecting
     * @param x1 x coordinate of the top left corner of the first rect
     * @param w1 width of the first rect
     * @param y1 y coordinate of the top left corner of the first rect
     * @param h1 height of the first rect
     * @param x2 x coordinate of the top left corner of the second rect
     * @param w2 width of the second rect
     * @param y2 y coordinate of the top left corner of the second rect
     * @param h2 height of the second rect
     * @return true if they intersect, false if not
     */
    private boolean checkCollision(float x1, float w1, float y1, float h1,float x2, float w2, float y2, float h2)
    {
        Rectangle rectangle1 = new Rectangle((int)x1,(int)y1,(int)w1,(int)h1);
        Rectangle rectangle2 = new Rectangle((int)x2,(int)y2,(int)w2,(int)h2);
        return rectangle1.intersects(rectangle2);
    }

    /**
     * creates a gamestate for when the game starts
     * @param clients the players that are in the game
     * @return the starting gamestate
     */
    public void createDefaultGameState(ArrayList<ClientInfo> clients)
    {
        for(ClientInfo client:clients)
        {
            Player player = new Player();
            player.setPlayerID(client.getClientID());
            player.setCannonAngle(0);
            player.setPlayerName(client.getClientName());
            player.setxPosition(map.getSpawnPoints()[client.getClientID()*2] * (float)1280/(float)48);
            player.setyPosition(map.getSpawnPoints()[client.getClientID()*2 + 1] * (float)720/(float)24);
            players.add(player);
        }
        Source.init();
    }

    /**
     * steps each of the animations in the gamestate
     */
    public void stepAnimations()
    {
        ArrayList<BlastAnimation> animationsToRemove = new ArrayList<>();
        for(BlastAnimation animation:blastAnimations)
        {
            if(animation.getProgress() >= 1)
            {
                animationsToRemove.add(animation);
            }
            animation.step();
        }
        blastAnimations.removeAll(animationsToRemove);
    }

    /**
     * checks if any players are colliding with powerups and changes
     * player values if they are colliding
     */
    public void checkPowerUpCollisions()
    {
        Powerup poweruptoRemove = null;
        for(Powerup powerup:availablePowerups) {
            for(Player currentPlayer:players)
            {
                if(checkCollision(currentPlayer.getxPosition(),currentPlayer.getWidth(),currentPlayer.getyPosition(), currentPlayer.getHeight(),
                        powerup.getxPosition(),powerup.getWidth(),powerup.getyPosition(),powerup.getHeight()))
                {
                    switch (powerup.getType())
                    {
                        case AMMO:currentPlayer.setAmmoCount(currentPlayer.getAmmoCount() + 8);break;
                        case SPEEDUP:currentPlayer.setPlayerSpeed(currentPlayer.getPlayerSpeed() + 0.2f);break;
                        case SHOT_SPEEDUP:currentPlayer.setProjectileSpeed(currentPlayer.getProjectileSpeed() + 3f);break;
                        case FIRE_RATE:currentPlayer.setTimeBetweenShots(currentPlayer.getTimeBetweenShots() - 150); break;
                    }
                    Source.picSou.play(Buffer.pickUpBuffer);

                    poweruptoRemove = powerup;
                }
            }
        }
        if(poweruptoRemove != null)
        {
            availablePowerups.remove(poweruptoRemove);
        }
    }

    /**
     * checks if projectiles are colliding with walls or other players
     * and makes changes accordingly, also creates animations for each
     * collision.
     * moves each projectile in the gamestate by their velocity
     */
    public void checkProjectileCollisions()
    {
        for(Projectile projectile:liveProjectiles)
        {
            projectile.setyPosition(projectile.getyPosition()+projectile.getyVelocity());
            projectile.setxPosition(projectile.getxPosition()+projectile.getxVelocity());
        }

        ArrayList<Projectile> toRemove = new ArrayList<>();

        for(Projectile projectile:liveProjectiles) {
            for(Player currentPlayer:players)
            {
                if(checkCollision(currentPlayer.getxPosition(),currentPlayer.getWidth(),currentPlayer.getyPosition(),currentPlayer.getHeight(),
                        projectile.getxPosition(),projectile.getWidth(),projectile.getyPosition(),projectile.getHeight()) && System.currentTimeMillis()-currentPlayer.getLastHitTime() > 50)
                {
                    if(currentPlayer.getPlayerID() != projectile.getPlayerID())
                    {
                        currentPlayer.setHP(currentPlayer.getHP()-1);
                        currentPlayer.setLastHitTime(System.currentTimeMillis());
                        BlastAnimation animation = new BlastAnimation(projectile.getxPosition(),projectile.getyPosition());
                        blastAnimations.add(animation);
                        toRemove.add(projectile);
                        if(currentPlayer.isDead())
                        {
                            Source.expSou.play(Buffer.explosionBuffer);
                            DatabaseConnetor dc = new DatabaseConnetor();
                            try {
                            	if(!GameState.playerName.equals("")) {
                            		dc.updateScore(GameState.playerName, GameState.score);
                            	}
								
							} catch (SQLException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
                            
                        }
                    }

                }
            }
            for(Player currentPlayer:aiTanks)
            {
                if(checkCollision(currentPlayer.getxPosition(),currentPlayer.getWidth(),currentPlayer.getyPosition(),currentPlayer.getHeight(),
                        projectile.getxPosition(),projectile.getWidth(),projectile.getyPosition(),projectile.getHeight()) && System.currentTimeMillis()-currentPlayer.getLastHitTime() > 50)
                {
                    if(currentPlayer.getPlayerID() != projectile.getPlayerID())
                    {
                        currentPlayer.setHP(currentPlayer.getHP()-1);
                        currentPlayer.setLastHitTime(System.currentTimeMillis());
                        BlastAnimation animation = new BlastAnimation(projectile.getxPosition(),projectile.getyPosition());
                        blastAnimations.add(animation);
                        toRemove.add(projectile);
                        if(currentPlayer.isDead())
                        {
                            Source.expSou.play(Buffer.explosionBuffer);
                            GameState.score+=20;
                        }
                    }

                }
            }

            for(int x = 0; x < map.gethCount(); x++)
            {
                for(int y = 0; y < map.getvCount(); y++)
                {
                    if(map.getMaze()[x][y])
                    {
                        if(checkCollision(projectile.getxPosition(),projectile.getWidth(),projectile.getyPosition(),projectile.getHeight(),
                                x*1280f/map.gethCount(),1280f/map.gethCount(),y*720f/map.getvCount(),720f/map.getvCount()))
                        {
                            toRemove.add(projectile);
                            BlastAnimation animation = new BlastAnimation(projectile.getxPosition(),projectile.getyPosition());
                            blastAnimations.add(animation);
                        }
                    }
                }
            }

            if(projectile.getxPosition()<0 || projectile.getxPosition()>1280 || projectile.getyPosition()<0 || projectile.getyPosition() >720) {
                toRemove.add(projectile);
            }
        }
        liveProjectiles.removeAll(toRemove);
    }

    /**
     * moves ai players in the game, has a 1% chance of changing inputs each
     * frame, moves each ai tank according to its input state following the
     * same rules as normal players
     */
    public void moveAiTanks()
    {
        for(AITank aiTank:aiTanks)
        {
            if(!aiTank.isDead())
            {
                Random random = new Random();
                float prob = random.nextFloat();
                if(prob < 0.01f)
                {
                    aiTank.setInputState(aiTank.createAiInput());
                }

                InputState inputState = aiTank.getInputState();

                if(inputState.wPressed)
                {
                    if(!checkWallCollision(aiTank,0,-aiTank.getPlayerSpeed()))
                    {
                        aiTank.setyPosition(aiTank.getyPosition() - aiTank.getPlayerSpeed());
                    }
                }
                if(inputState.aPressed)
                {
                    if(!checkWallCollision(aiTank,-aiTank.getPlayerSpeed(), 0))
                    {
                        aiTank.setxPosition(aiTank.getxPosition() - aiTank.getPlayerSpeed());
                    }
                }
                if(inputState.sPressed)
                {
                    if(!checkWallCollision(aiTank,0, aiTank.getPlayerSpeed()))
                    {
                        aiTank.setyPosition(aiTank.getyPosition() + aiTank.getPlayerSpeed());
                    }
                }
                if(inputState.dPressed)
                {
                    if(!checkWallCollision(aiTank,aiTank.getPlayerSpeed(), 0))
                    {
                        aiTank.setxPosition(aiTank.getxPosition() + aiTank.getPlayerSpeed());
                    }
                }

                Player closest = getClosestPlayer(aiTank.getxPosition(),aiTank.getyPosition());
                aiTank.setCannonAngle((float)Math.toDegrees(Math.atan2(
                        closest.getxPosition()-(aiTank.getxPosition()+aiTank.getWidth()/2),
                        (aiTank.getyPosition()+aiTank.getHeight()/2)-closest.getyPosition())));

                if(inputState.mLeftPressed && System.currentTimeMillis() - aiTank.getLastFireTime() > 1000)
                {
                    Projectile newProjectile = new Projectile();
                    newProjectile.setxPosition(aiTank.getxPosition() + aiTank.getWidth()/2);
                    newProjectile.setyPosition(aiTank.getyPosition() + aiTank.getHeight()/2);
                    newProjectile.setxVelocity(aiTank.getProjectileSpeed()  * (float) Math.sin(Math.toRadians(aiTank.getCannonAngle())));
                    newProjectile.setyVelocity(aiTank.getProjectileSpeed() * (float) -Math.cos(Math.toRadians(aiTank.getCannonAngle())));
                    newProjectile.setPlayerID(-1);
                    getLiveProjectiles().add(newProjectile);
                    aiTank.setLastFireTime(System.currentTimeMillis());
                    Source.fireSou.play(Buffer.fireBuffer);
                }
            }
        }
    }

    /**
     * returns the closest real player to a given position
     * @param x the x position to check
     * @param y the y position to check
     * @return the closest player to the x and y positions
     */
    private Player getClosestPlayer(float x, float y)
    {
        Player closest = null;
        double closestDistance = 1000000000;
        for (Player player:players)
        {
            if(Math.sqrt((player.getxPosition() - x)*(player.getxPosition() - x) + (player.getyPosition() - y)*(player.getyPosition() - y)) < closestDistance)
            {
                closest = player;
                closestDistance = Math.sqrt((player.getxPosition() - x)*(player.getxPosition() - x) + (player.getyPosition() - y)*(player.getxPosition() - x));
            }
        }
        return closest;
    }

    /**
     * fills the powerups up to 10 when called, powerups are chosen randomly
     */
    public void updatePowerup() {

        Random random = new Random();
        while(availablePowerups.size() <= 10)
        {
            int x = random.nextInt(map.gethCount() -2) +1;
            int y = random.nextInt(map.getvCount() -2) +1;
            int powerupType = random.nextInt(4);

            if(map.getMaze()[x][y] != true)
            {
                Powerup.Type type;
                if(powerupType == 0)
                {
                    type = Powerup.Type.FIRE_RATE;
                }
                else if(powerupType == 1)
                {
                    type = Powerup.Type.AMMO;
                }
                else if(powerupType == 2){
                    type = Powerup.Type.SPEEDUP;
                }
                else
                {
                    type = Powerup.Type.SHOT_SPEEDUP;
                }
                Powerup powerup = new Powerup(type,(x*(1280/(float)map.gethCount())),(y*(720/(float)map.getvCount())));
                availablePowerups.add(powerup);
            }
        }
    }

    /**
     * spawns a new ai tank if the time since the last spawn has been 5 seconds
     */
    public void updateAiTanks()
    {
        if(System.currentTimeMillis() - lastAiSpawnTime > 5000)
        {
            Random random = new Random();
            AITank aiTank = new AITank();

            int x,y;

            do {
                x = random.nextInt(map.gethCount());
                y = random.nextInt(map.getvCount());
            }
            while(!(map.getMaze()[x][y] != true && map.getMaze()[x+1][y] != true &&
                    map.getMaze()[x][y+1] != true && map.getMaze()[x+1][y+1] != true));

            aiTank.setCannonAngle(180);
            aiTank.setxPosition(x*(1280/(float)map.gethCount()));
            aiTank.setyPosition((y*(720/(float)map.getvCount())));
            aiTanks.add(aiTank);
            lastAiSpawnTime = System.currentTimeMillis();
        }
    }

    /**
     * tests whether the game has ended or not
     * @param gameMode the gamemode of the current gamestate
     * @return true if the game has ended, false if not
     */
    public boolean gameEnded(Server.GameMode gameMode)
    {
        if(players.size() == 0)
        {
            return false;
        }
        int aliveCount = 0;
        for(Player player:players)
        {
            if(!player.isDead())
            {
                aliveCount += 1;
            }
        }

        if(gameMode == Server.GameMode.PVP)
        {
            return aliveCount == 1;
        }
        else if(gameMode == Server.GameMode.TPVE)
        {
            return aliveCount <= 0;
        }
        else
        {
            return false;
        }
    }

    public boolean getLoginState() {
		return loginState;
	}

	public void setLoginState(boolean loginState) {
		this.loginState = loginState;
	}

	public void setAvailablePowerups(ArrayList<Powerup> availablePowerups) {
		this.availablePowerups = availablePowerups;
	}

	public ArrayList<AITank> getAiTanks() {
		return aiTanks;
	}

	public void setAiTanks(ArrayList<AITank> aiTanks) {
		this.aiTanks = aiTanks;
	}

	public ArrayList<Player> getPlayers() {
        return players;
    }


    public ArrayList<Projectile> getLiveProjectiles() {
        return liveProjectiles;
    }


    public ArrayList<Powerup> getAvailablePowerups() {
        return availablePowerups;
    }


    public Map getMap() {
        return map;
    }

    public ArrayList<BlastAnimation> getBlastAnimations() {
        return blastAnimations;
    }

    public boolean isFinished() {
        return finished;
    }

    public void setFinished(boolean finished) {
        this.finished = finished;
    }

    public int getGameCountdown() {
        return gameCountdown;
    }

    public void setGameCountdown(int gameCountdown) {
        this.gameCountdown = gameCountdown;
    }

    public String getWinMessage() {
        return winMessage;
    }

    public void setWinMessage(String winMessage) {
        this.winMessage = winMessage;
    }
}
