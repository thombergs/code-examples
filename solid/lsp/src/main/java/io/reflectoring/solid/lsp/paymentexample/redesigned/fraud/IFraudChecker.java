package io.reflectoring.solid.lsp.paymentexample.redesigned.fraud;

import io.reflectoring.solid.lsp.paymentexample.common.exceptions.FraudDetectedException;

public interface IFraudChecker {
    void runChecks() throws FraudDetectedException;
}
