package chat;

import chat.models.File;
import chat.models.Message;
import chat.models.Notifications;
import chat.models.User;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.*;
import java.util.Date;

public class TestTCP {
    public static void main(String args[]) throws IOException {
        ServerSocket sock=new ServerSocket(6666);
        Socket distant=sock.accept();
        ObjectOutputStream objectOutput = new ObjectOutputStream(distant.getOutputStream());
        /** User Def **/
        User Vincent=new User(1,"20100",new Date(),InetAddress.getLocalHost());
        User Kompe=new User(2,"Walleza",new Date(),InetAddress.getLocalHost());
        /** Msg def **/
        Message msg=new Message(1,Vincent,Kompe,"Coucou");
        File f=new File(Vincent,Kompe);
        Notifications notifications=Notifications.createNewUserPaquet(Vincent,Kompe);
        objectOutput.writeObject(notifications);
        distant.close();
    }
}
