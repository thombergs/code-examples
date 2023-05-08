package io.pratik.services;

import io.pratik.models.AccountDetail;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AccountService {
    private static final Logger LOG = LogManager.getLogger(AccountService.class);

    public Optional<AccountDetail> getAccount(final String accountNo) {
        // simulating an account not exists scenario
        if(accountNo.endsWith("000")){
            LOG.error("Account not found:: {}", accountNo);
            return Optional.empty();
        }
        return Optional.ofNullable(AccountDetail.builder().accountHolder("Jack Melon")
                .accountNo("GWR" + accountNo)
                .balance(19000.89)
                .currency("USD")
                .openingDate("12/01/2015")
                .build());

    }
}
