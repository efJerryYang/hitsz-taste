package me.efjerryyang.webserver.model;

import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
public class User {
    private Long userId;
    private String name;
    private String phone;
    private String password;
    private String address;
    private String email;

    public User() {
    }

    public User(Long userId, String name, String phone, String password, String address, String email) {
        this.userId = userId;
        this.name = name;
        this.phone = phone;
        this.password = password;
        this.address = address;
        this.email = email;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        User user = (User) o;
        return Objects.equals(userId, user.userId) &&
                Objects.equals(name, user.name) &&
                Objects.equals(email, user.email) &&
                Objects.equals(password, user.password) &&
                Objects.equals(phone, user.phone) &&
                Objects.equals(address, user.address);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, name, email, password, phone, address);
    }

}
