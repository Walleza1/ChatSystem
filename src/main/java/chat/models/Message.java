package chat.models;

import java.sql.Timestamp;
import java.util.Date;

public class Message extends Packet {

	private static final long serialVersionUID = 1L;

	private int idMessage;
    private String contenu;

	public Message(int idMessage,User source, User destination, String contenu) {
		super(source, destination);
		this.idMessage = idMessage;
		this.contenu = contenu;
	}

	Message(int idMessage, User source, User destination, String contenu, Date ts) {
		super(source, destination);
		this.idMessage = idMessage;
		this.contenu = contenu;
		this.timeStamp = ts;
	}

	public int getIdMessage() {
		return idMessage;
	}
	public String getContenu() {
		return contenu;
	}

}

