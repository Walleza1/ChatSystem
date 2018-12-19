package chat;

import chat.models.*;
import chat.net.NetworkManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.*;

public class Controller implements Observer,Runnable {
    private User self;
    private User distant;
    public ObservableList<User> userList = FXCollections.observableArrayList();

    private NetworkManager myNet;
    private Boolean usernameOk = true;

    private Controller(){
        try {
            this.myNet=NetworkManager.getInstance();
            this.myNet.addObserver(this);
            this.self=new User("Moi", new Date(), myNet.getMyAddr());
            this.distant=new User("Lui",new Date(), InetAddress.getByName("10.32.0.83"));
            this.userList.add(new User("Test",new Date(), InetAddress.getByName("1.1.1.1")));
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

    public User getUserFromPseudo (String pseudo){
        for(User u : userList){
            if(pseudo.equals(u.getPseudo())){
                return u;
            }
        }
        System.out.println("user was not found with that pseudo");
        return null;
    }

    public boolean isUsernameAvailable(String s){
        boolean retour=true;
        this.setUsername(s);
        Notifications notifications=Notifications.createNewUserPaquet(this.self,this.distant);
        this.sendPacket(notifications);

        ArrayList<User> receivedList=NetworkManager.getInstance().receiveList();
        for (User u:receivedList){
            if (s.equals(u.getPseudo())){
                retour=false;
            }
        }
        userList.add(getSelf());
        System.out.println("Pseudo libre : "+retour);
        return true;
    }

    public boolean usernameInList(String s){
        boolean res = false;
        for (User u : userList){
            if (u.getPseudo().equals(s)){
                res = true;
            }
        }
        return res;
    }

    @Override
    public void update(Observable observable, Object o) {
        Packet p=(Packet)o;
        if (p instanceof Notifications) {
            System.out.println("Received Notifications "+((Notifications) p).getType().toString());
            //TYPE NEW USER
            if (((Notifications) p).getType() == Notifications.NotificationType.newUser) {
                //TODO
                //Send list of users
                ArrayList<User> listUser=new ArrayList<User>();
                for (User u : this.userList){
                    listUser.add(u);
                }
                UserListPacket pack=new UserListPacket(p.getDestination(),this.self,listUser);

                this.userList.add(p.getSource());
                //TYPE NEW PSEUDO
            } else if (((Notifications) p).getType() == Notifications.NotificationType.newPseudo){
                boolean alreadyIn=false;
                System.out.println("newPseudoNotif received");
                for (User u : userList){
                    if(u.getAddress() == p.getSource().getAddress()){
                        alreadyIn=true;
                        u.setPseudo(p.getSource().getPseudo());
                        userList.notifyAll();
                    }
                }
                if (!alreadyIn){
                    userList.add(p.getSource());
                }
            }
        }else{
            if (p instanceof Message) {
                Message m=(Message) p;
                System.out.println("From " + ((Packet) o).getSource().getPseudo() + " : " + m.getContenu());
            }
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
        Notifications notifications=Notifications.createNewUserPaquet(this.self,this.distant);
        this.sendPacket(notifications);
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
        INSTANCE = new Controller();
    }
}
