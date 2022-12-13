package me.efjerryyang.webserver.dao;

import me.efjerryyang.webserver.model.OrderItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


@Repository
public class OrderItemDAO implements DAO<OrderItem> {
    private static final Logger logger = LoggerFactory.getLogger(OrderItemDAO.class);
    private final MySQLConnection mysqlConnection;
    private final Connection connection;

    @Autowired
    public OrderItemDAO(MySQLConnection mysqlConnection) throws SQLException {
        this.mysqlConnection = mysqlConnection;
        logger.debug("Obtaining connection to database");
        this.connection = mysqlConnection.getConnection();
    }

    @Override
    public OrderItem create(OrderItem orderItem) {
        logger.info("Creating order item with order_id {}, dish_id {} and quantity {}", orderItem.getOrderId(), orderItem.getDishId(), orderItem.getQuantity());
        String sql = "INSERT INTO hitsz_taste.order_items (order_id, dish_id, quantity) VALUES (?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setObject(1, orderItem.getOrderId());
            statement.setObject(2, orderItem.getDishId());
            statement.setObject(3, orderItem.getQuantity());
            statement.executeUpdate();
            // No single candidate key, so we need to get the generated key from the result set
            ResultSet resultSet = statement.getGeneratedKeys();

            logger.debug("Successfully created order item with order_id {}, dish_id {} and quantity {}", orderItem.getOrderId(), orderItem.getDishId(), orderItem.getQuantity());
            return orderItem;
        } catch (SQLException e) {
            logger.error("Error creating order item in database", e);
            return null;
        }
    }

    @Override
    public OrderItem update(OrderItem orderItem) {
        logger.info("Updating order item with order_id {}, dish_id {} and quantity {}", orderItem.getOrderId(), orderItem.getDishId(), orderItem.getQuantity());
        String sql = "UPDATE hitsz_taste.order_items SET quantity = ? WHERE order_id = ? AND dish_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setObject(1, orderItem.getQuantity());
            statement.setObject(2, orderItem.getOrderId());
            statement.setObject(3, orderItem.getDishId());
            statement.executeUpdate();
            logger.info("Successfully updated order item with order_id {}, dish_id {} and quantity {}", orderItem.getOrderId(), orderItem.getDishId(), orderItem.getQuantity());
            return orderItem;
        } catch (SQLException e) {
            logger.error("Error updating order item in database", e);
            return null;
        }
    }

    @Override
    public OrderItem update(OrderItem orderItemOld, OrderItem orderItemNew) {
        logger.info("Updating order item with order_id {}, dish_id {} and quantity {}", orderItemOld.getOrderId(), orderItemOld.getDishId(), orderItemOld.getQuantity());
        String sql = "UPDATE hitsz_taste.order_items SET order_id = ?, dish_id = ?, quantity = ? WHERE order_id = ? AND dish_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setObject(1, orderItemNew.getOrderId());
            statement.setObject(2, orderItemNew.getDishId());
            statement.setObject(3, orderItemNew.getQuantity());
            statement.setObject(4, orderItemOld.getOrderId());
            statement.setObject(5, orderItemOld.getDishId());
            statement.setObject(6, orderItemOld.getQuantity());
            statement.executeUpdate();
            logger.info("Successfully updated order item with order_id {}, dish_id {} and quantity {}", orderItemOld.getOrderId(), orderItemOld.getDishId(), orderItemOld.getQuantity());
            return orderItemNew;
        } catch (SQLException e) {
            logger.error("Error updating order item in database", e);
            return null;
        }

    }

    @Override
    public OrderItem getFromResultSet(ResultSet resultSet) throws SQLException {
        OrderItem orderItem = new OrderItem();
        orderItem.setOrderId(resultSet.getLong("order_id"));
        orderItem.setDishId(resultSet.getLong("dish_id"));
        orderItem.setQuantity(resultSet.getLong("quantity"));
        return orderItem;
    }

    @Override
    public List<OrderItem> getAll() {
        logger.info("Getting all order items");
        String sql = "SELECT * FROM hitsz_taste.order_items";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            ResultSet resultSet = statement.executeQuery();
            List<OrderItem> orderItems = new ArrayList<>();
            while (resultSet.next()) {
                orderItems.add(getFromResultSet(resultSet));
            }
            logger.info("Successfully got all order items, total number: {}", orderItems.size());
            return orderItems;
        } catch (SQLException e) {
            logger.error("Error getting all order items from database", e);
            return null;
        }
    }

    @Override
    public void deleteAll() {
        logger.info("Deleting all order items, total number: {}", getAll().size());
        String sql = "DELETE FROM hitsz_taste.order_items";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.executeUpdate();
            logger.info("Successfully deleted all order items, total number: {}", getAll().size());
        } catch (SQLException e) {
            logger.error("Error deleting all order items from database", e);
        }
    }

    @Override
    public void delete(OrderItem orderItem) {
        logger.info("Deleting order item with order_id {}, dish_id {} and quantity {}", orderItem.getOrderId(), orderItem.getDishId(), orderItem.getQuantity());
        String sql = "DELETE FROM hitsz_taste.order_items WHERE order_id = ? AND dish_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setObject(1, orderItem.getOrderId());
            statement.setObject(2, orderItem.getDishId());
            statement.executeUpdate();
            logger.info("Successfully deleted order item with order_id {}, dish_id {} and quantity {}", orderItem.getOrderId(), orderItem.getDishId(), orderItem.getQuantity());
        } catch (SQLException e) {
            logger.error("Error deleting order item from database", e);
        }
    }

    public Boolean exists(OrderItem orderItem) {
        logger.info("Checking if order item with order_id {}, dish_id {} and quantity {} exists", orderItem.getOrderId(), orderItem.getDishId(), orderItem.getQuantity());
        String sql = "SELECT * FROM hitsz_taste.order_items WHERE order_id = ? AND dish_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setObject(1, orderItem.getOrderId());
            statement.setObject(2, orderItem.getDishId());
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                logger.info("Order item with order_id {}, dish_id {} and quantity {} exists", orderItem.getOrderId(), orderItem.getDishId(), orderItem.getQuantity());
                return true;
            } else {
                logger.info("Order item with order_id {}, dish_id {} and quantity {} does not exist", orderItem.getOrderId(), orderItem.getDishId(), orderItem.getQuantity());
                return false;
            }
        } catch (SQLException e) {
            logger.error("Error checking if order item exists in database", e);
            return null;
        }
    }
}
