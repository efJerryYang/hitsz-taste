package me.efjerryyang.webserver.model;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class User {
    private Long userId;
    private String name;
    private String phone;
    private String password;
    private String address;
    private String email;
}
