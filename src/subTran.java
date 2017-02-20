import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Scanner;


public class subTran {
	private String JDBC_DRIVER;
	private String DB_URL;
	private String USER;
	private String PASS;
	private Connection conn;
	private Statement stmt;

	public subTran(){
		JDBC_DRIVER = "com.mysql.jdbc.Driver";
		DB_URL = "jdbc:mysql://localhost/subscription?allowUrlInLocalInfile=true"; //allows reading from file
		USER = "root";
		PASS = "arlington1234%";
		conn = null;
		stmt = null;
	}

	public void customerDetails(){
		int ID,no_of_subscription;
		String name="",addr="";
		Scanner sc = new Scanner(System.in);

		System.out.println("Enter the customer ID number");
		while (!sc.hasNextInt()) {
			System.out.println("Please enter a valid ID number");
			sc.nextLine();
		}
		ID = sc.nextInt();

		Scanner sc1 = new Scanner(System.in);
		System.out.println("Enter the customer address number");
		addr = sc1.nextLine();

		Scanner sc2 = new Scanner(System.in);
		System.out.println("Enter the customer name");
		name = sc2.nextLine();

		no_of_subscription = 1;

		String sql = "INSERT INTO customer(IdNo, Addr,Cust_Name, No_of_subscription) VALUES ('"+ID+"','"+name+"','"+addr+"','"+no_of_subscription+"')";
		try {
			stmt.executeUpdate(sql);
			System.out.println("Transaction 1 completed");
		} catch (SQLException e) {
			if(e.getErrorCode() == 1062 ){
				System.out.println("Duplicate primary key entered!");
			} else {
				System.out.print("Error occured! Transaction failed!");
			}
		}

		sc.close();
		sc1.close();
		sc2.close();
	}

	public void newMagzinePub(String i){
		String publicationName,publicationType="";
		Scanner sc = new Scanner(System.in);
		if(i.equals("2")){
			System.out.println("Enter the new magazine publication name");
			publicationType = "m";
		} else {
			System.out.println("Enter the new newspaper publication name");
			publicationType = "n";
		}
		publicationName = sc.nextLine();

		String sql = "INSERT INTO publication(Name,PublicationType) VALUES ('"+publicationName+"','"+publicationType+"')";
		try {
			stmt.executeUpdate(sql);
			System.out.println("Transaction 2 completed");
		} catch (SQLException e) {
			if(e.getErrorCode() == 1062 ){
				System.out.print("Duplicate key entered!");
			} else {
				System.out.print("Error occured! Transaction failed!");
			}
		}
		sc.close();
	}

	public void newMagazineRate(){
		int no_of_issues,cost;
		Scanner sc = new Scanner(System.in);

		System.out.println("Enter number of issues");
		while (!sc.hasNextInt()) {
			System.out.println("Please enter a valid ID number");
			sc.nextLine();
		}
		no_of_issues = sc.nextInt();

		System.out.println("Enter the cost");
		while (!sc.hasNextInt()) {
			System.out.println("Please enter a valid number!");
			sc.nextLine();
		}
		cost = sc.nextInt();

		String sql = "INSERT INTO magazine_cost(No_of_issues,Cost) VALUES ('"+no_of_issues+"','"+cost+"')";
		try {
			stmt.executeUpdate(sql);
			System.out.println("Transaction 4 completed");
		} catch (SQLException e) {
			if(e.getErrorCode() == 1062 ){
				System.out.print("Duplicate key entered!");
			} else {
				System.out.print("Error occured! Transaction failed!");
			}
		}

		sc.close();
	}


