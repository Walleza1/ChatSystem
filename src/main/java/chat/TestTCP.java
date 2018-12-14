package chat;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;

public class TestTCP {
    public static void main(String args[]) throws IOException, ClassNotFoundException {
        /*ServerSocket sock=new ServerSocket(6666);
        Socket distant=sock.accept();
        /** User Def **/
        /*User Vincent=new User("20100",new Date(),InetAddress.getLocalHost());
        User Kompe=new User("Walleza",new Date(),InetAddress.getLocalHost());
        /** Msg def **/
        /*Message msg=new Message(1,Vincent,Kompe,"Coucou");
        File f=new File(Vincent,Kompe);
        Notifications notifications=Notifications.createNewUserPaquet(Vincent,Kompe);
        objectOutput.writeObject(notifications);
        distant.close();*/
/*
        ObjectInputStream in = new ObjectInputStream(new BufferedInputStream(distant.getInputStream()));
        Packet p = (Packet) in.readObject();

        System.out.println("Packet received "+p.getClass().toString());*/
        InetAddress local=Inet4Address.getLocalHost();
        System.out.println(local);
        InetAddress myAddr=null;
        try(final DatagramSocket socket = new DatagramSocket()){
            socket.connect(InetAddress.getByName("8.8.8.8"), 10002);
            myAddr=socket.getLocalAddress();
            socket.close();
        }
        NetworkInterface i=NetworkInterface.getByInetAddress(myAddr);
        System.out.println(i.getInterfaceAddresses().size());
        System.out.println(i.getInterfaceAddresses().get(0).getBroadcast());
    }
}
