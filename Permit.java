package DBMS_Project_Demo;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.util.Scanner;


public class Permit {

	static Scanner scan = new Scanner(System.in);
    static int selection = 0;
    static int permitID=0;
    static String spaceType="";
    static String startDate="";
    static String expirationDate="";
    static String permitType="";
    static String expirationTime="";
    static int lot=0;
    static String zoneID="";
    
    
	public static void permitOptions(Statement statement) throws ClassNotFoundException, SQLException, ParseException {

        do {

            System.out.println("Permit Menu");
            System.out.println();
            System.out.println("1. Add Permit Information");
            System.out.println("2. Update Permit Validity");
            System.out.println("3. Update Permit Information");
            System.out.println("4. Get Permit Information");
            System.out.println("5. Check Permit Validity");
            System.out.println("6. Delete Permit");
            System.out.println("7. Back to Home Menu");

            System.out.println("Make a selection: ");

            selection = scan.nextInt();

            switch (selection) {
            case 1:
                addPermitInformation(statement);
                break;
            case 2:
            	int permitIDupdate;
            	String newexpirationDate;
            	System.out.println("Enter the Permit ID of the record that needs to be updated: ");
            	permitIDupdate = scan.nextInt();
            	scan.nextLine();
            	System.out.println("Enter the new expiration Date: ");
            	newexpirationDate = scan.nextLine();
            	updatePermitExpiration(statement, permitIDupdate, newexpirationDate);
            	break;
            case 3:
            	updatePermitInformation(statement);
            	break;
            case 4:
            	getValuesFromUser(statement);
            	break;
            case 5:
            	checkPermitIDGetUserChoice(statement);
            	break;
            case 6:
            	int permitIDdelete;
            	System.out.println("Enter the Permit ID of the record that needs to be deleted: ");
            	permitIDdelete = scan.nextInt();
            	deletePermit(statement, permitIDdelete);
            	break;
            case 7:
                System.out.println("Back to Home menu");
                break;
            default:
                System.out.println("The selection is invalid");
                break;
            }

        } while (selection != 7);
    }
	//this method is used to add Permit Information
	public static void addPermitInformation(Statement statement) throws ParseException, ClassNotFoundException, SQLException {

        System.out.println("1. Enter Permit ID: ");
        permitID = scan.nextInt();
        scan.nextLine();
        System.out.println("2. Enter Space Type(“Electric”, “Handicap”, “Compact car”, default is “Regular”): ");
        spaceType = scan.nextLine();
        System.out.println("3. Enter Start Date(YYYY-MM-DD): ");
        startDate = scan.nextLine();
        System.out.println("4. Enter Expiration Date(YYYY-MM-DD): ");
        expirationDate = scan.nextLine();
        System.out.println("5. Enter Permit Type(“residential”, “commuter”, “peak hours”, “special event”, and “Park & Ride”): ");
        permitType = scan.nextLine();
        System.out.println("6. Enter ExpirationTime(HH:mm:ss): ");
        expirationTime = scan.nextLine();
        System.out.println("7. Enter parking lot ID: ");
        lot = scan.nextInt();
        scan.nextLine();
        System.out.println("8. Enter zone ID : ");
        zoneID = scan.nextLine();
        insertPermit(statement, permitID, spaceType, startDate, expirationDate,
        		permitType, expirationTime, lot, zoneID);      
    }
	