	public void newMagazine(){
		String name="",frequency = "",startDate,endD="",type="";
		Date current,endDate;
		int IdNo,No_of_issues,subscription_cost=0;

		Scanner sc = new Scanner(System.in);

		while(type.equals("")){
			System.out.println("Enter magazine's publication name");
			name = sc.nextLine();
			String sql = "SELECT PublicationType from publication where Name='" + name + "';";
			ResultSet res;
			try {
				res = stmt.executeQuery(sql);
				//type = res.getString("PublicationType");
				if (res.next() && res.getString("PublicationType").equals("m")) {
					type = res.getString("PublicationType");
				} else {
					System.out.println("Invalid magazine publication name! select a valid name from publication table!");
					type = "";
				}
			} catch (SQLException e) {
				System.out.println(e);
				System.out.println("Invalid magazine publication name! select a valid name from publication table!");
				type = "";
			}	
		}

		System.out.println("Enter the custmer ID");
		while (!sc.hasNextInt()) {
			System.out.println("Please enter a valid number!");
			sc.nextLine();
		}
		IdNo = sc.nextInt();

		while(!(frequency.equals("w") || frequency.equals("m") || frequency.equals("q"))){
			System.out.println("Enter the frequency : (Weekly(w)/Monthly(m)/Quarterly(q)");
			frequency = sc.next();
		}


		//while(!(No_of_issues==7||No_of_issues==5||No_of_issues==2||No_of_issues==1)){
		System.out.println("Enter the no of issues");
		while (!sc.hasNextInt()) {
			System.out.println("Please enter a valid number!");
			sc.nextLine();
		}
		No_of_issues = sc.nextInt();
		//}

		//subscription_cost = sc.next();
		String sql = "SELECT Cost FROM magazine_cost AS NC where NC.No_of_issues=" + No_of_issues + ";";
		ResultSet res;
		try {
			res = stmt.executeQuery(sql);
			if (res.next()) {
				subscription_cost = res.getInt("Cost");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		sdf.setLenient(false);

		while(true){
			System.out.println("Enter magazine's subscription start date only in the format yyyy-mm-dd");
			startDate = sc.next();

			try {
				current = sdf.parse(startDate);
				Calendar cal = Calendar.getInstance();
				cal.setTime(current);
				if(frequency.equals("w")){
					cal.add(Calendar.DATE, 7);
					endD = sdf.format(cal.getTime());
				} else if(frequency.equals("m")){
					cal.set(Calendar.MONTH, (cal.get(Calendar.MONTH)+1));
				} else{
					cal.set(Calendar.MONTH, (cal.get(Calendar.MONTH)+3));
				}
				if(!frequency.equals("w")){
					endDate = cal.getTime();
					Calendar cal1 = Calendar.getInstance();
					cal1.setTime(endDate);
					int year = cal1.get(Calendar.YEAR);
					int month = cal1.get(Calendar.MONTH)+1;
					int day = cal1.get(Calendar.DAY_OF_MONTH);
					endD = year+"-"+month+"-"+day;

					Date date1 = sdf.parse(endD);
					Date todayDate = sdf.parse(sdf.format(new Date() ));

					if(!date1.after(todayDate)){
						System.out.println("End date is smaller than current date! Invalid start date!");
						continue;
					}
				}
				break;
			} catch (Exception e) {
				System.out.println("Invalid date");
			}
		}

		String result = "INSERT INTO magazine(Name,IdNo,Frequency,No_of_issues,Subscription_cost,Start_Date,End_Date) VALUES ('"+name+"','"+IdNo+"','"+frequency+"','"+No_of_issues+"','"+subscription_cost+"','"+startDate+"','"+endD+"')";
		try {
			stmt.executeUpdate(result);
			String update = "UPDATE customer SET No_of_subscription = No_of_subscription + 1 WHERE IdNo=" + IdNo;
			stmt.executeUpdate(update);
			System.out.println("Transaction 4 completed");
		} catch (SQLException e) {
			if(e.getSQLState().startsWith("23"))
				System.out.println("Foreign key violation! Transaction failed!");
			else
				e.printStackTrace();
		}
		sc.close();
	}

	public void newNewspaper(){
		String name="",startDate,endD,type="";
		Date current,endDate;
		int IdNo,No_of_days=0,No_of_months,subscription_cost;

		Scanner sc = new Scanner(System.in);

		while(type.equals("")){
			System.out.println("Enter newspaper's publication name");
			name = sc.nextLine();
			String sql = "select PublicationType from publication where name='" + name + "'";
			ResultSet res;
			try {
				res = stmt.executeQuery(sql);
				if (res.next() && res.getString("PublicationType").equals("newspaper")) {
					type = res.getString("PublicationType");
				} else {
					System.out.println("Invalid newspaper publication name! select a valid name from publication table!");
					type = "";
				}
			} catch (SQLException e) {
				System.out.println("Invalid newspaper publication name! select a valid name from publication table!");
				type = "";
			}	
		}

		System.out.println("Enter the custmer ID");
		while (!sc.hasNextInt()) {
			System.out.println("Please enter a valid number!");
			sc.nextLine();
		}
		IdNo = sc.nextInt();

		System.out.println("Enter the no of months");
		while (!sc.hasNextInt()) {
			System.out.println("Please enter a valid number!");
			sc.nextLine();
		}
		No_of_months = sc.nextInt();

		while(!(No_of_days==7||No_of_days==5||No_of_days==2||No_of_days==1)){
			System.out.println("Enter the number of days in (7,5,2 or 1)");
			while (!sc.hasNextInt()) {
				System.out.println("Please enter a valid number!");
				sc.nextLine();

			}
			No_of_days = sc.nextInt();
		}

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		sdf.setLenient(false);

		while(true){
			System.out.println("Enter newspaper's subscription start date only in the format yyyy-mm-dd");
			startDate = sc.next();

			try {
				current = sdf.parse(startDate);
				Calendar cal = Calendar.getInstance();
				cal.setTime(current);
				cal.set(Calendar.MONTH, (cal.get(Calendar.MONTH)+No_of_months));
				endDate = cal.getTime();

				Calendar cal1 = Calendar.getInstance();
				cal1.setTime(endDate);
				int year = cal1.get(Calendar.YEAR);
				int month = cal1.get(Calendar.MONTH)+1;
				int day = cal1.get(Calendar.DAY_OF_MONTH);
				endD = year+"-"+month+"-"+day;

				Date date1 = sdf.parse(endD);
				Date todayDate = sdf.parse(sdf.format(new Date() ));
				if(!date1.after(todayDate)){
					System.out.println("End date is smaller than current date! Invalid start date!");
					continue;
				}

				break;
			} catch (Exception e) {
				System.out.println("Invalid date");
			}
		}

		subscription_cost = No_of_months * 4 * costForDays(No_of_days);

		String sql = "INSERT INTO newspaper(Name,IdNo,No_of_months,Subscription_cost,No_of_days,Start_Date,End_Date) VALUES ('"+name+"','"+IdNo+"','"+No_of_months+"','"+subscription_cost+"','"+No_of_days+"','"+startDate+"','"+endD+"')";
		try {
			stmt.executeUpdate(sql);
			String update = "UPDATE customer SET No_of_subscription = No_of_subscription + 1 WHERE IdNo=" + IdNo;
			stmt.executeUpdate(update);
			System.out.println("Transaction 4 completed");
		} catch (SQLException e) {
			if(e.getSQLState().startsWith("23"))
				System.out.print("Foreign key violation! Transaction failed!");
			else
				e.printStackTrace();
		}
		sc.close();
	}

	public int costForDays(int No_of_days){
		System.out.println(No_of_days);
		String sql = "SELECT Cost FROM newspaper_cost AS NC where NC.No_of_days=" + No_of_days + ";";
		ResultSet res;
		try {
			res = stmt.executeQuery(sql);
			if (res.next()) {
				return res.getInt("Cost");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}

	public void updateMagazineCost(){
		int No_of_issues=0,cost=-1;
		Scanner sc = new Scanner(System.in);

		while(cost<=0){
			if(cost == -1){
				System.out.println("Enter the No_of_issues field to be update");
			} else {
				System.out.println("No_of_issues not found! Please re-enter!");
			}
			while (!sc.hasNextInt()) {
				System.out.println("Please enter a valid number!");
				sc.nextLine();
			}
			No_of_issues = sc.nextInt();
			String sql = "SELECT Cost from magazine_cost where No_of_issues=" + No_of_issues;
			ResultSet res;
			try {
				res = stmt.executeQuery(sql);
				if (res.next()) {
					System.out.println("Current cost = " + res.getInt("Cost"));
					cost = res.getInt("Cost");
				} else {
					cost = 0;
				}
			} catch (SQLException e) {
				System.out.println("Error occured! Transaction failed! Try again!");
				return;
			}	
		}
		cost = 0;
		while(cost <= 0) {
			System.out.println("Enter the new cost(greater than zero)");
			while (!sc.hasNextInt()) {
				System.out.println("Please enter a valid number!");
				sc.nextLine();
			}
			cost = sc.nextInt();
		}
		String sql = "UPDATE magazine_cost SET Cost=" + cost + " WHERE No_of_issues="+ No_of_issues + ";";
		try {
			stmt.executeUpdate(sql);
			System.out.println("Transaction completed!");
		} catch (SQLException e) {
			System.out.println("Update failed. Try again!");
		}
		sc.close();
	}

	public void updateNewspaperCost(){
		int No_of_days=0,cost=-1;
		Scanner sc = new Scanner(System.in);

		while(cost<=0){
			if(cost == -1){
				System.out.println("Enter the No_of_days field to be update");
			} else {
				System.out.println("No_of_days not found! Please re-enter!");
			}
			while (!sc.hasNextInt()) {
				System.out.println("Please enter a valid number!");
				sc.nextLine();
			}
			No_of_days = sc.nextInt();
			String sql = "SELECT Cost from newspaper_cost where No_of_days=" + No_of_days;
			ResultSet res;
			try {
				res = stmt.executeQuery(sql);
				if (res.next()) {
					System.out.println("Current cost = " + res.getInt("Cost"));
					cost = res.getInt("Cost");
				} else {
					cost = 0;
				}
			} catch (SQLException e) {
				System.out.println("Error occured! Transaction failed! Try again!");
				return;
			}	
		}
		cost = 0;
		while(cost <= 0) {
			System.out.println("Enter the new cost(greater than zero)");
			while (!sc.hasNextInt()) {
				System.out.println("Please enter a valid number!");
				sc.nextLine();
			}
			cost = sc.nextInt();
		}
		String sql = "UPDATE newspaper_cost SET Cost=" + cost + " WHERE No_of_days="+ No_of_days + ";";
		try {
			stmt.executeUpdate(sql);
			System.out.println("Transaction completed!");
		} catch (SQLException e) {
			System.out.println("Update failed. Try again!");
		}
		sc.close();
	}

	public void printAllData(){
		String sql = "(SELECT *,'newspaper' as Publication_Type from newspaper as n,customer as c where n.IdNo=c.IdNo) UNION (SELECT *,'magazine' as Publication_Type from magazine as n,customer as c where n.IdNo=c.IdNo)";
		ResultSet rs;
		try {
			rs = stmt.executeQuery(sql);
			System.out.println("IdNo" + "\t" + "Subscription_cost" +
					"\t" + "Start_Date" + "\t" + "End_Date" + "\t" + "Publication_Type" + "\t" + "Cust_Name" +
					"\t" + "Name");
			System.out.println("-----" + "\t" + "----------------" +
					"\t" + "-----------" + "\t" + "-----------" +
					"\t" + "-----------------" + "\t" + "----------" + "\t" + "-------------");
			while (rs.next()) {
				String coffeeName = rs.getString("Cust_Name").trim();
				int supplierID = rs.getInt("IdNo");
				String price = rs.getString("Name").trim();
				int sales = rs.getInt("Subscription_cost");
				String startDate = rs.getString("Start_Date").trim();
				if(startDate.charAt(startDate.length()-1) == '\''){
					startDate = rs.getString("Start_Date").substring(0, startDate.length()-1);
				}
				String total = rs.getString("End_Date").trim();
				if(total.charAt(total.length()-1) == '\''){
					total = rs.getString("End_Date").substring(0, total.length()-1);
				}
				String pub = rs.getString("Publication_Type").trim();
				System.out.println( supplierID + "\t" + sales +
						"\t\t\t" + startDate + "\t" + total + "\t" + pub + "\t\t" + coffeeName + "\t\t" + price);
			}
		} catch (SQLException e) {
			System.out.println("Transaction failed");
		}
	}

	public void printCustomers(){
		String sql = "SELECT * from CUSTOMER";
		ResultSet rs;
		try {
			rs = stmt.executeQuery(sql);
			System.out.println("IdNo" + "\t" + "No_of_subscriptions" +
					"\t" + "Cust_Name" + "\t" + "Addr");
			System.out.println("-----" + "\t" + "-------------------" +
					"\t" + "---------" + "\t" + "------");
			while (rs.next()) {
				int IdNo = rs.getInt("IdNo");
				String Cust_Name = rs.getString("Cust_Name").trim();
				String Addr = rs.getString("Addr");
				int No_of_subscription = rs.getInt("No_of_subscription");

				System.out.println( IdNo + "\t" + No_of_subscription +
						"\t\t\t" + Cust_Name + "\t\t" + Addr);
			}
		} catch (SQLException e) {
			System.out.println("Transaction failed");
		}
	}

	public void printMagazine(){
		String sql = "SELECT * from MAGAZINE";
		ResultSet rs;
		try {
			rs = stmt.executeQuery(sql);
			System.out.println("IdNo" + "\t" + "Frequency" +
					"\t" + "No_of_issues" + "\t" + "Subscription_cost" 
					+ "\t" + "StartDate" + "\t" + "EndDate" + "\t\t" + "Name");
			System.out.println("-----" + "\t" + "--------" +
					"\t" + "-----------" + "\t" + "------------------" +
					"\t" + "-----------" + "\t" + "-------------" + "\t" + "--------");
			while (rs.next()) {
				int IdNo = rs.getInt("IdNo");
				String Frequency = rs.getString("Frequency").trim();
				int No_of_issues = rs.getInt("No_of_issues");
				int Subscription_cost = rs.getInt("Subscription_cost");
				String Start_Date = rs.getString("Start_Date").trim();
				if(Start_Date.charAt(Start_Date.length()-1) == '\''){
					Start_Date = rs.getString("Start_Date").substring(0, Start_Date.length()-1);
				}
				String End_Date = rs.getString("End_Date").trim();
				if(End_Date.charAt(End_Date.length()-1) == '\''){
					End_Date = rs.getString("End_Date").substring(0, End_Date.length()-1);
				}
				String Name = rs.getString("Name");

				System.out.println( IdNo + "\t" + Frequency +
						"\t\t" + No_of_issues + "\t\t" + Subscription_cost 
						+ "\t\t\t" + Start_Date + "\t" + End_Date + "\t" + Name);
			}
		} catch (SQLException e) {
			System.out.println("Transaction failed");
		}
	}
	public void printNewspaper(){
		String sql = "SELECT * from newspaper";
		ResultSet rs;
		try {
			rs = stmt.executeQuery(sql);
			System.out.println("IdNo" + "\t" + "No_of_months" +
			"\t" + "Subscription_cost" + "\t" + "No_of_days" + "\t" + 
			"StartDate" + "\t" + "EndDate" + "\t\t" + "Name");
			System.out.println("-----" + "\t" + "--------" +
					"\t" + "--------------------" + "\t" + "----------" +
					"\t" + "-----------" + "\t" + "-------------" + "\t" + "--------");
			while (rs.next()) {
				int IdNo = rs.getInt("IdNo");
				String Frequency = rs.getString("No_of_months").trim();
				int Subscription_cost = rs.getInt("Subscription_cost");
				int No_of_issues = rs.getInt("No_of_days");
				String Start_Date = rs.getString("Start_Date").trim();
				String Name = rs.getString("Name").trim();
				
				if(Start_Date.charAt(Start_Date.length()-1) == '\''){
					Start_Date = rs.getString("Start_Date").substring(0, Start_Date.length()-1);
				}
				String End_Date = rs.getString("End_Date").trim();
				if(End_Date.charAt(End_Date.length()-1) == '\''){
					End_Date = rs.getString("End_Date").substring(0, End_Date.length()-1);
				}
				
				System.out.println( IdNo + "\t" + Frequency +
						"\t\t" + Subscription_cost + "\t\t\t" + No_of_issues  
						+ "\t\t" + Start_Date + "\t" + End_Date + "\t" + Name);
			}
		} catch (SQLException e) {
			System.out.println("Transaction failed");
		}
	}
	public void printMagazineCost(){
		String sql = "SELECT * from magazine_cost";
		ResultSet rs;
		try {
			rs = stmt.executeQuery(sql);
			System.out.println("No_of_issues" + "\t" + "Cost");
			System.out.println("------------" + "\t" + "-----");
			while (rs.next()) {
				int No_of_issues = rs.getInt("No_of_issues");
				int Cost = rs.getInt("Cost");

				System.out.println( No_of_issues + "\t\t" + Cost);
			}
		} catch (SQLException e) {
			System.out.println("Transaction failed");
		}
	}
	public void printNewspaperCost(){
		String sql = "SELECT * from newspaper_cost";
		ResultSet rs;
		try {
			rs = stmt.executeQuery(sql);
			System.out.println("No_of_days" + "\t" + "Cost");
			System.out.println("------------" + "\t" + "-----");
			while (rs.next()) {
				int No_of_days = rs.getInt("No_of_days");
				int Cost = rs.getInt("Cost");

				System.out.println( No_of_days + "\t\t" + Cost);
			}
		} catch (SQLException e) {
			System.out.println("Transaction failed");
		}
	}
	public void printPublication(){
		String sql = "SELECT * from PUBLICATION";
		ResultSet rs;
		try {
			rs = stmt.executeQuery(sql);
			System.out.println("PublicationType" + "\t\t" + "Publication_name");
			System.out.println("-----------------" + "\t" + "-------------------");
			while (rs.next()) {
				String Name = rs.getString("Name").trim();
				String PublicationType = rs.getString("PublicationType");

				System.out.println( PublicationType + "\t\t\t" + Name);
			}
		} catch (SQLException e) {
			System.out.println("Transaction failed");
		}
	}
	public void chooseTransaction(){
		Scanner sc = new Scanner(System.in);
		System.out.println("Enter the transaction number:");
		System.out.println("1 - Enter New customer details");
		System.out.println("2 - Enter new Magazine Publication");
		System.out.println("3 - Enter new Newspaper Publication");
		System.out.println("4 - Enter new Magazine rate");
		System.out.println("5 - Update Magazine rate");
		System.out.println("6 - Update Newspaper rate");
		System.out.println("7 - Create new publication for existing customer");
		System.out.println("8 - Print all details");
		System.out.println("9 - Print customers details");
		System.out.println("10 - Print magazine details");
		System.out.println("11 - Print newspaper details");
		System.out.println("12 - Print magazine cost details");
		System.out.println("13 - Print newspaper cost details");
		System.out.println("14 - Print publication details");
		String i = sc.next();
		switch (i) {
		case "1":  System.out.println("Entered number = " + i);
		customerDetails();
		break;
		case "2":  System.out.println("Entered number = " + i);
		//newMagazine();
		newMagzinePub(i);
		break;
		case "3":  System.out.println("Entered number = " + i);
		//newNewspaper();
		newMagzinePub(i);
		break;
		case "4":  System.out.println("Entered number = " + i);
		newMagazineRate();
		return;
		case "5":  System.out.println("Entered number = " + i);
		updateMagazineCost();
		return;
		case "6":  System.out.println("Entered number = " + i);
		updateNewspaperCost();
		return;
		case "7":  System.out.println("Entered number = " + i);
		System.out.println("Which new publication do you want to create?");
		System.out.println("Select 1 for magazine and 2 for newspaper!");
		Scanner case7 = new Scanner(System.in);
		System.out.println("Enter the transaction number");
		String publicationChoice = case7.next();
		switch (publicationChoice) {
		case "1":newMagazine();
		break;
		case "2":newNewspaper();
		break;
		default:System.out.println("Invalid choice!");
		}
		return;
		case "8":  System.out.println("Entered number = " + i);
		printAllData();
		return;
		case "9": printCustomers();
		return;
		case "10": printMagazine();
		return;
		case "11": printNewspaper();
			return;
		case "12": printMagazineCost();
			return;
		case "13": printNewspaperCost();
			return;
		case "14": printPublication();
			return;
		default: System.out.println("Enter a valid number");
		chooseTransaction();
		break;
		}

	}

	public void openDBConnection(){
		try {
			Class.forName(JDBC_DRIVER);
			System.out.println("Connecting to a selected database...");
			conn = DriverManager.getConnection(DB_URL, USER, PASS);
			System.out.println("Connected database successfully...");
			System.out.println("Creating table in given database...");
			stmt = conn.createStatement();
			chooseTransaction();
			//conn.close();
		} catch (SQLException se) {
			se.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (stmt != null)
					conn.close();
			} catch (SQLException se) {
			}
			try {
				if (conn != null)
					conn.close();
			} catch (SQLException se) {
				se.printStackTrace();
			}
		}
	}

	public void closeDB(){
		try {
			System.out.println("Database closed");
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	public static void main(String[] args) {
		subTran transactionObj = new subTran();
		transactionObj.openDBConnection();
		transactionObj.closeDB();
	}

}
