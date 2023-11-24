package DBMS_Project_Demo;

import java.util.*;
import java.sql.*;

public class LotZoneSpace {

	public static void parkingOptions() throws ClassNotFoundException {
		Connection conn = null;
        String jdbcURL = "jdbc:mariadb://classdb2.csc.ncsu.edu:3306/sraghuk";

        try {
            Class.forName("org.mariadb.jdbc.Driver");

            String user = "sraghuk";
            String passwd = "200534075";

            Statement stmt = null;
            ResultSet rs = null;

            try {
                conn = DriverManager.getConnection(jdbcURL, user, passwd);
                stmt = conn.createStatement();

                Scanner scanner = new Scanner(System.in);
                int option;

                do {
                    System.out.println("\nMenu:");
                    System.out.println("1. Parking Lot");
                    System.out.println("2. Zone");
                    System.out.println("3. Space");
                    System.out.println("4. Detect Parking Violation");
                    System.out.println("5. Number of drivers having permits for a given parking zone");
                    System.out.println("6. Available space numbers for a given space type in a parking lot");
                    System.out.println("7. The list of zones for each lot");
                    System.out.println("9. Detect Parking Violation Cars Count");
                    System.out.println("8. Exit");
                    System.out.print("Choose an option: ");

                    option = scanner.nextInt();

                    switch (option) {
                        case 1:
                            handleParkingLotMenu(stmt);
                            break;
                        case 2:
                            handleZoneMenu(stmt);
                            break;
                        case 3:
                            handleSpaceMenu(stmt);
                            break;
                        case 4:
                        	displayViolationCars(stmt);
                        	break;
                        case 5:
                        	getNumberOfEmployeesWithPermitsForZone(stmt, scanner);
                            break;
                        case 6:
                        	getAvailableSpaces(stmt, scanner);
                            break;
                        case 7:
                        	getListOfZonesForEachLot(stmt);
                            break;
                            
                        case 9:
                        	displayViolationCarsCount(stmt);
                        	break;
                        case 8:
                            System.out.println("Back to main menu.");
                            break;
                        default:
                            System.out.println("Invalid option. Please try again.");
                    }
                }while(option!=8);
            } catch (SQLException e) {
                e.printStackTrace();
            } 
        } finally {
            // Close the connection in the finally block to ensure it is always closed
            try {
                if (conn != null && !conn.isClosed()) {
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } 
    }

    private static void handleParkingLotMenu(Statement stmt) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\nParking Lot Menu:");
            System.out.println("1. Display Parking Lot Table");
            System.out.println("2. Add Lot information");
            System.out.println("3. Delete Lot information");
            System.out.println("4. Update Lot information");
            System.out.println("5. Get Lot information");
            System.out.println("6. Back to Main Menu");
            System.out.print("Choose an option: \n");

            int lotOption = scanner.nextInt();

            switch (lotOption) {
                case 1:
                	displayEntireParkingLotTable(stmt);
                	break;
                case 2:
                    addParkingLotInfo(stmt, scanner);
                    break;
                case 3:
                    deleteParkingLotInfo(stmt, scanner);
                    break;
                case 4:
                    updateParkingLotInfo(stmt, scanner);
                    break;
                case 5:
                    getParkingLotInfo(stmt, scanner);
                    break;
                case 6:
                    System.out.println("Returning to Main Menu...");
                    return; // Exit the method to go back to the main menu
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }
    
    private static void displayEntireParkingLotTable(Statement stmt) {
        try {
            String query = "SELECT * FROM ParkingLot;";
            ResultSet resultSet = stmt.executeQuery(query);
            ResultSetMetaData metaData = resultSet.getMetaData();

            int columnCount = metaData.getColumnCount();

            while (resultSet.next()) {
                for (int i = 1; i <= columnCount; i++) {
                    System.out.print(metaData.getColumnName(i) + ": " + resultSet.getString(i) + " ");
                }
                System.out.println();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    

    private static void addParkingLotInfo(Statement stmt, Scanner scanner) {
        try {
            System.out.print("Enter lotID: ");
            int lotID = scanner.nextInt();

            System.out.print("Enter address: ");
            scanner.nextLine(); // Consume newline left by nextInt()
            String address = scanner.nextLine();

            System.out.print("Enter name: ");
            String name = scanner.nextLine();

            String query = String.format(
                    "INSERT INTO ParkingLot (lotID, address, name) VALUES (%d, '%s', '%s');",
                    lotID, address, name);

            stmt.executeUpdate(query);

            System.out.println("Parking Lot information added successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void deleteParkingLotInfo(Statement stmt, Scanner scanner) {
        try {
            System.out.print("Enter lotID to delete: ");
            int lotID = scanner.nextInt();

            String query = String.format("DELETE FROM ParkingLot WHERE lotID = %d;", lotID);

            int rowsAffected = stmt.executeUpdate(query);

            if (rowsAffected > 0) {
                System.out.println("Parking Lot information deleted successfully!");
            } else {
                System.out.println("No Parking Lot found with the given lotID.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void updateParkingLotInfo(Statement stmt, Scanner scanner) {
        try {
            System.out.print("Enter lotID to update: ");
            int lotID = scanner.nextInt();

            System.out.print("Enter new address (press Enter to keep the current value): ");
            scanner.nextLine(); // Consume newline left by nextInt()
            String newAddress = scanner.nextLine();

            System.out.print("Enter new name (press Enter to keep the current value): ");
            String newName = scanner.nextLine();

            if (newAddress.isEmpty() && newName.isEmpty()) {
                System.out.println("No updates provided. Returning to the Parking Lot Menu.");
                return;
            }

            StringBuilder queryBuilder = new StringBuilder("UPDATE ParkingLot SET ");

            if (!newAddress.isEmpty()) {
                queryBuilder.append("address = '").append(newAddress).append("'");
            }

            if (!newName.isEmpty()) {
                if (queryBuilder.charAt(queryBuilder.length() - 1) != ' ') {
                    if (!newAddress.isEmpty()) {
                        queryBuilder.append(", ");
                    }
                }
                queryBuilder.append("name = '").append(newName).append("'");
            }

            queryBuilder.append(" WHERE lotID = ").append(lotID).append(";");

            String query = queryBuilder.toString();

            int rowsAffected = stmt.executeUpdate(query);

            if (rowsAffected > 0) {
                System.out.println("Parking Lot information updated successfully!");
            } else {
                System.out.println("No Parking Lot found with the given lotID.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    private static void getParkingLotInfo(Statement stmt, Scanner scanner) {
        try {
            System.out.print("Enter name to get information: ");
            scanner.nextLine(); // Consume newline left by nextInt()
            String lotName = scanner.nextLine();

            String query = String.format("SELECT * FROM ParkingLot WHERE name = '%s';", lotName);

            ResultSet resultSet = stmt.executeQuery(query);
            ResultSetMetaData metaData = resultSet.getMetaData();

            int columnCount = metaData.getColumnCount();

            while (resultSet.next()) {
                for (int i = 1; i <= columnCount; i++) {
                    System.out.print(metaData.getColumnName(i) + ": " + resultSet.getString(i) + " ");
                }
                System.out.println();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void handleZoneMenu(Statement stmt) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\nZone Menu:");
            System.out.println("1. Display entire Zone table");
            System.out.println("2. Add Zone information");
            System.out.println("3. Delete Zone information");
            System.out.println("4. Update Zone information");
            System.out.println("5. Get Zone information");
            System.out.println("6. Back to Main Menu");
            System.out.print("Choose an option: ");

            int zoneOption = scanner.nextInt();

            switch (zoneOption) {
            	case 1:
            		displayEntireZoneTable(stmt);
            		break;
                case 2:
                    addZoneInfo(stmt, scanner);
                    break;
                case 3:
                    deleteZoneInfo(stmt, scanner);
                    break;
                case 4:
                    updateZoneInfo(stmt, scanner);
                    break;
                case 5:
                    getZoneInfo(stmt, scanner);
                    break;
                case 6:
                    System.out.println("Returning to Main Menu...");
                    return; // Exit the method to go back to the main menu
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    private static void addZoneInfo(Statement stmt, Scanner scanner) {
        try {
            System.out.print("Enter zoneID: ");
            String zoneID = scanner.next();

            // Check if the entered zoneID is valid
            if (!isValidZoneID(zoneID)) {
                System.out.println("Error: Invalid zoneID. Please enter a valid zoneID.");
                return; // Exit the method if the zoneID is invalid
            }

            System.out.print("Enter lotID: ");
            int lotID = scanner.nextInt();

            System.out.print("Enter zone name: ");
            scanner.nextLine(); // Consume newline left by nextInt()
            String zoneName = scanner.nextLine();

            String query = String.format(
                    "INSERT INTO Zone (zoneID, lotID, zone_name) VALUES ('%s', %d, '%s');",
                    zoneID, lotID, zoneName);

            stmt.executeUpdate(query);

            System.out.println("Zone information added successfully!");
        } catch (SQLIntegrityConstraintViolationException e) {
            System.out.println("Error: Zone with the provided zoneID and lotID already exists.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Helper method to check if the entered zoneID is valid
    private static boolean isValidZoneID(String zoneID) {
        // Define a list of valid zone IDs
        String[] validZoneIDs = {"A", "B", "C", "D", "AS", "BS", "CS", "DS", "V"};

        // Check if the entered zoneID is in the list of valid IDs
        for (String validID : validZoneIDs) {
            if (zoneID.equals(validID)) {
                return true;
            }
        }

        return false;
    }


    private static void deleteZoneInfo(Statement stmt, Scanner scanner) {
        try {
            System.out.print("Enter zoneID to delete: ");
            String zoneID = scanner.next();

            System.out.print("Enter lotID to delete: ");
            int lotID = scanner.nextInt();

            // Delete records in child tables (e.g., Space) first
            deleteRecordsInChildTables(stmt, zoneID, lotID);

            // Now, delete the record in the parent table (Zone)
            String query = String.format(
                    "DELETE FROM Zone WHERE zoneID = '%s' AND lotID = %d;",
                    zoneID, lotID);

            int rowsAffected = stmt.executeUpdate(query);

            if (rowsAffected > 0) {
                System.out.println("Zone information deleted successfully!");
            } else {
                System.out.println("No Zone found with the given parameters.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void deleteRecordsInChildTables(Statement stmt, String zoneID, int lotID) throws SQLException {
        // Delete records in child tables (e.g., Space) where zoneID and lotID match
        // Adjust the table names and column names according to your database schema
        String[] childTables = {"Space"};  // Add other child tables as needed

        for (String childTable : childTables) {
            String deleteQuery = String.format(
                    "DELETE FROM %s WHERE zoneID = '%s' AND lotID = %d;",
                    childTable, zoneID, lotID);

            stmt.executeUpdate(deleteQuery);
        }
    }


    private static void updateZoneInfo(Statement stmt, Scanner scanner) {
        try {
            System.out.print("Enter zoneID to update: ");
            String zoneID = scanner.next();

            System.out.print("Enter lotID to update: ");
            int lotID = scanner.nextInt();

            System.out.print("Enter new zone name: ");
            scanner.nextLine(); // Consume newline left by nextInt()
            String newZoneName = scanner.nextLine();

            String query = String.format(
                    "UPDATE Zone SET zone_name = '%s' WHERE zoneID = '%s' AND lotID = %d;",
                    newZoneName, zoneID, lotID);

            int rowsAffected = stmt.executeUpdate(query);

            if (rowsAffected > 0) {
                System.out.println("Zone information updated successfully!");
            } else {
                System.out.println("No Zone found with the given parameters.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void getZoneInfo(Statement stmt, Scanner scanner) {
        try {
            System.out.print("Enter zoneID: ");
            String zoneID = scanner.next();

            System.out.print("Enter lotID: ");
            int lotID = scanner.nextInt();

            System.out.print("Enter zone name (press Enter if not known): ");
            scanner.nextLine(); // Consume newline left by nextInt()
            String zoneName = scanner.nextLine().trim();

            StringBuilder queryBuilder = new StringBuilder("SELECT * FROM Zone WHERE zoneID = '")
                    .append(zoneID)
                    .append("' AND lotID = ")
                    .append(lotID);

            if (!zoneName.isEmpty()) {
                queryBuilder.append(" AND zone_name = '").append(zoneName).append("'");
            }

            String query = queryBuilder.toString();

            ResultSet resultSet = stmt.executeQuery(query);
            ResultSetMetaData metaData = resultSet.getMetaData();

            int columnCount = metaData.getColumnCount();

            while (resultSet.next()) {
                for (int i = 1; i <= columnCount; i++) {
                    System.out.print(metaData.getColumnName(i) + ": " + resultSet.getString(i) + " ");
                }
                System.out.println();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void displayEntireZoneTable(Statement stmt) {
        try {
            String query = "SELECT * FROM Zone;";
            ResultSet resultSet = stmt.executeQuery(query);
            ResultSetMetaData metaData = resultSet.getMetaData();

            int columnCount = metaData.getColumnCount();

            while (resultSet.next()) {
                for (int i = 1; i <= columnCount; i++) {
                    System.out.print(metaData.getColumnName(i) + ": " + resultSet.getString(i) + " ");
                }
                System.out.println();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void handleSpaceMenu(Statement stmt) {
    	Scanner scanner = new Scanner(System.in);
        int option;
        do {
            System.out.println("Space Menu:");
            System.out.println("1. Display Space Table");
            System.out.println("2. Add Space Info");
            System.out.println("3. Delete Space Info");
            System.out.println("4. Update Space Info");
            System.out.println("5. Get Space Info");
            System.out.println("6. Back to Main Menu");
            System.out.print("Choose an option: ");

            option = scanner.nextInt();

            switch (option) {
	            case 1: 
	            	displayEntireSpaceTable(stmt);
	            	break;
                case 2:
                    addSpaceInfo(stmt, scanner);
                    break;
                case 3:
                    deleteSpaceInfo(stmt, scanner);
                    break;
                case 4:
                    updateSpaceInfo(stmt, scanner);
                    break;
                case 5:
                    getSpaceInfo(stmt, scanner);
                    break;
                case 6:
                    System.out.println("Returning to the main menu.");
                    return;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        } while (option != 5);
    }

    private static void addSpaceInfo(Statement stmt, Scanner scanner) {
        try {
            System.out.print("Enter spaceNum: ");
            int spaceNum = scanner.nextInt();
            scanner.nextLine(); // Consume newline left by nextInt()

            System.out.print("Enter zoneID: ");
            String zoneID = scanner.next();

            System.out.print("Enter lotID: ");
            int lotID = scanner.nextInt();
            scanner.nextLine(); // Consume newline left by nextInt()

            System.out.print("Enter space type (Options: Electric, Handicap, Compact Car, Regular, press Enter for default 'Regular'): ");
            String spaceTypeInput = scanner.nextLine().trim();
            String spaceType = spaceTypeInput.isEmpty() ? "Regular" : validateSpaceType(spaceTypeInput);

            System.out.print("Enter availability status: ");
            String availabilityStatus = scanner.next();

            String query = String.format(
                    "INSERT INTO Space (spaceNum, zoneID, lotID, spaceType, availabilityStatus) " +
                            "VALUES (%d, '%s', %d, '%s', '%s');",
                    spaceNum, zoneID, lotID, spaceType, availabilityStatus);

            stmt.executeUpdate(query);

            System.out.println("Space information added successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    private static String validateSpaceType(String spaceTypeInput) {
        String[] validSpaceTypes = {"Electric", "Handicap", "Compact Car", "Regular"};

        for (String validType : validSpaceTypes) {
            if (validType.equalsIgnoreCase(spaceTypeInput)) {
                return spaceTypeInput;
            }
        }

        System.out.println("Invalid space type. Setting default to 'Regular'.");
        return "Regular";
    }




    private static void deleteSpaceInfo(Statement stmt, Scanner scanner) {
        try {
            System.out.print("Enter spaceNum to delete: ");
            int spaceNum = scanner.nextInt();

            String query = String.format(
                    "DELETE FROM Space WHERE spaceNum = %d;",
                    spaceNum);

            stmt.executeUpdate(query);

            System.out.println("Space information deleted successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void updateSpaceInfo(Statement stmt, Scanner scanner) {
        try {
            System.out.print("Enter spaceNum to update: ");
            int spaceNum = scanner.nextInt();
            scanner.nextLine(); // Consume newline left by nextInt()

            System.out.print("Enter new space type (press Enter to keep the current value): ");
            String newSpaceType = scanner.nextLine();

            System.out.print("Enter new availability status (press Enter to keep the current value): ");
            String newAvailabilityStatus = scanner.nextLine();

            if (newSpaceType.isEmpty() && newAvailabilityStatus.isEmpty()) {
                System.out.println("No updates provided. Exiting update operation.");
                return;
            }

            StringBuilder queryBuilder = new StringBuilder("UPDATE Space SET ");

            if (!newSpaceType.isEmpty()) {
                queryBuilder.append("spaceType = '").append(newSpaceType).append("'");
            }

            if (!newSpaceType.isEmpty() && !newAvailabilityStatus.isEmpty()) {
                queryBuilder.append(", ");
            }

            if (!newAvailabilityStatus.isEmpty()) {
                queryBuilder.append("availabilityStatus = '").append(newAvailabilityStatus).append("'");
            }

            queryBuilder.append(" WHERE spaceNum = ").append(spaceNum).append(";");

            String query = queryBuilder.toString();

            int rowsAffected = stmt.executeUpdate(query);

            if (rowsAffected > 0) {
                System.out.println("Space information updated successfully!");
            } else {
                System.out.println("No Space found with the given spaceNum.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    private static void getSpaceInfo(Statement stmt, Scanner scanner) {
        try {
            System.out.print("Enter spaceNum: ");
            int spaceNum = scanner.nextInt();

            String query = String.format(
                    "SELECT * FROM Space WHERE spaceNum = %d;",
                    spaceNum);

            ResultSet resultSet = stmt.executeQuery(query);
            ResultSetMetaData metaData = resultSet.getMetaData();

            int columnCount = metaData.getColumnCount();

            while (resultSet.next()) {
                for (int i = 1; i <= columnCount; i++) {
                    System.out.print(metaData.getColumnName(i) + ": " + resultSet.getString(i) + " ");
                }
                System.out.println();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    private static void displayEntireSpaceTable(Statement stmt) {
        try {
            String query = "SELECT * FROM Space;";
            ResultSet resultSet = stmt.executeQuery(query);
            ResultSetMetaData metaData = resultSet.getMetaData();

            int columnCount = metaData.getColumnCount();

            while (resultSet.next()) {
                for (int i = 1; i <= columnCount; i++) {
                    System.out.print(metaData.getColumnName(i) + ": " + resultSet.getString(i) + " ");
                }
                System.out.println();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    private static void getNumberOfEmployeesWithPermitsForZone(Statement stmt, Scanner scanner) {
        try {
            System.out.print("Enter status (E, S, V): ");
            String status = scanner.next().toUpperCase(); // Convert to uppercase for case-insensitivity

            String[] validZoneIDs;
            String employeeType;

            switch (status) {
                case "E":
                    validZoneIDs = new String[]{"A", "B", "C", "D"};
                    employeeType = "Employees";
                    break;
                case "S":
                    validZoneIDs = new String[]{"AS", "BS", "CS", "DS"};
                    employeeType = "Students";
                    break;
                case "V":
                    validZoneIDs = new String[]{"V"};
                    employeeType = "Visitors";
                    break;
                default:
                    System.out.println("Invalid status. Please enter a valid status (E, S, V).");
                    return;
            }

            System.out.print("Enter zoneID (" + String.join(", ", validZoneIDs) + "): ");
            String zoneID = scanner.next().toUpperCase(); // Convert to uppercase for case-insensitivity

            // Check if the entered zoneID is valid
            if (!Arrays.asList(validZoneIDs).contains(zoneID)) {
                System.out.println("Invalid zoneID. Please enter a valid zoneID.");
                return;
            }

            String query = String.format(
                    "SELECT COUNT(DISTINCT d.driverID) AS NumberOf%s " +
                            "FROM Zone z " +
                            "JOIN Permit p ON z.lotID = p.lot " +
                            "JOIN AllottedTo a ON a.permitID = p.permitID " +
                            "JOIN Driver d ON a.driverID = d.driverID " +
                            "WHERE d.status = '%s' AND z.zoneID = '%s';", employeeType, status, zoneID);

            ResultSet resultSet = stmt.executeQuery(query);
            if (resultSet.next()) {
                int numberOfEmployees = resultSet.getInt("NumberOf" + employeeType);
                System.out.println("Number of " + employeeType + " with permits for the given zone and status: " + numberOfEmployees);
            } else {
                System.out.println("No data found for the given zone and status.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



    private static void getAvailableSpaces(Statement stmt, Scanner scanner) {
        try {
            System.out.print("Enter spaceType (Options: Electric, Compact Car, Handicap, Regular): ");
            String spaceType = scanner.next().trim();
            scanner.nextLine();  // Consume the newline character

            System.out.print("Enter zoneID (Options: A, B, C, D, AS, BS, CS, DS, V): ");
            String zoneID = scanner.next().trim();
            scanner.nextLine();  // Consume the newline character

            System.out.print("Enter lotID (press Enter to skip): ");
            String lotIDInput = scanner.nextLine().trim();
            int lotID = lotIDInput.isEmpty() ? -1 : Integer.parseInt(lotIDInput);

            StringBuilder queryBuilder = new StringBuilder("SELECT s.spaceNum, s.lotID, s.zoneID " +
                    "FROM Space s " +
                    "JOIN ParkingLot p ON s.lotID = p.lotID " +
                    "WHERE s.spaceType = ? AND s.zoneID = ?");

            if (lotID != -1) {
                queryBuilder.append(" AND s.lotID = ?");
            }

            String query = queryBuilder.toString();
            PreparedStatement preparedStatement = stmt.getConnection().prepareStatement(query);

            preparedStatement.setString(1, spaceType);
            preparedStatement.setString(2, zoneID);

            if (lotID != -1) {
                preparedStatement.setInt(3, lotID);
            }

            ResultSet resultSet = preparedStatement.executeQuery();

            if (!resultSet.isBeforeFirst()) {
                System.out.println("No data available for the given input.");
                return;
            }

            while (resultSet.next()) {
                int spaceNum = resultSet.getInt("spaceNum");
                int spaceLotID = resultSet.getInt("lotID");
                String spaceZoneID = resultSet.getString("zoneID");
                System.out.println("Available Space Number: " + spaceNum + " | LotID: " + spaceLotID + " | ZoneID: " + spaceZoneID);
            }
        } catch (SQLException | NumberFormatException e) {
            e.printStackTrace();
        }
    }




   


    private static void getListOfZonesForEachLot(Statement stmt) {
        try {
            String query = "SELECT lotID, zoneID FROM Zone GROUP BY lotID, zoneID";
            ResultSet resultSet = stmt.executeQuery(query);

            while (resultSet.next()) {
                int lotID = resultSet.getInt("lotID");
                String zoneID = resultSet.getString("zoneID");
                System.out.println("Lot: " + lotID + " | Zone: " + zoneID);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    private static void displayViolationCars(Statement stmt) {
        try {
//            String combinedQuery = "SELECT DISTINCT pa.licenseNum\r\n"
//            		+ "FROM ParkedAt pa\r\n"
//            		+ "LEFT JOIN Permit pe ON pa.lotID = pe.lot\r\n"
//            		+ "WHERE pa.lotID IS NULL\r\n"
//            		+ "   OR pa.zoneID IS NULL\r\n"
//            		+ "   OR pa.spaceNum IS NULL\r\n"
//            		+ "   OR pe.permitID IS NULL\r\n"
//            		+ "   OR pa.lotID != pe.lot\r\n"
//            		+ "   OR pa.zoneID != pe.zoneID\r\n"
//            		+ "   OR pa.spaceNum NOT IN (\r\n"
//            		+ "      SELECT spaceNum\r\n"
//            		+ "      FROM Permit\r\n"
//            		+ "      WHERE zoneID = pa.zoneID AND lot = pa.lotID\r\n"
//            		+ "   )";
        	 String combinedQuery = "SELECT DISTINCT v.licenseNum  FROM Vehicle v   JOIN Has h ON h.licenseNum = v.licenseNum  JOIN ParkedAt p ON p.licenseNum = v.licenseNum  JOIN Space s ON p.lotID = s.lotID  JOIN AllottedTo a on a.driverID = h.driverID  JOIN Permit x on x.permitID = a.permitID  WHERE s.spaceType<>x.spaceType OR x.lot<>s.LotID;";


            ResultSet resultSet = stmt.executeQuery(combinedQuery);

            System.out.println("License Numbers of Vehicles in Violation:");
            while (resultSet.next()) {
                String licenseNum = resultSet.getString("licenseNum");
                System.out.println(licenseNum);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    private static void displayViolationCarsCount(Statement stmt) {
        try {
//            String combinedQuery = "SELECT DISTINCT pa.licenseNum\r\n"
//            		+ "FROM ParkedAt pa\r\n"
//            		+ "LEFT JOIN Permit pe ON pa.lotID = pe.lot\r\n"
//            		+ "WHERE pa.lotID IS NULL\r\n"
//            		+ "   OR pa.zoneID IS NULL\r\n"
//            		+ "   OR pa.spaceNum IS NULL\r\n"
//            		+ "   OR pe.permitID IS NULL\r\n"
//            		+ "   OR pa.lotID != pe.lot\r\n"
//            		+ "   OR pa.zoneID != pe.zoneID\r\n"
//            		+ "   OR pa.spaceNum NOT IN (\r\n"
//            		+ "      SELECT spaceNum\r\n"
//            		+ "      FROM Permit\r\n"
//            		+ "      WHERE zoneID = pa.zoneID AND lot = pa.lotID\r\n"
//            		+ "   )";
            String combinedQuery = "SELECT DISTINCT v.licenseNum  FROM Vehicle v   JOIN Has h ON h.licenseNum = v.licenseNum  JOIN ParkedAt p ON p.licenseNum = v.licenseNum  JOIN Space s ON p.lotID = s.lotID  JOIN AllottedTo a on a.driverID = h.driverID  JOIN Permit x on x.permitID = a.permitID  WHERE s.spaceType<>x.spaceType OR x.lot<>s.LotID;";

            ResultSet resultSet = stmt.executeQuery(combinedQuery);

            int c=0;
            System.out.println("License Numbers of Vehicles in Violation:");
            while (resultSet.next()) {
            	c++;
                String licenseNum = resultSet.getString("licenseNum");
//                System.out.println(licenseNum);
            }
            System.out.println("Count Of Cars that are currently in violation "+ c);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


}
