package chat.models;

/** Class design to handle communication between
 * Discussion Thread and UnicastManager**/
public class ObserverFlag {

    public enum Flag {
        close, packetReceived;
    }

    private Flag flag;
    private Packet packet;

    public ObserverFlag(Flag flag) {
        this.flag = flag;
        this.packet=null;
    }

    public ObserverFlag(Flag flag, Packet p) {
        this.flag = flag;
        this.packet = p;
    }

    public Flag getFlag() {
        return flag;
    }

    public Packet getPacket() {
        return packet;
    }
}
