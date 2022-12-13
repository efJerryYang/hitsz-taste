package me.efjerryyang.webserver.dao;

import me.efjerryyang.webserver.model.Cafeteria;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class CafeteriaDAO implements DAO<Cafeteria> {
    private static final Logger logger = LoggerFactory.getLogger(CafeteriaDAO.class);
    private MySQLConnection mysqlConnection;
    private Connection connection;

    @Autowired
    public CafeteriaDAO(MySQLConnection mySQLConnection) throws SQLException {
        this.mysqlConnection = mySQLConnection;
        logger.debug("Obtaining connection to database");
        this.connection = mySQLConnection.getConnection();
    }

    @Override
    public Cafeteria create(Cafeteria cafeteria) {
        logger.info("Creating cafeteria with id {} and name {}", cafeteria.getCafeteriaId(), cafeteria.getName());
        String sql = "INSERT INTO hitsz_taste.cafeterias (cafeteria_id, name, location, active) VALUES (?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setObject(1, cafeteria.getCafeteriaId());
            statement.setObject(2, cafeteria.getName());
            statement.setObject(3, cafeteria.getLocation());
            statement.setObject(4, cafeteria.getIsActive());
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

    @Override
    public Cafeteria update(Cafeteria cafeteria) {
        logger.info("Updating cafeteria with id {} and name {}", cafeteria.getCafeteriaId(), cafeteria.getName());
        String sql = "UPDATE hitsz_taste.cafeterias SET name = ?, location = ?, active = ? WHERE cafeteria_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            // Set the values for the cafeteria in the SQL statement
            logger.debug("Setting cafeteria values in SQL statement: name = {}, location = {}, active = {}, cafeteria_id = {}", cafeteria.getName(), cafeteria.getLocation(), cafeteria.getIsActive(), cafeteria.getCafeteriaId());
            statement.setObject(1, cafeteria.getName());
            statement.setObject(2, cafeteria.getLocation());
            statement.setObject(3, cafeteria.getIsActive());
            statement.setObject(4, cafeteria.getCafeteriaId());
            statement.executeUpdate();
            logger.info("Successfully updated cafeteria with id {}", cafeteria.getCafeteriaId());
            return cafeteria;
        } catch (SQLException e) {
            logger.error("Error updating cafeteria in database", e);
            return null;
        }
    }

    @Override
    public Cafeteria update(Cafeteria cafeteriaOld, Cafeteria cafeteriaNew) {
        logger.info("Updating cafeteria with id {}", cafeteriaOld.getCafeteriaId());
        String sql = "UPDATE hitsz_taste.cafeterias SET cafeteria_id = ?, name = ?, location = ?, active = ? WHERE cafeteria_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setObject(1, cafeteriaNew.getCafeteriaId());
            statement.setObject(2, cafeteriaNew.getName());
            statement.setObject(3, cafeteriaNew.getLocation());
            statement.setObject(4, cafeteriaNew.getIsActive());
            statement.setObject(5, cafeteriaOld.getCafeteriaId());
            statement.executeUpdate();
            logger.info("Successfully updated cafeteria with id {}", cafeteriaOld.getCafeteriaId());
            return cafeteriaNew;
        } catch (SQLException e) {
            logger.error("Error updating cafeteria in database", e);
            return null;
        }
    }

    @Override
    public Cafeteria getFromResultSet(ResultSet resultSet) throws SQLException {
        logger.debug("Getting cafeteria from result set");
        Cafeteria cafeteria = new Cafeteria();
        cafeteria.setCafeteriaId(resultSet.getLong("cafeteria_id"));
        cafeteria.setName(resultSet.getString("name"));
        cafeteria.setLocation(resultSet.getString("location"));
        cafeteria.setIsActive(resultSet.getBoolean("active"));
        logger.debug("Successfully got cafeteria from result set");
        return cafeteria;
    }

    @Override
    public List<Cafeteria> getAll() {
        logger.info("Getting all cafeterias from database");
        String sql = "SELECT * FROM hitsz_taste.cafeterias";
        List<Cafeteria> cafeterias = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement(sql); ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                Cafeteria cafeteria = getFromResultSet(resultSet);
                cafeterias.add(cafeteria);
            }
            logger.info("Successfully got all cafeterias");
            return cafeterias;
        } catch (SQLException e) {
            logger.error("Error getting all cafeterias from database", e);
            return null;
        }
    }

    @Override
    public void deleteAll() {
        logger.info("Deleting all cafeterias from database");
        String sql = "DELETE FROM hitsz_taste.cafeterias";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.executeUpdate();
            logger.info("Successfully deleted all cafeterias");
        } catch (SQLException e) {
            logger.error("Error deleting all cafeterias from database", e);
        }
    }

    @Override
    public void delete(Cafeteria cafeteria) {
        logger.info("Deleting cafeteria with id {}", cafeteria.getCafeteriaId());
        String sql = "DELETE FROM hitsz_taste.cafeterias WHERE cafeteria_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setObject(1, cafeteria.getCafeteriaId());
            statement.executeUpdate();
            logger.info("Successfully deleted cafeteria with id {}", cafeteria.getCafeteriaId());
        } catch (SQLException e) {
            logger.error("Error deleting cafeteria from database", e);
        }
    }

    public Cafeteria deleteById(Long cafeteriaId) {
        logger.info("Deleting cafeteria with id {}", cafeteriaId);
        String sql = "DELETE FROM hitsz_taste.cafeterias WHERE cafeteria_id = ?";
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

    public Cafeteria activeById(Long cafeteriaId) {
        logger.info("Activating cafeteria with id {}", cafeteriaId);
        String sql = "UPDATE hitsz_taste.cafeterias SET active = true WHERE cafeteria_id = ?";
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

    public Cafeteria disableById(Long cafeteriaId) {
        logger.info("Disabling cafeteria with id {}", cafeteriaId);
        String sql = "UPDATE hitsz_taste.cafeterias SET active = false WHERE cafeteria_id = ?";
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

    public Cafeteria getById(Long cafeteriaId) {
        logger.info("Getting cafeteria with id {}", cafeteriaId);
        String sql = "SELECT * FROM hitsz_taste.cafeterias WHERE cafeteria_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setObject(1, cafeteriaId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                Cafeteria cafeteria = getFromResultSet(resultSet);
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
}