package me.efjerryyang.webserver.controller;

import me.efjerryyang.webserver.model.User;
import me.efjerryyang.webserver.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class SignUpController {

    private final UserService userService;

    public SignUpController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/signup")
    public String signupForm(Model model) {
        model.addAttribute("user", new User());
        return "signup";
    }

    @PostMapping("/signup")
    public String signupSubmit(@ModelAttribute User user, Model model) {
        User existingUser = userService.get(user);
        if (existingUser != null) {
            // User already exists
            model.addAttribute("error", "Username is already taken");
            return "signup";
        } else {
            // Create new user
            userService.create(user);
            return "redirect:/login";
        }
    }
}
