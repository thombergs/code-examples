package io.reflectoring.account_management_service.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.reflectoring.account_management_service.constant.TransactionStatus;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document
@ToString
@NoArgsConstructor
public class Transaction {

    @Id
    @JsonProperty("transaction_id")
    private String transactionId;
    private String date;

    @JsonProperty("amount_deducted")
    private double amountDeducted;

    @JsonProperty("store_name")
    private String storeName;

    @JsonProperty("store_id")
    private String storeId;

    @JsonProperty("card_id")
    private String cardId;

    @JsonProperty("transaction_location")
    private String transactionLocation;
    private TransactionStatus status;
}
