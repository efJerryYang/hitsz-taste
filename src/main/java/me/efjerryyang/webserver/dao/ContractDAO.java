package me.efjerryyang.webserver.dao;

import me.efjerryyang.webserver.model.Contract;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class ContractDAO implements DAO<Contract> {
    private static final Logger logger = LoggerFactory.getLogger(ContractDAO.class);
    private final MysqlConnection mysqlConnection;
    private final Connection connection;

    @Autowired
    public ContractDAO(MysqlConnection mysqlConnection) throws SQLException {
        this.mysqlConnection = mysqlConnection;
        logger.debug("Obtaining connection to database");
        this.connection = mysqlConnection.getConnection();
    }

    @Override
    public Contract create(Contract contract) {
        logger.info("Creating contract with cafeteria id {}, merchant id {}, start date {}, end date {}", contract.getCafeteriaId(), contract.getMerchantId(), contract.getStartTimestamp(), contract.getEndTimestamp());
        String sql = "INSERT INTO hitsz_taste.contracts (cafeteria_id, merchant_id, start_date, end_date) VALUES (?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setObject(1, contract.getCafeteriaId());
            statement.setObject(2, contract.getMerchantId());
            statement.setObject(3, contract.getStartTimestamp());
            statement.setObject(4, contract.getEndTimestamp());

            statement.executeUpdate();
            logger.info("Successfully created contract with cafeteria id {}, merchant id {}, start date {}, end date {}", contract.getCafeteriaId(), contract.getMerchantId(), contract.getStartTimestamp(), contract.getEndTimestamp());
            return contract;
        } catch (SQLException e) {
            logger.error("Error creating contract in database", e);
            return null;
        }
    }

    @Override
    public Contract update(Contract object) {
        logger.info("Updating contract with cafeteria id {}, merchant id {}, start date {}, end date {}", object.getCafeteriaId(), object.getMerchantId(), object.getStartTimestamp(), object.getEndTimestamp());
        String sql = "UPDATE hitsz_taste.contracts SET cafeteria_id = ?, merchant_id = ?, start_date = ?, end_date = ? WHERE cafeteria_id = ? AND merchant_id = ? AND start_date = ? AND end_date = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            // Set the values for the contract in the SQL statement
            logger.debug("Setting contract values in SQL statement: cafeteria_id = {}, merchant_id = {}, start_date = {}, end_date = {}", object.getCafeteriaId(), object.getMerchantId(), object.getStartTimestamp(), object.getEndTimestamp());
            statement.setObject(1, object.getCafeteriaId());
            statement.setObject(2, object.getMerchantId());
            statement.setObject(3, object.getStartTimestamp());
            statement.setObject(4, object.getEndTimestamp());
            statement.setObject(5, object.getCafeteriaId());
            statement.setObject(6, object.getMerchantId());
            statement.setObject(7, object.getStartTimestamp());
            statement.setObject(8, object.getEndTimestamp());
            statement.executeUpdate();
            logger.info("Successfully updated contract with cafeteria id {}, merchant id {}, start date {}, end date {}", object.getCafeteriaId(), object.getMerchantId(), object.getStartTimestamp(), object.getEndTimestamp());
            return object;
        } catch (SQLException e) {
            logger.error("Error updating contract in database", e);
            return null;
        }
    }

    @Override
    public Contract update(Contract contractOld, Contract contractNew) {
        logger.info("Updating contract with cafeteria id {}, merchant id {}, start date {}, end date {}", contractOld.getCafeteriaId(), contractOld.getMerchantId(), contractOld.getStartTimestamp(), contractOld.getEndTimestamp());
        String sql = "UPDATE hitsz_taste.contracts SET cafeteria_id = ?, merchant_id = ?, start_date = ?, end_date = ? WHERE cafeteria_id = ? AND merchant_id = ? AND start_date = ? AND end_date = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            // Set the values for the contract in the SQL statement
            logger.debug("Setting contract values in SQL statement: cafeteria_id = {}, merchant_id = {}, start_date = {}, end_date = {}", contractNew.getCafeteriaId(), contractNew.getMerchantId(), contractNew.getStartTimestamp(), contractNew.getEndTimestamp());
            statement.setObject(1, contractNew.getCafeteriaId());
            statement.setObject(2, contractNew.getMerchantId());
            statement.setObject(3, contractNew.getStartTimestamp());
            statement.setObject(4, contractNew.getEndTimestamp());
            statement.setObject(5, contractOld.getCafeteriaId());
            statement.setObject(6, contractOld.getMerchantId());
            statement.setObject(7, contractOld.getStartTimestamp());
            statement.setObject(8, contractOld.getEndTimestamp());
            statement.executeUpdate();
            logger.info("Successfully updated contract with cafeteria id {}, merchant id {}, start date {}, end date {}", contractNew.getCafeteriaId(), contractNew.getMerchantId(), contractNew.getStartTimestamp(), contractNew.getEndTimestamp());
            return contractNew;
        } catch (SQLException e) {
            logger.error("Error updating contract in database", e);
            return null;
        }
    }

    @Override
    public Contract getFromResultSet(ResultSet resultSet) throws SQLException {
        logger.debug("Getting contract from result set");
        return new Contract(
                resultSet.getObject("cafeteria_id", Long.class),
                resultSet.getObject("merchant_id", Long.class),
                resultSet.getObject("start_timestamp", Timestamp.class),
                resultSet.getObject("end_timestamp", Timestamp.class)
        );
    }

    @Override
    public List<Contract> getAll() {
        logger.info("Getting all contracts");
        String sql = "SELECT * FROM hitsz_taste.contracts";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            ResultSet resultSet = statement.executeQuery();
            List<Contract> contracts = new ArrayList<>();
            while (resultSet.next()) {
                contracts.add(getFromResultSet(resultSet));
            }
            logger.info("Successfully got all contracts, total number of contracts: {}", contracts.size());
            return contracts;
        } catch (SQLException e) {
            logger.error("Error getting all contracts from database", e);
            return null;
        }
    }

    @Override
    public void deleteAll() {
        logger.info("Deleting all contracts");
        String sql = "DELETE FROM hitsz_taste.contracts";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.executeUpdate();
            logger.info("Successfully deleted all contracts, total number of contracts: {}", getAll().size());
        } catch (SQLException e) {
            logger.error("Error deleting all contracts from database", e);
        }
    }

    @Override
    public void delete(Contract object) {
        logger.info("Deleting contract with cafeteria id {}, merchant id {}, start date {}, end date {}", object.getCafeteriaId(), object.getMerchantId(), object.getStartTimestamp(), object.getEndTimestamp());
        String sql = "DELETE FROM hitsz_taste.contracts WHERE cafeteria_id = ? AND merchant_id = ? AND start_date = ? AND end_date = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setObject(1, object.getCafeteriaId());
            statement.setObject(2, object.getMerchantId());
            statement.setObject(3, object.getStartTimestamp());
            statement.setObject(4, object.getEndTimestamp());
            statement.executeUpdate();
            logger.info("Successfully deleted contract with cafeteria id {}, merchant id {}, start date {}, end date {}", object.getCafeteriaId(), object.getMerchantId(), object.getStartTimestamp(), object.getEndTimestamp());
        } catch (SQLException e) {
            logger.error("Error deleting contract from database", e);
        }
    }

    public List<Long> getMerchantIdsByCafeteriaId(Long cafeteriaId) {
        logger.info("Getting merchants by cafeteria id {}", cafeteriaId);
        String sql = "SELECT merchant_id FROM hitsz_taste.contracts WHERE cafeteria_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setObject(1, cafeteriaId);
            ResultSet resultSet = statement.executeQuery();
            List<Long> merchantIds = new ArrayList<>();
            while (resultSet.next()) {
                merchantIds.add(resultSet.getObject("merchant_id", Long.class));
            }
            logger.info("Successfully got merchants by cafeteria id {}, total number of merchants: {}", cafeteriaId, merchantIds.size());
            return merchantIds;
        } catch (SQLException e) {
            logger.error("Error getting merchants by cafeteria id from database", e);
            return null;
        }
    }

    public List<Long> getCafeteriaIdsByMerchantId(Long merchantId) {
        logger.info("Getting cafeterias by merchant id {}", merchantId);
        String sql = "SELECT cafeteria_id FROM hitsz_taste.contracts WHERE merchant_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setObject(1, merchantId);
            ResultSet resultSet = statement.executeQuery();
            List<Long> cafeteriaIds = new ArrayList<>();
            while (resultSet.next()) {
                cafeteriaIds.add(resultSet.getObject("cafeteria_id", Long.class));
            }
            logger.info("Successfully got cafeterias by merchant id {}, total number of cafeterias: {}", merchantId, cafeteriaIds.size());
            return cafeteriaIds;
        } catch (SQLException e) {
            logger.error("Error getting cafeterias by merchant id from database", e);
            return null;
        }
    }
}