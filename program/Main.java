package program;

import static org.lwjgl.glfw.GLFW.glfwPollEvents;
import static org.lwjgl.glfw.GLFW.glfwWindowShouldClose;

import java.io.FileNotFoundException;
import java.net.InetAddress;
import java.net.UnknownHostException;

import audio.Buffer;
import audio.Source;
import gamelogic.GameState;
import input.InputHandler;
import interfaceui.DatabaseUI;
import interfaceui.GUI;
import interfaceui.LoginUI;
import networking.Client;
import networking.Server;
import rendering.RenderingHandler;
import rendering.WindowHandler;

public class Main {

    public enum State { MAIN_MENU, JOINING_MENU, LOBBY, PAUSED ,PLAYING, OPTIONS, JOIN_GAME, OPTIONS_GAME}

    public static State state = State.MAIN_MENU;

    public static GameState gameState = new GameState();
    public static GUI gui;
    public static InputHandler inputHandler;
    public static WindowHandler windowHandler;   public static RenderingHandler renderingHandler;
    public static Client client;
    public static Server server;
    public static LoginUI loginUI = new LoginUI(gameState);
    public static DatabaseUI databaseUI = new DatabaseUI(gameState);

    public static void main(String[] args)
    {
        inputHandler = new InputHandler();
        windowHandler = new WindowHandler(inputHandler);
        renderingHandler = new RenderingHandler();
        gui = new GUI();
        Buffer.init();
        Source.init();
        
        
        //write initialisation code here
        try {
            System.out.println(InetAddress.getLocalHost());
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        
        try {
			Buffer.loadALData();
		} catch (FileNotFoundException e){
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        /*
         * !!the soundeffects can only play out of while loop, cause data run everytime when doing loop,
         *   that sound like 'dudududu...' noise.
         */
        Source.bgSou.play(Buffer.bgBuffer);
        while (!glfwWindowShouldClose(windowHandler.getWindowID()))
        {	
            windowHandler.update();
            renderingHandler.update(gameState,gui,windowHandler,state);


            if(state != State.PLAYING)
            {
                glfwPollEvents();
            }
            else
            {
                gameState = client.receiveUDPPacket();
                glfwPollEvents();
                client.sendUDPPacket(inputHandler.state);
            }

        }
        

        Source.destroy();
        Buffer.cleanup();
        windowHandler.destroy();
        renderingHandler.destroy();
        if(client != null)
        {
            client.close();

            if(client.getClientInfo().isHost())
            {
                server.close();
            }
        }

    }
}