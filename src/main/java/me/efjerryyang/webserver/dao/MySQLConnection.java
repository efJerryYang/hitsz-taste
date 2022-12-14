package me.efjerryyang.webserver.dao;

import me.efjerryyang.webserver.ApplicationProperties;
import me.efjerryyang.webserver.util.CryptoUtilAES;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@Component
public class MySQLConnection {

    private static final Logger logger = LoggerFactory.getLogger(MySQLConnection.class);
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
        this.applicationProperties = applicationProperties;
        this.databaseDriver = applicationProperties.getDriverClassName();
        this.databaseUrl = applicationProperties.getDatabaseUrl();
        this.databaseUsername = applicationProperties.getDatabaseUsername();
        this.keyString = applicationProperties.getKey();
        this.iv = applicationProperties.getIv();
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
        logger.debug("Creating connection to database: {}", databaseUrl);
        if (connection == null) {
            try {
                String encryptedPassword = System.getenv("ENCRYPTED_PASSWORD");
                Class.forName(databaseDriver);
                connection = DriverManager.getConnection(databaseUrl, databaseUsername, decryptPassword(encryptedPassword));
                logger.debug("Connection to database {} successful", databaseUrl);
            } catch (ClassNotFoundException e) {
                logger.error("Failed to load JDBC driver class: {}", databaseDriver, e);
                e.printStackTrace();
            } catch (Exception e) {
                logger.error("Exception while creating connection to database: {}", databaseUrl, e);
                e.printStackTrace();
            }
        }
        return connection;
    }

    public String decryptPassword(String encryptedPassword) {
        CryptoUtilAES utilAES = new CryptoUtilAES(keyString, iv);
        return utilAES.decrypter(encryptedPassword);
    }

    public void close() throws SQLException {
        if (connection != null) {
            connection.close();
            connection = null;
        }
    }

}
