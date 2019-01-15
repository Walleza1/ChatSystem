package chat;

import chat.models.*;
import chat.net.NetworkManager;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.collections.transformation.SortedList;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.Semaphore;

public class Controller implements Observer {
    private User self;
    private ObservableList<User> userList = FXCollections.observableArrayList();
    private Semaphore userListSemaphore;
    private NetworkManager myNet;
    private ObservableMap<InetAddress,ArrayList<Message>> messageLog = FXCollections.observableHashMap();
    private Stage stage;
    private Database db;

    private Controller(){
        this.myNet=NetworkManager.getInstance();
        this.myNet.addObserver(this);
        this.self=new User("Moi", myNet.getMyAddr());
        this.userListSemaphore=new Semaphore(1);
        this.db = Database.getInstance();
        try {
            this.userList.add(new User("Test",InetAddress.getByName("1.1.1.1")));
            this.messageLog.put(InetAddress.getByName("1.1.1.1"),new ArrayList<>());
        } catch (UnknownHostException e) {
            e.printStackTrace();
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

    public ObservableList<User> getList () {
        return userList;
    }

    public ObservableMap<InetAddress,ArrayList<Message>> getMap () {
        return messageLog;
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

    public void setUsername (String s){
        self.setPseudo(s);
    }

    public void updateSelfUsername(User u){
        System.out.println("Coucou");
        db.updateSelfUsername(this.getSelf());
    }

    void setStage(Stage s){
        this.stage = s;
    }

    private Stage getStage(){
        return this.stage;
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
     */
    public boolean isUsernameAvailable(String s){
        boolean available=true;
        this.setUsername(s);
        if(db.isSelfUsernameSet(this.getSelf())){
            if(!db.getSelf().equals(this.getSelf().getPseudo())){
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Erreur");
                alert.setHeaderText(null);
                alert.setContentText("Vous devez vous reconnecter avec le même " +
                        "nom d'utilisateur que la dernière fois.");
                alert.showAndWait();
                userList.clear();
                userListSemaphore.release();
                return false;
            }
        }
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
                    if(!db.sameAsPreviousIP(u)){
                        System.out.println("Pseudo pas libre");
                        available = false;
                    } else {
                        available = true;
                    }
                }
            }
        }
        if (available){
            //DB TEST
            if(!db.isSelfUsernameSet(this.getSelf())){
                db.addSelfUsername(this.getSelf());
                userList.add(this.getSelf());
            } else {
                userList.add(this.getSelf());
                userListSemaphore.release();
                return true ;
            }

        }else{
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Erreur");
            alert.setHeaderText(null);
            alert.setContentText("Ce nom d'utilisateur est déjà pris. Recommencez.");
            alert.showAndWait();
            userList.clear();
        }

        userListSemaphore.release();
        return available;
    }

    /**
     * Verify if username is in userlist.
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
     */
    @Override
    public void update(Observable observable, Object o) {
        Packet p=(Packet)o;
        if (p instanceof Notifications) {
            System.out.println("Received Notifications "+((Notifications) p).getType().toString());
            //TYPE NEW USER
            if (((Notifications) p).getType() == Notifications.NotificationType.newUser) {
                handlerNewUser(p);
            } else if (((Notifications) p).getType() == Notifications.NotificationType.logout){
                handlerLogOut(p);
            }else if (((Notifications) p).getType() == Notifications.NotificationType.newPseudo){
                handlerNewPseudo(p);
            }
        }else{
            if (p instanceof Message) {
                handlerMessage(p);
            }
            else if(p instanceof UserListPacket){
                handlerListUser(p);
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
        db.saveIP(self.getAddress());
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

    private void handlerNewUser(Packet p){
        //Send list of users
        try {
            userListSemaphore.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        ArrayList<User> listUser = new ArrayList<>(this.userList);

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
                db.addUser(p.getSource());
                this.messageLog.put(p.getSource().getAddress(), new ArrayList<Message>());

                Platform.runLater(new Runnable() {
                                      @Override
                                      public void run() {
                                          // Update UI here.
                //PUSH NOTIFICATION TEST
                Image img = new Image("/new_user.png");
                org.controlsfx.control.Notifications.create().owner(getStage())
                        .title("Nouvel utilisateur").text(p.getSource().getPseudo() + "est en ligne.")
                        .graphic(new ImageView(img)).position(Pos.BOTTOM_LEFT).show();
                                      }
                });

            }
        }
        userListSemaphore.release();
    }
    private void handlerLogOut(Packet p){
        System.out.println("LogOut received");
        try {
            userListSemaphore.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        userList.removeIf(user -> user.equals(p.getSource()));

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                // Update UI here.
        //PUSH NOTIFICATION TEST
        Image img = new Image("/user_leaving.png");
        org.controlsfx.control.Notifications.create().owner(getStage())
                .title("Déconnexion").text(p.getSource().getPseudo() + "est hors ligne.")
                .graphic(new ImageView(img)).position(Pos.BOTTOM_LEFT).show();
            }
        });
        userListSemaphore.release();

    }

    private void handlerNewPseudo(Packet p) {
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
                db.updateUsername(p.getSource());

                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        // Update UI here.
                //PUSH NOTIFICATION TEST
                Image img = new Image("/new_username.png");
                org.controlsfx.control.Notifications.create().owner(getStage())
                        .title("Nouveau nom d'utilisateur").text(u.getPseudo() + "devient " + p.getSource().getPseudo())
                        .graphic(new ImageView(img)).position(Pos.BOTTOM_LEFT).show();

                u.setPseudo(p.getSource().getPseudo());
            }
        });
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

    private void handlerMessage(Packet p){
        Message m=(Message) p;
        System.out.println("From " + p.getSource().getPseudo() + " : " + m.getContenu());
        ArrayList<Message> tmp = new ArrayList<>(getMessageListFromUser(p.getSource()));
        tmp.add(m);
        this.messageLog.remove(p.getSource().getAddress());
        this.messageLog.put(p.getSource().getAddress(),tmp);
    }

    private void handlerListUser(Packet p){
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
                messageLog.put(u.getAddress(), new ArrayList<>());
            }else{
                System.out.println("User en doublon oublie");
            }
        }
        userListSemaphore.release();
    }
}
