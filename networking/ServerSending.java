package networking;

import java.io.*;
import java.net.DatagramPacket;
import java.net.SocketException;

public class ServerSending extends Thread{

    private boolean running = true;

    /**
     * start the thread that sends the gamestate to clients
     */
    @Override
    public void run() {

        while (running) {
            try {
                ByteArrayOutputStream byteStream = new ByteArrayOutputStream(5000);
                ObjectOutputStream outputStream = new ObjectOutputStream(new BufferedOutputStream(byteStream));

                //send updated gamestate to clients
                outputStream.reset();
                Server.gameStateLock.lock();
                outputStream.writeObject(Server.gameState);
                Server.gameStateLock.unlock();
                outputStream.flush();
                byte[] sendBuffer = byteStream.toByteArray();

                for (ClientInfo client : Server.clients) {
                    DatagramPacket sendPacket = new DatagramPacket(sendBuffer, sendBuffer.length, client.getAddress(), client.getUDPPort());
                    Server.UDPSocket.send(sendPacket);
                    outputStream.reset();
                }

                outputStream.reset();

            } catch (SocketException e) {

            }catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void setRunning(boolean running) {
        this.running = running;
    }
}
