package me.efjerryyang.webserver.dao;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.sql.DataSource;
import java.io.FileInputStream;
import java.security.Key;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Base64;
import java.util.Properties;


@Component
public class MySQLConnection {

    @Value("${database.driver}")
    private String databaseDriver;

    @Value("${database.url}")
    private String databaseUrl;

    @Value("${database.username}")
    private String databaseUsername;

    @Value("${database.password}")
    private String encryptedPassword;

    @Value("${database.")

    // The JDBC connection object
    private Connection connection;

    public MySQLConnection() {
        // Load the JDBC driver class
        try {
            Class.forName(databaseDriver);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public Connection getConnection(DataSource dataSource) throws SQLException {
        if (connection == null) {
            connection = dataSource.getConnection();
        }
        return connection;
    }

    public Connection getConnection() throws SQLException {

        if (connection == null) {
            try {
                String encryptedPassword = System.getenv("ENCRYPTED_PASSWORD");
                String decryptedPassword = decryptPassword(encryptedPassword);
                Class.forName(databaseDriver);
                connection = DriverManager.getConnection(
                        databaseUrl, databaseUsername, decryptedPassword
                );
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return connection;
    }

    public String decryptPassword(String encryptedPassword) throws Exception {
        // Read the key from the application.properties file
        // Create a new Properties instance

        Properties properties = new Properties();

        // Load the properties file
        properties.load(new FileInputStream("application.properties"));

        // Get the value of the "database.password" property
        String keyString = properties.getProperty("database.key");
        String iv = properties.getProperty("database.iv");

        byte[] keyBytes = Base64.getDecoder().decode(keyString);
        Key key = new SecretKeySpec(keyBytes, "AES");

        // Initialize the cipher in decrypt mode using the key and the specified IV and mode
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding");
        cipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(Base64.getDecoder().decode(iv)));

        // Decrypt the password using the cipher
        byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(encryptedPassword));

        // Return the decrypted password as a string
        return new String(decryptedBytes);
    }

    public void close() throws SQLException {
        if (connection != null) {
            connection.close();
            connection = null;
        }
    }

}
