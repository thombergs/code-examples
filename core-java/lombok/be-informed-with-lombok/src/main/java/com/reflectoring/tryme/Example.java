package com.reflectoring.tryme;

import com.reflectoring.lombok.model.Account;
import lombok.extern.slf4j.Slf4j;


@Slf4j
public class Example {

    public static void main(String[] args) {
        Account account = Account.builder().acctName("Savings")
                .acctNo("A001090")
                .build();
        log.info("Account details : {}", account);
    }
}
