package me.efjerryyang.webserver.dao;

import me.efjerryyang.webserver.model.Order;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class OrderDAO implements DAO<Order> {
    private static final Logger logger = LoggerFactory.getLogger(OrderDAO.class);
    private final MysqlConnection mysqlConnection;
    private final Connection connection;

    @Autowired
    public OrderDAO(MysqlConnection mysqlConnection) throws SQLException {
        this.mysqlConnection = mysqlConnection;
        logger.debug("Obtaining connection to database");
        this.connection = mysqlConnection.getConnection();
    }

    @Override
    public Order create(Order order) {
        logger.info("Creating order with order_id {} and user_id {}", order.getOrderId(), order.getUserId());
        String sql = "INSERT INTO hitsz_taste.orders (order_id, user_id, total_price, address, contact, status, create_at) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setObject(1, order.getOrderId());
            statement.setObject(2, order.getUserId());
            statement.setObject(3, order.getTotalPrice());
            statement.setObject(4, order.getAddress());
            statement.setObject(5, order.getContact());
            statement.setObject(6, order.getStatus());
            statement.setObject(7, order.getCreateAt());
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

    @Override
    public Order update(Order order) {
        logger.info("Updating order with order_id {}, user_id {}, total_price {}, address {}, contact {}, status {}, create_at {}", order.getOrderId(), order.getUserId(), order.getTotalPrice(), order.getAddress(), order.getContact(), order.getStatus(), order.getCreateAt());
        String sql = "UPDATE hitsz_taste.orders SET user_id = ?, total_price = ?, address = ?, contact = ?, status = ?, create_at = ? WHERE order_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            // Set the values for the order in the SQL statement
            statement.setObject(1, order.getUserId());
            statement.setObject(2, order.getTotalPrice());
            statement.setObject(3, order.getAddress());
            statement.setObject(4, order.getContact());
            statement.setObject(5, order.getStatus());
            statement.setObject(6, order.getCreateAt());
            statement.setObject(7, order.getOrderId());
            statement.executeUpdate();
            logger.info("Successfully updated order with id {}", order.getOrderId());
            return order;
        } catch (SQLException e) {
            logger.error("Error updating order in database", e);
            return null;
        }
    }

    @Override
    public Order update(Order objectOld, Order objectNew) {
        logger.info("Updating order with order_id {}, user_id {}, total_price {}, address {}, contact {}, status {}, create_at {}", objectOld.getOrderId(), objectOld.getUserId(), objectOld.getTotalPrice(), objectOld.getAddress(), objectOld.getContact(), objectOld.getStatus(), objectOld.getCreateAt());
        String sql = "UPDATE hitsz_taste.orders SET user_id = ?, total_price = ?, address = ?, contact = ?, status = ?, create_at = ? WHERE order_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            // Set the values for the order in the SQL statement
            statement.setObject(1, objectNew.getUserId());
            statement.setObject(2, objectNew.getTotalPrice());
            statement.setObject(3, objectNew.getAddress());
            statement.setObject(4, objectNew.getContact());
            statement.setObject(5, objectNew.getStatus());
            statement.setObject(6, objectNew.getCreateAt());
            statement.setObject(7, objectOld.getOrderId());
            statement.executeUpdate();
            logger.info("Successfully updated order with id {}", objectOld.getOrderId());
            return objectNew;
        } catch (SQLException e) {
            logger.error("Error updating order in database", e);
            return null;
        }
    }

    @Override
    public Order getFromResultSet(ResultSet resultSet) throws SQLException {
        Order order = new Order();
        order.setOrderId(resultSet.getLong("order_id"));
        order.setUserId(resultSet.getLong("user_id"));
        order.setTotalPrice(resultSet.getFloat("total_price"));
        order.setAddress(resultSet.getString("address"));
        order.setContact(resultSet.getString("contact"));
        order.setStatus(resultSet.getString("status"));
        order.setCreateAt(resultSet.getTimestamp("create_at"));
        return order;
    }

    @Override
    public List<Order> getAll() {
        logger.info("Getting all orders");
        String sql = "SELECT * FROM hitsz_taste.orders";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            ResultSet resultSet = statement.executeQuery();
            List<Order> orders = new ArrayList<>();
            while (resultSet.next()) {
                orders.add(getFromResultSet(resultSet));
            }
            logger.info("Successfully retrieved all orders");
            return orders;
        } catch (SQLException e) {
            logger.error("Error retrieving all orders from database", e);
            return null;
        }
    }

    @Override
    public void deleteAll() {
        logger.info("Deleting all orders");
        String sql = "DELETE FROM hitsz_taste.orders";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.executeUpdate();
            logger.info("Successfully deleted all orders");
        } catch (SQLException e) {
            logger.error("Error deleting all orders from database", e);
        }
    }

    @Override
    public void delete(Order order) {
        logger.info("Deleting order with order_id {}", order.getOrderId());
        String sql = "DELETE FROM hitsz_taste.orders WHERE order_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setObject(1, order.getOrderId());
            statement.executeUpdate();
            logger.info("Successfully deleted order with id {}", order.getOrderId());
        } catch (SQLException e) {
            logger.error("Error deleting order from database", e);
        }
    }

    /**
     * Get orders in a given date range
     *
     * @param startTimestamp Start date of the range
     * @param endTimestamp   End date of the range
     * @return List of orders in the given date range
     */
    public List<Order> getOrderByDateRange(Timestamp startTimestamp, Timestamp endTimestamp) {
        logger.info("Getting orders between {} and {}", startTimestamp, endTimestamp);
        String sql = "SELECT * FROM hitsz_taste.orders WHERE create_at BETWEEN ? AND ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setObject(1, startTimestamp);
            statement.setObject(2, endTimestamp);
            ResultSet resultSet = statement.executeQuery();
            List<Order> orders = new ArrayList<>();
            while (resultSet.next()) {
                orders.add(getFromResultSet(resultSet));
            }
            logger.info("Successfully retrieved orders between {} and {}", startTimestamp, endTimestamp);
            return orders;
        } catch (SQLException e) {
            logger.error("Error retrieving orders between {} and {} from database", startTimestamp, endTimestamp, e);
            return null;
        }
    }

    /**
     * Get orders for a given user
     *
     * @param userId User ID
     * @return List of orders for the given user
     */
    public List<Order> getOrderByUserId(Long userId) {
        logger.info("Getting orders for user with id {}", userId);
        String sql = "SELECT * FROM hitsz_taste.orders WHERE user_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setObject(1, userId);
            ResultSet resultSet = statement.executeQuery();
            List<Order> orders = new ArrayList<>();
            while (resultSet.next()) {
                orders.add(getFromResultSet(resultSet));
            }
            logger.info("Successfully retrieved orders for user with id {}", userId);
            return orders;
        } catch (SQLException e) {
            logger.error("Error retrieving orders for user with id {} from database", userId, e);
            return null;
        }
    }

    /**
     * Get orders for a given user by date range
     *
     * @param userId         User ID
     * @param startTimestamp Start date of the range
     * @param endTimestamp   End date of the range
     * @return List of orders for the given user in the given date range
     */
    public List<Order> getOrderByUserIdAndDateRange(Long userId, Timestamp startTimestamp, Date endTimestamp) {
        logger.info("Getting orders for user with id {} between {} and {}", userId, startTimestamp, endTimestamp);
        String sql = "SELECT * FROM hitsz_taste.orders WHERE user_id = ? AND create_at BETWEEN ? AND ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setObject(1, userId);
            statement.setObject(2, startTimestamp);
            statement.setObject(3, endTimestamp);
            ResultSet resultSet = statement.executeQuery();
            List<Order> orders = new ArrayList<>();
            while (resultSet.next()) {
                orders.add(getFromResultSet(resultSet));
            }
            logger.info("Successfully retrieved orders for user with id {} between {} and {}", userId, startTimestamp, endTimestamp);
            return orders;
        } catch (SQLException e) {
            logger.error("Error retrieving orders for user with id {} between {} and {} from database", userId, startTimestamp, endTimestamp, e);
            return null;
        }
    }

    public Long getNextId() {
        logger.info("Getting next order id");
        String sql = "SELECT MAX(order_id) FROM hitsz_taste.orders";
        try (PreparedStatement statement = connection.prepareStatement(sql); ResultSet resultSet = statement.executeQuery()) {
            if (resultSet.next()) {
                Long nextId = resultSet.getLong(1) + 1;
                logger.info("Successfully retrieved next order id {}", nextId);
                return nextId;
            } else {
                logger.info("Successfully retrieved next order id 1");
                return 1L;
            }
        } catch (SQLException e) {
            logger.error("Error retrieving next order id from database", e);
            return null;
        }
    }
    // TODO: change 'cancelled' status to OrderStatus.CANCELLED
    public void cancel(Long orderId) {
        logger.info("Cancelling order with id {}", orderId);
        String sql = "UPDATE hitsz_taste.orders SET status = ? WHERE order_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setObject(1, "cancelled");
            statement.setObject(2, orderId);
            statement.executeUpdate();
            logger.info("Successfully cancelled order with id {}", orderId);
        } catch (SQLException e) {
            logger.error("Error cancelling order with id {} from database", orderId, e);
        }
    }

    public void cancel(Order order) {
        cancel(order.getOrderId());
    }

    public Order getById(Long orderId) {
        logger.info("Getting order with id {}", orderId);
        String sql = "SELECT * FROM hitsz_taste.orders WHERE order_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setObject(1, orderId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                Order order = getFromResultSet(resultSet);
                logger.info("Successfully retrieved order with id {}", orderId);
                return order;
            } else {
                logger.info("No order with id {} found", orderId);
                return null;
            }
        } catch (SQLException e) {
            logger.error("Error retrieving order with id {} from database", orderId, e);
            return null;
        }
    }
}