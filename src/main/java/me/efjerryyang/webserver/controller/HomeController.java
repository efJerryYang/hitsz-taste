package me.efjerryyang.webserver.controller;

import jakarta.servlet.http.HttpSession;
import me.efjerryyang.webserver.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class HomeController {
    public static final Logger logger = LoggerFactory.getLogger(HomeController.class);
    @Autowired
    private HttpSession session;
    @Autowired
    private UserService userService;

    @GetMapping("/home")
    public String home(Model model) {
        logger.info("HomeController.home() called");
        if (session.getAttribute("username") != null) {
            session.setAttribute("isLoggedIn", true);
            session.setAttribute("lastActivity", System.currentTimeMillis());
            model.addAttribute("username", session.getAttribute("username"));
        }
        return "home";
    }

    @PostMapping("/home/updateOrder")
    public String updateOrder() {
        logger.info("HomeController.order() called");

        return "redirect:/home";
    }

    @PostMapping("/home/removeOrder")
    public String removeOrder() {
        logger.info("HomeController.removeOrder() called");
        return "redirect:/home";
    }

    @PostMapping("/home/checkout")
    public String checkout() {
        logger.info("HomeController.checkout() called");
        if (session.getAttribute("username") == null) {
            return "redirect:/login";
        }
        return "redirect:/home";
    }
}
