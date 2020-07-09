package io.reflectoring.resilience4j.retry.flightservice;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SearchRequest {
    String from;
    String to;
    String flightDate;
}
