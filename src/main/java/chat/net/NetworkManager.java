package chat.net;

import chat.models.Notifications;
import chat.models.User;

import java.io.IOException;
import java.net.*;
import java.util.Date;
import java.util.Observable;
import java.util.Observer;

public class NetworkManager extends Observable implements Observer {
    private static NetworkManager uniqueInstance=null;
    public static int BROADCAST_PORT=6666;
    public static int UNICAST_PORT=6667;
    private InetAddress MyAddr;

    private BroadcastManager broadcastManager;
    private UnicastManager unicastManager;

    private NetworkManager() throws SocketException {
        this.broadcastManager =new BroadcastManager(new DatagramSocket(BROADCAST_PORT));
        try {
            this.unicastManager =new UnicastManager(new ServerSocket(UNICAST_PORT));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Thread b=new Thread(broadcastManager);
        Thread c=new Thread(unicastManager);

        this.broadcastManager.addObserver(this);
        this.unicastManager.addObserver(this);

        b.start();
        c.start();
        /** User Def **/
        User Vincent= null;
        User Kompe=null;
        try {
            Vincent = new User("20100",new Date(), InetAddress.getLocalHost());
            Kompe=new User("Walleza",new Date(),InetAddress.getLocalHost());
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        Notifications notifications=Notifications.createNewUserPaquet(Vincent,Kompe);

        broadcastManager.sendPacket(notifications);

        try(final DatagramSocket socket = new DatagramSocket()){
            socket.connect(InetAddress.getByName("8.8.8.8"), 10002);
            this.MyAddr=socket.getLocalAddress();
        } catch (UnknownHostException e) {
            this.MyAddr=null;
        }
    }

    public static NetworkManager getInstance() throws SocketException {
        if (uniqueInstance==null){
            uniqueInstance=new NetworkManager();
        }
        return uniqueInstance;
    }

    public InetAddress getMyAddr() {
        return MyAddr;
    }

    @Override
    public void update(Observable observable, Object o) {
        this.setChanged();
        if (o!=null) {
            System.out.println("Message received " + o.getClass().toString()+" in "+this.getClass().toString());
        }
        notifyObservers();
        this.clearChanged();
    }
}
