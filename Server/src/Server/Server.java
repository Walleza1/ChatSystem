package Server;

import chat.models.Notifications;
import chat.models.User;
import chat.models.UserListPacket;
import chat.net.NetworkManager;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.concurrent.Semaphore;

public class Server extends HttpServlet {
    private List<User> listUser;
    private Semaphore userListSemaphore;

    public Server(){
        this.listUser= new ArrayList<>();
        this.userListSemaphore=new Semaphore(1);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        // Setting up the content type of web page
        //get notifications packet
        Notifications notifications=(Notifications) deserialize(req.getParameter("packet"));
        System.out.println("Packet received from "+notifications.getSource().getPseudo());
        ArrayList<User> to_notify= new ArrayList<>();
        switch (notifications.getType()){
            case newUser:
                System.out.println("NewUser received");
                try {
                    userListSemaphore.acquire();
                } catch (InterruptedException e) {
                    System.out.println("Thread stopped");
                }
                if (listUser.contains(notifications.getSource())){
                    listUser.get(listUser.indexOf(notifications.getSource())).setStatus(User.Status.online);
                    if (!listUser.get(listUser.indexOf(notifications.getSource())).getPseudo().equals(notifications.getSource().getPseudo())){
                        listUser.get(listUser.indexOf(notifications.getSource())).setPseudo(notifications.getSource().getPseudo());
                    }
                }else{
                    listUser.add(notifications.getSource());
                }
                to_notify.addAll(listUser);
                userListSemaphore.release();
                break;
            case newPseudo:
                try {
                    userListSemaphore.acquire();
                } catch (InterruptedException e) {
                    System.out.println("Thread stopped");
                }
                System.out.println("NewPseudo received");
                if (listUser.contains(notifications.getSource())){
                    listUser.get(listUser.indexOf(notifications.getSource())).setPseudo(notifications.getSource().getPseudo());
                }else{
                    listUser.add(notifications.getSource());
                }
                for (User u:listUser){
                    if (!u.equals(notifications.getSource())){
                        to_notify.add(u);
                    }
                }
                userListSemaphore.release();
                break;
            case logout:
                System.out.println("Logout received");
                try {
                    userListSemaphore.acquire();
                } catch (InterruptedException e) {
                    System.out.println("Thread stopped");
                }
                if (listUser.contains(notifications.getSource())){
                    listUser.get(listUser.indexOf(notifications.getSource())).setStatus(User.Status.offline);
                }else{
                    listUser.add(notifications.getSource());
                    listUser.get(listUser.indexOf(notifications.getSource())).setStatus(User.Status.offline);
                }
                userListSemaphore.release();
                for (User u:listUser){
                    if (!u.equals(notifications.getSource())){
                        to_notify.add(u);
                    }
                }
                break;
            default:
                break;
        }
        ArrayList<User> tosend = new ArrayList<>(listUser);
        System.out.println(this.listUser);
        for (User u : to_notify){
            UserListPacket userListPacket=new UserListPacket(null,u,tosend);
            Socket distant=new Socket(u.getAddress(), NetworkManager.USERLIST_PORT);
            ObjectOutputStream out=new ObjectOutputStream(distant.getOutputStream());
            out.writeObject(userListPacket);
            out.flush();
            out.close();

            System.out.println("Notified user "+u.getPseudo());
        }
    }

    public void destroy()
    {
        // Leaving empty. Use this if you want to perform
        //something at the end of Servlet life cycle.
    }

    private Object deserialize(String serializedObject){
        byte[] bytes= Base64.getDecoder().decode(serializedObject.getBytes());
        ByteArrayInputStream inputStream=new ByteArrayInputStream(bytes);
        ObjectInputStream objectInputStream;
        Object ret=null;
        try {
            objectInputStream=new ObjectInputStream(inputStream);
            ret=objectInputStream.readObject();
        }catch (IOException | ClassNotFoundException e){
            e.printStackTrace();
        }
        return ret;
    }
}
