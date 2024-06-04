package DB2024Team11;

import java.sql.*;
import java.util.Scanner; 
/* 
 * 작성자 : Sanna Ascard Soederstroem(2271001)
 * Customer 클래스는 손님 정보를 관리하는 기능을 제공한다. 
 */
public class Customer {
	private final Connection conn;
	private Scanner scanner;
	
	public Customer(Connection conn, Scanner scanner){
        this.conn = conn;
        this.scanner = scanner;
	}
   	/*
   	* handleCustomerOperations 메서드는 고객 관리 메뉴를 표시하고 사용자의 선택을 처리한다.
    	*/
	public void handleCustomerOperations() {
		boolean running = true;
		while(running) {
			System.out.println("\n==== Customer Menu ====");
			System.out.println("1. Login");
			System.out.println("2. Register Customer Account");

			System.out.print("Enter your choice: ");
			int choice = scanner.nextInt();
			scanner.nextLine();  // 입력 버퍼 비우기

			switch (choice) {
			case 1 -> loginUser();
			case 2 -> signUpUser();
			case 3 -> running = false;
			default -> System.out.println("Invalid choice. Please enter a number between 1 and 3.");
			}
		 }
	}
	    
	/*
	 * handleAdminOperations 메서드는 고객 관리 메뉴를 표시하고 사용자의 선택을 처리한다.
	 */
	public void handleAdminOperations() {
		boolean running = true;
		while(running) {
			System.out.println("\n==== Admin Menu ====");
			System.out.println("1. Customer Management");
			System.out.println("2. View Customer Birthdays (Send Coupon)");
			System.out.println("3. Back");

			System.out.print("Enter your choice: ");
			int choice = scanner.nextInt();
			scanner.nextLine();  // 입력 버퍼 비우기

			switch (choice) {
			case 1 -> manageCustomers();
			case 2 -> viewBirthdays();
			default -> System.out.println("Invalid choice. Please enter a number between 1 and 3.");
			}
		}
	}
	 
