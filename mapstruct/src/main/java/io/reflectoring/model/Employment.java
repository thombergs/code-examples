package io.reflectoring.model;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@Data
@Builder
@ToString
public class Employment {
    private DesignationCode designation;
    private long salary;
}
