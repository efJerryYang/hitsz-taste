package me.efjerryyang.webserver.controller;

import jakarta.servlet.http.HttpServletRequest;
import me.efjerryyang.webserver.model.User;
import me.efjerryyang.webserver.service.UserService;
import me.efjerryyang.webserver.service.ValidationService;
import me.efjerryyang.webserver.util.CryptoUtilHash;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Controller
public class SignupController {
    private static final Logger logger = LoggerFactory.getLogger(SignupController.class);
    private final UserService userService;
    private final ValidationService validationService;

    public SignupController(UserService userService, ValidationService validationService) {
        this.userService = userService;
        this.validationService = validationService;
    }

    @GetMapping("/signup")
    public String signup(Model model) {
        return "signup";
    }

    @PostMapping("/signup")
    public String handleSignupForm(@RequestParam("username") String username, @RequestParam("password") String password, @RequestParam("phone") String phone, @RequestParam("email") String email, @RequestParam("options") String options, /* @RequestParam("nojs") String nojs, */ Model model) {
        // validate if javascript is disabled
        logger.info("username: {} password: {} phone: {} email: {} options: {}", username, password, phone, email, options);
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        String acceptHeader = request.getHeader("Accept");
        String jsEnabled = request.getParameter("jsEnabled");
        if (!validationService.isJavascriptEnabled(acceptHeader, jsEnabled)) {
            // javascript is disabled
            logger.info("Validating form data from server side");
            // validate the form input
            if (username == null || username.isEmpty() || password == null || password.isEmpty() || phone == null || phone.isEmpty() || email == null || email.isEmpty() || options == null || options.isEmpty()) {
                logger.info("SignupController.handleSignupForm() called with empty fields");
                model.addAttribute("error", "Please fill out all the fields");
                return "signup";
            }
            if (!validationService.isPhone(phone)) {
                logger.info("SignupController.handleSignupForm() called with invalid phone number");
                model.addAttribute("error", "Please enter a valid phone number");
                return "signup";
            }
            if (!validationService.isEmail(email)) {
                logger.info("SignupController.handleSignupForm() called with invalid email");
                model.addAttribute("error", "Please enter a valid email");
                return "signup";
            }
            if (!validationService.isRole(options)) {
                logger.info("SignupController.handleSignupForm() called with invalid options");
                model.addAttribute("error", "Please select an role");
                return "signup";
            }
            // compute the sha256 hash of the password
            password = CryptoUtilHash.hash(password);
            logger.info("hashed password: {}", password);
        }
        User user = new User();
        if (options != null) {
            if ("customer".equals(options)) {
                logger.info("SignupController.handleSignupForm() called with customer option");
            } else if ("employee".equals(options)) {
                logger.info("SignupController.handleSignupForm() called with employee option");
            }
            String salt = CryptoUtilHash.getSalt();
            user.setSalt(salt);
            user.setName(username);
            user.setPassword(CryptoUtilHash.hashWithSalt(password, salt));
            user.setPhone(phone);
            user.setEmail(email);
        } else {
            logger.info("SignupController.handleSignupForm() called with invalid option");
            model.addAttribute("error", "Please select a role");
            return "signup";
        }
        try {
            user.setUserId(userService.getNextId());
            user.setIsActive(true);
//            userService.create(user);
            System.out.println(user);
        } catch (RuntimeException e) {
            logger.info("SignupController.handleSignupForm() called with duplicate user");
            model.addAttribute("error", "User already exists");
            return "signup";
        } catch (Exception e) {
            logger.info("SignupController.handleSignupForm() called with error");
            model.addAttribute("error", "Error creating user");
            return "signup";
        }
        // TODO: Redirect to a confirmation page or display a success message on the same page
        return "redirect:/login";
    }
}
