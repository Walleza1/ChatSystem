package chat.net;

import chat.models.ObserverFlag;
import chat.models.Packet;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;

public class UnicastManager extends Observable implements Runnable, Observer {
    public ServerSocket socket;

    private HashMap<InetAddress, Discussion> chatRooms;

    public UnicastManager(ServerSocket socket) {
        this.socket=socket;
        this.chatRooms=new HashMap<InetAddress, Discussion>();
    }

    @Override
    public void notifyObservers(Object o) {
        this.setChanged();
        super.notifyObservers(o);
        this.clearChanged();
    }

    /**
     * ServerSocket is in accept state. If we receive a connexion on that socket,
     * we create a new Discussion with that person and i'll observer all possible events.
     */
    @Override
    public void run() {
        try {
            while(true) {
                System.out.println("UnicastManager en attente");
                Socket distant = this.socket.accept();
                Discussion discussion =new Discussion(distant);
                discussion.addObserver(this);
                chatRooms.put(distant.getInetAddress(), discussion);
                new Thread(discussion).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * If we receive a flag close by one of our discussion, remove it from the list
     * Else notify observers that we received a packet
     * @param observable
     * @param o
     */
    @Override
    public void update(Observable observable, Object o) {
        if (o instanceof ObserverFlag){
            ObserverFlag observerFlag=(ObserverFlag) o;
            // Si on reçoit un packet on notifie nos observers en leur donnant le packet
            if (observerFlag.getFlag() == ObserverFlag.Flag.packetReceived){
                String received=observerFlag.getPacket().getClass().toString();
                System.out.println("Unicast : "+received+" from "+observerFlag.getPacket().getSource().getPseudo()+" at "+observerFlag.getPacket().getSource().getAddress().toString());
                notifyObservers(observerFlag.getPacket());
            }else{
                // Sinon on supprime le Thread de notre liste.
                observable.deleteObserver(this);
                Discussion t=(Discussion) observable;
                chatRooms.remove(t.distant.getInetAddress());
            }
        }
    }

    /**
     * Send a Packet in TCP.
     * If the destination is already in Discussion then use that discussion
     * Else create the discussion.
     * @param p
     */
    public void sendPacket(Packet p){
        InetAddress address=p.getDestination().getAddress();
        if (chatRooms.containsKey(address)){
            try {
                chatRooms.get(address).sendMessage(p);
            } catch (IOException e) {
                System.out.println("Erreur à l'envois d'un msg à quelqu'un déjà existant");
            }
        }else{
            Socket distant= null;
            Discussion discussion = null;
            try {
                distant = new Socket(address, NetworkManager.UNICAST_PORT);
                discussion = new Discussion(distant);
                discussion.addObserver(this);
                chatRooms.put(distant.getInetAddress(), discussion);
                new Thread(discussion).start();
            } catch (IOException e) {
                System.out.println("Erreur à l'envois d'un nouveau message");
            }
        }
    }
}
