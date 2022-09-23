package com.reflectoring.csrf.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class CustomerEmailService {
    private static final Logger logger = LoggerFactory.getLogger(CustomerEmailService.class.getSimpleName());

    public void registerEmail(String email) {
        logger.info("Customer email : {}", email);
    }

}
