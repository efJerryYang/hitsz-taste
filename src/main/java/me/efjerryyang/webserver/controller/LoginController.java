package me.efjerryyang.webserver.controller;

import me.efjerryyang.webserver.model.User;
import me.efjerryyang.webserver.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public String login(@RequestParam("username") String username,
            @RequestParam("password") String password,
            Model model) {
        // the username passed in can be either phone or email or username
        // check if the usernamme is a phone number, the phone number can be in the format of 555-555-1234 or 12345678901
        if (username.matches("\\d{3}-\\d{3}-\\d{4}") || username.matches("\\d{10}")) {
            // username is a phone number
            // check if the password is correct
            User user = userService.getByPhoneAndPassword(username, password);
            if (user == null) {
                // Login failed
                model.addAttribute("error", "Invalid username or password");
                return "welcome";
            } else {
                // Login succeeded
                model.addAttribute("user", user);
                return "home";
            }
        } else if (username.matches("\\w+@\\w+\\.\\w+")) {
            // username is an email
            // check if the password is correct
            User user = userService.getByEmailAndPassword(username, password);
            if (user == null) {
                // Login failed
                model.addAttribute("error", "Invalid username or password");
                return "welcome";
            } else {
                // Login succeeded
                model.addAttribute("user", user);
                return "home";
            }
        } else {
            // username is a username
            // check if the password is correct
            User user = userService.getByUsernameAndPassword(username, password);
            if (user == null) {
                // Login failed
                model.addAttribute("error", "Invalid username or password");
                return "welcome";
            } else {
                // Login succeeded
                model.addAttribute("user", user);
                return "home";
            }
        }
    }
}
