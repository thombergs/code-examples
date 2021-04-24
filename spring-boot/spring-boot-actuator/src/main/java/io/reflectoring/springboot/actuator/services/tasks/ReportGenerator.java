package io.reflectoring.springboot.actuator.services.tasks;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ReportGenerator {
  @Scheduled(cron = "0 0 12 * * *")
  public void generateReports() {
    System.out.println("Generating reports");
  }
}
