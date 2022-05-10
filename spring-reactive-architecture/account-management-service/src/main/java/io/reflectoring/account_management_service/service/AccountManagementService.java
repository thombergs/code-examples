package io.reflectoring.account_management_service.service;

import io.reflectoring.account_management_service.constant.TransactionStatus;
import io.reflectoring.account_management_service.kafka.producer.TransactionProducer;
import io.reflectoring.account_management_service.model.Transaction;
import io.reflectoring.account_management_service.repository.TransactionRepository;
import io.reflectoring.account_management_service.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
public class AccountManagementService {

    @Autowired
    private TransactionRepository transactionRepo;

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private TransactionProducer producer;

    public Mono<Transaction> manage(Transaction transaction) {
        return userRepo.findByCardId(transaction.getCardId())
                .map(u -> {
                    if (transaction.getStatus().equals(TransactionStatus.VALID)) {
                        List<Transaction> newList = new ArrayList<>();
                        newList.add(transaction);
                        if (Objects.isNull(u.getValidTransactions()) || u.getValidTransactions().isEmpty()) {
                            u.setValidTransactions(newList);
                        } else {
                            u.getValidTransactions().add(transaction);
                        }
                    }
                    log.info("User details: {}", u);
                    return u;
                })
                .flatMap(userRepo::save)
                .map(u -> {
                    if (transaction.getStatus().equals(TransactionStatus.VALID)) {
                        transaction.setStatus(TransactionStatus.SUCCESS);
                    }
                    return transaction;
                })
                .flatMap(transactionRepo::save);
    }

    public void asyncProcess(Transaction transaction) {
        userRepo.findByCardId(transaction.getCardId())
                .map(u -> {
                    if (transaction.getStatus().equals(TransactionStatus.VALID)) {
                        List<Transaction> newList = new ArrayList<>();
                        newList.add(transaction);
                        if (Objects.isNull(u.getValidTransactions()) || u.getValidTransactions().isEmpty()) {
                            u.setValidTransactions(newList);
                        } else {
                            u.getValidTransactions().add(transaction);
                        }
                    }
                    log.info("User details: {}", u);
                    return u;
                })
                .flatMap(userRepo::save)
                .map(u -> {
                    if (transaction.getStatus().equals(TransactionStatus.VALID)) {
                        transaction.setStatus(TransactionStatus.SUCCESS);
                        producer.sendMessage(transaction);
                    }
                    return transaction;
                })
                .filter(t -> t.getStatus().equals(TransactionStatus.VALID)
                        || t.getStatus().equals(TransactionStatus.SUCCESS)
                )
                .flatMap(transactionRepo::save)
                .subscribe();
    }
}
