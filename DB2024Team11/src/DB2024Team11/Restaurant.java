package DB2024Team11;

import java.sql.Connection;
import java.util.Scanner;

/**
 * Restaurant 클래스는 레스토랑 예약을 관리하는 기능을 제공합니다.
 */
public class Restaurant {
    private final Connection conn;
    private final Scanner scanner;

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
            System.out.println("1. View Restaurant Information");
            System.out.println("2. Create Restaurant Information");
            System.out.println("3. Back");

            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // 개행

            switch (choice) {
                case 1 -> getRestaurantInfo();
                case 2 -> running = false;
                default -> System.out.println("Invalid choice. Please enter a number between 1 and 5.");
            }
        }
    }

    /**
     * 레스토랑 정보를 조회합니다.
     */
    private void getRestaurantInfo() {
        // 여기에서 실제 예약 정보를 조회하는 SQL 쿼리와 그 결과를 처리하는 코드
        System.out.println("Viewing restaurant information...");
    }

    /**
     * 레스토랑 정보를 생성합니다.
     */
    private void createRestaurantInfo() {
        // 여기에서 실제 레스토랑 정보를 생성하는 SQL 쿼리와 입력받은 데이터를 처리하는 코드
        System.out.println("Creating restaurant information...");
    }
}
