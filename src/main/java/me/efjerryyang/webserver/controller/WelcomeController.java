package me.efjerryyang.webserver.controller;

import me.efjerryyang.webserver.dao.UserDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class WelcomeController {
    private static final Logger logger = LoggerFactory.getLogger(WelcomeController.class);
    private UserDAO userDAO;

    @Autowired
    public WelcomeController(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    @GetMapping("/")
    public String welcome(@ModelAttribute("model") ModelMap model) {
        logger.info("WelcomeController.welcome() called");
        return "welcome";
    }
}

