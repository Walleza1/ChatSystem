package chat.listeners;

import java.net.Socket;
import java.util.Observable;

public class DiscussionListener extends Observable implements Runnable {
    public Socket distant;

    public DiscussionListener(Socket distant) {
        this.distant = distant;
    }

    @Override
    public void run() {
        //TODO
    }
}
