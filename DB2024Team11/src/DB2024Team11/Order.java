
//TestMain.java 활용해서 실행함
//각 함수들 관리자용/고객용 구분 완료
//입력 받는 매개변수 동적쿼리, 중첩, 인덱스, 조인, 트랜잭션, SELECT, INSERT, UPDATE, DELETE문 활용된 부분 표시 완료

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
     * 주문 메뉴를 표시하고 사용자의 선택에 따라 작업 처리 (매개변수를 갖는 동적 쿼리 - 입력 받기)
     */
    public void handleAdminOperations() {
        boolean running = true;
        while (running) {
            System.out.println("\n====[Admin Order Menu]====");
            System.out.println("1. Create New Order");
            System.out.println("2. Modify/Delete Order");
            System.out.println("3. Order Detail");
            System.out.println("4. Search Orders by Restaurant and Reservation ID");
            System.out.println("5. Search Orders by Location and Category");
            System.out.println("6. Exit");
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt(); // 입력 받기
            scanner.nextLine();  // 입력 버퍼 비우기

            switch (choice) {
                case 1 -> adminOrderCreation();
                case 2 -> adminOrderModifyOrDelete();
                case 3 -> adminOrderConfirmation();
                case 4 -> searchOrdersByRestaurantAndReservation(); // 인덱스 활용
                case 5 -> searchOrdersByLocationAndCategory(); // 중첩 활용
                case 6 -> running = false;
                default -> System.out.println("Invalid choice. Please enter a number between 1 and 6.");
            }
        }
    }

    public void handleCustomerOperations() {
        boolean running = true;
        while (running) {
            System.out.println("\n====[Customer Order Menu]====");
            System.out.println("1. Create New Order");
            System.out.println("2. Request Order Modification/Deletion");
            System.out.println("3. Order Detail");
            System.out.println("4. Search Orders by Restaurant and Reservation ID");
            System.out.println("5. Search Orders by Location and Category");
            System.out.println("6. Exit");
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt(); // 입력 받기
            scanner.nextLine();  // 입력 버퍼 비우기

            switch (choice) {
                case 1 -> customerOrderCreation(); //INSERT, SELECT, UPDATE, 트랜잭션 활용
                case 2 -> customerOrderModifyOrDelete();
                case 3 -> customerOrderConfirmation();
                case 4 -> searchOrdersByRestaurantAndReservation(); // 인덱스 활용
                case 5 -> searchOrdersByLocationAndCategory(); // 중첩 활용
                case 6 -> running = false;
                default -> System.out.println("Invalid choice. Please enter a number between 1 and 5.");
            }
        }
    }

    /**
     * 관리자용 주문 생성 (INSERT, SELECT, UPDATE문 활용, 트랜잭션 활용)
     */
    private void adminOrderCreation() {
        System.out.print("\n----Admin Order Creation----\n");

        System.out.print("Enter restaurant ID: ");
        int restaurantId = scanner.nextInt(); // 입력 받기
        scanner.nextLine();

        System.out.print("Enter reservation ID: ");
        int reservationId = scanner.nextInt(); // 입력 받기
        scanner.nextLine();

        System.out.print("Enter menu ID: ");
        int menuId = scanner.nextInt(); // 입력 받기
        scanner.nextLine();

        // 현재 시간을 order_time으로 설정
        String orderTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        try {
            conn.setAutoCommit(false); // 트랜잭션 시작

            // 주문 생성
            String orderSql = "INSERT INTO DB2024_ORDER (reservation_id, menu_id, restaurant_id, order_time) VALUES (?, ?, ?, ?)";
            PreparedStatement orderPstmt = conn.prepareStatement(orderSql);
            orderPstmt.setInt(1, reservationId);
            orderPstmt.setInt(2, menuId);
            orderPstmt.setInt(3, restaurantId);
            orderPstmt.setString(4, orderTime);
            orderPstmt.executeUpdate();

            // 메뉴 재고 확인 및 감소
            String stockSql = "SELECT stock FROM DB2024_MENU WHERE menu_id = ?";
            PreparedStatement stockPstmt = conn.prepareStatement(stockSql);
            stockPstmt.setInt(1, menuId);
            ResultSet rs = stockPstmt.executeQuery();

            if (rs.next()) {
                int stock = rs.getInt("stock");
                if (stock >= 1) {
                    String updateStockSql = "UPDATE DB2024_MENU SET stock = stock - 1 WHERE menu_id = ?";
                    PreparedStatement updateStockPstmt = conn.prepareStatement(updateStockSql);
                    updateStockPstmt.setInt(1, menuId);
                    updateStockPstmt.executeUpdate();
                    updateStockPstmt.close();
                } else {
                    conn.rollback();
                    System.out.println("Stock is insufficient.");
                    return;
                }
            } else {
                conn.rollback();
                System.out.println("Menu not found.");
                return;
            }

            rs.close();
            stockPstmt.close();
            orderPstmt.close();

            conn.commit(); // 트랜잭션 커밋
            System.out.println("Order created successfully.");
        } catch (SQLException e) {
            try {
                conn.rollback(); // 트랜잭션 롤백
            } catch (SQLException rollbackException) {
                rollbackException.printStackTrace();
            }
            System.out.println("Error creating order:");
            e.printStackTrace();
        } finally {
            try {
                conn.setAutoCommit(true); // 자동 커밋 모드로 전환
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 관리자용 주문 수정/삭제 (매개변수를 갖는 동적 쿼리 - 입력 받기)
     */
    private void adminOrderModifyOrDelete() {
        System.out.print("\n----Admin Order Modify or Delete----\n");
        System.out.print("Enter order ID: ");
        int orderId = scanner.nextInt(); // 입력 받기
        scanner.nextLine();

        System.out.print("\n----Select Option----\n");
        System.out.println("1. Modify Order");
        System.out.println("2. Delete Order");
        System.out.print("Enter your choice: ");
        int choice = scanner.nextInt(); // 입력 받기
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
     * 관리자용 주문 확인 (SELECT문 활용)
     */
    private void adminOrderConfirmation() {
        System.out.print("\n----Admin Order Detail----\n");
        System.out.print("Enter order ID: ");
        int orderId = scanner.nextInt(); // 입력 받기
        scanner.nextLine();

        try {
            String sql = "SELECT * FROM DB2024_ORDER WHERE order_id = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, orderId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                System.out.print("\n----Order Info----\n");
                System.out.println("Restaurant ID: " + rs.getInt("restaurant_id"));
                System.out.println("Reservation ID: " + rs.getInt("reservation_id"));
                System.out.println("Order ID: " + rs.getInt("order_id"));
                System.out.println("Menu ID: " + rs.getInt("menu_id"));
                System.out.println("Menu Name: " + rs.getString("menu_name"));
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
     * 고객용 주문 생성 (SELECT, INSERT, UPDATE문 활용, 트랜잭션 활용)
     */
    private void customerOrderCreation() {
        System.out.print("\n----Customer Order Creation----\n");
        System.out.print("Enter your customer ID: ");
        int customerId = scanner.nextInt(); // 입력 받기
        scanner.nextLine();

        System.out.print("Enter reservation ID: ");
        int reservationId = scanner.nextInt(); // 입력 받기
        scanner.nextLine();

        System.out.print("Enter restaurant ID: ");
        int restaurantId = scanner.nextInt(); // 입력 받기
        scanner.nextLine();

        System.out.print("Enter menu ID: ");
        int menuId = scanner.nextInt(); // 입력 받기
        scanner.nextLine();

        // 현재 시간을 order_time으로 설정
        String orderTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        try {
            conn.setAutoCommit(false); // 트랜잭션 시작

            // 예약이 고객의 예약인지 확인
            String verifySql = "SELECT * FROM DB2024_RESERVATION WHERE reservation_id = ? AND customer_id = ?";
            PreparedStatement verifyPstmt = conn.prepareStatement(verifySql);
            verifyPstmt.setInt(1, reservationId);
            verifyPstmt.setInt(2, customerId);
            ResultSet rs = verifyPstmt.executeQuery();

            if (rs.next()) {
                // 주문 생성
                String orderSql = "INSERT INTO DB2024_ORDER (reservation_id, menu_id, restaurant_id, order_time) VALUES (?, ?, ?, ?)";
                PreparedStatement orderPstmt = conn.prepareStatement(orderSql);
                orderPstmt.setInt(1, reservationId);
                orderPstmt.setInt(2, menuId);
                orderPstmt.setInt(3, restaurantId);
                orderPstmt.setString(4, orderTime);
                orderPstmt.executeUpdate();

                // 메뉴 재고 확인 및 감소
                String stockSql = "SELECT stock FROM DB2024_MENU WHERE menu_id = ?";
                PreparedStatement stockPstmt = conn.prepareStatement(stockSql);
                stockPstmt.setInt(1, menuId);
                ResultSet stockRs = stockPstmt.executeQuery();

                if (stockRs.next()) {
                    int stock = stockRs.getInt("stock");
                    if (stock >= 1) {
                        String updateStockSql = "UPDATE DB2024_MENU SET stock = stock - 1 WHERE menu_id = ?";
                        PreparedStatement updateStockPstmt = conn.prepareStatement(updateStockSql);
                        updateStockPstmt.setInt(1, menuId);
                        updateStockPstmt.executeUpdate();
                        updateStockPstmt.close();
                    } else {
                        conn.rollback();
                        System.out.println("Stock is insufficient.");
                        return;
                    }
                } else {
                    conn.rollback();
                    System.out.println("Menu not found.");
                    return;
                }

                stockRs.close();
                stockPstmt.close();
                orderPstmt.close();
            } else {
                conn.rollback();
                System.out.println("Reservation ID does not belong to the customer.");
                return;
            }

            rs.close();
            verifyPstmt.close();

            conn.commit(); // 트랜잭션 커밋
            System.out.println("Order created successfully.");
        } catch (SQLException e) {
            try {
                conn.rollback(); // 트랜잭션 롤백
            } catch (SQLException rollbackException) {
                rollbackException.printStackTrace();
            }
            System.out.println("Error creating order:");
            e.printStackTrace();
        } finally {
            try {
                conn.setAutoCommit(true); // 자동 커밋 모드로 전환
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 고객용 주문 수정/삭제 요청 (SELECT문 활용, 조인 활용)
     */
    private void customerOrderModifyOrDelete() {
        System.out.print("\n----Customer Order Modify or Delete----\n");
        System.out.print("Enter your customer ID: ");
        int customerId = scanner.nextInt(); // 입력 받기
        scanner.nextLine();

        System.out.print("Enter order ID: ");
        int orderId = scanner.nextInt(); // 입력 받기
        scanner.nextLine();

        try {
            // Verify if the order belongs to the customer
            String verifySql = "SELECT o.order_id FROM DB2024_ORDER o JOIN DB2024_RESERVATION r ON o.reservation_id = r.reservation_id WHERE o.order_id = ? AND r.customer_id = ?";
            PreparedStatement verifyPstmt = conn.prepareStatement(verifySql);
            verifyPstmt.setInt(1, orderId);
            verifyPstmt.setInt(2, customerId);
            ResultSet rs = verifyPstmt.executeQuery();

            if (rs.next()) {
                System.out.print("\n----Select Option----\n");
                System.out.println("1. Modify Order");
                System.out.println("2. Delete Order");
                System.out.print("Enter your choice: ");
                int choice = scanner.nextInt(); // 입력 받기
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
     * 고객용 주문 확인 (SELECT문 활용, 조인 활용)
     */
    private void customerOrderConfirmation() {
        System.out.print("\n----Customer Order Detail----\n");
        System.out.print("Enter your customer name: ");
        String customerName = scanner.nextLine(); // 입력 받기

        System.out.print("Enter restaurant ID: ");
        int restaurantId = scanner.nextInt(); // 입력 받기
        scanner.nextLine();

        try {
            String sql = "SELECT o.* FROM DB2024_ORDER o JOIN DB2024_RESERVATION r ON o.reservation_id = r.reservation_id JOIN DB2024_CUSTOMER c ON r.customer_id = c.customer_id WHERE c.customer_name = ? AND o.restaurant_id = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, customerName);
            pstmt.setInt(2, restaurantId);
            ResultSet rs = pstmt.executeQuery();

            System.out.print("\n----Order List----\n");
            while (rs.next()) {
                System.out.println("Restaurant ID: " + rs.getInt("restaurant_id"));
                System.out.println("Reservation ID: " + rs.getInt("reservation_id"));
                System.out.println("Order ID: " + rs.getInt("order_id"));
                System.out.println("Menu ID: " + rs.getInt("menu_id"));
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
     * 특정 식당과 예약번호로 주문 검색 (SELECT문 활용, 인덱스 활용)
     */
    private void searchOrdersByRestaurantAndReservation() {
        System.out.print("\n----Search Using Index----\n");
        System.out.print("Enter restaurant ID: ");
        int restaurantId = scanner.nextInt(); // 입력 받기
        scanner.nextLine();

        System.out.print("Enter reservation ID: ");
        int reservationId = scanner.nextInt(); // 입력 받기
        scanner.nextLine();

        try {
            String sql = "SELECT * FROM DB2024_ORDER WHERE restaurant_id = ? AND reservation_id = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, restaurantId);
            pstmt.setInt(2, reservationId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                System.out.println("\n------------------------");
                System.out.print("\n----Order Info----\n");
                System.out.println("Restaurant ID: " + rs.getInt("restaurant_id"));
                System.out.println("Reservation ID: " + rs.getInt("reservation_id"));
                System.out.println("Order ID: " + rs.getInt("order_id"));
                System.out.println("Menu ID: " + rs.getInt("menu_id"));
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
     * 특정 위치와 카테고리의 레스토랑들의 주문 검색 (SELECT문 활용, 중첩 활용)
     */
    private void searchOrdersByLocationAndCategory() {
        System.out.print("\n----Search Orders by Location and Category----\n");
        System.out.print("Enter location (e.g., 서대문구): ");
        String location = scanner.nextLine(); // 입력 받기

        System.out.print("Enter category (e.g., 한식): ");
        String category = scanner.nextLine(); // 입력 받기

        try {
            // 중첩문 사용
            String sql = "SELECT o.restaurant_id, o.order_id, r.customer_id, o.order_time " +
                    "FROM DB2024_ORDER o " +
                    "JOIN DB2024_RESERVATION r ON o.reservation_id = r.reservation_id " +
                    "WHERE o.restaurant_id IN (SELECT restaurant_id " +
                    "FROM DB2024_RESTAURANT " +
                    "WHERE location LIKE ? AND restaurant_category LIKE ?) " +
                    "GROUP BY o.restaurant_id, o.order_id " +
                    "ORDER BY o.order_time";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, "%" + location + "%");
            pstmt.setString(2, "%" + category + "%");
            ResultSet rs = pstmt.executeQuery();

            System.out.print("\n----Order List----\n");
            while (rs.next()) {
                System.out.println("Restaurant ID: " + rs.getInt("restaurant_id"));
                System.out.println("Order ID: " + rs.getInt("order_id"));
                System.out.println("Customer ID: " + rs.getInt("customer_id"));
                System.out.println("Order Time: " + rs.getString("order_time"));
                System.out.println("------------------------");
            }

            rs.close();
            pstmt.close();
        } catch (SQLException e) {
            System.out.println("Error searching orders by location and category:");
            e.printStackTrace();
        }
    }

    /**
     * 주문 수정 (UPDATE문 활용)
     */
    private void modifyOrder(int orderId) {
        System.out.print("\n----Order Modification----\n");
        System.out.print("Enter new menu ID: ");
        int newMenuId = scanner.nextInt(); // 입력 받기
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
     * 주문 삭제 (DELETE문 활용)
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
