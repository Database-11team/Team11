package DB2024Team11;
import java.io.IOException;
import java.sql.DriverManager;
import java.sql.SQLException;

import java.sql.Connection;
/*
작성자 : 박민경(2117016)
Script: MySQL로의 Connection을 위한 클래스.
*/
public class DBConnector {
    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    static final String url = "jdbc:mysql://localhost/DB2024Team11";
    static final String username = "DB2024Team11";
    static final String password = "DB2024Team11";
    private static Connection conn;


    /**
     * @return true if the connection was successfully made
     * @throws SQLException
     * @throws IOException
     */
    public static Connection make_connection() throws SQLException, IOException {
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("Could not load the driver");

            System.out.println("Message: " + e.getMessage());

            return null;
        }
        conn = DriverManager.getConnection(url,username,password);
        return conn;
    }
}
