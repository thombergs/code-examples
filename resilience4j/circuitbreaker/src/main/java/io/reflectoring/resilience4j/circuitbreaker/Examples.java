package io.reflectoring.resilience4j.circuitbreaker;

import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig.SlidingWindowType;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.github.resilience4j.decorators.Decorators;
import io.github.resilience4j.micrometer.tagged.TaggedCircuitBreakerMetrics;
import io.micrometer.core.instrument.Meter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import io.reflectoring.resilience4j.circuitbreaker.model.Flight;
import io.reflectoring.resilience4j.circuitbreaker.model.SearchRequest;
import io.reflectoring.resilience4j.circuitbreaker.services.FlightSearchService;
import io.reflectoring.resilience4j.circuitbreaker.services.delays.AlwaysSlowNSeconds;
import io.reflectoring.resilience4j.circuitbreaker.services.failures.SucceedNTimesAndThenFail;
import io.reflectoring.resilience4j.circuitbreaker.services.failures.SucceedXTimesFailYTimesAndThenSucceed;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.StreamSupport;

public class Examples
{
    static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss SSS");

    void displayDefaultValues() {
        CircuitBreakerConfig config = CircuitBreakerConfig.ofDefaults();
        System.out.println("failureRateThreshold = " + config.getFailureRateThreshold());
        System.out.println("minimumNumberOfCalls = " + config.getMinimumNumberOfCalls());
        System.out.println("permittedNumberOfCallsInHalfOpenState = " + config.getPermittedNumberOfCallsInHalfOpenState());
        System.out.println("maxWaitDurationInHalfOpenState = " + config.getMaxWaitDurationInHalfOpenState());
        System.out.println("slidingWindowSize = " + config.getSlidingWindowSize());
        System.out.println("slidingWindowType = " + config.getSlidingWindowType());
        System.out.println("slowCallRateThreshold = " + config.getSlowCallRateThreshold());
        System.out.println("slowCallDurationThreshold = " + config.getSlowCallDurationThreshold());
        System.out.println("automaticTransitionFromOpenToHalfOpenEnabled = " + config.isAutomaticTransitionFromOpenToHalfOpenEnabled());
        System.out.println("writableStackTraceEnabled = " + config.isWritableStackTraceEnabled());
    }

