package io.reflectoring.resilience4j.retry;

import static java.time.temporal.ChronoUnit.SECONDS;

import io.github.resilience4j.core.IntervalFunction;
import io.github.resilience4j.micrometer.tagged.TaggedRetryMetrics;
import io.github.resilience4j.retry.Retry;
import io.github.resilience4j.retry.RetryConfig;
import io.github.resilience4j.retry.RetryRegistry;
import io.micrometer.core.instrument.Meter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import io.reflectoring.resilience4j.retry.exceptions.FlightServiceBaseException;
import io.reflectoring.resilience4j.retry.exceptions.RateLimitExceededException;
import io.reflectoring.resilience4j.retry.exceptions.SeatsUnavailableException;
import io.reflectoring.resilience4j.retry.model.BookingRequest;
import io.reflectoring.resilience4j.retry.model.BookingResponse;
import io.reflectoring.resilience4j.retry.model.Flight;
import io.reflectoring.resilience4j.retry.model.SearchRequest;
import io.reflectoring.resilience4j.retry.model.SearchResponse;
import io.reflectoring.resilience4j.retry.services.FlightBookingService;
import io.reflectoring.resilience4j.retry.services.FlightSearchService;
import io.reflectoring.resilience4j.retry.services.failures.FailHalfTheTime;
import io.reflectoring.resilience4j.retry.services.failures.FailNTimes;
import io.reflectoring.resilience4j.retry.services.failures.RateLimitFailNTimes;
import io.vavr.CheckedFunction0;
import java.time.Duration;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.StreamSupport;

public class Examples {

    void generalUsagePattern() {
        RetryConfig config = RetryConfig.ofDefaults(); // ----> 1
        RetryRegistry registry = RetryRegistry.of(config); // ----> 2
        Retry retry = registry.retry("flightSearchService", config); // ----> 3

        FlightSearchService service = new FlightSearchService();
        SearchRequest request = new SearchRequest("NYC", "LAX", "07/31/2020");
        Supplier<List<Flight>> flightSearchSupplier = () -> service.searchFlights(request); // ----> 4

        Supplier<List<Flight>> retryingFlightSearch = Retry.decorateSupplier(retry, flightSearchSupplier); // ----> 5

        System.out.println(retryingFlightSearch.get()); // ----> 6
    }

    void basicExample() {
        RetryConfig config = RetryConfig.custom().maxAttempts(3).waitDuration(Duration.of(2, SECONDS)).build();
        RetryRegistry registry = RetryRegistry.of(config);
        Retry retry = registry.retry("flightSearchService", config);

        FlightSearchService service = new FlightSearchService();
        service.setPotentialFailure(new FailNTimes(1));

        SearchRequest request = new SearchRequest("NYC", "LAX", "07/31/2020");
        Supplier<List<Flight>> flightSearchSupplier = () -> service.searchFlights(request);

        Supplier<List<Flight>> retryingFlightSearch = Retry.decorateSupplier(retry, flightSearchSupplier);
        System.out.println(retryingFlightSearch.get());
    }

    void basicExample_ServiceThrowingException_v1() {
        RetryConfig config = RetryConfig.custom().maxAttempts(3).build();
        RetryRegistry registry = RetryRegistry.of(config);
        Retry retry = registry.retry("flightSearchService", config);

        FlightSearchService service = new FlightSearchService();
        service.setPotentialFailure(new FailNTimes(1));

        SearchRequest request = new SearchRequest("NYC", "LAX", "07/31/2020");
        Supplier<List<Flight>> flightSearchSupplier = () -> {
            try {
                return service.searchFlightsThrowingException(request);
            } catch (Exception e) {
                // handle exception
            }
            return Collections.emptyList();
        };

        Supplier<List<Flight>> retryingFlightSearch = Retry.decorateSupplier(retry, flightSearchSupplier);
        System.out.println(retryingFlightSearch.get());
    }

    void basicExample_ServiceThrowingException_v2() {
        RetryConfig config = RetryConfig.custom().maxAttempts(3).build();
        RetryRegistry registry = RetryRegistry.of(config);
        Retry retry = registry.retry("flightSearchService", config);

        FlightSearchService service = new FlightSearchService();
        service.setPotentialFailure(new FailNTimes(1));

        SearchRequest request = new SearchRequest("NYC", "LAX", "07/31/2020");
        CheckedFunction0<List<Flight>> retryingFlightSearch = Retry.decorateCheckedSupplier(retry, () -> service.searchFlightsThrowingException(request));

        try {
            System.out.println(retryingFlightSearch.apply());
        } catch (Throwable throwable) {
            // handle exception
        }
    }

