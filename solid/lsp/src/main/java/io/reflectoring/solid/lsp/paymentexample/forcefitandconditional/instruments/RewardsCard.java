package io.reflectoring.solid.lsp.paymentexample.forcefitandconditional.instruments;

import io.reflectoring.solid.lsp.paymentexample.common.instruments.PaymentInstrument;
import io.reflectoring.solid.lsp.paymentexample.common.exceptions.FraudDetectedException;
import io.reflectoring.solid.lsp.paymentexample.common.exceptions.PaymentFailedException;
import java.util.Date;

public class RewardsCard extends PaymentInstrument {
    public RewardsCard(String name, String cardNumber, String verificationCode, Date expiryDate) {
        // Reward cards don't have verification code and expiry date, so we are forced to pass null
        super(name, cardNumber, null, null);
    }

    @Override
    public void runFraudChecks() throws FraudDetectedException {
        // do nothing since Rewards Cards don't require fraud detection
        System.out.println("Doing nothing related to fraud checks in overridden method");
    }

    @Override
    public void sendToPaymentGateway() throws PaymentFailedException {
        // do nothing since Rewards Cards don't communicate with payment gateways
        System.out.println("Doing nothing related to payment gateway in overridden method");
    }
}