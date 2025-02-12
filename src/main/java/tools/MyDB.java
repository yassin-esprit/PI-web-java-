package tools;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MyDB {
    public final String URL = "jdbc:mysql://localhost:3306/dbpi";
    public final String USER = "root";
    public final String pwd = "";
    private Connection connection;
    private static MyDB myDB;

    private MyDB() {
        try {
            connection = DriverManager.getConnection(URL, USER, pwd);
            System.out.println("Connection établie");
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    public static MyDB getInstance() {
        if (myDB == null) {
            myDB = new MyDB();
        }
        return myDB;
    }

    public Connection getConnection() {
        return connection;
    }
}