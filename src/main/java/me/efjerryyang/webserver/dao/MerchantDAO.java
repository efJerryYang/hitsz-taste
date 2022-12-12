package me.efjerryyang.webserver.dao;

import me.efjerryyang.webserver.dao.MySQLConnection;
import me.efjerryyang.webserver.model.Merchant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class MerchantDAO {
    private static final Logger logger = LoggerFactory.getLogger(MerchantDAO.class);
    private MySQLConnection mysqlConnection;
    private Connection connection;

    @Autowired
    public MerchantDAO(MySQLConnection mySQLConnection) throws SQLException {
        this.mysqlConnection = mySQLConnection;
        logger.debug("Obtaining connection to database");
        this.connection = mySQLConnection.getConnection();
    }

    public Merchant createMerchant(Merchant merchant) {
        logger.info("Creating merchant with id {} and name {}", merchant.getMerchantId(), merchant.getName());
        String sql = "INSERT INTO merchants (merchant_id, name) VALUES (?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setObject(1, merchant.getMerchantId());
            statement.setObject(2, merchant.getName());
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

    public Merchant updateMerchant(Merchant merchant) {
        logger.info("Updating merchant with id {} and name {}", merchant.getMerchantId(), merchant.getName());
        String sql = "UPDATE merchants SET name = ? WHERE merchant_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            // Set the values for the merchant in the SQL statement
            logger.debug("Setting merchant values in SQL statement: name = {}, merchant_id = {}", merchant.getName(), merchant.getMerchantId());
            statement.setObject(1, merchant.getName());
            statement.setObject(2, merchant.getMerchantId());
            statement.executeUpdate();
            logger.info("Successfully updated merchant with id {}", merchant.getMerchantId());
            return merchant;
        } catch (SQLException e) {
            logger.error("Error updating merchant in database", e);
            return null;
        }
    }

    public Merchant getMerchantById(Long merchantId) {
        logger.info("Getting merchant with id {}", merchantId);
        String sql = "SELECT * FROM merchants WHERE merchant_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setObject(1, merchantId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                Merchant merchant = new Merchant();
                merchant.setMerchantId(resultSet.getLong("merchant_id"));
                merchant.setName(resultSet.getString("name"));
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

    public List<Merchant> getAllMerchants() {
        logger.info("Getting all merchants");
        String sql = "SELECT * FROM merchants";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            ResultSet resultSet = statement.executeQuery();
            List<Merchant> merchants = new ArrayList<>();
            while (resultSet.next()) {
                Merchant merchant = getMerhcantFromResultSet(resultSet);
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

    public void deleteMerchantById(Long merchantId) {
        logger.info("Deleting merchant with id {}", merchantId);
        String sql = "DELETE FROM merchants WHERE merchant_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setObject(1, merchantId);
            statement.executeUpdate();
            logger.info("Successfully deleted merchant with id {}", merchantId);
        } catch (SQLException e) {
            logger.error("Error deleting merchant from database", e);
        }
    }

    public void deleteAllMerchants() {
        logger.info("Deleting all merchants");
        String sql = "DELETE FROM merchants";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.executeUpdate();
            logger.info("Successfully deleted all merchants");
        } catch (SQLException e) {
            logger.error("Error deleting merchants from database", e);
        }
    }

    public void activeMerchantById(Long merchantId) {
        logger.info("Activating merchant with id {}", merchantId);
        String sql = "UPDATE merchants SET active = true WHERE merchant_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setObject(1, merchantId);
            statement.executeUpdate();
            logger.info("Successfully activated merchant with id {}", merchantId);
        } catch (SQLException e) {
            logger.error("Error activating merchant from database", e);
        }
    }

    public void deactivateMerchantById(Long merchantId) {
        logger.info("Deactivating merchant with id {}", merchantId);
        String sql = "UPDATE merchants SET active = false WHERE merchant_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setObject(1, merchantId);
            statement.executeUpdate();
            logger.info("Successfully deactivated merchant with id {}", merchantId);
        } catch (SQLException e) {
            logger.error("Error deactivating merchant from database", e);
        }
    }

    public Merchant getMerhcantFromResultSet(ResultSet resultSet) throws SQLException {
        Merchant merchant = new Merchant();
        merchant.setMerchantId(resultSet.getLong("merchant_id"));
        merchant.setName(resultSet.getString("name"));
        return merchant;
    }


}