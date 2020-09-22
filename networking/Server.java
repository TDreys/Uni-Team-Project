package networking;

import gamelogic.GameState;
import gamelogic.entities.Player;
import input.InputState;
import program.Main;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.concurrent.locks.ReentrantLock;

public class Server extends Thread {

    public enum GameMode {PVP,TPVE}
    public enum State {WAITING, RUNNING, FINISHED}

    private static final int UDP_PORT = 55556;
    private static final int TCP_PORT = 55555;

    public static ArrayList<ClientInfo> clients = new ArrayList<>();
    public static ArrayList<InputState> clientInputStates = new ArrayList<>();
    public static DatagramSocket UDPSocket;
    public static GameState gameState;
    public static ReentrantLock inputStateLock = new ReentrantLock();
    public static ReentrantLock gameStateLock = new ReentrantLock();
    public static State state = State.WAITING;
    public static GameMode gameMode = GameMode.PVP;

    public ServerSocket serverSocket;
    private ServerReceiving serverReceiving = new ServerReceiving();
    private ServerSending serverSending = new ServerSending();
    private ServerClientManager serverClientManager = new ServerClientManager();
    private long gameStartTime;
    private boolean running;

    public Server()
    {
        running = true;
        state = State.WAITING;
        gameState = null;
        UDPSocket = null;
        clients.clear();
        clientInputStates.clear();
        try {
            serverSocket = new ServerSocket(TCP_PORT,0,InetAddress.getLocalHost());
            serverSocket.setSoTimeout(600000);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * starts the server thread
     */
    @Override
    public void run() {
        serverClientManager.start();
        startReceivingClientInfo();
        startGameLoop();
        finishGame();
    }

    /**
     * closes the server thread and other threads dependant on the server thread
     */
    public void close()
    {
        try {
            serverReceiving.setRunning(false);
            serverSending.setRunning(false);
            serverClientManager.setRunning(false);
            running = false;

            if(serverSocket != null)
            {
                serverSocket.close();
            }
            if(UDPSocket != null)
            {
                UDPSocket.close();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * this method makes the server listen for new clients before the game has started
     */
    private void startReceivingClientInfo()
    {
        try {

            while (state == State.WAITING && running)
            {
                Socket clientSocket = serverSocket.accept();
                ObjectInputStream inputStream = new ObjectInputStream(clientSocket.getInputStream());
                ClientInfo newClient = (ClientInfo) inputStream.readObject();

                ObjectOutputStream outputStream = new ObjectOutputStream(clientSocket.getOutputStream());
                outputStream.writeObject(clients.size());
                outputStream.writeObject(clients);
                outputStream.close();
                clientSocket.close();
                newClient.setClientID(clients.size());
                clients.add(newClient);
                serverClientManager.sendMessage("PLAYER_JOIN:" + newClient.getClientName());
            }

            serverSocket.close();

        }
        catch (SocketTimeoutException e)
        {
            if(clients.size() == 0)
            {
                try {
                    serverSocket.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            else
            {
                state = State.RUNNING;
                Main.state = Main.State.PLAYING;
                try {
                    serverSocket.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
        catch (SocketException e) {

        }catch (IOException e) {
            e.printStackTrace();
        }
        catch (ClassNotFoundException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * this method starts the main game loop
     * the server starts the receiving and sending threads
     * the server reads the input states for each client
     * and performs the game logic 200 times a second
     */
    private void startGameLoop()
    {
        try {
            UDPSocket = new DatagramSocket(UDP_PORT, InetAddress.getLocalHost());
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        gameState = new GameState();
        gameState.createDefaultGameState(clients);
        serverReceiving.start();
        serverSending.start();
        try {
            gameState.setGameCountdown(3);
            Thread.sleep(1000);
            gameState.setGameCountdown(2);
            Thread.sleep(1000);
            gameState.setGameCountdown(1);
            Thread.sleep(1000);
            gameState.setGameCountdown(0);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        gameStartTime = System.currentTimeMillis();

        while (state == State.RUNNING && running) {

            try {
                Thread.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            inputStateLock.lock();
            for (InputState inputState : clientInputStates) {
                gameStateLock.lock();
                gameState.stepInputs(inputState);
                gameStateLock.unlock();
            }
            inputStateLock.unlock();

            gameStateLock.lock();
            gameState.moveAiTanks();
            gameState.checkPowerUpCollisions();
            gameState.checkProjectileCollisions();
            gameState.stepAnimations();
            gameState.updatePowerup();
            if (gameMode == GameMode.TPVE)
            {
                gameState.updateAiTanks();
            }

            if(gameState.gameEnded(gameMode))
            {
                gameState.setFinished(true);
                Player winner = null;
                for(Player player:gameState.getPlayers())
                {
                    if (!player.isDead())
                    {
                        winner = player;
                    }
                }
                if(winner != null)
                {
                    gameState.setWinMessage(winner.getPlayerName() + " Wins!");
                }
                else
                {
                    gameState.setWinMessage("You survived " + (System.currentTimeMillis() - gameStartTime)/1000 + " seconds");
                }
                state = State.FINISHED;
            }
            gameStateLock.unlock();
        }
    }

    /**
     * make the server wait 3 seconds and send a gameover message to the clients
     */
    private void finishGame()
    {
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        serverClientManager.sendMessage("GAMEOVER");

        this.close();
    }
}
