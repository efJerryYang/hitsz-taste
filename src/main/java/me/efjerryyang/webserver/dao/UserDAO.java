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
    private final MysqlConnection mysqlConnection;
    private final Connection connection;

    @Autowired
    public UserDAO(MysqlConnection mysqlConnection) throws SQLException {
        this.mysqlConnection = mysqlConnection;
        logger.debug("Obtaining connection to database");
        this.connection = mysqlConnection.getConnection();
    }

    @Override
    public User create(User user) {
        logger.info("Creating user with id {} and username {}", user.getUserId(), user.getUsername());
        String sql = "INSERT INTO users (user_id, username, firstname, lastname, id_number, phone, password, address, email, is_active, salt, create_at) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setObject(1, user.getUserId());
            statement.setObject(2, user.getUsername());
            statement.setObject(3, user.getFirstname());
            statement.setObject(4, user.getLastname());
            statement.setObject(5, user.getIdNumber());
            statement.setObject(6, user.getPhone());
            statement.setObject(7, user.getPassword());
            statement.setObject(8, user.getAddress());
            statement.setObject(9, user.getEmail());
            statement.setObject(10, user.getIsActive());
            statement.setObject(11, user.getSalt());
            statement.setObject(12, user.getCreatedAt());

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
        logger.info("Updating user with id {} and username {}", user.getUserId(), user.getUsername());
        String sql = "UPDATE users SET username = ?, firstname = ?, lastname = ?, id_number = ?, phone = ?, password = ?, address = ?, email = ?, is_active = ?, salt = ?, create_at = ? WHERE user_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            // Set the values for the user in the SQL statement
            logger.debug("Setting user values in SQL statement: username = {}, firstname = {}, lastname = {}, id_number = {}, email = {}, password = {}, phone = {}, address = {}, is_active = {}, salt = {}, create_at = {}, user_id = {}", user.getUsername(), user.getFirstname(), user.getLastname(), user.getIdNumber(), user.getEmail(), user.getPassword(), user.getPhone(), user.getAddress(), user.getIsActive(), user.getSalt(), user.getCreatedAt(), user.getUserId());
            statement.setString(1, user.getUsername());
            statement.setString(2, user.getFirstname());
            statement.setString(3, user.getLastname());
            statement.setString(4, user.getIdNumber());
            statement.setString(5, user.getEmail());
            statement.setString(6, user.getPassword());
            statement.setString(7, user.getPhone());
            statement.setString(8, user.getAddress());
            statement.setBoolean(9, user.getIsActive());
            statement.setString(10, user.getSalt());
            statement.setTimestamp(11, user.getCreatedAt());
            statement.setLong(12, user.getUserId());

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
        String sql = "UPDATE users SET username = ?, firstname = ?, lastname = ?, id_number = ?, email = ?, password = ?, phone = ?, address = ?, is_active = ?, salt = ?, create_at = ? WHERE user_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            // Set the values for the user in the SQL statement
            logger.debug("Setting user values in SQL statement: username = {}, firstname = {}, lastname = {}, id_number = {}, email = {}, password = {}, phone = {}, address = {}, is_active = {}, salt = {}, create_at = {}, user_id = {}", objectNew.getUsername(), objectNew.getFirstname(), objectNew.getLastname(), objectNew.getIdNumber(), objectNew.getEmail(), objectNew.getPassword(), objectNew.getPhone(), objectNew.getAddress(), objectNew.getIsActive(), objectNew.getSalt(), objectNew.getCreatedAt(), objectOld.getUserId());
            statement.setString(1, objectNew.getUsername());
            statement.setString(2, objectNew.getFirstname());
            statement.setString(3, objectNew.getLastname());
            statement.setString(4, objectNew.getIdNumber());
            statement.setString(5, objectNew.getEmail());
            statement.setString(6, objectNew.getPassword());
            statement.setString(7, objectNew.getPhone());
            statement.setString(8, objectNew.getAddress());
            statement.setBoolean(9, objectNew.getIsActive());
            statement.setString(10, objectNew.getSalt());
            statement.setTimestamp(11, objectNew.getCreatedAt());
            statement.setLong(12, objectOld.getUserId());


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
            User user = new User();
            user.setUserId(resultSet.getLong("user_id"));
            user.setUsername(resultSet.getString("username"));
            user.setFirstname(resultSet.getString("firstname"));
            user.setLastname(resultSet.getString("lastname"));
            user.setIdNumber(resultSet.getString("id_number"));
            user.setEmail(resultSet.getString("email"));
            user.setPassword(resultSet.getString("password"));
            user.setPhone(resultSet.getString("phone"));
            user.setAddress(resultSet.getString("address"));
            user.setIsActive(resultSet.getBoolean("is_active"));
            user.setSalt(resultSet.getString("salt"));
            user.setCreatedAt(resultSet.getTimestamp("create_at"));
            logger.info("Successfully retrieved user from ResultSet");
            return user;
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

    public Long getNextId() {
        logger.info("Getting next user id");
        String sql = "SELECT MAX(user_id) FROM hitsz_taste.users";
        try (PreparedStatement statement = connection.prepareStatement(sql); ResultSet resultSet = statement.executeQuery()) {
            if (resultSet.next()) {
                Long nextId = resultSet.getLong(1) + 1;
                logger.info("Successfully retrieved next user id: {}", nextId);
                return nextId;
            } else {
                logger.info("Successfully retrieved next user id: 1");
                return 1L;
            }
        } catch (SQLException e) {
            logger.error("Error getting next user id", e);
            return null;
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


    public User getByUsername(String username) {
        logger.info("Getting user with username {}", username);
        String sql = "SELECT * FROM users WHERE username = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setObject(1, username);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                User user = getFromResultSet(resultSet);
                logger.info("Successfully retrieved user with username {}", username);
                return user;
            } else {
                logger.info("No user with username {} found", username);
                return null;
            }
        } catch (SQLException e) {
            logger.error("Error getting user with username {} from database", username, e);
            return null;
        }
    }

    public User getByUsernameAndPassword(String username, String password) {
        logger.info("Getting user with user" +
                "username {} and password {}", username, password);
        String sql = "SELECT * FROM users WHERE username = ? AND password = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setObject(1, username);
            statement.setObject(2, password);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                User user = getFromResultSet(resultSet);
                logger.info("Successfully retrieved user with username {} and password {}", username, password);
                return user;
            } else {
                logger.info("No user with username {} and password {} found", username, password);
                return null;
            }
        } catch (SQLException e) {
            logger.error("Error getting user with username {} and password {}", username, password, e);
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

    public String getSaltByEmail(String email) {
        // get salt from database
        logger.info("Getting salt for user with email {}", email);
        String sql = "SELECT salt FROM users WHERE email = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setObject(1, email);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                String salt = resultSet.getString("salt");
                logger.info("Successfully retrieved salt for user with email {}", email);
                return salt;
            } else {
                logger.info("No user with email {} found", email);
                return null;
            }
        } catch (SQLException e) {
            logger.error("Error getting salt for user with email {}", email, e);
            return null;
        }
    }

    public String getSaltByPhone(String phone) {
        // get salt from database
        logger.info("Getting salt for user with phone {}", phone);
        String sql = "SELECT salt FROM users WHERE phone = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setObject(1, phone);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                String salt = resultSet.getString("salt");
                logger.info("Successfully retrieved salt for user with phone {}", phone);
                return salt;
            } else {
                logger.info("No user with phone {} found", phone);
                return null;
            }
        } catch (SQLException e) {
            logger.error("Error getting salt for user with phone {}", phone, e);
            return null;
        }
    }

    public String getSaltByUsername(String username) {
        // get salt from database
        logger.info("Getting salt for user with username {}", username);
        String sql = "SELECT salt FROM users WHERE username = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setObject(1, username);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                String salt = resultSet.getString("salt");
                logger.info("Successfully retrieved salt for user with username {}", username);
                return salt;
            } else {
                logger.info("No user with username {} found", username);
                return null;
            }
        } catch (SQLException e) {
            logger.error("Error getting salt for user with username {}", username, e);
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

    public void activateById(Long userId) {
        logger.info("Activating user with id {}", userId);
        String sql = "UPDATE users SET is_active = true WHERE user_id= ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setObject(1, userId);
            statement.executeUpdate();
            logger.info("Successfully active user with id {}", userId);
        } catch (SQLException e) {
            logger.error("Error activating user from database", e);
            e.printStackTrace();
        }
    }

    public void disableById(Long userId) {
        logger.info("Disabling user with id {}", userId);
        String sql = "UPDATE users SET is_active = false WHERE user_id= ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setObject(1, userId);
            statement.executeUpdate();
            logger.info("Successfully disabled user with id {}", userId);
        } catch (SQLException e) {
            logger.error("Error disabling user from database", e);
            e.printStackTrace();
        }
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
                return null;
            }
        } catch (SQLException e) {
            logger.error("An exception was thrown while querying the database", e);
            return null;
        }

    }

}