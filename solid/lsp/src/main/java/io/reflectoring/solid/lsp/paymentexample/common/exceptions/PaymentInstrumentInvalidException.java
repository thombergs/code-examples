package io.reflectoring.solid.lsp.paymentexample.common.exceptions;

public class PaymentInstrumentInvalidException extends Exception {
    public PaymentInstrumentInvalidException(String message) {
        super(message);
    }
}