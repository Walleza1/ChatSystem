package chat.listeners;

import chat.models.Packet;
import chat.models.UserListPacket;
import com.sun.org.apache.bcel.internal.generic.INSTANCEOF;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.Observable;

public class BroadcastListener extends Observable implements Runnable{
    protected DatagramSocket socket;

    public BroadcastListener(DatagramSocket socket) {
        this.socket = socket;
    }

    public void run() {
        byte[] incomingData = new byte[1024];
        while(true) {
            DatagramPacket incomingPacket = new DatagramPacket(incomingData, incomingData.length);
            try {
                socket.receive(incomingPacket);
                byte[] data = incomingPacket.getData();
                String receveid=new String(incomingData,0,incomingPacket.getLength());
                ByteArrayInputStream in = new ByteArrayInputStream(data);
                ObjectInputStream is = new ObjectInputStream(in);
                Packet p = (Packet) is.readObject();
                //pas la peine de lire ce qu'on envoie !
                //if(!p.getAddrSource().equals(ContactCollection.getMe().getIp())) {
                managePacket(p);
                //}
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void notifyObservers() {
        this.setChanged();
        super.notifyObservers();
        this.clearChanged();
    }

    protected void managePacket(Packet p) {
        //String received=new String(p.getData(),0,p.getLength());
        String received=p.getClass().toString();
        System.out.println("Broadcast : "+received);
        this.setChanged();
        notifyObservers();
        this.clearChanged();
    }

}
