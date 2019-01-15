package chat.models;

import java.net.InetAddress;
import java.sql.*;

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
                    "(IP VARCHAR(20)," +
                    "Username VARCHAR(50) NOT NULL);";
            stmt.executeUpdate(sql);

            sql =  "CREATE TABLE IF NOT EXISTS Messages " +
                    "(ID INT PRIMARY KEY ," +
                    "IP VARCHAR(20) NOT NULL ," +
                    "Content VARCHAR(280));";
            stmt.executeUpdate(sql);

            sql =  "CREATE TABLE IF NOT EXISTS Self " +
                    "(USERNAME VARCHAR(50));";
            stmt.executeUpdate(sql);

            sql =  "CREATE TABLE IF NOT EXISTS IPS " +
                    "(IP VARCHAR(20) PRIMARY KEY);";
            stmt.executeUpdate(sql);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addUser(User u){
        try {
            String sql = "INSERT INTO USERS VALUES (?,?)";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, u.getAddress().toString());
            ps.setString(2, u.getPseudo());
            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addMessage(User distant,  String content){
        try {
            String sql = "INSERT INTO MESSAGES VALUES (?,?,?)";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, distant.getAddress().toString());
            ps.setString(2, content);
            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateUsername(User u){
        try {
            String sql = "UPDATE USERS SET USERNAME = ? WHERE IP = ?;";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, u.getPseudo());
            ps.setString(2, u.getAddress().toString());
            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateIP(User u){
        try {
            String sql = "UPDATE USERS SET IP = ? WHERE USERNAME = ?;";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, u.getAddress().toString());
            ps.setString(2, u.getPseudo());
            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addSelfUsername(User u){
        try {
            String sql = "INSERT INTO SELF VALUES ( ? );";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, u.getPseudo());
            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Boolean isSelfUsernameSet(User u){
        try {
            String sql = "SELECT USERNAME FROM SELF;";
            Statement s = con.createStatement();
            ResultSet rs = s.executeQuery(sql);
            int tmp = 0;
            while(rs.next()) {
                System.out.println("test" + rs.getString("USERNAME"));
                tmp++;
            }
            return tmp != 0;

        } catch (SQLException e) {
            if(!e.getSQLState().equals("02000")){
                e.printStackTrace();
                return false;
            }  else {
                System.out.println("false");
                return false;
            }

        }
    }

    public String getSelf(){
        try {
            String sql = "SELECT * FROM SELF;";
            Statement s = con.createStatement();
            ResultSet rs = s.executeQuery(sql);
            String res=null;
            while(rs.next()) {
                 res = rs.getString("USERNAME");
            }
            return res;

        } catch (SQLException e) {
            e.printStackTrace();
            return "ERROR";
        }
    }

    public void updateSelfUsername(User u){
        try {
            String sql = "UPDATE SELF SET USERNAME = ?;";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, u.getPseudo());
            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean sameAsPreviousIP (User u){
        try {
            boolean res = false;
            String sql = "SELECT * FROM IPS;";
            Statement s = con.createStatement();
            ResultSet rs = s.executeQuery(sql);
            while(rs.next()) {
                if(u.getAddress().toString().equals(rs.getString("IP"))){
                    res = true;
                }
            }
            return res;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }

    public void saveIP (InetAddress addr){
        try {
            String sql = "INSERT INTO IPS VALUES ( ? );";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, addr.toString());
            ps.executeUpdate();

        } catch (SQLException e) {
            System.out.println(e.getSQLState());
            if(!e.getSQLState().equals("23505")){
                e.printStackTrace();
            }
        }
    }

}
