package io.pratik.accountProcessor;

import io.pratik.models.AccountDetail;
import io.pratik.services.AccountService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.ThreadContext;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;


@RestController
@RequestMapping("/accounts")
public class AccountInquiryController {
    private AccountService accountService;
    private static final Logger LOG = LogManager.getLogger(AccountInquiryController.class);


    public AccountInquiryController(final AccountService accountService){
        this.accountService = accountService;
    }
    @GetMapping("/{accountNo}")
    @ResponseBody
    public AccountDetail getAccountDetails(@PathVariable("accountNo") String accountNo) {
        ThreadContext.put("accountNo", accountNo);
        LOG.info("fetching account details for account ");
        Optional<AccountDetail> accountDetail = accountService.getAccount(accountNo);
        LOG.info("Details of account {}", accountDetail);
        ThreadContext.clearAll();
        return accountDetail.orElse(AccountDetail.builder().build());
    }
}
