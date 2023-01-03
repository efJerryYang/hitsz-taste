package me.efjerryyang.webserver.service;

import me.efjerryyang.webserver.dao.UserDAO;
import me.efjerryyang.webserver.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);
    // This layer is used to handle business logic over the data access layer
    // Even if the data access layer changes, this layer should provide the same interface to the controller layer
    private UserDAO userDAO;


    @Autowired
    public UserService(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    public void create(User user) {
        User getByUsername = userDAO.getByUsername(user.getUsername());
        User getByEmail = userDAO.getByEmail(user.getEmail());
        User getByPhone = userDAO.getByPhone(user.getPhone());
        if (getByUsername != null) {
            throw new RuntimeException("Username already exists");
        }
        if (getByEmail != null) {
            throw new RuntimeException("Email already exists");
        }
        if (getByPhone != null) {
            throw new RuntimeException("Phone already exists");
        }
        if (this.get(user) == null) {
            userDAO.create(user);
            logger.info("User created: " + user);
        } else {
            throw new RuntimeException("User already exists");
        }
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

    public Long getNextId() {
        return userDAO.getNextId();
    }

    public User getByUsernameAndPassword(String username, String password) {
        return userDAO.getByUsernameAndPassword(username, password);
    }

    public User getByEmailAndPassword(String email, String password) {
        return userDAO.getByEmailAndPassword(email, password);
    }

    public User getByPhoneAndPassword(String phone, String password) {
        return userDAO.getByPhoneAndPassword(phone, password);
    }

    public String getSaltByPhone(String phone) {
        return userDAO.getSaltByPhone(phone);
    }

    public String getSaltByUsername(String username) {
        return userDAO.getSaltByUsername(username);
    }

    public String getSaltByEmail(String email) {
        return userDAO.getSaltByEmail(email);
    }

}
