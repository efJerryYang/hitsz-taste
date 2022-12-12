package me.efjerryyang.webserver.dao;

import me.efjerryyang.webserver.model.User;
import me.efjerryyang.webserver.util.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Component
public class UserDAO {
    private static final Logger logger = LoggerFactory.getLogger(UserDAO.class);
    private MySQLConnection mysqlConnection;
    private Connection connection;

    @Autowired
    public UserDAO(MySQLConnection mySQLConnection) throws SQLException {
        this.mysqlConnection = mySQLConnection;
        this.connection = mySQLConnection.getConnection();
    }

    public User createUser(User user) {
        logger.info("Creating user with id {} and name {}", user.getUserId(), user.getName());
        String sql = "INSERT INTO users (user_id, name, email, password, phone, address) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setObject(1, user.getUserId());
            statement.setObject(2, user.getName());
            statement.setObject(3, user.getEmail());
            statement.setObject(4, user.getPassword());
            statement.setObject(5, user.getPhone());
            statement.setObject(6, user.getAddress());
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

    public User updateUser(User user) {
        logger.info("Updating user with id {} and name {}", user.getUserId(), user.getName());
        String sql = "UPDATE users SET name = ?, email = ?, password = ?, phone = ?, address = ? WHERE user_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            // Set the values for the user in the SQL statement
            statement.setObject(1, user.getName());
            statement.setObject(2, user.getEmail());
            statement.setObject(3, user.getPassword());
            statement.setObject(4, user.getPhone());
            statement.setObject(5, user.getAddress());
            statement.setObject(6, user.getUserId());
            statement.executeUpdate();
            logger.info("Successfully updated user with id {}", user.getUserId());
            return user;
        } catch (SQLException e) {
            logger.error("Error updating user in database", e);
            return null;
        }
    }


    public User deleteUserById(Long userId) {
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

    public User getUserById(Long queryId) throws SQLException {
        String sql = "SELECT * FROM users WHERE userID = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setObject(1, queryId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                logger.info("User with id {} was found in the database", queryId);
                return getUserFromResultSet(resultSet);
            } else {
                logger.warn("No user with id {} was found in the database", queryId);
                return new User();
            }
        } catch (SQLException e) {
            logger.error("An exception was thrown while querying the database", e);
            return null;
        }

    }

    public List<User> getAllUsers() {
        logger.info("Getting all users from database");
        String sql = "SELECT * FROM users";
        List<User> users = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                User user = getUserFromResultSet(resultSet);
                users.add(user);
            }
            logger.info("Successfully retrieved {} users from database", users.size());
            return users;
        } catch (SQLException e) {
            logger.error("Error getting users from database", e);
            return null;
        }
    }

    private User getUserFromResultSet(ResultSet resultSet) {
        try {
            // Retrieve the data for the user
            Long userID = resultSet.getLong("user_id");
            String name = resultSet.getString("name");
            String email = resultSet.getString("email");
            String password = resultSet.getString("password");
            String phone = resultSet.getString("phone");
            String address = resultSet.getString("address");
            logger.info("Successfully retrieved user from ResultSet");
            return new User(userID, name, email, password, phone, address);
        } catch (SQLException e) {
            // Log the exception and return a default user
            logger.error("Error retrieving user from ResultSet: ", e);
            return new User();
        }
    }
}