    void countBasedSlidingWindow_FailedCalls() {
        CircuitBreakerConfig config = CircuitBreakerConfig
            .custom()
            .slidingWindowType(SlidingWindowType.COUNT_BASED)
            .slidingWindowSize(10)
            .failureRateThreshold(70.0f)
            .build();
        CircuitBreakerRegistry registry = CircuitBreakerRegistry.of(config);
        CircuitBreaker circuitBreaker = registry.circuitBreaker("flightSearchService");

        FlightSearchService service = new FlightSearchService();
        SearchRequest request = new SearchRequest("NYC", "LAX", "12/31/2020");
        service.setPotentialFailure(new SucceedNTimesAndThenFail(3));

        Supplier<List<Flight>> flightsSupplier = () -> service.searchFlights(request);
        Supplier<List<Flight>> decoratedFlightsSupplier = circuitBreaker.decorateSupplier(flightsSupplier);

        for (int i=0; i<20; i++) {
            try {
                System.out.println(decoratedFlightsSupplier.get());
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    void countBasedSlidingWindow_SlowCalls() {
        CircuitBreakerConfig config = CircuitBreakerConfig
            .custom()
            .slidingWindowType(SlidingWindowType.COUNT_BASED)
            .slidingWindowSize(10)
            .slowCallRateThreshold(70.0f)
            .slowCallDurationThreshold(Duration.ofSeconds(2))
            .build();
        CircuitBreakerRegistry registry = CircuitBreakerRegistry.of(config);
        CircuitBreaker circuitBreaker = registry.circuitBreaker("flightSearchService");

        FlightSearchService service = new FlightSearchService();
        SearchRequest request = new SearchRequest("NYC", "LAX", "12/31/2020");
        service.setPotentialDelay(new AlwaysSlowNSeconds(2));

        Supplier<List<Flight>> flightsSupplier = circuitBreaker.decorateSupplier(() -> service.searchFlights(request));

        for (int i=0; i<20; i++) {
            try {
                System.out.println(flightsSupplier.get());
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    void countBasedSlidingWindow_Failed_And_SlowCalls() {
        CircuitBreakerConfig config = CircuitBreakerConfig
            .custom()
            .slidingWindowType(SlidingWindowType.COUNT_BASED)
            .slidingWindowSize(10)
            .failureRateThreshold(70.0f)
            .slowCallRateThreshold(70.0f)
            .slowCallDurationThreshold(Duration.ofSeconds(2))
            .build();
        CircuitBreakerRegistry registry = CircuitBreakerRegistry.of(config);
        CircuitBreaker circuitBreaker = registry.circuitBreaker("flightSearchService");

        FlightSearchService service = new FlightSearchService();
        SearchRequest request = new SearchRequest("NYC", "LAX", "12/31/2020");
        service.setPotentialDelay(new AlwaysSlowNSeconds(2));

        Supplier<List<Flight>> flightsSupplier = circuitBreaker.decorateSupplier(() -> service.searchFlights(request));

        for (int i=0; i<20; i++) {
            try {
                System.out.println(flightsSupplier.get());
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    void timeBasedSlidingWindow_FailedCalls() {
        CircuitBreakerConfig config = CircuitBreakerConfig
            .custom()
            .slidingWindowType(SlidingWindowType.TIME_BASED)
            .minimumNumberOfCalls(3)
            .slidingWindowSize(10)
            .failureRateThreshold(70.0f)
            .build();
        CircuitBreakerRegistry registry = CircuitBreakerRegistry.of(config);
        CircuitBreaker circuitBreaker = registry.circuitBreaker("flightSearchService");

        FlightSearchService service = new FlightSearchService();
        SearchRequest request = new SearchRequest("NYC", "LAX", "12/31/2020");
        service.setPotentialFailure(new SucceedNTimesAndThenFail(3));
        service.setPotentialDelay(new AlwaysSlowNSeconds(0));

        Supplier<List<Flight>> flightsSupplier = circuitBreaker.decorateSupplier(() -> service.searchFlights(request));

        System.out.println("Start time: " + LocalDateTime.now().format(formatter));
        for (int i=0; i<20; i++) {
            try {
                System.out.println(flightsSupplier.get());
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    void timeBasedSlidingWindow_SlowCalls() {
        CircuitBreakerConfig config = CircuitBreakerConfig
            .custom()
            .slidingWindowType(SlidingWindowType.TIME_BASED)
            .minimumNumberOfCalls(10)
            .slidingWindowSize(10)
            .slowCallRateThreshold(70.0f)
            .slowCallDurationThreshold(Duration.ofSeconds(1))
            .build();
        CircuitBreakerRegistry registry = CircuitBreakerRegistry.of(config);
        CircuitBreaker circuitBreaker = registry.circuitBreaker("flightSearchService");

        FlightSearchService service = new FlightSearchService();
        SearchRequest request = new SearchRequest("NYC", "LAX", "12/31/2020");
        service.setPotentialDelay(new AlwaysSlowNSeconds(1));

        Supplier<List<Flight>> flightsSupplier = circuitBreaker.decorateSupplier(() -> service.searchFlights(request));

        System.out.println("Start time: " + LocalDateTime.now().format(formatter));
        for (int i=0; i<20; i++) {
            try {
                System.out.println(flightsSupplier.get());
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    void circuitBreakerOpenAndThenClose() {
        CircuitBreakerConfig config = CircuitBreakerConfig
            .custom()
            .slidingWindowType(SlidingWindowType.COUNT_BASED)
            .slidingWindowSize(10)
            .failureRateThreshold(25.0f)
            .waitDurationInOpenState(Duration.ofSeconds(10))
            .permittedNumberOfCallsInHalfOpenState(4)
            .build();
        CircuitBreakerRegistry registry = CircuitBreakerRegistry.of(config);
        CircuitBreaker circuitBreaker = registry.circuitBreaker("flightSearchService");

        circuitBreaker.getEventPublisher().onCallNotPermitted(e -> {
            System.out.println(e.toString());
            // just to simulate lag so the circuitbreaker can change state
            try {
                Thread.sleep(1000);
            } catch (InterruptedException interruptedException) {
                interruptedException.printStackTrace();
            }
        });
        circuitBreaker.getEventPublisher().onError(e -> System.out.println(e.toString()));
        circuitBreaker.getEventPublisher().onStateTransition(e -> System.out.println(e.toString()));

        FlightSearchService service = new FlightSearchService();
        SearchRequest request = new SearchRequest("NYC", "LAX", "12/31/2020");
        service.setPotentialFailure(new SucceedXTimesFailYTimesAndThenSucceed(4, 4));

        Supplier<List<Flight>> flightsSupplier = circuitBreaker.decorateSupplier(() -> service.searchFlights(request));

        for (int i=0; i<50; i++) {
            try {
                System.out.println(flightsSupplier.get());
                Thread.sleep(1000);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    void fallback() {
        CircuitBreakerConfig config = CircuitBreakerConfig
            .custom()
            .slidingWindowType(SlidingWindowType.COUNT_BASED)
            .minimumNumberOfCalls(5)
            .slidingWindowSize(10)
            .failureRateThreshold(50.0f)
            .writableStackTraceEnabled(false)
            .build();
        CircuitBreakerRegistry registry = CircuitBreakerRegistry.of(config);
        CircuitBreaker circuitBreaker = registry.circuitBreaker("flightSearchService");

        circuitBreaker.getEventPublisher().onStateTransition(e -> System.out.println(e.toString()));
        circuitBreaker.getEventPublisher().onError(e -> System.out.println(e.toString()));

        FlightSearchService service = new FlightSearchService();
        SearchRequest request = new SearchRequest("NYC", "LAX", "12/31/2020");
        service.setPotentialFailure(new SucceedNTimesAndThenFail(3));

        Supplier<List<Flight>> flightsSupplier = () -> service.searchFlights(request);
        Supplier<List<Flight>> decorated = Decorators
            .ofSupplier(flightsSupplier)
            .withCircuitBreaker(circuitBreaker)
            .withFallback(Arrays.asList(CallNotPermittedException.class),
                            e -> this.getFlightSearchResultsFromCache(request))
            .decorate();

        for (int i=0; i<10; i++) {
            try {
                System.out.println(decorated.get());
            }
            catch (Exception e) {
            }
        }
    }

    private List<Flight> getFlightSearchResultsFromCache(SearchRequest request) {
        List<Flight> flights = Arrays.asList(
            new Flight("XY 765", request.getFlightDate(), request.getFrom(), request.getTo()),
            new Flight("XY 781", request.getFlightDate(), request.getFrom(), request.getTo()),
            new Flight("XY 732", request.getFlightDate(), request.getFrom(), request.getTo()),
            new Flight("XY 746", request.getFlightDate(), request.getFrom(), request.getTo())
        );
        System.out.println("Returning flight search results from the cache");
        return flights;
    }

    void writeStackTraceDisabled() {
        CircuitBreakerConfig config = CircuitBreakerConfig
            .custom()
            .slidingWindowType(SlidingWindowType.COUNT_BASED)
            .slidingWindowSize(10)
            .failureRateThreshold(70.0f)
            .writableStackTraceEnabled(false)
            .build();
        CircuitBreakerRegistry registry = CircuitBreakerRegistry.of(config);
        CircuitBreaker circuitBreaker = registry.circuitBreaker("flightSearchService");

        FlightSearchService service = new FlightSearchService();
        SearchRequest request = new SearchRequest("NYC", "LAX", "12/31/2020");
        service.setPotentialFailure(new SucceedNTimesAndThenFail(3));

        Supplier<List<Flight>> flightsSupplier = () -> service.searchFlights(request);
        Supplier<List<Flight>> decoratedFlightsSupplier = circuitBreaker.decorateSupplier(flightsSupplier);

        for (int i=0; i<15; i++) {
            try {
                System.out.println(decoratedFlightsSupplier.get());
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    void events() {
        CircuitBreakerConfig config = CircuitBreakerConfig
            .custom()
            .slidingWindowType(SlidingWindowType.COUNT_BASED)
            .slidingWindowSize(10)
            .failureRateThreshold(70.0f)
            .build();
        CircuitBreakerRegistry registry = CircuitBreakerRegistry.of(config);
        CircuitBreaker circuitBreaker = registry.circuitBreaker("flightSearchService");

        circuitBreaker.getEventPublisher()
            .onCallNotPermitted(e -> System.out.println(e.toString()));
        circuitBreaker.getEventPublisher().onError(e -> System.out.println(e.toString()));
        circuitBreaker.getEventPublisher()
            .onFailureRateExceeded(e -> System.out.println(e.toString()));
        circuitBreaker.getEventPublisher().onStateTransition(e -> System.out.println(e.toString()));

        FlightSearchService service = new FlightSearchService();
        SearchRequest request = new SearchRequest("NYC", "LAX", "12/31/2020");
        service.setPotentialFailure(new SucceedNTimesAndThenFail(3));

        Supplier<List<Flight>> flightsSupplier = circuitBreaker
            .decorateSupplier(() -> service.searchFlights(request));

        for (int i = 0; i < 20; i++) {
            try {
                System.out.println(flightsSupplier.get());
            } catch (Exception e) {
            }
        }
    }

    void metrics() {
        CircuitBreakerConfig config = CircuitBreakerConfig
            .custom()
            .slidingWindowType(SlidingWindowType.COUNT_BASED)
            .slidingWindowSize(10)
            .failureRateThreshold(70.0f)
            .build();
        CircuitBreakerRegistry registry = CircuitBreakerRegistry.of(config);
        CircuitBreaker circuitBreaker = registry.circuitBreaker("flightSearchService");

        MeterRegistry meterRegistry = new SimpleMeterRegistry();
        TaggedCircuitBreakerMetrics.ofCircuitBreakerRegistry(registry)
            .bindTo(meterRegistry);

        circuitBreaker.getEventPublisher()
            .onCallNotPermitted(e -> printMetricDetails(meterRegistry));
        circuitBreaker.getEventPublisher().onError(e -> printMetricDetails(meterRegistry));
        circuitBreaker.getEventPublisher()
            .onFailureRateExceeded(e -> printMetricDetails(meterRegistry));
        circuitBreaker.getEventPublisher().onStateTransition(e -> printMetricDetails(meterRegistry));

        FlightSearchService service = new FlightSearchService();
        SearchRequest request = new SearchRequest("NYC", "LAX", "12/31/2020");
        service.setPotentialFailure(new SucceedNTimesAndThenFail(3));

        Supplier<List<Flight>> flightsSupplier = circuitBreaker
            .decorateSupplier(() -> service.searchFlights(request));

        for (int i = 0; i < 20; i++) {
            try {
                System.out.println(flightsSupplier.get());
            } catch (Exception e) {
            }
        }
        printMetricDetails(meterRegistry);
    }

    void printMetricDetails(MeterRegistry meterRegistry) {
        Consumer<Meter> meterConsumer = meter -> {
            String desc = meter.getId().getDescription();
            String metricName = meter.getId().getName();
            String tagName = "";
            String tagValue = "";
            if (metricName.equals("resilience4j.circuitbreaker.state")) {
                tagName = "state";
                tagValue = meter.getId().getTag(tagName);
            }
            if (metricName.equals("resilience4j.circuitbreaker.calls")) {
                tagName = "kind";
                tagValue = meter.getId().getTag(tagName);
            }
            Double metricValue = StreamSupport.stream(meter.measure().spliterator(), false)
                .filter(m -> {
                    return m.getStatistic().name().equals("VALUE");
                })
                .findFirst()
                .map(m -> m.getValue())
                .orElse(0.0);
            System.out.print(desc + " - " + metricName + ": " + metricValue);
            if (!tagValue.isEmpty()) {
                System.out.println(", " + tagName + ": " + tagValue);
            }
            else {
                System.out.println();
            }
        };
        meterRegistry.forEachMeter(meterConsumer);
    }

    public static void main( String[] args )
    {
        Examples examples = new Examples();
        System.out.println("--------------------------- displayDefaultValues --------------------------------------------");
        examples.displayDefaultValues();
        System.out.println("-----------------------------------------------------------------------");

        System.out.println("---------------------------- countBasedSlidingWindow_FailedCalls -------------------------------------------");
        examples.countBasedSlidingWindow_FailedCalls();
        System.out.println("-----------------------------------------------------------------------");

        System.out.println("----------------------------- countBasedSlidingWindow_SlowCalls ------------------------------------------");
        examples.countBasedSlidingWindow_SlowCalls();
        System.out.println("-----------------------------------------------------------------------");

        System.out.println("------------------------------ timeBasedSlidingWindow_FailedCalls -----------------------------------------");
        examples.timeBasedSlidingWindow_FailedCalls();
        System.out.println("-----------------------------------------------------------------------");

        System.out.println("-------------------------------- timeBasedSlidingWindow_SlowCalls ---------------------------------------");
        examples.timeBasedSlidingWindow_SlowCalls();
        System.out.println("-----------------------------------------------------------------------");

        System.out.println("--------------------------------- writeStackTraceDisabled --------------------------------------");
        examples.writeStackTraceDisabled();
        System.out.println("-----------------------------------------------------------------------");

        System.out.println("---------------------------------- circuitBreakerOpenAndThenClose -------------------------------------");
        examples.circuitBreakerOpenAndThenClose();
        System.out.println("-----------------------------------------------------------------------");

        System.out.println("---------------------------------- fallback -------------------------------------");
        examples.fallback();
        System.out.println("-----------------------------------------------------------------------");

        System.out.println("---------------------------------- events -------------------------------------");
        examples.events();
        System.out.println("-----------------------------------------------------------------------");

        System.out.println("----------------------------------- metrics ------------------------------------");
        examples.metrics();
        System.out.println("-----------------------------------------------------------------------");
    }
}