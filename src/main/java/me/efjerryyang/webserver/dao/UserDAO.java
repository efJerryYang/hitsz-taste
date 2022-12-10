package me.efjerryyang.webserver.dao;

import me.efjerryyang.webserver.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.*;

@Component
public class UserDAO {
    private MySQLConnection mysqlConnection;
    private Connection connection;

    @Autowired
    public UserDAO(MySQLConnection mySQLConnection) throws SQLException {
        this.mysqlConnection = mySQLConnection;
        this.connection = mySQLConnection.getConnection();
    }

    public User createUser(User user) throws SQLException {
        String sql = "INSERT INTO users (user_id, name, email, password, phone, address) VALUES (?, ?, ?, ?, ?, ?)";
        PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        statement.setLong(1, user.getUserId());
        statement.setString(2, user.getName());
        statement.setString(3, user.getEmail());
        statement.setString(4, user.getPassword());
        statement.setString(5,user.getPhone());
        statement.setString(6, user.getAddress());
        statement.executeUpdate();

        ResultSet resultSet = statement.getGeneratedKeys();
        if (resultSet.next()) {
            user.setUserId(resultSet.getLong(1));
        }
        return user;
    }

    public User getUserById(long id) throws SQLException {

        PreparedStatement statement = connection.prepareStatement("SELECT * FROM users WHERE userID = ?");
        statement.setLong(1, id);
        ResultSet resultSet = statement.executeQuery();
        if (resultSet.next()) {
            // Retrieve the data for the user
            long userID = resultSet.getLong("user_id");
            String name = resultSet.getString("name");
            String email = resultSet.getString("email");
            String password = resultSet.getString("password");
            String phone = resultSet.getString("phone");
            String address = resultSet.getString("address");

            return new User(userID, name, email, password, phone, address);
        } else {
            return new User(); // Todo: return an empty user object
        }

    }

}