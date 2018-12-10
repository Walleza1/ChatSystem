package chat.listeners;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.Observable;

//@TODO:
public abstract class FluxListener extends Observable implements Runnable {
    public Socket socket;

    public FluxListener(Socket socket) {
        this.socket = socket;
    }

    @Override
    public synchronized void run() {
        while(!socket.isClosed()){
            try {
                BufferedReader in=new BufferedReader(new InputStreamReader(socket.getInputStream()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
