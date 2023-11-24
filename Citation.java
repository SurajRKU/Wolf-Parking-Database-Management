package DBMS_Project_Demo;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class Citation {

    private static final Scanner scanner = new Scanner(System.in);

	    public static void citationOptions(Statement statement) throws SQLException {
	        try {
	            // Step 1: Load and register the JDBC driver
	            Class.forName("com.mysql.cj.jdbc.Driver");

	            int choice;
				do {
				// Provide options to the user
				System.out.println("Choose an option:");
				System.out.println("1. Insert Citation");
				System.out.println("2. Update Citation");
				System.out.println("3. Get Citation");
				System.out.println("4. Delete Citation");
				System.out.println("5. Generate a report for the total number of citations given in all zones in the lot for a given time range");
				System.out.println("6. Generate Citation Report");
				System.out.println("7. Get Citations for a particular driver");
				System.out.println("8. Generate Citation fee");
				System.out.println("9. Back to the main menu");

				// Get user choice
				choice = scanner.nextInt();
				scanner.nextLine(); // Consume the newline character

				// Perform the chosen action
				switch (choice) {
				    case 1:
				        insertCitation(statement);
				        break;
				    case 2:
				        updateCitation(statement);
				        break;
				    case 3:
				        getCitation(statement);
				        break;
				    case 4:
				        deleteCitation(statement);
				        break;
				    case 5:
				    	generateCitationReport(statement);
				    	break;
				    case 6:
				    	generateCitationReportForALot(statement);
				    	break;
				    case 7:
				    	retrieveCitationsForDriver(statement);
				    	break;
				    case 8:
				    	generateCitationFee(statement);
				    	break;
				    case 9:
				        System.out.println("Back to Home menu");
				        break;
				    default:
				        System.out.println("Invalid choice. Please enter a number between 1 and 4.");
				}
				}while(choice!=9);

	        } catch (ClassNotFoundException e) {
	            e.printStackTrace();
	        }
	    }

	    private static void insertCitation(Statement statement) {
	        try{
	            System.out.println("Enter Citation Number:");
	            int citationNum = scanner.nextInt();
	            scanner.nextLine(); // Consume the newline character

	            System.out.println("Enter Citation Time (HH:mm:ss):");
	            String citationTime = scanner.nextLine();

	            System.out.println("Enter Citation Date (YYYY-MM-DD):");
	            String citationDate = scanner.nextLine();

	            System.out.println("Enter Lot:");
	            int lot = scanner.nextInt();
	            scanner.nextLine(); // Consume the newline character

	            System.out.println("Enter Payment Status(Paid/Unpaid) :");
	            String paymentStatus = scanner.nextLine();

	            System.out.println("Enter Category (Invalid Permit,Expired Permit,No Permit) :");
	            String category = scanner.nextLine();

	            String insertQuery = "INSERT INTO Citation (citationNum, citationTime, citationDate, lot, paymentStatus, category) " +
	                    "VALUES (" + citationNum + ", '" + citationTime + "', '" + citationDate + "', " + lot + ", '" +
	                    paymentStatus + "', '" + category + "')";

	            int rowsAffected = statement.executeUpdate(insertQuery);

	            if (rowsAffected > 0) {
	                System.out.println("Citation information inserted successfully.");
	            } else {
	                System.out.println("Failed to insert citation information.");
	            }

	        } catch (SQLException e) {
	            handleSQLException(e);
	        }
	    }

	    private static void updateCitation(Statement statement) {
	        System.out.println("Enter Citation Number to update:");
			int citationNum = scanner.nextInt();
			scanner.nextLine(); // Consume the newline character

			// Display menu for update options
			System.out.println("Choose an option:");
			System.out.println("1. Update specific attribute");
			System.out.println("2. Update all information");

			// Get user choice
			int updateChoice = scanner.nextInt();
			scanner.nextLine(); // Consume the newline character

			switch (updateChoice) {
			    case 1:
			        updateSpecificAttribute(statement, citationNum);
			        break;
			    case 2:
			        updateAllInformation(statement, citationNum);
			        break;
			    default:
			        System.out.println("Invalid choice. No update performed.");
			}
	    }

	    private static void updateSpecificAttribute(Statement statement, int citationNum) {
	        // Display menu for attribute selection
	        System.out.println("Select attribute to update:");
	        System.out.println("1. Citation Time");
	        System.out.println("2. Citation Date");
	        System.out.println("3. Lot");
	        System.out.println("4. Payment Status");
	        System.out.println("5. Category");

	        // Get user choice
	        int choice = scanner.nextInt();
	        scanner.nextLine(); // Consume the newline character

	        // Get the new value from the user
	        System.out.println("Enter new value:");
	        String newValue = scanner.nextLine();

	        String attributeName;
	        switch (choice) {
	            case 1:
	                attributeName = "citationTime";
	                break;
	            case 2:
	                attributeName = "citationDate";
	                break;
	            case 3:
	                attributeName = "lot";
	                break;
	            case 4:
	                attributeName = "paymentStatus";
	                break;
	            case 5:
	                attributeName = "category";
	                break;
	            default:
	                System.out.println("Invalid choice. No attribute updated.");
	                return;
	        }

	        String updateQuery = "UPDATE Citation SET " + attributeName + " = '" + newValue + "' WHERE citationNum = " + citationNum;

	        int rowsAffected = executeUpdate(statement, updateQuery);
	        printUpdateResult(rowsAffected);

	    }

	    private static void updateAllInformation(Statement statement, int citationNum) {
	        System.out.println("Enter new Citation Time (HH:mm:ss):");
	        String newCitationTime = scanner.nextLine();

	        System.out.println("Enter new Citation Date (YYYY-MM-DD):");
	        String newCitationDate = scanner.nextLine();

	        System.out.println("Enter new Lot:");
	        int newLot = scanner.nextInt();
	        scanner.nextLine(); // Consume the newline character

	        System.out.println("Enter new Payment Status:");
	        String newPaymentStatus = scanner.nextLine();

	        System.out.println("Enter new Category:");
	        String newCategory = scanner.nextLine();

	        String updateQuery = "UPDATE Citation SET " +
	                "citationTime = '" + newCitationTime + "', " +
	                "citationDate = '" + newCitationDate + "', " +
	                "lot = " + newLot + ", " +
	                "paymentStatus = '" + newPaymentStatus + "', " +
	                "category = '" + newCategory + "' " +
	                "WHERE citationNum = " + citationNum;

	        int rowsAffected = executeUpdate(statement, updateQuery);
	        printUpdateResult(rowsAffected);
	    }

	    private static void getCitation(Statement statement) {
	        System.out.println("Choose an option:");
			System.out.println("1. Retrieve based on specific attribute");
			System.out.println("2. Retrieve entire Citation table");

			// Get user choice
			int getCitationChoice = scanner.nextInt();
			scanner.nextLine(); // Consume the newline character

			switch (getCitationChoice) {
			    case 1:
			        retrieveBasedOnAttribute(statement);
			        break;
			    case 2:
			        retrieveEntireTable(statement);
			        break;
			    default:
			        System.out.println("Invalid choice. No information retrieved.");
			}
	    }

	    private static void retrieveBasedOnAttribute(Statement statement) {
	        // Display menu for attribute selection
	        System.out.println("Select attribute to retrieve:");
	        System.out.println("1. Citation Number");
	        System.out.println("2. Citation Time");
	        System.out.println("3. Citation Date");
	        System.out.println("4. Lot");
	        System.out.println("5. Payment Status");
	        System.out.println("6. Category");

	        // Get user choice
	        int choice = scanner.nextInt();
	        scanner.nextLine(); // Consume the newline character

	        String attributeName;
	        switch (choice) {
	            case 1:
	                attributeName = "citationNum";
	                break;
	            case 2:
	                attributeName = "citationTime";
	                break;
	            case 3:
	                attributeName = "citationDate";
	                break;
	            case 4:
	                attributeName = "lot";
	                break;
	            case 5:
	                attributeName = "paymentStatus";
	                break;
	            case 6:
	                attributeName = "category";
	                break;
	            default:
	                System.out.println("Invalid choice. No information retrieved.");
	                return;
	        }

	        System.out.println("Enter the value for " + attributeName + ":");
	        String attributeValue = scanner.nextLine();

	        String selectQuery = "SELECT * FROM Citation WHERE " + attributeName + " = '" + attributeValue + "'";

	        try (ResultSet resultSet = statement.executeQuery(selectQuery)) {
	            while (resultSet.next()) {
	                // Process and print citation information
	                System.out.println("Citation Number: " + resultSet.getInt("citationNum"));
	                System.out.println("Citation Time: " + resultSet.getString("citationTime"));
	                System.out.println("Citation Date: " + resultSet.getString("citationDate"));
	                System.out.println("Lot: " + resultSet.getInt("lot"));
	                System.out.println("Payment Status: " + resultSet.getString("paymentStatus"));
	                System.out.println("Category: " + resultSet.getString("category"));
	                System.out.println("------------");
	            }
	        } catch (SQLException e) {
	            handleSQLException(e);
	        }
	    }

	    private static void retrieveEntireTable(Statement statement) {
	        String selectQuery = "SELECT * FROM Citation";

	        try (ResultSet resultSet = statement.executeQuery(selectQuery)) {
	            while (resultSet.next()) {
	                // Process and print citation information
	                System.out.println("Citation Number: " + resultSet.getInt("citationNum"));
	                System.out.println("Citation Time: " + resultSet.getString("citationTime"));
	                System.out.println("Citation Date: " + resultSet.getString("citationDate"));
	                System.out.println("Lot: " + resultSet.getInt("lot"));
	                System.out.println("Payment Status: " + resultSet.getString("paymentStatus"));
	                System.out.println("Category: " + resultSet.getString("category"));
	                System.out.println("------------");
	            }
	        } catch (SQLException e) {
	            handleSQLException(e);
	        }
	    }
	    
	    private static int executeUpdate(Statement statement, String query) {
	        try {
	            return statement.executeUpdate(query);
	        } catch (SQLException e) {
	            handleSQLException(e);
	            return 0;
	        }
	    }

	    private static void printUpdateResult(int rowsAffected) {
	        if (rowsAffected > 0) {
	            System.out.println("Citation information updated successfully.");
	        } else {
	            System.out.println("Citation not found for update.");
	        }
	    }



	    private static void deleteCitation(Statement statement) {
	        try{
	            System.out.println("Enter Citation Number to delete:");
	            int citationNum = scanner.nextInt();
	            scanner.nextLine(); // Consume the newline character

	            String deleteQuery = "DELETE FROM Citation WHERE citationNum = " + citationNum;
	            int rowsAffected = statement.executeUpdate(deleteQuery);

	            if (rowsAffected > 0) {
	                System.out.println("Citation information deleted successfully.");
	            } else {
	                System.out.println("Citation not found for deletion.");
	            }

	        } catch (SQLException e) {
	            handleSQLException(e);
	        }
	    }
	    
	    private static void generateCitationReportForALot(Statement statement) {
	        try{
	            System.out.println("Enter Lot:");
	            int lot = scanner.nextInt();
	            scanner.nextLine(); // Consume the newline character

	            System.out.println("Enter Start Date (YYYY-MM-DD):");
	            String startDate = scanner.nextLine();

	            System.out.println("Enter End Date (YYYY-MM-DD):");
	            String endDate = scanner.nextLine();

	            String selectQuery = "SELECT * FROM Citation WHERE lot = " + lot +
	                    " AND citationDate BETWEEN '" + startDate + "' AND '" + endDate + "'";

	            try (ResultSet resultSet = statement.executeQuery(selectQuery)) {
	                while (resultSet.next()) {
	                    // Process and print citation information
	                    System.out.println("Citation Number: " + resultSet.getInt("citationNum"));
	                    System.out.println("Citation Time: " + resultSet.getString("citationTime"));
	                    System.out.println("Citation Date: " + resultSet.getString("citationDate"));
	                    System.out.println("Lot: " + resultSet.getInt("lot"));
	                    System.out.println("Payment Status: " + resultSet.getString("paymentStatus"));
	                    System.out.println("Category: " + resultSet.getString("category"));
	                    System.out.println("------------");
	                }
	            }

	        } catch (SQLException e) {
	            handleSQLException(e);
	        }
	    }
	    
	    private static void retrieveCitationsForDriver(Statement statement) throws SQLException {
	        System.out.print("Enter Driver ID: ");
	        long driverID = scanner.nextLong();

	        // SQL query to retrieve citations for a specific driver
	        String sqlQuery = "SELECT R.citationNum FROM RaisedTo R JOIN Has H on R.licenseNum = H.licenseNum AND H.driverID = "+ driverID;

	       try (ResultSet resultSet = statement.executeQuery(sqlQuery)) {
	                if (!resultSet.isBeforeFirst()) {
	                    System.out.println("No citations found for the specified driver.");
	                } else {
	                    System.out.println("Citations for Driver ID " + driverID + ":");
	                    while (resultSet.next()) {
	                        System.out.println("Citation Number: " + resultSet.getInt("citationNum"));
	                        System.out.println("--------------------------------------");
	                    }
	                }
	            }
	    }
	    
	    private static void generateCitationReport(Statement statement) throws SQLException {
	        System.out.print("Enter Start Date (YYYY-MM-DD): ");
	        String startDate = scanner.nextLine();

	        System.out.print("Enter End Date (YYYY-MM-DD): ");
	        String endDate = scanner.nextLine();

	        // SQL query to count the number of citations in the given time range
	        String sqlQuery = "SELECT l.lotID AS LotID,\r\n"
	        		+ "COUNT(c.citationNum) AS TotalCitations\r\n"
	        		+ "FROM ParkingLot l\r\n"
	        		+ "JOIN Citation c ON l.lotID = c.lot\r\n"
	        		+ "WHERE c.citationDate BETWEEN '"
	        		+ startDate
	        		+ "' AND '"
	        		+ endDate
	        		+ "'GROUP BY l.lotID\r\n"
	        		+ "ORDER BY l.lotID";

	        try (ResultSet resultSet = statement.executeQuery(sqlQuery)) {
	            	System.out.println("Total Citations in a particular Parking Lot between " + startDate + " and " + endDate + ": ");
	            	System.out.println("Lot ID : Total Citations"); 
	                while (resultSet.next()) {
	                    int totalCitations = resultSet.getInt("totalCitations");
	                    int lotID = resultSet.getInt("LotID");
	                   System.out.println(lotID + ":" + totalCitations);
	                } 
	            }
	        }


	    public static void generateCitationFee(Statement statement) {
	        try {
	            String sqlQuery = "SELECT "
	                    + "c.citationNum, "
	                    + "c.citationDate, "
	                    + "c.citationTime, "
	                    + "c.lot, "
	                    + "c.paymentStatus, "
	                    + "c.category, "
	                    + "d.driverID, "
	                    + "cf.fee AS actualFee, "
	                    + "CASE "
	                    + "    WHEN d.isHandicapped = 'Yes' THEN cf.fee * 0.5 "
	                    + "    ELSE cf.fee "
	                    + "END AS discountedFee "
	                    + "FROM Citation c "
	                    + "JOIN CitationFee cf ON c.category = cf.category "
	                    + "LEFT JOIN Appeals a ON c.citationNum = a.citationNum "
	                    + "LEFT JOIN Driver d ON a.driverID = d.driverID "
	                    + "WHERE c.paymentStatus = 'Unpaid'";

	            try (ResultSet resultSet = statement.executeQuery(sqlQuery)) {
	                while (resultSet.next()) {
	                	int citationNum = resultSet.getInt("citationNum");
	                    String citationDate = resultSet.getString("citationDate");
	                    String citationTime = resultSet.getString("citationTime");
	                    int lot = resultSet.getInt("lot");
	                    String paymentStatus = resultSet.getString("paymentStatus");
	                    String category = resultSet.getString("category");
	                    long driverID = resultSet.getLong("driverID");
	                    int actualFee = resultSet.getInt("actualFee");
	                    int discountedFee = resultSet.getInt("discountedFee");

	                    // Print or process the values
	                    System.out.println("CitationNum: " + citationNum);
	                    System.out.println("CitationDate: " + citationDate);
	                    System.out.println("CitationTime: " + citationTime);
	                    System.out.println("Lot: " + lot);
	                    System.out.println("PaymentStatus: " + paymentStatus);
	                    System.out.println("Category: " + category);
	                    System.out.println("DriverID: " + driverID);
	                    System.out.println("ActualFee: " + actualFee);
	                    System.out.println("DiscountedFee: " + discountedFee);
	                    System.out.println("---------------------------------");
	                }
	            }

	        } catch (SQLException e) {
	            handleSQLException(e);
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
