// Order.java
package DB2024Team11;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Date;
import java.sql.Time;
import java.util.Scanner;

/**
 * Order 클래스는 주문을 관리하는 기능을 제공합니다.
 */
public class Order {
    private final Connection conn;
    private final Scanner scanner;

    /**
     * Order 클래스의 생성자.
     *
     * @param conn    데이터베이스 연결 객체
     * @param scanner 사용자 입력을 받기 위한 Scanner 객체
     */
    public Order(Connection conn, Scanner scanner) {
        this.conn = conn;
        this.scanner = scanner;
    }

    /**
     * 주문 생성 처리
     */
    public void adminOrderCreation() {
        System.out.print("Enter reservation ID: ");
        int reservationId = scanner.nextInt();
        scanner.nextLine(); // 개행

        createOrder(reservationId);
    }

    /**
     * 주문 생성
     *
     * @param reservationId 예약 ID
     */
    private void createOrder(int reservationId) {
        System.out.print("Enter menu ID: ");
        int menuId = scanner.nextInt();
        scanner.nextLine(); // 개행

        System.out.print("Enter restaurant ID: ");
        int restaurantId = scanner.nextInt();
        scanner.nextLine(); // 개행

        try {
            String sql = "INSERT INTO DB2024_ORDER (menu_id, restaurant_id, reservation_id, order_time) VALUES (?, ?, ?, NOW())";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, menuId);
            pstmt.setInt(2, restaurantId);
            pstmt.setInt(3, reservationId);
            pstmt.executeUpdate();
            pstmt.close();

            System.out.println("Order created successfully.");
        } catch (SQLException e) {
            System.out.println("Error creating order:");
            e.printStackTrace();
        }
    }

    /**
     * 관리자 주문 변경/삭제 처리
     */
    public void adminOrderModifyOrDelete() {
        System.out.print("Enter reservation ID: ");
        int reservationId = scanner.nextInt();
        scanner.nextLine(); // 개행

        System.out.print("Enter order ID: ");
        int orderId = scanner.nextInt();
        scanner.nextLine(); // 개행

        System.out.println("1. Modify Order");
        System.out.println("2. Delete Order");
        System.out.print("Enter your choice: ");
        int choice = scanner.nextInt();
        scanner.nextLine(); // 개행

        switch (choice) {
            case 1 -> modifyOrder(orderId);
            case 2 -> deleteOrder(orderId);
            default -> System.out.println("Invalid choice. Please enter 1 or 2.");
        }
    }

    /**
     * 주문 수정
     *
     * @param orderId 주문 ID
     */
    private void modifyOrder(int orderId) {
        System.out.print("Enter new menu ID: ");
        int newMenuId = scanner.nextInt();
        scanner.nextLine(); // 개행

        try {
            String sql = "UPDATE DB2024_ORDER SET menu_id = ? WHERE order_id = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, newMenuId);
            pstmt.setInt(2, orderId);
            pstmt.executeUpdate();
            pstmt.close();

            System.out.println("Order modified successfully.");
        } catch (SQLException e) {
            System.out.println("Error modifying order:");
            e.printStackTrace();
        }
    }

    /**
     * 주문 삭제
     *
     * @param orderId 주문 ID
     */
    private void deleteOrder(int orderId) {
        try {
            String sql = "DELETE FROM DB2024_ORDER WHERE order_id = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, orderId);
            pstmt.executeUpdate();
            pstmt.close();

            System.out.println("Order deleted successfully.");
        } catch (SQLException e) {
            System.out.println("Error deleting order:");
            e.printStackTrace();
        }
    }

    /**
     * 관리자 주문 확인 처리
     */
    public void adminOrderConfirmation() {
        System.out.print("Enter reservation ID: ");
        int reservationId = scanner.nextInt();
        scanner.nextLine(); // 개행

        viewOrders(reservationId);
    }

    /**
     * 주문 조회
     *
     * @param reservationId 예약 ID
     */
    private void viewOrders(int reservationId) {
        try {
            String sql = "SELECT * FROM DB2024_ORDER WHERE reservation_id = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, reservationId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                System.out.println("Order ID: " + rs.getInt("order_id"));
                System.out.println("Menu ID: " + rs.getInt("menu_id"));
                System.out.println("Restaurant ID: " + rs.getInt("restaurant_id"));
                System.out.println("Reservation ID: " + rs.getInt("reservation_id"));
                System.out.println("Order Time: " + rs.getTimestamp("order_time"));
                System.out.println();
            }

            rs.close();
            pstmt.close();
        } catch (SQLException e) {
            System.out.println("Error retrieving orders:");
            e.printStackTrace();
        }
    }
}
