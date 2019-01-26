package chat.net;

import chat.models.UserListPacket;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.Observable;

public class UserListManager extends Observable implements Runnable {
    private ServerSocket serverSocket;

    UserListManager(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;

    }

    /**
     * Stop my runnable.
     */
    void stop(){
        try {
            this.serverSocket.close();
        } catch (IOException e) {
            System.out.println("Already Closed");
        }
    }

    /**Listen to a port, and wait for a userListPacket.
     * if received before timeout ok
     * if not, return empty list
     * */
    @Override
    public void run() {
        try {
            System.out.println("UserListManager lancé");
            while(!this.serverSocket.isClosed()) {
                Socket distant = this.serverSocket.accept();
                System.out.println("Connect received");
                ObjectInputStream in = new ObjectInputStream(new BufferedInputStream(distant.getInputStream()));
                UserListPacket listPacket = (UserListPacket) in.readObject();
                System.out.println("List Received");
                this.setChanged();
                notifyObservers(listPacket);
                this.clearChanged();
                distant.close();
            }
        } catch (ClassNotFoundException|IOException e) {
            System.out.println("Error userList receiver");
            if (e instanceof SocketTimeoutException){
                System.out.println("Timed out");
            }
        }
    }
}
