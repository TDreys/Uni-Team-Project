package networking;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.*;

public class ServerClientManager extends Thread{

    private static final int PORT = 55557;
    private ServerSocket serverSocket;
    private boolean running = true;

    /**
     * start the thread that handles connections to clients while the game is running
     */
    @Override
    public void run() {

        try {
            serverSocket = new ServerSocket(PORT,0, InetAddress.getLocalHost());
            while (running)
            {
                Socket clientSocket = serverSocket.accept();
                ObjectInputStream inputStream = new ObjectInputStream(clientSocket.getInputStream());
                String message = inputStream.readUTF();
                clientSocket.close();

                if(message.contains("START_GAME"))
                {
                    if(message.substring(11).equals("PVP"))
                    {
                        Server.gameMode = Server.GameMode.PVP;
                    }
                    else if(message.substring(11).equals("TPVE"))
                    {
                        Server.gameMode = Server.GameMode.TPVE;
                    }
                    Server.state = Server.State.RUNNING;
                    for(ClientInfo client:Server.clients)
                    {
                        clientSocket = new Socket(client.getAddress(),client.getListenPort());
                        ObjectOutputStream outputStream = new ObjectOutputStream(clientSocket.getOutputStream());
                        outputStream.writeUTF("GAME_STARTED");
                        outputStream.close();
                        clientSocket.close();
                    }
                }
            }

            serverSocket.close();

        }catch (SocketException e) {

        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * send a message to every client currently connected
     * @param message the message to send
     */
    public void sendMessage(String message)
    {
        try {
            for(ClientInfo client:Server.clients)
            {
                Socket clientSocket = new Socket(client.getAddress(),client.getListenPort());
                ObjectOutputStream outputStream = new ObjectOutputStream(clientSocket.getOutputStream());
                outputStream.writeUTF(message);
                outputStream.close();
                clientSocket.close();
            }
        } catch (SocketException e) {

        }catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void setRunning(boolean running) {
        try {
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.running = running;
    }
}
