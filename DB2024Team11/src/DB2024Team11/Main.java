package DB2024Team11;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Scanner;

/**
 * Main 클래스는 애플리케이션의 진입점
 * 사용자에게 역할 선택할 수 있는 메뉴 제공
 */
public class Main {
    public static void main(String[] args) {
        Connection conn;
        try {
            // 데이터베이스에 연결
            conn = DBConnector.makeConnection();
            if (conn != null) {
                System.out.println("Connection to the database was successful!");

                Scanner scanner = new Scanner(System.in);
                boolean running = true;
                System.out.println("Welcome to the Restaurant Management System!");

                while (running) {

                    // 사용자 역할 선택
                    System.out.println("\n==== Select Your Role ====");
                    System.out.println("1. Restaurant Manager");
                    System.out.println("2. Customer");
                    System.out.println("3. Exit");
                    System.out.print("Enter your choice: ");
                    int choice = scanner.nextInt();
                    scanner.nextLine();

                    switch (choice) {
                        case 1 -> {
                            // 레스토랑 관리자 역할 처리
                            Restaurant restaurant = new Restaurant(conn, scanner);
                            restaurant.handleOperations();
                        }
                        case 2 -> {
                            // 고객 역할 처리
                            Customer customer = new Customer(conn, scanner);
                            customer.handleOperations();
                        }
                        case 3 ->
                            // 프로그램을 종료
                                running = false;
                        default -> System.out.println("Invalid choice. Please enter 1, 2, or 3.");
                    }

                    // 사용자가 프로그램 종료 여부 선택
                    System.out.print("\nDo you want to exit? (y/n): ");
                    String answer = scanner.nextLine();
                    if (!answer.equalsIgnoreCase("n")) {
                        running = false;
                    }
                }

            } else {
                System.out.println("Failed to make a connection to the database.");
            }
        } catch (SQLException | IOException e) {
            System.out.println("An error occurred:");
            e.printStackTrace();
        } finally {
            // 연결 종료
            DBConnector.closeConnection();
            System.out.println("Database connection closed.");
        }
    }
}
