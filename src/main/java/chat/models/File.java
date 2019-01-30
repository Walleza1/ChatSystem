package chat.models;

public class File extends Packet {

    private static final long serialVersionUID = 1L;

    private byte[] content;
    private String name;

    public File(User source, User destination, byte[] content, String n) {
        super(source, destination);
        this.content = content;
        this.name = n;
    }

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[]c){
        this.content = c;
    }

    public String getName(){
        return this.name;
    }
}

