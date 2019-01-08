package chat;

import chat.models.*;
import chat.net.NetworkManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.collections.transformation.SortedList;

import java.net.InetAddress;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.Semaphore;

public class Controller implements Observer {
    private User self;
    private ObservableList<User> userList = FXCollections.observableArrayList();
    private Semaphore userListSemaphore;
    private NetworkManager myNet;
    private ObservableMap<InetAddress,ArrayList<Message>> messageLog = FXCollections.observableHashMap();

    private Controller(){
        this.myNet=NetworkManager.getInstance();
        this.myNet.addObserver(this);
        this.self=new User("Moi", myNet.getMyAddr());
        this.userListSemaphore=new Semaphore(1);
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

    public ObservableMap<InetAddress,ArrayList<Message>> getMap () {
        return messageLog;
    }

    public User getUserFromAddress (InetAddress addr) {
        try {
            userListSemaphore.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        for (User u : userList){
            if(u.getAddress().equals(addr)){
                return u;
            }
        }
            userListSemaphore.release();
        return null;
    }

    public ArrayList<String> getHistoryFromUser (User u) {
        ArrayList<String> res = new ArrayList<>();
        SimpleDateFormat formater = new SimpleDateFormat("dd/MM hh:mm");
        for (Message m : messageLog.get(u.getAddress())){
            if (m.getSource().equals(self)){
                res.add("(" + formater.format(m.getTimeStamp()) + ") Moi : " + m.getContenu());
            } else {
                res.add("(" + formater.format(m.getTimeStamp()) + ") " + m.getSource().getPseudo() + " : " + m.getContenu());
            }

        }
        return res;
    }

    public ArrayList<Message> getMessageListFromUser(User u) {
        System.out.println(messageLog.get(u.getAddress()));
        return messageLog.get(u.getAddress());
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
        User ret=null;
        try {
            userListSemaphore.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        for(User u : userList){
            if(pseudo.equals(u.getPseudo())){
                ret=u;
            }
        }
        userListSemaphore.release();
        return ret;
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
        for (int i=0;i<5;i++) {
            this.sendPacket(notifications);
        }
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        try {
            userListSemaphore.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("End waited");
        for (User u : this.userList){
            if (u.getPseudo().equals(getSelf().getPseudo())){
                if (!u.getAddress().equals(getSelf().getAddress())){
                    System.out.println("Pseudo pas libre");
                    retour=false;
                }
            }
        }
        if (retour){
            userList.add(this.getSelf());
        }
        userListSemaphore.release();
        return retour;
    }

    /**
     * Verify if username is in userlist.
     * @param s
     * @return
     */
    public boolean usernameInList(String s){
        boolean res = false;
        try {
            userListSemaphore.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        for (User u : userList){
            if (u.getPseudo().equals(s)){
                res = true;
            }
        }
        userListSemaphore.release();
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
                try {
                    userListSemaphore.acquire();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                listUser.addAll(this.userList);

                UserListPacket pack=new UserListPacket(this.self,p.getSource(),listUser);
                SortedList<User> sortedListUser=this.userList.sorted(new Comparator<User>() {
                    @Override
                    public int compare(User user, User t1) {
                        return user.getTimeStamp().compareTo(t1.getTimeStamp());
                    }
                });
                //To verify
                List<User> lastThreeUser= sortedListUser.subList(Math.max(sortedListUser.size()-3,0),sortedListUser.size());
                if (lastThreeUser.contains(this.getSelf())){
                    NetworkManager.getInstance().sendUserList(pack);
                    System.out.println("List send");
                }

                if (!this.userList.contains(p.getSource())){
                    boolean isHisPseudoAvailable=true;
                    for (User u : this.userList){
                        if (u.getPseudo().equals(p.getSource().getPseudo())){
                            isHisPseudoAvailable=false;
                        }
                    }
                    if (isHisPseudoAvailable) {
                        System.out.println("Ajout new User");
                        this.userList.add(p.getSource());
                        this.messageLog.put(p.getSource().getAddress(), new ArrayList<Message>());
                    }
                }
                userListSemaphore.release();
            } else if (((Notifications) p).getType() == Notifications.NotificationType.logout){
                try {
                    userListSemaphore.acquire();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                for (User u : userList){
                    //When i received this packet i remove
                    if (u.getAddress()==p.getSource().getAddress()){
                        userList.remove(u);
                    }
                }
                userListSemaphore.release();
                //TYPE NEW PSEUDO
            }else if (((Notifications) p).getType() == Notifications.NotificationType.newPseudo){
                boolean alreadyIn=false;
                System.out.println("newPseudoNotif received");
                try {
                    userListSemaphore.acquire();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                for (User u : userList){
                    if(p.getSource().getAddress().equals(u.getAddress())){
                        alreadyIn=true;
                        u.setPseudo(p.getSource().getPseudo());
                    }
                }
                User tmp = userList.get(0);
                userList.remove(userList.get(0));
                userList.add(tmp);
                
                if (!alreadyIn){
                    userList.add(p.getSource());
                }
                userListSemaphore.release();
            }
        }else{
            if (p instanceof Message) {
                Message m=(Message) p;
                System.out.println("From " + p.getSource().getPseudo() + " : " + m.getContenu());
                ArrayList<Message> tmp = new ArrayList<>();
                tmp.addAll(getMessageListFromUser(p.getSource()));
                tmp.add(m);
                this.messageLog.remove(p.getSource().getAddress());
                this.messageLog.put(p.getSource().getAddress(),tmp);
            }
            else if(p instanceof UserListPacket){
                System.out.println("Received userlistPacket");
                UserListPacket userListPacket=(UserListPacket) p;
                try {
                    userListSemaphore.acquire();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                for (User u : userListPacket.getUserList()){
                    if (!userList.contains(u)) {
                        userList.add(u);
                        messageLog.put(u.getAddress(), new ArrayList<Message>());
                    }else{
                        System.out.println("User en doublon oublie");
                    }
                }
                userListSemaphore.release();
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

    void stop(){
        System.out.println("Closing it");
        this.myNet.stop();
    }
}
