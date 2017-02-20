//STEP 1. Import required packages
import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.*;

public class InsertData {
	// JDBC driver name and database URL
	static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
	static final String DB_URL = "jdbc:mysql://localhost/soccercup?allowUrlInLocalInfile=true"; //allows reading from file

	// Database credentials
	static final String USER = "root";
	static final String PASS = "arlington1234%";

	public static void main(String[] args) {
		Connection conn = null;
		Statement stmt = null;
		try {
			// STEP 2: Register JDBC driver
			// load driver class dynamically only when needed
			// open when required for security reasons
			Class.forName("com.mysql.jdbc.Driver");

			// STEP 3: Open a connection
			System.out.println("Connecting to a selected database...");
			conn = DriverManager.getConnection(DB_URL, USER, PASS);
			System.out.println("Connected database successfully...");

			// STEP 4: Execute a query
			System.out.println("Creating table in given database...");
			stmt = conn.createStatement();
			
			/* Specify the path of file from which data should be loaded */
			//String csvFile = "E:/UTASubjects/sem2/db1/project/Input_Data_Project1_Spring_2016/Input_Data/Country.csv";
			//String csvFile = "E:/UTASubjects/sem2/db1/project/Input_Data_Project1_Spring_2016/Input_Data/Players.csv";
			//String csvFile = "E:/UTASubjects/sem2/db1/project/Input_Data_Project1_Spring_2016/Input_Data/Match_results.csv";
			//String csvFile = "E:/UTASubjects/sem2/db1/project/Input_Data_Project1_Spring_2016/Input_Data/Player_Cards.csv";
			String csvFile = "E:/UTASubjects/sem2/db1/project/Input_Data_Project1_Spring_2016/Input_Data/Magazine.csv";
			
			BufferedReader br = null;
			String line = "";
			String cvsSplitBy = ",";
			try {
				br = new BufferedReader(new FileReader(csvFile));
				while ((line = br.readLine()) != null) {

				    String[] val = line.split(cvsSplitBy);
					
					//String sql = "INSERT INTO COUNTRY(Country_name, Population, No_of_Worldcup_won, Manager) VALUES ("+val[0]+",'"+val[1]+"','"+val[2]+"',"+val[3]+")";
					//String sql = "INSERT INTO PLAYER(Player_id,name, fname,lname, DOB,Country, Height, Club, Position, Caps_for_country,Is_captain) VALUES ('"+val[0]+"',"+val[1]+","+val[2]+","+val[3]+","+val[4]+","+val[5]+",'"+val[6]+"',"+val[7]+","+val[8]+",'"+val[9]+"',"+val[10]+")";
					//String sql = "INSERT INTO MATCH_RESULTS(Match_id, Date_of_match,Start_time_of_match, Team1,Team2, Team1_score, Team2_score, Stadium_name, host_city) VALUES ('"+val[0]+"',"+val[1]+","+val[2]+","+val[3]+","+val[4]+",'"+val[5]+"','"+val[6]+"',"+val[7]+","+val[8]+")";
					//String sql = "INSERT INTO PLAYER_CARD(Player_id,Yellow_Cards, Red_cards ) VALUES ('"+val[0]+"','"+val[1]+"','"+val[2]+"')";
					String sql = "INSERT INTO MAGAZINE(Name,IdNo,Frequency,No_of_issues,Subscription_cost,Start_Date,End_Date) VALUES ('"+val[0]+"','"+val[1]+"','"+val[2]+"','"+val[3]+"','"+val[4]+"','"+val[5]+"','"+val[6]+"')";
					
					stmt.executeUpdate(sql);
					System.out.println("Insertion Completed!");
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			//Close the connection
            conn.close();
		} catch (SQLException se) {
			// Handle errors for JDBC
			se.printStackTrace();
		} catch (Exception e) {
			// Handle errors for Class.forName
			e.printStackTrace();
		} finally {
			// finally block used to close resources
			try {
				if (stmt != null)
					conn.close();
			} catch (SQLException se) {
			}// do nothing
			try {
				if (conn != null)
					conn.close();
			} catch (SQLException se) {
				se.printStackTrace();
			}// end finally try
		}// end try
	}// end main
}// end JDBCExample
