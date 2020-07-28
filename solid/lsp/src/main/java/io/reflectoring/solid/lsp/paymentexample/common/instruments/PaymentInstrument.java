package io.reflectoring.solid.lsp.paymentexample.common.instruments;

import io.reflectoring.solid.lsp.paymentexample.common.exceptions.FraudDetectedException;
import io.reflectoring.solid.lsp.paymentexample.common.exceptions.PaymentFailedException;
import io.reflectoring.solid.lsp.paymentexample.common.exceptions.PaymentInstrumentInvalidException;
import io.reflectoring.solid.lsp.paymentexample.common.external.PaymentGateway;
import io.reflectoring.solid.lsp.paymentexample.common.external.ThirdPartyFraudDetectionSystem;
import java.util.Date;

public abstract class PaymentInstrument {
  String name;
  String cardNumber;
  String verificationCode;
  Date expiryDate;
  String fingerprint;
  ThirdPartyFraudDetectionSystem fraudDetection = new ThirdPartyFraudDetectionSystem();
  PaymentGateway paymentGateway = new PaymentGateway();

  public PaymentInstrument(String name, String cardNumber, String verificationCode,
      Date expiryDate) {
    this.name = name;
    this.cardNumber = cardNumber;
    this.verificationCode = verificationCode;
    this.expiryDate = expiryDate;
  }

  public void validate() throws PaymentInstrumentInvalidException {
    // basic validation on name, expiryDate etc.
    if (name == null || name.isEmpty()) {
      throw new PaymentInstrumentInvalidException("Name is invalid");
    }
    // other validations
  }

  public void runFraudChecks() throws FraudDetectedException {
    // run checks against a third-party system
    System.out.println("Running fraud checks against third-party system");
    fraudDetection.process(name, cardNumber, verificationCode, expiryDate);
    System.out.println("Fraud checks passed");
  }

  public void sendToPaymentGateway() throws PaymentFailedException {
    // send details to payment gateway (PG) and save the fingerprint
    // received from PG
    System.out.println("Sending details to payment gateway");
    this.fingerprint = paymentGateway.process(name, cardNumber, verificationCode, expiryDate);
    System.out.println("Payment gateway response received");
  }

  public String getFingerprint() {
    return fingerprint;
  }
}