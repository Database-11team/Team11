package DB2024Team11;

import java.sql.Connection;
import java.util.Scanner;

/**
 * Restaurant 클래스는 레스토랑 예약을 관리하는 기능을 제공합니다.
 */
public class Restaurant {
    private Connection conn;
    private Scanner scanner;

    /**
     * Restaurant 클래스의 생성자.
     *
     * @param conn    데이터베이스 연결 객체
     * @param scanner 사용자 입력을 받기 위한 Scanner 객체
     */
    public Restaurant(Connection conn, Scanner scanner) {
        this.conn = conn;
        this.scanner = scanner;
    }

    /**
     * 레스토랑 관리자 메뉴를 표시하고 사용자의 선택에 따라 작업 처리.
     */
    public void handleOperations() {
        boolean running = true;
        while (running) {
            System.out.println("\n==== Restaurant Manager Menu ====");
            System.out.println("1. View Reservations");
            System.out.println("2. Add New Reservation");
            System.out.println("3. Update Reservation");
            System.out.println("4. Delete Reservation");
            System.out.println("5. Back");

            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // 개행

            switch (choice) {
                case 1:
                    viewReservations();
                    break;
                case 2:
                    addReservation();
                    break;
                case 3:
                    updateReservation();
                    break;
                case 4:
                    deleteReservation();
                    break;
                case 5:
                    running = false;
                    break;
                default:
                    System.out.println("Invalid choice. Please enter a number between 1 and 5.");
            }
        }
    }

    /**
     * 예약 정보를 조회하는 메서드.
     */
    private void viewReservations() {
        // 예약 조회 로직 구현
        System.out.println("Viewing reservations...");
    }

    /**
     * 새 예약을 추가하는 메서드.
     */
    private void addReservation() {
        // 새 예약 추가 로직 구현
        System.out.println("Adding new reservation...");
    }

    /**
     * 예약 정보를 업데이트하는 메서드.
     */
    private void updateReservation() {
        // 예약 업데이트 로직 구현
        System.out.println("Updating reservation...");
    }

    /**
     * 예약 정보를 삭제하는 메서드.
     */
    private void deleteReservation() {
        // 예약 삭제 로직 구현
        System.out.println("Deleting reservation...");
    }
}
