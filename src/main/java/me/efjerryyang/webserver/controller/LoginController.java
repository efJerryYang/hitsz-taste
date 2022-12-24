package me.efjerryyang.webserver.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import me.efjerryyang.webserver.model.User;
import me.efjerryyang.webserver.service.UserService;
import me.efjerryyang.webserver.service.ValidationService;
import me.efjerryyang.webserver.util.CryptoUtilHash;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.sql.Timestamp;
// password: 1234567890
// hashed password: c775e7b757ede630cd0aa1113bd102661ab38829ca52a6422ab782862f268646

// salt: f38140ea4774036cc005934d3733ea73
// salt-hashed password: 877179f6b1531a8d5a18f37a8c4aeee811e426f489f299ad3fc9e3c9ff73409a

// salt: 419e1c50a5ad83497bb92865742f9da0
// salt-hashed password: 107a2f74dd8b9c856eead9a4c960929e268e9a0e8b52faa6629fcc2401477021

@Controller
public class LoginController {
    private static final Logger logger = org.slf4j.LoggerFactory.getLogger(LoginController.class);
    private final UserService userService;

    private final ValidationService validationService;

    @Autowired
    private HttpSession session;

    public LoginController(UserService userService, ValidationService validationService) {
        this.userService = userService;
        this.validationService = validationService;
    }

    @PostMapping("/login")
    public String login(@RequestParam(value = "username", defaultValue = "") String username,
                        @RequestParam(value = "password", defaultValue = "") String password, Model model
    ) {
        logger.info("username: {} password: {}", username, password);
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        String acceptHeader = request.getHeader("Accept");
        String jsEnabled = request.getParameter("jsEnabled");
        // validate if javascript is enabled
        if (!validationService.isJavascriptEnabled(acceptHeader, jsEnabled)) {
            // javascript is disabled
            logger.info("Validating login form data from server side");
            if (username.isEmpty() || password.isEmpty()) {
                model.addAttribute("username", username);
                model.addAttribute("password", password);
                model.addAttribute("error", "Please fill in all fields");
                return "welcome";
            }
            if (!validationService.isUsername(username) && !validationService.isEmail(username) && !validationService.isPhone(username)) {
                model.addAttribute("error", "Invalid username");
                model.addAttribute("username", username);
                model.addAttribute("password", password);
                return "welcome";
            }
            password = CryptoUtilHash.hash(password);
            logger.info("hashed password: {}", password);
        }
        logger.info("username: {} password: {}", username, password);
        // the username passed in can be either phone or email or username
        String salt;
        User user;
        if (password.equals("c775e7b757ede630cd0aa1113bd102661ab38829ca52a6422ab782862f268646") && username.equals("root")) {
            salt = "f38140ea4774036cc005934d3733ea73";
            user = new User(999L, "root", null, null, null, "helloworldbro@outlook.com", CryptoUtilHash.hashWithSalt(password, salt), "18908170365", null, true, salt, Timestamp.valueOf("2022-12-22 16:34:05"));
            session.setAttribute("username", user.getUsername());
            return "redirect:/home";
        } else { // Currently disabled, use a test account for testing
            if (validationService.isPhone(username)) {
                // username is a phone number
                // check if the password is correct
                try {
                    salt = userService.getSaltByPhone(username);
                    user = userService.getByPhoneAndPassword(username, CryptoUtilHash.hashWithSalt(password, salt));
                } catch (Exception e) {
                    logger.error("Error: {}", e.getMessage());
                    model.addAttribute("error", "Invalid username or password");
                    model.addAttribute("username", username);
                    model.addAttribute("password", password);
                    return "welcome";
                }
                // Login succeeded
                session.setAttribute("username", user.getUsername());
                return "redirect:/home";
            } else if (validationService.isEmail(username)) {
                // username is an email
                // check if the password is correct
                try {
                    salt = userService.getSaltByEmail(username);
                    user = userService.getByEmailAndPassword(username, CryptoUtilHash.hashWithSalt(password, salt));
                } catch (Exception e) {
                    logger.error("Error: {}", e.getMessage());
                    model.addAttribute("error", "Invalid username or password");
                    model.addAttribute("username", username);
//                model.addAttribute("password", password);
                    return "welcome";
                }
                // Login succeeded
                session.setAttribute("username", user.getUsername());
                return "redirect:/home";
            } else if (validationService.isUsername(username)) {
                // username is a username
                // check if the password is correct
                try {
                    salt = userService.getSaltByUsername(username);
                    user = userService.getByUsernameAndPassword(username, CryptoUtilHash.hashWithSalt(password, salt));
                } catch (Exception e) {
                    logger.error("Error: {}", e.getMessage());
                    model.addAttribute("error", "Invalid username or password");
                    model.addAttribute("username", username);
//                model.addAttribute("password", password);
                    return "welcome";
                }
                // Login succeeded
                session.setAttribute("username", user.getUsername());
                return "redirect:/home";
            } else {
                // this should never happen
                // username is not a phone number, email or username
                model.addAttribute("error", "Invalid username or password");
                return "welcome";
            }
        }
    }
}
