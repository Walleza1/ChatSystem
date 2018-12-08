package chat;

import chat.listeners.BroadcastListener;
import chat.listeners.UnicastListener;
import chat.models.User;
import chat.listeners.DatagramListener;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Date;

public class MainCLI{
    public static int BROADCAST_PORT=6666;
    public static int UNICAST_PORT=6667;
    public static void main(String[] args) throws IOException {
        /** User Def **/
        User Vincent=new User(1,"20100",new Date(),InetAddress.getLocalHost());
        User Kompe=new User(2,"Walleza",new Date(),InetAddress.getLocalHost());
        /** Msg def **/
        //Message msg=new Message(0,Vincent,Kompe,new Date(),"Coucou t nul");

        Thread Broadcast=new BroadcastListener(new DatagramSocket(BROADCAST_PORT));
        Thread Unicast=new UnicastListener(new DatagramSocket(UNICAST_PORT));
        Broadcast.start();
        Unicast.start();
        System.out.println("Ready");
    }

}
