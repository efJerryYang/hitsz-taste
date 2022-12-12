package me.efjerryyang.webserver.dao;

import me.efjerryyang.webserver.dao.MySQLConnection;
import me.efjerryyang.webserver.model.OrderItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


@Repository
public class OrderItemDAO {
    private static final Logger logger = LoggerFactory.getLogger(OrderItemDAO.class);
    private MySQLConnection mysqlConnection;
    private Connection connection;

    @Autowired
    public OrderItemDAO(MySQLConnection mySQLConnection) throws SQLException {
        this.mysqlConnection = mySQLConnection;
        logger.debug("Obtaining connection to database");
        this.connection = mySQLConnection.getConnection();
    }

    public OrderItem createOrderItem(OrderItem orderItem) {
        logger.info("Creating order item with order_id {], dish_id {} and quantity {}", orderItem.getOrderId(), orderItem.getDishId(), orderItem.getQuantity());
        String sql = "INSERT INTO order_items (order_id, dish_id, quantity) VALUES (?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setObject(1, orderItem.getOrderId());
            statement.setObject(2, orderItem.getDishId());
            statement.setObject(3, orderItem.getQuantity());
            statement.executeUpdate();
            // No single candidate key, so we need to get the generated key from the result set
            ResultSet resultSet = statement.getGeneratedKeys();
            logger.info("Successfully created order item with order_id {], dish_id {} and quantity {}", orderItem.getOrderId(), orderItem.getDishId(), orderItem.getQuantity());
            return orderItem;
        } catch (SQLException e) {
            logger.error("Error creating order item in database", e);
            return null;
        }
    }

    public OrderItem updateOrderItem(OrderItem orderItemOld, OrderItem orderItemNew) {
        logger.info("Updating order item with order_id {], dish_id {} and quantity {}", orderItemOld.getOrderId(), orderItemOld.getDishId(), orderItemOld.getQuantity());
        String sql = "UPDATE order_items SET order_id = ?, dish_id = ?, quantity = ? WHERE order_id = ? AND dish_id = ? AND quantity = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setObject(1, orderItemNew.getOrderId());
            statement.setObject(2, orderItemNew.getDishId());
            statement.setObject(3, orderItemNew.getQuantity());
            statement.setObject(4, orderItemOld.getOrderId());
            statement.setObject(5, orderItemOld.getDishId());
            statement.setObject(6, orderItemOld.getQuantity());
            statement.executeUpdate();
            logger.info("Successfully updated order item with order_id {], dish_id {} and quantity {}", orderItemOld.getOrderId(), orderItemOld.getDishId(), orderItemOld.getQuantity());
            return orderItemNew;
        } catch (SQLException e) {
            logger.error("Error updating order item in database", e);
            return null;
        }

    }

    private OrderItem getOrderItemFromResultSet(ResultSet resultSet) throws SQLException {
        OrderItem orderItem = new OrderItem();
        orderItem.setOrderId(resultSet.getLong("order_id"));
        orderItem.setDishId(resultSet.getLong("dish_id"));
        orderItem.setQuantity(resultSet.getLong("quantity"));
        return orderItem;
    }

    public List<OrderItem> getAllOrderItems() {
        logger.info("Getting all order items");
        String sql = "SELECT * FROM order_items";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            ResultSet resultSet = statement.executeQuery();
            List<OrderItem> orderItems = new ArrayList<>();
            while (resultSet.next()) {
                orderItems.add(getOrderItemFromResultSet(resultSet));
            }
            logger.info("Successfully retrieved all order items");
            return orderItems;
        } catch (SQLException e) {
            logger.error("Error getting all order items from database", e);
            return null;
        }
    }

    public void deleteAllOrderItems() {
        logger.info("Deleting all order items");
        String sql = "DELETE FROM order_items";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.executeUpdate();
            logger.info("Successfully deleted all order items");
        } catch (SQLException e) {
            logger.error("Error deleting all order items from database", e);
        }
    }

    public OrderItem deleteOrderItemByCandidateKey(Long orderId, Long dishId, Long quantity) {
        logger.info("Deleting order item with order_id {], dish_id {} and quantity {}", orderId, dishId, quantity);
        String sql = "DELETE FROM order_items WHERE order_id = ? AND dish_id = ? AND quantity = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setObject(1, orderId);
            statement.setObject(2, dishId);
            statement.setObject(3, quantity);
            statement.executeUpdate();
            logger.info("Successfully deleted order item with order_id {], dish_id {} and quantity {}", orderId, dishId, quantity);
            return new OrderItem(orderId, dishId, quantity);
        } catch (SQLException e) {
            logger.error("Error deleting order item from database", e);
            return null;
        }
    }

    public Boolean orderItemExists(Long orderId, Long dishId, Long quantity) {
        logger.info("Checking if order item with order_id {], dish_id {} and quantity {} exists", orderId, dishId, quantity);
        String sql = "SELECT * FROM order_items WHERE order_id = ? AND dish_id = ? AND quantity = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setObject(1, orderId);
            statement.setObject(2, dishId);
            statement.setObject(3, quantity);
            ResultSet resultSet = statement.executeQuery();
            Boolean exists = resultSet.next();
            logger.info("Successfully checked if order item with order_id {], dish_id {} and quantity {} exists", orderId, dishId, quantity);
            return exists;
        } catch (SQLException e) {
            logger.error("Error checking if order item exists in database", e);
            return null;
        }
    }
}
