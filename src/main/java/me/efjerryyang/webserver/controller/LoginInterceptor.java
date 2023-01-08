package me.efjerryyang.webserver.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class LoginInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession();
        if (session.getAttribute("username") != null) {
            request.setAttribute("username", session.getAttribute("username"));
            return true;
        }
        // Check if session has expired
        if (session.isNew()) {
            response.sendRedirect("/sessionExpired");
            request.setAttribute("sessionExpired", true);
            return false;
        }
        return true;
    }
}
