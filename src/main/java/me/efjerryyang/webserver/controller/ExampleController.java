package me.efjerryyang.webserver.controller;

import me.efjerryyang.webserver.dao.UserDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

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

    @RequestMapping("/hello")
    public String hello(Model model, @RequestParam(value = "name", required = false, defaultValue = "World") String name) {
        model.addAttribute("name", name);
        return "welcome";
    }
}

