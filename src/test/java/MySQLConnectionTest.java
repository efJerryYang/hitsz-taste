import me.efjerryyang.webserver.ApplicationProperties;
import me.efjerryyang.webserver.dao.MySQLConnection;
import org.junit.jupiter.api.Test;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.Properties;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class MySQLConnectionTest {

    @Test
    public void testGetConnection() throws SQLException, IOException {
        // Load the properties file
        ApplicationProperties applicationProperties = new ApplicationProperties();
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream("application.properties");

        Properties properties = new Properties();
        properties.load(inputStream);

        // Print the properties object
        properties.list(System.out);
        System.out.println("=========================================================================================");
        // Get the keys in the properties object
        Enumeration<?> enumeration = properties.propertyNames();

        // Iterate over the keys
        while (enumeration.hasMoreElements()) {
            // Get the key
            String key = (String) enumeration.nextElement();

            // Get the value of the key
            String value = properties.getProperty(key);

            // Print the key and value
            System.out.println(key + ": " + value);
        }

//        System.exit(0);
        // Get the values of the properties that you need
//        String databaseDriver = properties.getProperty("database.driver");
//        String databaseUrl = properties.getProperty("spring.datasource.url");
//        String databaseUsername = properties.getProperty("spring.datasource.username");
//        String databasePassword = properties.getProperty("database.password");
//        String keyString = properties.getProperty("database.key");
//        String iv = properties.getProperty("database.iv");

        MySQLConnection conn = new MySQLConnection(
                applicationProperties.getDriverClassName(),
                applicationProperties.getDatabaseUrl(),
                applicationProperties.getDatabaseUsername(),
                applicationProperties.getDatabasePassword(),
                applicationProperties.getKey(),
                applicationProperties.getIv()
//                databaseDriver, databaseUrl, databaseUsername, databasePassword, keyString, iv
        );
        Connection connection = conn.getConnection();
        assertNotNull(connection);
        // Test that the connection is closed after calling the close method
//        conn.close();
//        assertTrue(conn.getConnection().isClosed());
//
//        // Test that a new connection can be established after closing the old one
//        conn = new MySQLConnection();
//        connection = conn.getConnection();
//        assertNotNull(connection);
    }

}

