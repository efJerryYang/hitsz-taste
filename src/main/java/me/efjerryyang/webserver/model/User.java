package me.efjerryyang.webserver.model;

public class User {
    private long userId;
    private String name;
    private String phone;
    private String password;
    private String address;
    private String email;

    public User(){

    }
    public User(long userId, String name, String phone, String password, String address, String email) {
        this.userId = userId;
        this.name = name;
        this.phone = phone;
        this.password = password;
        this.address = address;
        this.email = email;
    }

    public long getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public String getPassword() {
        return password;
    }

    public String getAddress() {
        return address;
    }

    public String getEmail() {
        return email;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
