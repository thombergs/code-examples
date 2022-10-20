package com.reflectoring.csrf.controller;

import com.reflectoring.csrf.service.CustomerEmailService;
import org.junit.jupiter.params.shadow.com.univocity.parsers.annotations.Headers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class EmailController {
    private static final Logger logger = LoggerFactory.getLogger(EmailController.class.getSimpleName());

    private final CustomerEmailService customerEmailService;

    public EmailController(@Autowired CustomerEmailService customerEmailService) {
        this.customerEmailService = customerEmailService;
    }

    @PostMapping("/registerEmail")
    public String registerCustomerEmail(@RequestParam String newEmail) {
        logger.info("Email: {}", newEmail);
        customerEmailService.registerEmail(newEmail);
        return "emailChange";
    }
}
