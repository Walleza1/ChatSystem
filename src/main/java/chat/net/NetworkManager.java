package chat.net;

import chat.Controller;
import chat.models.Notifications;
import chat.models.Packet;
import chat.models.UserListPacket;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.*;
import java.util.Observable;
import java.util.Observer;

public class NetworkManager extends Observable implements Observer {
    private static NetworkManager uniqueInstance=null;

    /** Static definition of our Broadcast port**/
    public static int BROADCAST_PORT=6666;
    /** Static definition of our Unicast port**/
    public static int UNICAST_PORT=6667;

    public static int USERLIST_PORT=6668;
    public static int USERLIST_TIMEOUT_MS=10000;

    private DatagramSocket udpSocket;
    private ServerSocket tcpSocket;
    private ServerSocket userSocket;
    private InetAddress myAddr;
    public static InetAddress broadcastAddr;

    private ServerManager serverManager;
    private BroadcastManager broadcastManager;
    private UnicastManager unicastManager;
    private UserListManager userListManager;

    /**
     * Create our NetworkManager
     * Initialize BroadcastManager and UnicastManager
     * Search our routable InetAddress and the broadcast InetAddress
     * @throws SocketException
     */
    private NetworkManager() {
        try {
            this.tcpSocket=new ServerSocket(UNICAST_PORT);
            this.userSocket=new ServerSocket(USERLIST_PORT);
            this.unicastManager =new UnicastManager(tcpSocket);
            this.userListManager=new UserListManager(userSocket);

        } catch (IOException e) {
            System.out.println("Error lors création socket");
        }
        Thread c=new Thread(unicastManager);
        Thread d=new Thread(userListManager);

        this.unicastManager.addObserver(this);
        this.userListManager.addObserver(this);

        c.start();
        d.start();

        try {
            this.myAddr=InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
            System.out.println("Addresse locale non trouvée");
        }
        // Get our local network broadcast InetAddress.
        NetworkInterface ni= null;
        try {
            ni = NetworkInterface.getByInetAddress(this.myAddr);
        } catch (SocketException e) {
            e.printStackTrace();
        }
        for (int i=0;i<ni.getInterfaceAddresses().size();i++){
            if (ni.getInterfaceAddresses().get(i).getBroadcast() != null){
                broadcastAddr=ni.getInterfaceAddresses().get(i).getBroadcast();
            }
        }

        initBroadcast();
    }

    public void initBroadcast(){
        try {
            this.udpSocket=new DatagramSocket(BROADCAST_PORT);
            this.udpSocket.setBroadcast(true);
            this.broadcastManager =new BroadcastManager(udpSocket);
            Thread b=new Thread(broadcastManager);
            this.broadcastManager.addObserver(this);
            b.start();
        } catch (SocketException e) {
            e.printStackTrace();
        }
        this.serverManager=null;
    }

    public void initServer(String urlServer){
        this.serverManager=new ServerManager(urlServer);
        this.broadcastManager.stop();
        this.udpSocket=null;
        this.broadcastManager=null;
    }
    /** Singleton Instance **/
    public static NetworkManager getInstance() {
        if (uniqueInstance==null){
            uniqueInstance=new NetworkManager();
        }
        return uniqueInstance;
    }

    /**
     * Stop my runnable.
     */
    public void stop(){
        this.unicastManager.stop();
        this.broadcastManager.stop();
        this.userListManager.stop();
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
            if (Controller.getInstance().isServerless()) {
                this.broadcastManager.sendPacket(p);
            }else{
                this.serverManager.sendPacket((Notifications) p);
            }
        }else{
           this.unicastManager.sendPacket(p);
        }
    }

    /**
     * Method to send a UserListPacket
     * @param p Packet to send
     */
    public void sendUserList(UserListPacket p){
        try {
            System.out.println("Envois de la liste");
            System.out.println(p.getDestination().getAddress().toString());
            Socket distant=new Socket(p.getDestination().getAddress(),NetworkManager.USERLIST_PORT);
            ObjectOutputStream out=new ObjectOutputStream(distant.getOutputStream());
            out.writeObject(p);
            out.close();
            System.out.println("Liste envoyé");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /** If we receive a Packet, transmit it to our observers **/
    @Override
    public void update(Observable observable, Object o) {
        this.setChanged();
        if (observable instanceof UserListManager){
            System.out.println("UserList on ");
            notifyObservers(o);
        }else if(observable instanceof BroadcastManager) {
            System.out.println("Br received");
            notifyObservers(o);
        }else if(observable instanceof UnicastManager){
            System.out.println("Uni received");
            notifyObservers(o);
        }
        this.clearChanged();
    }
}
