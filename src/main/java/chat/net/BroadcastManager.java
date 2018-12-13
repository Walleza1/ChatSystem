package chat.net;

import chat.models.Packet;

import java.io.*;
import java.net.*;
import java.util.Observable;

public class BroadcastManager extends Observable implements Runnable{
    protected DatagramSocket socket;

    public BroadcastManager(DatagramSocket socket) {
        this.socket = socket;
    }

    public void run() {
        byte[] incomingData = new byte[1024];
        while(!this.socket.isClosed()) {
            DatagramPacket incomingPacket = new DatagramPacket(incomingData, incomingData.length);
            try {
                socket.receive(incomingPacket);
                byte[] data = incomingPacket.getData();

                //String receveid=new String(incomingData,0,incomingPacket.getLength());
                ByteArrayInputStream in = new ByteArrayInputStream(data);
                ObjectInputStream is = new ObjectInputStream(in);

                Packet p = (Packet) is.readObject();

                //pas la peine de lire ce qu'on envoie !
                //if(!p.getAddrSource().equals(ContactCollection.getMe().getIp())) {
                managePacket(p);
                //}
            } catch (IOException | ClassNotFoundException e) {
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
        String received=p.getClass().toString();
        System.out.println("Broadcast : "+received);
        this.setChanged();
        notifyObservers();
        this.clearChanged();
    }

    public void sendPacket(Packet p){
        try {
            DatagramSocket senderSocket=new DatagramSocket();
            senderSocket.setBroadcast(true);
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ObjectOutputStream os = new ObjectOutputStream(outputStream);
            os.writeObject(p);
            byte[] data = outputStream.toByteArray();
            outputStream.close();
            os.close();
            DatagramPacket sendPacket = new DatagramPacket(data, data.length, NetworkManager.broadcastAddr, NetworkManager.BROADCAST_PORT);
            senderSocket.send(sendPacket);
            senderSocket.close();
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
