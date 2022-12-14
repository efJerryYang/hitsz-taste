package me.efjerryyang.webserver;

import me.efjerryyang.webserver.dao.*;
import me.efjerryyang.webserver.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Configuration;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

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
        SpringApplication.run(Main.class, args);
//        Main main = new Main();
//        main.run(args);
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
        CafeteriaDAO cafeteriaDAO = daoFactory.getCafeteriaDAO();
        CategoryDAO categoryDAO = daoFactory.getCategoryDAO();
        ContractDAO contractDAO = daoFactory.getContractDAO();
        DiscountDAO discountDAO = daoFactory.getDiscountDAO();
        DishDAO dishDAO = daoFactory.getDishDAO();
        DishDiscountDAO dishDiscountDAO = daoFactory.getDishDiscountDAO();
        MerchantDAO merchantDAO = daoFactory.getMerchantDAO();
        MerchantUserDAO merchantUserDAO = daoFactory.getMerchantUserDAO();
        OrderDAO orderDAO = daoFactory.getOrderDAO();
        OrderItemDAO orderItemDAO = daoFactory.getOrderItemDAO();
        ReviewDAO reviewDAO = daoFactory.getReviewDAO();
        RoleDAO roleDAO = daoFactory.getRoleDAO();
        UserRoleDAO userRoleDAO = daoFactory.getUserRoleDAO();
        UserDAO userDAO = daoFactory.getUserDAO();


        List<Cafeteria> cafeteriaList = cafeteriaDAO.getAll();
        List<Category> categoryList = categoryDAO.getAll();
        List<Contract> contractList = contractDAO.getAll();
        List<Discount> discountList = discountDAO.getAll();
        List<Dish> dishList = dishDAO.getAll();
        List<DishDiscount> dishDiscountList = dishDiscountDAO.getAll();
        List<Merchant> merchantList = merchantDAO.getAll();
        List<MerchantUser> merchantUserList = merchantUserDAO.getAll();
        List<Order> orderList = orderDAO.getAll();
        List<OrderItem> orderItemList = orderItemDAO.getAll();
        List<Review> reviewList = reviewDAO.getAll();
        List<Role> roleList = roleDAO.getAll();
        List<UserRole> userRoleList = userRoleDAO.getAll();
        List<User> userList = userDAO.getAll();

        List<IModel> modelList = new ArrayList<IModel>();
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
        for (IModel model : modelList) {
            logger.info(model.toString());
        }

//        UserController userController = context.getBean(UserController.class);
//        userController.login("555-555-1212", "p@ssw0rd");
//        userController.login("555-555-1213", "password");
    }
}

