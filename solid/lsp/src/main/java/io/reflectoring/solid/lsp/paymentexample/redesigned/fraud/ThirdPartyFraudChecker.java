package io.reflectoring.solid.lsp.paymentexample.redesigned.fraud;

import io.reflectoring.solid.lsp.paymentexample.common.exceptions.FraudDetectedException;
import io.reflectoring.solid.lsp.paymentexample.common.external.ThirdPartyFraudDetectionSystem;
import java.util.Date;

public class ThirdPartyFraudChecker implements IFraudChecker {
    String name;
    String cardNumber;
    String verificationCode;
    Date expiryDate;

    ThirdPartyFraudDetectionSystem fraudDetection = new ThirdPartyFraudDetectionSystem();

    public ThirdPartyFraudChecker(String name, String cardNumber, String verificationCode, Date expiryDate) {
        this.name = name;
        this.cardNumber = cardNumber;
        this.verificationCode = verificationCode;
        this.expiryDate = expiryDate;
    }

    @Override
    public void runChecks() throws FraudDetectedException {
        // run checks against a third-party system
        System.out.println("Running fraud checks against third-party system");
        fraudDetection.process(name, cardNumber, verificationCode, expiryDate);
        System.out.println("Fraud checks passed");

    }
}