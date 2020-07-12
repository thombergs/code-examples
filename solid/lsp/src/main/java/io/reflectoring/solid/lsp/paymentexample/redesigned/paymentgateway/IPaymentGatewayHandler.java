package io.reflectoring.solid.lsp.paymentexample.redesigned.paymentgateway;

import io.reflectoring.solid.lsp.paymentexample.common.exceptions.PaymentFailedException;
import io.reflectoring.solid.lsp.paymentexample.redesigned.model.PaymentGatewayResponse;

public interface IPaymentGatewayHandler {
    PaymentGatewayResponse handlePayment() throws PaymentFailedException;
}