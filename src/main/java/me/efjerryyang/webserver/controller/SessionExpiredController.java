package me.efjerryyang.webserver.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SessionExpiredController {
    @GetMapping("/sessionExpired")
    public String sessionExpired(Model model) {
        model.addAttribute("sessionExpired", true);
        return "notice_session_expired";
    }
}
