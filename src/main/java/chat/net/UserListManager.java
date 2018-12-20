package chat.net;

import chat.models.User;
import chat.models.UserListPacket;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Observable;

public class UserListManager extends Observable implements Runnable {
    private ServerSocket serverSocket;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private ArrayList<User> userList;
    public UserListManager(ServerSocket serverSocket) throws IOException {
        this.serverSocket = serverSocket;
        userList=new ArrayList<User>();
    }

    public ArrayList<User> getUserList() {
        return userList;
    }

    @Override
    public void run() {
        ArrayList<User> ret = new ArrayList<User>();
        ServerSocket serverSocket = null;
        try {
            while(!this.serverSocket.isClosed()) {
                System.out.println("UserListManager lancé");
                Socket distant = this.serverSocket.accept();
                System.out.println("Connect received");
                ObjectInputStream in = new ObjectInputStream(new BufferedInputStream(distant.getInputStream()));
                UserListPacket listPacket = (UserListPacket) in.readObject();
                ret = listPacket.getUserList();
                this.setChanged();
                notifyObservers(ret);
                this.clearChanged();
                distant.close();
            }
        } catch (ClassNotFoundException|IOException e) {
            System.out.println("Error");
            if (e instanceof SocketTimeoutException){
                System.out.println("Timed out");
            }
            ret = new ArrayList<User>();
        }
        this.userList = ret;
    }
}
