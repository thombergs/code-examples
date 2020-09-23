package io.reflectoring.resilience4j.ratelimit;

import io.github.resilience4j.micrometer.tagged.TaggedRateLimiterMetrics;
import io.github.resilience4j.ratelimiter.RateLimiter;
import io.github.resilience4j.ratelimiter.RateLimiterConfig;
import io.github.resilience4j.ratelimiter.RateLimiterRegistry;
import io.github.resilience4j.retry.Retry;
import io.github.resilience4j.retry.RetryConfig;
import io.github.resilience4j.retry.RetryRegistry;
import io.micrometer.core.instrument.Meter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import io.reflectoring.resilience4j.ratelimit.model.Flight;
import io.reflectoring.resilience4j.ratelimit.model.SearchRequest;
import io.reflectoring.resilience4j.ratelimit.services.FlightSearchService;
import io.vavr.CheckedFunction0;
import java.time.Duration;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.StreamSupport;

public class Examples
{
    void displayDefaultValues() {
        RateLimiterConfig config = RateLimiterConfig.ofDefaults();

        System.out.println("Limit for period = " + config.getLimitForPeriod());
        System.out.println(("Refresh period = " + Duration.from(config.getLimitRefreshPeriod()).toNanos()));
        System.out.println("Timeout value = " + Duration.from(config.getTimeoutDuration()).toMillis());
    }

    void basicExample() {
        RateLimiterConfig config = RateLimiterConfig.custom().
            limitForPeriod(1).
            limitRefreshPeriod(Duration.ofSeconds(1)).
            timeoutDuration(Duration.ofSeconds(1)).build();
        RateLimiterRegistry registry = RateLimiterRegistry.of(config);
        RateLimiter limiter = registry.rateLimiter("flightSearchService");

        FlightSearchService service = new FlightSearchService();
        SearchRequest request = new SearchRequest("NYC", "LAX", "07/31/2020");

        Supplier<List<Flight>> flightsSupplier = RateLimiter.decorateSupplier(limiter, () -> service.searchFlights(request));
        for (int i=0; i<3; i++) {
            System.out.println(flightsSupplier.get());
        }
    }

