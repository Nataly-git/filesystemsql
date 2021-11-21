package test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBWorker {
    private final String URL = "jdbc:mysql://localhost:3306/mydbtest";
    private final String USER_NAME = "root";
    private final String PASSWORD = "root";
    private Connection connection;

    public DBWorker() {
            try {
                connection = DriverManager.getConnection(URL, USER_NAME, PASSWORD);
            } catch (SQLException e) {
                e.printStackTrace();
            }
    }

    public Connection getConnection() {
        return connection;
    }

    public void closeConnection(){
        try{
        if (connection != null && !connection.isClosed())
                connection.close();
            } catch (SQLException e){
                e.printStackTrace();
            }
    }
}
