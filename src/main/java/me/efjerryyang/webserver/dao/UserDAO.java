package me.efjerryyang.webserver.dao;

import me.efjerryyang.webserver.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class UserDAO implements DAO<User> {
    private static final Logger logger = LoggerFactory.getLogger(UserDAO.class);
    private final MySQLConnection mysqlConnection;
    private final Connection connection;

    @Autowired
    public UserDAO(MySQLConnection mysqlConnection) throws SQLException {
        this.mysqlConnection = mysqlConnection;
        logger.debug("Obtaining connection to database");
        this.connection = mysqlConnection.getConnection();
    }

    @Override
    public User create(User user) {
        logger.info("Creating user with id {} and name {}", user.getUserId(), user.getName());
        String sql = "INSERT INTO users (user_id, name, email, password, phone, address, active) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setObject(1, user.getUserId());
            statement.setObject(2, user.getName());
            statement.setObject(3, user.getEmail());
            statement.setObject(4, user.getPassword());
            statement.setObject(5, user.getPhone());
            statement.setObject(6, user.getAddress());
            statement.setObject(7, user.getIsActive());
            statement.executeUpdate();
            ResultSet resultSet = statement.getGeneratedKeys();
            if (resultSet.next()) {
                user.setUserId(resultSet.getLong(1));
            }
            logger.info("Successfully created user with id {}", user.getUserId());
            return user;
        } catch (SQLException e) {
            logger.error("Error creating user in database", e);
            return null;
        }
    }

    @Override
    public User update(User user) {
        logger.info("Updating user with id {} and name {}", user.getUserId(), user.getName());
        String sql = "UPDATE users SET name = ?, email = ?, password = ?, phone = ?, address = ? WHERE user_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            // Set the values for the user in the SQL statement
            logger.debug("Setting user values in SQL statement: name = {}, email = {}, password = {}, phone = {}, address = {}, user_id = {}, active = {}", user.getName(), user.getEmail(), user.getPassword(), user.getPhone(), user.getAddress(), user.getUserId(), user.getIsActive());
            statement.setObject(1, user.getName());
            statement.setObject(2, user.getEmail());
            statement.setObject(3, user.getPassword());
            statement.setObject(4, user.getPhone());
            statement.setObject(5, user.getAddress());
            statement.setObject(6, user.getUserId());
            statement.setObject(7, user.getIsActive());
            statement.executeUpdate();
            logger.info("Successfully updated user with id {}", user.getUserId());
            return user;
        } catch (SQLException e) {
            logger.error("Error updating user in database", e);
            return null;
        }
    }

    @Override
    public User update(User objectOld, User objectNew) {
        logger.info("Updating user with id {}", objectOld.getUserId());
        String sql = "UPDATE users SET name = ?, email = ?, password = ?, phone = ?, address = ?, active = ? WHERE user_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setObject(1, objectNew.getName());
            statement.setObject(2, objectNew.getEmail());
            statement.setObject(3, objectNew.getPassword());
            statement.setObject(4, objectNew.getPhone());
            statement.setObject(5, objectNew.getAddress());
            statement.setObject(6, objectNew.getIsActive());
            statement.setObject(7, objectOld.getUserId());
            statement.executeUpdate();
            logger.info("Successfully updated user with id {}", objectOld.getUserId());
            return objectNew;
        } catch (SQLException e) {
            logger.error("Error updating user in database", e);
            return null;
        }
    }

    @Override
    public User getFromResultSet(ResultSet resultSet) {
        try {
            // Retrieve the data for the user
            Long userID = resultSet.getLong("user_id");
            String name = resultSet.getString("name");
            String email = resultSet.getString("email");
            String password = resultSet.getString("password");
            String phone = resultSet.getString("phone");
            String address = resultSet.getString("address");
            Boolean isActive = resultSet.getBoolean("active");
            logger.info("Successfully retrieved user from ResultSet");
            return new User(userID, name, email, password, phone, address, isActive);
        } catch (SQLException e) {
            // Log the exception and return a default user
            logger.error("Error retrieving user from ResultSet: ", e);
            return new User();
        }
    }

    @Override
    public List<User> getAll() {
        logger.info("Getting all users from database");
        String sql = "SELECT * FROM users";
        List<User> users = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement(sql); ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                User user = getFromResultSet(resultSet);
                users.add(user);
            }
            logger.info("Successfully retrieved {} users from database", users.size());
            return users;
        } catch (SQLException e) {
            logger.error("Error getting users from database", e);
            return null;
        }
    }

    @Override
    public void deleteAll() {
        logger.info("Deleting all users");
        String sql = "DELETE FROM users";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.executeUpdate();
            logger.info("Successfully deleted all users");
        } catch (SQLException e) {
            logger.error("Error deleting all users in database", e);
        }
    }

    @Override
    public void delete(User user) {
        logger.info("Deleting user with id {}", user.getUserId());
        String sql = "DELETE FROM users WHERE user_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setObject(1, user.getUserId());
            statement.executeUpdate();
            logger.info("Successfully deleted user with id {}", user.getUserId());
        } catch (SQLException e) {
            logger.error("Error deleting user in database", e);
        }
    }

    public User getByEmail(String email) {
        logger.info("Getting user with email {}", email);
        String sql = "SELECT * FROM users WHERE email = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setObject(1, email);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                User user = getFromResultSet(resultSet);
                logger.info("Successfully retrieved user with email {}", email);
                return user;
            } else {
                logger.info("No user with email {} found", email);
                return null;
            }
        } catch (SQLException e) {
            logger.error("Error getting user with email {} from database", email, e);
            return null;
        }
    }

    public User getByNameAndPassword(String name, String password){
        logger.info("Getting user with name {} and password {}", name, password);
        String sql = "SELECT * FROM users WHERE name = ? AND password = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setObject(1, name);
            statement.setObject(2, password);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                User user = getFromResultSet(resultSet);
                logger.info("Successfully retrieved user with name {} and password {}", name, password);
                return user;
            } else {
                logger.info("No user with name {} and password {} found", name, password);
                return null;
            }
        } catch (SQLException e) {
            logger.error("Error getting user with name {} and password {}", name, password, e);
            return null;
        }
    }

    public User getByEmailAndPassword(String email, String password) {
        logger.info("Getting user with email {} and password {}", email, password);
        String sql = "SELECT * FROM users WHERE email = ? AND password = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setObject(1, email);
            statement.setObject(2, password);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                User user = getFromResultSet(resultSet);
                logger.info("Successfully retrieved user with email {} and password {}", email, password);
                return user;
            } else {
                logger.info("No user with email {} and password {} found", email, password);
                return null;
            }
        } catch (SQLException e) {
            logger.error("Error getting user with email {} and password {}", email, password, e);
            return null;
        }
    }

    public User getByPhoneAndPassword(String phone, String password) {
        logger.info("Getting user with phone {} and password {}", phone, password);
        String sql = "SELECT * FROM users WHERE phone = ? AND password = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setObject(1, phone);
            statement.setObject(2, password);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                User user = getFromResultSet(resultSet);
                logger.info("Successfully retrieved user with phone {} and password {}", phone, password);
                return user;
            } else {
                logger.info("No user with phone {} and password {} found", phone, password);
                return null;
            }
        } catch (SQLException e) {
            logger.error("Error getting user with phone {} and password {} from database", phone, password, e);
            return null;
        }
    }

    public User getByPhone(String phone) {
        logger.info("Getting user with phone {}", phone);
        String sql = "SELECT * FROM users WHERE phone = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setObject(1, phone);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                User user = getFromResultSet(resultSet);
                logger.info("Successfully retrieved user with phone {}", phone);
                return user;
            } else {
                logger.info("No user with phone {} found", phone);
                return null;
            }
        } catch (SQLException e) {
            logger.error("Error getting user with phone {} from database", phone, e);
            return null;
        }
    }

    public User deleteById(Long userId) {
        logger.info("Deleting user with id {}", userId);
        String sql = "DELETE FROM users WHERE user_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            // Set the user id for the user to be deleted in the SQL statement
            statement.setObject(1, userId);
            statement.executeUpdate();
            logger.info("Successfully deleted user with id {}", userId);
            return new User();
        } catch (SQLException e) {
            logger.error("Error deleting user from database", e);
            return null;
        }
    }

    public User activeById(Long userId) {
        logger.info("Activating user with id {}", userId);
        String sql = "UPDATE users SET active = true WHERE user_id= ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setObject(1, userId);
            statement.executeUpdate();
            logger.info("Successfully active user with id {}", userId);
            return new User();
        } catch (SQLException e) {
            logger.error("Error activating user from database", e);
            e.printStackTrace();
        }
        return new User();
    }

    public User disableById(Long userId) {
        logger.info("Disabling user with id {}", userId);
        String sql = "UPDATE users SET active = false WHERE user_id= ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setObject(1, userId);
            statement.executeUpdate();
            logger.info("Successfully disabled user with id {}", userId);
            return new User();
        } catch (SQLException e) {
            logger.error("Error disabling user from database", e);
            e.printStackTrace();
        }
        return new User();
    }

    public User getById(Long queryId) {
        logger.info("Getting user by id {}", queryId);
        String sql = "SELECT * FROM users WHERE user_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setObject(1, queryId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                logger.info("User with id {} was found in the database", queryId);
                return getFromResultSet(resultSet);
            } else {
                logger.warn("No user with id {} was found in the database", queryId);
                return new User();
            }
        } catch (SQLException e) {
            logger.error("An exception was thrown while querying the database", e);
            return null;
        }

    }

}