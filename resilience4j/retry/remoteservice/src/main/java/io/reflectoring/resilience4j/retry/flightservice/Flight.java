package io.reflectoring.resilience4j.retry.flightservice;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

@Data
@AllArgsConstructor
@ToString
public class Flight {
    String flightNumber;
    String flightDate;
    String from;
    String to;
}
