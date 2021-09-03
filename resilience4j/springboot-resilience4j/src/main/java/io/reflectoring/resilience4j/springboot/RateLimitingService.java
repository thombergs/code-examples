package io.reflectoring.resilience4j.springboot;

import io.github.resilience4j.ratelimiter.RateLimiterRegistry;
import io.github.resilience4j.ratelimiter.RequestNotPermitted;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.RetryRegistry;
import io.github.resilience4j.retry.annotation.Retry;
import io.reflectoring.resilience4j.springboot.model.Flight;
import io.reflectoring.resilience4j.springboot.model.SearchRequest;
import io.reflectoring.resilience4j.springboot.services.FlightSearchService;
import java.time.Duration;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service
public class RateLimitingService {
  @Autowired
  private FlightSearchService remoteSearchService;

  @Autowired
  private RPMRateLimitedFlightSearchSearch rpmRateLimitedFlightSearchSearch;

  @Autowired
  private RateLimiterRegistry registry;

  @Autowired
  private RetryRegistry retryRegistry;

  DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss SSS");

  @RateLimiter(name = "basicExample")
  List<Flight> basicExample(SearchRequest request) {
    return remoteSearchService.searchFlights(request);
  }

  @RateLimiter(name = "timeoutExample")
  List<Flight> timeoutExample(SearchRequest request) {
    return remoteSearchService.searchFlights(request);
  }

  @RateLimiter(name = "multipleRateLimiters_rps_limiter")
  List<Flight> multipleRateLimitsExample(SearchRequest request) {
    return rpmRateLimitedFlightSearchSearch.searchFlights(request, remoteSearchService);
  }

// doesn't work - @RateLimiter is not a repeatable annotation
//  @RateLimiter(name = "multipleRateLimiters_rps_limiter")
//  @RateLimiter(name = "multipleRateLimiters_rpm_limiter")
//  List<Flight> multipleRateLimitsExample(SearchRequest request) {
//    return remoteSearchService.searchFlights(request, remoteSearchService);
//  }

// doesn't work - calls within a Spring bean don't go thru the Spring proxy
//  @RateLimiter(name = "multipleRateLimiters_rps_limiter")
//  List<Flight> rpsLimitedSearch(SearchRequest request) {
//    return rpmLimitedSearch(request, remoteSearchService);
//  }

//  @RateLimiter(name = "multipleRateLimiters_rpm_limiter")
//  List<Flight> rpmLimitedSearch(SearchRequest request) {
//    return remoteSearchService.searchFlights(request, remoteSearchService);
//  }


  @RateLimiter(name = "changeLimitsExample")
  public List<Flight> changeLimitsExample(SearchRequest request) {
    return remoteSearchService.searchFlights(request);
  }

  @Retry(name = "retryAndRateLimitExample")
  @RateLimiter(name = "retryAndRateLimitExample")
  public List<Flight> retryAndRateLimit(SearchRequest request) {
    return remoteSearchService.searchFlights(request);
  }

  @RateLimiter(name = "rateLimiterEventsExample")
  public List<Flight> rateLimiterEventsExample(SearchRequest request) {
    return remoteSearchService.searchFlights(request);
  }

  public void updateRateLimits(String rateLimiterName, int newLimitForPeriod, Duration newTimeoutDuration) {
    io.github.resilience4j.ratelimiter.RateLimiter limiter = registry.rateLimiter(rateLimiterName);
    limiter.changeLimitForPeriod(newLimitForPeriod);
    limiter.changeTimeoutDuration(newTimeoutDuration);
  }

  @RateLimiter(name = "fallbackExample", fallbackMethod = "localCacheFlightSearch")
  public List<Flight> fallbackExample(SearchRequest request) {
    return remoteSearchService.searchFlights(request);
  }

  private List<Flight> localCacheFlightSearch(SearchRequest request, RequestNotPermitted rnp) {
    System.out.println("Returning search results from cache");
    return Arrays.asList(
        new Flight("XY 765", request.getFlightDate(), request.getFrom(), request.getTo()),
        new Flight("XY 781", request.getFlightDate(), request.getFrom(), request.getTo()));
  }

  @PostConstruct
  public void postConstruct() {
    io.github.resilience4j.retry.Retry.EventPublisher retryEventPublisher = retryRegistry
        .retry("retryAndRateLimitExample")
        .getEventPublisher();

    retryEventPublisher.onRetry(System.out::println);
    retryEventPublisher.onSuccess(System.out::println);

    io.github.resilience4j.ratelimiter.RateLimiter.EventPublisher eventPublisher = registry
        .rateLimiter("rateLimiterEventsExample")
        .getEventPublisher();

    eventPublisher.onSuccess(System.out::println);
    eventPublisher.onFailure(System.out::println);
  }
}

@Component
class RPMRateLimitedFlightSearchSearch {
  @RateLimiter(name = "multipleRateLimiters_rpm_limiter")
  List<Flight> searchFlights(SearchRequest request, FlightSearchService remoteSearchService) {
    return remoteSearchService.searchFlights(request);
  }
}