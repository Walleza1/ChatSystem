package chat;

import chat.models.ObserverFlag;
import chat.models.Packet;
import chat.models.User;
import chat.models.UserListPacket;
import chat.net.NetworkManager;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Date;

public class TestTCP {
    public static void main(String args[]) throws UnknownHostException {


       // ServerSocket sock=new ServerSocket(6666);
       // Socket distant=sock.accept();
        /** User Def **/
        User Vincent=new User("20100",new Date(),InetAddress.getLocalHost());
        User Kompe=new User("Walleza",new Date(),InetAddress.getLocalHost());
        /** Msg def **/
        ArrayList<User> listUser=new ArrayList<User>();
        listUser.add(Kompe);
        UserListPacket packet=new UserListPacket(Vincent,Kompe,listUser);

        try {
            DatagramSocket senderSocket = new DatagramSocket();
            senderSocket.setBroadcast(true);
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ObjectOutputStream os = new ObjectOutputStream(outputStream);
            os.writeObject(packet);
            byte[] data = outputStream.toByteArray();
            outputStream.close();
            os.close();
            DatagramPacket sendPacket = new DatagramPacket(data, data.length, InetAddress.getLocalHost(), NetworkManager.BROADCAST_PORT);
            senderSocket.send(sendPacket);
            senderSocket.close();
        }catch (IOException e){
            System.out.println(e);
        }
       /* ObjectOutputStream objectOutput=new ObjectOutputStream(distant.getOutputStream());
        objectOutput.writeObject(packet);

        ObjectInputStream inputStream=new ObjectInputStream(new BufferedInputStream(distant.getInputStream()));

        Packet p = (Packet) inputStream.readObject();

        if (p instanceof UserListPacket){
            UserListPacket userL=(UserListPacket) p;
            System.out.println("Packet UserListPacket");
            System.out.println("Nombre de personnes dans la liste "+userL.getUserList().size());
        }*/

        //distant.close();
        /*Message msg=new Message(1,Vincent,Kompe,"Coucou");
        File f=new File(Vincent,Kompe);
        Notifications notifications=Notifications.createNewUserPaquet(Vincent,Kompe);
        */

        /*ObjectInputStream in = new ObjectInputStream(new BufferedInputStream(distant.getInputStream()));
        Packet p = (Packet) in.readObject();
        distant.close();

        System.out.println("Packet received "+p.getClass().toString());*/
    }
}
