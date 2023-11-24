package DBMS_Project_Demo;


import java.sql.*;
import java.sql.Date;
import java.util.*;
public class Driver{

   public static void driverOptions(Statement statement) {
        try {
            Scanner scanner = new Scanner(System.in);
            int option;

            do {
                displayDriverMenu();
                option = scanner.nextInt();

                switch (option) {
                    case 1:
                        displayAllDrivers(statement);
                        break;
                    case 2:
                        addDriver(statement, scanner);
                        break;
                    case 3:
                        deleteDriver(statement, scanner);
                        break;
                    case 4:
                        updateDriver(statement, scanner);
                        break;
                    case 5:
                        displayDriverByID(statement, scanner);
                        break;
                    case 6:
                    	assignPermitToDriver2(statement, scanner);
                    	break;
                    case 7:
                    	displayAllottedPermitToDriver(statement, scanner);
                        break;
                    case 8:
                    	getCitationsForDriver(statement, scanner);
                        break;
                    case 9:
                    	settlesCitation(statement, scanner);
                    	break;
                    case 10:
                    	appealsCitation(statement,scanner);
                    	break;
                    case 0:
                        System.out.println("Exiting Driver Management");
                        break;
                    default:
                        System.out.println("Invalid option. Please choose a valid option.");
                        break;
                }
            } while (option != 0);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void displayDriverMenu() {
        System.out.println("Driver Management Menu:");
        System.out.println("1) Display All Drivers");
        System.out.println("2) Add Driver");
        System.out.println("3) Delete Driver");
        System.out.println("4) Update Driver");
        System.out.println("5) Get Driver Details by ID");
        System.out.println("6) Assign Permit to Driver");
        System.out.println("7) Get Permit details assigned to a particular Driver");
        System.out.println("8) Get Citations details assigned to a particular Driver");
        System.out.println("9) Settle citations for a particular Driver");
        System.out.println("10) Appeal citations for a particular Driver");
        System.out.println("0) Go Back to Main Menu");
        System.out.println("Select an option: ");
    }

    private static void displayAllDrivers(Statement statement) throws SQLException {
        String query = "SELECT * FROM Driver";
        ResultSet resultSet = statement.executeQuery(query);

        // Display all drivers
        while (resultSet.next()) {
            long driverID = resultSet.getLong("driverID");
            String name = resultSet.getString("name");
            String status = resultSet.getString("status");
            String isHandicapped = resultSet.getString("isHandicapped");

            System.out.println("Driver ID: " + driverID + ", Name: " + name + ", Status: " + status + ", Handicapped: " + isHandicapped);
        }

        resultSet.close();
    }

    private static void addDriver(Statement statement, Scanner scanner) throws SQLException {
        System.out.print("Enter Driver ID: ");
        long driverID = scanner.nextLong();
        scanner.nextLine(); // Consume the newline character

        // Check if the driverID already exists in the database
        if (isDriverIDExists(statement, driverID)) {
            System.out.println("Driver ID already exists in the database.");
            return;
        }

        System.out.print("Enter Driver Name: ");
        String name = scanner.nextLine();
        System.out.print("Enter Driver Status (E/V/S): ");
        String status = scanner.next().toUpperCase();

        // Validate the status (E/V/S)
        if (!status.equals("E") && !status.equals("V") && !status.equals("S")) 
        {
            System.out.println("Invalid status. Status must be E or V or S.");
            //return;
        }
        else {
        System.out.print("Is the Driver Handicapped? (Yes/No): ");
        String isHandicapped = scanner.next();

        String query = String.format("INSERT INTO Driver (driverID, name, status, isHandicapped) VALUES (%d, '%s', '%s', '%s')", driverID, name, status, isHandicapped);
        statement.executeUpdate(query);
        System.out.println("Driver added successfully.");
        }
        }

    private static boolean isDriverIDExists(Statement statement, long driverID) throws SQLException {
        String query = String.format("SELECT * FROM Driver WHERE driverID = %d", driverID);
        ResultSet resultSet = statement.executeQuery(query);

        boolean exists = resultSet.next();
        resultSet.close();
        return exists;
    }


    private static void deleteDriver(Statement statement, Scanner scanner) throws SQLException {
        System.out.print("Enter Driver ID to delete: ");
        long driverID = scanner.nextLong();

        String query = String.format("DELETE FROM Driver WHERE driverID = %d", driverID);
        int rowsAffected = statement.executeUpdate(query);

        if (rowsAffected > 0) {
            System.out.println("Driver deleted successfully.");
        } else {
            System.out.println("No driver found with ID: " + driverID);
        }
    }

    private static void updateDriver(Statement statement, Scanner scanner) throws SQLException {
        System.out.print("Enter Driver ID to update: ");
        long driverID = scanner.nextLong();
        scanner.nextLine(); // Consume the newline character

        System.out.print("Enter updated Driver Name: ");
        String name = scanner.nextLine();
        System.out.print("Enter updated Driver Status: ");
        String status = scanner.nextLine();
        System.out.print("Is the Driver Handicapped? (true/false): ");
        String isHandicapped = scanner.nextLine();

        String query = String.format("UPDATE Driver SET name='%s', status='%s', isHandicapped='%s' WHERE driverID=%d",
                name, status, isHandicapped, driverID);
        int rowsAffected = statement.executeUpdate(query);

        if (rowsAffected > 0) {
            System.out.println("Driver updated successfully.");
        } else {
            System.out.println("No driver found with ID: " + driverID);
        }
    }

    private static void displayDriverByID(Statement statement, Scanner scanner) throws SQLException {
        System.out.print("Enter Driver ID: ");
        long driverID = scanner.nextLong();

        String query = String.format("SELECT * FROM Driver WHERE driverID = %d", driverID);
        ResultSet resultSet = statement.executeQuery(query);

        if (resultSet.next()) {
            String name = resultSet.getString("name");
            String status = resultSet.getString("status");
            String isHandicapped = resultSet.getString("isHandicapped");

            System.out.println("Driver ID: " + driverID + ", Name: " + name + ", Status: " + status + ", Handicapped: " + isHandicapped);
        } else {
            System.out.println("No driver found with ID: " + driverID);
        }

        resultSet.close();
    }
    
    

    private static int getAssignedPermitsCount(Statement statement, int driverID) throws SQLException {
        String query = String.format("SELECT COUNT(*) AS assignedPermits FROM AllottedTo WHERE driverID = %d", driverID);
        ResultSet resultSet = statement.executeQuery(query);

        int assignedPermits = 0;
        if (resultSet.next()) {
            assignedPermits = resultSet.getInt("assignedPermits");
        }

        resultSet.close();
        return assignedPermits;
    }
    private static void displayAllottedPermitToDriver(Statement statement, Scanner scanner) throws SQLException {
        System.out.print("Enter Driver ID: ");
        long driverID = scanner.nextLong();

        String query = String.format("SELECT p.* FROM Permit p JOIN AllottedTo a ON p.permitID = a.permitID WHERE a.driverID = %d", driverID);
        ResultSet resultSet = statement.executeQuery(query);

        System.out.println("Allotted Permits to Driver ID " + driverID + ":");
        while (resultSet.next()) {
            int permitID = resultSet.getInt("permitID");
            String spaceType = resultSet.getString("spaceType");
            Date startDate = resultSet.getDate("startDate");
            Date expirationDate = resultSet.getDate("expirationDate");
            String permitType = resultSet.getString("permitType");
            Time expirationTime = resultSet.getTime("expirationTime");

            System.out.println("Permit ID: " + permitID + ", Space Type: " + spaceType + ", Start Date: " + startDate +
                    ", Expiration Date: " + expirationDate + ", Permit Type: " + permitType + ", Expiration Time: " + expirationTime);
        }
    }

    private static String getDriverCategory(Statement statement, int driverID) throws SQLException {
        String query = String.format("SELECT status FROM Driver WHERE driverID = %d", driverID);
        ResultSet resultSet = statement.executeQuery(query);

        String driverCategory = "";
        if (resultSet.next()) {
            driverCategory = resultSet.getString("status");
        }

        resultSet.close();
        return driverCategory;
    }

    private static int getMaxPermitsForCategory(String driverCategory) {
        if (driverCategory.equalsIgnoreCase("student") || driverCategory.equalsIgnoreCase("visitor")) {
            return 1; // Max 1 permit for student/visitor
        } else if (driverCategory.equalsIgnoreCase("employee")) {
            return 2; // Max 2 permits for employee
        }
        return 0;
    }

    private static boolean checkSpecialEventOrParkAndRide(Statement statement, int permitID) throws SQLException {
        String query = String.format("SELECT permitType FROM Permit WHERE permitID = %d", permitID);
        ResultSet resultSet = statement.executeQuery(query);

        boolean isSpecialEventOrParkAndRide = false;
        if (resultSet.next()) {
            String permitType = resultSet.getString("permitType");
            isSpecialEventOrParkAndRide = permitType.equalsIgnoreCase("special event") || permitType.equalsIgnoreCase("park & ride");
        }

        resultSet.close();
        return isSpecialEventOrParkAndRide;
    }


    private static void getCitationsForDriver(Statement statement, Scanner scanner) throws SQLException {
        System.out.print("Enter Driver ID: ");
        long driverID = scanner.nextLong();

        String query = "SELECT r.citationNum, r.licenseNum, c.citationTime, c.citationDate, c.lot, c.paymentStatus, c.category " +
                "FROM RaisedTo r " +
                "JOIN Has h ON r.licenseNum = h.licenseNum " +
                "JOIN Citation c ON r.citationNum = c.citationNum " +
                "WHERE h.driverID = " + driverID;

        ResultSet resultSet = statement.executeQuery(query);

//        if(resultSet.getFetchSize()==0) {
//        	System.out.println("No Citation Details found for this driver");
//        	return;
//        }
        System.out.println("Citations with Car Details for Driver ID " + driverID + ":");
        
        while (resultSet.next()) {
            int citationNum = resultSet.getInt("citationNum");
            String licenseNum = resultSet.getString("licenseNum");
            Time citationTime = resultSet.getTime("citationTime");
            Date citationDate = resultSet.getDate("citationDate");
            int lot = resultSet.getInt("lot");
            String paymentStatus = resultSet.getString("paymentStatus");
            String category = resultSet.getString("category");

            System.out.println("Citation Number: " + citationNum + ", License Number: " + licenseNum +
                    ", Citation Time: " + citationTime + ", Citation Date: " + citationDate +
                    ", Lot: " + lot + ", Payment Status: " + paymentStatus + ", Category: " + category);
        }

        resultSet.close();
    }


    private static void appealsCitation(Statement statement, Scanner scanner) throws SQLException {
        
    	System.out.print("Enter Driver ID: ");
        long driverID = scanner.nextLong();
    	String checkAppealQuery = "SELECT citationNum FROM Appeals WHERE driverID = " + driverID;
        ResultSet appealResultSet = statement.executeQuery(checkAppealQuery);
        

        if (appealResultSet.next()) {
            int citedAppeal = appealResultSet.getInt("citationNum");
            System.out.println("Driver with ID " + driverID + " has already appealed Citation: " + citedAppeal);
        } else {
            String query = "SELECT citationNum FROM RaisedTo r JOIN Has h ON r.licenseNum = h.licenseNum WHERE h.driverID = " + driverID;
            ResultSet resultSet = statement.executeQuery(query);
            int citationNum = 0;

            if (resultSet.next()) {
                citationNum = resultSet.getInt("citationNum");
            } else {
                System.out.println("No citation found for Driver ID: " + driverID);
                return;
            }

            // Appeal Citation
            String appealQuery = String.format("INSERT INTO Appeals (driverID, citationNum) VALUES (%d, %d)", driverID, citationNum);
            statement.executeUpdate(appealQuery);
            System.out.println("Citation " + citationNum + " appealed successfully for Driver ID: " + driverID);
        }
    }
    
    private static void settlesCitation(Statement statement, Scanner scanner) throws SQLException {
        
    	System.out.print("Enter Driver ID: ");
        long driverID = scanner.nextLong();
    	String checkSettleQuery = "SELECT citationNum FROM Settles WHERE driverID = " + driverID;
        ResultSet settleResultSet = statement.executeQuery(checkSettleQuery);

        if (settleResultSet.next()) {
            int citedSettle = settleResultSet.getInt("citationNum");
            System.out.println("Driver with ID " + driverID + " has already settled Citation: " + citedSettle);
        } else {
            String query = "SELECT citationNum FROM RaisedTo r JOIN Has h ON r.licenseNum = h.licenseNum WHERE h.driverID = " + driverID;
            ResultSet resultSet = statement.executeQuery(query);
            int citationNum = 0;

            if (resultSet.next()) {
                citationNum = resultSet.getInt("citationNum");
            } else {
                System.out.println("No citation found for Driver ID: " + driverID);
                return;
            }

            // Settle Citation
            System.out.print("Enter the Payment ID of the settlement: ");
            int payID = scanner.nextInt();
            String settleQuery = String.format("INSERT INTO Settles (driverID, citationNum, payID) VALUES (%d, %d, %d)", driverID, citationNum,payID);
            statement.executeUpdate(settleQuery);
            System.out.println("Citation " + citationNum + " settled successfully for Driver ID: " + driverID);
        }
    }
    private static void assignPermitToDriver(Statement statement, Scanner scanner) throws SQLException 
{
        System.out.print("Enter Driver ID: ");
        long driverID = scanner.nextLong();

        String permitCheckQuery = "SELECT permitID FROM AllottedTo WHERE driverID = " + driverID;
        ResultSet permitResultSet = statement.executeQuery(permitCheckQuery);

        if (permitResultSet.next()) {
            System.out.println("Driver with ID " + driverID + " already has a permit assigned.");
        } 
else {
            scanner.nextLine(); // Consume the newline character
            System.out.println("Valid Space Types: Regular, Handicapped, Compact Car, Electric");
            System.out.print("Select Space Type: ");
            String spaceType = scanner.nextLine();
            
            System.out.println("Valid  Lots: 1, 2, 3, 4");
            System.out.print("Select Lot: ");
            int LotId = scanner.nextInt();
            scanner.nextLine();
            
            String zoneOptions = getZoneOptions(statement, driverID);
           
            
            System.out.println("As the Driver is visitor, status is V");
            
            
            	System.out.println("Select Zone from the below options:");
            
            System.out.println(zoneOptions);
            
           
            String zone = scanner.nextLine();

            System.out.println("Select Space Number from 1, 2, 3, 4, 5");
            System.out.print("Enter Space Number: ");
            int spaceNum = scanner.nextInt();
            

            // Check availability and validity of selected space details
            String spaceAvailabilityCheckQuery = String.format("SELECT * FROM Space WHERE zoneID='%s' AND LotID= '%d' AND spacetype='%s' AND spaceNum=%d AND availabilityStatus='Available'", zone,LotId, spaceType, spaceNum);
            ResultSet spaceAvailabilityResultSet = statement.executeQuery(spaceAvailabilityCheckQuery);
             
            if (spaceAvailabilityResultSet.next()) 
           {
                // Insert into Permit table
            	System.out.println(spaceAvailabilityResultSet);
            	System.out.println("Enter Permit ID");
            	
                int permitID = scanner.nextInt(); // Implement your logic to generate a unique permit ID
                scanner.nextLine();
                System.out.println("Enter Start Date");
                String startDate  = scanner.nextLine();
                
                System.out.println("Enter Expiration Date");
                String expirationDate  = scanner.nextLine();
                //scanner.nextLine();
               
                System.out.println("Enter ExpirationTime");
                String expirationTime  = scanner.nextLine();
              //  scanner.nextLine();
                
                System.out.println("Enter PermitType");
                String permitType  = scanner.nextLine();
                
               
                String insertPermitQuery = String.format("INSERT INTO Permit (permitID,spaceType,startDate,expirationDate,permitType, expirationTime,lot, zoneId) VALUES (%d, '%s', '%s', '%s','%s', '%s', %d , '%s')", permitID, spaceType, startDate, expirationDate, permitType,expirationTime,LotId,zone);
                statement.executeUpdate(insertPermitQuery);
                
                
                
                
       

                // Insert into AllottedTo table
                String assignPermitQuery = String.format("INSERT INTO AllottedTo (permitID, driverID) VALUES (%d, %d)", permitID, driverID);
                statement.executeUpdate(assignPermitQuery);

                // Update space availability status
                String updateSpaceAvailabilityQuery = String.format("UPDATE Space SET availabilityStatus='Not Available' WHERE zoneID='%s' AND spaceType='%s' AND spaceNum=%d", zone, spaceType, spaceNum);
                statement.executeUpdate(updateSpaceAvailabilityQuery);

                System.out.println("Permit assigned successfully to Driver ID: " + driverID);
            } else 
            {
                System.out.println("Space is not available or invalid details provided.");
            }
        }
    }
    
    private static String getZoneOptions(Statement statement, long driverID) throws SQLException {
        String driverStatusQuery = "SELECT status FROM Driver WHERE driverID = " + driverID;
        ResultSet statusResultSet = statement.executeQuery(driverStatusQuery);
        if (statusResultSet.next()) {
            String driverStatus = statusResultSet.getString("status");
            if (driverStatus.equals("E")) {
                return "Employee Zones: AE, BE, CE, DE";
            } else if (driverStatus.equals("S")) {
                return "Student Zones: AS, BS, CS, DS";
            }
            else if(driverStatus.equals('V'))
            {return "V";}
        }
        return "No valid zones available for this driver status.";
    }
    private static void assignPermitToDriver1(Statement statement, Scanner scanner) throws SQLException {
        System.out.print("Enter Driver ID: ");
        long driverID = scanner.nextLong();

        String permitCheckQuery = "SELECT permitID FROM AllottedTo WHERE driverID = " + driverID;
        ResultSet permitResultSet = statement.executeQuery(permitCheckQuery);

        if (permitResultSet.next()) {
            System.out.println("Driver with ID " + driverID + " already has a permit assigned.");
        } else {
            scanner.nextLine(); // Consume the newline character
            System.out.println("Valid Space Types: Regular, Handicapped, Compact Car, Electric");
            System.out.print("Select Space Type: ");
            String spaceType = scanner.nextLine();
            
            System.out.println("Valid Lots: 1, 2, 3, 4");
            System.out.print("Select Lot: ");
            int LotId = scanner.nextInt();
            scanner.nextLine();

            String zoneOptions = getZoneOptions1(statement, driverID);
            String zone = "";
            
            System.out.println("As the Driver is visitor, status is V");
            if (zoneOptions.equals("V")) {
                zone = "V";
            } else {
                System.out.println("Select Zone from the below options:");
                System.out.println(zoneOptions);
                zone = scanner.nextLine();
            }

            System.out.println("Select Space Number from 1, 2, 3, 4, 5");
            System.out.print("Enter Space Number: ");
            int spaceNum = scanner.nextInt();

            // Check availability and validity of selected space details
            String spaceAvailabilityCheckQuery = String.format("SELECT * FROM Space WHERE zoneID='%s' AND LotID='%d' AND spaceType='%s' AND spaceNum=%d AND availabilityStatus='Available'", zone, LotId, spaceType, spaceNum);
            ResultSet spaceAvailabilityResultSet = statement.executeQuery(spaceAvailabilityCheckQuery);
             
            if (spaceAvailabilityResultSet.next()) {
                // Insert into Permit table
                System.out.println("Enter Permit ID");
                int permitID = scanner.nextInt(); // Implement your logic to generate a unique permit ID
                scanner.nextLine();
                System.out.println("Enter Start Date");
                String startDate = scanner.nextLine();
                // ... Rest of the permit details input

                String insertPermitQuery = String.format("INSERT INTO Permit (permitID, spaceType, startDate, lot, zoneId) VALUES (%d, '%s', '%s', %d, '%s')", permitID, spaceType, startDate, LotId, zone);
                statement.executeUpdate(insertPermitQuery);

                // Insert into AllottedTo table
                String assignPermitQuery = String.format("INSERT INTO AllottedTo (permitID, driverID) VALUES (%d, %d)", permitID, driverID);
                statement.executeUpdate(assignPermitQuery);

                // Update space availability status
                String updateSpaceAvailabilityQuery = String.format("UPDATE Space SET availabilityStatus='Not Available' WHERE zoneID='%s' AND spaceType='%s' AND spaceNum=%d", zone, spaceType, spaceNum);
                statement.executeUpdate(updateSpaceAvailabilityQuery);

                System.out.println("Permit assigned successfully to Driver ID: " + driverID);
            } else {
                System.out.println("Space is not available or invalid details provided.");
            }
        }
    }

    private static String getZoneOptions1(Statement statement, long driverID) throws SQLException {
        String driverStatusQuery = "SELECT status FROM Driver WHERE driverID = " + driverID;
        ResultSet statusResultSet = statement.executeQuery(driverStatusQuery);
        if (statusResultSet.next()) {
            String driverStatus = statusResultSet.getString("status");
            if (driverStatus.equals("E")) {
                return "Employee Zones: A, B, C, D";
            } else if (driverStatus.equals("S")) {
                return "Student Zones: AS, BS, CS, DS";
            } else if (driverStatus.equals("V")) {
                return "V";
            }
        }
        return "No valid zones available for this driver status.";
    }
    private static void assignPermitToDriver2(Statement statement, Scanner scanner) throws SQLException {
        System.out.print("Enter Driver ID: ");
        long driverID = scanner.nextLong();

        String permitCheckQuery = "SELECT permitID FROM AllottedTo WHERE driverID = " + driverID;
        ResultSet permitResultSet = statement.executeQuery(permitCheckQuery);

        if (permitResultSet.next()) {
            String driverStatusQuery = "SELECT status FROM Driver WHERE driverID = " + driverID;
            ResultSet statusResultSet = statement.executeQuery(driverStatusQuery);
            if (statusResultSet.next()) {
                String driverStatus = statusResultSet.getString("status");
                if (driverStatus.equals("E")) {
                    System.out.println("Driver with ID " + driverID + " is an Employee.");
                    System.out.println("As the Driver is an Employee, they can have maximum upto two permits assigned.");
                    int count = countPermitsForDriverID(statement,driverID);
                    
                    System.out.println("Do you want to continue assigning another permit? (Y/N)");
                    String continueInput = scanner.next().toUpperCase();

                    if (!continueInput.equals("Y")) {
                       // System.out.println("This Driver is an Employee and since this driver already has 2 Permits Assigned, More permits cannot be assigned\n");
                    	return;}
                    if (count ==2) {
                    	System.out.println("This Driver is an Employee and since this driver already has 2 Permits Assigned, More permits cannot be assigned\n");
                        return;
                    }    
                        
                        
                    
                } else if (driverStatus.equals("S")) {
                    System.out.println("Driver with ID " + driverID + " is a Student.");
                    System.out.println("As the Driver is a Student, they can have only one permit assigned.");
                    return;
                } else if (driverStatus.equals("V")){
                    System.out.println("Driver with ID " + driverID + " is a Visitor.");
                    System.out.println("As the Driver is a Visitor, they can have only one permit assigned.");
                    return;
                }
            }
        }

        scanner.nextLine(); // Consume the newline character
        System.out.println("Valid Space Types: Regular, Handicapped, Compact Car, Electric");
        System.out.print("Select Space Type: ");
        String spaceType = scanner.nextLine();

        System.out.println("Valid Lots: 1, 2, 3, 4");
        System.out.print("Select Lot: ");
        int LotId = scanner.nextInt();
        scanner.nextLine();

        String zoneOptions = getZoneOptions2(statement, driverID);
        String zone = "";

        //System.out.println("As the Driver is visitor, status is V");
        if (zoneOptions.equals("V")) {
            zone = "V";
        } else {
            System.out.println("Select Zone from the below options:");
            System.out.println(zoneOptions);
            zone = scanner.nextLine();
        }

        System.out.println("Select Space Number from 1, 2, 3, 4, 5");
        System.out.print("Enter Space Number: ");
        int spaceNum = scanner.nextInt();

        // Check availability and validity of selected space details
        String spaceAvailabilityCheckQuery = String.format("SELECT * FROM Space WHERE zoneID='%s' AND LotID='%d' AND spaceType='%s' AND spaceNum=%d AND availabilityStatus='Available'", zone, LotId, spaceType, spaceNum);
        ResultSet spaceAvailabilityResultSet = statement.executeQuery(spaceAvailabilityCheckQuery);

        if (spaceAvailabilityResultSet.next()) {
            // Insert into Permit table
            System.out.println("Enter Permit ID");
            int permitID = scanner.nextInt(); // Implement your logic to generate a unique permit ID
            scanner.nextLine();
            System.out.println("Enter Start Date");
            String startDate = scanner.nextLine();
            
            
            
            System.out.println("Enter Expiration Date");
            String expirationDate  = scanner.nextLine();
            //scanner.nextLine();
           
            System.out.println("Enter ExpirationTime");
            String expirationTime  = scanner.nextLine();
          //  scanner.nextLine();
            
            System.out.println("Enter PermitType");
            String permitType  = scanner.nextLine();
            
           
            
            
            // ... Rest of the permit details input

            //String insertPermitQuery = String.format("INSERT INTO Permit (permitID, spaceType, startDate,expirationDate,permitType,expirationTime, lot, zoneId) VALUES (%d, '%s', '%s','%s','%s','%s' %d, '%s')", permitID, spaceType, startDate,expirationDate,expirationTime,permitType, LotId, zone);
            //statement.executeUpdate(insertPermitQuery);
            String insertPermitQuery = String.format("INSERT INTO Permit (permitID,spaceType,startDate,expirationDate,permitType, expirationTime,lot, zoneId) VALUES (%d, '%s', '%s', '%s','%s', '%s', %d , '%s')", permitID, spaceType, startDate, expirationDate, permitType,expirationTime,LotId,zone);
            statement.executeUpdate(insertPermitQuery);
            // Insert into AllottedTo table
            String assignPermitQuery = String.format("INSERT INTO AllottedTo (permitID, driverID) VALUES (%d, %d)", permitID, driverID);
            statement.executeUpdate(assignPermitQuery);

            // Update space availability status
            String updateSpaceAvailabilityQuery = String.format("UPDATE Space SET availabilityStatus='Not Available' WHERE zoneID='%s' AND spaceType='%s' AND spaceNum=%d", zone, spaceType, spaceNum);
            statement.executeUpdate(updateSpaceAvailabilityQuery);

            System.out.println("Permit assigned successfully to Driver ID: " + driverID);
        } else {
            System.out.println("Space is not available or invalid details provided.");
        }
    }

    private static String getZoneOptions2(Statement statement, long driverID) throws SQLException {
        String driverStatusQuery = "SELECT status FROM Driver WHERE driverID = " + driverID;
        ResultSet statusResultSet = statement.executeQuery(driverStatusQuery);
        if (statusResultSet.next()) {
            String driverStatus = statusResultSet.getString("status");
            if (driverStatus.equals("E")) {
                return "Employee Zones: A, B, C, D";
            } else if (driverStatus.equals("S")) {
                return "Student Zones: AS, BS, CS, DS";
            } else if (driverStatus.equals("V")) {
                return "V";
            }
        }
        return "No valid zones available for this driver status.";
    }

    
    private static int countPermitsForDriverID(Statement statement, long driverID) throws SQLException {
        String countQuery = "SELECT COUNT(*) AS permitCount FROM AllottedTo WHERE driverID = " + driverID;
        ResultSet countResultSet = statement.executeQuery(countQuery);

        if (countResultSet.next()) {
            return countResultSet.getInt("permitCount");
        }

        return 0; // Return 0 if no permits found or an error occurs
    }
    
    
}




