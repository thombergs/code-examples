package io.reflectoring.structuredlogging;

import io.reflectoring.CombobulatorException;
import io.reflectoring.FluxCompensatorException;
import io.reflectoring.MaintenanceWindowException;
import io.reflectoring.ThingyException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Random;


@RestController
public class WebController {

    private final DomainService domainService;

    private static final Logger logger = LoggerFactory.getLogger(WebController.class);

    private final Random random = new Random();

    public WebController(DomainService domainService) {
        this.domainService = domainService;
    }

    @GetMapping("/hello")
    String helloWorld() {
        logger.info("log event from the web controller");
        return domainService.hello();
    }

    @GetMapping("/thirdparty")
    String thirdparty() throws InterruptedException {
        logger.info("log event from the web controller");
        return domainService.callThirdPartyService();
    }

    @GetMapping("/exception")
    String error() {
        logger.info("log event from the web controller");

        int r = random.nextInt(101);

        if (r <= 10) {
            throw new CombobulatorException();
        } else if (r <= 30) {
            throw new FluxCompensatorException();
        } else if (r <= 80) {
            throw new ThingyException();
        } else {
            throw new MaintenanceWindowException();
        }
    }

}
