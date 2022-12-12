package me.efjerryyang.webserver;

import lombok.Data;
import lombok.ToString;
import me.efjerryyang.webserver.util.CryptoUtilAES;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

@Data
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

    public String getDatabasePassword() {
        // Should only be called in Unit Test
        String encryptedPassword = System.getenv("ENCRYPTED_PASSWORD");
        CryptoUtilAES utilAES = new CryptoUtilAES(key, iv);
        return utilAES.decrypter(encryptedPassword);
    }
} // ApplicationProperties.java
