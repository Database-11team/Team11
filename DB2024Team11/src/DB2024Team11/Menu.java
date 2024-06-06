package DB2024Team11;

import java.sql.*;
import java.util.Scanner;

public class Menu {
    private final Connection conn;
    private final Scanner scanner;

    /**
     * Payment 클래스의 생성자.
     * @param conn    데이터베이스 연결 객체
     * @param scanner 사용자 입력을 받기 위한 Scanner 객체
     */
    public Menu(Connection conn, Scanner scanner) {
        this.conn = conn;
        this.scanner = scanner;
    }

    /*
    * 사용자별 Operation 정의
    * Manager : INSERT, UPDATE, DELETE 모두 가능
    * */
    public void handleAdminOperations() {
        boolean running = true;
        while (running) {
            System.out.println("\n==== Menu Manager ====");
            System.out.println("1. View Menu Information by ID");
            System.out.println("2. Create Menu Information");
            System.out.println("3. Update Menu Information");
            System.out.println("4. Delete Menu Information");
            System.out.println("5. Search Menu Information");
            System.out.println("6. Back");

            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // 개행

            switch (choice) {
                case 1 -> getMenu();
                case 2 -> createMenu();
                case 3 -> updateMenu();
                case 4 -> deleteMenu();
                case 5 -> searchMenu();
                case 6 -> running = false;
                default -> System.out.println("Invalid choice. Please enter a number between 1 and 6.");
            }
        }
    }
    public void handleCustomerOperations() {
        boolean running = true;
        while (running) {
            System.out.println("\n==== Menu Customer ====");
            System.out.println("1. View Menu Information by ID");
            System.out.println("2. Search Menu Information");
            System.out.println("3. Back");

            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // 개행

            switch (choice) {
                case 1 -> getMenu();
                case 2 -> searchMenu();
                case 3 -> running = false;
                default -> System.out.println("Invalid choice. Please enter a number between 1 and 6.");
            }
        }
    }
    /*
    *  CREATE, READ, UPDATE, DELETE BY ID(PK)
    * */

