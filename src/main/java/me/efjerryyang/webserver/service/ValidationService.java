package me.efjerryyang.webserver.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class ValidationService {
    private static final Logger logger = LoggerFactory.getLogger(ValidationService.class);

    public boolean isEmail(String email) {
        return email.matches("^\\w+@\\w+\\.\\w+$");
    }

    public boolean isPhone(String phone) {

        return phone.matches("^\\d{3}-\\d{3}-\\d{4}$") || phone.matches("^\\d{10,11}$") || phone.matches("^\\+?86\\d{11}$");
    }

    public boolean isUsername(String username) {
        // "^[a-zA-Z][a-zA-Z0-9_.-]{5,19}$"
        return username.matches("^[a-zA-Z][a-zA-Z0-9_.-]{5,19}$");
    }

    public boolean isName(String name) {
        // ^[\p{L}\s-']+$
        return name.matches("^\\p{L}+$");
    }

    public boolean isRole(String role) {
        return role.matches("^(admin|customer|employee)$");
    }

    public boolean isJavascriptEnabled(String acceptHeader, String jsEnabled) {
        logger.info("checking if javascript is enabled");
        logger.info("acceptHeader: " + acceptHeader);
        logger.info("jsEnabled: " + jsEnabled);
        // chrome:text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9
        // firefox: text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,*/*;q=0.8
        if (acceptHeader != null && (acceptHeader.contains("application/javascript") || jsEnabled.equals("true"))) {
            logger.info("accept Heade: {}, jsEnabled: {}", acceptHeader, jsEnabled);
            // JavaScript is likely enabled on the client
            logger.info("javascript is enabled");
            return true;
        } else {
            // return false if we cannot determine if JavaScript is enabled
            logger.info("javascript is disabled");
            return false;
        }
    }
}