	private static void insertPermit(Statement statement, int permitID, String spaceType, String startDate,
            String expirationDate, String permitType, String expirationTime, int lot, String zoneID) throws SQLException {
	String insertQuery = "INSERT INTO Permit (permitID, spaceType, startDate, expirationDate, permitType, expirationTime, lot, zoneID) VALUES ("
	+ permitID + ", '" + spaceType + "', '" + startDate + "', '" + expirationDate + "', '"
	+ permitType + "', '" + expirationTime + "', " + lot + ", '"+ zoneID + "')";
	
	try {
		statement.executeUpdate(insertQuery);
		System.out.println("Permit information inserted successfully.");
	} catch (SQLException e) {
		handleSQLException(e);
	}
}
	public static void updatePermitExpiration(Statement statement, int permitID, String newExpirationDate) throws SQLException {
		String updateQuery = "UPDATE Permit SET expirationDate = '" + newExpirationDate + "' WHERE permitID = " + permitID;

        try {
            int rowsAffected = statement.executeUpdate(updateQuery);
            if (rowsAffected > 0) {
                System.out.println("Permit information updated successfully.");
            } else {
                System.out.println("Permit not found for update.");
            }
        } catch (SQLException e) {
            handleSQLException(e);
        }   
	}
	//this method deletes permit
	private static void deletePermit(Statement statement, int permitID) throws SQLException {

        String deleteQuery = "DELETE FROM Permit WHERE permitID = " + permitID;
        try {
            int rowsAffected = statement.executeUpdate(deleteQuery);
            if (rowsAffected > 0) {
                System.out.println("Permit information deleted successfully.");
            } else {
                System.out.println("Permit not found for deletion.");
            }
        } catch (SQLException e) {
            handleSQLException(e);
        }
    }
	
	//update permit information
	private static void updatePermitInformation(Statement statement)
            throws SQLException {
		System.out.println("Enter Permit ID to update:");
		   int permitID = scan.nextInt();
		   scan.nextLine();

		// Display menu for attribute selection
		   System.out.println("Select option:");
		   System.out.println("1. Update a Single Attribute");
		   System.out.println("2. Update Multiple Attributes");

		   // Get user choice
		   int optionChoice = scan.nextInt();
		   scan.nextLine();

		   if (optionChoice == 1) {
		       // Update a single attribute
		       updateSingleAttribute(statement, permitID);
		   } else if (optionChoice == 2) {
		       // Update multiple attributes
		       updateMultipleAttributes(statement, permitID);
		   } else {
		       System.out.println("Invalid option. No attribute updated.");
		   } 
    }
	
	private static void updateSingleAttribute(Statement statement, int permitID) {
        // Display menu for attribute selection
        System.out.println("Select attribute to update:");
        System.out.println("1. Space Type(“Electric”, “Handicap”, “Compact car”, default is “Regular”)");
        System.out.println("2. Start Date(YYYY-MM-DD)");
        System.out.println("3. Expiration Date(YYYY-MM-DD)");
        System.out.println("4. Permit Type(“residential”, “commuter”, “peak hours”, “special event”, and “Park & Ride”)");
        System.out.println("5. Expiration Time(HH:mm:ss)");
        System.out.println("6. Lot");
        System.out.println("7. ZoneID");

        // Get user choice
        int choice = scan.nextInt();
        scan.nextLine();

        // Get the new value from the user
        System.out.println("Enter new value:");
        String newValue = scan.nextLine();

        String attributeName;
        switch (choice) {
            case 1:
                attributeName = "spaceType";
                break;
            case 2:
                attributeName = "startDate";
                break;
            case 3:
                attributeName = "expirationDate";
                break;
            case 4:
                attributeName = "permitType";
                break;
            case 5:
                attributeName = "expirationTime";
                break;
            case 6:
                attributeName = "lot";
                break;
            case 7:
                attributeName = "zoneID";
                break;
            default:
                System.out.println("Invalid choice. No attribute updated.");
                return;
        }

        String updateQuery = "UPDATE Permit SET " + attributeName + " = '" + newValue + "' WHERE permitID = " + permitID;

        try {
            int rowsAffected = statement.executeUpdate(updateQuery);
            if (rowsAffected > 0) {
                System.out.println("Permit information updated successfully.");
            } else {
                System.out.println("Permit not found for update.");
            }
        } catch (SQLException e) {
            handleSQLException(e);
        }
    }

