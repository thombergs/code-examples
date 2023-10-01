package io.reflectoring.openfeign.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import feign.Response;
import feign.RetryableException;
import feign.codec.ErrorDecoder;

import java.io.IOException;
import java.io.InputStream;

public class CalculatorErrorDecoder implements ErrorDecoder {
    private final ErrorDecoder defaultErrorDecoder = new Default();

    @Override
    public Exception decode(String methodKey, Response response) {
        ExceptionMessage message = null;
        try (InputStream bodyIs = response.body().asInputStream()) {
            ObjectMapper mapper = new ObjectMapper();
            message = mapper.readValue(bodyIs, ExceptionMessage.class);
        } catch (IOException e) {
            return new Exception(e.getMessage());
        }
        final String messageStr = message == null ? "" : message.getMessage();
        switch (response.status()) {
            case 400:
                return new RuntimeException(messageStr.isEmpty() ? "Bad Request" : messageStr);
            case 401:
                return new RetryableException(response.status(), response.reason(), response.request()
                                                                                            .httpMethod(), null, response.request());
            case 404:
                return new RuntimeException(messageStr.isEmpty() ? "Not found" : messageStr);
            default:
                return defaultErrorDecoder.decode(methodKey, response);
        }
    }
}
