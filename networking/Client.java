package networking;

import audio.Source;
import gamelogic.GameState;
import input.InputState;
import program.Main;

import java.io.*;
import java.net.*;
import java.util.ArrayList;

public class Client extends Thread{

    private ClientInfo clientInfo;
    private InetAddress hostAddress;
    private ServerSocket listenSocket;
    private DatagramSocket UDPSocket;
    private boolean running = true;

    public Client(String clientName, InetAddress address, boolean isHost) {
        this.clientInfo = new ClientInfo();
        this.clientInfo.setClientName(clientName);
        this.clientInfo.setAddress(address);
        this.clientInfo.setHost(isHost);
        try {
            this.listenSocket = new ServerSocket(0,0,InetAddress.getLocalHost());
            this.clientInfo.setListenPort(this.listenSocket.getLocalPort());
            this.start();
        } catch (SocketException e) {

        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * starts the client thread which listens to messages from the server
     */
    @Override
    public void run() {

        while(running)
        {
            try {
                Socket clientSocket = listenSocket.accept();
                ObjectInputStream inputStream = new ObjectInputStream(clientSocket.getInputStream());
                String message =inputStream.readUTF();
                inputStream.close();
                clientSocket.close();

                if(message.equals("GAMEOVER"))
                {
                    Main.client.close();
                    Source.movSou.delete();
                    Main.state = Main.State.MAIN_MENU;
                    Main.gui.getLobbyPlayersRowTable().getRowTexts().clear();
                }
                else if(message.substring(0,12).equals("PLAYER_JOIN:"))
                {
                    Main.gui.getLobbyPlayersRowTable().addRowLabel(message.substring(12));
                }
                else if(message.equals("GAME_STARTED"))
                {
                    Main.state = Main.State.PLAYING;
                }

            } catch (SocketException e) {

            }catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * sends a message to the server
     * @param message the message to send
     */
    public void sendMessage(String message)
    {
        try {
            Socket clientSocket = new Socket(hostAddress,55557);
            ObjectOutputStream outputStream = new ObjectOutputStream(clientSocket.getOutputStream());
            outputStream.writeUTF(message);
            outputStream.close();
            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * this method will connect the client to the specified server
     * the client sends information about itself to the server
     * the server responds with its address
     *
     * @param address the address of the server
     * @param port the port number for the server
     */
    public int connectToServer(InetAddress address, int port) throws IOException
    {
        Socket clientSocket;

        try {
            clientSocket = new Socket(address,port);
            clientSocket.setSoTimeout(10000);

            if(UDPSocket == null)
            {
                UDPSocket = new DatagramSocket();
                this.clientInfo.setUDPPort(UDPSocket.getLocalPort());
            }

            ObjectOutputStream outputStream = new ObjectOutputStream(clientSocket.getOutputStream());
            outputStream.writeObject(this.clientInfo);

            ObjectInputStream inputStream = new ObjectInputStream(clientSocket.getInputStream());
            this.hostAddress = address;
            Main.gui.getLobbyIPLabel().setText("lobby IP: " + hostAddress.getHostAddress());
            int ID = (int)inputStream.readObject();
            this.clientInfo.setClientID(ID);

            ArrayList<ClientInfo> clientInfos = (ArrayList<ClientInfo>) inputStream.readObject();
            for(ClientInfo info:clientInfos)
            {
                Main.gui.getLobbyPlayersRowTable().addRowLabel(info.getClientName());
            }

            inputStream.close();
            clientSocket.close();
            return ID;

        }  catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return 0;
    }

    /**
     * this method takes the input state for the client and sends it
     * to the server it is connected to
     * must be called after calling connectToServer()
     * @param state the input state for the client
     */
    public void sendUDPPacket(InputState state)
    {
        try {
            ByteArrayOutputStream byteStream = new ByteArrayOutputStream(200);
            ObjectOutputStream outputStream = new ObjectOutputStream(new BufferedOutputStream(byteStream));
            outputStream.flush();
            outputStream.writeObject(state);
            outputStream.flush();
            byte[] sendBuffer = byteStream.toByteArray();

            DatagramPacket sendPacket = new DatagramPacket(sendBuffer, sendBuffer.length, hostAddress, 55556);
            UDPSocket.send(sendPacket);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * this method waits to receive the updated gamestate from the server
     * and returns the received gamestate
     * must be called after calling connectToServer()
     * @return the gamestate from the server
     */
    public GameState receiveUDPPacket()
    {
        try {
            byte[] recvBuf = new byte[10000];
            DatagramPacket packet = new DatagramPacket(recvBuf, recvBuf.length);
            UDPSocket.receive(packet);
            ByteArrayInputStream byteStream = new ByteArrayInputStream(recvBuf);
            ObjectInputStream inputStream = new ObjectInputStream(new BufferedInputStream(byteStream));
            GameState gameState = (GameState) inputStream.readObject();
            inputStream.close();
            return gameState;
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        catch (ClassNotFoundException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * closes the udp socket for the client
     */
    public void close()
    {
        running = false;
        try {
            listenSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        UDPSocket.close();
    }


    public InetAddress getHostAddress() {
        return hostAddress;
    }

    public void setHostAddress(InetAddress hostAddress) {
        this.hostAddress = hostAddress;
    }

    public ClientInfo getClientInfo() {
        return clientInfo;
    }
}
