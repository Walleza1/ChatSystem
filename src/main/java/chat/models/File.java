package chat.models;

public class File extends Packet {
    private static final long serialVersionUID = 1L;

    public File(User source, User destination) {
        super(source, destination);
    }
}
