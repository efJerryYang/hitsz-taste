package me.efjerryyang.webserver.model;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class User {
    private Long userId;
    private String name;
    private String email;
    private String password;
    private String phone;
    private String address;
    private Boolean isActive;
}
