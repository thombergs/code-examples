package io.reflectoring.solid.lsp.paymentexample.common;

import io.reflectoring.solid.lsp.paymentexample.common.instruments.PaymentInstrument;
import io.reflectoring.solid.lsp.paymentexample.common.exceptions.FraudDetectedException;
import io.reflectoring.solid.lsp.paymentexample.common.exceptions.PaymentFailedException;
import io.reflectoring.solid.lsp.paymentexample.common.exceptions.PaymentInstrumentInvalidException;

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
    String identifier = paymentInstrument.getFingerprint();
    // save fingerprint and order details in DB
    if (identifier == null) {
      throw new NullPointerException("Fingerprint/identifier is required to save to database");
    }
  }
}