package chat.models;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

public abstract class Packet implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private User Source;
    private User Destination;
    String timeStamp;


    public Packet(User source, User destination ){
        Source = source;
        Destination = destination;
        timeStamp = new SimpleDateFormat("dd/MM HH:mm").format(new Date());
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public User getSource() {
        return Source;
    }

    public User getDestination() {
        return Destination;
    }
}
