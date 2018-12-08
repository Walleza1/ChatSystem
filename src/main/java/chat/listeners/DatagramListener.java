package chat.listeners;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

public abstract class DatagramListener extends Thread{
    protected DatagramSocket socket;

    public DatagramListener(DatagramSocket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        byte[] incomingData = new byte[1024];
        while(true) {
            DatagramPacket incomingPacket = new DatagramPacket(incomingData, incomingData.length);
            try {
                socket.receive(incomingPacket);
                byte[] data = incomingPacket.getData();
                String receveid=new String(incomingData,0,incomingPacket.getLength());
                /*ByteArrayInputStream in = new ByteArrayInputStream(data);
                ObjectInputStream is = new ObjectInputStream(in);
*/
                //Packet p = (Packet) is.readObject();
                //pas la peine de lire ce qu'on envoie !
                //if(!p.getAddrSource().equals(ContactCollection.getMe().getIp())) {
                managePacket(incomingPacket);
                //}
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    protected abstract void managePacket(DatagramPacket p);
}
