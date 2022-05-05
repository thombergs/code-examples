package com.reflectoring.lombok.model;

import lombok.Builder;
import lombok.NonNull;

@Builder
public class Job {
    private String id;

    @NonNull
    private JobType jobType;
}
