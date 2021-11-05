package io.reflectoring.resilience4j.springboot;

import io.reflectoring.resilience4j.springboot.model.Flight;
import io.reflectoring.resilience4j.springboot.model.SearchRequest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TimeLimiterExamplesRunner {

  @Autowired
  private TimeLimitingService service;

  private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss SSS");

  public static void main(String[] args) {
    TimeLimiterExamplesRunner runner = new TimeLimiterExamplesRunner();
    runner.run();
  }

  static void delay(int seconds) {
    // sleep to simulate delay
    try {
      Thread.sleep(seconds * 1000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  public void run() {
    System.out.println("Running timelimiter examples");

    System.out.println(
        "----------------------- basicExample ----------------------------------------------------");
    basicExample();

    delay(2); // delay just to let the above async operation to complete

    System.out.println(
        "----------------------------------------------------------------------------------------------------");

    System.out.println("----------------------- timeoutExample ----------------------------------------------");
    timeoutExample();

    delay(2); // delay just to let the above async operation to complete

    System.out.println("----------------------------------------------------------------------------------------------------");

    System.out.println("----------------------- fallbackExample ----------------------------------------------");
    fallbackExample();

    delay(2); // delay just to let the above async operation to complete

    System.out.println("----------------------------------------------------------------------------------------------------");

    System.out.println(
        "----------------------- eventsExample ----------------------------------------------------");
    eventsExample();
    delay(10); // delay just to let the above async operation to complete
    System.out.println(
        "----------------------------------------------------------------------------------------------------");
  }

  private void eventsExample() {
    SearchRequest request = new SearchRequest("NYC", "LAX", "10/30/2021");
    for (int i = 0; i < 10; i++) {
      int attempt = i;
      service.eventsExample(request)
          .whenComplete((r, t) -> {
            if (t != null) {
              System.out.println("Error occurred on search " + attempt + ": " + t.getMessage());
            }
            if (r != null) {
              System.out
                  .println("Search " + attempt + " successful, found " + r.size() + " flights");
            }
          });
    }
  }

  private void timeoutExample() {
    SearchRequest request = new SearchRequest("NYC", "LAX", "10/30/2021");
    System.out.println("Calling search; current thread = " + Thread.currentThread().getName());
    CompletableFuture<List<Flight>> results = service.timeoutExample(request);
    results.whenComplete((result, ex) -> {
      if (ex != null) {
        System.out.println("Exception " +
            ex.getMessage() +
            " on thread " +
            Thread.currentThread().getName() +
            " at " +
            LocalDateTime.now().format(formatter));
        ex.printStackTrace();
      }
      if (result != null) {
        System.out.println(result + " on thread " + Thread.currentThread().getName());
      }
    });
  }

  private void basicExample() {
    SearchRequest request = new SearchRequest("NYC", "LAX", "10/30/2021");
    System.out.println("Calling search; current thread = " + Thread.currentThread().getName());
    CompletableFuture<List<Flight>> results = service.basicExample(request);
    results.whenComplete((result, ex) -> {
      if (ex != null) {
        System.out.println("Exception " +
            ex.getMessage() +
            " on thread " +
            Thread.currentThread().getName() +
            " at " +
            LocalDateTime.now().format(formatter));
      }
      if (result != null) {
        System.out.println(result + " on thread " + Thread.currentThread().getName());
      }
    });
  }

  private void fallbackExample() {
    SearchRequest request = new SearchRequest("NYC", "LAX", "10/30/2021");
    System.out.println("Calling search; current thread = " + Thread.currentThread().getName());
    CompletableFuture<List<Flight>> results = service.fallbackExample(request);
    results.whenComplete((result, ex) -> {
      if (ex != null) {
        System.out.println("Exception " +
            ex.getMessage() +
            " on thread " +
            Thread.currentThread().getName() +
            " at " +
            LocalDateTime.now().format(formatter));
      }
      if (result != null) {
        System.out.println(result + " on thread " + Thread.currentThread().getName());
      }
    });
  }
}