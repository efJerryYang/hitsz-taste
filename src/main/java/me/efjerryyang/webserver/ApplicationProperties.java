package me.efjerryyang.webserver;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Configuration
@PropertySource("classpath:application.properties")
@Service
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

    public ApplicationProperties(){
        System.out.println("===================== In ApplicationProperties <Constructor> =====================");
    }

    public int getPort() {
        return port;
    }

    public String getServletContextPath() {
        return servletContextPath;
    }

    public String getDriverClassName() {
        return driverClassName;
    }

    public String getDatabasePlatform() {
        return databasePlatform;
    }

    public String getDdlAuto() {
        return ddlAuto;
    }

    public String getKey() {
        return key;
    }

    public String getIv() {
        return iv;
    }

    public String getDatabaseUrl() {
        return databaseUrl;
    }

    public String getDatabaseUsername() {
        return databaseUsername;
    }

    public String getDatabasePassword() {
        return databasePassword;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public void setServletContextPath(String servletContextPath) {
        this.servletContextPath = servletContextPath;
    }

    public void setDriverClassName(String driverClassName) {
        this.driverClassName = driverClassName;
    }

    public void setDatabasePlatform(String databasePlatform) {
        this.databasePlatform = databasePlatform;
    }

    public void setDdlAuto(String ddlAuto) {
        this.ddlAuto = ddlAuto;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void setIv(String iv) {
        this.iv = iv;
    }

    public void setDatabaseUrl(String databaseUrl) {
        this.databaseUrl = databaseUrl;
    }

    public void setDatabaseUsername(String databaseUsername) {
        this.databaseUsername = databaseUsername;
    }

    public void setDatabasePassword(String databasePassword) {
        this.databasePassword = databasePassword;
    }
}
