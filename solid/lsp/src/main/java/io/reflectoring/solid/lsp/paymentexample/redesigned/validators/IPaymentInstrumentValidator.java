package io.reflectoring.solid.lsp.paymentexample.redesigned.validators;

import io.reflectoring.solid.lsp.paymentexample.common.exceptions.PaymentInstrumentInvalidException;

public interface IPaymentInstrumentValidator {
    void validate() throws PaymentInstrumentInvalidException;
}
