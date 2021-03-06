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
    public enum Status {
        online, offline
    }
    private Status status;

    public User(String pseudo, InetAddress address, String ID, Status s) {
        this.pseudo = pseudo;
        this.timeStamp=new Date();
        this.address = address;
        this.UUID = ID;
        this.status = s;
    }

    public String getUUID() {return UUID;}

    public Status getStatus() {return status;}

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

    public void setStatus (Status s){ this.status = s;}

    @Override
    public boolean equals(Object o) {
        if (o instanceof User) {
            User u = (User) o;
            return u.getUUID().equals(this.getUUID());
        }else{
            return false;
        }
    }

    @Override
    public String toString() {
        return "User{" +
                "UUID='" + UUID + '\'' +
                ", pseudo='" + pseudo + '\'' +
                ", timeStamp=" + timeStamp +
                ", address=" + address +
                ", status=" + status +
                '}';
    }
}
