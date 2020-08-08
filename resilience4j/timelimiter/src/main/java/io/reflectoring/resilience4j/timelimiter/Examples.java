package io.reflectoring.resilience4j.timelimiter;

import io.github.resilience4j.micrometer.tagged.TaggedTimeLimiterMetrics;
import io.github.resilience4j.timelimiter.TimeLimiter;
import io.github.resilience4j.timelimiter.TimeLimiterConfig;
import io.github.resilience4j.timelimiter.TimeLimiterRegistry;
import io.micrometer.core.instrument.Measurement;
import io.micrometer.core.instrument.Meter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import io.reflectoring.resilience4j.timelimiter.model.Flight;
import io.reflectoring.resilience4j.timelimiter.model.SearchRequest;
import io.reflectoring.resilience4j.timelimiter.services.FlightSearchService;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
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

public class Examples {

  DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss SSS");

  void printDefaultValues() {
    TimeLimiterConfig config = TimeLimiterConfig.ofDefaults();

    System.out.println(
        "getTimeoutDuration in ms = " + Duration.from(config.getTimeoutDuration()).toMillis());
    System.out.println("shouldCancelRunningFuture = " + config.shouldCancelRunningFuture());
  }

  void basicExample_Successful() {
    TimeLimiterConfig config = TimeLimiterConfig.custom()
        .timeoutDuration(Duration.ofSeconds(2))
        .build();

    TimeLimiterRegistry registry = TimeLimiterRegistry.of(config);
    TimeLimiter limiter = registry.timeLimiter("flightSearch");

    FlightSearchService service = new FlightSearchService();
    SearchRequest request = new SearchRequest("NYC", "LAX", "08/30/2020");

    Supplier<List<Flight>> flightSupplier = () -> service.searchFlightsTakingOneSecond(request);
    Supplier<CompletionStage<List<Flight>>> origCompletionStageSupplier = () -> CompletableFuture
        .supplyAsync(flightSupplier);
    ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
    Supplier<CompletionStage<List<Flight>>> decoratedCompletionStageSupplier = limiter
        .decorateCompletionStage(scheduler, origCompletionStageSupplier);

    decoratedCompletionStageSupplier.get().whenComplete((result, ex) -> {
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

  void basicExample_TimeoutException() {
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
    Supplier<CompletionStage<List<Flight>>> decoratedCompletionStageSupplier = limiter
        .decorateCompletionStage(scheduler, origCompletionStageSupplier);

    decoratedCompletionStageSupplier.get().whenComplete((result, ex) -> {
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
  }

  void eventsExample() {
    TimeLimiterConfig config = TimeLimiterConfig.custom()
        .cancelRunningFuture(false)
        .timeoutDuration(Duration.ofSeconds(2))
        .build();
    TimeLimiterRegistry registry = TimeLimiterRegistry.of(config);
    TimeLimiter limiter = registry.timeLimiter("flightSearch");

    limiter.getEventPublisher().onSuccess(e -> System.out.println(e.toString()));
    limiter.getEventPublisher().onError(e -> System.out.println(e.toString()));
    limiter.getEventPublisher().onTimeout(e -> System.out.println(e.toString()));

    FlightSearchService service = new FlightSearchService();
    SearchRequest request = new SearchRequest("NYC", "LAX", "07/31/2020");

    Supplier<List<Flight>> flightsSupplier = () -> service.searchFlightsTakingRandomTime(request);
    ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
    Supplier<CompletionStage<List<Flight>>> origCompletionStageSupplier = () -> CompletableFuture
        .supplyAsync(flightsSupplier);
    Supplier<CompletionStage<List<Flight>>> decoratedCompletionStageSupplier = limiter
        .decorateCompletionStage(scheduler, origCompletionStageSupplier);

    for (int i = 0; i < 10; i++) {
      int attempt = i;
      decoratedCompletionStageSupplier
          .get()
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
    scheduler.shutdown();
  }

  void metricsExample() {
    TimeLimiterConfig config = TimeLimiterConfig.custom()
        .cancelRunningFuture(false)
        .timeoutDuration(Duration.ofSeconds(2))
        .build();
    TimeLimiterRegistry registry = TimeLimiterRegistry.of(config);
    TimeLimiter limiter = registry.timeLimiter("flightSearch");

    MeterRegistry meterRegistry = new SimpleMeterRegistry();
    TaggedTimeLimiterMetrics.ofTimeLimiterRegistry(registry).bindTo(meterRegistry);

    FlightSearchService service = new FlightSearchService();
    SearchRequest request = new SearchRequest("NYC", "LAX", "07/31/2020");

    Supplier<List<Flight>> flightsSupplier = () -> service.searchFlightsTakingRandomTime(request);
    ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
    Supplier<CompletionStage<List<Flight>>> origCompletionStageSupplier = () -> CompletableFuture
        .supplyAsync(flightsSupplier);
    Supplier<CompletionStage<List<Flight>>> decoratedCompletionStageSupplier = limiter
        .decorateCompletionStage(scheduler, origCompletionStageSupplier);

    List<CompletableFuture<List<Flight>>> futures = new ArrayList<>();
    for (int i = 0; i < 10; i++) {
      int attempt = i;
      CompletionStage<List<Flight>> stage = decoratedCompletionStageSupplier
          .get()
          .whenComplete((r, t) -> {
            if (t != null) {
              System.out.println("Error occurred on search " + attempt + ": " + t.getMessage());
            }
            if (r != null) {
              System.out
                  .println("Search " + attempt + " successful, found " + r.size() + " flights");
            }
          });

      futures.add(stage.toCompletableFuture());
    }
    CompletableFuture
        .allOf(futures.toArray(new CompletableFuture[0]))
        .whenComplete((r, t) -> printMetricDetails(meterRegistry));

    scheduler.shutdown();
  }

  void printMetricDetails(MeterRegistry meterRegistry) {
    Consumer<Meter> meterConsumer = meter -> {
      String desc = meter.getId().getDescription();
      String metricName = meter.getId().getName();
      String metricKind = meter.getId().getTag("kind");
      Double metricValue = StreamSupport.stream(meter.measure().spliterator(), false)
          .filter(m -> m.getStatistic().name().equals("COUNT"))
          .findFirst()
          .map(Measurement::getValue)
          .orElse(0.0);
      System.out.println(desc + " - " + metricName + "(" + metricKind + ")" + ": " + metricValue);
    };
    meterRegistry.forEachMeter(meterConsumer);
  }

  public static void main(String[] args) {
    Examples examples = new Examples();
    System.out.println("----------------------------------------------------------------------------------------------------");
    System.out.println("----------------------- printDefaultValues ----------------------------------------------------");
    examples.printDefaultValues();
    System.out.println("----------------------------------------------------------------------------------------------------");
    System.out.println("----------------------- whenToUseExample ----------------------------------------------------");
    examples.whenToUseExample();
    delay(2); // delay just to let the above async operation to complete
    System.out.println("----------------------------------------------------------------------------------------------------");
    System.out.println("----------------------- whenToUseExample_Blocking ----------------------------------------------------");
    try {
      examples.whenToUseExample_Blocking();
    } catch (InterruptedException | ExecutionException| TimeoutException e) {
      e.printStackTrace();
    }
    delay(2); // delay just to let the above async operation to complete
    System.out.println("----------------------------------------------------------------------------------------------------");
    System.out.println("----------------------- basicExample_Successful ----------------------------------------------------");
    examples.basicExample_Successful();

    delay(2); // delay just to let the above async operation to complete

    System.out.println("----------------------------------------------------------------------------------------------------");
    System.out.println("----------------------- basicExample_TimeoutException ----------------------------------------------");
    examples.basicExample_TimeoutException();

    delay(2); // delay just to let the above async operation to complete

    System.out.println("----------------------------------------------------------------------------------------------------");
    System.out.println("-------------------------basicExample_ExcecuteCompletionStage --------------------------------------");
    examples.basicExample_ExcecuteCompletionStage();

    delay(2); // delay just to let the above async operation to complete

    System.out.println("----------------------------------------------------------------------------------------------------");
    System.out.println("----------------------- eventsExample ----------------------------------------------------");
    examples.eventsExample();
    delay(10); // delay just to let the above async operation to complete
    System.out.println("----------------------------------------------------------------------------------------------------");
    System.out.println("----------------------- metricsExample ----------------------------------------------------");
    examples.metricsExample();
    delay(10); // delay just to let the above async operation to complete
    System.out.println("----------------------------------------------------------------------------------------------------");
  }
}