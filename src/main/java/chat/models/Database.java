package chat.models;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

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
        System.out.println("Created table in given database...");
    }

    private void tryToCreateTables(){
        try {
            Statement stmt = con.createStatement();
            String sql =  "CREATE TABLE IF NOT EXISTS Users (id INT NOT NULL);";
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
