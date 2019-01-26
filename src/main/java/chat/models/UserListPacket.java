package chat.models;

import java.util.ArrayList;

public class UserListPacket extends Packet {

	private static final long serialVersionUID = 1L;

	private ArrayList <User> UserList;

	public UserListPacket(User source, User destination, ArrayList<User> userList) {
		super(source, destination);
		UserList = userList;
	}

	public ArrayList<User> getUserList() {
		return UserList;
	}
}
