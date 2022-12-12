package me.efjerryyang.webserver;

import me.efjerryyang.webserver.dao.MySQLConnection;
import me.efjerryyang.webserver.dao.UserDAO;
import me.efjerryyang.webserver.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Configuration;

import java.sql.Connection;

import static org.testng.AssertJUnit.assertNotNull;

@SpringBootApplication
@Configuration
public class Main {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);
    private static AnnotationConfigApplicationContext context;
    @Autowired
    private ApplicationProperties applicationProperties;
    @Autowired
    private MySQLConnection mySQLConnection;
    @Autowired
    private UserDAO userDAO;

    public static void main(String[] args) throws Exception {
//        SpringApplication.run(Main.class, args);
        Main main = new Main();
        main.run(args);
    }

    public void run(String... args) throws Exception {
        // Initialize the Spring Framework
        context = new AnnotationConfigApplicationContext();

        // Register the AppConfig class as a configuration class
        context.register(AppConfig.class);

        // Refresh the context to load the registered configuration class(es)
        context.refresh();

        // Get an instance of the MySQLConnection class using the Spring container
        mySQLConnection = context.getBean(MySQLConnection.class);
        // Create a connection to the MySQL server
        Connection conn = mySQLConnection.getConnection();

        assertNotNull(conn);

        userDAO = context.getBean(UserDAO.class);

        // Get a user by their ID
        var nUser = new User(20201212L, "Jerry Yang", "efjerryyang@outlook.com", "password", "17722603524", "Harbin Institute of Technology, Shenzhen", true);
        var tmp = userDAO.createUser(nUser);
        var userList = userDAO.getAllUsers();
        for (User user : userList) {
            // Print the user's information
            logger.info("User: " + user.getName() + " (" + user.getEmail() + ")");
            System.out.println(user);
    }
}
}

