package io.reflectoring.structuredlogging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class Timer {

    private final DomainService domainService;

    private static final Logger logger = LoggerFactory.getLogger(Timer.class);

    public Timer(DomainService domainService) {
        this.domainService = domainService;
    }

    @Scheduled(fixedDelay = 5000)
    void scheduledHello() {
        MDC.put("codePath", "timer");
        logger.info("log event from timer");
        domainService.hello();
        MDC.remove("codePath");
    }

}
