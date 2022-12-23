package me.efjerryyang.webserver.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class NoticeController {
    private static final Logger logger = LoggerFactory.getLogger(NoticeController.class);
    @GetMapping("/notice_customer")
    public String noticeCustomer(Model model) {
        logger.info("NoticeController.noticeCustomer() called");
        return "notice_customer";
    }
    @GetMapping("/notice_staff")
    public String noticeStaff(Model model) {
        logger.info("NoticeController.noticeStaff() called");
        return "notice_staff";
    }
    @GetMapping("/notice_admin")
    public String noticeAdmin(Model model) {
        logger.info("NoticeController.noticeAdmin() called");
        return "notice_admin";
    }
}
