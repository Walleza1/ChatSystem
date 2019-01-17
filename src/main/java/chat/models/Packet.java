package chat.models;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

public abstract class Packet implements Serializable {

	private static final long serialVersionUID = 1L;
	
	protected User Source;
    protected User Destination;
    protected String timeStamp;


    public Packet(User source, User destination ){
        Source = source;
        Destination = destination;
        timeStamp = new SimpleDateFormat("dd/MM HH:mm").format(new Date());
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public User getSource() {
        return Source;
    }

    public void setSource(User source) {
        Source = source;
    }

    public User getDestination() {
        return Destination;
    }

    public void setDestination(User destination) {
        Destination = destination;
    }
}
