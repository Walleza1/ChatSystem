package chat.models;

import java.util.ArrayList;

public class UserListPacket extends Packet {

	private static final long serialVersionUID = 1L;

	private ArrayList <User> UserList = new ArrayList <User> ();

	public UserListPacket(User source, User destination, ArrayList<User> userList) {
		super(source, destination);
		UserList = userList;
	}

	public ArrayList<User> getUserList() {
		return UserList;
	}

	public void setUserList(ArrayList<User> userList) {
		UserList = userList;
	}	

	public void addToList(ArrayList<User> list , User u) {
		list.add(u);
	}
}
