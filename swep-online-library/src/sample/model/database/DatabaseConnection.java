package sample.model.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * A DatabaseConnection handles the application's connection to the external database. It is implemented after the
 * Singleton pattern to ensure a single, steady connection throughout the program's runtime. A database connection will
 * be opened upon program start.
 */
public class DatabaseConnection {

    // Singleton instance

    private static DatabaseConnection instance;

    // Database connection credentials

    private Connection connection;
    private final String url = "jdbc:mysql://localhost:3306/online-library";
    private final String username = "root";
    private final String password = "root";

    /**
     * Default constructor. Get's called when getInstance() detects no existing instance.
     */
    private DatabaseConnection() {

    }

    /**
     * Returns the currently saved instance of a DatabaseConnection or creates a new one if not existent. Forces
     * the Singleton pattern.
     * @return The currently saved DatabaseInstance or new instance if not existent before call.
     */
    public static DatabaseConnection getInstance() {

        if(instance == null) {
            instance = new DatabaseConnection();
        }
        return instance;

    }

    /**
     * Connects to the corresponding MySQL database upon.
     * Constructed after: https://www.youtube.com/watch?v=9rTJa4l8YQ0
     *
     * In case this method shows compiler errors: Make sure 'mysql-connector-java-8.0.20.jar' is integrated into
     * the project (IntelliJ: Settings -> Project Structure... -> Project Settings -> Libraries)
     *
     * You can find the .jar in "/src/externalLibraries/mysql-connector-java-8.0.20.jar"
     *
     */
    public void connect() {

        try {

            System.out.println("Attempting to connect to database...");

            // Load the JDBC MySQL driver

            // Note: Loading class `com.mysql.jdbc.Driver' is deprecated. The new driver class is
            // `com.mysql.cj.jdbc.Driver'.

            Class.forName("com.mysql.cj.jdbc.Driver");

            // Establish connection to the database (In this case just localhost)

            connection = DriverManager.getConnection(
                    url, username, password
            );

            // Testing if connection works

            System.out.println("Database connection successfully established!");

        } catch (ClassNotFoundException cnfEx) {
            cnfEx.printStackTrace();
        } catch (SQLException sqlEx) {
            sqlEx.printStackTrace();
        }

    }

    /**
     * Closes the connection to the database. Used when:
     * - A user proceeds from the login/register form to the catalog
     */
    public void disconnect() {

        try {

            System.out.println("Closing database connection...");
            connection.close();
            System.out.println("Disconnected from database!");

        } catch(SQLException sqlEx) {
            sqlEx.printStackTrace();
        }

    }

    /**
     * Getter method. Returns an instance of the current DatabaseConnection.
     * @return instance of current DatabaseConnection.
     */
    public Connection getConnection() {
        return connection;
    }

}
