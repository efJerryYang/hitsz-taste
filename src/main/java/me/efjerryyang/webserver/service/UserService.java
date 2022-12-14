package me.efjerryyang.webserver.service;

import me.efjerryyang.webserver.dao.UserDAO;
import me.efjerryyang.webserver.model.User;

public class UserService {

    // This layer is used to handle business logic over the data access layer
    // Even if the data access layer changes, this layer should provide the same interface to the controller layer
    private UserDAO userDAO;



    public UserService(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    public User get(User user) {
        if (user.getUserId() != null) {
            return userDAO.getById(user.getUserId());
        } else if (user.getPhone() != null) {
            return userDAO.getByPhone(user.getPhone());
        } else if (user.getEmail() != null) {
            return userDAO.getByEmail(user.getEmail());
        } else {
            return null;
        }
    }

    public void create(User user) {
        userDAO.create(user);
    }
    public User getByUsernameAndPassword(String username, String password) {
        return userDAO.getByNameAndPassword(username, password);
    }

    public User getByEmailAndPassword(String email, String password) {
        return userDAO.getByEmailAndPassword(email, password);
    }
    public User getByPhoneAndPassword(String phone, String password) {
        return userDAO.getByPhoneAndPassword(phone, password);
    }

}
