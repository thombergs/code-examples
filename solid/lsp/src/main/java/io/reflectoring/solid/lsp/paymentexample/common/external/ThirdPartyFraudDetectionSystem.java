package io.reflectoring.solid.lsp.paymentexample.common.external;

import io.reflectoring.solid.lsp.paymentexample.common.exceptions.FraudDetectedException;
import java.util.Date;

public class ThirdPartyFraudDetectionSystem {
    public void process(String name, String cardNumber, String verificationCode, Date expiryDate) throws FraudDetectedException {
        // sleep 2s to simulate network latency
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if (cardNumber.startsWith("0")) {
            throw new FraudDetectedException("Fraud detected!");
        }

        if (cardNumber.startsWith("2")) {
            throw new FraudDetectedException("Unrecognized card type for fraud detection!");
        }
    }
}