    void predicateExample() {
        RetryConfig config = RetryConfig.<SearchResponse>custom().
                                            maxAttempts(3).
                                            waitDuration(Duration.of(3, SECONDS)).
                                            retryOnResult(searchResponse -> searchResponse.getErrorCode().equals("FS-167")).
                                            build();
        RetryRegistry registry = RetryRegistry.of(config);
        Retry retry = registry.retry("flightSearchService", config);

        FlightSearchService service = new FlightSearchService();
        SearchRequest request = new SearchRequest("NYC", "LAX", "07/31/2020");
        CheckedFunction0<SearchResponse> retryingFlightSearch = Retry.decorateCheckedSupplier(retry, () -> service.httpSearchFlights(request));
        SearchResponse response;
        try {
           response = retryingFlightSearch.apply();
           System.out.println(response);
        } catch (Throwable throwable) {
            // handle exception
        }
    }

    void retryExceptionsIgnoreExceptionsExample() {
        RetryConfig config = RetryConfig.custom().
                maxAttempts(3).
                waitDuration(Duration.of(3, SECONDS)).
                retryExceptions(FlightServiceBaseException.class).
                ignoreExceptions(SeatsUnavailableException.class).
                build();
        RetryRegistry registry = RetryRegistry.of(config);
        Retry retry = registry.retry("flightBookService", config);

        FlightBookingService service = new FlightBookingService();
        System.out.println("Example to illustrate: rate limit runtime exception - will be retried");
        // rate limit exception
        service.setPotentialFailure(new RateLimitFailNTimes(2));
        Flight flight = new Flight("XY 213", "07/30/2020", "NYC", "LAX");
        BookingRequest request = new BookingRequest(UUID.randomUUID().toString(), flight, 2, "C");
        Supplier<BookingResponse> bookingResponseSupplier = () -> service.bookFlight(request);
        Supplier<BookingResponse> bookingResponse = Retry.decorateSupplier(retry, bookingResponseSupplier);

        try {
            System.out.println(bookingResponse.get());
        }
        catch (RateLimitExceededException rle) {
            rle.printStackTrace();
        }

        System.out.println("Example to illustrate: no seats available runtime exception will not be retried");
        // seats not available checked exception
        FlightBookingService service2 = new FlightBookingService();
        Flight flight2 = new Flight("XY 765", "07/30/2020", "NYC", "LAX");
        BookingRequest request2 = new BookingRequest(UUID.randomUUID().toString(), flight2, 2, "C");
        CheckedFunction0<BookingResponse> bookingResponseSupplier2 = () -> service2.bookFlight(request2);
        CheckedFunction0<BookingResponse> bookingResponse2 = Retry.decorateCheckedSupplier(retry, bookingResponseSupplier2);

        try {
            System.out.println(bookingResponse2.apply());
        }
        catch (Throwable e) {
            e.printStackTrace();
        }
    }

    void intervalFunction_Random() {
        RetryConfig config = RetryConfig.custom().
                                        maxAttempts(4).
                                        intervalFunction(IntervalFunction.ofRandomized(2000)).
                                        build();
        RetryRegistry registry = RetryRegistry.of(config);
        Retry retry = registry.retry("flightSearchService", config);

        FlightSearchService service = new FlightSearchService();
        service.setPotentialFailure(new FailNTimes(3));

        SearchRequest request = new SearchRequest("NYC", "LAX", "07/31/2020");
        Supplier<List<Flight>> flightSearchSupplier = () -> service.searchFlights(request);

        Supplier<List<Flight>> retryingFlightSearch = Retry.decorateSupplier(retry, flightSearchSupplier);
        System.out.println(retryingFlightSearch.get());
    }

    void intervalFunction_Exponential() {
        RetryConfig config = RetryConfig.custom().
                                            maxAttempts(6).
                                            intervalFunction(IntervalFunction.ofExponentialBackoff(1000, 2)).
                                            build();
        RetryRegistry registry = RetryRegistry.of(config);
        Retry retry = registry.retry("flightSearchService", config);

        FlightSearchService service = new FlightSearchService();
        service.setPotentialFailure(new FailNTimes(5));

        SearchRequest request = new SearchRequest("NYC", "LAX", "07/31/2020");
        Supplier<List<Flight>> flightSearchSupplier = () -> service.searchFlights(request);

        Supplier<List<Flight>> retryingFlightSearch = Retry.decorateSupplier(retry, flightSearchSupplier);
        System.out.println(retryingFlightSearch.get());
    }

    void asyncRetryExample()  {
        FlightSearchService service = new FlightSearchService();
        service.setPotentialFailure(new FailNTimes(4));
        SearchRequest request = new SearchRequest("NYC", "LAX", "07/31/2020");

        RetryConfig config = RetryConfig.custom().maxAttempts(5).waitDuration(Duration.of(1, SECONDS)).build();
        RetryRegistry registry = RetryRegistry.of(config);
        Retry retry = registry.retry("flightSearchService", config);

        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
        Supplier<CompletionStage<List<Flight>>> completionStageSupplier = () -> CompletableFuture.supplyAsync(() -> service.searchFlights(request));
        retry.executeCompletionStage(scheduler, completionStageSupplier).thenAccept(System.out::println);

        // Sleep a few seconds to let the other thread complete
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        scheduler.shutdown();
    }

