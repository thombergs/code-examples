package io.reflectoring.structuredlogging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;

@Component
public class DomainService {

    private static final Logger logger = LoggerFactory.getLogger(DomainService.class);

    String hello(){
        logger.info("log event from the domain service");
        return "hello";
    }

    String callThirdPartyService() throws InterruptedException {
        logger.info("log event from the domain service");
        Instant start = Instant.now();

        Thread.sleep(2000); // simulating an expensive operation

        Duration duration = Duration.between(start, Instant.now());

        MDC.put("thirdPartyCallDuration", String.valueOf(duration.getNano()));
        logger.info("call to third party service successful!");
        MDC.remove("thirdPartyCallDuration");

        return "hello";
    }

}
