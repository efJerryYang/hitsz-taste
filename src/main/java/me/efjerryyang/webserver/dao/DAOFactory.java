package me.efjerryyang.webserver.dao;

import me.efjerryyang.webserver.dao.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.SQLException;

@Component
public class DAOFactory {
    private MySQLConnection conn;

    @Autowired
    public DAOFactory(MySQLConnection conn) {
        this.conn = conn;
    }

    public CafeteriaDAO getCafeteriaDAO() throws SQLException {
        return new CafeteriaDAO(conn);
    }

    public ContractDAO getContractDAO() throws SQLException {
        return new ContractDAO(conn);
    }

    public DiscountDAO getDiscountDAO() throws SQLException {
        return new DiscountDAO(conn);
    }

    public DishDAO getDishDAO() throws SQLException {
        return new DishDAO(conn);
    }

    public MerchantDAO getMerchantDAO() throws SQLException {
        return new MerchantDAO(conn);
    }

    public OrderDAO getOrderDAO() throws SQLException {
        return new OrderDAO(conn);
    }

    public OrderItemDAO getOrderItemDAO() throws SQLException {
        return new OrderItemDAO(conn);
    }

    public UserDAO getUserDAO() throws SQLException {
        return new UserDAO(conn);
    }
}
