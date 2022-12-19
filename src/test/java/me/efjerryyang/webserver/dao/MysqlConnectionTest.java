package me.efjerryyang.webserver.dao;

import me.efjerryyang.webserver.AppConfig;
import me.efjerryyang.webserver.ApplicationProperties;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Configuration;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


@SpringBootApplication
@Configuration
public class MysqlConnectionTest {
    private static MysqlConnection mysqlConnection;
    private static AnnotationConfigApplicationContext context;

    @BeforeAll
    public static void setup() throws IOException {
        // Initialize the Spring Framework
        context = new AnnotationConfigApplicationContext();

        // Register the AppConfig class as a configuration class
        context.register(AppConfig.class);

        // Refresh the context to load the registered configuration class(es)
        context.refresh();

        // Get an instance of the MysqlConnection class using the Spring container
        mysqlConnection = context.getBean(MysqlConnection.class);
    }

    @AfterAll
    public static void tearDown() {
        // Close the Spring Framework context
        context.close();
    }

    @Test
    public void testGetConnection() throws SQLException {
        // Use the MysqlConnection instance to get a connection to the database
        Connection connection = mysqlConnection.getConnection();

        // Check that the connection is not null
        assertNotNull(connection);
    }

    @Test
    public void testConstructor_invalidDriverClassName_throwsSQLException() {
        // Create a mock ApplicationProperties instance with an invalid driver class name
        ApplicationProperties applicationProperties = mock(ApplicationProperties.class);
        when(applicationProperties.getDriverClassName()).thenReturn("invalid.driver.class.Name");
        // Verify that the MysqlConnection constructor throws a SQLException when given an invalid driver class name
        assertThrows(SQLException.class, () -> new MysqlConnection(applicationProperties));
    }

    @Test
    public void testConstructor_validDriverClassName_doesNotThrowSQLException() throws SQLException {
        // Create a mock ApplicationProperties instance with a valid driver class name
        ApplicationProperties applicationProperties = mock(ApplicationProperties.class);
        when(applicationProperties.getDriverClassName()).thenReturn("com.mysql.cj.jdbc.Driver");

        // Verify that the MysqlConnection constructor does not throw a SQLException when given a valid driver class name
        assertDoesNotThrow(() -> new MysqlConnection(applicationProperties));
    }

    @Test
    public void testGetConnection_invalidDatabaseCredentials_throwsSQLException() throws SQLException, IOException {
        // Create a mock ApplicationProperties instance with invalid database credentials
        ApplicationProperties applicationProperties = mock(ApplicationProperties.class);
        when(applicationProperties.getDriverClassName()).thenReturn("com.mysql.cj.jdbc.Driver");
        when(applicationProperties.getDatabaseUrl()).thenReturn("jdbc:mysql://localhost:3306/invalid_database");
        when(applicationProperties.getDatabaseUsername()).thenReturn("invalid_username");

        // Create a MysqlConnection instance with the mock ApplicationProperties instance
        MysqlConnection mysqlConnection = new MysqlConnection(applicationProperties);

        // Verify that the getConnection method throws a SQLException when given invalid database credentials
        assertThrows(SQLException.class, mysqlConnection::getConnection);
    }


    @Test
    public void testClose_validConnection_closesConnection() throws SQLException, IOException {
        // Create a mock ApplicationProperties instance with a valid driver class name
        ApplicationProperties applicationProperties = mock(ApplicationProperties.class);
        when(applicationProperties.getDriverClassName()).thenReturn("com.mysql.cj.jdbc.Driver");

        // Create a MysqlConnection instance with the mock ApplicationProperties instance
        MysqlConnection mysqlConnection = new MysqlConnection(applicationProperties);

        // Get a connection to the database
        Connection connection = mysqlConnection.getConnection();

        // Verify that the connection is open
        assertFalse(connection.isClosed());
        // Close the connection to the database
        connection.close();

        // Verify that the connection is closed
        assertTrue(connection.isClosed());
    }


    @Test
    public void testConstructor_invalidDatabaseUrl_throwsSQLException() throws SQLException, IOException {
        // Create a mock ApplicationProperties instance with an invalid database URL
        ApplicationProperties applicationProperties = mock(ApplicationProperties.class);
        when(applicationProperties.getDatabaseUrl()).thenReturn("jdbc:mysql://localhost:3306/invalid_database");
        // Create a MysqlConnection instance with the mock ApplicationProperties instance
        MysqlConnection mysqlConnection = new MysqlConnection(applicationProperties);

        // Verify that the getConnection method throws a SQLException when given invalid database credentials
        assertThrows(SQLException.class, mysqlConnection::getConnection);

        // Verify that the correct error message is thrown
        try {
            mysqlConnection.getConnection();
        } catch (SQLException e) {
            assertEquals("Failed to create connection to database", e.getMessage());
        }

        // Clean up
        mysqlConnection.close();
    }

    @Test
    public void testGetConnection_invalidCredentials_throwsSQLException() throws InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException, SQLException, IOException {
// Create a mock ApplicationProperties instance with invalid database credentials
        ApplicationProperties applicationProperties = mock(ApplicationProperties.class);
        when(applicationProperties.getDatabaseUrl()).thenReturn("jdbc:mysql://localhost:3306/invalid_database");
        when(applicationProperties.getDatabaseUsername()).thenReturn("invalid_username");
        when(applicationProperties.getDatabasePassword()).thenReturn("invalid_password");

// Create a MysqlConnection instance with the mock ApplicationProperties instance
        MysqlConnection mysqlConnection = new MysqlConnection(applicationProperties);

// Verify that the getConnection method throws a SQLException when given invalid database credentials
        assertThrows(SQLException.class, mysqlConnection::getConnection);

    }

    @Test
    public void testGetConnection_validCredentials_returnsConnection() throws SQLException, IOException, InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
        // Create a mock ApplicationProperties instance with valid database credentials
        ApplicationProperties applicationProperties = mock(ApplicationProperties.class);
        when(applicationProperties.getDriverClassName()).thenReturn("com.mysql.cj.jdbc.Driver");
        when(applicationProperties.getDatabaseUrl()).thenReturn("jdbc:mysql://localhost:3306/hitsz_taste");
        when(applicationProperties.getDatabaseUsername()).thenReturn("hitsz_taste");
        when(applicationProperties.getDatabasePassword()).thenReturn("hitsz_taste_password");

        // Create a MysqlConnection instance with the mock ApplicationProperties instance
        MysqlConnection mysqlConnection = new MysqlConnection(applicationProperties);

        // Get a connection to the database
        Connection connection = mysqlConnection.getConnection();

        // Verify that the connection is open
        assertFalse(connection.isClosed());
    }
}


