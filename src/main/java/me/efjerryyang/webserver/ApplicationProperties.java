package me.efjerryyang.webserver;

import me.efjerryyang.webserver.util.CryptoUtilAES;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
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

    public ApplicationProperties() {
        System.out.println("===================== In ApplicationProperties <Constructor> =====================");
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getServletContextPath() {
        return servletContextPath;
    }

    public void setServletContextPath(String servletContextPath) {
        this.servletContextPath = servletContextPath;
    }

    public String getDriverClassName() {
        return driverClassName;
    }

    public void setDriverClassName(String driverClassName) {
        this.driverClassName = driverClassName;
    }

    public String getDatabasePlatform() {
        return databasePlatform;
    }

    public void setDatabasePlatform(String databasePlatform) {
        this.databasePlatform = databasePlatform;
    }

    public String getDdlAuto() {
        return ddlAuto;
    }

    public void setDdlAuto(String ddlAuto) {
        this.ddlAuto = ddlAuto;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getIv() {
        return iv;
    }

    public void setIv(String iv) {
        this.iv = iv;
    }

    public String getDatabaseUrl() {
        return databaseUrl;
    }

    public void setDatabaseUrl(String databaseUrl) {
        this.databaseUrl = databaseUrl;
    }

    public String getDatabaseUsername() {
        return databaseUsername;
    }

    public void setDatabaseUsername(String databaseUsername) {
        this.databaseUsername = databaseUsername;
    }

    public String getDatabasePassword() {
        // Should only be called in Unit Test
        String encryptedPassword = System.getenv("ENCRYPTED_PASSWORD");
        CryptoUtilAES utilAES = new CryptoUtilAES(key, iv);
        return utilAES.decrypter(encryptedPassword);
    }
} // ApplicationProperties.java
