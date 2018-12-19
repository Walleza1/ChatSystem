package chat.net;

import chat.models.Notifications;
import chat.models.Packet;
import chat.models.User;
import chat.models.UserListPacket;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.*;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

public class NetworkManager extends Observable implements Observer {
    private static NetworkManager uniqueInstance=null;

    /** Static definition of our Broadcast port**/
    public static int BROADCAST_PORT=6666;
    /** Static definition of our Unicast port**/
    public static int UNICAST_PORT=6667;

    public static int USERLIST_PORT=6668;
    public static int USERLIST_TIMEOUT_MS=1000;

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
    private NetworkManager() {
        try {
            this.udpSocket=new DatagramSocket(BROADCAST_PORT);
            this.udpSocket.setBroadcast(true);
            this.tcpSocket=new ServerSocket(UNICAST_PORT);
            this.broadcastManager =new BroadcastManager(udpSocket);
            this.unicastManager =new UnicastManager(tcpSocket);

        } catch (IOException e) {
            System.out.println("Error lors création socket");
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
        } catch (SocketException e) {
            e.printStackTrace();
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
    }

    /** Singleton Instance **/
    public static NetworkManager getInstance() {
        if (uniqueInstance==null){
            uniqueInstance=new NetworkManager();
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

    public void sendUserList(UserListPacket p){
        try {
            Socket distant=new Socket(p.getDestination().getAddress(),NetworkManager.USERLIST_PORT);
            ObjectOutputStream out=new ObjectOutputStream(distant.getOutputStream());
            out.writeObject(p);
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public ArrayList<User> receiveList(){
        ArrayList<User> ret;
        ServerSocket serverSocket = null;
        try {
            serverSocket=new ServerSocket(NetworkManager.USERLIST_PORT);
            //timeout after 1s
            serverSocket.setSoTimeout(NetworkManager.USERLIST_TIMEOUT_MS);
            Socket distant=serverSocket.accept();
            ObjectInputStream in= new ObjectInputStream(new BufferedInputStream(distant.getInputStream()));
            UserListPacket listPacket=(UserListPacket) in.readObject();
            ret=listPacket.getUserList();
            serverSocket.close();
        } catch (IOException | ClassNotFoundException e) {
           ret=new ArrayList<User>();
           if (e instanceof SocketTimeoutException){
               try {
                   serverSocket.close();
               } catch (IOException e1) {
                   System.out.println("Server socket close failed. Userlist");
               }
           }
        }
        return ret;
    }
    /** If we receive a Packet, transmit it to our observers **/
    @Override
    public void update(Observable observable, Object o) {
        this.setChanged();
        notifyObservers(o);
        this.clearChanged();
    }
}
