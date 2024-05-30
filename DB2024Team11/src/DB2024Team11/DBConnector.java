package DB2024Team11;
import java.io.IOException;
import java.sql.DriverManager;
import java.sql.SQLException;

import java.sql.Connection;
/*
 * 작성자 : 박민경(2117016)
 * Script: MySQL로의 Connection을 위한 클래스.
*/
public class DBConnector {
    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    static final String url = "jdbc:mysql://localhost/DB2024Team11";
    static final String username = "DB2024Team11";
    static final String password = "DB2024Team11";
    private static Connection conn;


    /**
     * 데이터베이스에 연결을 설정합니다.
     *
     * @return 연결이 성공적으로 이루어지면 Connection 객체를 반환하고, 실패하면 null반환.
     * @throws SQLException 데이터베이스 접근 오류가 발생할 경우
     * @throws IOException I/O 오류가 발생할 경우
     */
    public static Connection make_connection() throws SQLException, IOException {
        if (conn != null) {
            return conn;
        }
        try {
            Class.forName(JDBC_DRIVER);
        } catch (ClassNotFoundException e) {
            System.out.println("드라이버를 로드할 수 없습니다");
            System.out.println("메시지: " + e.getMessage());
            return null;
        }
        try {
            conn = DriverManager.getConnection(url, username, password);
        } catch (SQLException e) {
            System.out.println("연결을 설정할 수 없습니다");
            System.out.println("메시지: " + e.getMessage());
            throw e;
        }
        return conn;
    }

    /**
     * 데이터베이스 연결 종료.
     */
    public static void close_connection() {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                System.out.println("연결을 종료할 수 없습니다");
                System.out.println("메시지: " + e.getMessage());
            } finally {
                conn = null;
            }
        }
    }
}