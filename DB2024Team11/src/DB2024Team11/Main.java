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
    private static Connection conn;
    private static Scanner scanner;
    public static void main(String[] args) {
        try {
            // 데이터베이스에 연결
            conn = DBConnector.makeConnection();
            if (conn != null) {
                System.out.println("Connection to the database was successful!");

                Scanner scanner = new Scanner(System.in);
                boolean running = true;
                System.out.println("""
                        ======================================================
                        ||\tWelcome to the Restaurant Management System!\t||
                        ======================================================""");

                while (running) {
                    System.out.print("\033[H\033[2J");
                    System.out.flush();

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
                            handleAdminOperations(conn,scanner);
                        }
                        case 2 -> {
                            // 고객 역할 처리
                            handleCustomerOperations(conn,scanner);
                        }
                        case 3 ->
                            // 프로그램을 종료
                                running = false;
                        default -> System.out.println("Invalid choice. Please enter 1, 2, or 3.");
                    }
                    if (running) continue;
                    // 사용자가 프로그램 종료 여부 선택
                    System.out.print("\nDo you want to exit? (y/n): ");
                    String answer = scanner.nextLine();
                    if (answer.equalsIgnoreCase("n")) {
                        running = true;
                    }
                }

            } else {
                System.out.println("Failed to make a connection to the database.");
            }
        } catch (SQLException | IOException e) {
            System.out.println("An error occurred:");
            e.printStackTrace();
        } finally {
            DBConnector.closeConnection();
            System.out.println("Database connection closed.");
        }

    }
    public static void handleCustomerOperations(Connection conn, Scanner scanner) {
        boolean running = true;
        while (running) {
            System.out.println("\n==== Customer Menu ====");
            System.out.println("1. Restaurant");
            System.out.println("2. Customer");
            System.out.println("3. Menu");
            System.out.println("4. Order");
            System.out.println("5. Reservation");
            System.out.println("6. Payment & Receipt");
            System.out.println("7. Back");
            System.out.print("Enter your choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // 개행

            switch (choice) {

                case 1 -> {
                    Restaurant restaurant = new Restaurant(conn, scanner);
                    restaurant.handleCustomerOperations();
                }
                case 2 -> {
                    Customer customer = new Customer(conn, scanner);
                    customer.handleCustomerOperations();
                }
                case 3 -> {
                    Menu menu = new Menu(conn, scanner);
                    menu.handleCustomerOperations();
                }
                case 4 -> {
                    Order order = new Order(conn, scanner);
                    order.handleCustomerOperations();
                }
                case 5 -> {
                    Reservation reservation = new Reservation(conn, scanner);
                    reservation.handleCustomerOperations();
                }
                case 6 -> {
                    Payment payment = new Payment(conn, scanner);
                    payment.handleCustomerOperations();
                }
                case 7 -> {
                    return;
                }
                default -> System.out.println("Invalid choice. Please enter a number between 1 and 7.");
            }
        }
    }
    public static void handleAdminOperations(Connection conn, Scanner scanner) {
        boolean running = true;
        while (running) {
            System.out.println("\n==== Manager Menu ====");
            System.out.println("1. Restaurant");
            System.out.println("2. Customer");
            System.out.println("3. Menu");
            System.out.println("4. Order");
            System.out.println("5. Reservation");
            System.out.println("6. Payment");
            System.out.println("7. Back");
            System.out.print("Enter your choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // 개행
            switch (choice) {
                case 1 -> {
                    Restaurant restaurant = new Restaurant(conn, scanner);
                    restaurant.handleAdminOperations();
                }
                case 2 -> {
                    Customer customer = new Customer(conn, scanner);
                    customer.handleAdminOperations();
                }
                case 3 -> {
                    Menu menu = new Menu(conn, scanner);
                    menu.handleAdminOperations();
                }
                case 4 -> {
                    Order order = new Order(conn, scanner);
                    order.handleAdminOperations();
                }
                case 5 -> {
                    Reservation reservation = new Reservation(conn, scanner);
                    reservation.handleAdminOperations();
                }
                case 6 -> {
                    Payment payment = new Payment(conn, scanner);
                    payment.handleAdminOperations();
                }
                case 7 -> {
                    return;
                }
                default -> System.out.println("Invalid choice. Please enter a number between 1 and 7.");
            }
        }
    }
}
