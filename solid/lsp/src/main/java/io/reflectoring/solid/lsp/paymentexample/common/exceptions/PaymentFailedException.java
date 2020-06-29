package io.reflectoring.solid.lsp.paymentexample.common.exceptions;

public class PaymentFailedException extends Exception {
    public PaymentFailedException(String message) {
        super(message);
    }
}
