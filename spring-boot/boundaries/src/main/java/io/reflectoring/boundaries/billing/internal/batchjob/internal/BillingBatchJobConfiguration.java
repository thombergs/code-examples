package io.reflectoring.boundaries.billing.internal.batchjob.internal;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableScheduling
@ComponentScan
class BillingBatchJobConfiguration {

}
