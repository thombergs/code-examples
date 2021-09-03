package io.reflectoring.resilience4j.springboot.predicates;

import io.reflectoring.resilience4j.springboot.model.SearchResponse;
import java.util.function.Predicate;

public class ConditionalRetryPredicate implements Predicate<SearchResponse> {
  @Override
  public boolean test(SearchResponse searchResponse) {
    if (searchResponse.getErrorCode() != null) {
      System.out.println("Search returned error code = " + searchResponse.getErrorCode());
      return searchResponse.getErrorCode().equals("FS-167");
    }
    return false;
  }
}