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
            handleAdminReservation();
        } else {
            System.out.println("Invalid role. Please specify either 'customer' or 'manager'.");
        }
    }

    /**
     * 고객의 예약 관련 기능 선택
     */
    public void handleCustomerReservation() {
        System.out.println("\n==== Customer Reservation Menu ====");
        System.out.println("1. Create Reservation");
        System.out.println("2. Update Reservation");
        System.out.println("3. Cancel Reservation");
        System.out.println("4. View Reservation");

        System.out.print("Enter your choice: ");
        int choice = scanner.nextInt();
        scanner.nextLine();  // 입력 버퍼 비우기

        switch (choice) {
            case 1 -> createReservation();
            case 2 -> updateReservation();
            case 3 -> cancelReservation();
            case 4 -> viewReservation();
            default -> System.out.println("Invalid choice. Please enter a number between 1 and 4.");
        }
    }

    /**
     * 관리자의 예약 관련 기능 선택
     */
    public void handleAdminReservation() {
        System.out.println("\n==== Manager Reservation Menu ====");
        System.out.println("1. Create Reservation");
        System.out.println("2. Update Reservation");
        System.out.println("3. Cancel Reservation");
        System.out.println("4. View Reservation");
        System.out.println("5. Confirm Reservation");

        System.out.print("Enter your choice: ");
        int choice = scanner.nextInt();
        scanner.nextLine();  // 입력 버퍼 비우기

        switch (choice) {
            case 1 -> createReservation();
            case 2 -> updateReservation();
            case 3 -> cancelReservation();
            case 4 -> viewReservation();
            case 5 -> confirmReservation();
            default -> System.out.println("Invalid choice. Please enter a number between 1 and 5.");
        }
    }

    /**
     * 예약 생성 기능
     */
    private void createReservation() {
        try {
            //사용자 ID 입력
            System.out.print("Enter customer ID: ");
            int customerId=scanner.nextInt();

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

            //예약 확정 여부 초기화
            int confirmed=0;

            // 예약 신청
            String sql = "INSERT INTO Reservation (CustomerID, RestaurantID, ReservationDate, ReservationTime, NumberOfGuests, ReservationConfirmed) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, customerId );
            pstmt.setInt(2, restaurantId);
            pstmt.setString(3, reservationDate);
            pstmt.setString(4, reservationTime);
            pstmt.setInt(5, numberOfGuests);
            pstmt.setInt(6, confirmed);

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
     * 예약 변경 기능
     */
    private void updateReservation() {
        try {
            //예약 ID 입력받기
            System.out.print("Enter reservation ID to update: ");
            int reservationId = scanner.nextInt();
            scanner.nextLine();

            //현재 예약 정보 가져오기
            String querySql = "SELECT * FROM Reservation WHERE ReservationID = ?";
            PreparedStatement queryStmt = conn.prepareStatement(querySql);
            queryStmt.setInt(1, reservationId);
            ResultSet rs = queryStmt.executeQuery();

            if (rs.next()) {
                String currentDate = rs.getString("ReservationDate");
                String currentTime = rs.getString("ReservationTime");
                int currentGuests = rs.getInt("NumberOfGuests");

                System.out.println("Current Reservation Information:");
                System.out.println("Date: " + currentDate);
                System.out.println("Time: " + currentTime);
                System.out.println("Number of Guests: " + currentGuests);

                //변경할 예약 정보 입력받기 
                System.out.print("Enter new date (YYYY-MM-DD) or press Enter to keep current (" + currentDate + "): ");
                String newDate = scanner.nextLine();
                if (newDate.isEmpty()) {
                    newDate = currentDate;
                }

                System.out.print("Enter new time (HH:MM) or press Enter to keep current (" + currentTime + "): ");
                String newTime = scanner.nextLine();
                if (newTime.isEmpty()) {
                    newTime = currentTime;
                }

                System.out.print("Enter new number of guests or press Enter to keep current (" + currentGuests + "): ");
                String newGuestsInput = scanner.nextLine();
                int newGuests = newGuestsInput.isEmpty() ? currentGuests : Integer.parseInt(newGuestsInput);

                //예약 정보 업데이트 
                String updateSql = "UPDATE Reservation SET ReservationDate = ?, ReservationTime = ?, NumberOfGuests = ?, ReservationConfirmed = 0 WHERE ReservationID = ?";
                PreparedStatement updateStmt = conn.prepareStatement(updateSql);
                updateStmt.setString(1, newDate);
                updateStmt.setString(2, newTime);
                updateStmt.setInt(3, newGuests);
                updateStmt.setInt(4, reservationId);

                int rowsAffected = updateStmt.executeUpdate();
                if (rowsAffected > 0) {
                    System.out.println("Reservation successfully updated. It needs to be confirmed again by the manager.");
                } else {
                    System.out.println("Failed to update reservation. Please check the reservation ID.");
                }

                updateStmt.close();
            } else {
                System.out.println("Reservation not found. Please check the reservation ID.");
            }

            rs.close();
            queryStmt.close();
        } catch (SQLException e) {
            System.out.println("Error updating reservation:");
            e.printStackTrace();
        }
    }

    /**
     * 예약 취소 기능
     */
    private void cancelReservation() {
        try {
            //예약 ID 입력받기
            System.out.print("Enter reservation ID to cancel: ");
            int reservationId = scanner.nextInt();
            scanner.nextLine();  // Clear input buffer

            //예약 삭제하기
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
            System.out.print("Enter reservation ID to view: ");
            int reservationId = scanner.nextInt();
            scanner.nextLine();

            String sql = "SELECT * FROM Reservation WHERE ReservationID = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, reservationId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                int customerId = rs.getInt("CustomerID");
                int restaurantId = rs.getInt("RestaurantID");
                String date = rs.getString("ReservationDate");
                String time = rs.getString("ReservationTime");
                int guests = rs.getInt("NumberOfGuests");
                boolean confirmed = rs.getBoolean("ReservationConfirmed");

                System.out.println("Reservation Details:");
                System.out.println("Customer ID: " + customerId);
                System.out.println("Restaurant ID: " + restaurantId);
                System.out.println("Date: " + date);
                System.out.println("Time: " + time);
                System.out.println("Number of Guests: " + guests);
                System.out.println("Confirmed: " + (confirmed ? "Yes" : "No"));
            } else {
                System.out.println("Reservation not found. Please check the reservation ID.");
            }

            rs.close();
            pstmt.close();
        } catch (SQLException e) {
            System.out.println("Error viewing reservation:");
            e.printStackTrace();
        }
    }

    /**
     * 예약 확정 기능
     */
    private void confirmReservation() {
        try {
            // 사용자로부터 예약 ID 입력 받기
            System.out.print("Enter reservation ID: ");
            int reservationId = scanner.nextInt();
            scanner.nextLine();  // 입력 버퍼 비우기

            // 사용자로부터 확정 또는 거부 여부 입력 받기
            System.out.print("Confirm or Reject? (C/R): ");
            String choice = scanner.nextLine();

            // 예약 상태 업데이트
            String sql = "UPDATE Reservation SET reservation_confirmed = ? WHERE reservation_id = ?";
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
}