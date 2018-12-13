package chat.net;

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

    protected void managePacket(Packet p) {
        String received=p.getClass().toString();
        System.out.println("Unicast : "+received+" from "+p.getSource().getPseudo()+" at "+p.getSource().getAddress().toString());
        this.notifyObservers(p);
    }

    @Override
    public void run() {
        try {
            while(!this.socket.isClosed()) {
                System.out.println("Serveur en attente");
                Socket distant = this.socket.accept();
                System.out.println("Connexion détectée");
                Discussion discussion =new Discussion(distant);
                discussion.addObserver(this);
                chatRooms.put(distant.getInetAddress(), discussion);
                new Thread(discussion).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(Observable observable, Object o) {
        managePacket((Packet) o);
        System.out.println("Taille ChatRooms "+this.chatRooms.size());
        Discussion mine=((Discussion)observable);
        try {
            mine.sendMessage((Packet)o);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
