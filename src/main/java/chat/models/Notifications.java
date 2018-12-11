package chat.models;

import java.net.InetAddress;

public class Notifications extends Packet {
	public enum NotificationType {
		newPseudo, newUser, logout;
	}

	private static final long serialVersionUID = 1L;
	private NotificationType type;


	public Notifications(User source, User destination) {
		super(source, destination);
	}

	public Notifications(User source, User destination, NotificationType type) {
		super(source, destination);
		this.type = type;
	}

	public NotificationType getType() {
		return type;
	}

	public static Notifications createNewPseudoPaquet(User source, User destination) {
		Notifications ret = new Notifications(source, destination);
		ret.type = NotificationType.newPseudo;
		return ret;
	}

	public static Notifications createNewUserPaquet(User source, User destination) {
		Notifications ret = new Notifications(source, destination);
		ret.type = NotificationType.newUser;
		return ret;
	}

	public static Notifications createLogOutPaquet(User source, User destination) {
		Notifications ret = new Notifications(source, destination);
		ret.type = NotificationType.logout;
		return ret;
	}
}