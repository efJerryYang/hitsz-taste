package me.efjerryyang.webserver;

import me.efjerryyang.webserver.dao.DAOFactory;
import me.efjerryyang.webserver.dao.MysqlConnection;
import me.efjerryyang.webserver.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

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

    @Autowired
    private UserService userService;

}
