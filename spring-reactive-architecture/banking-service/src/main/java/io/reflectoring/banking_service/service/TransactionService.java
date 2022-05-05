package io.reflectoring.banking_service.service;

import io.reflectoring.banking_service.constant.TransactionStatus;
import io.reflectoring.banking_service.kafka.producer.TransactionProducer;
import io.reflectoring.banking_service.model.Transaction;
import io.reflectoring.banking_service.repository.TransactionRepository;
import io.reflectoring.banking_service.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Objects;

@Slf4j
@Service
public class TransactionService {
    private static final String USER_NOTIFICATION_SERVICE_URL = "http://localhost:8081/notify/fraudulent-transaction";
    private static final String REPORTING_SERVICE_URL = "http://localhost:8082/report/";
    private static final String ACCOUNT_MANAGER_SERVICE_URL = "http://localhost:8083/banking/process";

    @Autowired
    private TransactionRepository transactionRepo;

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private WebClient webClient;

    @Autowired
    TransactionProducer producer;

    @Transactional
    public Mono<Transaction> process(Transaction transaction) {

        return Mono.just(transaction)
                .flatMap(transactionRepo::save)
                .flatMap(t -> userRepo.findByCardId(t.getCardId())
                        .map(u -> {
                            log.info("User details: {}", u);
                            if (t.getStatus().equals(TransactionStatus.INITIATED)) {
                                // Check whether the card details are valid or not
                                if (Objects.isNull(u)) {
                                    t.setStatus(TransactionStatus.CARD_INVALID);
                                }

                                // Check whether the account is blocked or not
                                else if (u.isAccountLocked()) {
                                    t.setStatus(TransactionStatus.ACCOUNT_BLOCKED);
                                }

                                else {
                                    // Check if it's a valid transaction or not. The Transaction would be considered valid
                                    // if it has been requested from the same home country of the user, else will be considered
                                    // as fraudulent
                                    if (u.getHomeCountry().equalsIgnoreCase(t.getTransactionLocation())) {
                                        t.setStatus(TransactionStatus.VALID);

                                        // Call Reporting Service to report valid transaction to bank and deduct amount if funds available
                                        return webClient.post()
                                                .uri(REPORTING_SERVICE_URL)
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .body(BodyInserters.fromValue(t))
                                                .retrieve()
                                                .bodyToMono(Transaction.class)
                                                .zipWhen(t1 ->
                                                                // Call Account Manager service to process the transaction and send the money
                                                                webClient.post()
                                                                    .uri(ACCOUNT_MANAGER_SERVICE_URL)
                                                                    .contentType(MediaType.APPLICATION_JSON)
                                                                    .body(BodyInserters.fromValue(t))
                                                                    .retrieve()
                                                                    .bodyToMono(Transaction.class)
                                                                    .log(),
                                                                    (t1, t2) -> t2
                                                )
                                                .log()
                                                .share()
                                                .block();
                                    } else {
                                        t.setStatus(TransactionStatus.FRAUDULENT);

                                        // Call User Notification service to notify for a fraudulent transaction
                                        // attempt from the User's card
                                        return webClient.post()
                                                .uri(USER_NOTIFICATION_SERVICE_URL)
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .body(BodyInserters.fromValue(t))
                                                .retrieve()
                                                .bodyToMono(Transaction.class)
                                                .zipWhen(t1 ->
                                                                // Call Reporting Service to notify bank that there has been an attempt for fraudulent transaction
                                                                // and if this attempt exceeds 3 times then auto-block the card and account
                                                                webClient.post()
                                                                    .uri(REPORTING_SERVICE_URL)
                                                                    .contentType(MediaType.APPLICATION_JSON)
                                                                    .body(BodyInserters.fromValue(t))
                                                                    .retrieve()
                                                                    .bodyToMono(Transaction.class)
                                                                    .log(),
                                                                    (t1, t2) -> t2
                                                )
                                                .log()
                                                .share()
                                                .block();
                                    }
                                }
                            } else {
                                // For any other case, the transaction will be considered failure
                                t.setStatus(TransactionStatus.FAILURE);
                            }
                            return t;
                        }));
    }

    public void asyncProcess(Transaction transaction) {
        userRepo.findByCardId(transaction.getCardId())
                .map(u -> {
                    if (transaction.getStatus().equals(TransactionStatus.INITIATED)) {
                        log.info("Consumed message for processing: {}", transaction);
                        log.info("User details: {}", u);
                        // Check whether the card details are valid or not
                        if (Objects.isNull(u)) {
                            transaction.setStatus(TransactionStatus.CARD_INVALID);
                        }

                        // Check whether the account is blocked or not
                        else if (u.isAccountLocked()) {
                            transaction.setStatus(TransactionStatus.ACCOUNT_BLOCKED);
                        }

                        else {
                            // Check if it's a valid transaction or not. The Transaction would be considered valid
                            // if it has been requested from the same home country of the user, else will be considered
                            // as fraudulent
                            if (u.getHomeCountry().equalsIgnoreCase(transaction.getTransactionLocation())) {
                                transaction.setStatus(TransactionStatus.VALID);
                            } else {
                                transaction.setStatus(TransactionStatus.FRAUDULENT);
                            }
                        }
                        producer.sendMessage(transaction);
                    }
                    return transaction;
                })
                .filter(t -> t.getStatus().equals(TransactionStatus.VALID)
                        || t.getStatus().equals(TransactionStatus.FRAUDULENT)
                        || t.getStatus().equals(TransactionStatus.CARD_INVALID)
                        || t.getStatus().equals(TransactionStatus.ACCOUNT_BLOCKED)
                )
                .flatMap(transactionRepo::save)
                .subscribe();
    }
}
