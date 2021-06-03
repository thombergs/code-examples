package io.reflectoring.springboot.actuator.services.tasks;

import java.util.Date;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class CleanUpAbandonedBaskets {

  @Scheduled(fixedDelay = 900000)
  public void process() {
    System.out.println("Cleaning abandoned baskets, current time: " + new Date());
    try {
      Thread.sleep(1000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }
}
