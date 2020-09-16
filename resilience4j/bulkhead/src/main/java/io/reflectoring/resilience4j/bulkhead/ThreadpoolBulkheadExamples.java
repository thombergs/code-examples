package io.reflectoring.resilience4j.bulkhead;

import io.github.resilience4j.bulkhead.ThreadPoolBulkhead;
import io.github.resilience4j.bulkhead.ThreadPoolBulkheadConfig;
import io.github.resilience4j.bulkhead.ThreadPoolBulkheadRegistry;
import io.github.resilience4j.micrometer.tagged.TaggedThreadPoolBulkheadMetrics;
import io.micrometer.core.instrument.Meter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import io.reflectoring.resilience4j.bulkhead.model.Flight;
import io.reflectoring.resilience4j.bulkhead.model.SearchRequest;
import io.reflectoring.resilience4j.bulkhead.services.FlightSearchService;
import io.reflectoring.resilience4j.bulkhead.utils.RequestTrackingIdHolder;
import io.reflectoring.resilience4j.bulkhead.utils.RequestTrackingIdPropagator;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletionStage;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.StreamSupport;

public class ThreadpoolBulkheadExamples
{
  void printDefaultValues() {
    ThreadPoolBulkheadConfig config = ThreadPoolBulkheadConfig.ofDefaults();
    System.out.println("Max thread pool size = " + config.getMaxThreadPoolSize());
    System.out.println("Max core thread pool size = " + config.getCoreThreadPoolSize());
    System.out.println("Keep alive duration  = " + config.getKeepAliveDuration());
    System.out.println("Queue capacity = " + config.getQueueCapacity());
  }

  void basicExample() {
    ThreadPoolBulkheadConfig config = ThreadPoolBulkheadConfig.custom()
        .maxThreadPoolSize(2)
        .coreThreadPoolSize(1)
        .queueCapacity(1)
        .build();
    ThreadPoolBulkheadRegistry registry = ThreadPoolBulkheadRegistry.of(config);
    ThreadPoolBulkhead bulkhead = registry.bulkhead("flightSearchService");

    FlightSearchService service = new FlightSearchService();
    SearchRequest request = new SearchRequest("NYC", "LAX", "09/30/2020");

    Supplier<List<Flight>> flightsSupplier = () -> service.searchFlightsTakingOneSecond(request);
    Supplier<CompletionStage<List<Flight>>> decoratedFlightsSupplier = ThreadPoolBulkhead.decorateSupplier(bulkhead, flightsSupplier);

    for (int i=0; i<3; i++) {
      decoratedFlightsSupplier
          .get()
          .whenComplete((r,t) -> {
            if (r != null) {
              System.out.println("Received results");
            }
            if (t != null) {
              t.printStackTrace();
            }
          });
    }
  }

  void basicExample_BulkheadFullException() {
    ThreadPoolBulkheadConfig config = ThreadPoolBulkheadConfig.custom()
        .maxThreadPoolSize(2)
        .coreThreadPoolSize(1)
        .queueCapacity(1)
        .build();
    ThreadPoolBulkheadRegistry registry = ThreadPoolBulkheadRegistry.of(config);
    ThreadPoolBulkhead bulkhead = registry.bulkhead("flightSearchService");

    FlightSearchService service = new FlightSearchService();
    SearchRequest request = new SearchRequest("NYC", "LAX", "08/30/2020");

    Supplier<List<Flight>> flightsSupplier = () -> service.searchFlightsTakingOneSecond(request);
    Supplier<CompletionStage<List<Flight>>> decoratedFlightsSupplier = ThreadPoolBulkhead.decorateSupplier(bulkhead, flightsSupplier);

    for (int i=0; i<4; i++) {
      decoratedFlightsSupplier
          .get()
          .whenComplete((r,t) -> {
            if (r != null) {
              System.out.println("Received results");
            }
            if (t != null) {
              t.printStackTrace();
            }
          });
    }
  }

