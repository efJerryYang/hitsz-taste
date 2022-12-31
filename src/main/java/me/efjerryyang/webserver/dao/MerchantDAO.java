package me.efjerryyang.webserver.dao;

import me.efjerryyang.webserver.model.Merchant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class MerchantDAO implements DAO<Merchant> {
    private static final Logger logger = LoggerFactory.getLogger(MerchantDAO.class);
    private final MysqlConnection mysqlConnection;
    private final Connection connection;

    @Autowired
    public MerchantDAO(MysqlConnection mysqlConnection) throws SQLException {
        this.mysqlConnection = mysqlConnection;
        logger.debug("Obtaining connection to database");
        this.connection = mysqlConnection.getConnection();
    }

    @Override
    public Merchant create(Merchant merchant) {
        logger.info("Creating merchant with id {} and name {}", merchant.getMerchantId(), merchant.getName());
        String sql = "INSERT INTO hitsz_taste.merchants (merchant_id, name, active) VALUES (?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setObject(1, merchant.getMerchantId());
            statement.setObject(2, merchant.getName());
            statement.setObject(3, merchant.getIsActive());
            statement.executeUpdate();
            ResultSet resultSet = statement.getGeneratedKeys();
            if (resultSet.next()) {
                merchant.setMerchantId(resultSet.getLong(1));
            }
            logger.info("Successfully created merchant with id {}", merchant.getMerchantId());
            return merchant;
        } catch (SQLException e) {
            logger.error("Error creating merchant in database", e);
            return null;
        }
    }

    @Override
    public Merchant update(Merchant merchant) {
        logger.info("Updating merchant with id {} and name {}", merchant.getMerchantId(), merchant.getName());
        String sql = "UPDATE hitsz_taste.merchants SET name = ?, active = ? WHERE merchant_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            // Set the values for the merchant in the SQL statement
            logger.debug("Setting merchant values in SQL statement: name = {}, merchant_id = {}", merchant.getName(), merchant.getMerchantId());
            statement.setObject(1, merchant.getName());
            statement.setObject(2, merchant.getIsActive());
            statement.setObject(3, merchant.getMerchantId());
            // Execute the SQL statement
            statement.executeUpdate();
            logger.info("Successfully updated merchant with id {}", merchant.getMerchantId());
            return merchant;
        } catch (SQLException e) {
            logger.error("Error updating merchant in database", e);
            return null;
        }
    }

    @Override
    public Merchant update(Merchant objectOld, Merchant objectNew) {
        logger.info("Updating merchant with id {} and name {}", objectOld.getMerchantId(), objectOld.getName());
        String sql = "UPDATE hitsz_taste.merchants SET name = ?, active = ? WHERE merchant_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            // Set the values for the merchant in the SQL statement
            logger.debug("Setting merchant values in SQL statement: name = {}, merchant_id = {}", objectNew.getName(), objectNew.getMerchantId());
            statement.setObject(1, objectNew.getName());
            statement.setObject(2, objectNew.getIsActive());
            statement.setObject(3, objectOld.getMerchantId());
            // Execute the SQL statement
            statement.executeUpdate();
            logger.info("Successfully updated merchant with id {}", objectOld.getMerchantId());
            return objectNew;
        } catch (SQLException e) {
            logger.error("Error updating merchant in database", e);
            return null;
        }
    }

    @Override
    public Merchant getFromResultSet(ResultSet resultSet) throws SQLException {
        logger.debug("Creating merchant from result set");
        Merchant merchant = new Merchant();
        merchant.setMerchantId(resultSet.getLong("merchant_id"));
        merchant.setName(resultSet.getString("name"));
        merchant.setIsActive(resultSet.getBoolean("active"));
        return merchant;
    }

    @Override
    public List<Merchant> getAll() {
        logger.info("Getting all merchants");
        String sql = "SELECT * FROM hitsz_taste.merchants";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            ResultSet resultSet = statement.executeQuery();
            List<Merchant> merchants = new ArrayList<>();
            while (resultSet.next()) {
                Merchant merchant = getFromResultSet(resultSet);
                merchants.add(merchant);
                logger.debug("Added merchant with id {} to list of merchants", merchant.getMerchantId());
            }
            logger.info("Successfully retrieved all merchants");
            return merchants;
        } catch (SQLException e) {
            logger.error("Error retrieving merchants from database", e);
            return null;
        }
    }

    @Override
    public void deleteAll() {
        logger.info("Deleting all merchants");
        String sql = "DELETE FROM hitsz_taste.merchants";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.executeUpdate();
            logger.info("Successfully deleted all merchants");
        } catch (SQLException e) {
            logger.error("Error deleting merchants from database", e);
        }
    }

    @Override
    public void delete(Merchant object) {
        logger.info("Deleting merchant with id {}", object.getMerchantId());
        String sql = "DELETE FROM hitsz_taste.merchants WHERE merchant_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setObject(1, object.getMerchantId());
            statement.executeUpdate();
            logger.info("Successfully deleted merchant with id {}", object.getMerchantId());
        } catch (SQLException e) {
            logger.error("Error deleting merchant from database", e);
        }
    }

    public Merchant getById(Long merchantId) {
        logger.info("Getting merchant with id {}", merchantId);
        String sql = "SELECT * FROM hitsz_taste.merchants WHERE merchant_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setObject(1, merchantId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                Merchant merchant = getFromResultSet(resultSet);
                logger.info("Successfully retrieved merchant with id {}", merchantId);
                return merchant;
            }
            logger.info("No merchant found with id {}", merchantId);
            return null;
        } catch (SQLException e) {
            logger.error("Error retrieving merchant from database", e);
            return null;
        }
    }

    public void deleteById(Long merchantId) {
        logger.info("Deleting merchant with id {}", merchantId);
        String sql = "DELETE FROM hitsz_taste.merchants WHERE merchant_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setObject(1, merchantId);
            statement.executeUpdate();
            logger.info("Successfully deleted merchant with id {}", merchantId);
        } catch (SQLException e) {
            logger.error("Error deleting merchant from database", e);
        }
    }

    public void activeById(Long merchantId) {
        logger.info("Activating merchant with id {}", merchantId);
        String sql = "UPDATE hitsz_taste.merchants SET active = true WHERE merchant_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setObject(1, merchantId);

            statement.executeUpdate();
            logger.info("Successfully activated merchant with id {}", merchantId);
        } catch (SQLException e) {
            logger.error("Error activating merchant from database", e);
        }
    }

    public void disableById(Long merchantId) {
        logger.info("Deactivating merchant with id {}", merchantId);
        String sql = "UPDATE hitsz_taste.merchants SET active = false WHERE merchant_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setObject(1, merchantId);
            statement.executeUpdate();
            logger.info("Successfully deactivated merchant with id {}", merchantId);
        } catch (SQLException e) {
            logger.error("Error deactivating merchant from database", e);
        }
    }

    public List<Merchant> getAllActive() {
        logger.info("Getting all active merchants");
        String sql = "SELECT * FROM hitsz_taste.merchants WHERE active = true";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            ResultSet resultSet = statement.executeQuery();
            List<Merchant> merchants = new ArrayList<>();
            while (resultSet.next()) {
                Merchant merchant = getFromResultSet(resultSet);
                merchants.add(merchant);
                logger.debug("Added merchant with id {} to list of merchants", merchant.getMerchantId());
            }
            logger.info("Successfully retrieved all active merchants");
            return merchants;
        } catch (SQLException e) {
            logger.error("Error retrieving merchants from database", e);
            return null;
        }
    }

    public List<Merchant> getAllInactive() {
        logger.info("Getting all inactive merchants");
        String sql = "SELECT * FROM hitsz_taste.merchants WHERE active = false";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            ResultSet resultSet = statement.executeQuery();
            List<Merchant> merchants = new ArrayList<>();
            while (resultSet.next()) {
                Merchant merchant = getFromResultSet(resultSet);
                merchants.add(merchant);
                logger.debug("Added merchant with id {} to list of merchants", merchant.getMerchantId());
            }
            logger.info("Successfully retrieved all inactive merchants");
            return merchants;
        } catch (SQLException e) {
            logger.error("Error retrieving merchants from database", e);
            return null;
        }
    }

    public List<Merchant> getAllMatching(String query) {
        logger.info("Getting all merchants matching query {}", query);
        String sql = "SELECT * FROM hitsz_taste.merchants WHERE name LIKE ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setObject(1, "%" + query + "%");
            ResultSet resultSet = statement.executeQuery();
            List<Merchant> merchants = new ArrayList<>();
            while (resultSet.next()) {
                Merchant merchant = getFromResultSet(resultSet);
                merchants.add(merchant);
                logger.debug("Added merchant with id {} to list of merchants", merchant.getMerchantId());
            }
            logger.info("Successfully retrieved all merchants matching query {}", query);
            return merchants;
        } catch (SQLException e) {
            logger.error("Error retrieving merchants from database", e);
            return null;
        }
    }

    public List<Merchant> getAllByNames(List<String> names) {
        logger.info("Getting all merchants with names {}", names);
        String sql = "SELECT * FROM hitsz_taste.merchants WHERE name IN (";
        for (int i = 0; i < names.size(); i++) {
            sql += "?";
            if (i != names.size() - 1) {
                sql += ", ";
            }
        }
        sql += ")";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            for (int i = 0; i < names.size(); i++) {
                statement.setObject(i + 1, names.get(i));
            }
            ResultSet resultSet = statement.executeQuery();
            List<Merchant> merchants = new ArrayList<>();
            while (resultSet.next()) {
                Merchant merchant = getFromResultSet(resultSet);
                merchants.add(merchant);
                logger.debug("Added merchant with id {} to list of merchants", merchant.getMerchantId());
            }
            logger.info("Successfully retrieved all merchants with names {}", names);
            return merchants;
        } catch (SQLException e) {
            logger.error("Error retrieving merchants from database", e);
            return null;
        }
    }
}