package chat.models;

import java.net.InetAddress;
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

	public int getIdMessage() {
		return idMessage;
	}
	public String getContenu() {
		return contenu;
	}

}

