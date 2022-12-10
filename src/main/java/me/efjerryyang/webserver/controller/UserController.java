package me.efjerryyang.webserver.controller;

import me.efjerryyang.webserver.dao.UserDAO;
import me.efjerryyang.webserver.model.User;

import java.sql.SQLException;

public class UserController {
    private UserDAO userDAO;

    public UserController(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    public User getUserById(long id) throws SQLException {
        return userDAO.getUserById(id);
    }
}
