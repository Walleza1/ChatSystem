package chat;

import chat.models.Message;
import chat.models.Packet;
import chat.models.User;
import chat.net.NetworkManager;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.Observable;
import java.util.Observer;
import java.util.Scanner;

public class Controller implements Observer,Runnable {
    private User self;
    private User distant;
    private NetworkManager myNet;

    private Controller(){
        try {
            this.myNet=NetworkManager.getInstance();
            this.myNet.addObserver(this);
            this.self=new User("Moi", new Date(), myNet.getMyAddr());
            this.distant=new User("Lui",new Date(), InetAddress.getByName("10.32.2.222"));
        } catch (UnknownHostException e) {
            System.out.println("Distant failed");
        }
    }

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

    @Override
    public void update(Observable observable, Object o) {
        Packet p=(Packet)o;
        if (p instanceof Message) {
            Message m=(Message) p;
            System.out.println("From " + ((Packet) o).getSource().getPseudo() + " : " + m.getContenu());
        }
    }

    public void sendPacket(Packet p){
        this.myNet.sendPacket(p);
    }

    @Override
    public void run() {
        Scanner scan=new Scanner(System.in);
        Boolean close=false;
        Message msg=null;
        System.out.println("My address "+this.myNet.getMyAddr());
        while(!close){
            String str = scan.nextLine();
            if (str.equals("close")){
                close=true;
                continue;
            }
            msg=new Message(1,this.self,this.distant,str);
            this.sendPacket(msg);
        }
    }

    public void logout () {
        //TODO
        //Deletes all user data, resetting the app as if it was launched for the first time
    }
}
