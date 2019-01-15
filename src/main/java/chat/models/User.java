/**
 * @author : Jérôme Kompé
 */
package chat.models;

import java.io.Serializable;
import java.net.InetAddress;
import java.util.Date;

public class User implements Serializable {
	private static final long serialVersionUID = 1L;
	private String UUID;
    private String pseudo;
    private Date timeStamp;
    private InetAddress address;

    public User(String pseudo, InetAddress address, String ID) {
        this.pseudo = pseudo;
        this.timeStamp=new Date();
        this.address = address;
        this.UUID = ID;
    }

    public String getUUID() {return UUID;}

    public void setUUID(String s) {UUID = s;}

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
            if (B.getAddress().equals(this.getAddress())){
                ret=true;
            }
        }
        return ret;
    }
}
