package ec.edu.sistemalicencias.config;

import ec.edu.sistemalicencias.model.exceptions.BaseDatosException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Database configuration class.
 * Implements Singleton pattern to manage a single DB connection config.
 *
 * @author Sistema Licencias Ecuador
 * @version 1.0
 */
public class DatabaseConfig {

    // Singleton instance
    private static DatabaseConfig instance;

    // Database connection parameters
    private final String url;
    private final String user;
    private final String password;
    private final String driver;

    /**
     * Private constructor (Singleton)
     * PostgreSQL configuration
     */
    private DatabaseConfig() {

        this.driver = "org.postgresql.Driver";
        this.url = "jdbc:postgresql://localhost:5432/licencias_db";
        this.user = "postgres";
        this.password = "root";

        try {
            Class.forName(driver);
        } catch (ClassNotFoundException e) {
            System.err.println("Error loading PostgreSQL driver: " + e.getMessage());
        }
    }

    /**
     * Returns the single instance of DatabaseConfig
     */
    public static synchronized DatabaseConfig getInstance() {
        if (instance == null) {
            instance = new DatabaseConfig();
        }
        return instance;
    }

    /**
     * Creates and returns a database connection
     */
    public Connection getConnection() throws BaseDatosException {
        try {
            Connection connection = DriverManager.getConnection(url, user, password);
            connection.setAutoCommit(true);
            return connection;
        } catch (SQLException e) {
            throw new BaseDatosException("Error connecting to database", e);
        }
    }

    /**
     * Safely closes a database connection
     */
    public void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                if (!connection.isClosed()) {
                    connection.close();
                }
            } catch (SQLException e) {
                System.err.println("Error closing connection: " + e.getMessage());
            }
        }
    }

    /**
     * Verifies database connectivity
     */
    public boolean checkConnection() {
        try (Connection conn = getConnection()) {
            return conn != null && !conn.isClosed();
        } catch (Exception e) {
            System.err.println("Connection check failed: " + e.getMessage());
            return false;
        }
    }

    // Getters

    public String getUrl() {
        return url;
    }

    public String getUser() {
        return user;
    }

    public String getDriver() {
        return driver;
    }
}

