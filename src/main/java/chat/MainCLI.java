package chat;

import chat.models.Network;
import chat.models.User;

import java.io.IOException;
import java.net.InetAddress;
import java.util.Date;

public class MainCLI {
    public static void main(String[] args) throws IOException {
        /** User Def **/
        User Vincent=new User(1,"20100",new Date(),InetAddress.getLocalHost());
        User Kompe=new User(2,"Walleza",new Date(),InetAddress.getLocalHost());
        /** Msg def **/
        //Message msg=new Message(0,Vincent,Kompe,new Date(),"Coucou t nul");
        Network myNet=Network.getInstance();
    }
}
