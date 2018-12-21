package chat.models;

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

	/**
	 * Create a newPseudoPacket
	 * @param source User Sender
	 * @param destination User Dest
	 * @return Notifications NewPseudoPacket
	 */
	public static Notifications createNewPseudoPacket(User source, User destination) {
		Notifications ret = new Notifications(source, destination);
		ret.type = NotificationType.newPseudo;
		System.out.println(ret.type);
		return ret;
	}

	/**
	 * Create NewUserPacket
	 * @param source User Sender
	 * @param destination User Dest
	 * @return Notifications NewUserPacket
	 */
	public static Notifications createNewUserPacket(User source, User destination) {
		Notifications ret = new Notifications(source, destination);
		ret.type = NotificationType.newUser;
		return ret;
	}

	/**
	 * Create LogOutPacket
	 * @param source User Sender
	 * @param destination User Dest
	 * @return Notifications LogOutPacket
	 */
	public static Notifications createLogOutPacket(User source, User destination) {
		Notifications ret = new Notifications(source, destination);
		ret.type = NotificationType.logout;
		return ret;
	}
}