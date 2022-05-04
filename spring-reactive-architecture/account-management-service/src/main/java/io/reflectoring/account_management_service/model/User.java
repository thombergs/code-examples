package io.reflectoring.account_management_service.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Data
@Document
@ToString
@NoArgsConstructor
public class User {

    @Id
    private String id;

    @JsonProperty("first_name")
    private String firstName;

    @JsonProperty("last_name")
    private String lastName;
    private String email;
    private String address;

    @JsonProperty("home_country")
    private String homeCountry;
    private String gender;
    private String mobile;

    @JsonProperty("card_id")
    private String cardId;

    @JsonProperty("account_number")
    private String accountNumber;

    @JsonProperty("account_type")
    private String accountType;

    @JsonProperty("account_locked")
    private boolean accountLocked;

    @JsonProperty("fraudulent_activity_attempt_count")
    private Long fraudulentActivityAttemptCount;

    @Transient
    @JsonProperty("valid_transactions")
    private List<Transaction> validTransactions;

    @Transient
    @JsonProperty("fraudulent_transactions")
    private List<Transaction> fraudulentTransactions;
}
