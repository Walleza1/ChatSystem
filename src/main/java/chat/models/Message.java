package chat.models;

import java.net.InetAddress;
import java.util.Date;

public class Message extends Packet {

	private static final long serialVersionUID = 1L;
	
	private int idMessage;
    private Date timeStamp;
    private String contenu;

    public Message(User source, User destination, InetAddress addrSource, InetAddress addrDestination) {
        super(source, destination, addrSource, addrDestination);
    }

	public int getIdMessage() {
		return idMessage;
	}
	public Date getTimeStamp() {
		return timeStamp;
	}
	public String getContenu() {
		return contenu;
	}

}

