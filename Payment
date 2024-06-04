package DB2024Team11;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

/**
 * Payment 클래스는 결제를 처리하는 기능 제공.
 */
public class Payment {
    private final Connection conn;
    private final Scanner scanner;

    /**
     * Payment 클래스의 생성자.
     * @param conn    데이터베이스 연결 객체
     * @param scanner 사용자 입력을 받기 위한 Scanner 객체
     */
    public Payment(Connection conn, Scanner scanner) {
        this.conn = conn;
        this.scanner = scanner;
    }
    
    // ----------------------- 초기 매뉴얼 ------------------------------
    /**
     * 결제 처리 매뉴얼을 표시하고 사용자의 선택에 따라 작업 처리
     */
    public void cus_handleOperations() {
        boolean running = true;
        while (running) {
            System.out.println("\n==== Payment Management ====");
            System.out.println("1. View Receipt");
            System.out.println("2. Back");

            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine();  // 입력 버퍼 비우기

            switch (choice) {
                case 1 -> cus_getReceipt();
                case 2 -> running = false;
                default -> System.out.println("Invalid choice. Please enter a number between 1 and 2.");
            }
        }
    }
    public void res_handleOperations() {
        boolean running = true;
        while (running) {
            System.out.println("\n==== Payment Management ====");
            System.out.println("1. Make Payment Information");
            System.out.println("2. View Payment Information");
            System.out.println("3. Delete Payment Information");
            System.out.println("4. Change Payment Information");
            System.out.println("5. View Receipt");
            System.out.println("6. Back");

            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine();  // 입력 버퍼 비우기

            switch (choice) {
                case 1 -> makePayment();
                case 2 -> getPayment();
                case 3 -> deletePayment();
                case 4 -> changePayment();
                case 5 -> res_getReceipt();
                case 6 -> running = false;
                default -> System.out.println("Invalid choice. Please enter a number between 1 and 6.");
            }
        }
    }
    
    
    //  ---------------------- 영수증 뷰 관련 -----------------------------
    /**
     * [고객] 고객 정보와 날짜를 입력받아 영수증 조회.
     */
    private void cus_getReceipt() {
        System.out.print("Enter customer name: ");
        String customerName = scanner.nextLine();

        System.out.print("Enter phone number: ");
        String phoneNumber = scanner.nextLine();
        
        System.out.print("Enter date(YYYY-MM-DD): ");
        String date = scanner.nextLine();
        
        int payment_id = 0;
        try {
            String sql = "SELECT P.payment_id FROM DB2024_PAYMENT P "
                       + "JOIN DB2024_RESERVATION RV ON P.reservation_id = RV.reservation_id "
                       + "JOIN DB2024_CUSTOMER C ON RV.customer_id = C.customer_id "
                       + "WHERE C.customer_name = '?' AND C.phone_number = '?' "
                       + "AND DATE(P.payment_date) = '?'";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, customerName);
            pstmt.setString(2, phoneNumber);
            pstmt.setString(3, date);
            
            ResultSet rs = pstmt.executeQuery();
            if (rs.next())
            	payment_id = rs.getInt("payment_id");

            rs.close();
            pstmt.close();
        } catch (SQLException e) {
            System.out.println("Error retrieving payment information:");
            e.printStackTrace();
        }
        
