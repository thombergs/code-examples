package io.reflectoring.resilience4j.bulkhead;

import io.github.resilience4j.bulkhead.Bulkhead;
import io.github.resilience4j.bulkhead.BulkheadConfig;
import io.github.resilience4j.bulkhead.BulkheadRegistry;
import io.github.resilience4j.micrometer.tagged.TaggedBulkheadMetrics;
import io.micrometer.core.instrument.Meter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import io.reflectoring.resilience4j.bulkhead.model.Flight;
import io.reflectoring.resilience4j.bulkhead.model.SearchRequest;
import io.reflectoring.resilience4j.bulkhead.services.FlightSearchService;
import java.time.Duration;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.StreamSupport;

public class Examples
{
    void printDefaultValues() {
        BulkheadConfig config = BulkheadConfig.ofDefaults();
        System.out.println("Max concurrent calls = " + config.getMaxConcurrentCalls());
        System.out.println("Max wait duration = " + config.getMaxWaitDuration());
        System.out.println("Writable stack trace enabled = " + config.isWritableStackTraceEnabled());
        System.out.println("Fair call handling enabled = " + config.isFairCallHandlingEnabled());
    }

    void basicExample() {
        BulkheadConfig config = BulkheadConfig.custom()
            .maxConcurrentCalls(2)
            .maxWaitDuration(Duration.ofSeconds(2))
            .build();
        BulkheadRegistry registry = BulkheadRegistry.of(config);
        Bulkhead bulkhead = registry.bulkhead("flightSearchService");

        FlightSearchService service = new FlightSearchService();
        SearchRequest request = new SearchRequest("NYC", "LAX", "08/30/2020");

        Supplier<List<Flight>> flightsSupplier = () -> service.searchFlightsTakingOneSecond(request);
        Supplier<List<Flight>> decoratedFlightsSupplier = Bulkhead.decorateSupplier(bulkhead, flightsSupplier);

        for (int i=0; i<4; i++) {
            CompletableFuture
                .supplyAsync(decoratedFlightsSupplier)
                .thenAccept(flights -> System.out.println("Received results"));
        }
    }

    void basicExample_BulkheadFullException() {
        BulkheadConfig config = BulkheadConfig.custom()
            .maxConcurrentCalls(2)
            .maxWaitDuration(Duration.ofSeconds(1))
            .build();
        BulkheadRegistry registry = BulkheadRegistry.of(config);
        Bulkhead bulkhead = registry.bulkhead("flightSearchService");

        FlightSearchService service = new FlightSearchService();
        SearchRequest request = new SearchRequest("NYC", "LAX", "08/30/2020");

        Supplier<List<Flight>> flightsSupplier = () -> service.searchFlightsTakingOneSecond(request);
        Supplier<List<Flight>> decoratedFlightsSupplier = Bulkhead.decorateSupplier(bulkhead, flightsSupplier);

        for (int i=0; i<3; i++) {
            CompletableFuture
                .supplyAsync(decoratedFlightsSupplier)
                .whenComplete( (r, t) -> {
                    if (t != null) {
                        Throwable cause = t.getCause();
                        if (cause != null) {
                            cause.printStackTrace();
                        }
                    }
                    if (r != null) {
                        System.out.println("Received results");
                    }
                });
        }
    }

    void bulkheadException_WithStackTraceOff() {
        BulkheadConfig config = BulkheadConfig.custom()
            .maxConcurrentCalls(2)
            .maxWaitDuration(Duration.ofSeconds(1))
            .writableStackTraceEnabled(false)
            .build();
        BulkheadRegistry registry = BulkheadRegistry.of(config);
        Bulkhead bulkhead = registry.bulkhead("flightSearchService");

        FlightSearchService service = new FlightSearchService();
        SearchRequest request = new SearchRequest("NYC", "LAX", "08/30/2020");

        Supplier<List<Flight>> flightsSupplier = () -> service.searchFlightsTakingOneSecond(request);
        Supplier<List<Flight>> decoratedFlightsSupplier = Bulkhead.decorateSupplier(bulkhead, flightsSupplier);

        for (int i=0; i<3; i++) {
            CompletableFuture
                .supplyAsync(decoratedFlightsSupplier)
                .whenComplete( (r, t) -> {
                    if (t != null) {
                        Throwable cause = t.getCause();
                        if (cause != null) {
                            cause.printStackTrace();
                        }
                    }
                    if (r != null) {
                        System.out.println("Received results");
                    }
                });
        }

    }

