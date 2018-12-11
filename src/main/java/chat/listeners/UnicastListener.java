package chat.listeners;

import chat.models.Packet;
import chat.models.User;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Observable;

public class UnicastListener extends Observable implements Runnable{
    public ServerSocket socket;

    private HashMap<User,Thread> chatRooms;

    public UnicastListener(ServerSocket socket) {
        this.socket=socket;
        this.chatRooms=new HashMap<User,Thread>();
    }

    @Override
    public void notifyObservers() {
        this.setChanged();
        super.notifyObservers();
        this.clearChanged();
    }

    protected void managePacket(Packet p) {
        String received=p.getClass().toString();
        System.out.println("Unicast : "+received);
        notifyObservers();
    }

    @Override
    public void run() {
        try {
            Socket distant=this.socket.accept();
            ObjectInputStream is = new ObjectInputStream(distant.getInputStream());
            Packet p = (Packet) is.readObject();
            managePacket(p);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }
}
