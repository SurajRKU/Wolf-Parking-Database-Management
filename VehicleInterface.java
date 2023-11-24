package DBMS_Project_Demo;

import java.util.*;
import java.sql.*;

public class VehicleInterface {

	    // Database connection details
	    private static final String JDBC_URL = "jdbc:mariadb://classdb2.csc.ncsu.edu:3306/sraghuk";
	    private static final String USER = "sraghuk";
	    private static final String PASSWORD = "200534075";

	    public static void vehicleOptions() {
	        try {
	            Class.forName("org.mariadb.jdbc.Driver");
	            int option;
	            // Main menu for Vehicle management
	           do {
	                displayVehicleMenu();
	                Scanner scanner = new Scanner(System.in);
	                option = scanner.nextInt();

	                switch (option) {
	                    case 1:
	                        addVehicle();
	                        break;
	                    case 2:
	                        displayAllVehicles();
	                        break;
	                    case 3:
	                        updateVehicle();
	                        break;
	                    case 4:
	                        deleteVehicle();
	                        break;
	                    case 5:
	                        addVehicleOwner();
	                        break;
	                    case 6:
	                    	displayVehicleOwnersByDriverID();
	                        break;
	                    case 7:
	                    	displayVehicleOwnersByLicenseNumber();
	                        break;
	                    case 8:
	                        deleteVehicleOwner();
	                        break;
	                    case 9:
	                        System.out.println("Back to main Menu.");
	                        break;
	                    default:
	                        System.out.println("Invalid option. Please choose a valid option.");
	                        break;
	                }
	            }while(option!=9);
	        } catch (ClassNotFoundException | SQLException e) {  
	            e.printStackTrace();
	        }
	    }

	    private static void displayVehicleMenu() {
	        System.out.println("Vehicle Management Menu:");
	        System.out.println("1) Add Vehicle");
	        System.out.println("2) Display All Vehicles");
	        System.out.println("3) Update Vehicle Information");
	        System.out.println("4) Delete Vehicle Information");
	        System.out.println("5) Add Vehicle Owner");
	        System.out.println("6) Display Vehicle Owners By Driver ID");
	        System.out.println("7) Display Vehicle Owners By License Number");
	        System.out.println("8) Delete Vehicle Owner");
	        System.out.println("9) Exit");
	        System.out.print("Select an option: ");
	    }

	    private static void addVehicle() throws SQLException {
	    	Connection connection = null;
	        try { 
	        	connection = DriverManager.getConnection(JDBC_URL, USER, PASSWORD);
	            Scanner scanner = new Scanner(System.in);

	         // Disable autocommit to start a transaction
	            connection.setAutoCommit(false);
	            System.out.print("Enter License Number: ");
	            String licenseNum = scanner.nextLine();

	            System.out.print("Enter Manufacturer: ");
	            String manufacturer = scanner.nextLine();

	            System.out.print("Enter Color: ");
	            String color = scanner.nextLine();

	            System.out.print("Enter Model: ");
	            String model = scanner.nextLine();

	            System.out.print("Enter Year: ");
	            int year = scanner.nextInt();

	            // Insert new vehicle into the Vehicle table
	            String insertQuery = "INSERT INTO Vehicle (licenseNum, manufacturer, color, model, year) VALUES (?, ?, ?, ?, ?)";
	            PreparedStatement preparedStatement = connection.prepareStatement(insertQuery);
	                preparedStatement.setString(1, licenseNum);
	                preparedStatement.setString(2, manufacturer);
	                preparedStatement.setString(3, color);
	                preparedStatement.setString(4, model); 
	                preparedStatement.setInt(5, year);

	                int rowsAffected = preparedStatement.executeUpdate();
	                if (rowsAffected > 0) {
	                    System.out.println("Vehicle added successfully!");
	                } else {
	                    System.out.println("Failed to add the vehicle.");
	                }
	                // Commit the transaction
	                connection.commit();
	            }
	            catch (SQLException e) {
		            // Rollback the transaction in case of exception
		            try {
		                if (connection != null) {
		                    connection.rollback();
		                }
		            } catch (SQLException rollbackException) {
		                rollbackException.printStackTrace();
		            }
		    
		            e.printStackTrace();
	        } finally {
	    
	            if (connection != null) {
	                try {
	                    // Restore autocommit to true and close the connection
	                    connection.setAutoCommit(true);
	                    connection.close();
	                    System.out.println("Connection closed");
	                } catch (SQLException e) {
	                    e.printStackTrace();
	                }
	            }
	        }
	   }

