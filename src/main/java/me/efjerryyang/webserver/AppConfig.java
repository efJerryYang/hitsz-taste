package me.efjerryyang.webserver;

import me.efjerryyang.webserver.dao.MySQLConnection;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.sql.SQLException;

@Configuration
@ComponentScan(basePackages = {"me.efjerryyang.webserver"})
public class AppConfig {
    @Bean
    public ApplicationProperties applicationProperties() {
        return new ApplicationProperties();
    }

    @Bean
    public MySQLConnection mySQLConnection() throws SQLException, IOException {
        ApplicationProperties applicationProperties = new ApplicationProperties();
        return new MySQLConnection(applicationProperties);
    }
}
