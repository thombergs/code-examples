package io.reflectoring.solid.lsp.paymentexample.redesigned.instruments;

import io.reflectoring.solid.lsp.paymentexample.common.exceptions.FraudDetectedException;
import io.reflectoring.solid.lsp.paymentexample.common.exceptions.PaymentFailedException;
import io.reflectoring.solid.lsp.paymentexample.common.exceptions.PaymentInstrumentInvalidException;
import io.reflectoring.solid.lsp.paymentexample.redesigned.fraud.IFraudChecker;
import io.reflectoring.solid.lsp.paymentexample.redesigned.model.PaymentGatewayResponse;
import io.reflectoring.solid.lsp.paymentexample.redesigned.model.PaymentResponse;
import io.reflectoring.solid.lsp.paymentexample.redesigned.paymentgateway.IPaymentGatewayHandler;
import io.reflectoring.solid.lsp.paymentexample.redesigned.validators.IPaymentInstrumentValidator;
import java.util.Date;

public abstract class BaseBankCard implements IPaymentInstrument {
    String name;
    String cardNumber;
    String verificationCode;
    Date expiryDate;
    String fingerprint;
    IPaymentInstrumentValidator basicValidator;
    IFraudChecker fraudChecker;
    IPaymentGatewayHandler gatewayHandler;

    public BaseBankCard(String name,
                      String cardNumber,
                      String verificationCode,
                      Date expiryDate,
                      IPaymentInstrumentValidator basicValidator,
                      IFraudChecker fraudChecker,
                      IPaymentGatewayHandler gatewayHandler) {
        this.name = name;
        this.cardNumber = cardNumber;
        this.verificationCode = verificationCode;
        this.expiryDate = expiryDate;
        this.basicValidator = basicValidator;
        this.fraudChecker = fraudChecker;
        this.gatewayHandler = gatewayHandler;
    }

    @Override
    public void validate() throws PaymentInstrumentInvalidException {
        basicValidator.validate();
    }

    @Override
    public PaymentResponse collectPayment() throws PaymentFailedException {
        PaymentResponse response = new PaymentResponse();
        try {
            fraudChecker.runChecks();
            PaymentGatewayResponse pgResponse = gatewayHandler.handlePayment();
            response.setIdentifier(pgResponse.getFingerprint());
        } catch (FraudDetectedException e) {
            e.printStackTrace();
        }
        return response;
    }
}