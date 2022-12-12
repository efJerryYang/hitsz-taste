package me.efjerryyang.webserver;

import me.efjerryyang.webserver.dao.MySQLConnection;
import me.efjerryyang.webserver.dao.UserDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.sql.SQLException;

@Configuration
@ComponentScan(basePackages = {"me.efjerryyang.webserver"})
public class AppConfig {

    private static Logger logger = LoggerFactory.getLogger(AppConfig.class);

    @Autowired
    private ApplicationProperties applicationProperties;

    @Autowired
    private MySQLConnection mySQLConnection;

    @Bean
    public ApplicationProperties applicationProperties() {
        logger.debug("Creating application properties bean");
        return new ApplicationProperties();
    }

    @Bean
    public MySQLConnection mySQLConnection() throws SQLException, IOException {
        logger.debug("Creating MySQL connection bean");
        return new MySQLConnection(applicationProperties);
    }

    @Bean
    public UserDAO userDAO() throws SQLException {
        logger.debug("Creating user DAO bean");
        return new UserDAO(mySQLConnection);
    }

}