  void bulkheadException_WithStackTraceOff() {
    ThreadPoolBulkheadConfig config = ThreadPoolBulkheadConfig.custom()
        .maxThreadPoolSize(2)
        .coreThreadPoolSize(1)
        .queueCapacity(1)
        .writableStackTraceEnabled(false)
        .build();
    ThreadPoolBulkheadRegistry registry = ThreadPoolBulkheadRegistry.of(config);
    ThreadPoolBulkhead bulkhead = registry.bulkhead("flightSearchService");

    FlightSearchService service = new FlightSearchService();
    SearchRequest request = new SearchRequest("NYC", "LAX", "09/30/2020");

    Supplier<List<Flight>> flightsSupplier = () -> service.searchFlightsTakingOneSecond(request);
    Supplier<CompletionStage<List<Flight>>> decoratedFlightsSupplier = ThreadPoolBulkhead.decorateSupplier(bulkhead, flightsSupplier);

    for (int i=0; i<4; i++) {
      decoratedFlightsSupplier
          .get()
          .whenComplete((r,t) -> {
            if (r != null) {
              System.out.println("Received results");
            }
            if (t != null) {
              t.printStackTrace();
            }
          });
    }
  }

  void metricsExample() {
    ThreadPoolBulkheadConfig config = ThreadPoolBulkheadConfig.custom()
        .maxThreadPoolSize(5)
        .coreThreadPoolSize(3)
        .queueCapacity(5)
        .writableStackTraceEnabled(false)
        .build();
    ThreadPoolBulkheadRegistry registry = ThreadPoolBulkheadRegistry.of(config);
    ThreadPoolBulkhead bulkhead = registry.bulkhead("flightSearchService");

    MeterRegistry meterRegistry = new SimpleMeterRegistry();
    TaggedThreadPoolBulkheadMetrics.ofThreadPoolBulkheadRegistry(registry).bindTo(meterRegistry);

    bulkhead.getEventPublisher().onCallPermitted(e -> printMetricDetails(meterRegistry));
    bulkhead.getEventPublisher().onCallRejected(e -> printMetricDetails(meterRegistry));
    bulkhead.getEventPublisher().onCallFinished(e -> printMetricDetails(meterRegistry));

    FlightSearchService service = new FlightSearchService();
    SearchRequest request = new SearchRequest("NYC", "LAX", "09/30/2020");

    Supplier<List<Flight>> flightsSupplier = () -> service.searchFlightsTakingOneSecond(request);
    Supplier<CompletionStage<List<Flight>>> decoratedFlightsSupplier = ThreadPoolBulkhead.decorateSupplier(bulkhead, flightsSupplier);

    for (int i=0; i<11; i++) {
      decoratedFlightsSupplier
          .get()
          .whenComplete((r,t) -> {
            if (r != null) {
              System.out.println("Received results");
            }
            if (t != null) {
              t.printStackTrace();
            }
          });
    }
  }

  void contextPropagationExample_WithoutContextPropagator() {
    ThreadPoolBulkheadConfig config = ThreadPoolBulkheadConfig.custom()
        .maxThreadPoolSize(2)
        .coreThreadPoolSize(1)
        .queueCapacity(1)
        .build();
    ThreadPoolBulkheadRegistry registry = ThreadPoolBulkheadRegistry.of(config);
    ThreadPoolBulkhead bulkhead = registry.bulkhead("flightSearchService");

    FlightSearchService service = new FlightSearchService();
    SearchRequest request = new SearchRequest("NYC", "LAX", "09/30/2020");

    Supplier<List<Flight>> flightsSupplier = () -> service.searchFlightsTakingOneSecond_PrintCorrelationId(request);
    Supplier<CompletionStage<List<Flight>>> decoratedFlightsSupplier = ThreadPoolBulkhead.decorateSupplier(bulkhead, flightsSupplier);

    for (int i=0; i<2; i++) {
      String trackingId = UUID.randomUUID().toString();
      System.out.println("Setting trackingId " + trackingId + " on parent, main thread before calling flight search");
      RequestTrackingIdHolder.setRequestTrackingId(trackingId);
      decoratedFlightsSupplier
          .get()
          .whenComplete((r,t) -> {
            if (r != null) {
              System.out.println("Received results");
            }
            if (t != null) {
              t.printStackTrace();
            }
          });
    }
  }