    void timeoutExample() {
        RateLimiterConfig config = RateLimiterConfig.custom().
            limitForPeriod(1).
            limitRefreshPeriod(Duration.ofSeconds(1)).
            timeoutDuration(Duration.ofMillis(250)).build();
        RateLimiterRegistry registry = RateLimiterRegistry.of(config);
        RateLimiter limiter = registry.rateLimiter("flightSearchService");

        FlightSearchService service = new FlightSearchService();
        SearchRequest request = new SearchRequest("NYC", "LAX", "07/31/2020");

        Supplier<List<Flight>> flightsSupplier = RateLimiter.decorateSupplier(limiter, () -> service.searchFlights(request));
        for (int i=0; i<3; i++) {
            try {
                System.out.println(flightsSupplier.get());
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    void checkedExceptionExample() {
        RateLimiterConfig config = RateLimiterConfig.custom().
            limitForPeriod(1).
            limitRefreshPeriod(Duration.ofSeconds(1)).
            timeoutDuration(Duration.ofSeconds(1)).build();
        RateLimiterRegistry registry = RateLimiterRegistry.of(config);
        RateLimiter limiter = registry.rateLimiter("flightSearchService");

        FlightSearchService service = new FlightSearchService();
        SearchRequest request = new SearchRequest("NYC", "LAX", "07/31/2020");

        CheckedFunction0<List<Flight>> flights = RateLimiter.decorateCheckedSupplier(limiter, () -> service.searchFlightsThrowingException(request));
        try {
            System.out.println(flights.apply());
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }

    void usagePattern() {
        RateLimiterConfig config = RateLimiterConfig.ofDefaults();
        RateLimiterRegistry registry = RateLimiterRegistry.of(config);
        RateLimiter limiter = registry.rateLimiter("flightSearchService");

        FlightSearchService service = new FlightSearchService();
        SearchRequest request = new SearchRequest("NYC", "LAX", "07/31/2020");

        Supplier<List<Flight>> flightsSupplier = RateLimiter.decorateSupplier(limiter, () -> service.searchFlights(request));
        System.out.println(flightsSupplier.get());
    }


    void multipleLimits_2rps_40rpm_sequential() {
        RateLimiterConfig rpsConfig = RateLimiterConfig.custom().
            limitForPeriod(2).
            limitRefreshPeriod(Duration.ofSeconds(1)).
            timeoutDuration(Duration.ofMillis(2000)).build();
        RateLimiterConfig rpmConfig = RateLimiterConfig.custom().
            limitForPeriod(40).
            limitRefreshPeriod(Duration.ofMinutes(1)).
            timeoutDuration(Duration.ofMillis(2000)).build();

        RateLimiterRegistry registry = RateLimiterRegistry.of(rpsConfig);
        RateLimiter rpsLimiter = registry.rateLimiter("flightSearchService_rps", rpsConfig);
        RateLimiter rpmLimiter = registry.rateLimiter("flightSearchService_rpm", rpmConfig);


        FlightSearchService service = new FlightSearchService();
        SearchRequest request = new SearchRequest("NYC", "LAX", "07/31/2020");

        Supplier<List<Flight>> rpsLimitedSupplier = RateLimiter.decorateSupplier(rpsLimiter, () -> service.searchFlights(request));
        Supplier<List<Flight>> flightsSupplier = RateLimiter.decorateSupplier(rpmLimiter, rpsLimitedSupplier);
        for (int i=0; i<45; i++) {
            try {
                System.out.println(flightsSupplier.get());
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    void changeLimitsExample() {
        RateLimiterConfig config = RateLimiterConfig.custom().
            limitForPeriod(1).
            limitRefreshPeriod(Duration.ofSeconds(1)).
            timeoutDuration(Duration.ofSeconds(1)).build();
        RateLimiterRegistry registry = RateLimiterRegistry.of(config);
        RateLimiter limiter = registry.rateLimiter("flightSearchService");

        FlightSearchService service = new FlightSearchService();
        SearchRequest request = new SearchRequest("NYC", "LAX", "07/31/2020");

        Supplier<List<Flight>> flightsSupplier = RateLimiter.decorateSupplier(limiter, () -> service.searchFlights(request));
        for (int i=0; i<3; i++) {
            System.out.println(flightsSupplier.get());
        }

        limiter.changeLimitForPeriod(2);
        limiter.changeTimeoutDuration(Duration.ofSeconds(2));

        for (int i=0; i<4; i++) {
            System.out.println(flightsSupplier.get());
        }
    }

    void retryAndRateLimit() {
        RateLimiterConfig config = RateLimiterConfig.custom().
            limitForPeriod(1).
            limitRefreshPeriod(Duration.ofSeconds(1)).
            timeoutDuration(Duration.ofMillis(250)).build();
        RateLimiterRegistry registry = RateLimiterRegistry.of(config);
        RateLimiter rateLimiter = registry.rateLimiter("flightSearchService");

        FlightSearchService service = new FlightSearchService();
        SearchRequest request = new SearchRequest("NYC", "LAX", "07/31/2020");


        RetryConfig retryConfig = RetryConfig.custom().maxAttempts(2).waitDuration(Duration.ofSeconds(1)).build();
        RetryRegistry retryRegistry = RetryRegistry.of(retryConfig);
        Retry retry = retryRegistry.retry("rateLimitedFlightSearch", retryConfig);
        retry.getEventPublisher().onRetry(e -> System.out.println(e.toString()));
        retry.getEventPublisher().onSuccess(e -> System.out.println(e.toString()));

        Supplier<List<Flight>> rateLimitedFlightsSupplier = RateLimiter.decorateSupplier(rateLimiter, () -> service.searchFlights(request));
        Supplier<List<Flight>> retryingFlightsSupplier = Retry.decorateSupplier(retry, rateLimitedFlightsSupplier);

        System.out.println(retryingFlightsSupplier.get());
        System.out.println(retryingFlightsSupplier.get());
    }

    private void limit_1rps_concurrent() {
        RateLimiterConfig config = RateLimiterConfig.custom().
                                    limitForPeriod(1).
                                    limitRefreshPeriod(Duration.ofSeconds(1)).
                                    timeoutDuration(Duration.ofMillis(50)).build();
        RateLimiterRegistry registry = RateLimiterRegistry.of(config);
        RateLimiter limiter = registry.rateLimiter("flightSearchService");

        FlightSearchService service = new FlightSearchService();
        SearchRequest request = new SearchRequest("NYC", "LAX", "07/31/2020");

        Supplier<List<Flight>> flightsSupplier = RateLimiter.decorateSupplier(limiter, () -> service.searchFlights(request));

        CompletableFuture.supplyAsync(flightsSupplier).thenAccept(System.out::println);
        CompletableFuture.supplyAsync(flightsSupplier).whenComplete((flights, e) -> {
            if (e != null) e.printStackTrace();
        }).thenAccept(System.out::println);
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    void rateLimiterEvents() {
        RateLimiterConfig config = RateLimiterConfig.custom().
            limitForPeriod(1).
            limitRefreshPeriod(Duration.ofSeconds(1)).
            timeoutDuration(Duration.ofMillis(50)).build();
        RateLimiterRegistry registry = RateLimiterRegistry.of(config);
        RateLimiter limiter = registry.rateLimiter("flightSearchService");

        limiter.getEventPublisher().onSuccess(e -> System.out.println(e.toString()));
        limiter.getEventPublisher().onFailure(e -> System.out.println(e.toString()));

        FlightSearchService service = new FlightSearchService();
        SearchRequest request = new SearchRequest("NYC", "LAX", "07/31/2020");

        Supplier<List<Flight>> flightsSupplier = RateLimiter.decorateSupplier(limiter, () -> service.searchFlights(request));
        try {
            flightsSupplier.get();
            flightsSupplier.get();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    void rateLimiterMetrics() {
        RateLimiterConfig config = RateLimiterConfig.custom().
                                    limitForPeriod(1).
                                    limitRefreshPeriod(Duration.ofSeconds(1)).
                                    timeoutDuration(Duration.ofSeconds(1)).
                                    build();
        RateLimiterRegistry registry = RateLimiterRegistry.of(config);
        RateLimiter retry = registry.rateLimiter("flightSearchService", config);

        MeterRegistry meterRegistry = new SimpleMeterRegistry();
        TaggedRateLimiterMetrics.ofRateLimiterRegistry(registry).bindTo(meterRegistry);

        FlightSearchService service = new FlightSearchService();

        SearchRequest request = new SearchRequest("NYC", "LAX", "07/31/2020");
        Supplier<List<Flight>> flightsSupplier = RateLimiter.decorateSupplier(retry, () -> service.searchFlights(request));

        int count = 0;
        for (int i=0; i<50; i++) {
            count++;
            CompletableFuture.supplyAsync(flightsSupplier).whenComplete((flights, e) -> {
                if (e != null) e.printStackTrace();
            }).thenAccept(System.out::println);

            if (count % 10 == 0) {
                printMetricDetails(meterRegistry);
            }
        }
    }

    void printMetricDetails(MeterRegistry meterRegistry) {
        Consumer<Meter> meterConsumer = meter -> {
            String desc = meter.getId().getDescription();
            String metricName = meter.getId().getName();
            Double metricValue = StreamSupport.stream(meter.measure().spliterator(), false).
                filter(m -> m.getStatistic().name().equals("VALUE")).
                findFirst().
                map(m -> m.getValue()).
                orElse(0.0);
            System.out.println(desc + " - " + metricName + ": " + metricValue);
        };
        meterRegistry.forEachMeter(meterConsumer);
    }

    public static void main( String[] args ) {
        Examples examples = new Examples();
        System.out.println("---------------------------- displayDefaultValues -------------------------------------------");
        examples.displayDefaultValues();
        System.out.println("-----------------------------------------------------------------------");
        System.out.println("----------------------------- usagePattern ------------------------------------------");
        examples.usagePattern();
        System.out.println("-----------------------------------------------------------------------");
        System.out.println("----------------------------- basicExample ------------------------------------------");
        examples.basicExample();
        System.out.println("-----------------------------------------------------------------------");
        System.out.println("----------------------------- timeoutExample ------------------------------------------");
        examples.timeoutExample();
        System.out.println("-----------------------------------------------------------------------");
        System.out.println("------------------------------ checkedExceptionExample -----------------------------------------");
        examples.checkedExceptionExample();
        System.out.println("-----------------------------------------------------------------------");
        System.out.println("------------------------------ multipleLimits_2rps_40rpm_sequential -----------------------------------------");
        examples.multipleLimits_2rps_40rpm_sequential();
        System.out.println("-----------------------------------------------------------------------");
        System.out.println("------------------------------- changeLimitsExample ----------------------------------------");
        examples.changeLimitsExample();
        System.out.println("-----------------------------------------------------------------------");
        System.out.println("------------------------------- retryAndRateLimit ----------------------------------------");
        examples.retryAndRateLimit();
        System.out.println("-----------------------------------------------------------------------");
        System.out.println("------------------------------- limit_1rps_concurrent ----------------------------------------");
        examples.limit_1rps_concurrent();
        System.out.println("-----------------------------------------------------------------------");
        System.out.println("------------------------------ rateLimiterEvents -----------------------------------------");
        examples.rateLimiterEvents();
        System.out.println("-----------------------------------------------------------------------");
        System.out.println("------------------------------- rateLimiterMetrics ----------------------------------------");
        examples.rateLimiterMetrics();
        System.out.println("-----------------------------------------------------------------------");
    }
}