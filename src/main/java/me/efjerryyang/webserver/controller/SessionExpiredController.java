package me.efjerryyang.webserver.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SessionExpiredController {
    private static final long ONE_MINUTE_IN_MILLIS = 60 * 1000;
    private static final long ONE_HOUR_IN_MILLIS = 60 * ONE_MINUTE_IN_MILLIS;
    private static final long ONE_DAY_IN_MILLIS = 24 * ONE_HOUR_IN_MILLIS;
    public static final long SESSION_TIMEOUT = ONE_MINUTE_IN_MILLIS;
    @Autowired
    private HttpSession session;

    @GetMapping("/sessionExpired")
    public String sessionExpired(Model model) {
        // make the session expired
        session.invalidate();
        model.addAttribute("sessionExpired", true);
        return "notice_session_expired";
    }

    @GetMapping("/logout")
    public String logout(Model model) {
        session.invalidate();
        return "notice_logout_success";
    }

}
