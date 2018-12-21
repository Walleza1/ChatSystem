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

    /** DatagrammeSocket in received state.
     * We use ObjectInputStream And ObjectOutputStream.
     * When we're gonna receive a packet notify observers.
     */
    public void run() {
        byte[] incomingData = new byte[1024];
        while(!this.socket.isClosed()) {
            DatagramPacket incomingPacket = new DatagramPacket(incomingData, incomingData.length);
            try {
                socket.receive(incomingPacket);
                byte[] data = incomingPacket.getData();
                ByteArrayInputStream in = new ByteArrayInputStream(data);
                ObjectInputStream is = new ObjectInputStream(in);
                Packet p = (Packet) is.readObject();
                if (!p.getSource().getAddress().equals(NetworkManager.getInstance().getMyAddr())){
                    managePacket(p);
                }
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void notifyObservers(Object p) {
        this.setChanged();
        super.notifyObservers(p);
        this.clearChanged();
    }

    public void stop(){
        this.socket.close();
    }
    /**
     * What do our BroadcastManager do when it receive a packet.
     * Transmit it to our observers
     * @param p
     */
    protected void managePacket(Packet p) {
        String received=p.getClass().toString();
        System.out.println("Broadcast : "+received);
        notifyObservers(p);
    }

    /** Send Packet
     * Broadcast a UDP Packet thru local network.
     * @param p
     */
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
            System.out.println("Envois d'un packet udp broadcast");
        } catch (IOException e) {
            System.out.println("Envois d'un packet udp broadcast raté");
        }
    }
}
