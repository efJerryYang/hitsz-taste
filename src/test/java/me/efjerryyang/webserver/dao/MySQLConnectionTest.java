package me.efjerryyang.webserver.dao;

import me.efjerryyang.webserver.AppConfig;
import me.efjerryyang.webserver.ApplicationProperties;
import me.efjerryyang.webserver.dao.MySQLConnection;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.testng.AssertJUnit;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertNotNull;


@SpringBootApplication
@Configuration
public class MySQLConnectionTest {

    @Test
    public void testGetConnection() throws SQLException, IOException {
        // Initialize the Spring Framework
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();

        // Register the AppConfig class as a configuration class
        context.register(AppConfig.class);

        // Refresh the context to load the registered configuration class(es)
        context.refresh();

        // Get an instance of the MySQLConnection class using the Spring container
        MySQLConnection mySqlConnection = context.getBean(MySQLConnection.class);

        // Use the MySQLConnection instance to get a connection to the database
        Connection connection = mySqlConnection.getConnection();

        // Do something with the connection...
        assertNotNull(connection);
        // Close the Spring Framework context
        context.close();

    }
}
