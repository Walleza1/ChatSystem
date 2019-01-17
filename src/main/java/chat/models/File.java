package chat.models;

public class File extends Packet {
    private static final long serialVersionUID = 1L;

    private java.io.File content;

    public File(User source, User destination, java.io.File content) {
        super(source, destination);
        this.content = content;
    }

    public java.io.File getContent() {
        return content;
    }
}
