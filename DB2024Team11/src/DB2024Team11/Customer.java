package DB2024Team11;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

/**
 * Customer 클래스는 고객의 정보를 처리하는 기능 제공.
 */
public class Customer {
    private Connection conn;
    private Scanner scanner;

    /**
     * Customer 클래스의 생성자.
     *
     * @param conn    데이터베이스 연결 객체
     * @param scanner 사용자 입력을 받기 위한 Scanner 객체
     */
    public Customer(Connection conn, Scanner scanner) {
        this.conn = conn;
        this.scanner = scanner;
    }

    /**
     * 고객 메뉴를 표시하고 사용자의 선택에 따라 작업 처리
     */
    public void handleOperations() {
        boolean running = true;
        while (running) {
            System.out.println("\n==== Customer Menu ====");
            System.out.println("1. View Customer Information");
            System.out.println("2. Back");

            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine();  // 입력 버퍼 비우기

            switch (choice) {
                case 1:
                    getCustomerInfo();
                    break;
                case 2:
                    running = false;
                    break;
                default:
                    System.out.println("Invalid choice. Please enter a number between 1 and 2.");
            }
        }
    }

    /**
     * 사용자로부터 고객 이름과 전화번호를 입력받아 데이터베이스에서 해당 고객 정보를 조회.
     */
    private void getCustomerInfo() {
        System.out.print("Enter customer name: ");
        String customerName = scanner.nextLine();

        System.out.print("Enter phone number: ");
        String phoneNumber = scanner.nextLine();

        try {
            String sql = "SELECT * FROM DB2024_CUSTOMER WHERE customer_name = ? AND phone_number = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, customerName);
            pstmt.setString(2, phoneNumber);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                System.out.println("Customer ID: " + rs.getInt("customer_id"));
                System.out.println("Customer Name: " + rs.getString("customer_name"));
                System.out.println("Birthday: " + rs.getDate("birthday"));
                System.out.println("Phone Number: " + rs.getString("phone_number"));
            } else {
                System.out.println("Customer not found.");
            }

            rs.close();
            pstmt.close();
        } catch (SQLException e) {
            System.out.println("Error retrieving customer information:");
            e.printStackTrace();
        }
    }
}
