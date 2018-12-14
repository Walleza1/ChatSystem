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

    /** Static definition of our Broadcast port**/
    public static int BROADCAST_PORT=6666;
    /** Static definition of our Unicast port**/
    public static int UNICAST_PORT=6667;

    private DatagramSocket udpSocket;
    private ServerSocket tcpSocket;
    private InetAddress myAddr;
    public static InetAddress broadcastAddr;

    private BroadcastManager broadcastManager;
    private UnicastManager unicastManager;


    /**
     * Create our NetworkManager
     * Initialize BroadcastManager and UnicastManager
     * Search our routable InetAddress and the broadcast InetAddress
     * @throws SocketException
     */
    private NetworkManager() throws SocketException, UnknownHostException {
        this.broadcastManager =new BroadcastManager(udpSocket);
        try {
            this.unicastManager =new UnicastManager(tcpSocket);
            this.udpSocket=new DatagramSocket(BROADCAST_PORT);
            this.tcpSocket=new ServerSocket(UNICAST_PORT);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Thread b=new Thread(broadcastManager);
        Thread c=new Thread(unicastManager);

        this.broadcastManager.addObserver(this);
        this.unicastManager.addObserver(this);

        b.start();
        c.start();

        //Get our routable InetAddress
        try(final DatagramSocket socket = new DatagramSocket()){
            socket.connect(InetAddress.getByName("8.8.8.8"), 10002);
            this.myAddr =socket.getLocalAddress();
        } catch (UnknownHostException e) {
            System.out.println("No Internet");
        }
        // Get our local network broadcast InetAddress.
        NetworkInterface ni=NetworkInterface.getByInetAddress(this.myAddr);
        for (int i=0;i<ni.getInterfaceAddresses().size();i++){
            if (ni.getInterfaceAddresses().get(i).getBroadcast() != null){
                broadcastAddr=ni.getInterfaceAddresses().get(i).getBroadcast();
            }
        }
    }

    /** Singleton Instance **/
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
           this.unicastManager.sendPacket(p);
        }
    }
    /** If we receive a Packet, transmit it to our observers **/
    @Override
    public void update(Observable observable, Object o) {
        this.setChanged();
        if (o!=null) {
            System.out.println("NetWorkManager :" + o.getClass().toString());
        }
        notifyObservers(o);
        this.clearChanged();
    }
}
