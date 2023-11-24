package DBMS_Project_Demo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.util.Scanner;

public class DBMS_Demo {
    public static void main(String args[]) {

        String jdbcURL = "jdbc:mariadb://classdb2.csc.ncsu.edu:3306/sraghuk";
        
        
        try {

            // Load the driver. This creates an instance of the driver
            // and calls the registerDriver method to make MariaDB Thin
            // driver, available to clients.

            Class.forName("org.mariadb.jdbc.Driver");

            String user = "sraghuk";
            String passwd = "200534075";

            Connection conn = null;
            Statement stmt = null;
            ResultSet rs = null;

            try {

                // Get a connection from the first driver in the
                // DriverManager list that recognizes the URL jdbcURL

                conn = DriverManager.getConnection(jdbcURL, user, passwd);

                // Create a statement object that will be sending your
                // SQL statements to the DBMS

                stmt = conn.createStatement();

                while (true) {
                    // Display the main menu
                    displayMainMenu();

                    Scanner scanner = new Scanner(System.in);
                    int option = scanner.nextInt();

                    // Process user input for main menu
                    switch (option) {
                        case 1:
                            handleDriverMenu(stmt);
                            break;
                        case 2:
                            handleParkingLotMenu(stmt);
                            break;
                        case 3:
                            handleVehicleMenu(stmt);
                            break;
                        case 4:
                            handlePermitMenu(stmt);
                            break;
                        case 5:
                            handleCitationMenu(stmt);
                            break;
                        case 6:
                            handleSecurityMenu(stmt);
                            break;
                        case 7:
                            handleAdministratorMenu(stmt);
                            break;
                        case 8:
                            System.out.println("Exiting");
                            return;
                        default:
                            System.out.println("Invalid option. Please choose a valid option.");
                            break;
                    }
                }

            } finally {
                close(rs);
                close(stmt);
                close(conn);
            }
        } catch (Throwable oops) {
            oops.printStackTrace();
        }
    }

    static void displayMainMenu() {
        System.out.println("Main Menu:");
        System.out.println("1) Driver");
        System.out.println("2) Parking Lot, Space and Zone");
        System.out.println("3) Vehicle");
        System.out.println("4) Permit");
        System.out.println("5) Citation");
        System.out.println("6) Security");
        System.out.println("7) Administrator");
        System.out.println("8) Exit");
        System.out.print("Select an option: ");
    }

    static void handleDriverMenu(Statement stmt) {
        Driver.driverOptions(stmt);
    }
    
    static void handleParkingLotMenu(Statement stmt) throws ClassNotFoundException {
        LotZoneSpace.parkingOptions();;
    }
    static void handleVehicleMenu(Statement stmt) {
    	VehicleInterface.vehicleOptions();
    }
    static void handlePermitMenu(Statement stmt) throws ClassNotFoundException, SQLException, ParseException {
        Permit.permitOptions(stmt);
    }
    static void handleCitationMenu(Statement stmt) throws SQLException {
        Citation.citationOptions(stmt);
    }
    static void handleSecurityMenu(Statement stmt) {
        Security.securityOptions(stmt);
    }
    static void handleAdministratorMenu(Statement stmt) {
        Admin.adminOptions();
    }
    
    static void close(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
            } catch (Throwable whatever) {
            }
        }
    }

    static void close(Statement st) {
        if (st != null) {
            try {
                st.close();
            } catch (Throwable whatever) {
            }
        }
    }

    static void close(ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            } catch (Throwable whatever) {
            }
        }
    }
}
