package chat;

import chat.net.UserListManager;

import java.io.IOException;
import java.net.ServerSocket;

public class TestTCP {
    public static void main(String args[]) throws IOException, InterruptedException {
        UserListManager myManager=new UserListManager(new ServerSocket(6666));
        Thread myThread=new Thread(myManager);
        myThread.start();
        myThread.join();
    }
}
