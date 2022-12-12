package me.efjerryyang.webserver;

import me.efjerryyang.webserver.dao.MySQLConnection;
import me.efjerryyang.webserver.dao.UserDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.sql.SQLException;

@Configuration
@ComponentScan(basePackages = {"me.efjerryyang.webserver"})
public class AppConfig {

    @Autowired
    private ApplicationProperties applicationProperties;

    @Autowired
    private MySQLConnection mySQLConnection;

    @Bean
    public ApplicationProperties applicationProperties() {
        return new ApplicationProperties();
    }

    @Bean
    public MySQLConnection mySQLConnection() throws SQLException, IOException {
        return new MySQLConnection(applicationProperties);
    }

    @Bean
    public UserDAO userDAO() throws SQLException {
        return new UserDAO(mySQLConnection);
    }

}
