package chat.models;

import java.net.InetAddress;
import java.util.ArrayList;

public class UserListPacket extends Packet {

	private static final long serialVersionUID = 1L;
	
	private ArrayList <User> UserList = new ArrayList <User> (); 

	private UserListPacket(User source, User destination, InetAddress addrSource, InetAddress addrDestination) {
		super(source, destination, addrSource, addrDestination);
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
