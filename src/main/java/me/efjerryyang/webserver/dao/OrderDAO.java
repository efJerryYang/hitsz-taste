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
        String sql = "INSERT INTO hitsz_taste.orders (order_id, user_id, total_price, address, contact, status, timestamp) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setObject(1, order.getOrderId());
            statement.setObject(2, order.getUserId());
            statement.setObject(3, order.getTotalPrice());
            statement.setObject(4, order.getAddress());
            statement.setObject(5, order.getContact());
            statement.setObject(6, order.getStatus());
            statement.setObject(7, order.getTimestamp());
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
        logger.info("Updating order with order_id {}, user_id {}, total_price {}, address {}, contact {}, status {}, timestamp {}", order.getOrderId(), order.getUserId(), order.getTotalPrice(), order.getAddress(), order.getContact(), order.getStatus(), order.getTimestamp());
        String sql = "UPDATE hitsz_taste.orders SET user_id = ?, total_price = ?, address = ?, contact = ?, status = ?, timestamp = ? WHERE order_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            // Set the values for the order in the SQL statement
            statement.setObject(1, order.getUserId());
            statement.setObject(2, order.getTotalPrice());
            statement.setObject(3, order.getAddress());
            statement.setObject(4, order.getContact());
            statement.setObject(5, order.getStatus());
            statement.setObject(6, order.getTimestamp());
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
        logger.info("Updating order with order_id {}, user_id {}, total_price {}, address {}, contact {}, status {}, timestamp {}", objectOld.getOrderId(), objectOld.getUserId(), objectOld.getTotalPrice(), objectOld.getAddress(), objectOld.getContact(), objectOld.getStatus(), objectOld.getTimestamp());
        String sql = "UPDATE hitsz_taste.orders SET user_id = ?, total_price = ?, address = ?, contact = ?, status = ?, timestamp = ? WHERE order_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            // Set the values for the order in the SQL statement
            statement.setObject(1, objectNew.getUserId());
            statement.setObject(2, objectNew.getTotalPrice());
            statement.setObject(3, objectNew.getAddress());
            statement.setObject(4, objectNew.getContact());
            statement.setObject(5, objectNew.getStatus());
            statement.setObject(6, objectNew.getTimestamp());
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
        order.setTimestamp(resultSet.getDate("timestamp"));
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
     * @param startDate Start date of the range
     * @param endDate   End date of the range
     * @return List of orders in the given date range
     */
    public List<Order> getOrderByDateRange(Date startDate, Date endDate) {
        logger.info("Getting orders between {} and {}", startDate, endDate);
        String sql = "SELECT * FROM hitsz_taste.orders WHERE timestamp BETWEEN ? AND ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setObject(1, startDate);
            statement.setObject(2, endDate);
            ResultSet resultSet = statement.executeQuery();
            List<Order> orders = new ArrayList<>();
            while (resultSet.next()) {
                orders.add(getFromResultSet(resultSet));
            }
            logger.info("Successfully retrieved orders between {} and {}", startDate, endDate);
            return orders;
        } catch (SQLException e) {
            logger.error("Error retrieving orders between {} and {} from database", startDate, endDate, e);
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
     * @param userId    User ID
     * @param startDate Start date of the range
     * @param endDate   End date of the range
     * @return List of orders for the given user in the given date range
     */
    public List<Order> getOrderByUserIdAndDateRange(Long userId, Date startDate, Date endDate) {
        logger.info("Getting orders for user with id {} between {} and {}", userId, startDate, endDate);
        String sql = "SELECT * FROM hitsz_taste.orders WHERE user_id = ? AND timestamp BETWEEN ? AND ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setObject(1, userId);
            statement.setObject(2, startDate);
            statement.setObject(3, endDate);
            ResultSet resultSet = statement.executeQuery();
            List<Order> orders = new ArrayList<>();
            while (resultSet.next()) {
                orders.add(getFromResultSet(resultSet));
            }
            logger.info("Successfully retrieved orders for user with id {} between {} and {}", userId, startDate, endDate);
            return orders;
        } catch (SQLException e) {
            logger.error("Error retrieving orders for user with id {} between {} and {} from database", userId, startDate, endDate, e);
            return null;
        }
    }

}