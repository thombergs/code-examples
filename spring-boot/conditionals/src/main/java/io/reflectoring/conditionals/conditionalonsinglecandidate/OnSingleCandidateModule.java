package io.reflectoring.conditionals.conditionalonsinglecandidate;

import javax.sql.DataSource;

import org.springframework.boot.autoconfigure.condition.ConditionalOnSingleCandidate;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnSingleCandidate(DataSource.class)
class OnSingleCandidateModule {
}
