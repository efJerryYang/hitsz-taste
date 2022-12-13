package me.efjerryyang.webserver;

import me.efjerryyang.webserver.dao.DAOFactory;
import me.efjerryyang.webserver.dao.MySQLConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.*;

import java.io.IOException;
import java.sql.SQLException;

@Configuration
@ComponentScan(basePackages = {"me.efjerryyang.webserver"})
public class AppConfig {

    private static final Logger logger = LoggerFactory.getLogger(AppConfig.class);

    @Autowired
    private ApplicationProperties applicationProperties;

    @Bean
    @Scope("singleton")
    @Primary
    public DAOFactory daoFactory() throws SQLException, IOException {
        logger.debug("Creating DAO factory bean");
        return new DAOFactory(mysqlConnection());
    }

    @Bean
    @Scope("singleton")
    @Primary
    public MySQLConnection mysqlConnection() throws SQLException, IOException {
        logger.debug("Creating MySQL connection bean");
        return new MySQLConnection(applicationProperties);
    }

}
