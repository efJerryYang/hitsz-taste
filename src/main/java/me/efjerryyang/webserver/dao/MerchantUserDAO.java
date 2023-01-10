package me.efjerryyang.webserver.dao;

import me.efjerryyang.webserver.model.MerchantUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class MerchantUserDAO implements DAO<MerchantUser> {
    private static final Logger logger = LoggerFactory.getLogger(MerchantUserDAO.class);
    private final MysqlConnection mysqlConnection;
    private final Connection connection;

    @Autowired
    public MerchantUserDAO(MysqlConnection mysqlConnection) throws SQLException {
        logger.debug("Creating MerchantUserDAO");
        this.mysqlConnection = mysqlConnection;
        this.connection = mysqlConnection.getConnection();
    }


    @Override
    public MerchantUser create(MerchantUser object) {
        logger.debug("Creating MerchantUser with user_id {}, and merchant_id {}", object.getUserId(), object.getMerchantId());
        String sql = "INSERT INTO hitsz_taste.merchant_users (user_id, merchant_id, update_time, job_title, company) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setObject(1, object.getUserId());
            statement.setObject(2, object.getMerchantId());
            statement.setObject(3, object.getUpdateTime());
            statement.setObject(4, object.getJobTitle());
            statement.setObject(5, object.getCompany());
            statement.executeUpdate();
            logger.info("Successfully created MerchantUser with user_id {}, and merchant_id {}", object.getUserId(), object.getMerchantId());
            return object;
        } catch (SQLException e) {
            logger.error("Error creating MerchantUser in database", e);
            return null;
        }

    }

    @Override
    public MerchantUser update(MerchantUser object) {
        logger.debug("Updating MerchantUser with user_id {}, and merchant_id {}", object.getUserId(), object.getMerchantId());
        String sql = "UPDATE hitsz_taste.merchant_users SET update_time = ?, job_title = ?, company = ? WHERE user_id = ? AND merchant_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setObject(1, object.getUpdateTime());
            statement.setObject(2, object.getJobTitle());
            statement.setObject(3, object.getCompany());
            statement.setObject(4, object.getUserId());
            statement.setObject(5, object.getMerchantId());
            statement.executeUpdate();
            logger.info("Successfully updated MerchantUser with user_id {}, and merchant_id {}", object.getUserId(), object.getMerchantId());
            return object;
        } catch (SQLException e) {
            logger.error("Error updating MerchantUser in database", e);
            return null;
        }
    }

    @Override
    public MerchantUser update(MerchantUser objectOld, MerchantUser objectNew) {
        logger.debug("Updating MerchantUser with user_id {}, and merchant_id {}", objectOld.getUserId(), objectOld.getMerchantId());
        String sql = "UPDATE hitsz_taste.merchant_users SET update_time = ?, job_title = ?, company = ? WHERE user_id = ? AND merchant_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setObject(1, objectNew.getUpdateTime());
            statement.setObject(2, objectNew.getJobTitle());
            statement.setObject(3, objectNew.getCompany());
            statement.setObject(4, objectOld.getUserId());
            statement.setObject(5, objectOld.getMerchantId());
            statement.executeUpdate();
            logger.info("Successfully updated MerchantUser with user_id {}, and merchant_id {}", objectOld.getUserId(), objectOld.getMerchantId());
            return objectNew;
        } catch (SQLException e) {
            logger.error("Error updating MerchantUser in database", e);
            return null;
        }
    }

    @Override
    public MerchantUser getFromResultSet(ResultSet resultSet) throws SQLException {
        logger.debug("Getting MerchantUser from ResultSet");
        MerchantUser merchantUser = new MerchantUser();
        merchantUser.setUserId(resultSet.getObject("user_id", Long.class));
        merchantUser.setMerchantId(resultSet.getObject("merchant_id", Long.class));
        merchantUser.setUpdateTime(resultSet.getObject("update_time", Timestamp.class));
        merchantUser.setJobTitle(resultSet.getObject("job_title", String.class));
        merchantUser.setCompany(resultSet.getObject("company", String.class));
        return merchantUser;
    }

    @Override
    public List<MerchantUser> getAll() {
        logger.debug("Getting all MerchantUsers");
        String sql = "SELECT * FROM hitsz_taste.merchant_users";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            ResultSet resultSet = statement.executeQuery();
            List<MerchantUser> merchantUsers = new ArrayList<>();
            while (resultSet.next()) {
                merchantUsers.add(getFromResultSet(resultSet));
            }
            logger.info("Successfully got all MerchantUsers");
            return merchantUsers;
        } catch (SQLException e) {
            logger.error("Error getting all MerchantUsers from database", e);
            return null;
        }
    }

    @Override
    public void deleteAll() {
        logger.debug("Deleting all MerchantUsers");
        String sql = "DELETE FROM hitsz_taste.merchant_users";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.executeUpdate();
            logger.info("Successfully deleted all MerchantUsers");
        } catch (SQLException e) {
            logger.error("Error deleting all MerchantUsers from database", e);
        }
    }

    @Override
    public void delete(MerchantUser object) {
        logger.debug("Deleting MerchantUser with user_id {}, and merchant_id {}", object.getUserId(), object.getMerchantId());
        String sql = "DELETE FROM hitsz_taste.merchant_users WHERE user_id = ? AND merchant_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setObject(1, object.getUserId());
            statement.setObject(2, object.getMerchantId());
            statement.executeUpdate();
            logger.info("Successfully deleted MerchantUser with user_id {}, and merchant_id {}", object.getUserId(), object.getMerchantId());
        } catch (SQLException e) {
            logger.error("Error deleting MerchantUser from database", e);
        }
    }

    public MerchantUser getByUserIdAndMerchantId(Long userId, Long merchantId) {
        logger.debug("Getting MerchantUser with user_id {}, and merchant_id {}", userId, merchantId);
        String sql = "SELECT * FROM hitsz_taste.merchant_users WHERE user_id = ? AND merchant_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setObject(1, userId);
            statement.setObject(2, merchantId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                logger.info("Successfully got MerchantUser with user_id {}, and merchant_id {}", userId, merchantId);
                return getFromResultSet(resultSet);
            } else {
                logger.info("MerchantUser with user_id {}, and merchant_id {} does not exist", userId, merchantId);
                return null;
            }
        } catch (SQLException e) {
            logger.error("Error getting MerchantUser from database", e);
            return null;
        }
    }

    public List<Long> getMerchantIdByUserId(Long userId) {
        logger.debug("Getting MerchantUser with user_id {}", userId);
        String sql = "SELECT merchant_id FROM hitsz_taste.merchant_users WHERE user_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setObject(1, userId);
            ResultSet resultSet = statement.executeQuery();
            List<Long> merchantIds = new ArrayList<>();
            while (resultSet.next()) {
                merchantIds.add(resultSet.getObject("merchant_id", Long.class));
            }
            logger.info("Successfully got MerchantUser with user_id {}", userId);
            return merchantIds;
        } catch (SQLException e) {
            logger.error("Error getting MerchantUser from database", e);
            return null;
        }
    }
}