  void contextPropagationExample_WithPropagation() {
    ThreadPoolBulkheadConfig config = ThreadPoolBulkheadConfig.custom()
        .maxThreadPoolSize(2)
        .coreThreadPoolSize(1)
        .queueCapacity(1)
        .contextPropagator(new RequestTrackingIdPropagator())
        .build();
    ThreadPoolBulkheadRegistry registry = ThreadPoolBulkheadRegistry.of(config);
    ThreadPoolBulkhead bulkhead = registry.bulkhead("flightSearchService");

    FlightSearchService service = new FlightSearchService();
    SearchRequest request = new SearchRequest("NYC", "LAX", "09/30/2020");

    Supplier<List<Flight>> flightsSupplier = () -> service.searchFlightsTakingOneSecond_PrintCorrelationId(request);
    Supplier<CompletionStage<List<Flight>>> decoratedFlightsSupplier = ThreadPoolBulkhead.decorateSupplier(bulkhead, flightsSupplier);

    for (int i=0; i<2; i++) {
      String trackingId = UUID.randomUUID().toString();
      System.out.println("Setting trackingId " + trackingId + " on parent, main thread before calling flight search");
      RequestTrackingIdHolder.setRequestTrackingId(trackingId);
      decoratedFlightsSupplier
          .get()
          .whenComplete((r,t) -> {
            if (r != null) {
              System.out.println("Received results");
            }
            if (t != null) {
              t.printStackTrace();
            }
          });
    }
  }

  void printMetricDetails(MeterRegistry meterRegistry) {
    Consumer<Meter> meterConsumer = meter -> {
      String desc = meter.getId().getDescription();
      String metricName = meter.getId().getName();
      Double metricValue = StreamSupport.stream(meter.measure().spliterator(), false)
          .filter(m -> m.getStatistic().name().equals("VALUE"))
          .findFirst()
          .map(m -> m.getValue())
          .orElse(0.0);
      System.out.println(desc + " - " + metricName + ": " + metricValue);
    };
    meterRegistry.forEachMeter(meterConsumer);
  }

  static void delay(int seconds) {
    // sleep to simulate delay
    try {
      Thread.sleep(seconds * 1000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  public static void main( String[] args )
  {
    ThreadpoolBulkheadExamples  examples = new ThreadpoolBulkheadExamples();
    System.out.println("---------------------------- printDefaultValues -------------------------------------------");
    examples.printDefaultValues();
    System.out.println("-----------------------------------------------------------------------");

    System.out.println("---------------------------- basicExample -------------------------------------------");
    examples.basicExample();
    delay(5);
    System.out.println("-----------------------------------------------------------------------");


    System.out.println("---------------------------- basicExample_BulkheadFullException -------------------------------------------");
    try {
      examples.basicExample_BulkheadFullException();
    }
    catch (Exception e) {
      // do nothing
    }
    delay(10);
    System.out.println("-----------------------------------------------------------------------");


    System.out.println("---------------------------- bulkheadException_WithStackTraceOff -------------------------------------------");
    try {
      examples.bulkheadException_WithStackTraceOff();
    }
    catch (Exception e) {
      // do nothing
    }
    delay(10);
    System.out.println("-----------------------------------------------------------------------");


    System.out.println("---------------------------- metricsExample -------------------------------------------");
    try {
      examples.metricsExample();
    }
    catch (Exception e) {

    }
    delay(15);
    System.out.println("-----------------------------------------------------------------------");


    System.out.println("---------------------------- contextPropagationExample_WithoutContextPropagator -------------------------------------------");
    examples.contextPropagationExample_WithoutContextPropagator();
    System.out.println("-----------------------------------------------------------------------");


    System.out.println("---------------------------- contextPropagationExample_WithPropagation -------------------------------------------");
    examples.contextPropagationExample_WithPropagation();
    delay(10);
    System.out.println("-----------------------------------------------------------------------");
  }
}
