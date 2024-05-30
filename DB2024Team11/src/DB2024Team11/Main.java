package DB2024Team11;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Connection conn = null;
        try {
            conn = DBConnector.make_connection();
            if (conn != null) {
                System.out.println("Connection to the database was successful!");

                Scanner scanner = new Scanner(System.in);
                boolean running = true;
                System.out.println("Welcome to Restaurant Management System!");

                while (running) {

                    System.out.println("\n==== Select Your Role ====");
                    System.out.println("1. Restaurant Manager");
                    System.out.println("2. Customer");
                    System.out.println("3. Exit");
                    System.out.print("Enter your choice: ");
                    int choice = scanner.nextInt();
                    scanner.nextLine();
                    switch (choice) {
                        case 1:
                            Restaurant restaurant = new Restaurant(conn, scanner);
                            restaurant.handleOperations();
                            break;
                        case 2:
                            Customer customer = new Customer(conn, scanner);
                            customer.handleOperations();
                            break;
                        case 3:
                            break;
                        default:
                            System.out.println("Invalid choice. Please enter 1 or 2.");
                    }

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
            // Close the connection
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                System.out.println("Failed to close the connection:");
                e.printStackTrace();
            }
        }
    }
}
