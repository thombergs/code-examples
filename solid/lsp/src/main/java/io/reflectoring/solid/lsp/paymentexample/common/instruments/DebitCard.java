package io.reflectoring.solid.lsp.paymentexample.common.instruments;

import java.util.Date;

public class DebitCard extends PaymentInstrument {

  public DebitCard(String name, String cardNumber, String verificationCode,
      Date expiryDate) {
    super(name, cardNumber, verificationCode, expiryDate);
  }
}
