package me.efjerryyang.webserver.dao;

import me.efjerryyang.webserver.model.Role;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class RoleDAO implements DAO<Role> {
    private static final Logger logger = LoggerFactory.getLogger(RoleDAO.class);
    private final MySQLConnection mysqlConnection;
    private final Connection connection;

    @Autowired
    public RoleDAO(MySQLConnection mysqlConnection) throws SQLException {
        this.mysqlConnection = mysqlConnection;
        logger.debug("Creating connection to database");
        this.connection = mysqlConnection.getConnection();
    }

    @Override
    public Role create(Role object) {
        logger.info("Creating role with name = {}, role_id = {}", object.getName(), object.getRoleId());
        String sql = "INSERT INTO hitsz_taste.roles (name, role_id) VALUES (?, ?)";
        try (java.sql.PreparedStatement statement = connection.prepareStatement(sql, java.sql.Statement.RETURN_GENERATED_KEYS)) {
            statement.setObject(1, object.getName());
            statement.executeUpdate();
            logger.info("Successfully created role with name = {}, role_id = {}", object.getName(), object.getRoleId());
            return object;
        } catch (SQLException e) {
            logger.error("Error creating role in database", e);
            return null;
        }
    }

    @Override
    public Role update(Role object) {
        logger.info("Updating role with name = {}, role_id = {}", object.getName(), object.getRoleId());
        String sql = "UPDATE hitsz_taste.roles SET name = ? WHERE role_id = ?";
        try (java.sql.PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setObject(1, object.getName());
            statement.setObject(2, object.getRoleId());
            statement.executeUpdate();
            logger.info("Successfully updated role with name = {}, role_id = {}", object.getName(), object.getRoleId());
            return object;
        } catch (SQLException e) {
            logger.error("Error updating role in database", e);
            return null;
        }
    }

    @Override
    public Role update(Role objectOld, Role objectNew) {
        logger.info("Updating role with name = {}, role_id = {}", objectOld.getName(), objectOld.getRoleId());
        String sql = "UPDATE hitsz_taste.roles SET name = ? WHERE role_id = ?";
        try (java.sql.PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setObject(1, objectNew.getName());
            statement.setObject(2, objectOld.getRoleId());
            statement.executeUpdate();
            logger.info("Successfully updated role with name = {}, role_id = {}", objectNew.getName(), objectOld.getRoleId());
            return objectNew;
        } catch (SQLException e) {
            logger.error("Error updating role in database", e);
            return null;
        }
    }

    @Override
    public Role getFromResultSet(ResultSet resultSet) throws SQLException {
        return new Role(resultSet.getInt("role_id"), resultSet.getString("name"));
    }

    @Override
    public List<Role> getAll() {
        logger.info("Getting all roles");
        String sql = "SELECT * FROM hitsz_taste.roles";
        try (java.sql.PreparedStatement statement = connection.prepareStatement(sql)) {
            ResultSet resultSet = statement.executeQuery();
            List<Role> roles = new ArrayList<>();
            while (resultSet.next()) {
                roles.add(getFromResultSet(resultSet));
            }
            logger.info("Successfully got all roles, total = {}", roles.size());
            return roles;
        } catch (SQLException e) {
            logger.error("Error getting all roles from database", e);
            return null;
        }
    }

    @Override
    public void deleteAll() {
        logger.info("Deleting all roles");
        String sql = "DELETE FROM hitsz_taste.roles";
        try (java.sql.PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.executeUpdate();
            logger.info("Successfully deleted all roles");
        } catch (SQLException e) {
            logger.error("Error deleting all roles from database", e);
        }
    }

    @Override
    public void delete(Role object) {
        logger.info("Deleting role with name = {}, role_id = {}", object.getName(), object.getRoleId());
        String sql = "DELETE FROM hitsz_taste.roles WHERE role_id = ?";
        try (java.sql.PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setObject(1, object.getRoleId());
            statement.executeUpdate();
            logger.info("Successfully deleted role with name = {}, role_id = {}", object.getName(), object.getRoleId());
        } catch (SQLException e) {
            logger.error("Error deleting role from database", e);
        }
    }
}
