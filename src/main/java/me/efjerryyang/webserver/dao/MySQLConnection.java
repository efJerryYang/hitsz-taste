package me.efjerryyang.webserver.dao;

import me.efjerryyang.webserver.ApplicationProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.sql.DataSource;
import java.io.IOException;
import java.security.Key;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Base64;


@Component
public class MySQLConnection {

    private final ApplicationProperties applicationProperties;

    private final String databaseDriver;

    private final String databaseUrl;

    private final String databaseUsername;

    private final String keyString;

    private final String iv;
    // The JDBC connection object
    private Connection connection;

    @Autowired
    public MySQLConnection(ApplicationProperties applicationProperties) throws IOException, SQLException {
        System.out.println("===================== In MySQLConnection <Constructor> =====================");
        this.applicationProperties = applicationProperties;
        this.databaseDriver = applicationProperties.getDriverClassName();
        System.out.println("databaseDriver: " + databaseDriver);

        this.databaseUrl = applicationProperties.getDatabaseUrl();
        System.out.println("databaseUrl: " + databaseUrl);

        this.databaseUsername = applicationProperties.getDatabaseUsername();
        System.out.println("databaseUsername: " + databaseUsername);

        this.keyString = applicationProperties.getKey();
        System.out.println("keyString: " + keyString);

        this.iv = applicationProperties.getIv();
        System.out.println("iv: " + iv);
        // Load the JDBC driver class
        try {
            Class.forName(this.databaseDriver);
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
            System.out.println("Creating connection...");
            try {
                String encryptedPassword = System.getenv("ENCRYPTED_PASSWORD");
                System.out.println("encryptedPassword: " + encryptedPassword);
                Class.forName(databaseDriver);
                connection = DriverManager.getConnection(databaseUrl, databaseUsername, decryptPassword(encryptedPassword));
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return connection;
    }

    public String decryptPassword(String encryptedPassword) throws Exception {


        byte[] keyBytes = Base64.getDecoder().decode(keyString);
        Key key = new SecretKeySpec(keyBytes, "AES");

        // Initialize the cipher in decrypt mode using the key and the specified IV and mode
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
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
