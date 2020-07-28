package io.reflectoring.solid.lsp.paymentexample.common.instruments;

import io.reflectoring.solid.lsp.paymentexample.common.exceptions.PaymentInstrumentInvalidException;
import java.util.Date;

public class CreditCard extends PaymentInstrument {

  public CreditCard(String name, String cardNumber, String verificationCode,
      Date expiryDate) {
    super(name, cardNumber, verificationCode, expiryDate);
  }

  @Override
  public void validate() throws PaymentInstrumentInvalidException {
    super.validate();
    // additional validations for credit cards
  }
}