    void eventsExample() {
        BulkheadConfig config = BulkheadConfig.custom()
                                    .maxWaitDuration(Duration.ofMillis(500))
                                    .maxConcurrentCalls(3)
                                    .build();

        BulkheadRegistry registry = BulkheadRegistry.of(config);
        Bulkhead bulkhead = registry.bulkhead("flightSearch");

        bulkhead.getEventPublisher().onCallPermitted(e -> System.out.println(e.toString()));
        bulkhead.getEventPublisher().onCallFinished(e -> System.out.println(e.toString()));
        bulkhead.getEventPublisher().onCallRejected(e -> System.out.println(e.toString()));

        FlightSearchService service = new FlightSearchService();
        SearchRequest request = new SearchRequest("NYC", "LAX", "08/30/2020");

        Supplier<List<Flight>> flightsSupplier = () -> service.searchFlightsTakingRandomTime(request);
        Supplier<List<Flight>> decoratedFlightsSupplier = Bulkhead.decorateSupplier(bulkhead, flightsSupplier);

        for (int i=0; i<5; i++) {
            CompletableFuture
                .supplyAsync(decoratedFlightsSupplier)
                .whenComplete( (r, t) -> {
                    if (t != null) {
                        t.printStackTrace();
                    }
                    if (r != null) {
                        System.out.println("Received results");
                    }
                });
        }
    }

    void metricsExample() {
        BulkheadConfig config = BulkheadConfig.custom()
            .maxWaitDuration(Duration.ofMillis(500))
            .maxConcurrentCalls(8)
            .build();

        BulkheadRegistry registry = BulkheadRegistry.of(config);
        Bulkhead bulkhead = registry.bulkhead("flightSearchService");

        MeterRegistry meterRegistry = new SimpleMeterRegistry();
        TaggedBulkheadMetrics.ofBulkheadRegistry(registry).bindTo(meterRegistry);

        bulkhead.getEventPublisher().onCallPermitted(e -> printMetricDetails(meterRegistry));
        bulkhead.getEventPublisher().onCallRejected(e -> printMetricDetails(meterRegistry));
        bulkhead.getEventPublisher().onCallFinished(e -> printMetricDetails(meterRegistry));

        FlightSearchService service = new FlightSearchService();
        SearchRequest request = new SearchRequest("NYC", "LAX", "08/30/2020");

        Supplier<List<Flight>> flightsSupplier = () -> service.searchFlightsTakingRandomTime(request);
        Supplier<List<Flight>> decoratedFlightsSupplier = Bulkhead.decorateSupplier(bulkhead, flightsSupplier);

        for (int i=0; i<5; i++) {
            CompletableFuture.supplyAsync(decoratedFlightsSupplier)
                             .whenComplete( (r, t) -> {
                                 if (r != null) {
                                    System.out.println("Received results");
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
        Examples examples = new Examples();
        System.out.println("---------------------------- printDefaultValues -------------------------------------------");
        examples.printDefaultValues();
        System.out.println("-----------------------------------------------------------------------");

        System.out.println("---------------------------- basicExample -------------------------------------------");
        examples.basicExample();
        delay(3);
        System.out.println("-----------------------------------------------------------------------");

        System.out.println("---------------------------- basicExample_BulkheadFullException -------------------------------------------");
        examples.basicExample_BulkheadFullException();
        delay(3);
        System.out.println("-----------------------------------------------------------------------");

        System.out.println("---------------------------- bulkheadException_WithStackTraceOff -------------------------------------------");
        examples.bulkheadException_WithStackTraceOff();
        delay(10);
        System.out.println("-----------------------------------------------------------------------");

        System.out.println("---------------------------- eventsExample -------------------------------------------");
        examples.eventsExample();
        delay(5); // delay just to let the above operation complete
        System.out.println("-----------------------------------------------------------------------");

        System.out.println("---------------------------- metricsExample -------------------------------------------");
        examples.metricsExample();
        delay(15);
        System.out.println("-----------------------------------------------------------------------");
    }
}