package io.reflectoring.reporting_service.service;

import io.reflectoring.reporting_service.constant.TransactionStatus;
import io.reflectoring.reporting_service.kafka.producer.TransactionProducer;
import io.reflectoring.reporting_service.model.Transaction;
import io.reflectoring.reporting_service.model.User;
import io.reflectoring.reporting_service.repository.TransactionRepository;
import io.reflectoring.reporting_service.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
public class ReportingService {

    @Autowired
    private TransactionRepository transactionRepo;

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private TransactionProducer producer;

    public Mono<Transaction> report(Transaction transaction) {
        return userRepo.findByCardId(transaction.getCardId())
                .map(u -> {
                    if (transaction.getStatus().equals(TransactionStatus.FRAUDULENT)
                            || transaction.getStatus().equals(TransactionStatus.FRAUDULENT_NOTIFY_SUCCESS)
                            || transaction.getStatus().equals(TransactionStatus.FRAUDULENT_NOTIFY_FAILURE)) {

                        // Report the User's account and take automatic action against User's account or card
                        u.setFraudulentActivityAttemptCount(u.getFraudulentActivityAttemptCount() + 1);
                        u.setAccountLocked(u.getFraudulentActivityAttemptCount() > 3);
                        List<Transaction> newList = new ArrayList<>();
                        newList.add(transaction);
                        if (Objects.isNull(u.getFraudulentTransactions()) || u.getFraudulentTransactions().isEmpty()) {
                            u.setFraudulentTransactions(newList);
                        } else {
                            u.getFraudulentTransactions().add(transaction);
                        }
                    }
                    log.info("User details: {}", u);
                    return u;
                })
                .flatMap(userRepo::save)
                .map(u -> {
                    if (!transaction.getStatus().equals(TransactionStatus.VALID)) {
                        transaction.setStatus(u.isAccountLocked()
                                ? TransactionStatus.ACCOUNT_BLOCKED : TransactionStatus.FAILURE);
                    }
                    return transaction;
                })
                .flatMap(transactionRepo::save);
    }

    public void asyncProcess(Transaction transaction) {
        userRepo.findByCardId(transaction.getCardId())
                .map(u -> {
                    if (transaction.getStatus().equals(TransactionStatus.FRAUDULENT)
                            || transaction.getStatus().equals(TransactionStatus.FRAUDULENT_NOTIFY_SUCCESS)
                            || transaction.getStatus().equals(TransactionStatus.FRAUDULENT_NOTIFY_FAILURE)) {

                        // Report the User's account and take automatic action against User's account or card
                        u.setFraudulentActivityAttemptCount(u.getFraudulentActivityAttemptCount() + 1);
                        u.setAccountLocked(u.getFraudulentActivityAttemptCount() > 3);
                        List<Transaction> newList = new ArrayList<>();
                        newList.add(transaction);
                        if (Objects.isNull(u.getFraudulentTransactions()) || u.getFraudulentTransactions().isEmpty()) {
                            u.setFraudulentTransactions(newList);
                        } else {
                            u.getFraudulentTransactions().add(transaction);
                        }
                    }
                    log.info("User details: {}", u);
                    return u;
                })
                .flatMap(userRepo::save)
                .map(u -> {
                    if (!transaction.getStatus().equals(TransactionStatus.VALID)) {
                        transaction.setStatus(u.isAccountLocked()
                                ? TransactionStatus.ACCOUNT_BLOCKED : TransactionStatus.FAILURE);
                        producer.sendMessage(transaction);
                    }
                    return transaction;
                })
                .filter(t -> t.getStatus().equals(TransactionStatus.FAILURE)
                        || t.getStatus().equals(TransactionStatus.ACCOUNT_BLOCKED)
                )
                .flatMap(transactionRepo::save)
                .subscribe();
    }
}
