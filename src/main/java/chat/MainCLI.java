package chat;

import chat.models.Message;
import chat.models.Notifications;
import chat.net.NetworkManager;
import chat.models.User;

import java.io.IOException;
import java.net.InetAddress;
import java.util.Date;

public class MainCLI {
    public static void main(String[] args) throws IOException {
        /** User Def **/
        User Vincent = new User("20100", new Date(), InetAddress.getLocalHost());
        User Kompe = new User("Walleza", new Date(), InetAddress.getLocalHost());
        /** Msg def **/
        Message msg = new Message(1, Vincent, Kompe, "Coucou");
        NetworkManager myNet = NetworkManager.getInstance();

        Notifications notifications=Notifications.createNewUserPaquet(Vincent,Kompe);

        myNet.sendPacket(notifications);
    }
}
