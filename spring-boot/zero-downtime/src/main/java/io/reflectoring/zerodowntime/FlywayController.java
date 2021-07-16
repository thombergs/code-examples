package io.reflectoring.zerodowntime;

import org.flywaydb.core.Flyway;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
class FlywayController {

    private final Flyway flyway;

    public FlywayController(Flyway flyway) {
        this.flyway = flyway;
    }

    @GetMapping("/flywayMigrate")
    String flywayMigrate() {
        flyway.migrate();
        return "success";
    }

}
