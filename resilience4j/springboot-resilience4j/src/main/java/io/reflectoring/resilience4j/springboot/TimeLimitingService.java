package io.reflectoring.resilience4j.springboot;

import io.github.resilience4j.micrometer.tagged.TaggedTimeLimiterMetrics;
import io.github.resilience4j.ratelimiter.RequestNotPermitted;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.timelimiter.TimeLimiter.EventPublisher;
import io.github.resilience4j.timelimiter.TimeLimiterConfig;
import io.github.resilience4j.timelimiter.TimeLimiterRegistry;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import io.micrometer.core.instrument.Measurement;
import io.micrometer.core.instrument.Meter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import io.reflectoring.resilience4j.springboot.model.Flight;
import io.reflectoring.resilience4j.springboot.model.SearchRequest;
import io.reflectoring.resilience4j.springboot.services.FlightSearchService;
import java.sql.Time;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.StreamSupport;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TimeLimitingService {
  @Autowired
  private FlightSearchService remoteSearchService;

  @Autowired
  private TimeLimiterRegistry timeLimiterRegistry;

/*
  void printDefaultValues() {
    TimeLimiterConfig config = TimeLimiterConfig.ofDefaults();

    System.out.println(
        "getTimeoutDuration in ms = " + Duration.from(config.getTimeoutDuration()).toMillis());
    System.out.println("shouldCancelRunningFuture = " + config.shouldCancelRunningFuture());
  } */


  @TimeLimiter(name = "basicExample")
  CompletableFuture<List<Flight>> basicExample(SearchRequest request) {
    return CompletableFuture.supplyAsync(() -> remoteSearchService.searchFlightsTakingOneSecond(request));
  }

  @TimeLimiter(name = "timeoutExample")
  CompletableFuture<List<Flight>> timeoutExample(SearchRequest request) {
    return CompletableFuture.supplyAsync(() -> remoteSearchService.searchFlightsTakingOneSecond(request));
  }

  @TimeLimiter(name = "timeAndRateLimiter")
  @RateLimiter(name = "timeAndRateLimiter")
  CompletableFuture<List<Flight>> aspectOrderExample(SearchRequest request) {
    return CompletableFuture.supplyAsync(() -> remoteSearchService.searchFlightsTakingOneSecond(request));
  }

  /*
  void basicExample_ExcecuteCompletionStage() {
    TimeLimiterConfig config = TimeLimiterConfig.custom()
        .timeoutDuration(Duration.ofMillis(500))
        .build();

    TimeLimiterRegistry registry = TimeLimiterRegistry.of(config);
    TimeLimiter limiter = registry.timeLimiter("flightSearch");

    FlightSearchService service = new FlightSearchService();
    SearchRequest request = new SearchRequest("NYC", "LAX", "08/30/2020");

    Supplier<List<Flight>> flightSupplier = () -> service.searchFlightsTakingOneSecond(request);
    Supplier<CompletionStage<List<Flight>>> origCompletionStageSupplier = () -> CompletableFuture
        .supplyAsync(flightSupplier);
    ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
    CompletionStage<List<Flight>> decoratedCompletionStage = limiter
        .executeCompletionStage(scheduler, origCompletionStageSupplier);

    decoratedCompletionStage.whenComplete((result, ex) -> {
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

    scheduler.shutdown();
  }


  void whenToUseExample() {
    CompletableFuture.supplyAsync(this::slowMethod).thenAccept(System.out::println);
  }

  void whenToUseExample_Blocking()
      throws InterruptedException, ExecutionException, TimeoutException {
    CompletableFuture<Integer> completableFuture = CompletableFuture
        .supplyAsync(this::slowMethod);
    Integer result = completableFuture.get(3000, TimeUnit.MILLISECONDS);
    System.out.println(result);
  }

  int slowMethod() {
    System.out.println(Thread.currentThread().getName());
    // sleep to simulate delay
    try {
      Thread.sleep(2000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    return 0;
  }

  static void delay(int seconds) {
    // sleep to simulate delay
    try {
      Thread.sleep(seconds * 1000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  } */

  @TimeLimiter(name = "eventsExample")
  CompletableFuture<List<Flight>>  eventsExample(SearchRequest request) {
    return CompletableFuture.supplyAsync(() -> remoteSearchService.searchFlightsTakingRandomTime(request));
  }

  @TimeLimiter(name = "fallbackExample", fallbackMethod = "localCacheFlightSearch")
  CompletableFuture<List<Flight>> fallbackExample(SearchRequest request) {
    return CompletableFuture.supplyAsync(() -> remoteSearchService.searchFlightsTakingOneSecond(request));
  }

  private CompletableFuture<List<Flight>> localCacheFlightSearch(SearchRequest request, TimeoutException rnp) {
    System.out.println("Returning search results from cache");
    System.out.println(rnp.getMessage());
    CompletableFuture<List<Flight>> result = new CompletableFuture<>();
    result.complete(Arrays.asList(
        new Flight("XY 765", request.getFlightDate(), request.getFrom(), request.getTo()),
        new Flight("XY 781", request.getFlightDate(), request.getFrom(), request.getTo())));
    return result;
  }

  @PostConstruct
  void postConstruct() {
    EventPublisher eventPublisher = timeLimiterRegistry.timeLimiter("eventsExample").getEventPublisher();
    eventPublisher.onSuccess(System.out::println);
    eventPublisher.onError(System.out::println);
    eventPublisher.onTimeout(System.out::println);
  }
}