    private static void updateMultipleAttributes(Statement statement, int permitID) {
        // Get the new values from the user
        System.out.println("Enter new Space Type(“Electric”, “Handicap”, “Compact car”, default is “Regular”):");
        String newSpaceType = scan.nextLine();

        System.out.println("Enter new Start Date(YYYY-MM-DD):");
        String newStartDate = scan.nextLine();

        System.out.println("Enter new Expiration Date(YYYY-MM-DD):");
        String newExpirationDate = scan.nextLine();

        System.out.println("Enter new Permit Type (“residential”, “commuter”, “peak hours”, “special event”, and “Park & Ride”):");
        String newPermitType = scan.nextLine();

        System.out.println("Enter new Expiration Time(HH:mm:ss):");
        String newExpirationTime = scan.nextLine();

        System.out.println("Enter new Lot:");
        int newLot = scan.nextInt();
        scan.nextLine();
        
        System.out.println("Enter new zoneID:");
        String zoneID = scan.nextLine();

        String updateQuery = "UPDATE Permit SET spaceType = '" + newSpaceType + "', startDate = '" + newStartDate
                + "', expirationDate = '" + newExpirationDate + "', permitType = '" + newPermitType
                + "', expirationTime = '" + newExpirationTime + "', lot = " + newLot + ", zoneID = '" + zoneID
                + "' WHERE permitID = " + permitID;

        try {
            int rowsAffected = statement.executeUpdate(updateQuery);
            if (rowsAffected > 0) {
                System.out.println("Permit information updated successfully.");
            } else {
                System.out.println("Permit not found for update.");
            }
        } catch (SQLException e) {
            handleSQLException(e);
        }
    }
	
	private static void getValuesFromUser(Statement statement) throws SQLException {
		
        System.out.println("Enter the attribute based on which you want to retrieve values, or choose an option:");
        System.out.println("1. Permit ID");
        System.out.println("2. Space Type");
        System.out.println("3. Start Date");
        System.out.println("4. Expiration Date");
        System.out.println("5. Permit Type");
        System.out.println("6. Expiration Time");
        System.out.println("7. Lot");
        System.out.println("8. ZoneID");
        System.out.println("9. Retrieve the entire Permit Table");

        int choice = scan.nextInt();

        switch (choice) {
            case 1:
                getValueByAttribute(statement, "permitID");
                break;
            case 2:
                getValueByAttribute(statement, "spaceType");
                break;
            case 3:
                getValueByAttribute(statement, "startDate");
                break;
            case 4:
                getValueByAttribute(statement, "expirationDate");
                break;
            case 5:
                getValueByAttribute(statement, "permitType");
                break;
            case 6:
                getValueByAttribute(statement, "expirationTime");
                break;
            case 7:
                getValueByAttribute(statement, "lot");
                break;
            case 8:
                getValueByAttribute(statement, "zoneID");
                break;   
            case 9:
                retrieveEntirePermitTable(statement);
                break;
            default:
                System.out.println("Invalid choice. Please enter a number between 1 and 8.");
        }
    }

    private static void getValueByAttribute(Statement statement, String attribute) {

        System.out.println("Enter the value for " + attribute + ":");
        String value = scan.next();

        String selectQuery = "SELECT * FROM Permit WHERE " + attribute + " = '" + value + "'";

        try (ResultSet resultSet = statement.executeQuery(selectQuery)) {
            while (resultSet.next()) {
                // Process and print permit information
                System.out.println("Permit ID: " + resultSet.getInt("permitID"));
                System.out.println("Space Type: " + resultSet.getString("spaceType"));
                System.out.println("Start Date: " + resultSet.getString("startDate"));
                System.out.println("Expiration Date: " + resultSet.getString("expirationDate"));
                System.out.println("Permit Type: " + resultSet.getString("permitType"));
                System.out.println("Expiration Time: " + resultSet.getString("expirationTime"));
                System.out.println("Lot: " + resultSet.getInt("lot"));
                System.out.println("Zone ID: " + resultSet.getString("zoneID"));
                System.out.println("------------");
            }
        } catch (SQLException e) {
            handleSQLException(e);
        }
    }

