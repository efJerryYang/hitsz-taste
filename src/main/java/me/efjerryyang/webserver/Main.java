package me.efjerryyang.webserver;

import me.efjerryyang.webserver.dao.DAOFactory;
import me.efjerryyang.webserver.dao.MySQLConnection;
import me.efjerryyang.webserver.model.Model;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Configuration;

import java.sql.Connection;
import java.util.ArrayList;

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
        var categoryDAO = daoFactory.getCategoryDAO();
        var contractDAO = daoFactory.getContractDAO();
        var discountDAO = daoFactory.getDiscountDAO();
        var dishDAO = daoFactory.getDishDAO();
        var dishDiscountDAO = daoFactory.getDishDiscountDAO();
        var merchantDAO = daoFactory.getMerchantDAO();
        var merchantUserDAO = daoFactory.getMerchantUserDAO();
        var orderDAO = daoFactory.getOrderDAO();
        var orderItemDAO = daoFactory.getOrderItemDAO();
        var reviewDAO = daoFactory.getReviewDAO();
        var roleDAO = daoFactory.getRoleDAO();
        var userRoleDAO = daoFactory.getUserRoleDAO();
        var userDAO = daoFactory.getUserDAO();


        var cafeteriaList = cafeteriaDAO.getAll();
        var categoryList = categoryDAO.getAll();
        var contractList = contractDAO.getAll();
        var discountList = discountDAO.getAll();
        var dishList = dishDAO.getAll();
        var dishDiscountList = dishDiscountDAO.getAll();
        var merchantList = merchantDAO.getAll();
        var merchantUserList = merchantUserDAO.getAll();
        var orderList = orderDAO.getAll();
        var orderItemList = orderItemDAO.getAll();
        var reviewList = reviewDAO.getAll();
        var roleList = roleDAO.getAll();
        var userRoleList = userRoleDAO.getAll();
        var userList = userDAO.getAll();

        var modelList = new ArrayList<Model>();
        modelList.addAll(cafeteriaList);
        modelList.addAll(categoryList);
        modelList.addAll(contractList);
        modelList.addAll(discountList);
        modelList.addAll(dishList);
        modelList.addAll(dishDiscountList);
        modelList.addAll(merchantList);
        modelList.addAll(merchantUserList);
        modelList.addAll(orderList);
        modelList.addAll(orderItemList);
        modelList.addAll(reviewList);
        modelList.addAll(roleList);
        modelList.addAll(userRoleList);
        modelList.addAll(userList);
        for (var model : modelList) {
            logger.info(model.toString());
        }
    }
}

