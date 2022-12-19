package me.efjerryyang.webserver.controller;

import me.efjerryyang.webserver.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class HomeController {
    @Autowired
    private UserService userService;

//    @GetMapping("/")
//    public String home(Model model) {
//        return "home";
//    }
}
