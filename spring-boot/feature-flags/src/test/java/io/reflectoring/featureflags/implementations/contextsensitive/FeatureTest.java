package io.reflectoring.featureflags.implementations.contextsensitive;

import io.reflectoring.featureflags.implementations.contextsensitive.Feature.RolloutStrategy;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.offset;

public class FeatureTest {

    @Test
    void testHashCode(){
        Feature feature = new Feature(RolloutStrategy.PERCENTAGE, "true", "false", 50);
        assertThat(feature.percentageHashCode("1")).isCloseTo(27.74d, offset(0.01d));
        assertThat(feature.percentageHashCode("2")).isCloseTo(81.12d, offset(0.01d));
        assertThat(feature.percentageHashCode("3")).isCloseTo(21.69d, offset(0.01d));
    }

}
