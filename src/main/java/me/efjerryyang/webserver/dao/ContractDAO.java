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
    private final MySQLConnection mysqlConnection;
    private final Connection connection;

    @Autowired
    public ContractDAO(MySQLConnection mysqlConnection) throws SQLException {
        this.mysqlConnection = mysqlConnection;
        logger.debug("Obtaining connection to database");
        this.connection = mysqlConnection.getConnection();
    }

    @Override
    public Contract create(Contract contract) {
        logger.info("Creating contract with cafeteria id {}, merchant id {}, start date {}, end date {}", contract.getCafeteriaId(), contract.getMerchantId(), contract.getStartDate(), contract.getEndDate());
        String sql = "INSERT INTO hitsz_taste.contracts (cafeteria_id, merchant_id, start_date, end_date) VALUES (?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setObject(1, contract.getCafeteriaId());
            statement.setObject(2, contract.getMerchantId());
            statement.setObject(3, contract.getStartDate());
            statement.setObject(4, contract.getEndDate());

            statement.executeUpdate();
            logger.info("Successfully created contract with cafeteria id {}, merchant id {}, start date {}, end date {}", contract.getCafeteriaId(), contract.getMerchantId(), contract.getStartDate(), contract.getEndDate());
            return contract;
        } catch (SQLException e) {
            logger.error("Error creating contract in database", e);
            return null;
        }
    }

    @Override
    public Contract update(Contract object) {
        logger.info("Updating contract with cafeteria id {}, merchant id {}, start date {}, end date {}", object.getCafeteriaId(), object.getMerchantId(), object.getStartDate(), object.getEndDate());
        String sql = "UPDATE hitsz_taste.contracts SET cafeteria_id = ?, merchant_id = ?, start_date = ?, end_date = ? WHERE cafeteria_id = ? AND merchant_id = ? AND start_date = ? AND end_date = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            // Set the values for the contract in the SQL statement
            logger.debug("Setting contract values in SQL statement: cafeteria_id = {}, merchant_id = {}, start_date = {}, end_date = {}", object.getCafeteriaId(), object.getMerchantId(), object.getStartDate(), object.getEndDate());
            statement.setObject(1, object.getCafeteriaId());
            statement.setObject(2, object.getMerchantId());
            statement.setObject(3, object.getStartDate());
            statement.setObject(4, object.getEndDate());
            statement.setObject(5, object.getCafeteriaId());
            statement.setObject(6, object.getMerchantId());
            statement.setObject(7, object.getStartDate());
            statement.setObject(8, object.getEndDate());
            statement.executeUpdate();
            logger.info("Successfully updated contract with cafeteria id {}, merchant id {}, start date {}, end date {}", object.getCafeteriaId(), object.getMerchantId(), object.getStartDate(), object.getEndDate());
            return object;
        } catch (SQLException e) {
            logger.error("Error updating contract in database", e);
            return null;
        }
    }

    @Override
    public Contract update(Contract contractOld, Contract contractNew) {
        logger.info("Updating contract with cafeteria id {}, merchant id {}, start date {}, end date {}", contractOld.getCafeteriaId(), contractOld.getMerchantId(), contractOld.getStartDate(), contractOld.getEndDate());
        String sql = "UPDATE hitsz_taste.contracts SET cafeteria_id = ?, merchant_id = ?, start_date = ?, end_date = ? WHERE cafeteria_id = ? AND merchant_id = ? AND start_date = ? AND end_date = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            // Set the values for the contract in the SQL statement
            logger.debug("Setting contract values in SQL statement: cafeteria_id = {}, merchant_id = {}, start_date = {}, end_date = {}", contractNew.getCafeteriaId(), contractNew.getMerchantId(), contractNew.getStartDate(), contractNew.getEndDate());
            statement.setObject(1, contractNew.getCafeteriaId());
            statement.setObject(2, contractNew.getMerchantId());
            statement.setObject(3, contractNew.getStartDate());
            statement.setObject(4, contractNew.getEndDate());
            statement.setObject(5, contractOld.getCafeteriaId());
            statement.setObject(6, contractOld.getMerchantId());
            statement.setObject(7, contractOld.getStartDate());
            statement.setObject(8, contractOld.getEndDate());
            statement.executeUpdate();
            logger.info("Successfully updated contract with cafeteria id {}, merchant id {}, start date {}, end date {}", contractNew.getCafeteriaId(), contractNew.getMerchantId(), contractNew.getStartDate(), contractNew.getEndDate());
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
                resultSet.getObject("start_date", Date.class),
                resultSet.getObject("end_date", Date.class)
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
        logger.info("Deleting contract with cafeteria id {}, merchant id {}, start date {}, end date {}", object.getCafeteriaId(), object.getMerchantId(), object.getStartDate(), object.getEndDate());
        String sql = "DELETE FROM hitsz_taste.contracts WHERE cafeteria_id = ? AND merchant_id = ? AND start_date = ? AND end_date = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setObject(1, object.getCafeteriaId());
            statement.setObject(2, object.getMerchantId());
            statement.setObject(3, object.getStartDate());
            statement.setObject(4, object.getEndDate());
            statement.executeUpdate();
            logger.info("Successfully deleted contract with cafeteria id {}, merchant id {}, start date {}, end date {}", object.getCafeteriaId(), object.getMerchantId(), object.getStartDate(), object.getEndDate());
        } catch (SQLException e) {
            logger.error("Error deleting contract from database", e);
        }
    }
}