    /*
    * 1.
    * READ MENU BY ID
    * */
    private void getMenu() {
        System.out.print("Enter menu ID to view information: ");
        int menuId = scanner.nextInt();
        scanner.nextLine(); // 개행

        getMenuInfo(menuId);
    }
    private void getMenuInfo(int menuId) {
        String query = "SELECT * FROM DB2024_MENU WHERE menu_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, menuId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                System.out.println("-----------------------------------------------------");
                System.out.println("Menu ID: " + rs.getInt("menu_id"));
                System.out.println("Restaurant ID: " + rs.getInt("restaurant_id"));
                System.out.println("Menu Category: " + rs.getString("menu_category"));
                System.out.println("Menu Name: " + rs.getString("menu_name"));
                System.out.println("Price: " + rs.getInt("price"));
                System.out.println("Allergic Included: " + rs.getString("allergic_included"));
                System.out.println("Stock: " + rs.getInt("stock"));
                System.out.println("Description: " + rs.getString("description"));
                System.out.println("-----------------------------------------------------");
            } else {
                System.out.println("No menu found with the given ID.");
                System.out.println("-----------------------------------------------------\n");
            }
            System.out.println();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    /*
    *
    * CREATE MENU
    * */
    private void createMenu() {
        try {
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

            String query = "INSERT INTO DB2024_MENU (restaurant_id, menu_category, menu_name, price, allergic_included, stock, description) VALUES (?, ?, ?, ?, ?, ?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
                stmt.setInt(1, restaurantId);
                stmt.setString(2, menuCategory.isEmpty() ? null : menuCategory);
                stmt.setString(3, menuName.isEmpty() ? null : menuName);
                stmt.setInt(4, price);
                stmt.setString(5, allergicIncluded.isEmpty() ? null : allergicIncluded);
                stmt.setInt(6, stock);
                stmt.setString(7, description.isEmpty() ? null : description);

                int rowsInserted = stmt.executeUpdate();
                if (rowsInserted > 0) {
                    System.out.println("Menu information created successfully.");
                    try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                        if (generatedKeys.next()) {
                            int menuId = generatedKeys.getInt(1);
                            getMenuInfo(menuId);
                        }
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Rolling back data here....");
            try {
                if (conn != null) {
                    conn.rollback();
                }
            } catch (SQLException se2) {
                se2.printStackTrace();
            }
        }
    }

    /*
    *
    * UPDATE BY ID
    * */
    private void updateMenu() {
        System.out.print("Enter menu ID to update information: ");
        int menuId = scanner.nextInt();
        scanner.nextLine(); // 개행

        System.out.print("Enter new restaurant ID (leave blank to keep current): ");
        String newRestaurantIdInput = scanner.nextLine();
        Integer newRestaurantId = null; // 초기값은 null로 설정

        // 사용자가 입력한 값이 비어있지 않으면 정수로 변환하여 저장
        if (!newRestaurantIdInput.isBlank()) {
            newRestaurantId = Integer.parseInt(newRestaurantIdInput);
        }

        System.out.print("Enter new menu category (leave blank to keep current): ");
        String newCategory = scanner.nextLine();
        System.out.print("Enter new menu name (leave blank to keep current): ");
        String newName = scanner.nextLine();
        System.out.print("Enter new price (-1 to keep current): ");
        String newPriceInput = scanner.nextLine();
        Integer newPrice = null; // 초기값은 null로 설정

        // 사용자가 입력한 값이 비어있지 않으면 정수로 변환하여 저장
        if (!newPriceInput.isBlank()) {
            newPrice = Integer.parseInt(newPriceInput);
        }

        System.out.print("Enter new allergic information (leave blank to keep current): ");
        String newAllergic = scanner.nextLine();
        System.out.print("Enter new stock (-1 to keep current): ");
        String newStockInput = scanner.nextLine();
        Integer newStock = null; // 초기값은 null로 설정

        // 사용자가 입력한 값이 비어있지 않으면 정수로 변환하여 저장
        if (!newStockInput.isBlank()) {
            newStock = Integer.parseInt(newStockInput);
        }

        System.out.print("Enter new description (leave blank to keep current): ");
        String newDescription = scanner.nextLine();

        String query = "UPDATE DB2024_MENU " +
                "SET "
                + "restaurant_id = IF(? IS NOT NULL, ?, restaurant_id), "
                + "menu_category = IF(? != '', ?, menu_category), "
                + "menu_name = IF(? != '', ?, menu_name), "
                + "price = IF(? IS NOT NULL, ?, price), "
                + "allergic_included = IF(? != '', ?, allergic_included), "
                + "stock = IF(? IS NOT NULL, ?, stock), "
                + "description = IF(? != '', ?, description) "
                + "WHERE menu_id = ?";

        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setObject(1, newRestaurantId); // null일 경우도 처리하기 위해 setObject 사용
            stmt.setObject(2, newRestaurantId);
            stmt.setString(3, newCategory);
            stmt.setString(4, newCategory);
            stmt.setString(5, newName);
            stmt.setString(6, newName);
            stmt.setObject(7, newPrice);
            stmt.setObject(8, newPrice);
            stmt.setString(9, newAllergic);
            stmt.setString(10, newAllergic);
            stmt.setObject(11, newStock);
            stmt.setObject(12, newStock);
            stmt.setString(13, newDescription);
            stmt.setString(14, newDescription);
            stmt.setInt(15, menuId);

            int rowsUpdated = stmt.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Menu information updated successfully.");
                getMenuInfo(menuId);
                System.out.println("-----------------------------------------------------\n");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /*
    *
    * DELETE MENU BY ID
    * */
    private void deleteMenu() {
        boolean isMenuDeleted = false;
        do {
            System.out.print("Enter menu ID to delete (or enter 'q' to quit): ");

            try {
                String input = scanner.nextLine();
                if (input.equalsIgnoreCase("q")) {
                    break;
                }
                int menuId = Integer.parseInt(input);

                String query = "DELETE FROM DB2024_MENU WHERE menu_id = ?";
                try (PreparedStatement stmt = conn.prepareStatement(query)) {
                    stmt.setInt(1, menuId);
                    int rowsDeleted = stmt.executeUpdate();
                    if (rowsDeleted > 0) {
                        System.out.println("Menu " + menuId + " deleted successfully.");
                        isMenuDeleted = true;
                        break;
                    } else {
                        System.out.println("No menu found with the given ID.");
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                    System.out.println("An error occurred while deleting the menu. Please try again.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid menu ID (integer) or 'q' to quit.");
            }
        } while (!isMenuDeleted);
        System.out.println("-----------------------------------------------------\n");
    }

//---------------------------- SEARCH ---------------------------------------

    private void searchMenu() {
        boolean searchRunning = true;
        while (searchRunning) {
            System.out.println("\n==== Search Menu Options ====");
            System.out.println("1. Search by Category");
            System.out.println("2. Search by Price Range");
            System.out.println("3. Search by Menu Name (keyword)");
            System.out.println("4. Search by Restaurant");
            System.out.println("5. Back");

            System.out.print("Enter your choice: ");
            int searchChoice = scanner.nextInt();
            scanner.nextLine(); // 개행

            switch (searchChoice) {
                case 1 -> searchMenuByCategory();
                case 2 -> searchMenuByPriceRange();
                case 3 -> searchMenuByName();
                case 4 -> searchMenuByRestaurant();
                case 5 -> searchRunning = false;
                default -> System.out.println("Invalid choice. Please enter a number between 1 and 4.");
            }
        }
    }
    private void searchMenuByCategory() {
        System.out.print("Enter Category to search in menu: ");
        String category = scanner.nextLine();

        String query = "SELECT * FROM DB2024_MENU WHERE menu_category = ? ORDER BY price";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, category);
            try (ResultSet rs = stmt.executeQuery()) {
                processSearchResults(rs);
            }
        } catch (SQLException e) {
            System.err.println("Error searching menu by category: " + e.getMessage());
        }
    }
    /*
    * 가격대를 통해 메뉴를 검색할 수 있다.
    * 최소금액/ 최대금액을 설정할 수 있고, 값이 없는 경우 null사용할 수 있다.
    * 둘 모두를 설정하지 않으면 메세지 출력 후 종료
    * */
    private void searchMenuByPriceRange() {
        System.out.print("Enter minimum price (or press Enter for no minimum): ");
        String minPriceInput = scanner.nextLine();
        Integer minPrice = null;
        if (!minPriceInput.isEmpty()) {
            minPrice = Integer.parseInt(minPriceInput);
        }

        System.out.print("Enter maximum price (or press Enter for no maximum): ");
        String maxPriceInput = scanner.nextLine();
        Integer maxPrice = null;
        if (!maxPriceInput.isEmpty()) {
            maxPrice = Integer.parseInt(maxPriceInput);
        }

        String query;
        if (minPrice != null && maxPrice != null) {
            query = "SELECT * FROM DB2024_MENU WHERE price BETWEEN ? AND ? ORDER BY price";
        } else if (minPrice != null) {
            query = "SELECT * FROM DB2024_MENU WHERE price >= ? ORDER BY price";
        } else if (maxPrice != null) {
            query = "SELECT * FROM DB2024_MENU WHERE price <= ? ORDER BY price";
        } else {
            System.out.println("No price range specified. Exiting search.");
            return;
        }

        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            if (minPrice != null && maxPrice != null) {
                stmt.setInt(1, minPrice);
                stmt.setInt(2, maxPrice);
            } else if (minPrice != null) {
                stmt.setInt(1, minPrice);
            } else if (maxPrice != null) {
                stmt.setInt(1, maxPrice);
            }

            ResultSet rs = stmt.executeQuery();
            processSearchResults(rs);
            System.out.println("-----------------------------------------------------\n");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void searchMenuByName() {
        System.out.print("Enter keyword to search in menu name: ");
        String keyword = scanner.nextLine();
        // 입력한 키워드에 공백있는 경우에 대해서 토큰 분할
        String[] tokens = keyword.split("\\s+");

        // string builder를 사용해서 입력받은 토큰으로 쿼리 생성.
        // 토큰들은 And로 연결함
        StringBuilder queryBuilder = new StringBuilder("SELECT * FROM DB2024_MENU WHERE ");
        for (int i = 0; i < tokens.length; i++) {
            queryBuilder.append("menu_name LIKE ?");
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
            System.out.println("-----------------------------------------------------\n");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    private void searchMenuByRestaurant(){
        System.out.print("Enter Restaurant ID (or press Enter for no ID): ");
        System.out.print("Enter Restaurant Name (or press Enter for no ID): ");
        System.out.print("Enter Restaurant Location (or press Enter for no ID): ");
        String restaurantName = scanner.nextLine();

        String query = "SELECT M.menu_id, M.restaurant_id, M.menu_category, M.menu_name, M.price, M.allergic_included, M.stock, M.description, R.restaurant_name, R.location " +
                "FROM DB2024_MENU M " +
                "JOIN DB2024_RESTAURANT R ON M.restaurant_id = R.restaurant_id " +
                "WHERE R.restaurant_name LIKE ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, "%" + restaurantName + "%");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                System.out.println("\n-----------------------------------------------------");
                System.out.println("Menu ID: " + rs.getInt("menu_id"));
                System.out.println("Restaurant ID: " + rs.getInt("restaurant_id"));
                System.out.println("Restaurant Name: " + rs.getString("restaurant_name"));
                System.out.println("Location: " + rs.getString("location"));
                System.out.println("Menu Category: " + rs.getString("menu_category"));
                System.out.println("Menu Name: " + rs.getString("menu_name"));
                System.out.println("Price: " + rs.getInt("price"));
                System.out.println("Allergic Included: " + rs.getString("allergic_included"));
                System.out.println("Stock: " + rs.getInt("stock"));
                System.out.println("Description: " + rs.getString("description"));
                System.out.println("-----------------------------------------------------");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    private void processSearchResults(ResultSet rs) throws SQLException {
        while (rs.next()) {
            System.out.println("-----------------------------------------------------");
            System.out.println("Menu ID: " + rs.getInt("menu_id"));
            System.out.println("Restaurant ID: " + rs.getInt("restaurant_id"));
            System.out.println("Menu Category: " + rs.getString("menu_category"));
            System.out.println("Menu Name: " + rs.getString("menu_name"));
            System.out.println("Price: " + rs.getInt("price"));
            System.out.println("Allergic Included: " + rs.getString("allergic_included"));
            System.out.println("Stock: " + rs.getInt("stock"));
            System.out.println("Description: " + rs.getString("description"));
            System.out.println("-----------------------------------------------------");
        }
        System.out.println();
    }
}
