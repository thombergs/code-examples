package io.reflectoring.solid.lsp.paymentexample.redesigned.instruments;

import io.reflectoring.solid.lsp.paymentexample.common.exceptions.PaymentInstrumentInvalidException;
import io.reflectoring.solid.lsp.paymentexample.redesigned.fraud.IFraudChecker;
import io.reflectoring.solid.lsp.paymentexample.redesigned.paymentgateway.IPaymentGatewayHandler;
import io.reflectoring.solid.lsp.paymentexample.redesigned.validators.IPaymentInstrumentValidator;
import java.util.Date;

public class CreditCard extends BaseBankCard {

  public CreditCard(String name,
                    String cardNumber,
                    String verificationCode,
                    Date expiryDate,
                    IPaymentInstrumentValidator basicValidator,
                    IFraudChecker fraudChecker,
                    IPaymentGatewayHandler gatewayHandler) {
    super(name, cardNumber, verificationCode, expiryDate, basicValidator, fraudChecker, gatewayHandler);
  }

  @Override
  public void validate() throws PaymentInstrumentInvalidException {
    basicValidator.validate();
    // additional validations for credit cards
  }

}