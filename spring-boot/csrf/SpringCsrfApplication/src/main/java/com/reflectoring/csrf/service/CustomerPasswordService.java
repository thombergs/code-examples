package com.reflectoring.csrf.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class CustomerPasswordService {
    private static final Logger logger = LoggerFactory.getLogger(CustomerPasswordService.class.getSimpleName());

    public void changePassword(String newPassword) {
        logger.info("Customer password was changed to: {}", newPassword);
    }

}
