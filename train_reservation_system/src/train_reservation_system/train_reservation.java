package train_reservation_system;

import java.sql.Connection;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class train_reservation {
	
	public static Connection connectDB(){
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/train_reservation", "root", "root");
            return conn;
        } 
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
	
	public static boolean userVerification(int id) throws SQLException {
		boolean user = false;
		
		Connection conn = null;
		conn = connectDB();
		String sql = "Select count(*) from user_dts where login_id = ?";
		PreparedStatement ps = conn.prepareStatement(sql);
		ps.setInt(1, id);
		
		ResultSet rs = ps.executeQuery();
		
		if(rs.next()) {
			int count = rs.getInt(1);
			if(count > 0) {
				user = true;
			}
		}
		
		rs.close();
        ps.close();
        conn.close();
		
		return user;
	}
	public static void main(String[] args) throws SQLException{
        Scanner sc = new Scanner(System.in);

        Connection conn = null;
        PreparedStatement ps = null;
        conn = connectDB();

        int choice;
        do {  
            System.out.println("Enter your choice");  
            System.out.println("1. Enter Profile");  
            System.out.println("2. Enter Train Choice");  
            System.out.println("3. Display Person Information");  
            System.out.println("4. Display Complete Information");  
            System.out.println("5. Display Train Status");  
            System.out.print("6. Exit\n");
            choice = sc.nextInt();

            switch(choice){
                case 1: 
                System.out.println("Enter Your name: ");
                String name = sc.next();
                System.out.println("Enter your age: ");
                int age = sc.nextInt();
               
                int login_id = (int)((Math.random()*9000000) + 1000000);
                
                try{
                    String sql = "insert into user_dts(name, age, login_id) values(?,?,?)";
                    ps = conn.prepareStatement(sql);
                    ps.setString(1, name);
                    ps.setInt(2, age);
                    ps.setInt(3, login_id);
                    ps.execute();
                    System.out.println("Profile registered successfully");
                }
                catch (Exception e) {
                   System.out.println(e);
                }
               
                System.out.println("User Login id:"+ login_id);

                break;
                case 2:
                	
                	System.out.println("Enter your login id: ");
                	login_id = sc.nextInt();
                	
                	boolean flag = userVerification(login_id);
                	
                	if(flag == true) {
                		int train;
                    	System.out.println("Available train:");  
                        System.out.println("1. Train 1");  
                        System.out.println("2. Train 2");  
                        System.out.println("3. Train 3"); 
                        
                        train = sc.nextInt();
                        switch(train) {
                        case 1: 
                        	
                        	boolean seat = false;
                        	System.out.println("Available seates are:");
                        	try {
                        		String train_name = "train1";
                        		String status = "available";
                    			String sql = "select * from train_dts where train_name = ? and status = ?";
                    			ps = conn.prepareStatement(sql);
                    			ps.setString(1, train_name);
                    			ps.setString(2, status);

                    			ResultSet rs = ps.executeQuery();
                    			
                    			 System.out.printf("%-20s  %-15s%n",  "Boogie Number", "Seat Number");
                    	            System.out.println("---------------------------------------------");
                    			while (rs.next()) {
                    				int boogieNumber = rs.getInt("boogie_no");
                    				int seatNumber = rs.getInt("seat_no");
                    			
                    				
                    				 System.out.printf("%-20s %-15s%n", boogieNumber, seatNumber);
                    			}
                    			
                    			System.out.println("Select your boogie:");
                    			int boogie_no = sc.nextInt();
                    			
                    			System.out.println("Select your seat:");
                    			int seat_no = sc.nextInt();
                    			
                    			sql = "Select count(*) from train_dts where train_name = ? and boogie_no = ? and seat_no = ? and status = ?";
                    			ps = conn.prepareStatement(sql);
                    			ps.setString(1, train_name);
                    			ps.setInt(2, boogie_no);
                    			ps.setInt(3, seat_no);
                    			ps.setString(4, status);
                    			
                    			rs = ps.executeQuery();
                    			
                    			if(rs.next()) {
                    				int count = rs.getInt(1);
                    				if(count > 0) {
                    					seat = true;
                    				}
                    			}
                    			if(seat == true) {
                    				status = "booked";
                    				sql = "update train_dts set status = ?, user_id = ? where train_name = ? and boogie_no = ? and seat_no = ?";
                        			ps = conn.prepareStatement(sql);
                        			ps.setString(1, status);
                        			ps.setInt(2, login_id);
                        			ps.setString(3, train_name);
                        			ps.setInt(4, boogie_no);
                        			ps.setInt(5, seat_no);
                        			
                        			ps.executeUpdate();
                        			System.out.println("Seat booked");
                    			}
                    			else {
                    				System.out.println("Select valid seat.");
                    			}
                    			
                    		} catch (Exception e) {
                    			e.printStackTrace();
                    		}
                        	break;
                        case 2: 
                        	seat = false;
                        	System.out.println("Available seates are:");
                        	try {
                        		String train_name = "train2";
                        		String status = "available";
                    			String sql = "select * from train_dts where train_name = ? and status = ?";
                    			ps = conn.prepareStatement(sql);
                    			ps.setString(1, train_name);
                    			ps.setString(2, status);

                    			ResultSet rs = ps.executeQuery();
                    			
                    			 System.out.printf("%-20s  %-15s%n",  "Boogie Number", "Seat Number");
                    	            System.out.println("---------------------------------------------");
                    			while (rs.next()) {
                    				int boogieNumber = rs.getInt("boogie_no");
                    				int seatNumber = rs.getInt("seat_no");
                    			
                    				
                    				 System.out.printf("%-20s %-15s%n", boogieNumber, seatNumber);
                    			}
                    			
                    			System.out.println("Select your boogie:");
                    			int boogie_no = sc.nextInt();
                    			
                    			System.out.println("Select your seat:");
                    			int seat_no = sc.nextInt();
                    			
                    			sql = "Select count(*) from train_dts where train_name = ? and boogie_no = ? and seat_no = ? and status = ?";
                    			ps = conn.prepareStatement(sql);
                    			ps.setString(1, train_name);
                    			ps.setInt(2, boogie_no);
                    			ps.setInt(3, seat_no);
                    			ps.setString(4, status);
                    			
                    			rs = ps.executeQuery();
                    			
                    			if(rs.next()) {
                    				int count = rs.getInt(1);
                    				if(count > 0) {
                    					seat = true;
                    				}
                    			}
                    			if(seat == true) {
                    				status = "booked";
                    				sql = "update train_dts set status = ? where train_name = ? and boogie_no = ? and seat_no = ? and user_id = ?";
                        			ps = conn.prepareStatement(sql);
                        			ps.setString(1, status);
                        			ps.setString(2, train_name);
                        			ps.setInt(3, boogie_no);
                        			ps.setInt(4, seat_no);
                        			ps.setInt(5, login_id);
                        			ps.executeUpdate();
                        			System.out.println("Seat booked");
                    			}
                    			else {
                    				System.out.println("Select valid seat.");
                    			}
                    			
                    		} catch (Exception e) {
                    			e.printStackTrace();
                    		}
                        	break;
                        case 3: 
                        	seat = false;
                        	System.out.println("Available seates are:");
                        	try {
                        		String train_name = "train3";
                        		String status = "available";
                    			String sql = "select * from train_dts where train_name = ? and status = ?";
                    			ps = conn.prepareStatement(sql);
                    			ps.setString(1, train_name);
                    			ps.setString(2, status);

                    			ResultSet rs = ps.executeQuery();
                    			
                    			 System.out.printf("%-20s  %-15s%n",  "Boogie Number", "Seat Number");
                    	            System.out.println("---------------------------------------------");
                    			while (rs.next()) {
                    				int boogieNumber = rs.getInt("boogie_no");
                    				int seatNumber = rs.getInt("seat_no");
                    			
                    				
                    				 System.out.printf("%-20s %-15s%n", boogieNumber, seatNumber);
                    			}
                    			
                    			System.out.println("Select your boogie:");
                    			int boogie_no = sc.nextInt();
                    			
                    			System.out.println("Select your seat:");
                    			int seat_no = sc.nextInt();
                    			
                    			sql = "Select count(*) from train_dts where train_name = ? and boogie_no = ? and seat_no = ? and status = ?";
                    			ps = conn.prepareStatement(sql);
                    			ps.setString(1, train_name);
                    			ps.setInt(2, boogie_no);
                    			ps.setInt(3, seat_no);
                    			ps.setString(4, status);
                    			
                    			rs = ps.executeQuery();
                    			
                    			if(rs.next()) {
                    				int count = rs.getInt(1);
                    				if(count > 0) {
                    					seat = true;
                    				}
                    			}
                    			if(seat == true) {
                    				status = "booked";
                    				sql = "update train_dts set status = ? where train_name = ? and boogie_no = ? and seat_no = ? and user_id = ?";
                        			ps = conn.prepareStatement(sql);
                        			ps.setString(1, status);
                        			ps.setString(2, train_name);
                        			ps.setInt(3, boogie_no);
                        			ps.setInt(4, seat_no);
                        			ps.setInt(5, login_id);
                        			ps.executeUpdate();
                        			System.out.println("Seat booked");
                    			}
                    			else {
                    				System.out.println("Select valid seat.");
                    			}
                    			
                    		} catch (Exception e) {
                    			e.printStackTrace();
                    		}
                        	break;
                        	default: System.out.println("Invalid choice. Please choose a valid option");
                        }
                	}
                	else {
                		System.out.println("Please register your profile.");
                	}
                    break;
                    
                case 3:
                	System.out.println("Enter your login id: ");
                	login_id = sc.nextInt();
                case 4:
                case 5:
                case 6: System.out.println("Exiting the System.");
                break;
                default: System.out.println("Invalid choice. Please choose a valid option");
            }
        }while(choice !=6); 
    }
}
