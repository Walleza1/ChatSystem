package chat.models;

import java.io.Serializable;
import java.net.InetAddress;

public abstract class Packet implements Serializable {

	private static final long serialVersionUID = 1L;
	
	protected User Source;
    protected User Destination;
    protected InetAddress addrSource;
    protected InetAddress addrDestination;

    public Packet(User source, User destination, InetAddress addrSource, InetAddress addrDestination) {
        Source = source;
        Destination = destination;
        this.addrSource = addrSource;
        this.addrDestination = addrDestination;
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

    public InetAddress getAddrSource() {
        return addrSource;
    }

    public void setAddrSource(InetAddress addrSource) {
        this.addrSource = addrSource;
    }

    public InetAddress getAddrDestination() {
        return addrDestination;
    }

    public void setAddrDestination(InetAddress addrDestination) {
        this.addrDestination = addrDestination;
    }
}
