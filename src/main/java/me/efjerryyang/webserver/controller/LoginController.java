package me.efjerryyang.webserver.controller;

import jakarta.servlet.http.HttpServletRequest;
import me.efjerryyang.webserver.model.User;
import me.efjerryyang.webserver.service.UserService;
import me.efjerryyang.webserver.service.ValidationService;
import me.efjerryyang.webserver.util.CryptoUtilHash;
import org.bouncycastle.util.encoders.Hex;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Base64;

@Controller
public class LoginController {
    private static final Logger logger = org.slf4j.LoggerFactory.getLogger(LoginController.class);
    @Autowired
    private UserService userService;

    @Autowired
    private ValidationService validationService;
    // password: 1234567890
    // hashed password: c775e7b757ede630cd0aa1113bd102661ab38829ca52a6422ab782862f268646

    // salt: f38140ea4774036cc005934d3733ea73
    // salt-hashed password: 877179f6b1531a8d5a18f37a8c4aeee811e426f489f299ad3fc9e3c9ff73409a

    // salt: 419e1c50a5ad83497bb92865742f9da0
    // salt-hashed password: 107a2f74dd8b9c856eead9a4c960929e268e9a0e8b52faa6629fcc2401477021
    @PostMapping("/login")
    public String login(@RequestParam("username") String username, @RequestParam("password") String password, Model model) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        String acceptHeader = request.getHeader("Accept");
        String jsEnabled = request.getParameter("jsEnabled");

        if (!validationService.isJavascriptEnabled(acceptHeader, jsEnabled)) {
            password = CryptoUtilHash.hash(password);
            logger.info("hashed password: {}", password);
        }
        logger.info("username: {} password: {}", username, password);

//        // a test for salted password login
//        String salt1 = "f38140ea4774036cc005934d3733ea73";
//        String salt2 = "419e1c50a5ad83497bb92865742f9da0";
//        String expectedPassword1 = Base64.getEncoder().encodeToString(Hex.decode("877179f6b1531a8d5a18f37a8c4aeee811e426f489f299ad3fc9e3c9ff73409a"));
//        logger.info("Expected password1: " + expectedPassword1);
//        String expectedPassword2 = "107a2f74dd8b9c856eead9a4c960929e268e9a0e8b52faa6629fcc2401477021";
//        logger.info("Expected password2: " + expectedPassword2);
//
//        if (expectedPassword1.equals(CryptoUtilHash.hashWithSalt(password, salt1)))
//            logger.info("Password 1 is correct");
//        if (expectedPassword2.equals(CryptoUtilHash.hashWithSalt(password, salt2)))
//            logger.info("Password 2 is correct");
//        return "login";
        // the username passed in can be either phone or email or username
        if (validationService.isPhone(username)) {
            // username is a phone number
            // check if the password is correct
            String salt = userService.getSaltByPhone(username);
            User user = userService.getByPhoneAndPassword(username, CryptoUtilHash.hashWithSalt(password, salt));
            if (user == null) {
                // Login failed
                model.addAttribute("error", "Invalid username or password");
                return "welcome";
            } else {
                // Login succeeded
                model.addAttribute("user", user);
                return "home";
            }
        } else if (validationService.isEmail(username)) {
            // username is an email
            // check if the password is correct
            String salt = userService.getSaltByEmail(username);
            User user = userService.getByEmailAndPassword(username, CryptoUtilHash.hashWithSalt(password, salt));
            if (user == null) {
                // Login failed
                model.addAttribute("error", "Invalid username or password");
                return "welcome";
            } else {
                // Login succeeded
                model.addAttribute("user", user);
                return "home";
            }
        } else if (validationService.isUsername(username)) {
            // username is a username
            // check if the password is correct
            String salt = userService.getSaltByUsername(username);
            User user = userService.getByUsernameAndPassword(username, CryptoUtilHash.hashWithSalt(password, salt));
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
            // username is not a phone number, email or username
            model.addAttribute("error", "Invalid username or password");
            return "welcome";
        }
    }
}
