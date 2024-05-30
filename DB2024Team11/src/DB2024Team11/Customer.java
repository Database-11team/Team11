package DB2024Team11;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Customer {
    private Connection conn;
    private Scanner scanner;

    public Customer(Connection conn, Scanner scanner) {
        this.conn = conn;
        this.scanner = scanner;
    }

    public void handleOperations() {
        boolean running = true;
        while (running) {
            System.out.println("\n==== Customer Menu ====");
            System.out.println("1. View Customer Information");
            System.out.println("2. Back");

            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    getCustomerInfo();
                    break;
                case 2:
                    running = false;
                    break;
                default:
                    System.out.println("Invalid choice. Please enter a number between 1 and 2.");
            }
        }
    }

    private void getCustomerInfo() {
        System.out.print("Enter customer name: ");
        String customerName = scanner.nextLine();

        System.out.print("Enter phone number: ");
        String phoneNumber = scanner.nextLine();

        try {
            String sql = "SELECT * FROM DB2024_CUSTOMER WHERE customer_name = ? AND phone_number = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, customerName);
            pstmt.setString(2, phoneNumber);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                System.out.println("Customer ID: " + rs.getInt("customer_id"));
                System.out.println("Customer Name: " + rs.getString("customer_name"));
                System.out.println("Birthday: " + rs.getDate("birthday"));
                System.out.println("Phone Number: " + rs.getString("phone_number"));
            } else {
                System.out.println("Customer not found.");
            }

            rs.close();
            pstmt.close();
        } catch (SQLException e) {
            System.out.println("Error retrieving customer information:");
            e.printStackTrace();
        }
    }
}