    void retryEventsExample() {
        RetryConfig config = RetryConfig.custom().maxAttempts(3).waitDuration(Duration.of(1, SECONDS)).build();
        RetryRegistry registry = RetryRegistry.of(config);
        Retry retry = registry.retry("flightSearchService", config);

        Retry.EventPublisher publisher = retry.getEventPublisher();
        publisher.onRetry(event -> System.out.println(event.toString()));
        publisher.onSuccess(event -> System.out.println(event.toString()));

        FlightSearchService service = new FlightSearchService();
        service.setPotentialFailure(new RateLimitFailNTimes(2));

        SearchRequest request = new SearchRequest("NYC", "LAX", "07/31/2020");
        List<Flight> flights = retry.executeSupplier(() -> service.searchFlights(request));
        System.out.println(flights);
    }

    void retryOnException() {
        Predicate<Throwable> rateLimitPredicate = rle -> (rle instanceof  RateLimitExceededException)
                                                                && "RL-101".equals(((RateLimitExceededException) rle).getErrorCode());
        RetryConfig config = RetryConfig.custom().
                                            maxAttempts(3).
                                            waitDuration(Duration.of(1, SECONDS)).
                                            retryOnException(rateLimitPredicate).
                                            build();
        RetryRegistry registry = RetryRegistry.of(config);
        Retry retry = registry.retry("flightSearchService", config);

        FlightSearchService service = new FlightSearchService();
        service.setPotentialFailure(new RateLimitFailNTimes(2));

        SearchRequest request = new SearchRequest("NYC", "LAX", "07/31/2020");
        List<Flight> flights = retry.executeSupplier(() -> service.searchFlights(request));
        System.out.println(flights);
    }

    void retryMetrics() {
        RetryConfig config = RetryConfig.custom().maxAttempts(3).build();
        RetryRegistry retryRegistry = RetryRegistry.of(config);
        Retry retry = retryRegistry.retry("flightSearchService", config);

        MeterRegistry meterRegistry = new SimpleMeterRegistry();
        TaggedRetryMetrics.ofRetryRegistry(retryRegistry).bindTo(meterRegistry);

        FlightSearchService service = new FlightSearchService();

        SearchRequest request = new SearchRequest("NYC", "LAX", "07/31/2020");
        Supplier<List<Flight>> flights = Retry.decorateSupplier(retry, () -> service.searchFlights(request));

        for (int i=0; i<10; i++) {
            service.setPotentialFailure(new FailHalfTheTime(2));
            System.out.println(flights.get());
        }

        Consumer<Meter> meterConsumer = meter -> {
            String desc = meter.getId().getDescription();
            String metricName = meter.getId().getTag("kind");
            Double metricValue = StreamSupport.stream(meter.measure().spliterator(), false).
                                                filter(m -> m.getStatistic().name().equals("COUNT")).
                                                findFirst().
                                                map(m -> m.getValue()).
                                                orElse(0.0);
            System.out.println(desc + " - " + metricName + ": " + metricValue);
        };
        meterRegistry.forEachMeter(meterConsumer);
    }

    public static void main(String[] args) {
        Examples examples = new Examples();

        System.out.println("------------------------ generalUsagePattern ----------------------------------------------");
        examples.generalUsagePattern();
        System.out.println("----------------------------------------------------------------------");

        System.out.println("------------------------- basicExample ---------------------------------------------");
        examples.basicExample();
        System.out.println("----------------------------------------------------------------------");

        System.out.println("------------------------- basicExample_ServiceThrowingException_v1 ---------------------------------------------");
        examples.basicExample_ServiceThrowingException_v1();
        System.out.println("----------------------------------------------------------------------");

        System.out.println("-------------------------- basicExample_ServiceThrowingException_v2 --------------------------------------------");
        examples.basicExample_ServiceThrowingException_v2();
        System.out.println("----------------------------------------------------------------------");

        System.out.println("--------------------------- predicateExample -------------------------------------------");
        examples.predicateExample();
        System.out.println("----------------------------------------------------------------------");

        System.out.println("---------------------------- retryExceptionsIgnoreExceptionsExample ------------------------------------------");
        examples.retryExceptionsIgnoreExceptionsExample();
        System.out.println("----------------------------------------------------------------------");

        System.out.println("---------------------------- intervalFunction_Random ------------------------------------------");
        examples.intervalFunction_Random();
        System.out.println("----------------------------------------------------------------------");

        System.out.println("----------------------------- intervalFunction_Exponential -----------------------------------------");
        examples.intervalFunction_Exponential();
        System.out.println("----------------------------------------------------------------------");

        System.out.println("----------------------------- asyncRetryExample -----------------------------------------");
        examples.asyncRetryExample();
        System.out.println("----------------------------------------------------------------------");

        System.out.println("----------------------------- retryEventsExample -----------------------------------------");
        examples.retryEventsExample();
        System.out.println("----------------------------------------------------------------------");

        System.out.println("------------------------------ retryOnException ----------------------------------------");
        examples.retryOnException();
        System.out.println("----------------------------------------------------------------------");

        System.out.println("------------------------------- retryMetrics ---------------------------------------");
        examples.retryMetrics();
        System.out.println("----------------------------------------------------------------------");
    }
}