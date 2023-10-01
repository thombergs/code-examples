package io.reflectoring.openfeign.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.common.Slf4jNotifier;
import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import feign.Feign;
import feign.RetryableException;
import feign.hystrix.HystrixFeign;
import feign.jackson.JacksonDecoder;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@WireMockTest
public class CalculatorServiceTest {

    public static final String SERVER = "localhost";
    public static final int PORT = 8080;
    public static final String HOST = String.format("http://%s:%d/", SERVER, PORT);

    @RegisterExtension
    public static final WireMockExtension WIRE_MOCK_EXTENSION = WireMockExtension.newInstance()
                                                                                 .options(wireMockConfig().bindAddress(SERVER)
                                                                                                          .port(PORT)
                                                                                                          .notifier(new Slf4jNotifier(true)))
                                                                                 .build();

    @Test
    void givenTwoNumbersReturnAddition() {
        final long firstNumber = 10L;
        final long secondNumber = 20L;
        final long expectedResult = firstNumber + secondNumber;

        WIRE_MOCK_EXTENSION.stubFor(post(urlPathEqualTo("/operations/add")).withQueryParam("firstNumber", equalTo(String.valueOf(firstNumber)))
                                                                           .withQueryParam("secondNumber", equalTo(String.valueOf(secondNumber)))
                                                                           .willReturn(ok(String.valueOf(expectedResult))));

        // Call the method you want to test
        final CalculatorService target = Feign.builder().decoder(new JacksonDecoder())
                                              .target(CalculatorService.class, HOST);
        final Long result = target.add(firstNumber, secondNumber);

        // verify interactions and results
        assertThat(result).as("Incorrect addition for %d and %d", firstNumber, secondNumber).isEqualTo(expectedResult);
    }

    @Test
    void givenNegativeDivisorDivisionReturnsError() {
        final ExceptionMessage exceptionMessage = new ExceptionMessage(400, "Cannot divide by zero");
        ObjectMapper mapper = new ObjectMapper();
        String error = null;
        try {
            error = mapper.writeValueAsString(exceptionMessage);
        } catch (JsonProcessingException e) {
            Assertions.fail(e.getMessage());
        }
        assertThat(error).isNotEmpty();

        WIRE_MOCK_EXTENSION.stubFor(post(urlPathEqualTo("/operations/divide")).withQueryParam("firstNumber", equalTo("10"))
                                                                              .withQueryParam("secondNumber", equalTo("0"))
                                                                              .willReturn(badRequest().withBody(error)));

        // Call the method you want to test
        final long firstNumber = 10L;
        final long secondNumber = 0L;

        final CalculatorErrorDecoder errorDecoder = new CalculatorErrorDecoder();
        final CalculatorService target = Feign.builder().decoder(new JacksonDecoder()).errorDecoder(errorDecoder)
                                              .target(CalculatorService.class, HOST);

        // verify interactions and results
        assertThatThrownBy(() -> target.divide(firstNumber, secondNumber)).as("There should be divide by zero error")
                                                                          .isInstanceOf(RuntimeException.class)
                                                                          .hasMessageContaining(exceptionMessage.getMessage());
    }

    @Test
    void givenTwoNumbersAndServerReturningUnauthorizedErrorShouldRetry() {
        final ExceptionMessage exceptionMessage = new ExceptionMessage(401, "Unauthorized");
        ObjectMapper mapper = new ObjectMapper();
        String error = null;
        try {
            error = mapper.writeValueAsString(exceptionMessage);
        } catch (JsonProcessingException e) {
            Assertions.fail(e.getMessage());
        }
        assertThat(error).isNotEmpty();
        final long firstNumber = 10L;
        final long secondNumber = 20L;
        final long expectedResult = firstNumber + secondNumber;

        WIRE_MOCK_EXTENSION.stubFor(post(urlPathEqualTo("/operations/add")).withQueryParam("firstNumber", equalTo(String.valueOf(firstNumber)))
                                                                           .withQueryParam("secondNumber", equalTo(String.valueOf(secondNumber)))
                                                                           .willReturn(unauthorized().withBody(error)));

        // Call the method you want to test
        final long period = 100L;
        final long durationInSeconds = 1L;
        final int maxAttempts = 3;
        final CalculatorRetryer retryer = new CalculatorRetryer(period, maxAttempts);
        final CalculatorErrorDecoder errorDecoder = new CalculatorErrorDecoder();
        final CalculatorService target = Feign.builder().decoder(new JacksonDecoder()).retryer(retryer)
                                              .errorDecoder(errorDecoder)
                                              .target(CalculatorService.class, HOST);

        // verify interactions and results
        final SoftAssertions softly = new SoftAssertions();
        softly.assertThatThrownBy(() -> target.add(firstNumber, secondNumber)).as("There should be access error")
              .isInstanceOf(RetryableException.class)
              .hasMessageContaining(exceptionMessage.getMessage());
        final int attempt = retryer.getRetryAttempts();
        softly.assertThat(attempt).as("Expected to retry %d times but it retried %d times", maxAttempts, attempt)
              .isEqualTo(maxAttempts);
        softly.assertAll();
    }

    @Test
    void givenTwoNumbersAndServerReturningServerErrorShouldCircuitBreak() {
        final ExceptionMessage exceptionMessage = new ExceptionMessage(500, "Fatal Error");
        ObjectMapper mapper = new ObjectMapper();
        String error = null;
        try {
            error = mapper.writeValueAsString(exceptionMessage);
        } catch (JsonProcessingException e) {
            Assertions.fail(e.getMessage());
        }
        assertThat(error).isNotEmpty();
        final long firstNumber = 10L;
        final long secondNumber = 20L;
        final long expectedResult = firstNumber + secondNumber;

        WIRE_MOCK_EXTENSION.stubFor(post(urlPathEqualTo("/operations/add")).withQueryParam("firstNumber", equalTo(String.valueOf(firstNumber)))
                                                                           .withQueryParam("secondNumber", equalTo(String.valueOf(secondNumber)))
                                                                           .willReturn(serverError().withBody(error)));

        // Call the method you want to test
        final CalculatorHystrixFallback fallback = new CalculatorHystrixFallback();
        final CalculatorService target = HystrixFeign.builder().decoder(new JacksonDecoder())
                                                     .target(CalculatorService.class, HOST, fallback);

        // verify interactions and results
        final SoftAssertions softly = new SoftAssertions();
        softly.assertThat(target.add(firstNumber, secondNumber))
              .as("There should be result from fallback despite server error").isEqualTo(expectedResult);
        softly.assertAll();
    }
}
