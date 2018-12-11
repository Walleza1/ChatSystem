package chat.models;

import java.net.InetAddress;

public class File extends Packet {
    private static final long serialVersionUID = 1L;

    public File(User source, User destination) {
        super(source, destination);
    }
}
