package DBMS_Project_Demo;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class Security {

    private static final Scanner scanner = new Scanner(System.in);

    public static void securityOptions(Statement statement) {
        try {
            // Step 1: Load and register the JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            int choice;
			do {
			// Provide options to the user
			System.out.println("Choose an option:");
			System.out.println("1. Insert Security");
			System.out.println("2. Update Security");
			System.out.println("3. Get Security");
			System.out.println("4. Delete Security");
			System.out.println("5. Update payment status(Paid/Unpaid) for a specific citation");
			System.out.println("6. Generate Citation");
			System.out.println("7. Back to the main menu");

			// Get user choice
			choice = scanner.nextInt();
			scanner.nextLine(); // Consume the newline character

			// Perform the chosen action
			switch (choice) {
			    case 1:
			        insertSecurity(statement);
			        break;
			    case 2:
			        updateSecurity(statement);
			        break;
			    case 3:
			        getSecurity(statement);
			        break;
			    case 4:
			        deleteSecurity(statement);
			        break;
			    case 5:
			    	updatePaymentStatus(statement);
			    	break;
			    case 6:
			    	generateCitation(statement);
			        break;
			    case 7:
			        System.out.println("Back to Home menu");
			        break;
			    default:
			        System.out.println("Invalid choice. Please enter a number between 1 and 4.");
			}
         } while (choice != 7);

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static void insertSecurity(Statement statement) {
        try{
            System.out.println("Enter Security ID to insert:");
            int securityID = scanner.nextInt();
            scanner.nextLine(); // Consume the newline character

            String insertQuery = "INSERT INTO Security (securityID) VALUES (" + securityID + ")";
            int rowsAffected = statement.executeUpdate(insertQuery);

            if (rowsAffected > 0) {
                System.out.println("Security information inserted successfully.");
            } else {
                System.out.println("Failed to insert security information.");
            }

        } catch (SQLException e) {
            handleSQLException(e);
        }
    }

    private static void updateSecurity(Statement statement) {
        try{
            System.out.println("Enter Security ID to update:");
            int securityID = scanner.nextInt();
            scanner.nextLine(); // Consume the newline character

            System.out.println("Enter new Security ID:");
            int newSecurityID = scanner.nextInt();
            scanner.nextLine(); // Consume the newline character

            String updateQuery = "UPDATE Security SET securityID = " + newSecurityID + " WHERE securityID = " + securityID;
            int rowsAffected = statement.executeUpdate(updateQuery);

            if (rowsAffected > 0) {
                System.out.println("Security information updated successfully.");
            } else {
                System.out.println("Security not found for update.");
            }

        } catch (SQLException e) {
            handleSQLException(e);
        }
    }

    private static void getSecurity(Statement statement) {
        try{
            String selectQuery = "SELECT * FROM Security";

            try (ResultSet resultSet = statement.executeQuery(selectQuery)) {
                while (resultSet.next()) {
                    // Process and print security information
                    System.out.println("Security ID: " + resultSet.getInt("securityID"));
                    System.out.println("------------");
                }
            }

        } catch (SQLException e) {
            handleSQLException(e);
        }
    }

    private static void deleteSecurity(Statement statement) {
        try{
            System.out.println("Enter Security ID to delete:");
            int securityID = scanner.nextInt();
            scanner.nextLine(); // Consume the newline character

            String deleteQuery = "DELETE FROM Security WHERE securityID = " + securityID;
            int rowsAffected = statement.executeUpdate(deleteQuery);

            if (rowsAffected > 0) {
                System.out.println("Security information deleted successfully.");
            } else {
                System.out.println("Security not found for deletion.");
            }

        } catch (SQLException e) {
            handleSQLException(e);
        }
    }
    
    private static void updatePaymentStatus(Statement statement) {
        try {
            System.out.println("Enter Citation Number to update payment status:");
            int citationNum = scanner.nextInt();
            scanner.nextLine(); // Consume the newline character

            System.out.println("Enter new Payment Status:");
            String newPaymentStatus = scanner.nextLine();

            String updateQuery = "UPDATE Citation SET paymentStatus = '" + newPaymentStatus + "' WHERE citationNum = " + citationNum;
            int rowsAffected = statement.executeUpdate(updateQuery);

            if (rowsAffected > 0) {
                System.out.println("Payment status updated successfully.");
            } else {
                System.out.println("Citation not found for payment status update.");
            }

        } catch (SQLException e) {
            handleSQLException(e);
        }
    }
    
    private static void generateCitation(Statement statement) {
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

            System.out.println("Enter Payment Status:");
            String paymentStatus = scanner.nextLine();

            System.out.println("Enter Category (Invalid Permit,Expired Permit,No Permit) :");
            String category = scanner.nextLine();

            String insertQuery = "INSERT INTO Citation (citationNum, citationTime, citationDate, lot, paymentStatus, category) " +
                    "VALUES (" + citationNum + ", '" + citationTime + "', '" + citationDate + "', " + lot + ", '" +
                    paymentStatus + "', '" + category + "')";

            int rowsAffected = statement.executeUpdate(insertQuery);

            if (rowsAffected > 0) {
                System.out.println("Citation generated successfully.");
            } else {
                System.out.println("Failed to generate citation.");
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
