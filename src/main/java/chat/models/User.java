/**
 * @author : Jérôme Kompé
 */
package chat.models;

import java.io.Serializable;
import java.net.InetAddress;
import java.util.Date;

public class User implements Serializable {
	private static final long serialVersionUID = 1L;
    private String pseudo;
    private Date timeStamp;
    private InetAddress address;

    public User(String pseudo, Date timeStamp, InetAddress address) {
        this.pseudo = pseudo;
        this.timeStamp = timeStamp;
        this.address = address;
    }

    public User(String pseudo, InetAddress address) {
        this.pseudo = pseudo;
        this.timeStamp=new Date();
        this.address = address;
    }

    public String getPseudo() {
        return pseudo;
    }

    public Date getTimeStamp() {
        return timeStamp;
    }

    public InetAddress getAddress() {
        return address;
    }

}
