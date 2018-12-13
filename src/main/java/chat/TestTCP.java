package chat;

import chat.models.Packet;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.*;
import java.util.Enumeration;

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

        try(final DatagramSocket socket = new DatagramSocket()){
            socket.connect(InetAddress.getByName("8.8.8.8"), 10002);
            System.out.println("Here "+socket.getLocalAddress().getHostAddress());
        }
        Enumeration<NetworkInterface> e = NetworkInterface.getNetworkInterfaces();
        while (e.hasMoreElements())
        {
            NetworkInterface n = e.nextElement();
            System.out.println(n.getName());
            Enumeration<InetAddress> ee = n.getInetAddresses();
            while (ee.hasMoreElements())
            {
                InetAddress i = ee.nextElement();
                if (!i.isSiteLocalAddress()){
                    System.out.println("\t"+i.getHostAddress());
                }
            }
        }
    }
}
