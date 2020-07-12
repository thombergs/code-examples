package io.reflectoring.solid.lsp.paymentexample.redesigned.instruments;

import io.reflectoring.solid.lsp.paymentexample.redesigned.fraud.IFraudChecker;
import io.reflectoring.solid.lsp.paymentexample.redesigned.paymentgateway.IPaymentGatewayHandler;
import io.reflectoring.solid.lsp.paymentexample.redesigned.validators.IPaymentInstrumentValidator;
import java.util.Date;

public class DebitCard extends BaseBankCard {

  public DebitCard(String name,
                   String cardNumber,
                   String verificationCode,
                   Date expiryDate,
                   IPaymentInstrumentValidator basicValidator,
                   IFraudChecker fraudChecker,
                   IPaymentGatewayHandler gatewayHandler) {
    super(name, cardNumber, verificationCode, expiryDate, basicValidator, fraudChecker, gatewayHandler);
  }
}