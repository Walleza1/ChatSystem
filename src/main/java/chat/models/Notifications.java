package chat.models;

import java.net.InetAddress;

public class Notifications extends Packet {
	public enum NotificationType {
		newPseudo, newUser, logout;
	}

	private static final long serialVersionUID = 1L;
	private NotificationType type;


	public Notifications(User source, User destination, InetAddress addrSource, InetAddress addrDestination) {
		super(source, destination, addrSource, addrDestination);
	}

	public Notifications(User source, User destination, InetAddress addrSource, InetAddress addrDestination, NotificationType type) {
		super(source, destination, addrSource, addrDestination);
		this.type = type;
	}

	public NotificationType getType() {
		return type;
	}

	public Notifications createNewPseudoPaquet(User source, User destination, InetAddress addrSource, InetAddress addrDestination) {
		Notifications ret = new Notifications(source, destination, addrSource, addrDestination);
		ret.type = NotificationType.newPseudo;
		return ret;
	}

	public Notifications createNewUserPaquet(User source, User destination, InetAddress addrSource, InetAddress addrDestination) {
		Notifications ret = new Notifications(source, destination, addrSource, addrDestination);
		ret.type = NotificationType.newUser;
		return ret;
	}

	public Notifications createLogOutPaquet(User source, User destination, InetAddress addrSource, InetAddress addrDestination) {
		Notifications ret = new Notifications(source, destination, addrSource, addrDestination);
		ret.type = NotificationType.logout;
		return ret;
	}
}