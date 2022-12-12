package me.efjerryyang.webserver.dao;

import me.efjerryyang.webserver.dao.MySQLConnection;
import me.efjerryyang.webserver.model.Discount;
import org.slf4j.Logger;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class DiscountDAO {
    private static final Logger logger = LoggerFactory.getLogger(DiscountDAO.class);

    private MySQLConnection mysqlConnection;
    private Connection connection;

    @Autowired
    public DiscountDAO(MySQLConnection mySQLConnection) throws SQLException {
        this.mysqlConnection = mySQLConnection;
        logger.debug("Obtaining connection to database");
        this.connection = mySQLConnection.getConnection();
    }

    public Discount createDiscount(Discount discount) {
        logger.info("Creating discount with id {} and name {}", discount.getDiscountId(), discount.getName());
        String sql = "INSERT INTO discounts (discount_id, name) VALUES (?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setObject(1, discount.getDiscountId());
            statement.setObject(2, discount.getName());
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

    public Discount updateDiscount(Discount discount) {
        logger.info("Updating discount with id {} and name {}", discount.getDiscountId(), discount.getName());
        String sql = "UPDATE discounts SET name = ? WHERE discount_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            // Set the values for the discount in the SQL statement
            logger.debug("Setting discount values in SQL statement: name = {}, discount_id = {}", discount.getName(), discount.getDiscountId());
            statement.setObject(1, discount.getName());
            statement.setObject(2, discount.getDiscountId());
            statement.executeUpdate();
            logger.info("Successfully updated discount with id {}", discount.getDiscountId());
            return discount;
        } catch (SQLException e) {
            logger.error("Error updating discount in database", e);
            return null;
        }
    }

    public Discount getDiscountById(Long discountId) {
        logger.info("Getting discount with id {}", discountId);
        String sql = "SELECT * FROM discounts WHERE discount_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setObject(1, discountId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                Discount discount = new Discount();
                discount.setDiscountId(resultSet.getLong("discount_id"));
                discount.setName(resultSet.getString("name"));
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

    private Discount getDiscountFromResultSet(ResultSet resultSet) throws SQLException {
        Discount discount = new Discount();
        discount.setDiscountId(resultSet.getLong("discount_id"));
        discount.setName(resultSet.getString("name"));
        return discount;
    }

    public List<Discount> getAllDiscounts() {
        logger.info("Getting all discounts");
        String sql = "SELECT * FROM discounts";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            ResultSet resultSet = statement.executeQuery();
            List<Discount> discounts = new ArrayList<>();
            while (resultSet.next()) {
                Discount discount = getDiscountFromResultSet(resultSet);
                discounts.add(discount);
            }
            logger.info("Successfully retrieved all discounts");
            return discounts;
        } catch (SQLException e) {
            logger.error("Error getting all discounts from database", e);
            return null;
        }
    }

    public void deleteAllDiscounts() {
        logger.info("Deleting all discounts");
        String sql = "DELETE FROM discounts";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.executeUpdate();
            logger.info("Successfully deleted all discounts");
        } catch (SQLException e) {
            logger.error("Error deleting all discounts from database", e);
        }
    }

    public void deleteDiscountById(Long discountId) {
        logger.info("Deleting discount with id {}", discountId);
        String sql = "DELETE FROM discounts WHERE discount_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setObject(1, discountId);
            statement.executeUpdate();
            logger.info("Successfully deleted discount with id {}", discountId);
        } catch (SQLException e) {
            logger.error("Error deleting discount with id {} from database", discountId, e);
        }
    }

    public void deleteDiscountByName(String name) {
        logger.info("Deleting discount with name {}", name);
        String sql = "DELETE FROM discounts WHERE name = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setObject(1, name);
            statement.executeUpdate();
            logger.info("Successfully deleted discount with name {}", name);
        } catch (SQLException e) {
            logger.error("Error deleting discount with name {} from database", name, e);
        }
    }

    public void deleteDiscount(Discount discount) {
        logger.info("Deleting discount with id {}", discount.getDiscountId());
        String sql = "DELETE FROM discounts WHERE discount_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setObject(1, discount.getDiscountId());
            statement.executeUpdate();
            logger.info("Successfully deleted discount with id {}", discount.getDiscountId());
        } catch (SQLException e) {
            logger.error("Error deleting discount with id {} from database", discount.getDiscountId(), e);
        }
    }
}