package chat.listeners;

import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class UnicastListener extends DatagramListener {

    public UnicastListener(DatagramSocket socket) {
        super(socket);
    }

    @Override
    protected void managePacket(DatagramPacket p) {
        String received=new String(p.getData(),0,p.getLength());
        System.out.println("Unicast : "+received);
    }
}
