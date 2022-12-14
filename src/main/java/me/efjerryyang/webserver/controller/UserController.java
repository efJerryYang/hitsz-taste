package me.efjerryyang.webserver.controller;

import me.efjerryyang.webserver.dao.UserDAO;
import me.efjerryyang.webserver.model.User;
import me.efjerryyang.webserver.service.UserService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Controller
public class UserController {

    // This layer is used to handle the overall logic of user related requests

    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }



}
