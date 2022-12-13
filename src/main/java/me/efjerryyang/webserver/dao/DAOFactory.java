package me.efjerryyang.webserver.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.SQLException;

@Component
public class DAOFactory {
    private MySQLConnection mysqlConnection;

    @Autowired
    public DAOFactory(MySQLConnection mysqlConnection) {
        this.mysqlConnection = mysqlConnection;
    }

    public CafeteriaDAO getCafeteriaDAO() throws SQLException {
        return new CafeteriaDAO(mysqlConnection);
    }

    public ContractDAO getContractDAO() throws SQLException {
        return new ContractDAO(mysqlConnection);
    }

    public DiscountDAO getDiscountDAO() throws SQLException {
        return new DiscountDAO(mysqlConnection);
    }

    public DishDAO getDishDAO() throws SQLException {
        return new DishDAO(mysqlConnection);
    }

    public MerchantDAO getMerchantDAO() throws SQLException {
        return new MerchantDAO(mysqlConnection);
    }

    public OrderDAO getOrderDAO() throws SQLException {
        return new OrderDAO(mysqlConnection);
    }

    public OrderItemDAO getOrderItemDAO() throws SQLException {
        return new OrderItemDAO(mysqlConnection);
    }

    public UserDAO getUserDAO() throws SQLException {
        return new UserDAO(mysqlConnection);
    }
}
