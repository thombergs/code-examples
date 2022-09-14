package com.reflectoring.csrf.controller;

import com.reflectoring.csrf.service.CustomerPasswordService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class PasswordController {
    private static final Logger logger = LoggerFactory.getLogger(PasswordController.class.getSimpleName());

    private final CustomerPasswordService customerPasswordService;

    public PasswordController(@Autowired CustomerPasswordService customerPasswordService) {
        this.customerPasswordService = customerPasswordService;
    }

    @PostMapping("/changePassword")
    public String changeCustomerPassword(@RequestParam String newPassword, Model model) {
        logger.info("New password will be: {}", newPassword);
        customerPasswordService.changePassword(newPassword);
        model.addAttribute("newPassword", newPassword);
        return "passwordChanged";
    }
}
