package me.efjerryyang.webserver;

import me.efjerryyang.webserver.dao.MySQLConnection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import static org.testng.AssertJUnit.assertNotNull;


@Component
public class Main {

    @Autowired
    private ApplicationProperties applicationProperties;

    @Autowired
    private MySQLConnection mySQLConnection;

    public static void main(String[] args) throws SQLException, IOException {
        // Create a new instance of the Main class
        Main main = new Main();

        // Call the run() method to execute the code
        main.run();
    }

    public void run() throws SQLException, IOException {
        // Use the mySQLConnection object to get a connection to the MySQL server
        Connection connection = mySQLConnection.getConnection();
        // Use the connection to execute SQL statements
        // ...
        assertNotNull(connection);
    }
}