        if (payment_id > 0) {
            printReceipt(payment_id);
        } else {
            System.out.println("No payment found for the given details.");
        }
    }
    
    /**
     * [관리자] 결제번호를 입력받아 데이터베이스에서 영수증 조회.
     */
    private void res_getReceipt() {
        System.out.print("Enter payment ID: ");
        int payment_id = scanner.nextInt();
        scanner.nextLine();	//버퍼 지우기

        printReceipt(payment_id);
    }
    
    /**
     * 결제ID를 이용해 영수증 출력
     */
    private void printReceipt(int payment_id) {
        try {
            String sql = "SELECT * FROM Receipt WHERE payment_id = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, payment_id);
            ResultSet rs = pstmt.executeQuery();
            
            while(rs.next()) {
                System.out.println("Payment ID: " + rs.getInt("payment_id"));
                System.out.println("Payment Date: " + rs.getDate("payment_date"));
                System.out.println("Payment Type: " + rs.getString("payment_type"));
                System.out.println("Payment Amount: " + rs.getInt("payment_amount"));
                if (rs.getInt("cash_receipt_requested") == 1) {
                    System.out.println("Cash Receipt Requested: O");
                } else {
                    System.out.println("Cash Receipt Requested: X");
                }
                System.out.println("------------------------------");
                System.out.println("Restaurant Id: " + rs.getInt("restaurant_id"));
                System.out.println("Restaurant Name: " + rs.getString("restaurant_name"));
                System.out.println("Restaurant Location: " + rs.getString("restaurant_location"));
                System.out.println("Restaurant Contact: " + rs.getString("restaurant_contact"));
                System.out.println("[Menu]");
                System.out.println(rs.getString("menu_name") + "\t\t\t" + rs.getInt("menu_price"));
            }
      
            rs.close();
            pstmt.close();
        } catch (SQLException e) {
            System.out.println("Error printing receipt information:");
            e.printStackTrace();
        }
    }


    
    // ------------------------ PAYMENT 테이블 관련 ----------------------------
    /**
     * [관리자] 식당 이름과 예약번호를 입력받아 결제 정보 생성.
     */
    private void makePayment() {
	    try {
	        System.out.print("Enter reservation ID: ");
	        int reservationId = scanner.nextInt();
	        scanner.nextLine();
	
	        System.out.print("Enter payment date (YYYY-MM-DD): ");
	        String paymentDate = scanner.nextLine();
	
	        System.out.print("Enter payment type: ");
	        String paymentType = scanner.nextLine();
	
	        System.out.print("Enter payment amount: ");
	        int paymentAmount = scanner.nextInt();
	        scanner.nextLine();
	
	        System.out.print("Enter cash receipt requested: ");
	        int cash_receipt_requested = scanner.nextInt();
	        scanner.nextLine();
	        
	        String sql = "INSERT INTO DB2024_PAYMENT (reservation_id, payment_date, payment_type, payment_amount, cash_receipt_requested) VALUES (?, ?, ?, ?, ?)";
	        PreparedStatement pstmt = conn.prepareStatement(sql);
	        pstmt.setInt(1, reservationId);
	        pstmt.setString(2, paymentDate);
	        pstmt.setString(3, paymentType);
	        pstmt.setInt(4, paymentAmount);
	        pstmt.setInt(5, cash_receipt_requested);
	        pstmt.executeUpdate();
	        
	        System.out.println("Completed.");
	        pstmt.close();
	    } catch (SQLException e) {
	        System.out.println("Error making payment: ");
	        e.printStackTrace();
	    }
    }
    
    /**
     * [관리자] 결제번호를 입력받아 결제 정보를 조회.
     */
    private void getPayment() {
        System.out.print("Enter payment ID: ");
        int payment_id = scanner.nextInt();
        
        printPaymentInfo(payment_id);
    }
    
    /**
     * [관리자] 결제번호를 입력받아 데이터베이스에서 결제 정보를 확인 후 삭제.
     */
    private void deletePayment() {
        System.out.print("Enter payment ID: ");
        int payment_id = scanner.nextInt();
        printPaymentInfo(payment_id);
        
        System.out.print("Are you sure you want to delete this payment information?(O/X) :");
        String ans = scanner.nextLine();
        if(ans.equals("O")) {
        	try {
	        	String sql = "DELETE FROM DB2024_PAYMENT WHERE payment_id = ?";
	            PreparedStatement pstmt = conn.prepareStatement(sql);
	            pstmt.setInt(1, payment_id);
	            pstmt.executeUpdate();
	            System.out.println("Completed.");
        	} catch(SQLException e) {
        		System.out.println("Error deleting payment information:");
                e.printStackTrace();
        	}
        }
        else
        	System.out.println("Return to the initial screen\n");
    }
    
    /**
     * [관리자] 결제번호를 입력받아 결제 정보를 확인 후 정보 변경.
     */
    private void changePayment() {
    	System.out.print("Enter payment ID: ");
        int payment_id = scanner.nextInt();
        printPaymentInfo(payment_id);
        
        System.out.print("Are you sure you want to change this payment information?(O/X) :");
        String ans = scanner.nextLine();
        if(ans.equals("O")) {
            System.out.println("1. payment_type ");
            System.out.println("2. cash_receipt_requested");
            System.out.println("3. payment_amount");
            System.out.print("What information do you want to change(1~3): ");
            int choice = scanner.nextInt();
            scanner.nextLine();
            
            System.out.println("Enter the informatino after changing: ");
            int change1 = 0;		String change2 = "";
            
            try {
                switch(choice) {
                case 1:
                	change2 = scanner.nextLine();
                	String sql = "UPDATE DB2024_PAYMENT SET payment_type = '?' WHERE payment_id = ?";
                	PreparedStatement pstmt = conn.prepareStatement(sql);
                	pstmt.setString(1, change2);
                	pstmt.setInt(2, payment_id);
                	pstmt.executeUpdate();
                	break;
                case 2:
                	change1 = scanner.nextInt();	scanner.nextLine();
                	sql = "UPDATE DB2024_PAYMENT SET cash_receipt_requested = ? WHERE payment_id = ?";
                	pstmt = conn.prepareStatement(sql);
                	pstmt.setInt(1, change1);
                	pstmt.setInt(2, payment_id);
                	pstmt.executeUpdate();
                	break;
                case 3:
                	change1 = scanner.nextInt();	scanner.nextLine();
                	sql = "UPDATE DB2024_PAYMENT SET payment_amount = ? WHERE payment_id = ?";
                	pstmt = conn.prepareStatement(sql);
                	pstmt.setInt(1, change1);
                	pstmt.setInt(2, payment_id);
                	pstmt.executeUpdate();
                	break;
                default:
                	System.out.println("Invalide input");
                }
            } catch(SQLException e) {
        		System.out.println("Error changing payment information:");
                e.printStackTrace();
            }
            
            System.out.println("Completed.");
        }
        else
        	System.out.println("Return to the initial screen\n");
    }
   
    /**
     * 결제 ID를 이용해 결제 정보 출력
     */
    private void printPaymentInfo(int payment_id) {
        try {
            String sql = "SELECT * FROM DB2024_PAYMENT WHERE payment_id = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, payment_id);
            ResultSet rs = pstmt.executeQuery();
            
            while(rs.next()) {
	               System.out.println("Payment ID: " + rs.getInt("payment_id"));
	               System.out.println("Payment Date: " + rs.getDate("payment_date"));
	               System.out.println("Payment Type: " + rs.getString("payment_type"));
	               System.out.println("Payment Amount: " + rs.getInt("payment_amount"));
	               if(rs.getInt("cash_receipt_requested") == 1)	System.out.println("Cash receipt Requested: O");
	               else										System.out.println("Cash receipt Requested: X");
        	}
            
            rs.close();
            pstmt.close();
        } catch (SQLException e) {
         System.out.println("Error printing payment information:");
         e.printStackTrace();
     }
    }
}
