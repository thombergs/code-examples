package io.reflectoring.solid.lsp.paymentexample.redesigned.paymentgateway;

import io.reflectoring.solid.lsp.paymentexample.common.exceptions.PaymentFailedException;
import io.reflectoring.solid.lsp.paymentexample.common.external.PaymentGateway;
import io.reflectoring.solid.lsp.paymentexample.redesigned.model.PaymentGatewayResponse;
import java.util.Date;

public class PaymentGatewayHandler implements IPaymentGatewayHandler {
    String name;
    String cardNumber;
    String verificationCode;
    Date expiryDate;
    PaymentGateway gateway = new PaymentGateway();

    public PaymentGatewayHandler(String name, String cardNumber, String verificationCode, Date expiryDate) {
        this.name = name;
        this.cardNumber = cardNumber;
        this.verificationCode = verificationCode;
        this.expiryDate = expiryDate;
    }

    @Override
    public PaymentGatewayResponse handlePayment() throws PaymentFailedException {
        // send details to payment gateway (PG) and save the fingerprint
        // received from PG
        System.out.println("Sending details to payment gateway");
        String fingerprint = gateway.process(name, cardNumber, verificationCode, expiryDate);
        PaymentGatewayResponse pgResponse = new PaymentGatewayResponse();
        pgResponse.setFingerprint(fingerprint);
        System.out.println("Payment gateway response received");

        return pgResponse;
    }
}