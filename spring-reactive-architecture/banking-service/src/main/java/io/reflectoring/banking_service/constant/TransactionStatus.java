package io.reflectoring.banking_service.constant;

public enum TransactionStatus {
    INITIATED,
    SUCCESS,
    FAILURE,
    CANCELLED,
    VALID,
    ACCOUNT_BLOCKED,
    CARD_INVALID,
    FUNDS_UNAVAILABLE,
    FRAUDULENT,
    FRAUDULENT_NOTIFY_SUCCESS,
    FRAUDULENT_NOTIFY_FAILURE
}
