package me.efjerryyang.webserver.dao;

import me.efjerryyang.webserver.model.DishDiscount;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class DishDiscountDAO implements DAO<DishDiscount> {
    private static final Logger logger = LoggerFactory.getLogger(DishDiscountDAO.class);
    private final MysqlConnection mysqlConnection;
    private final Connection connection;

    @Autowired
    public DishDiscountDAO(MysqlConnection mysqlConnection) throws SQLException {
        logger.debug("Creating DishDiscountDAO");
        this.mysqlConnection = mysqlConnection;
        logger.debug("Getting connection to MySQL server");
        this.connection = mysqlConnection.getConnection();
    }

    @Override
    public DishDiscount create(DishDiscount object) {
        logger.debug("Creating new dish discount with dish_id = {} and discount_id = {}, date range = {} to {}",
                object.getDishId(), object.getDiscountId(), object.getStartDate(), object.getEndDate());
        String sql = "INSERT INTO hitsz_taste.dish_discounts (dish_id, discount_id, start_date, end_date) VALUES (?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setObject(1, object.getDishId());
            statement.setObject(2, object.getDiscountId());
            statement.setObject(3, object.getStartDate());
            statement.setObject(4, object.getEndDate());
            statement.executeUpdate();
            logger.debug("Successfully created new dish discount with dish_id = {} and discount_id = {}, date range = {} to {}",
                    object.getDishId(), object.getDiscountId(), object.getStartDate(), object.getEndDate());
            return object;
        } catch (SQLException e) {
            logger.error("Error creating new dish discount", e);
            return null;
        }

    }

    @Override
    public DishDiscount update(DishDiscount object) {
        logger.info("Updating dish discount with dish_id = {} and discount_id = {}, date range = {} to {}",
                object.getDishId(), object.getDiscountId(), object.getStartDate(), object.getEndDate());
        String sql = "UPDATE hitsz_taste.dish_discounts SET start_date = ?, end_date = ? WHERE dish_id = ? AND discount_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setObject(1, object.getStartDate());
            statement.setObject(2, object.getEndDate());
            statement.setObject(3, object.getDishId());
            statement.setObject(4, object.getDiscountId());
            statement.executeUpdate();
            logger.info("Successfully updated dish discount with dish_id = {} and discount_id = {}, date range = {} to {}",
                    object.getDishId(), object.getDiscountId(), object.getStartDate(), object.getEndDate());
            return object;
        } catch (SQLException e) {
            logger.error("Error updating dish discount", e);
            return null;
        }
    }

    @Override
    public DishDiscount update(DishDiscount objectOld, DishDiscount objectNew) {
        logger.info("Updating dish discount with dish_id = {} and discount_id = {}, date range = {} to {}",
                objectOld.getDishId(), objectOld.getDiscountId(), objectOld.getStartDate(), objectOld.getEndDate());
        String sql = "UPDATE hitsz_taste.dish_discounts SET dish_id = ?, discount_id = ?, start_date = ?, end_date = ? WHERE dish_id = ? AND discount_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setObject(1, objectNew.getDishId());
            statement.setObject(2, objectNew.getDiscountId());
            statement.setObject(3, objectNew.getStartDate());
            statement.setObject(4, objectNew.getEndDate());
            statement.setObject(5, objectOld.getDishId());
            statement.setObject(6, objectOld.getDiscountId());
            statement.executeUpdate();
            logger.info("Successfully updated dish discount with dish_id = {} and discount_id = {}, date range = {} to {}",
                    objectNew.getDishId(), objectNew.getDiscountId(), objectNew.getStartDate(), objectNew.getEndDate());
            return objectNew;
        } catch (SQLException e) {
            logger.error("Error updating dish discount", e);
            return null;
        }
    }

    @Override
    public DishDiscount getFromResultSet(ResultSet resultSet) throws SQLException {
        return new DishDiscount(resultSet.getObject("dish_id", Long.class),
                resultSet.getObject("discount_id", Long.class),
                resultSet.getObject("start_date", Date.class),
                resultSet.getObject("end_date", Date.class));
    }

    @Override
    public List<DishDiscount> getAll() {
        logger.debug("Getting all dish discounts");
        String sql = "SELECT * FROM hitsz_taste.dish_discounts";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            ResultSet resultSet = statement.executeQuery();
            List<DishDiscount> dishDiscounts = new ArrayList<>();
            while (resultSet.next()) {
                dishDiscounts.add(getFromResultSet(resultSet));
            }
            logger.debug("Successfully got all dish discounts");
            return dishDiscounts;
        } catch (SQLException e) {
            logger.error("Error getting all dish discounts", e);
            return null;
        }
    }

    @Override
    public void deleteAll() {
        logger.debug("Deleting all dish discounts");
        String sql = "DELETE FROM hitsz_taste.dish_discounts";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.executeUpdate();
            logger.debug("Successfully deleted all dish discounts");
        } catch (SQLException e) {
            logger.error("Error deleting all dish discounts", e);
        }
    }

    @Override
    public void delete(DishDiscount object) {
        logger.debug("Deleting dish discount with dish_id = {} and discount_id = {}, date range = {} to {}",
                object.getDishId(), object.getDiscountId(), object.getStartDate(), object.getEndDate());
        String sql = "DELETE FROM hitsz_taste.dish_discounts WHERE dish_id = ? AND discount_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setObject(1, object.getDishId());
            statement.setObject(2, object.getDiscountId());
            statement.executeUpdate();
            logger.debug("Successfully deleted dish discount with dish_id = {} and discount_id = {}, date range = {} to {}",
                    object.getDishId(), object.getDiscountId(), object.getStartDate(), object.getEndDate());
        } catch (SQLException e) {
            logger.error("Error deleting dish discount", e);
        }
    }
}
