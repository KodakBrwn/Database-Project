import java.sql.PreparedStatement;
import java.sql.DriverManager;
import java.sql.DatabaseMetaData;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

public class UI {
	
	private static String DATABASE = "community_rentals.db";
	private static String VIEW = "SELECT * FROM ";
	
	private static String INSERT1 = "INSERT INTO ";
	private static String INSERTD = " VALUES(?,?,?,?,?,?)";
	private static String INSERTDI = " VALUES(?,?,?,?,?,?,?)";
	private static String INSERTDR = " VALUES(?,?,?,?)";
	private static String INSERTEMP = " VALUES(?,?,?,?,?,?)";
	private static String INSERTEQUIP = " VALUES(?,?,?,?)";
	private static String INSERTEI = " VALUES(?,?,?,?,?,?,?)";
	private static String INSERTEO = " VALUES(?,?,?)";
	private static String INSERTER = " VALUES(?,?,?)";
	private static String INSERTIO = " VALUES(?,?,?)";
	private static String INSERTM = " VALUES(?,?,?,?,?,?,?)";
	private static String INSERTREN = " VALUES(?,?,?,?,?,?,?)";
	private static String INSERTREV = " VALUES(?,?,?,?)";
	private static String INSERTW = " VALUES(?,?,?,?,?,?)";
	private static String INSERTWO = " VALUES(?,?,?)";
	
	private static String DELETE1 = "DELETE FROM ";
	private static String DELETESER = " WHERE serial =? ";
	private static String DELETEMOD = " WHERE model_num =? ";
	private static String DELETEREP = " WHERE repair_id =? ";
	private static String DELETESSN = " WHERE ssn =? ";
	private static String DELETEOIN = " WHERE order_instance_num =? ";
	private static String DELETERIN = " WHERE rental_instance_num =? ";
	private static String DELETEMEM = " WHERE member_id =? ";
	private static String DELETEREN = " WHERE rental_id =? ";
	private static String DELETEREV = " WHERE review_num =? ";
	private static String DELETEW = " WHERE warehouse_id =? ";
	private static String DELETEO = " WHERE order_number =? ";
	
