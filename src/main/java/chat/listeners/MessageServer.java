package chat.listeners;

import java.net.ServerSocket;
import java.net.Socket;

//TODO
public class MessageServer extends Thread {
    public ServerSocket serverSocket;

    public MessageServer(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
    }

    @Override
    public void run() {
        while(!serverSocket.isClosed()){

        }
    }
}
