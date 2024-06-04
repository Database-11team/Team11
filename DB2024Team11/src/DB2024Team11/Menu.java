package DB2024Team11;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Menu {
    private final Connection conn;
    private final Scanner scanner;
    public Menu(Connection conn, Scanner scanner) {
        this.conn = conn;
        this.scanner = scanner;
    }

    public void handleOperations() {
        boolean running = true;
        while (running) {
            System.out.println("\n==== Menu Manager Menu ====");
            System.out.println("1. View Menu Information by ID");
            System.out.println("2. Create Menu Information");
            System.out.println("3. Update Menu Information");
            System.out.println("4. Delete Menu Information");
            System.out.println("5. Search Menu Information");
            System.out.println("3. Back");

            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // 개행

            switch (choice) {
                case 1 -> getMenuInfo();
                case 2 -> createMenuInfo();
                case 3 -> updateMenuInfo();
                case 4 -> deleteMenuInfo();
                case 5 -> searchMenu();
                case 6 -> running = false;
                default -> System.out.println("Invalid choice. Please enter a number between 1 and 6.");
            }
        }

    }
    public void searchMenu() {
        boolean searchRunning = true;
        while (searchRunning) {
            System.out.println("\n==== Search Menu Options ====");
            System.out.println("1. Search by Category");
            System.out.println("2. Search by Price Range");
            System.out.println("3. Search by Name (keyword)");
            System.out.println("4. Search by Name (keyword)");
            System.out.println("4. Back");

            System.out.print("Enter your choice: ");
            int searchChoice = scanner.nextInt();
            scanner.nextLine(); // 개행

            switch (searchChoice) {
                case 1 -> searchMenuByCategory();
                case 2 -> searchMenuByPriceRange();
                case 3 -> searchMenuByName();
                case 4 -> searchMenuByRestaurantID();
                case 5 -> searchRunning = false;
                default -> System.out.println("Invalid choice. Please enter a number between 1 and 4.");
            }
        }
    }
    public void getMenuInfo() {
        System.out.print("Enter menu ID to view information: ");
        int menuId = scanner.nextInt();
        scanner.nextLine(); // 개행

        String query = "SELECT * FROM DB2024_MENU WHERE menu_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, menuId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                System.out.println("\n-----------------------------------------------------");
                System.out.println("Menu ID: " + rs.getInt("menu_id"));
                System.out.println("Restaurant ID: " + rs.getInt("restaurant_id"));
                System.out.println("Menu Category: " + rs.getString("menu_category"));
                System.out.println("Menu Name: " + rs.getString("menu_name"));
                System.out.println("Price: " + rs.getInt("price"));
                System.out.println("Allergic Included: " + rs.getString("allergic_included"));
                System.out.println("Stock: " + rs.getInt("stock"));
                System.out.println("Description: " + rs.getString("description"));
                System.out.println("-----------------------------------------------------\n");


            } else {
                System.out.println("No menu found with the given ID.");
                System.out.println("-----------------------------------------------------\n");

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void createMenuInfo() {
        System.out.print("Enter restaurant ID: ");
        int restaurantId = scanner.nextInt();
        scanner.nextLine(); // 개행
        System.out.print("Enter menu category: ");
        String menuCategory = scanner.nextLine();
        System.out.print("Enter menu name: ");
        String menuName = scanner.nextLine();
        System.out.print("Enter price: ");
        int price = scanner.nextInt();
        scanner.nextLine(); // 개행
        System.out.print("Enter allergic information: ");
        String allergicIncluded = scanner.nextLine();
        System.out.print("Enter stock: ");
        int stock = scanner.nextInt();
        scanner.nextLine(); // 개행
        System.out.print("Enter description: ");
        String description = scanner.nextLine();

        String query = "INSERT INTO DB2024_MENU (restaurant_id, menu_category, menu_name, price, allergic_included, stock, description) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, restaurantId);
            stmt.setString(2, menuCategory);
            stmt.setString(3, menuName);
            stmt.setInt(4, price);
            stmt.setString(5, allergicIncluded);
            stmt.setInt(6, stock);
            stmt.setString(7, description);

            int rowsInserted = stmt.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("Menu information created successfully.");
                System.out.println("-----------------------------------------------------\n");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void updateMenuInfo() {
        System.out.print("Enter menu ID to update information: ");
        int menuId = scanner.nextInt();
        scanner.nextLine(); // 개행

        System.out.println("What information do you want to update?");
        System.out.println("1. Restaurant ID");
        System.out.println("2. Menu Category");
        System.out.println("3. Menu Name");
        System.out.println("4. Price");
        System.out.println("5. Allergic Information");
        System.out.println("6. Stock");
        System.out.println("7. Description");

        System.out.print("Enter your choice: ");
        int updateChoice = scanner.nextInt();
        scanner.nextLine(); // 개행

        String query;
        PreparedStatement stmt = null;
        switch (updateChoice) {
            case 1 -> {
                System.out.print("Enter new restaurant ID: ");
                int newRestaurantId = scanner.nextInt();
                scanner.nextLine(); // 개행
                query = "UPDATE DB2024_MENU SET restaurant_id = ? WHERE menu_id = ?";
                try {
                    stmt = conn.prepareStatement(query);
                    stmt.setInt(1, newRestaurantId);
                    stmt.setInt(2, menuId);
                    stmt.executeUpdate();
                    System.out.println("Restaurant ID for menu " + menuId + " updated successfully.");
                    System.out.println("-----------------------------------------------------\n");

                } catch (SQLException e) {
                    e.printStackTrace();
                } finally {
                    if (stmt != null) {
                        try {
                            stmt.close();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
            case 2 -> {
                System.out.print("Enter new menu category: ");
                String newCategory = scanner.nextLine();
                query = "UPDATE DB2024_MENU SET menu_category = ? WHERE menu_id = ?";
                try {
                    stmt = conn.prepareStatement(query);
                    stmt.setString(1, newCategory);
                    stmt.setInt(2, menuId);
                    stmt.executeUpdate();
                    System.out.println("Menu category for menu " + menuId + " updated successfully.");
                    System.out.println("-----------------------------------------------------\n");

                } catch (SQLException e) {
                    e.printStackTrace();
                } finally {
                    if (stmt != null) {
                        try {
                            stmt.close();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
            default ->{
                System.out.println("Invalid choice. Please enter a number between 1 and 7.");
                System.out.println("-----------------------------------------------------\n");
            }
        }
    }
    public void deleteMenuInfo() {
        System.out.print("Enter menu ID to delete: ");
        int menuId = scanner.nextInt();
        scanner.nextLine(); // 개행

        String query = "DELETE FROM DB2024_MENU WHERE menu_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, menuId);
            int rowsDeleted = stmt.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("Menu " + menuId + " deleted successfully.");
            } else {
                System.out.println("No menu found with the given ID.");
            }
            System.out.println("-----------------------------------------------------\n");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void searchMenuByCategory() {
        System.out.print("Enter menu category: ");
        String category = scanner.nextLine();
        searchMenuByCategoryAndPrice(category);
    }

    public void searchMenuByPriceRange() {
        System.out.print("Enter minimum price: ");
        int minPrice = scanner.nextInt();
        scanner.nextLine(); // 개행
        System.out.print("Enter maximum price: ");
        int maxPrice = scanner.nextInt();
        scanner.nextLine(); // 개행

        String query = "SELECT * FROM DB2024_MENU WHERE price BETWEEN ? AND ? ORDER BY price";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, minPrice);
            stmt.setInt(2, maxPrice);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                System.out.println("\n-----------------------------------------------------");
                System.out.println("Menu ID: " + rs.getInt("menu_id"));
                System.out.println("Restaurant ID: " + rs.getInt("restaurant_id"));
                System.out.println("Menu Category: " + rs.getString("menu_category"));
                System.out.println("Menu Name: " + rs.getString("menu_name"));
                System.out.println("Price: " + rs.getInt("price"));
                System.out.println("Allergic Included: " + rs.getString("allergic_included"));
                System.out.println("Stock: " + rs.getInt("stock"));
                System.out.println("Description: " + rs.getString("description"));
                System.out.println("-----------------------------------------------------\n");
            }
            System.out.println("-----------------------------------------------------\n");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void searchMenuByName() {
        System.out.print("Enter keyword to search in menu name: ");
        String keyword = scanner.nextLine();

        String query = "SELECT * FROM DB2024_MENU WHERE menu_name LIKE ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, "%" + keyword + "%"); // Add wildcards for partial search
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                System.out.println("\n-----------------------------------------------------");
                System.out.println("Menu ID: " + rs.getInt("menu_id"));
                System.out.println("Restaurant ID: " + rs.getInt("restaurant_id"));
                System.out.println("Menu Category: " + rs.getString("menu_category"));
                System.out.println("Menu Name: " + rs.getString("menu_name"));
                System.out.println("Price: " + rs.getInt("price"));
                System.out.println("Allergic Included: " + rs.getString("allergic_included"));
                System.out.println("Stock: " + rs.getInt("stock"));
                System.out.println("Description: " + rs.getString("description"));
                System.out.println("-----------------------------------------------------\n");
            }
            System.out.println("-----------------------------------------------------\n");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void searchMenuByRestaurantID(){
        System.out.print("Enter keyword to search in menu name: ");
        String keyword = scanner.nextLine();

        String query = "SELECT * FROM DB2024_MENU WHERE restaurant_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, "%" + keyword + "%"); // Add wildcards for partial search
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                System.out.println("\n-----------------------------------------------------");
                System.out.println("Menu ID: " + rs.getInt("menu_id"));
                System.out.println("Restaurant ID: " + rs.getInt("restaurant_id"));
                System.out.println("Menu Category: " + rs.getString("menu_category"));
                System.out.println("Menu Name: " + rs.getString("menu_name"));
                System.out.println("Price: " + rs.getInt("price"));
                System.out.println("Allergic Included: " + rs.getString("allergic_included"));
                System.out.println("Stock: " + rs.getInt("stock"));
                System.out.println("Description: " + rs.getString("description"));
                System.out.println("-----------------------------------------------------\n");
            }
            System.out.println("-----------------------------------------------------\n");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void searchMenuByCategoryAndPrice(String category) {
        String query = "SELECT * FROM DB2024_MENU WHERE menu_category = ? ORDER BY price";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, category);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                System.out.println("\n-----------------------------------------------------");
                System.out.println("Menu ID: " + rs.getInt("menu_id"));
                System.out.println("Restaurant ID: " + rs.getInt("restaurant_id"));
                System.out.println("Menu Category: " + rs.getString("menu_category"));
                System.out.println("Menu Name: " + rs.getString("menu_name"));
                System.out.println("Price: " + rs.getInt("price"));
                System.out.println("Allergic Included: " + rs.getString("allergic_included"));
                System.out.println("Stock: " + rs.getInt("stock"));
                System.out.println("Description: " + rs.getString("description"));
                System.out.println("-----------------------------------------------------\n");
            }
            System.out.println("-----------------------------------------------------\n");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
