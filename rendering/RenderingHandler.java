package rendering;

import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL11.glColor3f;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL13.GL_MULTISAMPLE;
import static program.Main.State.*;

import java.awt.Color;
import java.util.ArrayList;

import interfaceui.elements.*;
import org.lwjgl.opengl.GL;

import gamelogic.GameState;
import gamelogic.entities.*;
import interfaceui.GUI;
import program.Main;
import rendering.util.fontUtil.Text;

public class RenderingHandler {

    /**
     * initialise opengl and enable attributes that are needed, also initialises the basic renderer
     */
    public RenderingHandler()
    {
        GL.createCapabilities();
        BasicRenderer.init();
        glEnable(GL_MULTISAMPLE);
        glEnable(GL_BLEND);
    }

    /**
     * clears the screen then renders the appropriate scene
     */
    public void update(GameState gameState, GUI gui, WindowHandler windowHandler, Main.State state)
    {
        glClearColor(0f,0f,0f,1f);
        glClear(GL_COLOR_BUFFER_BIT|GL_DEPTH_BUFFER_BIT);

        //render different scene based on state of program
        if(state == Main.State.PLAYING)
        {
            renderGameState(gameState,windowHandler);
        }
        else if(state == MAIN_MENU)
        {
            renderGUIFrame(gui.getMainMenu());
        }
        else if(state == JOINING_MENU)
        {
            renderGUIFrame(gui.getJoiningMenu());
        }
        else if(state == PAUSED)
        {
            renderGUIFrame(gui.getPauseMenu());
        }
        else if(state == LOBBY)
        {
            renderGUIFrame(gui.getLobbyMenu());
        }
        else if(state == OPTIONS)
        {
            renderGUIFrame(gui.getOptionsMenu());
        }
        else if(state == JOIN_GAME)
        {
            renderGUIFrame(gui.getJoinGameMenu());
        }
        else if(state == OPTIONS_GAME)
        {
            renderGUIFrame(gui.getOptionsGameMenu());
        }

    }

    /**
     * destroys the basic renderer
     */
    public void destroy()
    {
        BasicRenderer.destroy();
    }

