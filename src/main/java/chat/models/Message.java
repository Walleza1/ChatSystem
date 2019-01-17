package chat.models;

public class Message extends Packet {

	private static final long serialVersionUID = 1L;

    private String contenu;

	public Message(User source, User destination, String contenu) {
		super(source, destination);
		this.contenu = contenu;
	}

	Message(User source, User destination, String contenu, String ts) {
		super(source, destination);
		this.contenu = contenu;
		this.timeStamp = ts;
	}

	public String getContenu() {
		return contenu;
	}

}

