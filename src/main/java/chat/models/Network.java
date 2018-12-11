package chat.models;

import chat.listeners.BroadcastListener;
import chat.listeners.UnicastListener;

import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.Observable;
import java.util.Observer;

public class Network extends Observable implements Observer {
    private static Network uniqueInstance=null;
    public int nb_message=0;
    public static int BROADCAST_PORT=6666;
    public static int UNICAST_PORT=6667;

    private BroadcastListener broadcastListener;
    private UnicastListener unicastListener;

    private Network() throws SocketException {
        this.broadcastListener=new BroadcastListener(new DatagramSocket(BROADCAST_PORT));
        //this.unicastListener=new UnicastListener(new DatagramSocket(UNICAST_PORT));
        Thread b=new Thread(broadcastListener);
        //Thread c=new Thread(unicastListener);

        this.broadcastListener.addObserver(this);

        b.start();
        //c.start();
    }

    public static Network getInstance() throws SocketException {
        if (uniqueInstance==null){
            uniqueInstance=new Network();
        }
        return uniqueInstance;
    }
    @Override
    public void update(Observable observable, Object o) {
        this.setChanged();
        notifyObservers();
        this.clearChanged();
    }
}
