package me.efjerryyang.webserver.controller;

import me.efjerryyang.webserver.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Controller
public class UserController {

    // This layer is used to handle the overall logic of user related requests

    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }


}
