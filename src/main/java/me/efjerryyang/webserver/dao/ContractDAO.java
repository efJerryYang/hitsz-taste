package me.efjerryyang.webserver.dao;

import me.efjerryyang.webserver.dao.MySQLConnection;
import me.efjerryyang.webserver.model.Contract;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class ContractDAO{
    private static final Logger logger = LoggerFactory.getLogger(ContractDAO.class);
    private MySQLConnection mysqlConnection;
    private Connection connection;

    @Autowired
    public ContractDAO(MySQLConnection mySQLConnection) throws SQLException {
        this.mysqlConnection = mySQLConnection;
        logger.debug("Obtaining connection to database");
        this.connection = mySQLConnection.getConnection();
    }

    public Contract createContract(Contract contract) {
        logger.info("Creating contract with cafeteria id {}, merchant id {}, start date {}, end date {}", contract.getCafeteriaId(), contract.getMerchantId(), contract.getStartDate(), contract.getEndDate());
        String sql = "INSERT INTO contracts (cafeteria_id, merchant_id, start_date, end_date) VALUES (?, ?, ?, ?)";
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

    public Contract updateContract(Contract contractOld, Contract contractNew) {
        logger.info("Updating contract with cafeteria id {}, merchant id {}, start date {}, end date {}", contractOld.getCafeteriaId(), contractOld.getMerchantId(), contractOld.getStartDate(), contractOld.getEndDate());
        String sql = "UPDATE contracts SET cafeteria_id = ?, merchant_id = ?, start_date = ?, end_date = ? WHERE cafeteria_id = ? AND merchant_id = ? AND start_date = ? AND end_date = ?";
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
}