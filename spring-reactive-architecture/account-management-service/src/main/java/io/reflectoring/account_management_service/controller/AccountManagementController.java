package io.reflectoring.account_management_service.controller;

import io.reflectoring.account_management_service.model.Transaction;
import io.reflectoring.account_management_service.service.AccountManagementService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequestMapping("/banking")
public class AccountManagementController {

    @Autowired
    private AccountManagementService accountManagementService;

    @PostMapping("/process")
    public Mono<Transaction> manage(@RequestBody Transaction transaction) {
        log.info("Process transaction with details in account management service: {}", transaction);
        return accountManagementService.manage(transaction);
    }
}
