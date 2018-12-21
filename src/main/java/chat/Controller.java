package chat;

import chat.models.*;
import chat.net.NetworkManager;
import chat.net.UserListManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.UnknownHostException;
import java.util.*;

public class Controller implements Observer,Runnable {

    private User self;
    private ObservableList<User> userList = FXCollections.observableArrayList();
    private NetworkManager myNet;
    private ObservableMap<User,ArrayList<Message>> messageLog = FXCollections.observableHashMap();
    private User test;

    private Controller(){
        this.myNet=NetworkManager.getInstance();
        this.myNet.addObserver(this);
        this.self=new User("Moi", new Date(), myNet.getMyAddr());
        try {
            this.test = new User("Test", new Date(), InetAddress.getByName("1.1.1.1"));
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        this.getList().add(test);
        ArrayList<Message> msgtest = new ArrayList<>();
        msgtest.add(new Message(0,self,test,"Coucou"));
        msgtest.add(new Message(1,self,test,"ça va ?"));
        msgtest.add(new Message(1,test,self,"Oui ça va"));
        messageLog.put(test,msgtest);

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

    public ObservableList<User> getList () {
        return userList;
    }

    public ObservableMap<User,ArrayList<Message>> getMap () {
        return messageLog;
    }

    public ArrayList<String> getHistoryFromUser (User u) {
        ArrayList<String> res = new ArrayList<>();
        for (Message m : messageLog.get(u)){
            if (m.getSource().equals(self)){
                res.add("Moi : " + m.getContenu());
            } else {
                res.add(m.getSource().getPseudo() + " : " + m.getContenu());
            }

        }
        return res;
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

    /**
     * Send a notification to notify my presence. Then wait for a UserListPacket.
     * @param s
     * @return
     */
    public boolean isUsernameAvailable(String s){
        boolean retour=true;
        this.setUsername(s);
        Notifications notifications=Notifications.createNewUserPacket(this.self,null);
        this.sendPacket(notifications);
        userList.add(getSelf());
        System.out.println("Pseudo libre : "+retour);
        return retour;
    }

    /**
     * Verify if username is in userlist.
     * @param s
     * @return
     */
    public boolean usernameInList(String s){
        boolean res = false;
        for (User u : userList){
            if (u.getPseudo().equals(s)){
                res = true;
            }
        }
        return res;
    }

    /**
     * Main method to handle packet.
     * @param observable
     * @param o
     */
    @Override
    public void update(Observable observable, Object o) {
        Packet p=(Packet)o;
        if (p instanceof Notifications) {
            System.out.println("Received Notifications "+((Notifications) p).getType().toString());
            //TYPE NEW USER
            if (((Notifications) p).getType() == Notifications.NotificationType.newUser) {
                //Send list of users
                ArrayList<User> listUser = new ArrayList<>();
                listUser.addAll(this.userList);
                System.out.println("Taille de ma liste "+listUser.size());
                UserListPacket pack=new UserListPacket(this.self,p.getSource(),listUser);
                NetworkManager.getInstance().sendUserList(pack);
                this.userList.add(p.getSource());
                System.out.println("List send");
                //TYPE NEW PSEUDO
            } else if (((Notifications) p).getType() == Notifications.NotificationType.logout){
                for (User u : userList){
                    //When i received this packet i remove
                    if (u.getAddress()==p.getSource().getAddress()){
                        userList.remove(u);
                    }
                }
            }else if (((Notifications) p).getType() == Notifications.NotificationType.newPseudo){
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
            else if(p instanceof UserListPacket){
                UserListPacket userListPacket=(UserListPacket) p;
                userList.addAll(userListPacket.getUserList());
            }else if (p instanceof File){
                System.out.println("File received");
            }
        }
    }

    /**
     * Give a packet to networkManger
     * @param p Packet to send
     */
    public void sendPacket(Packet p){
        this.myNet.sendPacket(p);
    }

    @Override
    public void run() {
        Scanner scan=new Scanner(System.in);
        boolean close=false;
        System.out.println("My address "+this.myNet.getMyAddr());
        Notifications notifications=Notifications.createNewUserPacket(this.self,null);
        this.sendPacket(notifications);
        while(!close){
            String str = scan.nextLine();
            if (str.equals("close")){
                close=true;
            }
        }
    }

    /**
     * LogOut method :
     * Send Notification when I logout. And start a New Controller.
     */
    public void logout () {
        Notifications notifications=Notifications.createLogOutPacket(this.self,null);
        this.sendPacket(notifications);
        System.out.println("Logout send");
        //Deletes all user data, resetting the app as if it was launched for the first time
        INSTANCE = new Controller();

    }
}
