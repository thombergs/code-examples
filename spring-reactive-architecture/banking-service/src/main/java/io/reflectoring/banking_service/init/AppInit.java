package io.reflectoring.banking_service.init;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.reflectoring.banking_service.kafka.producer.TransactionProducer;
import io.reflectoring.banking_service.model.Transaction;
import io.reflectoring.banking_service.model.User;
import io.reflectoring.banking_service.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Objects;

@Slf4j
@Component
public class AppInit implements ApplicationListener<ApplicationReadyEvent> {

    @Autowired
    private UserRepository userRepo;

    @Autowired
    TransactionProducer producer;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent applicationReadyEvent) {
        log.info("Application started");
        ObjectMapper mapper = new ObjectMapper();
        TypeReference<List<User>> typeReferenceUser = new TypeReference<List<User>>(){};
        InputStream inputStreamUser = TypeReference.class.getResourceAsStream("/json/users.json");
        try {
            List<User> usersList = mapper.readValue(inputStreamUser,typeReferenceUser);
            usersList.forEach(u -> {
                User user = userRepo.findByCardId(u.getCardId())
                        .share()
                        .block();
                if (Objects.isNull(user)) {
                    userRepo.save(u).subscribe();
                }
            });
            log.info("User Saved!");
        } catch (IOException e){
            log.error("Unable to save User: " + e.getMessage());
        }

        TypeReference<List<Transaction>> typeReferenceTransaction = new TypeReference<List<Transaction>>(){};
        InputStream inputStreamTransaction = TypeReference.class.getResourceAsStream("/json/transactions.json");
        try {
            List<Transaction> transactionsList = mapper.readValue(inputStreamTransaction, typeReferenceTransaction);
            transactionsList.forEach(t -> producer.sendMessage(t));
            log.info("Transactions Dispatched to Kafka topic!");
        } catch (IOException e){
            log.error("Unable to dispatch transactions to Kafka Topic: " + e.getMessage());
        }
    }
}
