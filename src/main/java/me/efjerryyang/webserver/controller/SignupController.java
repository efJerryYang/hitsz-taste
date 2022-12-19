package me.efjerryyang.webserver.controller;

import me.efjerryyang.webserver.model.User;
import me.efjerryyang.webserver.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class SignupController {
    private final UserService userService;

    public SignupController(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping("/signup")
    public String handleSignupForm(@RequestParam("username") String username,
                                   @RequestParam("password") String password,
                                   @RequestParam("email") String email,
                                   Model model) {
        // TODO: Validate form input and handle errors if necessary
        // validate if javascript is disabled
        if (username == null || username.isEmpty() || password == null || password.isEmpty() || email == null || email.isEmpty()) {
            model.addAttribute("error", "Please fill out all the fields");
            return "signup";
        }

        // TODO: Create a new user with the provided username, password, and email
        User user = new User();
        user.setName(username);
        user.setPassword(password);
        user.setEmail(email);

        // TODO: Redirect to a confirmation page or display a success message on the same page
        return "home";
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
