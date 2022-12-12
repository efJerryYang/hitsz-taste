package me.efjerryyang.webserver.dao;

import me.efjerryyang.webserver.dao.MySQLConnection;
import me.efjerryyang.webserver.model.Cafeteria;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class CafeteriaDAO {
    private static final Logger logger = LoggerFactory.getLogger(CafeteriaDAO.class);
    private MySQLConnection mysqlConnection;
    private Connection connection;
    @Autowired
    public CafeteriaDAO(MySQLConnection mySQLConnection) throws SQLException {
        this.mysqlConnection = mySQLConnection;
        logger.debug("Obtaining connection to database");
        this.connection = mySQLConnection.getConnection();
    }

    public Cafeteria createCafeteria(Cafeteria cafeteria) {
        logger.info("Creating cafeteria with id {} and name {}", cafeteria.getCafeteriaId(), cafeteria.getName());
        String sql = "INSERT INTO cafeterias (cafeteria_id, name, location) VALUES (?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setObject(1, cafeteria.getCafeteriaId());
            statement.setObject(2, cafeteria.getName());
            statement.setObject(3, cafeteria.getLocation());
            statement.executeUpdate();
            ResultSet resultSet = statement.getGeneratedKeys();
            if (resultSet.next()) {
                cafeteria.setCafeteriaId(resultSet.getLong(1));
            }
            logger.info("Successfully created cafeteria with id {}", cafeteria.getCafeteriaId());
            return cafeteria;
        } catch (SQLException e) {
            logger.error("Error creating cafeteria in database", e);
            return null;
        }
    }

    public Cafeteria updateCafeteria(Cafeteria cafeteria) {
        logger.info("Updating cafeteria with id {} and name {}", cafeteria.getCafeteriaId(), cafeteria.getName());
        String sql = "UPDATE cafeterias SET name = ?, location = ? WHERE cafeteria_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            // Set the values for the cafeteria in the SQL statement
            logger.debug("Setting cafeteria values in SQL statement: name = {}, location = {}, cafeteria_id = {}", cafeteria.getName(), cafeteria.getLocation(), cafeteria.getCafeteriaId());
            statement.setObject(1, cafeteria.getName());
            statement.setObject(2, cafeteria.getLocation());
            statement.setObject(3, cafeteria.getCafeteriaId());
            statement.executeUpdate();
            logger.info("Successfully updated cafeteria with id {}", cafeteria.getCafeteriaId());
            return cafeteria;
        } catch (SQLException e) {
            logger.error("Error updating cafeteria in database", e);
            return null;
        }
    }

    private Cafeteria getCafeteriaFromResultSet(ResultSet resultSet) throws SQLException {
        Cafeteria cafeteria = new Cafeteria();
        cafeteria.setCafeteriaId(resultSet.getLong("cafeteria_id"));
        cafeteria.setName(resultSet.getString("name"));
        cafeteria.setLocation(resultSet.getString("location"));
        cafeteria.setIsActive(resultSet.getBoolean("active"));
        return cafeteria;
    }

    public Cafeteria deleteCafeteriaById(Long cafeteriaId) {
        logger.info("Deleting cafeteria with id {}", cafeteriaId);
        String sql = "DELETE FROM cafeterias WHERE cafeteria_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setObject(1, cafeteriaId);
            statement.executeUpdate();
            logger.info("Successfully deleted cafeteria with id {}", cafeteriaId);
            return new Cafeteria();
        } catch (SQLException e) {
            logger.error("Error deleting cafeteria from database", e);
            return null;
        }
    }

    public Cafeteria activeCafeteriaById(Long cafeteriaId) {
        logger.info("Activating cafeteria with id {}", cafeteriaId);
        String sql = "UPDATE cafeterias SET active = true WHERE cafeteria_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setObject(1, cafeteriaId);
            statement.executeUpdate();
            logger.info("Successfully activated cafeteria with id {}", cafeteriaId);
            return new Cafeteria();
        } catch (SQLException e) {
            logger.error("Error activating cafeteria from database", e);
            return null;
        }
    }

    public Cafeteria disableCafeteriaById(Long cafeteriaId) {
        logger.info("Disabling cafeteria with id {}", cafeteriaId);
        String sql = "UPDATE cafeterias SET active = false WHERE cafeteria_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setObject(1, cafeteriaId);
            statement.executeUpdate();
            logger.info("Successfully disabled cafeteria with id {}", cafeteriaId);
            return new Cafeteria();
        } catch (SQLException e) {
            logger.error("Error disabling cafeteria from database", e);
            return null;
        }
    }

    public Cafeteria getCafeteriaById(Long cafeteriaId) {
        logger.info("Getting cafeteria with id {}", cafeteriaId);
        String sql = "SELECT * FROM cafeterias WHERE cafeteria_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setObject(1, cafeteriaId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                Cafeteria cafeteria = getCafeteriaFromResultSet(resultSet);
                logger.info("Successfully got cafeteria with id {}", cafeteriaId);
                return cafeteria;
            }
            logger.info("No cafeteria with id {} found", cafeteriaId);
            return null;
        } catch (SQLException e) {
            logger.error("Error getting cafeteria from database", e);
            return null;
        }
    }

    public List<Cafeteria> getAllCafeterias(){
        logger.info("Getting all cafeterias from database");
        String sql = "SELECT * FROM cafeterias";
        List<Cafeteria> cafeterias = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                Cafeteria cafeteria = getCafeteriaFromResultSet(resultSet);
                cafeterias.add(cafeteria);
            }
            logger.info("Successfully got all cafeterias");
            return cafeterias;
        } catch (SQLException e) {
            logger.error("Error getting all cafeterias from database", e);
            return null;
        }
    }
}