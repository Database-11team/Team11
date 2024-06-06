package DB2024Team11;

import java.sql.*;
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
    public void handleAdminOperations() {
        boolean running = true;
        while (running) {
            System.out.println("\n==== Restaurant Manager ====");
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
                case 1 -> getRestaurant();
                case 2 -> createRestaurant();
                case 3 -> updateRestaurant();
                case 4 -> deleteRestaurantInfo();
                case 5 -> searchRestaurant();
                case 6 -> running = false;
                default -> System.out.println("Invalid choice. Please enter a number between 1 and 6.");
            }
        }
    }
    public void handleCustomerOperations() {
        boolean running = true;
        while (running) {
            System.out.println("\n==== Restaurant Manager ====");
            System.out.println("1. View Restaurant Information");
            System.out.println("2. Search Restaurants");
            System.out.println("3. Back");

            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // 개행

            switch (choice) {
                case 1 -> getRestaurant();
                case 2 -> searchRestaurant();
                case 3 -> running = false;
                default -> System.out.println("Invalid choice. Please enter a number between 1 and 6.");
            }
        }
    }
    /**
     * 레스토랑 정보를 조회합니다.
     */
    private void getRestaurant() {
        System.out.print("Enter restaurant ID to view information: ");
        int restaurantId = scanner.nextInt();
        scanner.nextLine(); // 개행

        getRestaurantInfo(restaurantId);
    }

    private void getRestaurantInfo(int restaurantId) {
        String query = "SELECT * FROM DB2024_RESTAURANT WHERE restaurant_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, restaurantId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                System.out.println("\n-----------------------------------------------------");
                System.out.println("Restaurant ID: " + rs.getInt("restaurant_id"));
                System.out.println("Restaurant Name: " + rs.getString("restaurant_name"));
                System.out.println("Restaurant Category: " + rs.getString("restaurant_category"));
                System.out.println("Location: " + rs.getString("location"));
                System.out.println("Contact: " + rs.getString("contact"));
                System.out.println("Opening Hours: " + rs.getString("opening_hours"));
                System.out.println("Breaktime: " + rs.getString("breaktime"));
                System.out.println("Last Order: " + rs.getString("lastorder"));
                System.out.println("Closed Day: " + rs.getString("closed_day"));
                System.out.println("Number of Tables: " + rs.getInt("table_num"));
                System.out.println("Availability: " + (rs.getInt("availability") == 1 ? "Available" : "Not Available"));
                System.out.println("-----------------------------------------------------");
            } else {
                System.out.println("No restaurant found with the given ID.");
                System.out.println("-----------------------------------------------------\n");
            }
            System.out.println();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    /**
     * 레스토랑 정보를 생성합니다.
     */
    private void createRestaurant() {
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
        int availabilityInt = scanner.nextInt();
        scanner.nextLine(); // 개행
        boolean availability = availabilityInt == 1;

        String query = "INSERT INTO DB2024_RESTAURANT (restaurant_name, restaurant_category, location, contact, opening_hours, breaktime, lastorder, closed_day, table_num, availability) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try {
            conn.setAutoCommit(false); // Disable auto-commit

            try (PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
                stmt.setString(1, name.isEmpty() ? null : name);
                stmt.setString(2, category.isEmpty() ? null : category);
                stmt.setString(3, location.isEmpty() ? null : location);
                stmt.setString(4, contact.isEmpty() ? null : contact);
                stmt.setString(5, openingHours.isEmpty() ? null : openingHours);
                stmt.setString(6, breaktime.isEmpty() ? null : breaktime);
                stmt.setString(7, lastorder.isEmpty() ? null : lastorder);
                stmt.setString(8, closedDay.isEmpty() ? null : closedDay);
                stmt.setInt(9, tableNum);
                stmt.setBoolean(10, availability);

                int rowsInserted = stmt.executeUpdate();
                if (rowsInserted > 0) {
                    System.out.println("Restaurant information created successfully.");
                    try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                        if (generatedKeys.next()) {
                            int restaurantId = generatedKeys.getInt(1);
                            getRestaurantInfo(restaurantId);
                        }
                    }
                }
                conn.commit(); // Commit the transaction
            } catch (SQLException e) {
                e.printStackTrace();
                System.out.println("Rolling back data here....");
                if (conn != null) {
                    try {
                        conn.rollback();
                    } catch (SQLException se2) {
                        se2.printStackTrace();
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (conn != null) {
                    conn.setAutoCommit(true); // Enable auto-commit back
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 레스토랑 정보를 수정
     */
    private void updateRestaurant() {
        System.out.print("\nEnter restaurant ID to update: ");
        int restaurantId = scanner.nextInt();
        scanner.nextLine(); // 개행
        getRestaurantInfo(restaurantId);

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
        int availabilityInt = scanner.nextInt();
        scanner.nextLine(); // 개행

        String query = "UPDATE DB2024_RESTAURANT " +
                "SET " +
                "restaurant_name = IF(? != '', ?, restaurant_name), " +
                "restaurant_category = IF(? != '', ?, restaurant_category), " +
                "location = IF(? != '', ?, location), " +
                "contact = IF(? != '', ?, contact), " +
                "opening_hours = IF(? != '', ?, opening_hours), " +
                "breaktime = IF(? != '', ?, breaktime), " +
                "lastorder = IF(? != '', ?, lastorder), " +
                "closed_day = IF(? != '', ?, closed_day), " +
                "table_num = IF(? != -1, ?, table_num), " +
                "availability = IF(? != -1, ?, availability) " +
                "WHERE restaurant_id = ?";

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
            stmt.setInt(19, availabilityInt);
            stmt.setInt(20, availabilityInt);
            stmt.setInt(21, restaurantId);

            int rowsUpdated = stmt.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Restaurant information updated successfully.");
                getRestaurantInfo(restaurantId);
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
// ----------------------------------------- SEARCH ---------------------------------------------------
    /**
     * 특정 속성으로 레스토랑을 검색합니다.
     */
    private void searchRestaurant() {
        boolean searchRunning = true;
        while (searchRunning) {
            System.out.println("\n==== Search Menu Options ====");
            System.out.println("1. Search by Name");
            System.out.println("2. Search by Location");
            System.out.println("3. Search by Name & Location");
            System.out.println("4. Search by Category");
            System.out.println("5. Search by Available Date");
            System.out.println("6. Back");

            System.out.print("Enter your choice: ");
            int searchChoice = scanner.nextInt();
            scanner.nextLine(); // 개행

            switch (searchChoice) {
                case 1 -> searchByName();
                case 2 -> searchByLocation();
                case 3 -> searchByNameAndLocation();
                case 4 -> searchByCategory();
                case 5 -> searchByAvailDate();
                case 6 -> searchRunning = false;
                default -> System.out.println("Invalid choice. Please enter a number between 1 and 4.");
            }
        }
    }
    private void searchByName() {
        System.out.print("Enter restaurant name to search: ");
        String keyword = scanner.nextLine();
        // 입력한 키워드에 공백있는 경우에 대해서 토큰 분할
        String[] tokens = keyword.split("\\s+");

        // string builder를 사용해서 입력받은 토큰으로 쿼리 생성.
        // 토큰들은 And로 연결함
        StringBuilder queryBuilder = new StringBuilder("SELECT * FROM DB2024_RESTAURANT WHERE ");
        for (int i = 0; i < tokens.length; i++) {
            queryBuilder.append("restaurant_name LIKE ?");
            if (i < tokens.length - 1) {
                queryBuilder.append(" AND ");
            }
        }
        String query = queryBuilder.toString();
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            for (int i = 0; i < tokens.length; i++) {
                stmt.setString(i + 1, "%" + tokens[i] + "%"); // Add wildcards for partial search
            }
            ResultSet rs = stmt.executeQuery();
            processSearchResults(rs);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    private void searchByLocation() {
        System.out.print("Enter location to search: ");
        String location = scanner.nextLine();

        String query = "SELECT * FROM DB2024_RESTAURANT WHERE location LIKE ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, "%" + location + "%"); // Add wildcards for partial search
            ResultSet rs = stmt.executeQuery();
            processSearchResults(rs);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void searchByNameAndLocation() {
        System.out.print("Enter restaurant name to search: ");
        String name = scanner.nextLine();

        System.out.print("Enter location to search: ");
        String location = scanner.nextLine();

        String query = "SELECT * FROM DB2024_RESTAURANT WHERE restaurant_name LIKE ? AND location LIKE ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, "%" + name + "%");
            stmt.setString(2, "%" + location + "%");
            ResultSet rs = stmt.executeQuery();
            processSearchResults(rs);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void searchByCategory() {
        System.out.print("Enter restaurant category to search: ");
        String category = scanner.nextLine();

        String query = "SELECT * FROM DB2024_RESTAURANT WHERE restaurant_category = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, category);
            ResultSet rs = stmt.executeQuery();

            // Display search results
            processSearchResults(rs);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void searchByAvailDate() {
        System.out.print("Enter date to search for availability (YYYY-MM-DD): ");
        String targetDate = scanner.nextLine();

        String query = "SELECT r.restaurant_id, r.restaurant_name, r.location, r.category, r.opening_hours, r.closed_day \n" +
                "FROM DB2024_RESTAURANT r \n" +
                "JOIN DB2024_TABLE t ON r.restaurant_id = t.restaurant_id \n" +
                "LEFT JOIN DB2024_RESERVATION res ON t.table_id = res.table_id \n" +
                "AND res.reservation_date = ? \n" +
                "WHERE res.reservation_id IS NULL OR res.reservation_date != ?";

        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, targetDate);
            stmt.setString(2, targetDate);
            ResultSet rs = stmt.executeQuery();

            processSearchResults(rs);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    private void processSearchResults(ResultSet rs) throws SQLException {
        while (rs.next()) {
            System.out.println("-----------------------------------------------------");
            System.out.println("Restaurant ID: " + rs.getInt("restaurant_id"));
            System.out.println("Restaurant Name: " + rs.getString("restaurant_name"));
            System.out.println("Restaurant Category: " + rs.getString("restaurant_category"));
            System.out.println("Location: " + rs.getString("location"));
            System.out.println("Contact: " + rs.getString("contact"));
            System.out.println("Opening Hours: " + rs.getString("opening_hours"));
            System.out.println("Breaktime: " + rs.getString("breaktime"));
            System.out.println("Last Order: " + rs.getString("lastorder"));
            System.out.println("Closed Day: " + rs.getString("closed_day"));
            System.out.println("Number of Tables: " + rs.getInt("table_num"));
            System.out.println("Availability: " + (rs.getInt("availability") == 1 ? "Available" : "Not Available"));
            System.out.println("-----------------------------------------------------");
        }
        System.out.println();
    }
}
