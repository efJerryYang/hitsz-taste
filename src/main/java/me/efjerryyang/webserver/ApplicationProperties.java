package me.efjerryyang.webserver;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:application.properties")
public class ApplicationProperties {
    @Value("${server.port}")
    private int port;

    @Value("${server.servlet.context-path}")
    private String servletContextPath;

    @Value("${spring.datasource.driver-class-name}")
    private String driverClassName;

    @Value("${spring.jpa.database-platform}")
    private String databasePlatform;

    @Value("${spring.jpa.hibernate.ddl-auto}")
    private String ddlAuto;

    @Value("${spring.datasource.key}")
    private String key;

    @Value("${spring.datasource.iv}")
    private String iv;

    @Value("${spring.datasource.url}")
    private String databaseUrl;

    @Value("${spring.datasource.username}")
    private String databaseUsername;

    @Value("${spring.datasource.password}")
    private String databasePassword;

    public int getPort() {
        System.out.println("port: " + port);
        return port;
    }

    public String getServletContextPath() {
        System.out.println("servletContextPath: " + servletContextPath);
        return servletContextPath;
    }

    public String getDriverClassName() {
        System.out.println("driverClassName: " + driverClassName);
        return driverClassName;
    }

    public String getDatabasePlatform() {
        System.out.println("databasePlatform: " + databasePlatform);
        return databasePlatform;
    }

    public String getDdlAuto() {
        System.out.println("ddlAuto: " + ddlAuto);
        return ddlAuto;
    }

    public String getKey() {
        System.out.println("key: " + key);
        return key;
    }

    public String getIv() {
        System.out.println("iv: " + iv);
        return iv;
    }

    public String getDatabaseUrl() {
        System.out.println("databaseUrl: " + databaseUrl);
        return databaseUrl;
    }

    public String getDatabaseUsername() {
        System.out.println("databaseUsername: " + databaseUsername);
        return databaseUsername;
    }

    public String getDatabasePassword() {
        System.out.println("databasePassword: " + databasePassword);
        return databasePassword;
    }

}
