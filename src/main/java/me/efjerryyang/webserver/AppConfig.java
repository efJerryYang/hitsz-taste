package me.efjerryyang.webserver;

import me.efjerryyang.webserver.dao.DAOFactory;
import me.efjerryyang.webserver.dao.MySQLConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.io.IOException;
import java.sql.SQLException;

@Configuration
@ComponentScan(basePackages = {"me.efjerryyang.webserver"})
public class AppConfig {

    private static Logger logger = LoggerFactory.getLogger(AppConfig.class);

    @Autowired
    private ApplicationProperties applicationProperties;

    @Autowired
    @Qualifier("mysqlConnection")
    private MySQLConnection mysqlConnection;

    @Bean
    @Primary
    public ApplicationProperties applicationProperties() {
        logger.debug("Creating application properties bean");
        return new ApplicationProperties();
    }

    @Bean
    @Primary
    public MySQLConnection mysqlConnection() throws SQLException, IOException {
        logger.debug("Creating MySQL connection bean");
        return new MySQLConnection(applicationProperties);
    }

    @Bean
    @Primary
    public DAOFactory daoFactory() {
        logger.debug("Creating DAO factory bean");
        return new DAOFactory(mysqlConnection);
    }

}