	    private static void displayAllVehicles() throws SQLException {
	    	Connection connection = null;
	        try { 
	        	connection = DriverManager.getConnection(JDBC_URL, USER, PASSWORD);
	            Statement statement = connection.createStatement();
	            String selectQuery = "SELECT * FROM Vehicle";
	            try (ResultSet resultSet = statement.executeQuery(selectQuery)) {
	                while (resultSet.next()) {
	                    System.out.println("License Number: " + resultSet.getString("licenseNum"));
	                    System.out.println("Manufacturer: " + resultSet.getString("manufacturer"));
	                    System.out.println("Color: " + resultSet.getString("color"));
	                    System.out.println("Model: " + resultSet.getString("model"));
	                    System.out.println("Year: " + resultSet.getInt("year"));
	                    System.out.println("--------------------------------------");  
	                }
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

	    private static void updateVehicle() throws SQLException {
	    	Connection connection = null;
	        try { 
	        	connection = DriverManager.getConnection(JDBC_URL, USER, PASSWORD);
	            Scanner scanner = new Scanner(System.in);

	            System.out.print("Enter License Number of the Vehicle to Update: ");
	            String licenseNum = scanner.nextLine();

	            // Check if the vehicle exists
	            String checkQuery = "SELECT * FROM Vehicle WHERE licenseNum = ?";
	            try (PreparedStatement checkStatement = connection.prepareStatement(checkQuery)) {
	                checkStatement.setString(1, licenseNum);
	                try (ResultSet resultSet = checkStatement.executeQuery()) {
	                    if (!resultSet.next()) {
	                        System.out.println("Vehicle with License Number " + licenseNum + " not found.");
	                        return;
	                    }
	                }
	            }

	            System.out.print("Enter New Manufacturer (Press Enter to skip): ");
	            String newManufacturer = scanner.nextLine();

	            System.out.print("Enter New Color (Press Enter to skip): ");
	            String newColor = scanner.nextLine();

	            System.out.print("Enter New Model (Press Enter to skip): ");
	            String newModel = scanner.nextLine();

	            System.out.print("Enter New Year (Press Enter to skip): ");
	            String newYearInput = scanner.nextLine();
	            int newYear = newYearInput.isEmpty() ? -1 : Integer.parseInt(newYearInput);

	            // Update the vehicle information
	            String updateQuery = "UPDATE Vehicle SET manufacturer = COALESCE(?, manufacturer), color = COALESCE(?, color), model = COALESCE(?, model), year = CASE WHEN ? > 0 THEN ? ELSE year END WHERE licenseNum = ?";
	            try (PreparedStatement updateStatement = connection.prepareStatement(updateQuery)) {
	                updateStatement.setString(1, newManufacturer);
	                updateStatement.setString(2, newColor);
	                updateStatement.setString(3, newModel);
	                updateStatement.setInt(4, newYear);
	                updateStatement.setInt(5, newYear);
	                updateStatement.setString(6, licenseNum);

	                int rowsAffected = updateStatement.executeUpdate();
	                if (rowsAffected > 0) {
	                    System.out.println("Vehicle information updated successfully!");
	                } else {
	                    System.out.println("Failed to update the vehicle information.");
	                }
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

	    private static void deleteVehicle() throws SQLException {
	    	Connection connection = null;
	        try { 
	        	connection = DriverManager.getConnection(JDBC_URL, USER, PASSWORD);
	            Scanner scanner = new Scanner(System.in);

	            System.out.print("Enter License Number of the Vehicle to Delete: ");
	            String licenseNum = scanner.nextLine();

	            // Check if the vehicle exists
	            String checkQuery = "SELECT * FROM Vehicle WHERE licenseNum = ?";
	            try (PreparedStatement checkStatement = connection.prepareStatement(checkQuery)) {
	                checkStatement.setString(1, licenseNum);
	                try (ResultSet resultSet = checkStatement.executeQuery()) {
	                    if (!resultSet.next()) {
	                        System.out.println("Vehicle with License Number " + licenseNum + " not found.");
	                        return;
	                    }
	                }
	            }

	            // Delete the vehicle
	            String deleteQuery = "DELETE FROM Vehicle WHERE licenseNum = ?";
	            try (PreparedStatement deleteStatement = connection.prepareStatement(deleteQuery)) {
	                deleteStatement.setString(1, licenseNum);

	                int rowsAffected = deleteStatement.executeUpdate();
	                if (rowsAffected > 0) {
	                    System.out.println("Vehicle deleted successfully!");
	                } else {
	                    System.out.println("Failed to delete the vehicle.");
	                }
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
	    
	    private static void addVehicleOwner() throws SQLException {
	    	Connection connection = null;
	        try { 
	        	connection = DriverManager.getConnection(JDBC_URL, USER, PASSWORD);
	            Scanner scanner = new Scanner(System.in);

	            System.out.print("Enter Driver ID: ");
	            long driverID = scanner.nextLong();

	            System.out.print("Enter License Number: ");
	            String licenseNum = scanner.next();

	            // Insert new relationship into the Has table
	            String insertQuery = "INSERT INTO Has (driverID, licenseNum) VALUES (?, ?)";
	            try (PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {
	                preparedStatement.setLong(1, driverID);
	                preparedStatement.setString(2, licenseNum);

	                int rowsAffected = preparedStatement.executeUpdate();
	                if (rowsAffected > 0) {
	                    System.out.println("Vehicle owner relationship added successfully!");
	                } else {
	                    System.out.println("Failed to add the vehicle owner relationship.");
	                }
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

	    private static void displayVehicleOwnersByDriverID() throws SQLException {
	        Connection connection = null;

	        try {
	            connection = DriverManager.getConnection(JDBC_URL, USER, PASSWORD);
	            Scanner scanner = new Scanner(System.in);

	            System.out.print("Enter Driver ID to display Vehicle Owners: ");
	            long driverID = scanner.nextLong();

	            // Display the vehicle owners by driver ID
	            String query = "SELECT * FROM Has WHERE driverID = ?";
	            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
	                preparedStatement.setLong(1, driverID);

	                try (ResultSet resultSet = preparedStatement.executeQuery()) {
	                    while (resultSet.next()) {
	                        int displayedDriverID = resultSet.getInt("driverID");
	                        String licenseNum = resultSet.getString("licenseNum");

	                        System.out.println("Driver ID: " + displayedDriverID + ", License Number: " + licenseNum);
	                    }
	                }
	            }
	        } finally {
	            try {
	                if (connection != null && !connection.isClosed()) {
	                    connection.close();
	                }
	            } catch (SQLException e) {
	                e.printStackTrace();
	            }
	        }
	    }

	    private static void displayVehicleOwnersByLicenseNumber() throws SQLException {
	        Connection connection = null;

	        try {
	            connection = DriverManager.getConnection(JDBC_URL, USER, PASSWORD);
	            Scanner scanner = new Scanner(System.in);

	            System.out.print("Enter License Number to display Vehicle Owners: ");
	            String licenseNum = scanner.next();

	            // Display the vehicle owners by license number
	            String query = "SELECT * FROM Has WHERE licenseNum = ?";
	            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
	                preparedStatement.setString(1, licenseNum);

	                try (ResultSet resultSet = preparedStatement.executeQuery()) {
	                    while (resultSet.next()) {
	                        long driverID = resultSet.getLong("driverID");
	                        String displayedLicenseNum = resultSet.getString("licenseNum");

	                        System.out.println("Driver ID: " + driverID + ", License Number: " + displayedLicenseNum);
	                    }
	                }
	            }
	        } finally {
	            try {
	                if (connection != null && !connection.isClosed()) {
	                    connection.close();
	                }
	            } catch (SQLException e) {
	                e.printStackTrace();
	            }
	        }
	    }


	    private static void deleteVehicleOwner() throws SQLException {
	    	Connection connection = null;
	        try { 
	        	connection = DriverManager.getConnection(JDBC_URL, USER, PASSWORD);
	            Scanner scanner = new Scanner(System.in);

	            System.out.print("Enter Driver ID to delete the Vehicle Owner relationship: ");
	            long driverID = scanner.nextLong();

	            System.out.print("Enter License Number to delete the Vehicle Owner relationship: ");
	            String licenseNum = scanner.next();

	            // Delete the relationship from the Has table
	            String deleteQuery = "DELETE FROM Has WHERE driverID = ? AND licenseNum = ?";
	            try (PreparedStatement deleteStatement = connection.prepareStatement(deleteQuery)) {
	                deleteStatement.setLong(1, driverID);
	                deleteStatement.setString(2, licenseNum);

	                int rowsAffected = deleteStatement.executeUpdate();
	                if (rowsAffected > 0) {
	                    System.out.println("Vehicle owner relationship deleted successfully!");
	                } else {
	                    System.out.println("Failed to delete the vehicle owner relationship.");
	                }
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