   	/*
    	* loginUser 메서드는 고객의 로그인 기능을 제공한다.
	* 고객ID 받고 데이터베이스에서 고객 정보를 조회한다.
	*/
	private void loginUser() {
		System.out.print("Enter your customer ID: ");
		int customerId = scanner.nextInt();
		scanner.nextLine(); // Consume newline
		try {
			String sql = "SELECT * FROM DB2024_CUSTOMER WHERE customer_id = ?";
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, customerId);
			ResultSet rs = pstmt.executeQuery();
			
			if (rs.next()) {
				System.out.println("Customer Information:");
				System.out.println("Name: " + rs.getString("customer_name"));
				System.out.println("Birthday: " + rs.getDate("birthday"));
				System.out.println("Phone: " + rs.getString("phone_number"));
				
				System.out.println("1. Update Info");
				System.out.println("2. Delete Account");
				System.out.println("3. Exit");
				int choice = scanner.nextInt();
				scanner.nextLine(); // Consume newline
				
				switch (choice) {
				case 1 -> updateUser(customerId);
				case 2 -> deleteUser(customerId);
				case 3 -> System.out.println("Goodbye!");
				default -> System.out.println("Invalid selection.");
				}
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
	/*
 	 * updateUser 메서드는 고객의 전화번호를 업데이트하는 기능을 제공한다.
	 * 새로운 전화번호를 입력받고 데이터베이스에서  해당 고객의 정보를 업데이트한다.
	 */
	 private void updateUser(int customerId) {
	        System.out.print("Enter new phone number: ");
	        String newPhone = scanner.nextLine();

	        try {
	            String sql = "UPDATE DB2024_CUSTOMER SET phone_number = ? WHERE customer_id = ?";
	            PreparedStatement pstmt = conn.prepareStatement(sql);
	            pstmt.setString(1, newPhone);
	            pstmt.setInt(2, customerId);
	            int rowsAffected = pstmt.executeUpdate();
	            if (rowsAffected > 0) {
	                System.out.println("Information updated successfully.");
	            } else {
	                System.out.println("Failed to update information.");
	            }
	            pstmt.close();
	        } catch (SQLException e) {
	            System.out.println("Error updating customer information:");
	            e.printStackTrace();
	        }
	    }
	 /*
	  * deleteUser 메서드는 고객의 계정을 삭제하는 기능을 제공한다.
	  * 데이터베이스에서 해당 고객의 정보를 삭제한다.
	  */
	 private void deleteUser(int customerId) {
	     try {
	         String sql = "DELETE FROM DB2024_CUSTOMER WHERE customer_id = ?";
	         PreparedStatement pstmt = conn.prepareStatement(sql);
	         pstmt.setInt(1, customerId);
	         int rowsAffected = pstmt.executeUpdate();
	         if (rowsAffected > 0) {
	             System.out.println("Account deleted successfully.");
	         } else {
	             System.out.println("Failed to delete account.");
	         }
	         pstmt.close();
	     } catch (SQLException e) {
		     System.out.println("Error deleting customer information:");
		     e.printStackTrace();
	     }
	 }
	 /*
	  * signUpUser 메서드는 새로운 고객을 등록하는 기능을 제공한다.
	  * 이름, 생일, 전화번호를 입력받고 데이터베이스에 새로운 고객 정보를 삽입한다.
	  */
	 private void signUpUser() {
		 System.out.print("Enter your name: ");
	     String name = scanner.nextLine();
	     System.out.print("Enter your birthday (YYYY-MM-DD): ");
	     String birthday = scanner.nextLine();
	     System.out.print("Enter your phone number: ");
	     String phone = scanner.nextLine();
	    
	     try {
	    	 /// 단계 1: 새로운 고객 ID 생성
	    	 String idSql = "SELECT MAX(customer_id) FROM DB2024_CUSTOMER";
	    	 Statement idStmt = conn.createStatement();
	    	 ResultSet idRs = idStmt.executeQuery(idSql);
	    	 int newCustomerId = 1; // 기본값은 1로 설정 (고객이 없는 경우)
	    	 if (idRs.next()) {
	    		 newCustomerId = idRs.getInt(1) + 1;
	         }
	    	 idRs.close();
	    	 idStmt.close();
	    
	    	 // 단계 2: 생성된 ID를 사용하여 새로운 고객 정보 삽입
	    	 String sql = "INSERT INTO DB2024_CUSTOMER (customer_id, customer_name, birthday, phone_number) VALUES (?, ?, ?, ?)";
	    	 PreparedStatement pstmt = conn.prepareStatement(sql);
	    	 pstmt.setInt(1, newCustomerId);
	    	 pstmt.setString(2, name);
	    	 pstmt.setDate(3, Date.valueOf(birthday));
	    	 pstmt.setString(4, phone);
	    	 int rowsAffected = pstmt.executeUpdate();
	    	 if (rowsAffected > 0) {
	    		 System.out.println("Sign up successful. Your customer ID is " + newCustomerId);
	         } else {
	        	 System.out.println("Failed to sign up.");
	         }
		     pstmt.close();
	     }catch (SQLException e) {
		     System.out.println("Error signing up customer:");
		     e.printStackTrace();
	     }
	 }
	    
	 /*
	  * manageCustomers 메서드는 특정 고객의 정보를 조회하는 기능을 제공한다.
	  * 고객 ID를 입력받고, 데이터베이스에서 고객 정보를 조회한다.
	  */
	 private void manageCustomers() {
		 System.out.print("Enter customer ID to search: ");
		 int customerId = scanner.nextInt();
		 scanner.nextLine(); // Consume newline

		 try {
			 String sql = "SELECT * FROM DB2024_CUSTOMER WHERE customer_id = ?";
			 PreparedStatement pstmt = conn.prepareStatement(sql);
			 pstmt.setInt(1, customerId);
			 ResultSet rs = pstmt.executeQuery();
			 if (rs.next()) {
				 System.out.println("Customer Information:");
				 System.out.println("Name: " + rs.getString("customer_name"));
				 System.out.println("Birthday: " + rs.getDate("birthday"));
				 System.out.println("Phone: " + rs.getString("phone_number"));
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
	 private void viewBirthdays() {
		 try {
			 String sql = "SELECT * FROM DB2024_CUSTOMER WHERE MONTH(birthday) = MONTH(CURDATE()) AND DAY(birthday) = DAY(CURDATE())";
			 PreparedStatement pstmt = conn.prepareStatement(sql);
			 ResultSet rs = pstmt.executeQuery();
			 while (rs.next()) {
				 System.out.println("Customer ID: " + rs.getInt("customer_id"));
				 System.out.println("Name: " + rs.getString("customer_name"));
				 System.out.println("Birthday: " + rs.getDate("birthday"));
				 System.out.println("Phone: " + rs.getString("phone_number"));
				 // Logic to send coupon
				 System.out.println("Coupon sent to " + rs.getString("phone_number"));
			 }
			 rs.close();
			 pstmt.close();
		 } catch (SQLException e) {
			 System.out.println("Error retrieving birthday information:");
			 e.printStackTrace();
		 }
	 }
}
