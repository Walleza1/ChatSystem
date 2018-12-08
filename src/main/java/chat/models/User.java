/**
 * @author : Jérôme Kompé
 */
package chat.models;

import java.io.Serializable;
import java.net.InetAddress;
import java.util.Date;

public class User implements Serializable {
    private int idUser;
    private String pseudo;
    private Date timeStamp;
    private InetAddress address;

    public User(int idUser, String pseudo, Date timeStamp, InetAddress address) {
        this.idUser = idUser;
        this.pseudo = pseudo;
        this.timeStamp = timeStamp;
        this.address = address;
    }

    public User(int idUser, String pseudo, InetAddress address) {
        this.idUser = idUser;
        this.pseudo = pseudo;
        this.timeStamp=new Date();
        this.address = address;
    }

    public int getIdUser() {
        return idUser;
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

   /* @Override
    public String toJson() {
        return "{" +
                "\"idUser:\":"+this.idUser+","+
                "\"pseudo:\":\""+this.pseudo+"\","+
                "\"date\":\""+this.timeStamp.toString()+"\","+
                "\"address\":\""+this.address.toString()+"\","+
                "}";
    }*/
}
