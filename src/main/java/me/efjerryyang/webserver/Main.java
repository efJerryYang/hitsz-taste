package me.efjerryyang.webserver;

import me.efjerryyang.webserver.dao.MySQLConnection;
import org.apache.catalina.core.ApplicationContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import static org.testng.AssertJUnit.assertNotNull;

@SpringBootApplication
@Configuration
public class Main {
    public static void main(String[] args) throws SQLException {
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
