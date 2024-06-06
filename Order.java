//TestMain.java 활용해서 실행함
//각 함수들 관리자용/고객용 구분 완료
//인덱스 활용예제 포함 완료

package DB2024Team11;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

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
     * 주문 메뉴를 표시하고 사용자의 선택에 따라 작업 처리
     */
    public void handleAdminOperations() {
        boolean running = true;
        while (running) {
            System.out.println("\n==== Admin Order Menu ====");
            System.out.println("1. Create New Order");
            System.out.println("2. Modify/Delete Order");
            System.out.println("3. Order Confirmation");
            System.out.println("4. Search Orders by Restaurant and Reservation ID");
            System.out.println("5. Exit");
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine();  // 입력 버퍼 비우기

            switch (choice) {
                case 1 -> adminOrderCreation();
                case 2 -> adminOrderModifyOrDelete();
                case 3 -> adminOrderConfirmation();
                case 4 -> searchOrdersByRestaurantAndReservation(); // 인덱스 활용
                case 5 -> running = false;
                default -> System.out.println("Invalid choice. Please enter a number between 1 and 5.");
            }
        }
    }
    
    public void handleCustomerOperations() {
        boolean running = true;
        while (running) {
            System.out.println("\n==== Customer Order Menu ====");
            System.out.println("1. Create New Order");
            System.out.println("2. Request Order Modification/Deletion");
            System.out.println("3. Order Confirmation");
            System.out.println("4. Search Orders by Restaurant and Reservation ID");
            System.out.println("5. Exit");
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine();  // 입력 버퍼 비우기

            switch (choice) {
                case 1 -> customerOrderCreation();
                case 2 -> customerOrderModifyOrDelete();
                case 3 -> customerOrderConfirmation();
                case 4 -> searchOrdersByRestaurantAndReservation(); // 인덱스 활용
                case 5 -> running = false;
                default -> System.out.println("Invalid choice. Please enter a number between 1 and 5.");
            }
        }
    }

    /**
     * 관리자용 주문 생성
     */
    private void adminOrderCreation() {
        System.out.print("\n====Admin Order Creation====\n");
        System.out.print("Enter reservation ID: ");
        int reservationId = scanner.nextInt();
        scanner.nextLine();
        
        System.out.print("Enter restaurant ID: ");
        int restaurantId = scanner.nextInt();
        scanner.nextLine();

        System.out.print("Enter menu ID: ");
        int menuId = scanner.nextInt();
        scanner.nextLine();

        // 현재 시간을 order_time으로 설정
        String orderTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        try {
            String sql = "INSERT INTO DB2024_ORDER (reservation_id, menu_id, restaurant_id, order_time) VALUES (?, ?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, reservationId);
            pstmt.setInt(2, menuId);
            pstmt.setInt(3, restaurantId);
            pstmt.setString(4, orderTime);
            pstmt.executeUpdate();
            System.out.println("Order created successfully.");
            pstmt.close();
        } catch (SQLException e) {
            System.out.println("Error creating order:");
            e.printStackTrace();
        }
    }

    /**
     * 관리자용 주문 수정/삭제
     */
    private void adminOrderModifyOrDelete() {
        System.out.print("\n====Admin Order Modify or Delete====\n");
        System.out.print("Enter order ID: ");
        int orderId = scanner.nextInt();
        scanner.nextLine();

        System.out.print("\n====Select Option====\n");
        System.out.println("1. Modify Order");
        System.out.println("2. Delete Order");
        System.out.print("Enter your choice: ");
        int choice = scanner.nextInt();
        scanner.nextLine();

        if (choice == 1) {
            modifyOrder(orderId);
        } else if (choice == 2) {
            deleteOrder(orderId);
        } else {
            System.out.println("Invalid choice. Please enter 1 or 2.");
        }
    }

    /**
     * 관리자용 주문 확인
     */
    private void adminOrderConfirmation() {
        System.out.print("\n====Admin Order Confirmation====\n");
        System.out.print("Enter order ID: ");
        int orderId = scanner.nextInt();
        scanner.nextLine();

        try {
            String sql = "SELECT * FROM DB2024_ORDER WHERE order_id = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, orderId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                System.out.print("\n====Order Info====\n");
                System.out.println("Order ID: " + rs.getInt("order_id"));
                System.out.println("Reservation ID: " + rs.getInt("reservation_id"));
                System.out.println("Menu ID: " + rs.getInt("menu_id"));
                System.out.println("Restaurant ID: " + rs.getInt("restaurant_id"));
                System.out.println("Order Time: " + rs.getString("order_time"));
            } else {
                System.out.println("Order not found.");
            }

            rs.close();
            pstmt.close();
        } catch (SQLException e) {
            System.out.println("Error confirming order:");
            e.printStackTrace();
        }
    }

    /**
     * 고객용 주문 생성
     */
    private void customerOrderCreation() {
        System.out.print("\n====Customer Order Creation====\n");
        System.out.print("Enter your customer ID: ");
        int customerId = scanner.nextInt();
        scanner.nextLine();

        System.out.print("Enter reservation ID: ");
        int reservationId = scanner.nextInt();
        scanner.nextLine();
        
        System.out.print("Enter restaurant ID: ");
        int restaurantId = scanner.nextInt();
        scanner.nextLine();

        System.out.print("Enter menu ID: ");
        int menuId = scanner.nextInt();
        scanner.nextLine();

        // 현재 시간을 order_time으로 설정
        String orderTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        try {
            // Verify if the reservation belongs to the customer
            String verifySql = "SELECT * FROM DB2024_RESERVATION WHERE reservation_id = ? AND customer_id = ?";
            PreparedStatement verifyPstmt = conn.prepareStatement(verifySql);
            verifyPstmt.setInt(1, reservationId);
            verifyPstmt.setInt(2, customerId);
            ResultSet rs = verifyPstmt.executeQuery();

            if (rs.next()) {
                String sql = "INSERT INTO DB2024_ORDER (reservation_id, menu_id, restaurant_id, order_time) VALUES (?, ?, ?, ?)";
                PreparedStatement pstmt = conn.prepareStatement(sql);
                pstmt.setInt(1, reservationId);
                pstmt.setInt(2, menuId);
                pstmt.setInt(3, restaurantId);
                pstmt.setString(4, orderTime);
                pstmt.executeUpdate();
                System.out.println("Order created successfully.");
                pstmt.close();
            } else {
                System.out.println("Reservation ID does not belong to the customer.");
            }

            rs.close();
            verifyPstmt.close();
        } catch (SQLException e) {
            System.out.println("Error creating order:");
            e.printStackTrace();
        }
    }

    /**
     * 고객용 주문 수정/삭제 요청
     */
    private void customerOrderModifyOrDelete() {
        System.out.print("\n====Customer Order Modify or Delete====\n");
        System.out.print("Enter your customer ID: ");
        int customerId = scanner.nextInt();
        scanner.nextLine();

        System.out.print("Enter order ID: ");
        int orderId = scanner.nextInt();
        scanner.nextLine();

        try {
            // Verify if the order belongs to the customer
            String verifySql = "SELECT o.order_id FROM DB2024_ORDER o JOIN DB2024_RESERVATION r ON o.reservation_id = r.reservation_id WHERE o.order_id = ? AND r.customer_id = ?";
            PreparedStatement verifyPstmt = conn.prepareStatement(verifySql);
            verifyPstmt.setInt(1, orderId);
            verifyPstmt.setInt(2, customerId);
            ResultSet rs = verifyPstmt.executeQuery();

            if (rs.next()) {
                System.out.print("\n====Select Option====\n");
                System.out.println("1. Modify Order");
                System.out.println("2. Delete Order");
                System.out.print("Enter your choice: ");
                int choice = scanner.nextInt();
                scanner.nextLine();

                if (choice == 1) {
                    modifyOrder(orderId);
                } else if (choice == 2) {
                    deleteOrder(orderId);
                } else {
                    System.out.println("Invalid choice. Please enter 1 or 2.");
                }
            } else {
                System.out.println("Order ID does not belong to the customer.");
            }

            rs.close();
            verifyPstmt.close();
        } catch (SQLException e) {
            System.out.println("Error modifying/deleting order:");
            e.printStackTrace();
        }
    }

    /**
     * 고객용 주문 확인
     */
    private void customerOrderConfirmation() {
        System.out.print("\n====Customer Order Confirmation====\n");
        System.out.print("Enter your customer ID: ");
        int customerId = scanner.nextInt();
        scanner.nextLine();

        System.out.print("Enter restaurant ID: ");
        int restaurantId = scanner.nextInt();
        scanner.nextLine();

        try {
            String sql = "SELECT o.* FROM DB2024_ORDER o JOIN DB2024_RESERVATION r ON o.reservation_id = r.reservation_id WHERE r.customer_id = ? AND o.restaurant_id = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, customerId);
            pstmt.setInt(2, restaurantId);
            ResultSet rs = pstmt.executeQuery();

            System.out.print("\n====Order List====\n");
            while (rs.next()) {
                System.out.println("Order ID: " + rs.getInt("order_id"));
                System.out.println("Reservation ID: " + rs.getInt("reservation_id"));
                System.out.println("Menu ID: " + rs.getInt("menu_id"));
                System.out.println("Restaurant ID: " + rs.getInt("restaurant_id"));
                System.out.println("Order Time: " + rs.getString("order_time"));
                System.out.println("------------------------");
            }

            rs.close();
            pstmt.close();
        } catch (SQLException e) {
            System.out.println("Error confirming order:");
            e.printStackTrace();
        }
    }

    /**
     * 특정 식당과 예약번호로 주문 검색 (인덱스 활용)
     */
    private void searchOrdersByRestaurantAndReservation() {
        System.out.print("\n====Search Using Index====\n");
        System.out.print("Enter restaurant ID: ");
        int restaurantId = scanner.nextInt();
        scanner.nextLine();

        System.out.print("Enter reservation ID: ");
        int reservationId = scanner.nextInt();
        scanner.nextLine();

        try {
            String sql = "SELECT * FROM DB2024_ORDER WHERE restaurant_id = ? AND reservation_id = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, restaurantId);
            pstmt.setInt(2, reservationId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                System.out.println("\n------------------------");
                System.out.print("\n====Order Info====\n");
                System.out.println("Order ID: " + rs.getInt("order_id"));
                System.out.println("Reservation ID: " + rs.getInt("reservation_id"));
                System.out.println("Menu ID: " + rs.getInt("menu_id"));
                System.out.println("Restaurant ID: " + rs.getInt("restaurant_id"));
                System.out.println("Order Time: " + rs.getString("order_time"));
            }
            System.out.println("\n------------------------");

            rs.close();
            pstmt.close();
        } catch (SQLException e) {
            System.out.println("Error searching orders:");
            e.printStackTrace();
        }
    }

    /**
     * 주문 수정
     */
    private void modifyOrder(int orderId) {
        System.out.print("\n====Order Modification====\n");
        System.out.print("Enter new menu ID: ");
        int newMenuId = scanner.nextInt();
        scanner.nextLine();
        
        // 현재 시간을 order_time으로 설정
        String newOrderTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        try {
            String sql = "UPDATE DB2024_ORDER SET menu_id = ?, order_time = ? WHERE order_id = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, newMenuId);
            pstmt.setString(2, newOrderTime);
            pstmt.setInt(3, orderId);
            pstmt.executeUpdate();
            System.out.println("Order updated successfully.");
            pstmt.close();
        } catch (SQLException e) {
            System.out.println("Error updating order:");
            e.printStackTrace();
        }
    }

    /**
     * 주문 삭제
     */
    private void deleteOrder(int orderId) {
        try {
            String sql = "DELETE FROM DB2024_ORDER WHERE order_id = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, orderId);
            pstmt.executeUpdate();
            System.out.println("Order deleted successfully.");
            pstmt.close();
        } catch (SQLException e) {
            System.out.println("Error deleting order:");
            e.printStackTrace();
        }
    }
}
