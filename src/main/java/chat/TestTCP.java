package chat;

import chat.models.Message;
import chat.models.User;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Date;

public class TestTCP {
    public static void main(String args[]) throws IOException {

       // ServerSocket sock=new ServerSocket(6666);
       // Socket distant=sock.accept();
        /** User Def **/
        User Vincent=new User("20100",new Date(),InetAddress.getLocalHost());
        User Kompe=new User("Walleza",new Date(),InetAddress.getLocalHost());

        Message msg= new Message(1,Vincent,Kompe,"Test");

        ServerSocket net=new ServerSocket(6666);
        Socket distant=net.accept();
        ObjectOutputStream out=new ObjectOutputStream(distant.getOutputStream());
        out.writeObject(msg);
    }
}
