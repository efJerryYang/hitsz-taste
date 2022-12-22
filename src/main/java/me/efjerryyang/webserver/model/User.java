package me.efjerryyang.webserver.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class User implements IModel {
    private Long userId;
    private String username;
    private String firstname;
    private String lastname;
    private String idNumber;
    private String email;
    private String password;
    private String phone;
    private String address;
    private Boolean isActive;
    private String salt;
    private Timestamp createdAt;
}
