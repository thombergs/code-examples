package io.reflectoring.solid.lsp.paymentexample.redesigned.model;

public class PaymentGatewayResponse {
    String fingerprint;

    public PaymentGatewayResponse() {
    }

    public String getFingerprint() {
        return fingerprint;
    }

    public void setFingerprint(String fingerprint) {
        this.fingerprint = fingerprint;
    }
}
