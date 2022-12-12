package me.efjerryyang.webserver.util;

import org.slf4j.Logger;

public class LoggerFactory {

    // The logger instance that will be used
    private static Logger logger;

    // Private constructor to prevent instantiation
    private LoggerFactory() {}

    // Method to get a Logger instance for a class
    public static Logger getLogger(Class<?> clazz) {
        if (logger == null) {
            // Create a new Logger instance
            logger = LoggerFactory.getLogger(clazz);
        }
        return logger;
    }
}

