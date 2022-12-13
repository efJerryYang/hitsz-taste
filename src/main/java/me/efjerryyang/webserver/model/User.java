package me.efjerryyang.webserver.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class User implements Model {
    private Long userId;
    private String name;
    private String email;
    private String password;
    private String phone;
    private String address;
    private Boolean isActive;
}
