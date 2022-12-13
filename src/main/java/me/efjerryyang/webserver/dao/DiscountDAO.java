package me.efjerryyang.webserver.dao;

import me.efjerryyang.webserver.model.Discount;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class DiscountDAO implements DAO<Discount> {
    private static final Logger logger = LoggerFactory.getLogger(DiscountDAO.class);

    private MySQLConnection mysqlConnection;
    private Connection connection;

    @Autowired
    public DiscountDAO(MySQLConnection mysqlConnection) throws SQLException {
        this.mysqlConnection = mysqlConnection;
        logger.debug("Obtaining connection to database");
        this.connection = mysqlConnection.getConnection();
    }

    @Override
    public Discount create(Discount discount) {
        logger.info("Creating discount with id {} and name {}", discount.getDiscountId(), discount.getName());
        String sql = "INSERT INTO hitsz_taste.discounts (discount_id, name, percentage) VALUES (?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setObject(1, discount.getDiscountId());
            statement.setObject(2, discount.getName());
            statement.setObject(3, discount.getPercentage());
            statement.executeUpdate();
            ResultSet resultSet = statement.getGeneratedKeys();
            if (resultSet.next()) {
                discount.setDiscountId(resultSet.getLong(1));
            }
            logger.info("Successfully created discount with id {}", discount.getDiscountId());
            return discount;
        } catch (SQLException e) {
            logger.error("Error creating discount in database", e);
            return null;
        }
    }

    @Override
    public Discount update(Discount discount) {
        logger.info("Updating discount with id {} and name {}", discount.getDiscountId(), discount.getName());
        String sql = "UPDATE hitsz_taste.discounts SET name = ?, percentage = ? WHERE discount_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            // Set the values for the discount in the SQL statement
            logger.debug("Setting discount values in SQL statement: name = {}, discount_id = {}", discount.getName(), discount.getDiscountId());
            statement.setObject(1, discount.getName());
            statement.setObject(2, discount.getPercentage());
            statement.setObject(3, discount.getDiscountId());
            statement.executeUpdate();
            logger.info("Successfully updated discount with id {}", discount.getDiscountId());
            return discount;
        } catch (SQLException e) {
            logger.error("Error updating discount in database", e);
            return null;
        }
    }

    @Override
    public Discount update(Discount objectOld, Discount objectNew) {
        logger.info("Updating discount with id {} and name {}", objectOld.getDiscountId(), objectOld.getName());
        String sql = "UPDATE hitsz_taste.discounts SET name = ?, percentage = ? WHERE discount_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            // Set the values for the discount in the SQL statement
            logger.debug("Setting discount values in SQL statement: name = {}, discount_id = {}", objectNew.getName(), objectNew.getDiscountId());
            statement.setObject(1, objectNew.getName());
            statement.setObject(2, objectNew.getPercentage());
            statement.setObject(3, objectOld.getDiscountId());
            statement.executeUpdate();
            logger.info("Successfully updated discount with id {}", objectOld.getDiscountId());
            return objectNew;
        } catch (SQLException e) {
            logger.error("Error updating discount in database", e);
            return null;
        }
    }

    @Override
    public Discount getFromResultSet(ResultSet resultSet) throws SQLException {
        Discount discount = new Discount();
        discount.setDiscountId(resultSet.getLong("discount_id"));
        discount.setName(resultSet.getString("name"));
        discount.setPercentage(resultSet.getFloat("percentage"));
        return discount;
    }

    @Override
    public List<Discount> getAll() {
        logger.info("Getting all discounts");
        String sql = "SELECT * FROM hitsz_taste.discounts";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            ResultSet resultSet = statement.executeQuery();
            List<Discount> discounts = new ArrayList<>();
            while (resultSet.next()) {
                Discount discount = getFromResultSet(resultSet);
                discounts.add(discount);
            }
            logger.info("Successfully retrieved all discounts, {} found", discounts.size());
            return discounts;
        } catch (SQLException e) {
            logger.error("Error getting all discounts from database", e);
            return null;
        }
    }

    @Override
    public void deleteAll() {
        logger.info("Deleting all discounts");
        String sql = "DELETE FROM hitsz_taste.discounts";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.executeUpdate();
            logger.info("Successfully deleted all discounts");
        } catch (SQLException e) {
            logger.error("Error deleting all discounts from database", e);
        }
    }

    @Override
    public void delete(Discount object) {
        logger.info("Deleting discount with id {}", object.getDiscountId());
        String sql = "DELETE FROM hitsz_taste.discounts WHERE discount_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setObject(1, object.getDiscountId());
            statement.executeUpdate();
            logger.info("Successfully deleted discount with id {}", object.getDiscountId());
        } catch (SQLException e) {
            logger.error("Error deleting discount from database", e);
        }
    }

    public Discount getById(Long discountId) {
        logger.info("Getting discount with id {}", discountId);
        String sql = "SELECT * FROM hitsz_taste.discounts WHERE discount_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setObject(1, discountId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                Discount discount = getFromResultSet(resultSet);
                logger.info("Successfully retrieved discount with id {}", discountId);
                return discount;
            }
            logger.info("No discount found with id {}", discountId);
            return null;
        } catch (SQLException e) {
            logger.error("Error getting discount from database", e);
            return null;
        }
    }

    public void deleteById(Long discountId) {
        logger.info("Deleting discount with id {}", discountId);
        String sql = "DELETE FROM hitsz_taste.discounts WHERE discount_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setObject(1, discountId);
            statement.executeUpdate();
            logger.info("Successfully deleted discount with id {}", discountId);
        } catch (SQLException e) {
            logger.error("Error deleting discount with id {} from database", discountId, e);
        }
    }

    public void deleteByName(String name) {
        logger.info("Deleting discount with name {}", name);
        String sql = "DELETE FROM hitsz_taste.discounts WHERE name = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setObject(1, name);
            statement.executeUpdate();
            logger.info("Successfully deleted discount with name {}", name);
        } catch (SQLException e) {
            logger.error("Error deleting discount with name {} from database", name, e);
        }
    }

    public void deleteByPercentage(Float percentage) {
        logger.info("Deleting discount with percentage {}", percentage);
        String sql = "DELETE FROM hitsz_taste.discounts WHERE percentage = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setObject(1, percentage);
            statement.executeUpdate();
            logger.info("Successfully deleted discount with percentage {}", percentage);
        } catch (SQLException e) {
            logger.error("Error deleting discount with percentage {} from database", percentage, e);
        }
    }

    public void deleteByPercentageRange(Float minPercentage, Float maxPercentage) {
        logger.info("Deleting discount with percentage between {} and {}", minPercentage, maxPercentage);
        String sql = "DELETE FROM hitsz_taste.discounts WHERE percentage BETWEEN ? AND ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setObject(1, minPercentage);
            statement.setObject(2, maxPercentage);
            statement.executeUpdate();
            logger.info("Successfully deleted discount with percentage between {} and {}", minPercentage, maxPercentage);
        } catch (SQLException e) {
            logger.error("Error deleting discount with percentage between {} and {} from database", minPercentage, maxPercentage, e);
        }
    }
}