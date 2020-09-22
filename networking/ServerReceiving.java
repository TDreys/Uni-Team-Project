package networking;

import input.InputState;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.SocketException;

public class ServerReceiving extends Thread{

    private boolean running = true;

    /**
     * start the thread that receives input states from clients
     */
    @Override
    public void run() {
        while (running) {
            try {
                //receive inputs
                byte[] recvBuf = new byte[200];
                DatagramPacket packet = new DatagramPacket(recvBuf, recvBuf.length);
                Server.UDPSocket.receive(packet);
                ByteArrayInputStream byteInputStream = new ByteArrayInputStream(recvBuf);
                ObjectInputStream inputStream = new ObjectInputStream(new BufferedInputStream(byteInputStream));
                InputState inputState = (InputState) inputStream.readObject();
                boolean contained = false;
                Server.inputStateLock.lock();
                for(int i = 0; i < Server.clientInputStates.size(); i++)
                {
                    if (Server.clientInputStates.get(i).playerID == inputState.playerID)
                    {
                        Server.clientInputStates.set(i,inputState);
                        contained = true;
                    }
                }
                if(contained == false)
                {
                    Server.clientInputStates.add(inputState);
                }
                Server.inputStateLock.unlock();
                inputStream.close();

            } catch (SocketException e) {

            }catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    public void setRunning(boolean running) {
        this.running = running;
    }
}
