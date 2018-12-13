package chat;

import chat.net.Discussion;
import chat.models.Notifications;
import chat.models.User;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Date;

public class ClientTCP {

    public static void main(String args[]) throws IOException, ClassNotFoundException {
        Socket srv=new Socket(InetAddress.getLocalHost(),6667);
        //ObjectOutputStream objectOutput = new ObjectOutputStream(srv.getOutputStream());
        //ObjectInputStream in = new ObjectInputStream(new BufferedInputStream(srv.getInputStream()));

        /** User Def **/
        User Vincent=new User("20100",new Date(),InetAddress.getLocalHost());
        User Kompe=new User("Walleza",new Date(),InetAddress.getLocalHost());
        Notifications notifications=Notifications.createNewUserPaquet(Vincent,Kompe);

        Discussion n=new Discussion(srv);
        new Thread(n).start();
        n.sendMessage(notifications);
        /*int i=0;
        while(!srv.isClosed()) {
            if (i==0 || i==1) {
                objectOutput.writeObject(notifications);
            }
            i+=1;
            if (i==5){
                srv.close();
            }
            try{
                Packet p=(Packet) in.readObject();
                System.out.println("Received "+p.getClass().toString());
            }catch (SocketException e){
                srv.close();
            }
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }*/
    }

}