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
    private String status;

    public User(String pseudo, InetAddress address, String ID, String s) {
        this.pseudo = pseudo;
        this.timeStamp=new Date();
        this.address = address;
        this.UUID = ID;
        this.status = s;
    }

    public String getUUID() {return UUID;}

    public String getStatus() {return status;}

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

    public void setStatus (String s){ this.status = s;}

    @Override
    public boolean equals(Object o) {
        boolean ret=false;
        if (o instanceof User){
            User B=(User) o;
            if (B.getUUID().equals(this.getUUID())){
                ret=true;
            }
        }
        return ret;
    }
}
