package chat.net;

import chat.models.ObserverFlag;
import chat.models.Packet;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Observable;

public class Discussion extends Observable implements Runnable{
    Socket distant;
    private ObjectOutputStream out;
    private ObjectInputStream in;

    /**
     * Représentation d'une discussion en tcp.
     * @param distant Socket distant
     * @throws IOException Raise exception when socket creation failed.
     */
    Discussion(Socket distant) throws IOException {
        this.distant = distant;
        this.out = new ObjectOutputStream(distant.getOutputStream());
        this.in = new ObjectInputStream(new BufferedInputStream(distant.getInputStream()));
    }

    /** Envois d'un packet
     * Lorsqu'un packet est envoyé, il est envoyé en objet serialized.
     * @param p Packet
     */
    void sendMessage(Packet p){
        try {
            if (!this.distant.isClosed()){
                out.writeObject(p);
            }else{
                System.out.println("Socket closed");
            }
        } catch (IOException e) {
            System.out.println("Erreur à l'envois d'un msg à quelqu'un déjà existant");
        }
    }


    void stop(){
        try {
            this.distant.close();
        } catch (IOException e) {
            System.out.println("Is already stopped");
        }
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
        try {
            while(!this.distant.isClosed()) {
                Packet p = (Packet) in.readObject();
                this.setChanged();
                ObserverFlag observerFlag = new ObserverFlag(ObserverFlag.Flag.packetReceived, p);
                notifyObservers(observerFlag);
                this.clearChanged();
            }
        }catch (IOException | ClassNotFoundException e){
            this.setChanged();
            ObserverFlag observerFlag = new ObserverFlag(ObserverFlag.Flag.close, null);
            notifyObservers(observerFlag);
        }
    }
}
