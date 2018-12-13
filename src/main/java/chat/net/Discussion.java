package chat.net;

import chat.models.Packet;

import java.io.*;
import java.net.Socket;
import java.util.Observable;

public class Discussion extends Observable implements Runnable{
    public Socket distant;
    private ObjectOutputStream out;
    private ObjectInputStream in;

    public Discussion(Socket distant) throws IOException {
        this.distant = distant;
        this.out = new ObjectOutputStream(distant.getOutputStream());
        this.in = new ObjectInputStream(new BufferedInputStream(distant.getInputStream()));
    }


    public void sendMessage(Packet p) throws IOException {
        out.writeObject(p);
    }

    @Override
    public void run() {
        while(!this.distant.isClosed()){
            try {
                Packet p = (Packet) in.readObject();
                this.setChanged();
                notifyObservers(p);
                this.clearChanged();
            }catch (Exception e) {
                try {
                    this.distant.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }
}