	private static String D = "DRONE";
	private static String DI = "DRONE_INFO";
	private static String DR = "DRONE_Repair";
	private static String EMP = "EMPLOYEE";
	private static String EQUIP = "EQUIPMENT";
	private static String EI = "EQUIPMENT_INFO";
	private static String EO = "EQUIPMENT_ORDERED";
	private static String ER = "EQUIPMENT_RENTED";
	private static String IO = "ITEMS_ORDERED";
	private static String M = "MEMBER";
	private static String REN = "RENTAL";
	private static String REV = "REVIEW";
	private static String W = "WAREHOUSE";
	private static String WO = "WAREHOUSE_ODRER";
	
	
	public static void sqlQuery(Connection conn, String sql){
        try {
        	Statement stmt = conn.createStatement();
        	ResultSet rs = stmt.executeQuery(sql);
        	ResultSetMetaData rsmd = rs.getMetaData();
        	int columnCount = rsmd.getColumnCount();
        	for (int i = 1; i <= columnCount; i++) {
        		String value = rsmd.getColumnName(i);
        		System.out.printf("%-15s", value);
        	}
			System.out.print("\n");
        	while (rs.next()) {
        		for (int i = 1; i <= columnCount; i++) {
        			String columnValue = rs.getString(i);
            		System.out.printf("%-15s",columnValue);
        		}
    			System.out.print("\n");
        	}
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
	
	public static void printResultSet(ResultSet rs) {
		try {
			ResultSetMetaData rsmd = rs.getMetaData();
	    	int columnCount = rsmd.getColumnCount();
	    	while (rs.next()) {
	    		for (int i = 1; i <= columnCount; i++) {
	    			String columnValue = rs.getString(i);
	        		System.out.print(columnValue + " ");
	    		}
	    	} 
    	} catch (SQLException e) {
            System.out.println(e.getMessage());
        }
	}
	public static void sqlSearch(Connection conn, String sql, String toSearch){
        try {
        	Statement stmt = conn.createStatement();
        	ResultSet rs = stmt.executeQuery(sql);
        	ResultSetMetaData rsmd = rs.getMetaData();
        	int columnCount = rsmd.getColumnCount();
        	for (int i = 1; i <= columnCount; i++) {
        		String value = rsmd.getColumnName(i);
        		System.out.printf("%-15s", value);
        	}
			System.out.print("\n");
			ArrayList<Integer> trueIndexes = new ArrayList<Integer>();
        	int index = 0;
			while (rs.next()) {
        		index++;
        		for (int i = 1; i <= columnCount; i++) {
        			if(rs.getString(i).contains(toSearch)) {
        				trueIndexes.add(index);
        				break;
        			}
        		}
        	}
			
			ResultSet rsCopy = stmt.executeQuery(sql);
			ResultSetMetaData rsmd2 = rsCopy.getMetaData();
        	columnCount = rsmd2.getColumnCount();
			int index2 = 0;
			boolean bool = false;
			int size = trueIndexes.size();
        	while (rsCopy.next()) {
        		bool = false;
        		index2++;
        		for (int i = 1; i <= columnCount; i++) {
        			if(trueIndexes.size() !=0 && trueIndexes.get(0) == index2) {
        				String columnValue = rsCopy.getString(i);
                		System.out.printf("%-15s",columnValue);
                		bool = true;
        			}
        			if(bool == true && trueIndexes.size() !=0 && i == columnCount) {
        				trueIndexes.remove(0);
        				
        			}
        		}
        		if(size != trueIndexes.size()) {
        			System.out.print("\n");
        			size = trueIndexes.size();
        		}
        	}
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

	public static Connection initializeDB(String databaseFileName) {
    	/**
    	 * The "Connection String" or "Connection URL".
    	 * 
    	 * "jdbc:sqlite:" is the "subprotocol".
    	 * (If this were a SQL Server database it would be "jdbc:sqlserver:".)
    	 */
        String url = "jdbc:sqlite:" + databaseFileName;
        Connection conn = null; // If you create this variable inside the Try block it will be out of scope
        try {
            conn = DriverManager.getConnection(url);
            if (conn != null) {
            	// Provides some positive assurance the connection and/or creation was successful.
                DatabaseMetaData meta = conn.getMetaData();
                System.out.println("The driver name is " + meta.getDriverName());
                System.out.println("The connection to the database was successful.");
            } else {
            	// Provides some feedback in case the connection failed but did not throw an exception.
            	System.out.println("Null Connection");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            System.out.println("There was a problem connecting to the database.");
        }
        return conn;
    }
	
    public static void main(String[] args) throws SQLException {
    	
    	Connection conn = initializeDB(DATABASE);
    	
    	Scanner main = new Scanner(System.in);
    	int input;
    	do {
	    	System.out.println("What would you like to do?");
	    	System.out.println("Please enter a number to the coressponding function:");
			System.out.println("(0) Exit");
	    	System.out.println("(1) Add New Record");
	    	System.out.println("(2) Edit Existing Record");
	    	System.out.println("(3) Delete Existing Record");
	    	System.out.println("(4) Search");
	    	System.out.println("(5) View Table from Database");
	    	System.out.println("(6) Number of items a member has");
	    	System.out.println("(7) View Most Popular Item");
	    	System.out.println("(8) View Most Popular Manufacturer");
	    	System.out.println("(9) View Most Popular Drone");
	    	System.out.println("(10) View Items that are Checked out");
	    	System.out.println("(11) View Equipment by Type");
	    	System.out.print("Option: ");
	    	input = main.nextInt();
	    	if(input != 0) {
		    	if(input == 1) {
		    		Scanner a = new Scanner(System.in);
		    		System.out.println("Which entity would you like to add to?");
		    		System.out.println("Please enter a number to the coressponding entity:");
		    		System.out.println("(1) Drone");
		    		System.out.println("(2) Drone Information");
		        	System.out.println("(3) Drone Repair");
		        	System.out.println("(4) Employee");
		        	System.out.println("(5) Equipment");
		        	System.out.println("(6) Equipment Information");
		        	System.out.println("(7) Equipment Ordered");
		        	System.out.println("(8) Equipment Rented");
		        	System.out.println("(9) Items Ordered");
		        	System.out.println("(10) Member");
		        	System.out.println("(11) Rental");
		        	System.out.println("(12) Review");
		        	System.out.println("(13) Warehouse");
		        	System.out.println("(14) Warehouse Order");
		        	System.out.print("Option: ");
		        	int input2 = a.nextInt();
		        	a.nextLine();
		        	if(input2 == 1) {
		        		String toInsert = INSERT1 + D + INSERTD;
		        		System.out.print("Enter serial number: ");
		        		String serialnum = a.nextLine();
		        		System.out.print("Enter a fleet ID: ");
		        		String fleetid = a.nextLine();
		        		System.out.print("Enter a model number: ");
		        		String modelnum = a.nextLine();
		        		System.out.print("Enter a status(True or False): ");
		        		String status = a.nextLine();
		        		System.out.print("Enter a date(MM/DD/YYYY): ");
		        		String date = a.nextLine();
		        		System.out.print("Enter the warehouse ID: ");
		        		int warehouseid = a.nextInt();
		        		a.nextLine();
		        		PreparedStatement ps = conn.prepareStatement(toInsert);
		        		ps.setString(1, serialnum);
		        		ps.setString(2, fleetid);
		        		ps.setString(3, modelnum);
		        		ps.setString(4, status);
		        		ps.setString(5, date);
		        		ps.setInt(6, warehouseid);
		        		ps.executeUpdate();
		        		System.out.println("Record had been added.");
		        	} else if(input2 == 2) {
		        		String toInsert = INSERT1 + DI + INSERTDI;
		        		System.out.print("Enter model number: ");
		        		String modelnum = a.nextLine();
		        		System.out.print("Enter a manufacturer: ");
		        		String man = a.nextLine();
		        		System.out.print("Enter a description: ");
		        		String desc = a.nextLine();
		        		System.out.print("Enter a max speed: ");
		        		int speed = a.nextInt();
		        		a.nextLine();
		        		System.out.print("Enter a year: ");
		        		String year = a.nextLine();
		        		System.out.print("Enter a type: ");
		        		String type = a.nextLine();
		        		System.out.print("Enter a capacity: ");
		        		int cap = a.nextInt();
		        		a.nextLine();
		        		
		        		PreparedStatement ps = conn.prepareStatement(toInsert);
		        		ps.setString(1, modelnum);
		        		ps.setString(2, man);
		        		ps.setString(3, desc);
		        		ps.setInt(4, speed);
		        		ps.setString(5, year);
		        		ps.setString(6, type);
		        		ps.setInt(7, cap);
		        		ps.executeUpdate();
		        		System.out.println("Record had been added.");
		        	} else if(input2 == 3) {
		        		String toInsert = INSERT1 + DR + INSERTDR;
		        		System.out.print("Enter a repair ID: ");
		        		String repair = a.nextLine();
		        		System.out.print("Enter a ssn: ");
		        		String ssn = a.nextLine();
		        		System.out.print("Enter a Repair Date(MM/DD/YYYY): ");
		        		String date = a.nextLine();
		        		System.out.print("Enter a serial number: ");
		        		String serialnum = a.nextLine();
		        		PreparedStatement ps = conn.prepareStatement(toInsert);
		        		ps.setString(1, repair);
		        		ps.setString(2, ssn);
		        		ps.setString(3, date);
		        		ps.setString(4, serialnum);
		        		ps.executeUpdate();
		        		System.out.println("Record had been added.");
		        	} else if(input2 == 4) {
		        		String toInsert = INSERT1 + EMP + INSERTEMP;
		        		System.out.print("Enter a ssn: ");
		        		String ssn = a.nextLine();
		        		System.out.print("Enter a first name: ");
		        		String fname = a.nextLine();
		        		System.out.print("Enter a last name: ");
		        		String lname = a.nextLine();
		        		System.out.print("Enter a skill): ");
		        		String skill = a.nextLine();
		        		System.out.print("Enter a warehouse ID: ");
		        		String warehouseID = a.nextLine();
		        		System.out.print("Enter pay: ");
		        		int pay = a.nextInt();
		        		a.nextLine();
		        		PreparedStatement ps = conn.prepareStatement(toInsert);
		        		ps.setString(1, ssn);
		        		ps.setString(2, fname);
		        		ps.setString(3, lname);
		        		ps.setString(4, skill);
		        		ps.setString(5, warehouseID);
		        		ps.setInt(6, pay);
		        		ps.executeUpdate();
		        		System.out.println("Record had been added.");
		        	} else if(input2 == 5) {
		        		String toInsert = INSERT1 + EQUIP + INSERTEQUIP;
		        		System.out.print("Enter serial number: ");
		        		String serialnum = a.nextLine();
		        		System.out.print("Enter a model number: ");
		        		String modelnum = a.nextLine();
		        		System.out.print("Enter an inventory ID: ");
		        		String invID = a.nextLine();
		        		System.out.print("Enter a warranty date: ");
		        		String date = a.nextLine();
		        		PreparedStatement ps = conn.prepareStatement(toInsert);
		        		ps.setString(1, serialnum);
		        		ps.setString(2, modelnum);
		        		ps.setString(3, invID);
		        		ps.setString(4, date);
		        		ps.executeUpdate();
		        		System.out.println("Record had been added.");
		        	} else if(input2 == 6) {
		        		String toInsert = INSERT1 + EI + INSERTEI;
		        		System.out.print("Enter model number: ");
		        		String modelnum = a.nextLine();
		        		System.out.print("Enter a manufacturer: ");
		        		String man = a.nextLine();
		        		System.out.print("Enter a type: ");
		        		String type = a.nextLine();
		        		System.out.print("Enter a size: ");
		        		String size = a.nextLine();
		        		System.out.print("Enter a year: ");
		        		String year = a.nextLine();
		        		System.out.print("Enter the description: ");
		        		String desc = a.nextLine();
		        		System.out.print("Enter the weight: ");
		        		int weight = a.nextInt();
		        		a.nextLine();
		        		PreparedStatement ps = conn.prepareStatement(toInsert);
		        		ps.setString(1, modelnum);
		        		ps.setString(2, man);
		        		ps.setString(3, type);
		        		ps.setString(4, size);
		        		ps.setString(5, year);
		        		ps.setString(6, desc);
		        		ps.setInt(7, weight);
		        		ps.executeUpdate();
		        		System.out.println("Record had been added.");
		        	} else if(input2 == 7) {
		        		String toInsert = INSERT1 + EO + INSERTD;
		        		System.out.print("Enter an order instance number: ");
		        		String inum = a.nextLine();
		        		System.out.print("Enter an order number: ");
		        		String onum = a.nextLine();
		        		System.out.print("Enter an equipment serial number: ");
		        		String serialnum = a.nextLine();
		        		PreparedStatement ps = conn.prepareStatement(toInsert);
		        		ps.setString(1, inum);
		        		ps.setString(2, onum);
		        		ps.setString(3, serialnum);
		        		ps.executeUpdate();
		        		System.out.println("Record had been added.");
		        	} else if(input2 == 8) {
		        		String toInsert = INSERT1 + ER + INSERTER;
		        		System.out.print("Enter a rental instance number: ");
		        		String inum = a.nextLine();
		        		System.out.print("Enter a rental ID: ");
		        		String renID = a.nextLine();
		        		System.out.print("Enter an equipment serial number: ");
		        		String serialnum = a.nextLine();
		        		PreparedStatement ps = conn.prepareStatement(toInsert);
		        		ps.setString(1, inum);
		        		ps.setString(2, renID);
		        		ps.setString(3, serialnum);
		        		ps.executeUpdate();
		        		System.out.println("Record had been added.");
		        	} else if(input2 == 9) {
		        		String toInsert = INSERT1 + IO + INSERTIO;
		        		System.out.print("Enter an order instance number: ");
		        		String inum = a.nextLine();
		        		System.out.print("Enter a order number: ");
		        		String onum = a.nextLine();
		        		System.out.print("Enter a drone serial number: ");
		        		String serialnum = a.nextLine();
		        		PreparedStatement ps = conn.prepareStatement(toInsert);
		        		ps.setString(1, inum);
		        		ps.setString(2, onum);
		        		ps.setString(3, serialnum);
		        		ps.executeUpdate();
		        		System.out.println("Record had been added.");
		        	} else if(input2 == 10) {
		        		String toInsert = INSERT1 + M + INSERTM;
		        		System.out.print("Enter a member ID: ");
		        		String memID = a.nextLine();
		        		System.out.print("Enter a first name: ");
		        		String fname = a.nextLine();
		        		System.out.print("Enter a last name: ");
		        		String lname = a.nextLine();
		        		System.out.print("Enter an address: ");
		        		String add = a.nextLine();
		        		System.out.print("Enter a card number: ");
		        		String card = a.nextLine();
		        		System.out.print("Enter a start date(MM/DD/YYYY): ");
		        		String date = a.nextLine();
		        		System.out.print("Enter an email: ");
		        		String email = a.nextLine();
		        		PreparedStatement ps = conn.prepareStatement(toInsert);
		        		ps.setString(1, memID);
		        		ps.setString(2, fname);
		        		ps.setString(3, lname);
		        		ps.setString(4, add);
		        		ps.setString(5, date);
		        		ps.setString(6, card);
		        		ps.setString(7, email);
		        		ps.executeUpdate();
		        		System.out.println("Record had been added.");
		        	} else if(input2 == 11) {
		        		String toInsert = INSERT1 + REN + INSERTREN;
		        		System.out.print("Enter a rental ID: ");
		        		String renID = a.nextLine();
		        		System.out.print("Enter a member ID: ");
		        		String memID = a.nextLine();
		        		System.out.print("Enter a drone serial number: ");
		        		String serialnum = a.nextLine();
		        		System.out.print("Enter a price: ");
		        		int price = a.nextInt();
		        		a.nextLine();
		        		System.out.print("Enter a checkout date(MM/DD/YYYY): ");
		        		String checkdate = a.nextLine();
		        		System.out.print("Enter an arrival date(MM/DD/YYYY): ");
		        		String adate = a.nextLine();
		        		System.out.print("Enter a return date(MM/DD/YYYY): ");
		        		String rdate = a.nextLine();
		        		PreparedStatement ps = conn.prepareStatement(toInsert);
		        		ps.setString(1, renID);
		        		ps.setString(2, memID);
		        		ps.setString(3, serialnum);
		        		ps.setInt(4, price);
		        		ps.setString(5, checkdate);
		        		ps.setString(6, adate);
		        		ps.setString(7, rdate);
		        		ps.executeUpdate();
		        		System.out.println("Record had been added.");
		        	} else if(input2 == 12) {
		        		String toInsert = INSERT1 + REV + INSERTREV;
		        		System.out.print("Enter a review number: ");
		        		String rnum = a.nextLine();
		        		System.out.print("Enter an equipment serial number: ");
		        		String serialnum = a.nextLine();
		        		System.out.print("Enter a member ID: ");
		        		String memID = a.nextLine();
		        		System.out.print("Enter a rating: ");
		        		int rat = a.nextInt();
		        		a.nextLine();
		        		System.out.print("Enter review text: ");
		        		String txt = a.nextLine();
		        		PreparedStatement ps = conn.prepareStatement(toInsert);
		        		ps.setString(1, rnum);
		        		ps.setString(2, serialnum);
		        		ps.setString(3, memID);
		        		ps.setInt(4, rat);
		        		ps.setString(5, txt);
		        		ps.executeUpdate();
		        		System.out.println("Record had been added.");
		        	}  else if(input2 == 13) {
		        		String toInsert = INSERT1 + W + INSERTW;
		        		System.out.print("Enter a warehouse ID: ");
		        		String warID = a.nextLine();
		        		System.out.print("Enter a manager ssn: ");
		        		String ssn = a.nextLine();
		        		System.out.print("Enter a drone capacity: ");
		        		int dcap = a.nextInt();
		        		a.nextLine();
		        		System.out.print("Enter an equipment capacity: ");
		        		int ecap = a.nextInt();
		        		a.nextLine();
		        		System.out.print("Enter an address: ");
		        		String add = a.nextLine();
		        		System.out.print("Enter a phone number: ");
		        		String num = a.nextLine();
		        		PreparedStatement ps = conn.prepareStatement(toInsert);
		        		ps.setString(1, warID);
		        		ps.setString(2, ssn);
		        		ps.setInt(3, dcap);
		        		ps.setInt(4, ecap);
		        		ps.setString(5, add);
		        		ps.setString(6, num);
		        		ps.executeUpdate();
		        		System.out.println("Record had been added.");
		        	}  else if(input2 == 14) {
		        		String toInsert = INSERT1 + D + INSERTD;
		        		System.out.print("Enter an order number: ");
		        		String num = a.nextLine();
		        		System.out.print("Enter the number of items: ");
		        		int items = a.nextInt();
		        		a.nextLine();
		        		System.out.print("Enter a description: ");
		        		String desc = a.nextLine();
		        		PreparedStatement ps = conn.prepareStatement(toInsert);
		        		ps.setString(1, num);
		        		ps.setInt(2, items);
		        		ps.setString(3, desc);
		        		ps.executeUpdate();
		        		System.out.println("Record had been added.");
		        	}else {
		        		System.out.println("Not an option!");
		        	}
		    	} else if(input == 2) {
		    		Scanner e = new Scanner(System.in);
		    		System.out.println("Which entity would you like to edit in?");
		    		System.out.println("Please enter a number to the coressponding entity:");
		    		System.out.println("(1) Drone");
		    		System.out.println("(2) Drone Information");
		        	System.out.println("(3) Drone Repair");
		        	System.out.println("(4) Employee");
		        	System.out.println("(5) Equipment");
		        	System.out.println("(6) Equipment Information");
		        	System.out.println("(7) Equipment Ordered");
		        	System.out.println("(8) Equipment Rented");
		        	System.out.println("(9) Items Ordered");
		        	System.out.println("(10) Member");
		        	System.out.println("(11) Rental");
		        	System.out.println("(12) Review");
		        	System.out.println("(13) Warehouse");
		        	System.out.println("(14) Warehouse Order");
		        	System.out.print("Option: ");
		        	int input2 = e.nextInt();
		        	e.nextLine();
		        	if(input2 == 1) {
		        		Scanner edit = new Scanner(System.in);
			        	System.out.print("Enter the serial number that you would like to edit for: ");
			        	String serial = edit.nextLine();
			        	System.out.println("Enter elements to change. If no change re-enter old information."); //Will be changed later
		        		System.out.print("Enter a fleet ID: ");
		        		String fleetid = edit.nextLine();
		        		System.out.print("Enter a model number: ");
		        		String modelnum = edit.nextLine();
		        		System.out.print("Enter a status(True or False): ");
		        		String status = edit.nextLine();
		        		System.out.print("Enter a date(MM/DD/YYYY): ");
		        		String date = edit.nextLine();
		        		System.out.print("Enter the warehouse ID: ");
		        		int warehouseid = edit.nextInt();
		        		edit.nextLine();
			        	String query = "UPDATE DRONE SET fleet_id = ?, model_num = ?, status = ?, warranty_date = ?, warehouse_id = ? WHERE serial = ?; ";
			        	PreparedStatement ps = conn.prepareStatement(query);
			        	ps.setString(1, fleetid);
		        		ps.setString(2, modelnum);
		        		ps.setString(3, status);
		        		ps.setString(4, date);
		        		ps.setInt(5, warehouseid);
		        		ps.setString(6, serial);
		        		ps.executeUpdate();
		        		System.out.println("Successfully updated!");
		        	} else if(input2 == 2) {
		        		Scanner edit = new Scanner(System.in);
		        		System.out.print("Enter the model number that you would like to edit for: ");
			        	String modelnum = edit.nextLine();
			        	System.out.println("Enter elements to change. If no change re-enter old information."); //Will be changed later
		        		System.out.print("Enter a manufacturer: ");
		        		String man = edit.nextLine();
		        		System.out.print("Enter a description: ");
		        		String desc = edit.nextLine();
		        		System.out.print("Enter a max speed: ");
		        		int speed = edit.nextInt();
		        		edit.nextLine();
		        		System.out.print("Enter a year: ");
		        		String year = edit.nextLine();
		        		System.out.print("Enter a type: ");
		        		String type = edit.nextLine();
		        		System.out.print("Enter a capacity: ");
		        		int cap = edit.nextInt();
		        		edit.nextLine();
		        		String query = "UPDATE DRONE_INFO SET model_num = ?, manufacturer = ?, description = ?, max_speed = ?, year = ?, type = ?, capacity = ? WHERE model_num = ?;";
		        		PreparedStatement ps = conn.prepareStatement(query);
		        		ps.setString(1, man);
		        		ps.setString(2, desc);
		        		ps.setInt(3, speed);
		        		ps.setString(4, year);
		        		ps.setString(5, type);
		        		ps.setInt(6, cap);
		        		ps.setString(7, modelnum);
		        		ps.executeUpdate();
		        		System.out.println("Successfully updated!");
		        	} else if(input2 == 3) {
		        		Scanner edit = new Scanner(System.in);
		        		System.out.print("Enter the reapir ID that you would like to edit for: ");
			        	String repair = edit.nextLine();
			        	System.out.println("Enter elements to change. If no change re-enter old information."); //Will be changed later
			        	System.out.print("Enter a ssn: ");
		        		String ssn = edit.nextLine();
		        		System.out.print("Enter a Repair Date(MM/DD/YYYY): ");
		        		String date = edit.nextLine();
		        		System.out.print("Enter a serial number: ");
		        		String serialnum = edit.nextLine();
		        		String query = "UPDATE DRONE_REPAIR SET emp_ssn = ?, repair_date = ?, drone_serial = ? WHERE repair_id = ?;";
		        		PreparedStatement ps = conn.prepareStatement(query);
		        		ps.setString(1, ssn);
		        		ps.setString(2, date);
		        		ps.setString(3, serialnum);
		        		ps.setString(4, repair);
		        		ps.executeUpdate();
		        		System.out.println("Successfully updated!");
		        	} else if(input2 == 4) {
		        		Scanner edit = new Scanner(System.in);
		        		System.out.print("Enter the ssn that you would like to edit for: ");
			        	String ssn = edit.nextLine();
			        	System.out.println("Enter elements to change. If no change re-enter old information."); //Will be changed later
			        	System.out.print("Enter a first name: ");
		        		String fname = edit.nextLine();
		        		System.out.print("Enter a last name: ");
		        		String lname = edit.nextLine();
		        		System.out.print("Enter a skill): ");
		        		String skill = edit.nextLine();
		        		System.out.print("Enter a warehouse ID: ");
		        		String warehouseID = edit.nextLine();
		        		System.out.print("Enter pay: ");
		        		int pay = edit.nextInt();
		        		edit.nextLine();
		        		String query = "UPDATE EMPLOYEE SET fname = ?, lname = ?, skill = ?, warehouse_id = ?, pay = ? WHERE ssn = ?;";
		        		PreparedStatement ps = conn.prepareStatement(query);
		        		ps.setString(1, fname);
		        		ps.setString(2, lname);
		        		ps.setString(3, skill);
		        		ps.setString(4, warehouseID);
		        		ps.setInt(5, pay);
		        		ps.setString(6, ssn);
		        		ps.executeUpdate();
		        		System.out.println("Successfully updated!");
		        	} else if(input2 == 5) {
		        		Scanner edit = new Scanner(System.in);
		        		System.out.print("Enter the serial number that you would like to edit for: ");
			        	String serialnum = edit.nextLine();
			        	System.out.println("Enter elements to change. If no change re-enter old information."); //Will be changed later
			        	System.out.print("Enter a model number: ");
		        		String modelnum = edit.nextLine();
		        		System.out.print("Enter an inventory ID: ");
		        		String invID = edit.nextLine();
		        		System.out.print("Enter a warranty date: ");
		        		String date = edit.nextLine();
		        		String query = "UPDATE EQUIPMENT SET modelnum = ?, inventory_id = ?, warranty_date = ? WHERE serial = ?;";
		        		PreparedStatement ps = conn.prepareStatement(query);
		        		ps.setString(1, modelnum);
		        		ps.setString(2, invID);
		        		ps.setString(3, date);
		        		ps.setString(4, serialnum);
		        		ps.executeUpdate();
		        		System.out.println("Successfully updated!");
		        	} else if(input2 == 6) {
		        		Scanner edit = new Scanner(System.in);
		        		System.out.print("Enter the model number that you would like to edit for: ");
			        	String modelnum = edit.nextLine();
			        	System.out.println("Enter elements to change. If no change re-enter old information."); //Will be changed later
			        	System.out.print("Enter a manufacturer: ");
		        		String man = edit.nextLine();
		        		System.out.print("Enter a type: ");
		        		String type = edit.nextLine();
		        		System.out.print("Enter a size: ");
		        		String size = edit.nextLine();
		        		System.out.print("Enter a year: ");
		        		String year = edit.nextLine();
		        		System.out.print("Enter the description: ");
		        		String desc = edit.nextLine();
		        		System.out.print("Enter the weight: ");
		        		int weight = edit.nextInt();
		        		edit.nextLine();
		        		String query = "UPDATE EQUIPMENT_INFO SET manufacturer = ?, type = ?, size = ?, year = ?, description = ?, weight = ? WHERE model_num = ?;";
		        		PreparedStatement ps = conn.prepareStatement(query);
		        		ps.setString(1, man);
		        		ps.setString(2, type);
		        		ps.setString(3, size);
		        		ps.setString(4, year);
		        		ps.setString(5, desc);
		        		ps.setInt(6, weight);
		        		ps.setString(7, modelnum);
		        		ps.executeUpdate();
		        		System.out.println("Successfully updated!");
		        	} else if(input2 == 7) {
		        		Scanner edit = new Scanner(System.in);
		        		System.out.print("Enter the order instance number that you would like to edit for: ");
			        	String inum = edit.nextLine();
			        	System.out.println("Enter elements to change. If no change re-enter old information."); //Will be changed later
			        	System.out.print("Enter an order number: ");
		        		String onum = edit.nextLine();
		        		System.out.print("Enter an equipment serial number: ");
		        		String serialnum = edit.nextLine();
		        		String query = "UPDATE EQUIPMENT_ORDERED SET oder_num = ?, equipment_serial = ? WHERE order_instance_num = ?;";
		        		PreparedStatement ps = conn.prepareStatement(query);
		        		ps.setString(1, onum);
		        		ps.setString(2, serialnum);
		        		ps.setString(3, inum);
		        		ps.executeUpdate();
		        		System.out.println("Successfully updated!");
		        	} else if(input2 == 8) {
		        		Scanner edit = new Scanner(System.in);
		        		System.out.print("Enter the rental instance number that you would like to edit for: ");
			        	String inum = edit.nextLine();
			        	System.out.println("Enter elements to change. If no change re-enter old information."); //Will be changed later
			        	System.out.print("Enter a rental ID: ");
		        		String renID = edit.nextLine();
		        		System.out.print("Enter an equipment serial number: ");
		        		String serialnum = edit.nextLine();
		        		String query = "UPDATE EQUIPMENT_RENTED SET rental_id = ?, equipment_serial = ? WHERE rental_instance_num = ?;";
		        		PreparedStatement ps = conn.prepareStatement(query);
		        		ps.setString(1, renID);
		        		ps.setString(2, serialnum);
		        		ps.setString(3, inum);
		        		ps.executeUpdate();
		        		System.out.println("Successfully updated!");
		        	} else if(input2 == 9) {
		        		Scanner edit = new Scanner(System.in);
		        		System.out.print("Enter the order instance number that you would like to edit for: ");
			        	String inum = edit.nextLine();
			        	System.out.println("Enter elements to change. If no change re-enter old information."); //Will be changed later
			        	System.out.print("Enter a order number: ");
		        		String onum = edit.nextLine();
		        		System.out.print("Enter a drone serial number: ");
		        		String serialnum = edit.nextLine();
		        		String query = "UPDATE ITEMS_ORDERED SET order_num = ?, drone_serial = ? WHERE order_instance_num = ?; ";
		        		PreparedStatement ps = conn.prepareStatement(query);
		        		ps.setString(1, onum);
		        		ps.setString(2, serialnum);
		        		ps.setString(3, inum);
		        		ps.executeUpdate();
			        	System.out.println("Successfully updated!");
		        	} else if(input2 == 10) {
		        		Scanner edit = new Scanner(System.in);
		        		System.out.print("Enter the member ID that you would like to edit for: ");
			        	String memID = edit.nextLine();
			        	System.out.println("Enter elements to change. If no change re-enter old information."); //Will be changed later
		        		System.out.print("Enter a first name: ");
		        		String fname = edit.nextLine();
		        		System.out.print("Enter a last name: ");
		        		String lname = edit.nextLine();
		        		System.out.print("Enter an address: ");
		        		String add = edit.nextLine();
		        		System.out.print("Enter a card number: ");
		        		String card = edit.nextLine();
		        		System.out.print("Enter a start date(MM/DD/YYYY): ");
		        		String date = edit.nextLine();
		        		System.out.print("Enter an email: ");
		        		String email = edit.nextLine();
		        		String query = "UPDATE MEMBER SET fname = ?, lname = ?, address = ?, cardnum = ?, start = ?, email = ? WHERE member_id = ?;";
		        		PreparedStatement ps = conn.prepareStatement(query);
		        		ps.setString(1, memID);
		        		ps.setString(2, fname);
		        		ps.setString(3, lname);
		        		ps.setString(4, add);
		        		ps.setString(5, date);
		        		ps.setString(6, card);
		        		ps.setString(7, email);
		        		ps.executeUpdate();
			        	System.out.println("Successfully updated!");
		        	} else if(input2 == 11) {
		        		Scanner edit = new Scanner(System.in);
		        		System.out.print("Enter the rental ID that you would like to edit for: ");
			        	String renID = edit.nextLine();
			        	System.out.println("Enter elements to change. If no change re-enter old information."); //Will be changed later
			        	System.out.print("Enter a member ID: ");
		        		String memID = edit.nextLine();
		        		System.out.print("Enter a drone serial number: ");
		        		String serialnum = edit.nextLine();
		        		System.out.print("Enter a price: ");
		        		int price = edit.nextInt();
		        		edit.nextLine();
		        		System.out.print("Enter a checkout date(MM/DD/YYYY): ");
		        		String checkdate = edit.nextLine();
		        		System.out.print("Enter an arrival date(MM/DD/YYYY): ");
		        		String adate = edit.nextLine();
		        		System.out.print("Enter a return date(MM/DD/YYYY): ");
		        		String rdate = edit.nextLine();
		        		String query = "UPDATE RENTAL SET member_id = ?, drone_serial = ?, price = ?, checkout = ?, arrival = ?, return = ? WHERE rental_id = ?;";
		        		PreparedStatement ps = conn.prepareStatement(query);
		        		ps.setString(1, memID);
		        		ps.setString(2, serialnum);
		        		ps.setInt(3, price);
		        		ps.setString(4, checkdate);
		        		ps.setString(5, adate);
		        		ps.setString(6, rdate);
		        		ps.setString(7, renID);
		        		ps.executeUpdate();
			        	System.out.println("Successfully updated!");
		        	} else if(input2 == 12) {
		        		Scanner edit = new Scanner(System.in);
		        		System.out.print("Enter the review number that you would like to edit for: ");
			        	String rnum = edit.nextLine();
			        	System.out.println("Enter elements to change. If no change re-enter old information."); //Will be changed later
			        	System.out.print("Enter an equipment serial number: ");
		        		String serialnum = edit.nextLine();
		        		System.out.print("Enter a member ID: ");
		        		String memID = edit.nextLine();
		        		System.out.print("Enter a rating: ");
		        		int rat = edit.nextInt();
		        		edit.nextLine();
		        		System.out.print("Enter review text: ");
		        		String txt = edit.nextLine();
		        		String query = "UPDATE REVIEW SET equip_serial = ?, member_id = ?, rating = ?, review_text = ? WHERE review_num = ?;";
		        		PreparedStatement ps = conn.prepareStatement(query);
		        		ps.setString(1, serialnum);
		        		ps.setString(2, memID);
		        		ps.setInt(3, rat);
		        		ps.setString(4, txt);
		        		ps.setString(5, rnum);
		        		ps.executeUpdate();
			        	System.out.println("Successfully updated!");
		        	}  else if(input2 == 13) {
		        		Scanner edit = new Scanner(System.in);
		        		System.out.print("Enter the warehouse ID that you would like to edit for: ");
			        	String warID = edit.nextLine();
			        	System.out.println("Enter elements to change. If no change re-enter old information."); //Will be changed later
			        	System.out.print("Enter a manager ssn: ");
		        		String ssn = edit.nextLine();
		        		System.out.print("Enter a drone capacity: ");
		        		int dcap = edit.nextInt();
		        		edit.nextLine();
		        		System.out.print("Enter an equipment capacity: ");
		        		int ecap = edit.nextInt();
		        		edit.nextLine();
		        		System.out.print("Enter an address: ");
		        		String add = edit.nextLine();
		        		System.out.print("Enter a phone number: ");
		        		String num = edit.nextLine();
		        		String query = "UPDATE WAREHOUSE SET manager_ssn = ?, drone_capacity = ?, equipment_capacity = ?, address = ?, phone = ? WHERE warehouse_id = ?;";
		        		PreparedStatement ps = conn.prepareStatement(query);
		        		ps.setString(1, ssn);
		        		ps.setInt(2, dcap);
		        		ps.setInt(3, ecap);
		        		ps.setString(4, add);
		        		ps.setString(5, num);
		        		ps.setString(6, warID);
		        		ps.executeUpdate();
			        	System.out.println("Successfully updated!");
		        	}  else if(input2 == 14) {
		        		Scanner edit = new Scanner(System.in);
		        		System.out.print("Enter the order number that you would like to edit for: ");
			        	String num = edit.nextLine();
			        	System.out.println("Enter elements to change. If no change re-enter old information."); //Will be changed later
			        	System.out.print("Enter the number of items: ");
		        		int items = edit.nextInt();
		        		edit.nextLine();
		        		System.out.print("Enter a description: ");
		        		String desc = edit.nextLine();
		        		String query = "UPDATE WAREHOUSE_ORDER SET num_items = ?, description = ? WHERE order_number = ?;";
		        		PreparedStatement ps = conn.prepareStatement(query);
		        		ps.setInt(1, items);
		        		ps.setString(2, desc);
		        		ps.setString(3, num);
		        		ps.executeUpdate();
			        	System.out.println("Successfully updated!");
		        	}else {
		        		System.out.println("Not an option!");
		        	}
		    		
		    	} else if (input == 3) {
		    		Scanner d = new Scanner(System.in);
		    		System.out.println("Which entity would you like to delete in?");
		    		System.out.println("Please enter a number to the coressponding entity:");
		    		System.out.println("(1) Drone");
		    		System.out.println("(2) Drone Information");
		        	System.out.println("(3) Drone Repair");
		        	System.out.println("(4) Employee");
		        	System.out.println("(5) Equipment");
		        	System.out.println("(6) Equipment Information");
		        	System.out.println("(7) Equipment Ordered");
		        	System.out.println("(8) Equipment Rented");
		        	System.out.println("(9) Items Ordered");
		        	System.out.println("(10) Member");
		        	System.out.println("(11) Rental");
		        	System.out.println("(12) Review");
		        	System.out.println("(13) Warehouse");
		        	System.out.println("(14) Warehouse Order");
		        	System.out.print("Option: ");
		        	int input2 = d.nextInt();
		        	d.nextLine();
		        	if(input2 == 1) {
		        		String toDelete = DELETE1 + D + DELETESER;
			    		System.out.print("Enter the serial number that you would like to delete the record for: ");
			    		String num = d.nextLine();
			    		PreparedStatement ps = conn.prepareStatement(toDelete);
			    		ps.setString(1, num);
			    		ps.executeUpdate();
			    		System.out.println("Record had been deleted.");
		        	} else if(input2 == 2) {
		        		String toDelete = DELETE1 + DI + DELETEMOD;
			    		System.out.print("Enter the model number that you would like to delete the record for: ");
			    		String num = d.nextLine();
			    		PreparedStatement ps = conn.prepareStatement(toDelete);
			    		ps.setString(1, num);
			    		ps.executeUpdate();
			    		System.out.println("Record had been deleted.");
		        	} else if(input2 == 3) {
		        		String toDelete = DELETE1 + DR + DELETEREP;
			    		System.out.print("Enter the repair ID that you would like to delete the record for: ");
			    		String num = d.nextLine();
			    		PreparedStatement ps = conn.prepareStatement(toDelete);
			    		ps.setString(1, num);
			    		ps.executeUpdate();
			    		System.out.println("Record had been deleted.");
		        	} else if(input2 == 4) {
		        		String toDelete = DELETE1 + EMP + DELETESSN;
			    		System.out.print("Enter the ssn(XXX-XX-XXXX) that you would like to delete the record for: ");
			    		String num = d.nextLine();
			    		PreparedStatement ps = conn.prepareStatement(toDelete);
			    		ps.setString(1, num);
			    		ps.executeUpdate();
			    		System.out.println("Record had been deleted.");
		        	} else if(input2 == 5) {
		        		String toDelete = DELETE1 + EQUIP + DELETESER;
			    		System.out.print("Enter the serial number that you would like to delete the record for: ");
			    		String num = d.nextLine();
			    		PreparedStatement ps = conn.prepareStatement(toDelete);
			    		ps.setString(1, num);
			    		ps.executeUpdate();
			    		System.out.println("Record had been deleted.");
		        	} else if(input2 == 6) {
		        		String toDelete = DELETE1 + EI + DELETEMOD;
			    		System.out.print("Enter the model number that you would like to delete the record for: ");
			    		String num = d.nextLine();
			    		PreparedStatement ps = conn.prepareStatement(toDelete);
			    		ps.setString(1, num);
			    		ps.executeUpdate();
			    		System.out.println("Record had been deleted.");
		        	}else if(input2 == 7) {
		        		String toDelete = DELETE1 + EO + DELETEOIN;
			    		System.out.print("Enter the order instance number that you would like to delete the record for: ");
			    		String num = d.nextLine();
			    		PreparedStatement ps = conn.prepareStatement(toDelete);
			    		ps.setString(1, num);
			    		ps.executeUpdate();
			    		System.out.println("Record had been deleted.");
		        	} else if(input2 == 8) {
		        		String toDelete = DELETE1 + ER + DELETERIN;
			    		System.out.print("Enter the rental instance number that you would like to delete the record for: ");
			    		String num = d.nextLine();
			    		PreparedStatement ps = conn.prepareStatement(toDelete);
			    		ps.setString(1, num);
			    		ps.executeUpdate();
			    		System.out.println("Record had been deleted.");
		        	} else if(input2 == 9) {
		        		String toDelete = DELETE1 + IO + DELETEOIN;
			    		System.out.print("Enter the order instance number that you would like to delete the record for: ");
			    		String num = d.nextLine();
			    		PreparedStatement ps = conn.prepareStatement(toDelete);
			    		ps.setString(1, num);
			    		ps.executeUpdate();
			    		System.out.println("Record had been deleted.");
		        	} else if(input2 == 10) {
		        		String toDelete = DELETE1 + REN + DELETEREN;
			    		System.out.print("Enter the rental ID that you would like to delete the record for: ");
			    		String num = d.nextLine();
			    		PreparedStatement ps = conn.prepareStatement(toDelete);
			    		ps.setString(1, num);
			    		ps.executeUpdate();
			    		System.out.println("Record had been deleted.");
		        	} else if(input2 == 11) {
		        		String toDelete = DELETE1 + REV + DELETEREV;
			    		System.out.print("Enter the review number that you would like to delete the record for: ");
			    		String num = d.nextLine();
			    		PreparedStatement ps = conn.prepareStatement(toDelete);
			    		ps.setString(1, num);
			    		ps.executeUpdate();
			    		System.out.println("Record had been deleted.");
		        	} else if(input2 == 12) {
		        		String toDelete = DELETE1 + DI + DELETEMOD;
			    		System.out.print("Enter the model number that you would like to delete the record for: ");
			    		String num = d.nextLine();
			    		PreparedStatement ps = conn.prepareStatement(toDelete);
			    		ps.setString(1, num);
			    		ps.executeUpdate();
			    		System.out.println("Record had been deleted.");
		        	} else if(input2 == 13) {
		        		String toDelete = DELETE1 + W + DELETEW;
			    		System.out.print("Enter the warehouse ID that you would like to delete the record for: ");
			    		String num = d.nextLine();
			    		PreparedStatement ps = conn.prepareStatement(toDelete);
			    		ps.setString(1, num);
			    		ps.executeUpdate();
			    		System.out.println("Record had been deleted.");
		        	}  else if(input2 == 14) {
		        		String toDelete = DELETE1 + WO + DELETEO;
			    		System.out.print("Enter the order number that you would like to delete the record for: ");
			    		String num = d.nextLine();
			    		PreparedStatement ps = conn.prepareStatement(toDelete);
			    		ps.setString(1, num);
			    		ps.executeUpdate();
			    		System.out.println("Record had been deleted.");
		        	}else {
		        		System.out.println("Not an option!");
		        	}
		    	} else if(input == 4) {
		    		Scanner s = new Scanner(System.in);
		    		System.out.println("Which entity would you like to search in?");
		    		System.out.println("Please enter a number to the coressponding entity:");
		    		System.out.println("(1) Drone");
		    		System.out.println("(2) Drone Information");
		        	System.out.println("(3) Drone Repair");
		        	System.out.println("(4) Employee");
		        	System.out.println("(5) Equipment");
		        	System.out.println("(6) Equipment Information");
		        	System.out.println("(7) Equipment Ordered");
		        	System.out.println("(8) Equipment Rented");
		        	System.out.println("(9) Items Ordered");
		        	System.out.println("(10) Member");
		        	System.out.println("(11) Rental");
		        	System.out.println("(12) Review");
		        	System.out.println("(13) Warehouse");
		        	System.out.println("(14) Warehouse Order");
		        	System.out.print("Option: ");
		        	int input2 = s.nextInt();
		        	s.nextLine();
		        	if(input2 == 1) {
		        		String toView = VIEW + D;
		        		System.out.print("Enter something that you would like to search: ");
		        		String toSearch = s.nextLine();
		        		sqlSearch(conn, toView, toSearch);
		        	} else if(input2 == 2) {
		        		String toView = VIEW + DI;
		        		System.out.print("Enter something that you would like to search: ");
		        		String toSearch = s.nextLine();
		        		sqlSearch(conn, toView, toSearch);
		        	} else if(input2 == 3) {
		        		String toView = VIEW + DR;
		        		System.out.print("Enter something that you would like to search: ");
		        		String toSearch = s.nextLine();
		        		sqlSearch(conn, toView, toSearch);
		        	} else if(input2 == 4) {
		        		String toView = VIEW + EMP;
		        		System.out.print("Enter something that you would like to search: ");
		        		String toSearch = s.nextLine();
		        		sqlSearch(conn, toView, toSearch);
		        	} else if(input2 == 5) {
		        		String toView = VIEW + EQUIP;
		        		System.out.print("Enter something that you would like to search: ");
		        		String toSearch = s.nextLine();
		        		sqlSearch(conn, toView, toSearch);
		        	} else if(input2 == 6) {
		        		String toView = VIEW + EI;
		        		System.out.print("Enter something that you would like to search: ");
		        		String toSearch = s.nextLine();
		        		sqlSearch(conn, toView, toSearch);
		        	} else if(input2 == 7) {
		        		String toView = VIEW + EO;
		        		System.out.print("Enter something that you would like to search: ");
		        		String toSearch = s.nextLine();
		        		sqlSearch(conn, toView, toSearch);
		        	} else if(input2 == 8) {
		        		String toView = VIEW + ER;
		        		System.out.print("Enter something that you would like to search: ");
		        		String toSearch = s.nextLine();
		        		sqlSearch(conn, toView, toSearch);
		        	} else if(input2 == 9) {
		        		String toView = VIEW + IO;
		        		System.out.print("Enter something that you would like to search: ");
		        		String toSearch = s.nextLine();
		        		sqlSearch(conn, toView, toSearch);
		        	} else if(input2 == 10) {
		        		String toView = VIEW + M;
		        		System.out.print("Enter something that you would like to search: ");
		        		String toSearch = s.nextLine();
		        		sqlSearch(conn, toView, toSearch);
		        	} else if(input2 == 11) {
		        		String toView = VIEW + REN;
		        		System.out.print("Enter something that you would like to search: ");
		        		String toSearch = s.nextLine();
		        		sqlSearch(conn, toView, toSearch);
		        	} else if(input2 == 12) {
		        		String toView = VIEW + REV;
		        		System.out.print("Enter something that you would like to search: ");
		        		String toSearch = s.nextLine();
		        		sqlSearch(conn, toView, toSearch);
		        	}  else if(input2 == 13) {
		        		String toView = VIEW + W;
		        		System.out.print("Enter something that you would like to search: ");
		        		String toSearch = s.nextLine();
		        		sqlSearch(conn, toView, toSearch);
		        	}  else if(input2 == 14) {
		        		String toView = VIEW + WO;
		        		System.out.print("Enter something that you would like to search: ");
		        		String toSearch = s.nextLine();
		        		sqlSearch(conn, toView, toSearch);
		        	}else {
		        		System.out.println("Not an option!");
		        	}
		    	} else if(input == 5){
		    		Scanner add = new Scanner(System.in);
		    		System.out.println("Which entity would you like view?");
		    		System.out.println("Please enter a number to the coressponding entity:");
		    		System.out.println("(1) Drone");
		    		System.out.println("(2) Drone Information");
		        	System.out.println("(3) Drone Repair");
		        	System.out.println("(4) Employee");
		        	System.out.println("(5) Equipment");
		        	System.out.println("(6) Equipment Information");
		        	System.out.println("(7) Equipment Ordered");
		        	System.out.println("(8) Equipment Rented");
		        	System.out.println("(9) Items Ordered");
		        	System.out.println("(10) Member");
		        	System.out.println("(11) Rental");
		        	System.out.println("(12) Review");
		        	System.out.println("(13) Warehouse");
		        	System.out.println("(14) Warehouse Order");
		        	System.out.print("Option: ");
		        	int input2 = add.nextInt();
		        	add.nextLine();
		        	if(input2 == 1) {
		        		String toView = VIEW + D;
		        		sqlQuery(conn, toView);
		        	} else if(input2 == 2) {
		        		String toView = VIEW + DI;
		        		sqlQuery(conn, toView);
		        	} else if(input2 == 3) {
		        		String toView = VIEW + DR;
		        		sqlQuery(conn, toView);
		        	} else if(input2 == 4) {
		        		String toView = VIEW + EMP;
		        		sqlQuery(conn, toView);
		        	} else if(input2 == 5) {
		        		String toView = VIEW + EQUIP;
		        		sqlQuery(conn, toView);
		        	} else if(input2 == 6) {
		        		String toView = VIEW + EI;
		        		sqlQuery(conn, toView);
		        	} else if(input2 == 7) {
		        		String toView = VIEW + EO;
		        		sqlQuery(conn, toView);
		        	} else if(input2 == 8) {
		        		String toView = VIEW + ER;
		        		sqlQuery(conn, toView);
		        	} else if(input2 == 9) {
		        		String toView = VIEW + IO;
		        		sqlQuery(conn, toView);
		        	} else if(input2 == 10) {
		        		String toView = VIEW + M;
		        		sqlQuery(conn, toView);
		        	} else if(input2 == 11) {
		        		String toView = VIEW + REN;
		        		sqlQuery(conn, toView);
		        	} else if(input2 == 12) {
		        		String toView = VIEW + REV;
		        		sqlQuery(conn, toView);
		        	}  else if(input2 == 13) {
		        		String toView = VIEW + W;
		        		sqlQuery(conn, toView);
		        	}  else if(input2 == 14) {
		        		String toView = VIEW + WO;
		        		sqlQuery(conn, toView);
		        	}else {
		        		System.out.println("Not an option!");
		        	}
		    	} else if(input == 6) {
		    		Scanner n = new Scanner(System.in);
		    		System.out.print("Enter the member ID you would like to search: ");
		    		String str = n.nextLine();
		    		String query = "SELECT COUNT(RENTAL.rental_id) FROM MEMBER, RENTAL WHERE MEMBER.member_id = RENTAL.member_id AND MEMBER.member_id = ?;";
		    		PreparedStatement ps = conn.prepareStatement(query);
		    		ps.setString(1, str);
		    		ResultSet rs = ps.executeQuery();
		    		System.out.print("Member " + str + " has rented ");
		    		printResultSet(rs);
		    		System.out.print("device(s).");
		    		System.out.println();
		    	} else if(input == 7) {
		    		String query = "SELECT e.serial as most_rented_item, count(e.serial) as times_rented FROM equipment_rented as er, equipment as e WHERE er.equipment_serial = e.serial GROUP BY e.serial ORDER BY times_rented DESC LIMIT 1;";
		    		PreparedStatement ps = conn.prepareStatement(query);
		    		ResultSet rs = ps.executeQuery();
		    	    System.out.print("Serial Number, Count\n");
		    		printResultSet(rs);
		    		System.out.println();
		    	} else if(input == 8) {
		    		String query = "SELECT manufacturer, count(rental_instance_num) as total_equipment_rented FROM EQUIPMENT_RENTED, EQUIPMENT_INFO, EQUIPMENT WHERE modelnum = model_num AND equipment_serial = serial GROUP BY manufacturer ORDER BY total_equipment_rented DESC LIMIT 1;";
		    		PreparedStatement ps = conn.prepareStatement(query);
		    		ResultSet rs = ps.executeQuery();
		    		System.out.print("Manufacturer, Count\n");
		    		printResultSet(rs);
		    		System.out.println();
		    	} else if(input == 9) {
		    		String query = "SELECT drone_serial, count(drone_serial) as orders_delivered FROM rental GROUP BY drone_serial ORDER BY orders_delivered DESC LIMIT 1;";
		    		PreparedStatement ps = conn.prepareStatement(query);
		    		ResultSet rs = ps.executeQuery();
		    		System.out.print("Serial Number, Count\n");
		    		printResultSet(rs);
		    		System.out.println();
		    	} else if(input == 10) {
		    		//doesnt work
		    		String query = "SELECT MEMBER.member_id, MAX(RENTAL.rental_id) FROM EQUIPMENT_INFO as EQ_INFO, MEMBER, RENTAL, EQUIPMENT_RENTED, EQUIPMENT as EQ WHERE MEMBER.member_id = RENTAL.member_id AND EQUIPMENT_RENTED.equipment_serial = EQ.serial AND EQ.modelnum = EQ_INFO.model_num AND (EQ_INFO.type = 'computer' OR EQ_INFO.type = 'internet');";
		    		PreparedStatement ps = conn.prepareStatement(query);
		    		ResultSet rs = ps.executeQuery();
		    		printResultSet(rs);
		    		System.out.println();
		    	} else if(input == 11) {
		    		Scanner n = new Scanner(System.in);
		    		System.out.print("Enter a year to search: ");
		    		int year = n.nextInt();
		    		n.nextLine();
		    		String query = "SELECT description FROM EQUIPMENT_INFO as EQ_INFO WHERE EQ_INFO.manufacturer = MANUFACTURER AND EQ_INFO.year <?;";
		    		PreparedStatement ps = conn.prepareStatement(query);
		    		ps.setInt(1, year);
		    		ResultSet rs = ps.executeQuery();
		    		System.out.print("Equipment desrciptions of equipment released before " + year + ": ");
		    		printResultSet(rs);
		    		System.out.println();
		    	} else {
		    		System.out.println("Not an option!");
		    	}
	    	}
    	} while(input !=0);
    }
    
}