package chat.models;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.*;
import java.util.ArrayList;
import java.util.UUID;

public class Database {

    private Connection con = null;

    private static Database INSTANCE = null;

    public static Database getInstance(){
        if (INSTANCE == null){
            INSTANCE = new Database();
        }
        return INSTANCE;
    }

    private Database(){

        try {
            Class.forName("org.h2.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        try {
            con = DriverManager.getConnection ("jdbc:h2:~/Clavardage/db", "toto","toto");

        } catch (SQLException e) {
            e.printStackTrace();
        }

        tryToCreateTables();
        System.out.println("Created tables in database...");
    }

    private void tryToCreateTables(){
        try {
            Statement stmt = con.createStatement();
            String sql =  "CREATE TABLE IF NOT EXISTS Users " +
                    "(UUID VARCHAR(32) PRIMARY KEY," +
                    "Username VARCHAR(50) NOT NULL);";
            stmt.executeUpdate(sql);

            sql =  "CREATE TABLE IF NOT EXISTS Messages " +
                    "(SENDER VARCHAR(32), " +
                    "RECEIVER VARCHAR(32), " +
                    "Content VARCHAR(280), " +
                    "Timestamp TIME);";

            stmt.executeUpdate(sql);

            sql =  "CREATE TABLE IF NOT EXISTS Self " +
                    "(UUID VARCHAR(32) PRIMARY KEY);";
            stmt.executeUpdate(sql);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private boolean UUIDisSet(){
        try {
            String sql = "SELECT UUID FROM SELF;";
            Statement s = con.createStatement();
            ResultSet rs = s.executeQuery(sql);
            int tmp = 0;
            while(rs.next()) {
                tmp++;
            }
            return tmp != 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean UUIDNotInUsers(String s){
        try {
            String sql = "SELECT UUID FROM USERS WHERE UUID = '" + s + "';";
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(sql);
            int tmp = 1;
            while(rs.next()) {

                tmp++;
            }
            return tmp == 1;

        } catch (SQLException e) {
            e.printStackTrace();
            return true;
        }
    }

    private String UUIDfromDB(){
        try {
            String sql = "SELECT UUID FROM SELF;";
            Statement s = con.createStatement();
            ResultSet rs = s.executeQuery(sql);
            String res=null;
            while(rs.next()) {
                res = rs.getString("UUID");
            }
            return res;

        } catch (SQLException e) {
            e.printStackTrace();
            return "ERROR";
        }
    }

    private void storeUUID(String ID){
        try {
            String sql = "INSERT INTO SELF VALUES (?)";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, ID);
            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public String getUUID(){
        String ID;
        if(UUIDisSet()){
            ID = UUIDfromDB();
        } else {
            ID = UUID.randomUUID().toString().replace("-","");
            storeUUID(ID);
        }
        return ID;
    }

    public void addUser(User u){
        try {
            String sql = "INSERT INTO USERS VALUES (?,?)";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, u.getUUID());
            ps.setString(2, u.getPseudo());
            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void updateUsername(User u){
        try {
            String sql = "UPDATE USERS SET USERNAME = ? WHERE UUID = ?;";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, u.getPseudo());
            ps.setString(2, u.getUUID());
            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<User> getUsers(){
        try {
        String sql = "SELECT * FROM USERS";
        ResultSet rs = con.createStatement().executeQuery(sql);

        ArrayList <User> users = new ArrayList<>();

        while(rs.next()){
            User u = new User(rs.getString("USERNAME"), InetAddress.getByName("1.1.1.1"),
                    rs.getString("UUID"),User.Status.offline);
            users.add(u);
        }
            return users ;
        } catch (SQLException | UnknownHostException e) {
            e.printStackTrace();
            return null;
        }
    }

    public User getUserFromUUID(String ID){
        try {
            String sql = "SELECT * FROM USERS " +
                    "WHERE UUID = '" + ID + "';";

            ResultSet rs = con.createStatement().executeQuery(sql);
            User u = null;
            while(rs.next()){
                u = new User(rs.getString("USERNAME"), InetAddress.getByName("1.1.1.1"),
                        rs.getString("UUID"),User.Status.online);
            }
            return u;
        } catch (SQLException | UnknownHostException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void addMessage(User send, User dest,  Message m){
        try {
            String sql = "INSERT INTO MESSAGES VALUES (?,?,?,?)";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, send.getUUID());
            ps.setString(2, dest.getUUID());
            ps.setString(3, m.getContenu());
            ps.setTimestamp(4, new Timestamp(m.getTimeStamp().getTime()));
            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ArrayList <Message> getConv(User self, User u){
        try {
        String sql = "SELECT * FROM MESSAGES WHERE " +
                "(SENDER = ? AND RECEIVER = ?) OR (RECEIVER = ? AND SENDER = ?) " +
                "ORDER BY TIMESTAMP ASC";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setString(1, self.getUUID());
        ps.setString(2, u.getUUID());
        ps.setString(3, self.getUUID());
        ps.setString(4, u.getUUID());
        ResultSet rs;

            rs = ps.executeQuery();

        ArrayList<Message> messages = new ArrayList<>();

        while (rs.next()) {

            Message m = new Message(0,getUserFromUUID(rs.getString("SENDER")),
                    getUserFromUUID(rs.getString("RECEIVER")),
                    rs.getString("CONTENT"), rs.getTimestamp("TIMESTAMP"));
            messages.add(m);
        }
        return messages;

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

}