    private static void retrieveEntirePermitTable(Statement statement) {
        String selectQuery = "SELECT * FROM Permit";

        try (ResultSet resultSet = statement.executeQuery(selectQuery)) {
            while (resultSet.next()) {
                // Process and print permit information for the entire table
                System.out.println("Permit ID: " + resultSet.getInt("permitID"));
                System.out.println("Space Type: " + resultSet.getString("spaceType"));
                System.out.println("Start Date: " + resultSet.getString("startDate"));
                System.out.println("Expiration Date: " + resultSet.getString("expirationDate"));
                System.out.println("Permit Type: " + resultSet.getString("permitType"));
                System.out.println("Expiration Time: " + resultSet.getString("expirationTime"));
                System.out.println("Lot: " + resultSet.getInt("lot"));
                System.out.println("Zone ID: " + resultSet.getString("zoneID"));
                System.out.println("------------");
            }
        } catch (SQLException e) {
            handleSQLException(e);
        }
    }
    
    private static void checkPermitIDGetUserChoice(Statement statement) throws SQLException {
    	System.out.println("Do you have a specific Permit ID to check? (Y/N):");
        String choice = scan.next();

        if (choice.equalsIgnoreCase("Y")) {
            checkValidityOfSinglePermit(statement);
        } else if (choice.equalsIgnoreCase("N")) {
            retrieveAllValidPermits(statement);
        } else {
            System.out.println("Invalid choice. Please enter either Y or N.");
        }
    }

    private static void checkValidityOfSinglePermit(Statement statement) {
        System.out.println("Enter the permit ID to check its validity:");
        int permitID = scan.nextInt();

        boolean isValid = checkPermitValidity(statement, permitID);

        if (isValid) {
            System.out.println("The permit is valid.");
        } else {
            System.out.println("The permit is not valid.");
        }
    }

    private static void retrieveAllValidPermits(Statement statement) {
        String selectQuery = "SELECT permitID FROM Permit WHERE (expirationDate > CURDATE()) OR (expirationDate = CURDATE() AND expirationTime > CURTIME())";

        try (ResultSet resultSet = statement.executeQuery(selectQuery)) {
            System.out.println("Valid Permits:");
            while (resultSet.next()) {
                System.out.println("Permit ID: " + resultSet.getInt("permitID"));
            }
        } catch (SQLException e) {
            handleSQLException(e);
        }
    }

    private static boolean checkPermitValidity(Statement statement, int permitID) {
        String selectQuery = "SELECT permitID FROM Permit WHERE ((expirationDate > CURDATE()) OR (expirationDate = CURDATE() AND expirationTime > CURTIME())) AND permitID = " + permitID;

        try (ResultSet resultSet = statement.executeQuery(selectQuery)) {
            return resultSet.next(); // If there is a result, the permit is valid; otherwise, it's not valid.
        } catch (SQLException e) {
            handleSQLException(e);
            return false; // Return false in case of an exception
        }
    }
	
    private static void handleSQLException(SQLException e) {
        System.err.println("SQL Exception: " + e.getMessage());

        // Handle specific SQL exceptions
        if (e.getSQLState().equals("23000")) {
            // SQLState "23000" corresponds to a violation of a unique constraint (e.g., PRIMARY KEY or FOREIGN KEY)
            System.err.println("Error: Duplicate key or foreign key violation.");
        } else if (e.getSQLState().equals("23001")) {
            // SQLState "23001" corresponds to a violation of a CHECK constraint
            System.err.println("Error: Check constraint violation.");
        } else {
            // Display the SQL exception message for all other errors
            System.err.println("SQL State: " + e.getSQLState());
            System.err.println("Error Code: " + e.getErrorCode());
        }
    }
}

