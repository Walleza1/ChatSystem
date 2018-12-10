package chat.models;

public class Notification {

	private enum Type {
		  newPseudo,
		  newUser,
		  logout;	
		}
	
	Type type ;

	public Type getType() {
		return type;
	}	
}
