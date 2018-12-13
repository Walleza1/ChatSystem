package chat.net;

import chat.models.Notifications;
import chat.models.Packet;
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

    private InetAddress myAddr;
    public static InetAddress broadcastAddr;

    private BroadcastManager broadcastManager;
    private UnicastManager unicastManager;


    /**
     * Create our NetworkManager
     *
     * @throws SocketException
     */
    private NetworkManager() throws SocketException, UnknownHostException {
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

        try(final DatagramSocket socket = new DatagramSocket()){
            socket.connect(InetAddress.getByName("8.8.8.8"), 10002);
            this.myAddr =socket.getLocalAddress();
        } catch (UnknownHostException e) {
            System.out.println("No Internet");
        }

        NetworkInterface ni=NetworkInterface.getByInetAddress(this.myAddr);
        for (int i=0;i<ni.getInterfaceAddresses().size();i++){
            if (ni.getInterfaceAddresses().get(i).getBroadcast() != null){
                broadcastAddr=ni.getInterfaceAddresses().get(i).getBroadcast();
            }
        }
    }

    public static NetworkManager getInstance() throws SocketException {
        if (uniqueInstance==null){
            try {
                uniqueInstance=new NetworkManager();
            } catch (UnknownHostException e) {
                System.out.println("Cannot create NetWorkManager");
            }
        }
        return uniqueInstance;
    }

    /** Get actual InetAddress that is routable
     *
     * @return InetAddress
     */
    public InetAddress getMyAddr() {
        return myAddr;
    }

    /** NetworkManger SendPacket Message
     * If Packet is a Notification then give it to BroadcastManager
     * Else Give it to UnicastManager
     * **/
    public void sendPacket(Packet p){
        if (p instanceof Notifications){
            this.broadcastManager.sendPacket(p);
        }else{
            //TODO: Give it to UnicastManager
            System.out.println("Packet 1 to 1");
        }
    }
    @Override
    public void update(Observable observable, Object o) {
        this.setChanged();
        if (o!=null) {
            System.out.println("NetWorkManager :" + o.getClass().toString());
        }
        notifyObservers();
        this.clearChanged();
    }
}
