package me.efjerryyang.webserver.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class Role implements IModel {
    private Integer roleId;
    private String name;

    public static Role createRole(String roleName) {
        if (roleName == null || roleName.isEmpty()) {
            throw new RuntimeException("Role name cannot be empty");
        }
        Role role = new Role();
        switch (roleName) {
            case "admin":
                role.setRoleId(1);
                role.setName("admin");
                break;
            case "staff":
                role.setRoleId(2);
                role.setName("staff");
                break;
            case "customer":
                role.setRoleId(3);
                role.setName("customer");
                break;
            case "default":
                role.setRoleId(4);
                role.setName("default");
                break;
            default:
                throw new RuntimeException("Role name is invalid");
        }
        return role;
    }
}


