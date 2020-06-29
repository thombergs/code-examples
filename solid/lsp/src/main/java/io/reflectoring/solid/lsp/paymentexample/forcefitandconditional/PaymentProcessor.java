package io.reflectoring.solid.lsp.paymentexample.forcefitandconditional;

import io.reflectoring.solid.lsp.paymentexample.common.OrderDetails;
import io.reflectoring.solid.lsp.paymentexample.common.instruments.PaymentInstrument;
import io.reflectoring.solid.lsp.paymentexample.common.exceptions.FraudDetectedException;
import io.reflectoring.solid.lsp.paymentexample.common.exceptions.PaymentFailedException;
import io.reflectoring.solid.lsp.paymentexample.common.exceptions.PaymentInstrumentInvalidException;
import io.reflectoring.solid.lsp.paymentexample.forcefitandconditional.instruments.RewardsCard;

public class PaymentProcessor {
  public void process(OrderDetails orderDetails, PaymentInstrument paymentInstrument) {
    try {
      paymentInstrument.validate();
      paymentInstrument.runFraudChecks();
      paymentInstrument.sendToPaymentGateway();
      saveToDatabase(orderDetails, paymentInstrument);
    } catch (PaymentInstrumentInvalidException e) {
      e.printStackTrace();
    } catch (FraudDetectedException e) {
      e.printStackTrace();
    } catch (PaymentFailedException e) {
      e.printStackTrace();
    }
  }

  void saveToDatabase(OrderDetails orderDetails, PaymentInstrument paymentInstrument) {
    String fingerprint = paymentInstrument.getFingerprint();

    if (paymentInstrument instanceof RewardsCard) {
      System.out.println("Rewards cards don't have a fingerprint, using card number itself instead");
      // update rewards points and save in DB
    }
    else {
      // save fingerprint and order details in DB
      if (fingerprint == null) {
        throw new NullPointerException("Fingerprint is required to save to database");
      }
    }
  }
}