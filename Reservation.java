//Reservation.java

package DB2024Team11;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

/**
 * Reservation 클래스는 예약을 생성하고 관리하는 기능을 제공합니다.
 */
public class Reservation {
    private final Connection conn;
    private final Scanner scanner;

    /**
     * Reservation 클래스의 생성자.
     *
     * @param conn    데이터베이스 연결 객체
     * @param scanner 사용자 입력을 받기 위한 Scanner 객체
     */
    public Reservation(Connection conn, Scanner scanner) {
        this.conn = conn;
        this.scanner = scanner;
    }

    /*
     * @param role 사용자의 역할 (고객 또는 관리자)
     */
    public void handleOperations(String role) {
        if (role.equalsIgnoreCase("customer")) {
            handleCustomerReservation();
        } else if (role.equalsIgnoreCase("manager")) {
            handleManagerReservation();
        } else {
            System.out.println("Invalid role. Please specify either 'customer' or 'manager'.");
        }
    }

    /**
     * 고객의 예약 관련 기능 선택
     */
    private void handleCustomerReservation() {
        System.out.println("\n==== Customer Reservation Menu ====");
        System.out.println("1. Create Reservation");
        System.out.println("2. Cancel Reservation");
        System.out.println("3. View Reservation");

        System.out.print("Enter your choice: ");
        int choice = scanner.nextInt();
        scanner.nextLine();  // 입력 버퍼 비우기

        switch (choice) {
            case 1 -> createReservation();
            case 2 -> cancelReservation();
            case 3 -> viewReservation();
            default -> System.out.println("Invalid choice. Please enter a number between 1 and 4.");
        }
    }
    
    /**
     * 관리자의 예약 관련 기능 선택
     */
    private void handleManagerReservation() {
        System.out.println("\n==== Manager Reservation Menu ====");
        System.out.println("1. Create Reservation");
        System.out.println("2. Cancel Reservation");
        System.out.println("3. View Reservation");
        System.out.println("4. Confirm Reservation");

        System.out.print("Enter your choice: ");
        int choice = scanner.nextInt();
        scanner.nextLine();  // 입력 버퍼 비우기

        switch (choice) {
            case 1 -> createReservation();
            case 2 -> cancelReservation();
            case 3 -> viewReservations();
            case 4 -> confirmReservation();
            default -> System.out.println("Invalid choice. Please enter a number between 1 and 5.");
        }
    }

    /**
		 * 예약 생성 기능
		 */
		private void createReservation() {
		    try {
		        // 레스토랑 선택
		        System.out.print("Enter restaurant ID: ");
		        int restaurantId = scanner.nextInt();
		        scanner.nextLine(); // 개행
		
		        // 날짜 선택
		        System.out.print("Enter reservation date (YYYY-MM-DD): ");
		        String reservationDate = scanner.nextLine();
		
		        // 시간 선택
		        System.out.print("Enter reservation time (HH:MM:SS): ");
		        String reservationTime = scanner.nextLine();
		
		        // 인원 선택
		        System.out.print("Enter number of guests: ");
		        int numberOfGuests = scanner.nextInt();
		        scanner.nextLine(); // 개행
		
		        // 예약 신청
		        String sql = "INSERT INTO Reservation (CustomerID, RestaurantID, ReservationDate, ReservationTime, NumberOfGuests) VALUES (?, ?, ?, ?, ?)";
		        PreparedStatement pstmt = conn.prepareStatement(sql);
		        pstmt.setInt(1,  customerId);
		        pstmt.setInt(2, restaurantId);
		        pstmt.setString(3, reservationDate);
		        pstmt.setString(4, reservationTime);
		        pstmt.setInt(5, numberOfGuests);
		
		        int rowsAffected = pstmt.executeUpdate();
		        if (rowsAffected > 0) {
		            System.out.println("Reservation created successfully!");
		        } else {
		            System.out.println("Failed to create reservation.");
		        }
		        pstmt.close();
		    } catch (SQLException e) {
		        System.out.println("Error creating reservation:");
		        e.printStackTrace();
		    }
		}

    /**
     * 예약 취소 기능
     */
    private void cancelReservation() {
    try {
        System.out.print("Enter reservation ID to cancel: ");
        int reservationId = scanner.nextInt();
        scanner.nextLine();  // Clear input buffer

        String sql = "DELETE FROM Reservations WHERE id = ?";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setInt(1, reservationId);

        int rowsAffected = pstmt.executeUpdate();
        if (rowsAffected > 0) {
            System.out.println("Reservation canceled successfully.");
        } else {
            System.out.println("Failed to cancel reservation. Please make sure the reservation ID is correct.");
        }

        pstmt.close();
    } catch (SQLException e) {
        System.out.println("Error canceling reservation:");
        e.printStackTrace();
    }
}

    /**
     * 예약 조회 기능
     */
    private void viewReservation() {
    try {
        System.out.print("Enter reservation date (YYYY-MM-DD): ");
        String reservationDate = scanner.nextLine();

        String sql = "SELECT * FROM Reservations WHERE reservation_date = ?";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, reservationDate);

        ResultSet rs = pstmt.executeQuery();

        if (rs.next()) {
            System.out.println("Reservations for " + reservationDate + ":");
            System.out.println("----------------------------");
            do {
                System.out.println("Reservation ID: " + rs.getInt("id"));
                System.out.println("Customer ID: " + rs.getInt("customer_id"));
                System.out.println("Restaurant ID: " + rs.getInt("restaurant_id"));
                System.out.println("Reservation Date: " + rs.getString("reservation_date"));
                System.out.println("Reservation Time: " + rs.getString("reservation_time"));
                System.out.println("Number of Guests: " + rs.getInt("num_guests"));
                System.out.println("Reservation Status: " + (rs.getInt("confirmed") == 1 ? "Confirmed" : "Not Confirmed"));
                System.out.println("----------------------------");
            } while (rs.next());
        } else {
            System.out.println("No reservations found for the specified date.");
        }

        rs.close();
        pstmt.close();
    } catch (SQLException e) {
        System.out.println("Error viewing reservations:");
        e.printStackTrace();
    }
}

    /**
     * 예약 확정 기능
     */
    private void confirmㄱReservation() {
    try {
        // 사용자로부터 예약 ID 입력 받기
        System.out.print("Enter reservation ID: ");
        int reservationId = scanner.nextInt();
        scanner.nextLine();  // 입력 버퍼 비우기

        // 사용자로부터 확정 또는 거부 여부 입력 받기
        System.out.print("Confirm or Reject? (C/R): ");
        String choice = scanner.nextLine();

        // 예약 상태 업데이트
        String sql = "UPDATE Reservation SET status = ? WHERE reservation_id = ?";
        PreparedStatement pstmt = conn.prepareStatement(sql);

        if (choice.equalsIgnoreCase("C")) {
            pstmt.setString(1, "Confirmed");
        } else if (choice.equalsIgnoreCase("R")) {
            pstmt.setString(1, "Rejected");
        } else {
            System.out.println("Invalid choice. Please enter 'C' for Confirm or 'R' for Reject.");
            pstmt.close();
            return;
        }

        pstmt.setInt(2, reservationId);
        int rowsAffected = pstmt.executeUpdate();

        if (rowsAffected > 0) {
            System.out.println("Reservation status updated successfully!");
        } else {
            System.out.println("Failed to update reservation status.");
        }

        pstmt.close();
    } catch (SQLException e) {
        System.out.println("Error updating reservation status:");
        e.printStackTrace();
    }
}
