package me.efjerryyang.webserver.controller;

import me.efjerryyang.webserver.dao.UserDAO;
import me.efjerryyang.webserver.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class ExampleController {
    private static final Logger logger = LoggerFactory.getLogger(ExampleController.class);
    private UserDAO userDAO;

    @Autowired
    public ExampleController(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    @RequestMapping("/")
    public String example(@ModelAttribute("model") ModelMap model) {
        logger.info("ExampleController example() is executed!");
        model.addAttribute("name", "John Doe");
        return "example";
    }

    @RequestMapping("/get_all_users")
    public List<User> getAllUsers() {
        return userDAO.getAll();
    }

    @RequestMapping("/hello")
    public String hello(Model model, @RequestParam(value = "name", required = false, defaultValue = "World") String name) {
        model.addAttribute("name", name);
        return "welcome";
    }
}

