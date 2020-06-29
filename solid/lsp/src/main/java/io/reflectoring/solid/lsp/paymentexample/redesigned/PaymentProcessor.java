package io.reflectoring.solid.lsp.paymentexample.redesigned;

import io.reflectoring.solid.lsp.paymentexample.common.OrderDetails;
import io.reflectoring.solid.lsp.paymentexample.common.exceptions.PaymentFailedException;
import io.reflectoring.solid.lsp.paymentexample.common.exceptions.PaymentInstrumentInvalidException;
import io.reflectoring.solid.lsp.paymentexample.redesigned.instruments.IPaymentInstrument;
import io.reflectoring.solid.lsp.paymentexample.redesigned.model.PaymentResponse;

public class PaymentProcessor {
    public void process(OrderDetails orderDetails, IPaymentInstrument paymentInstrument) {
        try {
            paymentInstrument.validate();
            PaymentResponse response = paymentInstrument.collectPayment();
            saveToDatabase(orderDetails, response.getIdentifier());
        } catch (PaymentInstrumentInvalidException e) {
            e.printStackTrace();
        } catch (PaymentFailedException e) {
            e.printStackTrace();
        }
    }

    void saveToDatabase(OrderDetails orderDetails, String identifier) {
        // save the identifier and order details in DB
    }
}