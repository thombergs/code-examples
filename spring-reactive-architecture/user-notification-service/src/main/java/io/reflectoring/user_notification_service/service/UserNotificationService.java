package io.reflectoring.user_notification_service.service;

import io.reflectoring.user_notification_service.constant.TransactionStatus;
import io.reflectoring.user_notification_service.kafka.producer.TransactionProducer;
import io.reflectoring.user_notification_service.model.Transaction;
import io.reflectoring.user_notification_service.repository.TransactionRepository;
import io.reflectoring.user_notification_service.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Slf4j
@Service
public class UserNotificationService {

    @Autowired
    private TransactionRepository transactionRepo;

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private JavaMailSender emailSender;

    @Autowired
    private TransactionProducer producer;

    public Mono<Transaction> notify(Transaction transaction) {
        return userRepo.findByCardId(transaction.getCardId())
                .map(u -> {
                    if (transaction.getStatus().equals(TransactionStatus.FRAUDULENT)) {

                        // Notify user by sending email
                        SimpleMailMessage message = new SimpleMailMessage();
                        message.setFrom("noreply@baeldung.com");
                        message.setTo(u.getEmail());
                        message.setSubject("Fraudulent transaction attempt from your card");
                        message.setText("An attempt has been made to pay " + transaction.getStoreName()
                                + " from card " + transaction.getCardId() + " in the country "
                                + transaction.getTransactionLocation() + "." +
                                " Please report to your bank or block your card.");
                        emailSender.send(message);
                        transaction.setStatus(TransactionStatus.FRAUDULENT_NOTIFY_SUCCESS);
                    } else {
                        transaction.setStatus(TransactionStatus.FRAUDULENT_NOTIFY_FAILURE);
                    }
                    return transaction;
                })
                .onErrorReturn(transaction)
                .flatMap(transactionRepo::save);
    }

    public void asyncProcess(Transaction transaction) {
        userRepo.findByCardId(transaction.getCardId())
                .map(u -> {
                    if (transaction.getStatus().equals(TransactionStatus.FRAUDULENT)) {

                        try {
                            // Notify user by sending email
                            SimpleMailMessage message = new SimpleMailMessage();
                            message.setFrom("noreply@baeldung.com");
                            message.setTo(u.getEmail());
                            message.setSubject("Fraudulent transaction attempt from your card");
                            message.setText("An attempt has been made to pay " + transaction.getStoreName()
                                    + " from card " + transaction.getCardId() + " in the country "
                                    + transaction.getTransactionLocation() + "." +
                                    " Please report to your bank or block your card.");
                            emailSender.send(message);
                            transaction.setStatus(TransactionStatus.FRAUDULENT_NOTIFY_SUCCESS);
                        } catch (MailException e) {
                            transaction.setStatus(TransactionStatus.FRAUDULENT_NOTIFY_FAILURE);
                        }
                    }
                    return transaction;
                })
                .onErrorReturn(transaction)
                .filter(t -> t.getStatus().equals(TransactionStatus.FRAUDULENT)
                        || t.getStatus().equals(TransactionStatus.FRAUDULENT_NOTIFY_SUCCESS)
                        || t.getStatus().equals(TransactionStatus.FRAUDULENT_NOTIFY_FAILURE)
                )
                .map(t -> {
                    producer.sendMessage(t);
                    return t;
                })
                .flatMap(transactionRepo::save)
                .subscribe();
    }
}
