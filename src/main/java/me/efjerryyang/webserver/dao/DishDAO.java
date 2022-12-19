package me.efjerryyang.webserver.dao;

import me.efjerryyang.webserver.model.Dish;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


@Repository
public class DishDAO implements DAO<Dish> {
    private static final Logger logger = LoggerFactory.getLogger(DishDAO.class);
    private final MysqlConnection mysqlConnection;
    private final Connection connection;

    @Autowired
    public DishDAO(MysqlConnection mysqlConnection) throws SQLException {
        this.mysqlConnection = mysqlConnection;
        logger.debug("Obtaining connection to database");
        this.connection = mysqlConnection.getConnection();
    }

    @Override
    public Dish create(Dish dish) {
        logger.info("Creating dish with id {}, name {}, and price {} by merchant {}", dish.getDishId(), dish.getName(), dish.getPrice(), dish.getMerchantId());
        String sql = "INSERT INTO hitsz_taste.dishes (dish_id, name, price, merchant_id) VALUES (?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setObject(1, dish.getDishId());
            statement.setObject(2, dish.getName());
            statement.setObject(3, dish.getPrice());
            statement.setObject(4, dish.getMerchantId());

            statement.executeUpdate();
            ResultSet resultSet = statement.getGeneratedKeys();
            if (resultSet.next()) {
                dish.setDishId(resultSet.getLong(1));
            }
            logger.info("Successfully created dish with id {}", dish.getDishId());
            return dish;
        } catch (SQLException e) {
            logger.error("Error creating dish in database", e);
            return null;
        }
    }

    @Override
    public Dish update(Dish dish) {
        logger.info("Updating dish with id {}, name {}, and price {} by merchant {}", dish.getDishId(), dish.getName(), dish.getPrice(), dish.getMerchantId());
        String sql = "UPDATE hitsz_taste.dishes SET name = ?, price = ?, merchant_id = ? WHERE dish_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            // Set the values for the dish in the SQL statement
            logger.debug("Setting dish values in SQL statement: name = {}, price = {}, merchant_id = {}, dish_id = {}", dish.getName(), dish.getPrice(), dish.getMerchantId(), dish.getDishId());
            statement.setObject(1, dish.getName());
            statement.setObject(2, dish.getPrice());
            statement.setObject(3, dish.getMerchantId());
            statement.setObject(4, dish.getDishId());

            statement.executeUpdate();
            logger.info("Successfully updated dish with id {}", dish.getDishId());
            return dish;
        } catch (SQLException e) {
            logger.error("Error updating dish in database", e);
            return null;
        }
    }

    @Override
    public Dish update(Dish objectOld, Dish objectNew) {
        logger.info("Updating dish with id {}, name {}, and price {} by merchant {}", objectOld.getDishId(), objectOld.getName(), objectOld.getPrice(), objectOld.getMerchantId());
        String sql = "UPDATE hitsz_taste.dishes SET name = ?, price = ?, merchant_id = ? WHERE dish_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            // Set the values for the dish in the SQL statement
            logger.debug("Setting dish values in SQL statement: name = {}, price = {}, merchant_id = {}, dish_id = {}", objectNew.getName(), objectNew.getPrice(), objectNew.getMerchantId(), objectOld.getDishId());
            statement.setObject(1, objectNew.getName());
            statement.setObject(2, objectNew.getPrice());
            statement.setObject(3, objectNew.getMerchantId());
            statement.setObject(4, objectOld.getDishId());

            statement.executeUpdate();
            logger.info("Successfully updated dish with id {}", objectOld.getDishId());
            return objectNew;
        } catch (SQLException e) {
            logger.error("Error updating dish in database", e);
            return null;
        }

    }

    @Override
    public Dish getFromResultSet(ResultSet resultSet) throws SQLException {
        Dish dish = new Dish();
        dish.setDishId(resultSet.getLong("dish_id"));
        dish.setName(resultSet.getString("name"));
        dish.setPrice(resultSet.getFloat("price"));
        dish.setMerchantId(resultSet.getLong("merchant_id"));
        return dish;
    }

    @Override
    public List<Dish> getAll() {
        logger.info("Getting all dishes");
        String sql = "SELECT * FROM hitsz_taste.dishes";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            ResultSet resultSet = statement.executeQuery();
            List<Dish> dishes = new ArrayList<>();
            while (resultSet.next()) {
                dishes.add(getFromResultSet(resultSet));
            }
            logger.info("Successfully retrieved all dishes");
            return dishes;
        } catch (SQLException e) {
            logger.error("Error retrieving all dishes from database", e);
            return null;
        }
    }

    @Override
    public void deleteAll() {
        logger.info("Deleting all dishes");
        String sql = "DELETE FROM hitsz_taste.dishes";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.executeUpdate();
            logger.info("Successfully deleted all dishes");
        } catch (SQLException e) {
            logger.error("Error deleting all dishes from database", e);
        }
    }

    @Override
    public void delete(Dish object) {
        logger.info("Deleting dish with id {}", object.getDishId());
        String sql = "DELETE FROM hitsz_taste.dishes WHERE dish_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setObject(1, object.getDishId());
            statement.executeUpdate();
            logger.info("Successfully deleted dish with id {}", object.getDishId());
        } catch (SQLException e) {
            logger.error("Error deleting dish from database", e);
        }
    }

    public List<Dish> getAllByMerchantId(Long merchantId) {
        logger.info("Getting all dishes by merchant id {}", merchantId);
        String sql = "SELECT * FROM hitsz_taste.dishes WHERE merchant_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setObject(1, merchantId);
            ResultSet resultSet = statement.executeQuery();
            List<Dish> dishes = new ArrayList<>();
            while (resultSet.next()) {
                dishes.add(getFromResultSet(resultSet));
            }
            logger.info("Successfully retrieved all dishes by merchant id {}", merchantId);
            return dishes;
        } catch (SQLException e) {
            logger.error("Error retrieving all dishes by merchant id {} from database", merchantId, e);
            return null;
        }
    }

    public List<Dish> getAllByName(String name) {
        logger.info("Getting all dishes by name {}", name);
        String sql = "SELECT * FROM hitsz_taste.dishes WHERE name = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setObject(1, name);
            ResultSet resultSet = statement.executeQuery();
            List<Dish> dishes = new ArrayList<>();
            while (resultSet.next()) {
                dishes.add(getFromResultSet(resultSet));
            }
            logger.info("Successfully retrieved all dishes by name {}", name);
            return dishes;
        } catch (SQLException e) {
            logger.error("Error retrieving all dishes by name {} from database", name, e);
            return null;
        }
    }

    public Dish getById(Long dishId) {
        logger.info("Getting dish by id {}", dishId);
        String sql = "SELECT * FROM hitsz_taste.dishes WHERE dish_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setObject(1, dishId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                Dish dish = getFromResultSet(resultSet);
                logger.info("Successfully retrieved dish by id {}", dishId);
                return dish;
            } else {
                logger.info("No dish found with id {}", dishId);
                return null;
            }
        } catch (SQLException e) {
            logger.error("Error retrieving dish by id {} from database", dishId, e);
            return null;
        }
    }

    public void deleteById(Long dishId) {
        logger.info("Deleting dish by id {}", dishId);
        String sql = "DELETE FROM hitsz_taste.dishes WHERE dish_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setObject(1, dishId);
            statement.executeUpdate();
            logger.info("Successfully deleted dish by id {}", dishId);
        } catch (SQLException e) {
            logger.error("Error deleting dish by id {} from database", dishId, e);
        }
    }

    public void deleteAllByMerchantId(Long merchantId) {
        logger.info("Deleting dishes by merchant id {}", merchantId);
        String sql = "DELETE FROM hitsz_taste.dishes WHERE merchant_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setObject(1, merchantId);
            statement.executeUpdate();
            logger.info("Successfully deleted dishes by merchant id {}", merchantId);
        } catch (SQLException e) {
            logger.error("Error deleting dishes by merchant id {} from database", merchantId, e);
        }
    }

    public List<Dish> getAllByNameAndMerchantId(String name, Long merchantId) {
        logger.info("Getting all dishes by name {} and merchant id {}", name, merchantId);
        String sql = "SELECT * FROM dishes WHERE merchant_id = ? AND name = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setObject(1, merchantId);
            statement.setObject(2, name);
            ResultSet resultSet = statement.executeQuery();
            List<Dish> dishes = new ArrayList<>();
            while (resultSet.next()) {
                dishes.add(getFromResultSet(resultSet));
            }
            logger.info("Successfully retrieved all dishes by name {} and merchant id {}", name, merchantId);
            return dishes;
        } catch (SQLException e) {
            logger.error("Error retrieving all dishes by name {} and merchant id {} from database", name, merchantId, e);
            return null;
        }
    }


    public List<Dish> getAllByMerchantIdAndPriceRange(Long merchantId, Float minPrice, Float maxPrice) {
        logger.info("Getting all dishes by merchant id {} and price range {} - {}", merchantId, minPrice, maxPrice);
        String sql = "SELECT * FROM dishes WHERE merchant_id = ? AND price >= ? AND price <= ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setObject(1, merchantId);
            statement.setObject(2, minPrice);
            statement.setObject(3, maxPrice);
            ResultSet resultSet = statement.executeQuery();
            List<Dish> dishes = new ArrayList<>();
            while (resultSet.next()) {
                dishes.add(getFromResultSet(resultSet));
            }
            logger.info("Successfully retrieved all dishes by merchant id {} and price range {} - {}", merchantId, minPrice, maxPrice);
            return dishes;
        } catch (SQLException e) {
            logger.error("Error retrieving all dishes by merchant id {} and price range {} - {} from database", merchantId, minPrice, maxPrice, e);
            return null;
        }
    }

    public List<Dish> getAllByPriceRange(Float minPrice, Float maxPrice) {
        logger.info("Getting all dishes by price range {} - {}", minPrice, maxPrice);
        String sql = "SELECT * FROM hitsz_taste.dishes WHERE price >= ? AND price <= ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setObject(1, minPrice);
            statement.setObject(2, maxPrice);
            ResultSet resultSet = statement.executeQuery();
            List<Dish> dishes = new ArrayList<>();
            while (resultSet.next()) {
                dishes.add(getFromResultSet(resultSet));
            }
            logger.info("Successfully retrieved all dishes by price range {} - {}", minPrice, maxPrice);
            return dishes;
        } catch (SQLException e) {
            logger.error("Error retrieving all dishes by price range {} - {} from database", minPrice, maxPrice, e);
            return null;
        }
    }


}