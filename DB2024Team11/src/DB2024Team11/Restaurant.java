package DB2024Team11;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
            System.out.println("3. Update Restaurant Information");
            System.out.println("4. Delete Restaurant Information");
            System.out.println("5. Search Restaurants");
            System.out.println("6. Back");

            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // 개행

            switch (choice) {
                case 1 -> getRestaurantInfo();
                case 2 -> createRestaurantInfo();
                case 3 -> updateRestaurantInfo();
                case 4 -> deleteRestaurantInfo();
                case 5 -> searchRestaurant();
                case 6 -> running = false;
                default -> System.out.println("Invalid choice. Please enter a number between 1 and 6.");
            }
        }
    }
    /**
     * 레스토랑 정보를 조회합니다.
     */
    private void getRestaurantInfo() {
        System.out.print("Enter restaurant ID to view information: ");
        int restaurantId = scanner.nextInt();
        scanner.nextLine(); // 개행

        String query = "SELECT * FROM DB2024_RESTAURANT WHERE restaurant_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, restaurantId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                System.out.println("Restaurant ID: " + rs.getInt("restaurant_id"));
                System.out.println("Name: " + rs.getString("restaurant_name"));
                System.out.println("Category: " + rs.getString("restaurant_category"));
                System.out.println("Location: " + rs.getString("location"));
                System.out.println("Contact: " + rs.getString("contact"));
                System.out.println("Opening Hours: " + rs.getString("opening_hours"));
                System.out.println("Breaktime: " + rs.getString("breaktime"));
                System.out.println("Last Order: " + rs.getString("lastorder"));
                System.out.println("Closed Day: " + rs.getString("closed_day"));
                System.out.println("Table Number: " + rs.getInt("table_num"));
                System.out.println("Availability: " + rs.getBoolean("availability"));
            } else {
                System.out.println("No restaurant found with the given ID.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 레스토랑 정보를 생성합니다.
     */
    private void createRestaurantInfo() {
        System.out.print("Enter restaurant name: ");
        String name = scanner.nextLine();
        System.out.print("Enter restaurant category: ");
        String category = scanner.nextLine();
        System.out.print("Enter location: ");
        String location = scanner.nextLine();
        System.out.print("Enter contact: ");
        String contact = scanner.nextLine();
        System.out.print("Enter opening hours: ");
        String openingHours = scanner.nextLine();
        System.out.print("Enter breaktime: ");
        String breaktime = scanner.nextLine();
        System.out.print("Enter last order time: ");
        String lastorder = scanner.nextLine();
        System.out.print("Enter closed day: ");
        String closedDay = scanner.nextLine();
        System.out.print("Enter number of tables: ");
        int tableNum = scanner.nextInt();
        scanner.nextLine(); // 개행
        System.out.print("Enter availability (1 for available, 0 for not available): ");
        boolean availability = scanner.nextBoolean();
        scanner.nextLine(); // 개행

        String query = "INSERT INTO DB2024_RESTAURANT (restaurant_name, restaurant_category, location, contact, opening_hours, breaktime, lastorder, closed_day, table_num, availability) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, name);
            stmt.setString(2, category);
            stmt.setString(3, location);
            stmt.setString(4, contact);
            stmt.setString(5, openingHours);
            stmt.setString(6, breaktime);
            stmt.setString(7, lastorder);
            stmt.setString(8, closedDay);
            stmt.setInt(9, tableNum);
            stmt.setBoolean(10, availability);

            int rowsInserted = stmt.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("Restaurant information created successfully.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 레스토랑 정보를 수정
     */
    private void updateRestaurantInfo() {
        System.out.print("Enter restaurant ID to update: ");
        int restaurantId = scanner.nextInt();
        scanner.nextLine(); // 개행

        System.out.print("Enter new restaurant name (leave blank to keep current): ");
        String name = scanner.nextLine();
        System.out.print("Enter new restaurant category (leave blank to keep current): ");
        String category = scanner.nextLine();
        System.out.print("Enter new location (leave blank to keep current): ");
        String location = scanner.nextLine();
        System.out.print("Enter new contact (leave blank to keep current): ");
        String contact = scanner.nextLine();
        System.out.print("Enter new opening hours (leave blank to keep current): ");
        String openingHours = scanner.nextLine();
        System.out.print("Enter new breaktime (leave blank to keep current): ");
        String breaktime = scanner.nextLine();
        System.out.print("Enter new last order time (leave blank to keep current): ");
        String lastorder = scanner.nextLine();
        System.out.print("Enter new closed day (leave blank to keep current): ");
        String closedDay = scanner.nextLine();
        System.out.print("Enter new number of tables (enter -1 to keep current): ");
        int tableNum = scanner.nextInt();
        scanner.nextLine(); // 개행
        System.out.print("Enter new availability (1 for available, 0 for not available, -1 to keep current): ");
        int availability = scanner.nextInt();
        scanner.nextLine(); // 개행

        String query = "UPDATE DB2024_RESTAURANT SET "
                + "restaurant_name = IF(? != '', ?, restaurant_name), "
                + "restaurant_category = IF(? != '', ?, restaurant_category), "
                + "location = IF(? != '', ?, location), "
                + "contact = IF(? != '', ?, contact), "
                + "opening_hours = IF(? != '', ?, opening_hours), "
                + "breaktime = IF(? != '', ?, breaktime), "
                + "lastorder = IF(? != '', ?, lastorder), "
                + "closed_day = IF(? != '', ?, closed_day), "
                + "table_num = IF(? != -1, ?, table_num), "
                + "availability = IF(? != -1, ?, availability) "
                + "WHERE restaurant_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, name);
            stmt.setString(2, name);
            stmt.setString(3, category);
            stmt.setString(4, category);
            stmt.setString(5, location);
            stmt.setString(6, location);
            stmt.setString(7, contact);
            stmt.setString(8, contact);
            stmt.setString(9, openingHours);
            stmt.setString(10, openingHours);
            stmt.setString(11, breaktime);
            stmt.setString(12, breaktime);
            stmt.setString(13, lastorder);
            stmt.setString(14, lastorder);
            stmt.setString(15, closedDay);
            stmt.setString(16, closedDay);
            stmt.setInt(17, tableNum);
            stmt.setInt(18, tableNum);
            stmt.setInt(19, availability);
            stmt.setInt(20, availability);
            stmt.setInt(21, restaurantId);

            int rowsUpdated = stmt.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Restaurant information updated successfully.");
            } else {
                System.out.println("No restaurant found with the given ID.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
    /**
     * 레스토랑 정보를 삭제합니다.
     */
    private void deleteRestaurantInfo() {
        System.out.print("Enter restaurant ID to delete: ");
        int restaurantId = scanner.nextInt();
        scanner.nextLine(); // 개행

        String query = "DELETE FROM DB2024_RESTAURANT WHERE restaurant_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, restaurantId);

            int rowsDeleted = stmt.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("Restaurant information deleted successfully.");
            } else {
                System.out.println("No restaurant found with the given ID.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 특정 속성으로 레스토랑을 검색합니다.
     */
    private void searchRestaurant() {
        System.out.println("Search by:");
        System.out.println("1. Restaurant Name");
        System.out.println("2. Category");
        System.out.println("3. Location");
        System.out.println("4. Availability");
        System.out.println("5. Name and Location");

        System.out.print("Enter your choice: ");
        int choice = scanner.nextInt();
        scanner.nextLine(); // 개행

        String query = "";
        String userInput = "";

        switch (choice) {
            case 1 -> {
                System.out.print("Enter restaurant name: ");
                userInput = scanner.nextLine();
                query = "SELECT * FROM DB2024_RESTAURANT WHERE restaurant_name LIKE ?";
            }
            case 2 -> {
                System.out.print("Enter restaurant category: ");
                userInput = scanner.nextLine();
                query = "SELECT * FROM DB2024_RESTAURANT WHERE restaurant_category LIKE ?";
            }
            case 3 -> {
                System.out.print("Enter location: ");
                userInput = scanner.nextLine();
                query = "SELECT * FROM DB2024_RESTAURANT WHERE location LIKE ?";
            }
            case 4 -> {
                System.out.print("Enter availability (1 for available, 0 for not available): ");
                int availability = scanner.nextInt();
                scanner.nextLine(); // 개행
                query = "SELECT * FROM DB2024_RESTAURANT WHERE availability = ?";
                userInput = String.valueOf(availability);
            }
            case 5 -> {
                System.out.print("Enter restaurant name: ");
                String name = scanner.nextLine();
                System.out.print("Enter location: ");
                String location = scanner.nextLine();
                query = "SELECT * FROM DB2024_RESTAURANT WHERE restaurant_name LIKE ? AND location LIKE ?";
                userInput = name + "," + location;
            }
            default -> {
                System.out.println("Invalid choice.");
                System.out.println("-----------------------------------------------------\n");

                return;
            }
        }

        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            if (choice != 4) {
                stmt.setString(1, "%" + userInput + "%");
            } else {
                stmt.setInt(1, Integer.parseInt(userInput));
            }

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                System.out.println("\n-----------------------------------------------------");
                System.out.println("Restaurant ID: " + rs.getInt("restaurant_id"));
                System.out.println("Name: " + rs.getString("restaurant_name"));
                System.out.println("Category: " + rs.getString("restaurant_category"));
                System.out.println("Location: " + rs.getString("location"));
                System.out.println("Contact: " + rs.getString("contact"));
                System.out.println("Opening Hours: " + rs.getString("opening_hours"));
                System.out.println("Breaktime: " + rs.getString("breaktime"));
                System.out.println("Last Order: " + rs.getString("lastorder"));
                System.out.println("Closed Day: " + rs.getString("closed_day"));
                System.out.println("Table Number: " + rs.getInt("table_num"));
                System.out.println("Availability: " + rs.getBoolean("availability"));
                System.out.println("-----------------------------------------------------\n");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
