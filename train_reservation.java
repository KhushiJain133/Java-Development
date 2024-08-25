import java.sql.Connection;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;
import java.util.Random;

public class train_reservation {

	static String url = "jdbc:mysql://localhost:3306/train_reservation";
	static String username = "root";
	static String password = "root"; 

	// Database connection
	
	public static Connection connectDB(){
        try {
			Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(url, username, password);
            return conn;
        } 
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
	

	// User verification code

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


	// To handle train booking
	private static void handleBooking(String trainname, int login_id){

		Connection conn = connectDB();
        PreparedStatement ps = null;
		boolean seat = false;
                        	try {
								Scanner sc = new Scanner(System.in);
                        		String train_name = trainname;
                        		String status = "available";
                    			String sql = "select * from train_dts where train_name = ? and status = ?";
                    			ps = conn.prepareStatement(sql);
                    			ps.setString(1, train_name);
                    			ps.setString(2, status);

                    			ResultSet rs = ps.executeQuery();

								if(rs.next()){
									System.out.println("Available seates are:");
									System.out.printf("%-20s  %-15s%n",  "Boogie Number", "Seat Number");
                    	            System.out.println("---------------------------------------------");
                    			do{
                    				int boogieNumber = rs.getInt("boogie_no");
                    				int seatNumber = rs.getInt("seat_no");
                    			
                    				
                    				 System.out.printf("%-20s %-15s%n", boogieNumber, seatNumber);
                    			}while (rs.next());
                    			
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
                    			
                    		} 
							else{
								System.out.println("No seat available.");
							}
								}
								catch (Exception e) {
									e.printStackTrace();
								}	
                    			

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


			// Switch case to handle user selection of choice.
            switch(choice){

				// Create user profile
                case 1: 
                System.out.println("Enter Your name: ");
                String name = sc.next();
                System.out.println("Enter your age: ");
                int age = sc.nextInt();
               
				Random random = new Random();
                int login_id = random.nextInt(9000000) + 1000000;
                
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

				// For train booking
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
                        	
                        	handleBooking("train1", login_id);
                    			
                        	break;
                        case 2: 
						handleBooking("train2", login_id);
                        	break;
                        case 3: 
						handleBooking("train3", login_id);
                        	break;
                        	default: System.out.println("Invalid choice. Please choose a valid option");
                        }
                	}
                	else {
                		System.out.println("Please register your profile.");
                	}
                    break;
                    
				// To display individual personal details
                case 3:

					System.out.println("Enter your login id: ");
                	login_id = sc.nextInt();

					flag = userVerification(login_id);

					if(flag == true){
						String sql = "select * from train_dts inner join user_dts on user_id = login_id where login_id = ?";
						ps = conn.prepareStatement(sql);
						ps.setInt(1, login_id);
						ResultSet rs = ps.executeQuery();

						if(rs.next()){
							System.out.printf("%-20s  %-15s %-15s %-15s %-15s%n", "Name", "Age", "Train Name",  "Boogie Number", "Seat Number");
							do{

								String user_name = rs.getString("name");
								int user_age = rs.getInt("age");
								String train = rs.getString("train_name");
								int boogie_no = rs.getInt("boogie_no");
								int seat_no = rs.getInt("seat_no");
	
								System.out.printf("%-20s  %-15s %-15s %-15s %-15s%n", user_name, user_age, train, boogie_no, seat_no);
							}while (rs.next());
						}
						else{
							System.out.println("You have booked nothing.");
							
						}
	
					}
					else{
						System.out.println("Please register your profile.");
					}

					break;

				// to display the details of all the user registered in the system
                case 4:

				String sql = "select * from user_dts left join train_dts on login_id = user_id";
						ps = conn.prepareStatement(sql);
						ResultSet rs = ps.executeQuery();

						
							System.out.printf("%-20s  %-15s %-15s %-15s %-15s %-15s%n", "Login Id", "Name", "Age", "Train Name",  "Boogie Number", "Seat Number");
					
						while (rs.next()){

							int id = rs.getInt("login_id");
							String user_name = rs.getString("name");
							int user_age = rs.getInt("age");
							String train = rs.getString("train_name");
							int boogie_no = rs.getInt("boogie_no");
							int seat_no = rs.getInt("seat_no");

							System.out.printf("%-20s  %-15s %-15s %-15s %-15s %-15s%n", id, user_name, user_age, train, boogie_no, seat_no);
						}
						break;

				// To display train status of booked and unbooked seats.
                case 5: 

				System.out.println("Remaining seats:");
				String status = null;
				String train_name[] = {"train1", "train2", "train3"};

				for(int i=1 ; i<= train_name.length; i++){
					status = "available";
					System.out.println("Train " + i);

					for(int j=1 ; j<=3; j++){
						System.out.println("Boogie" + j);
						sql = "SELECT * FROM train_reservation.train_dts where train_name = ? and status = ? and boogie_no = ?";
						ps = conn.prepareStatement(sql);
						ps.setString(1, train_name[i-1]);
						ps.setString(2, status);
						ps.setInt(3, j);
						rs = ps.executeQuery();


						if(rs.next()){
							do{
								int seat_no = rs.getInt("seat_no");
							System.out.println("Seat No: " + seat_no );
							}while(rs.next());
							
						}
						else{
							System.out.println("Not available");
						}
						System.out.println(" ");
					}
				}

					System.out.println("Booked seats:");
					for(int i=1 ; i<= train_name.length; i++){
						status = "booked";
						System.out.println("Train " + i);
	
						for(int j=1 ; j<=3; j++){
							System.out.println("Boogie" + j);
							sql = "SELECT * FROM train_reservation.train_dts where train_name = ? and status = ? and boogie_no = ?";
							ps = conn.prepareStatement(sql);
							ps.setString(1, train_name[i-1]);
							ps.setString(2, status);
							ps.setInt(3, j);
							rs = ps.executeQuery();
	
	
							if(rs.next()){
								do{
									int seat_no = rs.getInt("seat_no");
								System.out.println("Seat No: " + seat_no );
								}while(rs.next());
								
							}
							else{
								System.out.println("Nothing Boooked.");
							}
							System.out.println(" ");
						}

				}
				break;
                case 6: System.out.println("Exiting the System.");
                break;
                default: System.out.println("Invalid choice. Please choose a valid option");
            }
        } while(choice != 6);
    }
}
