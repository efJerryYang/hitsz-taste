package me.efjerryyang.webserver.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import me.efjerryyang.webserver.util.TimeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import static me.efjerryyang.webserver.controller.SessionExpiredController.SESSION_TIMEOUT;

@Component
public class LoginInterceptor implements HandlerInterceptor {
    private static final Logger logger = LoggerFactory.getLogger(LoginInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession();
        Boolean isLoggedIn = (Boolean) session.getAttribute("isLoggedIn");
        Long lastActivity = (Long) session.getAttribute("lastActivity");
        Long currentTime = System.currentTimeMillis();

        // Check if the user is logged in
        if (isLoggedIn != null && isLoggedIn) {
            // Check if the session has expired
            if (lastActivity != null && (currentTime - lastActivity) > SESSION_TIMEOUT) {
                response.sendRedirect("/sessionExpired");
                logger.info("Session expired. Last activity: {} Current time: {}",
                        TimeUtil.timestampToDatetime(TimeUtil.longToTimestamp(lastActivity), true),
                        TimeUtil.timestampToDatetime(TimeUtil.longToTimestamp(currentTime), true));
                session.setAttribute("isLoggedIn", false);
                return false;
            } else {
                session.setAttribute("lastActivity", currentTime);
                logger.info("Session is still active. Update last activity to: {}",
                        TimeUtil.timestampToDatetime(TimeUtil.longToTimestamp(currentTime), true));
                return true;
            }
        }

        // Check if the user is trying to access a protected resource
        if (isProtectedResource(request)) {
            response.sendRedirect("/login");
            return false;
        }

        return true;
    }

    private boolean isProtectedResource(HttpServletRequest request) {
        // Check if the request is for a protected resource
        // You can use a list of URLs or use a more sophisticated method to determine this
        String requestURL = request.getRequestURL().toString();
        return requestURL.contains("/protected");
    }
}
