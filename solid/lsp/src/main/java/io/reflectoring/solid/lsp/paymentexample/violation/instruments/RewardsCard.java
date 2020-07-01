package io.reflectoring.solid.lsp.paymentexample.violation.instruments;

import io.reflectoring.solid.lsp.paymentexample.common.instruments.PaymentInstrument;
import java.util.Date;

public class RewardsCard extends PaymentInstrument {
    public RewardsCard(String name, String cardNumber, String verificationCode, Date expiryDate) {
        // Reward cards don't have verification code and expiry date, so we are forced to pass null
        super(name, cardNumber, null, null);
    }
}
