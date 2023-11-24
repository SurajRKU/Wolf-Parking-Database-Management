package DBMS_Project_Demo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Admin {

    // Database connection details
    private static final String JDBC_URL = "jdbc:mariadb://classdb2.csc.ncsu.edu:3306/sraghuk";
    private static final String USER = "sraghuk";
    private static final String PASSWORD = "200534075";

    // Method to display and handle Administrator options
    public static void adminOptions() {
        try {
            Class.forName("org.mariadb.jdbc.Driver");

            int option;
            // Main menu for Administrator functions
            do {
                displayMainMenu();
                Scanner scanner = new Scanner(System.in);
                option = scanner.nextInt();

                switch (option) {
                    case 1:
                        addParkingLot();
                        break;
                    case 2:
                        assignZonesAndSpaces();
                        break;
                    case 3:
                        assignPermitToDriver();
                        break;
                    case 4:
                        changeSpaceAvailability();
                        break;
                    case 5:
                        System.out.println("Back to the main menu.");
                        break;
                    default:
                        System.out.println("Invalid option. Please choose a valid option.");
                        break;
                }
            } while (option != 5);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    // Method to display the main menu for Administrator functions
    private static void displayMainMenu() {
        System.out.println("Administrator Functions Menu:");
        System.out.println("1) Add Parking Lot to the System");
        System.out.println("2) Assign Zones and Spaces to Lots");
        System.out.println("3) Assign Permit to Driver");
        System.out.println("4) Change Space Availability");
        System.out.println("5) Exit");
        System.out.print("Select an option: ");
    }

    // Method to add a parking lot to the system
    private static void addParkingLot() throws SQLException {
        Connection connection = null;
        Scanner scanner = new Scanner(System.in);

        // Disable autocommit to start a transaction
	    //connection.setAutoCommit(false);

        System.out.print("Enter Lot ID: ");
        int lotID = scanner.nextInt();
        
        scanner.nextLine();

        System.out.print("Enter Lot Name: ");
        String lotName = scanner.nextLine();

        System.out.print("Enter Lot Address: ");
        String lotAddress = scanner.nextLine();

        // SQL query to add a parking lot to the system
        String sqlQuery = "INSERT INTO ParkingLot (lotID, name, address) VALUES (?, ?, ?)";

	    try { 
            connection = DriverManager.getConnection(JDBC_URL, USER, PASSWORD);
            PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);

            preparedStatement.setInt(1, lotID);
            preparedStatement.setString(2, lotName);
            preparedStatement.setString(3, lotAddress);

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Parking Lot added successfully!"); 
            } else {
                System.out.println("Failed to add the parking lot.");
            }
            //connection.commit();
        } catch (SQLException e) {
		            // Rollback the transaction in case of exception
		            try {
		                if (connection != null) {
		                    connection.rollback();
		                }
		            } catch (SQLException rollbackException) {
		                //rollbackException.printStackTrace();
		            }
		    
		            e.printStackTrace();
	    } finally {

	            if (connection != null) {
	                try {
	                    // Restore autocommit to true and close the connection
	                    //connection.setAutoCommit(true);
	                    connection.close();
	                    System.out.println("Connection closed");
	                } catch (SQLException e) {
	                    e.printStackTrace();
	                }
	            }
	        }
    }

    // Method to assign zones and spaces to parking lots
    private static void assignZonesAndSpaces() throws SQLException {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter Lot ID to assign zones and spaces: ");
        int lotID = scanner.nextInt();

        // Check if the lot exists
        if (!lotExists(lotID)) {
            System.out.println("Parking Lot with ID " + lotID + " does not exist.");
            return;
        }

        System.out.print("Enter Zone ID: ");
        String zoneID = scanner.next();

        System.out.print("Enter Zone Name: ");
        String zoneName = scanner.next();

        // SQL query to assign zones and spaces to lots
        String sqlQuery = "INSERT INTO Zone (zoneID, zone_name, lotID) VALUES (?, ?, ?)";

        Connection connection = null;
        try {
            connection = DriverManager.getConnection(JDBC_URL, USER, PASSWORD);
            PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);

            preparedStatement.setString(1, zoneID);
            preparedStatement.setString(2, zoneName);
            preparedStatement.setInt(3, lotID);

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Zone assigned to Parking Lot successfully!");
            } else {
                System.out.println("Failed to assign zone to the parking lot.");
            }
        } finally {
            // Close the connection in the finally block to ensure it is always closed
            try {
                if (connection != null && !connection.isClosed()) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    // Method to assign a permit to a driver
    private static void assignPermitToDriver() throws SQLException {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter Driver ID to assign a permit: ");
        long driverID = scanner.nextLong();

        // Check if the driver exists
        if (!driverExists(driverID)) {
            System.out.println("Driver with ID " + driverID + " does not exist.");
            return;
        }

        System.out.print("Enter Permit ID: ");
        int permitID = scanner.nextInt();

        // SQL query to assign a permit to a driver
        String sqlQuery = "INSERT INTO AllottedTo (permitID, driverID) VALUES (?, ?)";

        Connection connection = null;
        try {
            connection = DriverManager.getConnection(JDBC_URL, USER, PASSWORD);
            PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);

            preparedStatement.setInt(1, permitID);
            preparedStatement.setLong(2, driverID);

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Permit assigned to Driver successfully!");
            } else {
                System.out.println("Failed to assign permit to the driver.");
            }
        } finally {
            // Close the connection in the finally block to ensure it is always closed
            try {
                if (connection != null && !connection.isClosed()) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    // Method to change space availability status
    private static void changeSpaceAvailability() throws SQLException {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter Lot ID where space availability needs to be changed: ");
        int lotID = scanner.nextInt();

        // Check if the lot exists
        if (!lotExists(lotID)) {
            System.out.println("Parking Lot with ID " + lotID + " does not exist.");
            return;
        }

        System.out.print("Enter Space Number: ");
        int spaceNum = scanner.nextInt();

        System.out.print("Enter New Availability Status (e.g., 'Available' or 'Occupied'): ");
        String newAvailabilityStatus = scanner.next();

        // SQL query to change space availability
        String sqlQuery = "UPDATE Space SET availabilityStatus = ? WHERE lotID = ? AND spaceNum = ?";

        Connection connection = null;
        try {
            connection = DriverManager.getConnection(JDBC_URL, USER, PASSWORD);
            PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);

            preparedStatement.setString(1, newAvailabilityStatus);
            preparedStatement.setInt(2, lotID);
            preparedStatement.setInt(3, spaceNum);

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Space availability status changed successfully!");
            } else {
                System.out.println("Failed to change space availability status.");
            }
        } finally {
            // Close the connection in the finally block to ensure it is always closed
            try {
                if (connection != null && !connection.isClosed()) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    // Method to check if a parking lot exists
    private static boolean lotExists(int lotID) throws SQLException {
        String sqlQuery = "SELECT * FROM ParkingLot WHERE lotID = ?";
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(JDBC_URL, USER, PASSWORD);
            PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);

            preparedStatement.setInt(1, lotID);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                return resultSet.next(); // If there is at least one result, the lot exists
            }
        } finally {
            // Close the connection in the finally block to ensure it is always closed
            try {
                if (connection != null && !connection.isClosed()) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    // Method to check if a driver exists
    private static boolean driverExists(long driverID) throws SQLException {
        String sqlQuery = "SELECT * FROM Driver WHERE driverID = ?";
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(JDBC_URL, USER, PASSWORD);
            PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);

            preparedStatement.setLong(1, driverID);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                return resultSet.next(); // If there is at least one result, the driver exists
            }
        } finally {
            // Close the connection in the finally block to ensure it is always closed
            try {
                if (connection != null && !connection.isClosed()) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

}