    /**
     * this method renders the game state given,
     * in order it renders the map,players, powerups and projectiles
     * @param state the game state to render
     */
    private void renderGameState(GameState state, WindowHandler windowHandler)
    {
        //render each map block

        Map map = state.getMap();
        for(int x = 0; x < map.gethCount(); x++)
        {
            for(int y = 0; y < map.getvCount(); y++)
            {
                if(map.getMaze()[x][y])
                {
                    BasicRenderer.renderQuad(x * windowHandler.getWIDTH() /  (float)map.gethCount() , y * windowHandler.getHEIGHT() / (float)map.getvCount(),
                            windowHandler.getWIDTH() / (float)map.gethCount(), windowHandler.getHEIGHT() / (float)map.getvCount(),
                            new Color(150, 70, 100));
                }
            }
        }
        
        //render each ai tank
        for (Player aiTank : state.getAiTanks()) {
            if(aiTank.isDead())
            {
                BasicRenderer.renderQuad(aiTank.getxPosition(), aiTank.getyPosition(),
                        aiTank.getWidth(), aiTank.getHeight(),
                        new Color(0.3f, 0.3f, 0.3f));
                glColor3f(0.2f, 0.2f, 0.2f);
                BasicRenderer.renderLine(aiTank.getxPosition() + aiTank.getWidth() / 2,
                        aiTank.getyPosition() + aiTank.getHeight() / 2,
                        (int) (aiTank.getxPosition() + aiTank.getWidth() / 2 + 35 * Math.sin(Math.toRadians((double) aiTank.getCannonAngle()))),
                        (int) (aiTank.getyPosition() + aiTank.getHeight() / 2 - 35 * Math.cos(Math.toRadians((double) aiTank.getCannonAngle()))),
                        4f);
            }
            else
            {
                BasicRenderer.renderQuad(aiTank.getxPosition(), aiTank.getyPosition(),
                        aiTank.getWidth(), aiTank.getHeight(),
                        new Color(0.7f, 0.5f, 0.2f));
                glColor3f(0.3f, 0.4f, 0.5f);
                BasicRenderer.renderLine(aiTank.getxPosition() + aiTank.getWidth() / 2,
                        aiTank.getyPosition() + aiTank.getHeight() / 2,
                        (int) (aiTank.getxPosition() + aiTank.getWidth() / 2 + 35 * Math.sin(Math.toRadians((double) aiTank.getCannonAngle()))),
                        (int) (aiTank.getyPosition() + aiTank.getHeight() / 2 - 35 * Math.cos(Math.toRadians((double) aiTank.getCannonAngle()))),
                        4f);
            }
        }

        //render each player
        for (Player player : state.getPlayers()) {
            if(player.getPlayerID() == Main.client.getClientInfo().getClientID())
            {
                int border = 2;
                BasicRenderer.renderQuad(player.getxPosition() - border, player.getyPosition() - border,
                        player.getWidth() + border*2, player.getHeight() + border*2,
                        new Color(0.8f, 1f, 0.8f));

                //render HUD
                BasicRenderer.renderQuad(1100 - border,10-border,160+ border*2,20+ border*2,new Color(255,255,255));
                float lost = 1 - (float)player.getHP()/(float)player.getMaxHP();
                if(lost < 1)
                {
                    BasicRenderer.renderQuad(1100+(160*lost),10,160*(1-lost),20,new Color(255,0,0));
                }

                Text ammoText = new Text("Ammo: " + player.getAmmoCount(),GUI.font);
                BasicRenderer.renderText(1100,40,ammoText);
                ammoText.destroy();

                if(player.isDead() && !state.isFinished())
                {
                    Text eliminatedText = new Text("You've been eliminated!",GUI.largeFont);
                    BasicRenderer.renderText((windowHandler.getWIDTH()/2) - (eliminatedText.getTextWidth()/4),300,eliminatedText);
                    eliminatedText.destroy();
                }

            }
            else
            {
                int border = 1;
                BasicRenderer.renderQuad(player.getxPosition() - border,player.getyPosition() - 10-border
                                        ,20+ border*2,4+ border*2,new Color(255,255,255));
                float lost = 1 - (float)player.getHP()/(float)player.getMaxHP();
                if(lost < 1)
                {
                    BasicRenderer.renderQuad(player.getxPosition()+(20*lost),player.getyPosition()-10,20*(1-lost),4,new Color(255,0,0));
                }
            }
            if(!player.isDead())
            {
                BasicRenderer.renderQuad(player.getxPosition(), player.getyPosition(),
                        player.getWidth(), player.getHeight(),
                        new Color(0.3f, 0.8f, 0.3f));
                glColor3f(0.2f, 0.7f, 0.3f);
                BasicRenderer.renderLine(player.getxPosition() + player.getWidth() / 2,
                        player.getyPosition() + player.getHeight() / 2,
                        (int) (player.getxPosition() + player.getWidth() / 2 + 35 * Math.sin(Math.toRadians( player.getCannonAngle()))),
                        (int) (player.getyPosition() + player.getHeight() / 2 - 35 * Math.cos(Math.toRadians(player.getCannonAngle()))),
                        4f);
            }
            else
            {
                BasicRenderer.renderQuad(player.getxPosition(), player.getyPosition(),
                        player.getWidth(), player.getHeight(),
                        new Color(0.3f, 0.3f, 0.3f));
                glColor3f(0.2f, 0.2f, 0.2f);
                BasicRenderer.renderLine(player.getxPosition() + player.getWidth() / 2,
                        player.getyPosition() + player.getHeight() / 2,
                        (int) (player.getxPosition() + player.getWidth() / 2 + 35 * Math.sin(Math.toRadians(player.getCannonAngle()))),
                        (int) (player.getyPosition() + player.getHeight() / 2 - 35 * Math.cos(Math.toRadians(player.getCannonAngle()))),
                        4f);
            }
        }
        
        //render each powerup
        for (Powerup powerup : state.getAvailablePowerups()) {
            Color powerUpColor;
            switch (powerup.getType()) {
                case FIRE_RATE:
                    powerUpColor = new Color(255, 70, 70);
                    break;
                case AMMO:
                    powerUpColor = new Color(140, 140, 170);
                    break;

                case SPEEDUP:
                    powerUpColor = new Color(0, 255, 255);
                    break;
                case SHOT_SPEEDUP:
                    powerUpColor = new Color(60, 255, 60);
                    break;
                default:
                    powerUpColor = new Color(255, 255, 255);
                    break;
            }
            BasicRenderer.renderQuad(powerup.getxPosition(), powerup.getyPosition(), powerup.getWidth(), powerup.getHeight(), powerUpColor);

        }

        //render each projectile
        for (Projectile projectile : state.getLiveProjectiles()) {
            BasicRenderer.renderQuad(projectile.getxPosition(), projectile.getyPosition(), projectile.getWidth(), projectile.getHeight(), new Color(255, 255, 255));
        }

        //render blast animations
        for(BlastAnimation blast:state.getBlastAnimations())
        {
            float progress = blast.getProgressFactor();
            BasicRenderer.renderQuad(blast.getCenterX()-(blast.getMaxSize()*progress/2),blast.getCenterY()-(blast.getMaxSize()*progress/2),
                    blast.getMaxSize()*progress,blast.getMaxSize()*progress,new Color(255,70,0));
        }

        if(state.isFinished())
        {
            Text gameoverText = new Text("GAME OVER",GUI.largeFont);
            BasicRenderer.renderText((windowHandler.getWIDTH()/2) - (gameoverText.getTextWidth()/4),300,gameoverText);
            gameoverText.destroy();

            Text winnerText = new Text(state.getWinMessage(),GUI.font);
            BasicRenderer.renderText((windowHandler.getWIDTH()/2) - (winnerText.getTextWidth()/4),370,winnerText);
            winnerText.destroy();
        }

        if(state.getGameCountdown() != 0)
        {
            Text countDown = new Text(Integer.toString(state.getGameCountdown()),GUI.largeFont);
            BasicRenderer.renderText((windowHandler.getWIDTH()/2) - (countDown.getTextWidth()/4),(windowHandler.getHEIGHT()/2)-(countDown.getTextHeight()/4),countDown);
            countDown.destroy();
        }

    }

