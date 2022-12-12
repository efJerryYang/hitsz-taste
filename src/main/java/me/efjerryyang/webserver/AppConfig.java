package me.efjerryyang.webserver;

import me.efjerryyang.webserver.dao.MySQLConnection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

import java.io.IOException;
import java.sql.SQLException;

@Configuration
@ComponentScan(basePackages = {"me.efjerryyang.webserver"})
public class AppConfig {

    @Autowired
    private ApplicationProperties applicationProperties;

    @Bean
    public ApplicationProperties applicationProperties() {
        System.out.println("===================== In AppConfig.applicationProperties() =====================");
        return new ApplicationProperties();
    };

    @Bean
    public MySQLConnection mySQLConnection() throws SQLException, IOException {
        System.out.println("===================== In AppConfig.mySQLConnection() =====================");
        System.out.println("port: " + applicationProperties.getPort());
        System.out.println("servletContextPath: " + applicationProperties.getServletContextPath());
        System.out.println("driverClassName: " + applicationProperties.getDriverClassName());
        System.out.println("databasePlatform: " + applicationProperties.getDatabasePlatform());
        System.out.println("ddlAuto: " + applicationProperties.getDdlAuto());
        System.out.println("key: " + applicationProperties.getKey());
        System.out.println("iv: " + applicationProperties.getIv());
        System.out.println("databaseUrl: " + applicationProperties.getDatabaseUrl());
        System.out.println("databaseUsername: " + applicationProperties.getDatabaseUsername());
        System.out.println("databasePassword: " + applicationProperties.getDatabasePassword());
        MySQLConnection mySQLConnection = new MySQLConnection(applicationProperties);
        return mySQLConnection;
    }

}
