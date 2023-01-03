package me.efjerryyang.webserver.service;

import me.efjerryyang.webserver.dao.UserRoleDAO;
import me.efjerryyang.webserver.model.UserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserRoleService {
    private final UserRoleDAO userRoleDAO;

    @Autowired
    public UserRoleService(UserRoleDAO userRoleDAO) {
        this.userRoleDAO = userRoleDAO;
    }
    public void create(UserRole userRole) {
        UserRole getByUserIdAndRoleId = userRoleDAO.getByUserIdAndRoleId(userRole.getUserId(), userRole.getRoleId());
        if (getByUserIdAndRoleId != null) {
            throw new RuntimeException("User role already exists");
        }
        userRoleDAO.create(userRole);
    }

    public UserRole bindUserAndRole(Long userId, Integer roleId) {
        UserRole userRole = new UserRole();
        userRole.setUserId(userId);
        userRole.setRoleId(roleId);
        userRole.setGrantDate(new java.sql.Timestamp(System.currentTimeMillis()));
        return userRole;
    }

}
