package chat.net;

import chat.models.ObserverFlag;
import chat.models.Packet;

import java.io.*;
import java.net.Socket;
import java.util.Observable;

public class Discussion extends Observable implements Runnable{
    public Socket distant;
    private ObjectOutputStream out;
    private ObjectInputStream in;

    /**
     * Représentation d'une discussion en tcp.
     * @param distant
     * @throws IOException
     */
    public Discussion(Socket distant) throws IOException {
        this.distant = distant;
        this.out = new ObjectOutputStream(distant.getOutputStream());
        this.in = new ObjectInputStream(new BufferedInputStream(distant.getInputStream()));
    }

    /** Envois d'un packet
     * Lorsqu'un packet est envoyé, il est envoyé en objet serialized.
     * @param p
     * @throws IOException
     */
    public void sendMessage(Packet p) throws IOException {
        out.writeObject(p);
    }

    /**
     * Socket en attente d'un packet.
     * Lorsque le Socket est fermé
     *  - Par le correspondant.
     *  - A cause d'une erreur TCP.
     * Je notifie mon observateur grâce à l'object ObserverFlag.
     */
    @Override
    public void run() {
        while(!this.distant.isClosed()){
            try {
                Packet p = (Packet) in.readObject();
                this.setChanged();
                ObserverFlag observerFlag=new ObserverFlag(ObserverFlag.Flag.packetReceived,p);
                notifyObservers(observerFlag);
                this.clearChanged();
            }catch (Exception e) {
                try {
                    this.distant.close();
                    ObserverFlag observerFlag=new ObserverFlag(ObserverFlag.Flag.close);
                    notifyObservers(observerFlag);
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }
}
