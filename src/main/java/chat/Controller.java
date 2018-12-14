package chat;
//TODO

import chat.models.User;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class Controller {

    private User self;

    {
        try {
            self = new User("default", InetAddress.getByName("127.0.0.1"));
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    private Controller(){}

    private static Controller INSTANCE = null;

    public static Controller getInstance() {
        if (INSTANCE == null){
            INSTANCE = new Controller();
        }
        return INSTANCE;
    }

    public User getSelf () {
        return self;
    }

    public void setSelf (User u) {
        this.self = u;
    }

    public void setUsername (String s){
        self.setPseudo(s);
    }

    public String getUsername (){
        return self.getPseudo();
    }

    public boolean isUsernameAvailable(String s){return true;}
}
