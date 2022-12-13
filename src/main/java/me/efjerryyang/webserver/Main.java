package me.efjerryyang.webserver;

import me.efjerryyang.webserver.dao.DAOFactory;
import me.efjerryyang.webserver.dao.MySQLConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Configuration;

import java.sql.Connection;

@SpringBootApplication
@Configuration
public class Main {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);
    private static AnnotationConfigApplicationContext context;

    @Autowired
    private ApplicationProperties applicationProperties;

    @Autowired
    private MySQLConnection mysqlConnection;

    @Autowired
    private DAOFactory daoFactory;

    public static void main(String[] args) throws Exception {
//        SpringApplication.run(Main.class, args);
        Main main = new Main();
        main.run(args);
    }

    public void run(String... args) throws Exception {
        // Initialize the Spring Framework
        context = new AnnotationConfigApplicationContext();

        // Register the AppConfig class as a configuration class
        context.register(AppConfig.class);

        // Refresh the context to load the registered configuration class(es)
        context.refresh();

        // Get an instance of the MySQLConnection class using the Spring container
        mysqlConnection = context.getBean(MySQLConnection.class);
        // Create a connection to the MySQL server
        Connection conn = mysqlConnection.getConnection();

        daoFactory = context.getBean(DAOFactory.class);
        var cafeteriaDAO = daoFactory.getCafeteriaDAO();
        var contractDAO = daoFactory.getContractDAO();
        var dishDAO = daoFactory.getDishDAO();
        var discountDAO = daoFactory.getDiscountDAO();
        var merchantDAO = daoFactory.getMerchantDAO();
        var orderDAO = daoFactory.getOrderDAO();
        var orderItemDAO = daoFactory.getOrderItemDAO();
        var userDAO = daoFactory.getUserDAO();

        var cafeteriaList = cafeteriaDAO.getAll();
        var contractList = contractDAO.getAll();
        var dishList = dishDAO.getAll();
        var discountList = discountDAO.getAll();
        var merchantList = merchantDAO.getAll();
        var orderList = orderDAO.getAll();
        var orderItemList = orderItemDAO.getAll();
        var userList = userDAO.getAll();

        for (var cafeteria : cafeteriaList) {
            System.out.println(cafeteria);
        }
        for (var contract : contractList) {
            System.out.println(contract);
        }
        for (var dish : dishList) {
            System.out.println(dish);
        }
        for (var discount : discountList) {
            System.out.println(discount);
        }
        for (var merchant : merchantList) {
            System.out.println(merchant);
        }
        for (var order : orderList) {
            System.out.println(order);
        }
        for (var orderItem : orderItemList) {
            System.out.println(orderItem);
        }
        for (var user : userList) {
            System.out.println(user);
        }

        logger.info("Closing connection");
    }
}

