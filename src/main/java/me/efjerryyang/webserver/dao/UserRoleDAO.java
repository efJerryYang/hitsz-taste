package me.efjerryyang.webserver.dao;

import me.efjerryyang.webserver.model.UserRole;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class UserRoleDAO implements DAO<UserRole> {
    private static final Logger logger = LoggerFactory.getLogger(UserRoleDAO.class);
    private MySQLConnection mysqlConnection;
    private Connection connection;

    @Autowired
    public UserRoleDAO(MySQLConnection mysqlConnection) throws SQLException {
        this.mysqlConnection = mysqlConnection;
        logger.debug("Obtaining connection to database");
        this.connection = mysqlConnection.getConnection();
    }

    @Override
    public UserRole create(UserRole userRole) {
        logger.debug("Creating user role with user_id = {} and role_id = {}", userRole.getUserId(), userRole.getRoleId());
        String sql = "INSERT INTO hitsz_taste.user_roles (user_id, role_id, grant_date) VALUES (?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setObject(1, userRole.getUserId());
            statement.setObject(2, userRole.getRoleId());
            statement.setObject(3, userRole.getGrantDate());
            statement.executeUpdate();
            logger.info("Successfully created user role with user_id = {} and role_id = {}", userRole.getUserId(), userRole.getRoleId());
            return userRole;
        } catch (SQLException e) {
            logger.error("Error creating user role in database", e);
            return null;
        }

    }

    @Override
    public UserRole update(UserRole userRole) {
        logger.debug("Updating user role with user_id = {} and role_id = {}", userRole.getUserId(), userRole.getRoleId());
        String sql = "UPDATE hitsz_taste.user_roles SET grant_date = ? WHERE user_id = ? AND role_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setObject(1, userRole.getGrantDate());
            statement.setObject(2, userRole.getUserId());
            statement.setObject(3, userRole.getRoleId());
            statement.executeUpdate();
            logger.info("Successfully updated user role with user_id = {} and role_id = {}", userRole.getUserId(), userRole.getRoleId());
            return userRole;
        } catch (SQLException e) {
            logger.error("Error updating user role in database", e);
            return null;
        }
    }

    @Override
    public UserRole update(UserRole userRoleOld, UserRole userRoleNew) {
        logger.debug("Updating user role with user_id = {} and role_id = {}", userRoleOld.getUserId(), userRoleOld.getRoleId());
        String sql = "UPDATE hitsz_taste.user_roles SET grant_date = ? WHERE user_id = ? AND role_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setObject(1, userRoleNew.getGrantDate());
            statement.setObject(2, userRoleOld.getUserId());
            statement.setObject(3, userRoleOld.getRoleId());
            statement.executeUpdate();
            logger.info("Successfully updated user role with user_id = {} and role_id = {}", userRoleOld.getUserId(), userRoleOld.getRoleId());
            return userRoleNew;
        } catch (SQLException e) {
            logger.error("Error updating user role in database", e);
            return null;
        }
    }

    @Override
    public UserRole getFromResultSet(ResultSet resultSet) throws SQLException {
        return new UserRole(
                resultSet.getObject("user_id", Long.class),
                resultSet.getObject("role_id", Long.class),
                resultSet.getObject("grant_date", Date.class)
        );
    }

    @Override
    public List<UserRole> getAll() {
        logger.debug("Getting all user roles");
        String sql = "SELECT * FROM hitsz_taste.user_roles";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            ResultSet resultSet = statement.executeQuery();
            List<UserRole> userRoles = new ArrayList<>();
            while (resultSet.next()) {
                userRoles.add(getFromResultSet(resultSet));
            }
            logger.info("Successfully got all user roles, total = {}", userRoles.size());
            return userRoles;
        } catch (SQLException e) {
            logger.error("Error getting all user roles from database", e);
            return null;
        }
    }

    @Override
    public void deleteAll() {
        logger.debug("Deleting all user roles");
        String sql = "DELETE FROM hitsz_taste.user_roles";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.executeUpdate();
            logger.info("Successfully deleted all user roles");
        } catch (SQLException e) {
            logger.error("Error deleting all user roles from database", e);
        }
    }

    @Override
    public void delete(UserRole userRole) {
        logger.debug("Deleting user role with user_id = {} and role_id = {}", userRole.getUserId(), userRole.getRoleId());
        String sql = "DELETE FROM hitsz_taste.user_roles WHERE user_id = ? AND role_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setObject(1, userRole.getUserId());
            statement.setObject(2, userRole.getRoleId());
            statement.executeUpdate();
            logger.info("Successfully deleted user role with user_id = {} and role_id = {}", userRole.getUserId(), userRole.getRoleId());
        } catch (SQLException e) {
            logger.error("Error deleting user role from database", e);
        }
    }
}
