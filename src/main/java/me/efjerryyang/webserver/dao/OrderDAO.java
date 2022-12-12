package me.efjerryyang.webserver.dao;

import me.efjerryyang.webserver.dao.MySQLConnection;
import me.efjerryyang.webserver.model.Order;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class OrderDAO {
    private static final Logger logger = LoggerFactory.getLogger(OrderDAO.class);
    private MySQLConnection mysqlConnection;
    private Connection connection;

    @Autowired
    public OrderDAO(MySQLConnection mySQLConnection) throws SQLException {
        this.mysqlConnection = mySQLConnection;
        logger.debug("Obtaining connection to database");
        this.connection = mySQLConnection.getConnection();
    }

    public Order createOrder(Order order) {
        logger.info("Creating order with order_id {} and user_id {}", order.getOrderId(), order.getUserId());
        String sql = "INSERT INTO orders (order_id, name) VALUES (?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setObject(1, order.getOrderId());
            statement.setObject(2, order.getUserId());
            statement.setObject(3, order.getCafeteriaId());
            statement.setObject(4, order.getPrice());
            statement.setObject(5, order.getOrderDate());
            statement.executeUpdate();
            ResultSet resultSet = statement.getGeneratedKeys();
            if (resultSet.next()) {
                order.setOrderId(resultSet.getLong(1));
            }
            logger.info("Successfully created order with id {}", order.getOrderId());
            return order;
        } catch (SQLException e) {
            logger.error("Error creating order in database", e);
            return null;
        }
    }

    public Order updateOrder(Order order) {
        logger.info("Updating order with order_id {}, user_id {}, cafeteria_id {}",
                order.getOrderId(), order.getUserId(), order.getCafeteriaId());
        String sql = "UPDATE orders SET user_id = ?, cafeteria_id = ?, price = ?, order_date = ? WHERE order_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            // Set the values for the order in the SQL statement
            statement.setObject(1, order.getUserId());
            statement.setObject(2, order.getCafeteriaId());
            statement.setObject(3, order.getPrice());
            statement.setObject(4, order.getOrderDate());
            statement.setObject(5, order.getOrderId());
            statement.executeUpdate();
            logger.info("Successfully updated order with id {}", order.getOrderId());
            return order;
        } catch (SQLException e) {
            logger.error("Error updating order in database", e);
            return null;
        }
    }

    private Order getOrderFromResultSet(ResultSet resultSet) throws SQLException {
        Order order = new Order();
        order.setOrderId(resultSet.getLong("order_id"));
        order.setUserId(resultSet.getLong("user_id"));
        order.setCafeteriaId(resultSet.getLong("cafeteria_id"));
        order.setPrice(resultSet.getFloat("price"));
        order.setOrderDate(resultSet.getDate("order_date"));
        return order;
    }

    public List<Order> getAllOrders() {
        logger.info("Getting all orders");
        String sql = "SELECT * FROM orders";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            ResultSet resultSet = statement.executeQuery();
            List<Order> orders = new ArrayList<>();
            while (resultSet.next()) {
                orders.add(getOrderFromResultSet(resultSet));
            }
            logger.info("Successfully retrieved all orders");
            return orders;
        } catch (SQLException e) {
            logger.error("Error retrieving all orders from database", e);
            return null;
        }
    }

    public void deleteAllOrders() {
        logger.info("Deleting all orders");
        String sql = "DELETE FROM orders";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.executeUpdate();
            logger.info("Successfully deleted all orders");
        } catch (SQLException e) {
            logger.error("Error deleting all orders from database", e);
        }
    }

    public List<Order> getOrderByDateRange(Date startDate, Date endDate){
        logger.info("Getting orders between {} and {}", startDate, endDate);
        String sql = "SELECT * FROM orders WHERE order_date BETWEEN ? AND ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setObject(1, startDate);
            statement.setObject(2, endDate);
            ResultSet resultSet = statement.executeQuery();
            List<Order> orders = new ArrayList<>();
            while (resultSet.next()) {
                orders.add(getOrderFromResultSet(resultSet));
            }
            logger.info("Successfully retrieved orders between {} and {}", startDate, endDate);
            return orders;
        } catch (SQLException e) {
            logger.error("Error retrieving orders between {} and {} from database", startDate, endDate, e);
            return null;
        }
    }

    public List<Order> getOrderByUserId(Long userId){
        logger.info("Getting orders for user with id {}", userId);
        String sql = "SELECT * FROM orders WHERE user_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setObject(1, userId);
            ResultSet resultSet = statement.executeQuery();
            List<Order> orders = new ArrayList<>();
            while (resultSet.next()) {
                orders.add(getOrderFromResultSet(resultSet));
            }
            logger.info("Successfully retrieved orders for user with id {}", userId);
            return orders;
        } catch (SQLException e) {
            logger.error("Error retrieving orders for user with id {} from database", userId, e);
            return null;
        }
    }

    public List<Order> getOrderByCafeteriaId(Long cafeteriaId){
        logger.info("Getting orders for cafeteria with id {}", cafeteriaId);
        String sql = "SELECT * FROM orders WHERE cafeteria_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setObject(1, cafeteriaId);
            ResultSet resultSet = statement.executeQuery();
            List<Order> orders = new ArrayList<>();
            while (resultSet.next()) {
                orders.add(getOrderFromResultSet(resultSet));
            }
            logger.info("Successfully retrieved orders for cafeteria with id {}", cafeteriaId);
            return orders;
        } catch (SQLException e) {
            logger.error("Error retrieving orders for cafeteria with id {} from database", cafeteriaId, e);
            return null;
        }
    }

    public List<Order> getOrderByCafeteriaIdAndDateRange(Long cafeteriaId, Date startDate, Date endDate){
        logger.info("Getting orders for cafeteria with id {} between {} and {}", cafeteriaId, startDate, endDate);
        String sql = "SELECT * FROM orders WHERE cafeteria_id = ? AND order_date BETWEEN ? AND ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setObject(1, cafeteriaId);
            statement.setObject(2, startDate);
            statement.setObject(3, endDate);
            ResultSet resultSet = statement.executeQuery();
            List<Order> orders = new ArrayList<>();
            while (resultSet.next()) {
                orders.add(getOrderFromResultSet(resultSet));
            }
            logger.info("Successfully retrieved orders for cafeteria with id {} between {} and {}", cafeteriaId, startDate, endDate);
            return orders;
        } catch (SQLException e) {
            logger.error("Error retrieving orders for cafeteria with id {} between {} and {} from database", cafeteriaId, startDate, endDate, e);
            return null;
        }
    }

    public List<Order> getOrderByUserIdAndDateRange(Long userId, Date startDate, Date endDate){
        logger.info("Getting orders for user with id {} between {} and {}", userId, startDate, endDate);
        String sql = "SELECT * FROM orders WHERE user_id = ? AND order_date BETWEEN ? AND ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setObject(1, userId);
            statement.setObject(2, startDate);
            statement.setObject(3, endDate);
            ResultSet resultSet = statement.executeQuery();
            List<Order> orders = new ArrayList<>();
            while (resultSet.next()) {
                orders.add(getOrderFromResultSet(resultSet));
            }
            logger.info("Successfully retrieved orders for user with id {} between {} and {}", userId, startDate, endDate);
            return orders;
        } catch (SQLException e) {
            logger.error("Error retrieving orders for user with id {} between {} and {} from database", userId, startDate, endDate, e);
            return null;
        }
    }

    public List<Order> getOrderByUserIdAndCafeteriaId(Long userId, Long cafeteriaId){
        logger.info("Getting orders for user with id {} and cafeteria with id {}", userId, cafeteriaId);
        String sql = "SELECT * FROM orders WHERE user_id = ? AND cafeteria_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setObject(1, userId);
            statement.setObject(2, cafeteriaId);
            ResultSet resultSet = statement.executeQuery();
            List<Order> orders = new ArrayList<>();
            while (resultSet.next()) {
                orders.add(getOrderFromResultSet(resultSet));
            }
            logger.info("Successfully retrieved orders for user with id {} and cafeteria with id {}", userId, cafeteriaId);
            return orders;
        } catch (SQLException e) {
            logger.error("Error retrieving orders for user with id {} and cafeteria with id {} from database", userId, cafeteriaId, e);
            return null;
        }
    }

}