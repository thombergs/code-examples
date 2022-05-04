package io.reflectoring.account_management_service.constant;

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
