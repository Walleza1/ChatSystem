package Server;

import chat.models.Notifications;
import chat.models.User;
import chat.models.UserListPacket;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;

import static chat.models.Notifications.createNewUserPacket;

public class Server extends HttpServlet {
    private String mymsg;
    private List<User> listUser;
    private Semaphore userListSemaphore;

    public Server(){
        this.listUser=new ArrayList<User>();
        this.userListSemaphore=new Semaphore(1);
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Setting up the content type of web page
        //get notifications packet
        Notifications notifications= createNewUserPacket(null,null);
        ArrayList<User> to_notify=new ArrayList<User>();

        switch (notifications.getType()){
            case newUser:
                try {
                    userListSemaphore.acquire();
                } catch (InterruptedException e) {
                    System.out.println("Thread stopped");
                }
                if (listUser.contains(notifications.getSource())){
                    listUser.get(listUser.indexOf(notifications.getSource())).setStatus(User.Status.online);
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
        for (User u : to_notify){
            UserListPacket userListPacket=new UserListPacket(null,u,to_notify);
        }
    }
    public void destroy()
    {
        // Leaving empty. Use this if you want to perform
        //something at the end of Servlet life cycle.
    }
}
