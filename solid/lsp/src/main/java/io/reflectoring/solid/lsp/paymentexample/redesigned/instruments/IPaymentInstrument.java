package io.reflectoring.solid.lsp.paymentexample.redesigned.instruments;

import io.reflectoring.solid.lsp.paymentexample.common.exceptions.PaymentFailedException;
import io.reflectoring.solid.lsp.paymentexample.common.exceptions.PaymentInstrumentInvalidException;
import io.reflectoring.solid.lsp.paymentexample.redesigned.model.PaymentResponse;

public interface IPaymentInstrument  {
    void validate() throws PaymentInstrumentInvalidException;
    PaymentResponse collectPayment() throws PaymentFailedException;
}