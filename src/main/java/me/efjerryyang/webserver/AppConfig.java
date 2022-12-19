package me.efjerryyang.webserver;

import me.efjerryyang.webserver.controller.ExampleController;
import me.efjerryyang.webserver.dao.DAOFactory;
import me.efjerryyang.webserver.dao.MysqlConnection;
import me.efjerryyang.webserver.service.UserService;
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
    private MysqlConnection mysqlConnection;

    @Autowired
    private ApplicationProperties applicationProperties;

    @Autowired
    private DAOFactory daoFactory;


//    @Bean
//    @Scope("singleton")
//    @Primary
//    public MysqlConnection mysqlConnection() throws SQLException, IOException {
//        logger.debug("Creating MySQL connection bean");
//        return new MysqlConnection(applicationProperties);
//    }

    @Bean
    @Scope("singleton")
    @Primary
    public DAOFactory daoFactory() {
        logger.debug("Creating DAO factory bean");
        return new DAOFactory(mysqlConnection);
    }

    @Bean
    @Scope("singleton")
    @Primary
    public UserService userService() throws SQLException {
        logger.debug("Creating User service bean");
        return new UserService(daoFactory.getUserDAO());
    }

//    @Bean
//    @Scope("singleton")
//    @Primary
//    public ExampleController exampleController() throws SQLException {
//        logger.debug("Creating Example controller bean");
//        return new ExampleController(daoFactory.getUserDAO());
//    }

}
