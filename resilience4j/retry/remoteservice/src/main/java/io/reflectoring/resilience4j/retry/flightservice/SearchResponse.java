package io.reflectoring.resilience4j.retry.flightservice;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class SearchResponse {
    String errorCode;
    List<Flight> flights;
}