    /**
     * this method renders a gui scene, each nodes parameters are pre-determined
     * it determines what type each node is and renders it appropriately
     * if the node contains text then a text object is made and then deleted
     * after being rendered. text rendering is currently not efficient
     * @param nodes
     */
    private void renderGUIFrame(ArrayList<Node> nodes)
    {
        for (Node node:nodes)
        {

            //render each button
            if(node instanceof Button)
            {
                Button button = (Button) node;
                if(button.isSelected())
                {
                    BasicRenderer.renderQuad(button.getX(), button.getY(), button.getWidth(), button.getHeight(), button.getColor());
                    BasicRenderer.renderHollowQuad(button.getX(),button.getY(),button.getWidth(),button.getHeight(),new Color(250,250,250),3f);
                }
                else {
                    BasicRenderer.renderQuad(button.getX(), button.getY(), button.getWidth(), button.getHeight(), button.getColor());
                }
                Text renderText = new Text(button.getText(), button.getFont());
                BasicRenderer.renderText(button.getX() + (button.getWidth() / 2) - (renderText.getTextWidth() / 4),
                        button.getY() + (button.getHeight() / 2) - (renderText.getTextHeight() / 4),
                        renderText);
                renderText.destroy();
            }
            //render each label
            else if (node instanceof Label)
            {
                Label label = (Label) node;
                Text renderText = new Text(label.getText(),label.getFont());
                BasicRenderer.renderText(label.getX(),label.getY(),renderText);
                renderText.destroy();
            }
            //render each text field
            else if (node instanceof TextField)
            {
                TextField textField = (TextField) node;
                Text renderText = new Text(textField.getContent(),textField.getFont());
                BasicRenderer.renderText(textField.getX()+textField.getWidth()/2-renderText.getTextWidth()/4,
                                        textField.getY()+textField.getHeight()/2-renderText.getTextHeight()/4,renderText);
                renderText.destroy();

                if(textField.isFocused())
                {
                    BasicRenderer.renderHollowQuad(textField.getX(),textField.getY(),textField.getWidth(),textField.getHeight(),new Color(250,250,250),3f);
                }
                else
                {
                    BasicRenderer.renderHollowQuad(textField.getX(),textField.getY(),textField.getWidth(),textField.getHeight(),new Color(130,130,130),3f);
                }
            }
            //render each rowtable
            else if (node instanceof RowTable)
            {
                RowTable rowTable = (RowTable) node;
                for (int i = 0; i < rowTable.getRowTexts().size(); i++)
                {
                    String currentText = rowTable.getRowTexts().get(i);
                    Text renderText = new Text(currentText,rowTable.getFont());

                    float boxHeight = rowTable.getHeight()/rowTable.getMaxRowCount();
                    float boxYCoord = rowTable.getY() + boxHeight * i;
                    BasicRenderer.renderHollowQuad(rowTable.getX(), boxYCoord, rowTable.getWidth(), boxHeight,new Color(255,255,255),3f);
                    BasicRenderer.renderText(rowTable.getX() + 10,boxYCoord+(boxHeight/2)-(renderText.getTextHeight()/4),renderText);
                    renderText.destroy();
                }
            }
        }
    }
}
