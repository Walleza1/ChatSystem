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

    public void setPseudo (String s){ this.pseudo = s;}

    @Override
    public boolean equals(Object o) {
        boolean ret=false;
        if (o instanceof User){
            User B=(User) o;
            System.out.println(B.getAddress());
            System.out.println(this.getAddress());
            if (B.getAddress().equals(this.getAddress())){
                ret=true;
            }
        }
        return ret;
    }
}
