import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalToIgnoringCase;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assumptions.assumeTrue;
import static org.junit.jupiter.api.Assumptions.assumingThat;

public class GoldFishTest {
    @Test
    public void testBooleanAssumption() {
        GoldFish goldFish = new GoldFish("Jelly", 1);

        assumeTrue(goldFish.getAge() > 0);
        assertThat(goldFish.getName(), equalToIgnoringCase("Jelly"));
    }

    @Test
    public void testAssumption() {
        GoldFish goldFish = new GoldFish("Jelly", 1);

        assumingThat(goldFish.getAge() > 0,
                () -> assertThat(goldFish.getName(), equalToIgnoringCase("Jelly")));
    }

    @Test
    public void testException() {
        GoldFish goldFish = new GoldFish("Goldy", 0);

        RuntimeException exception = assertThrows(RuntimeException.class, goldFish::calculateSpeed);

        assertThat(exception.getMessage(), equalToIgnoringCase("This will fail :(("));
    }

    @ParameterizedTest
    @MethodSource("provideFishes")
    public void parameterizedTest(GoldFish goldFish) {
        assertTrue(goldFish.getAge() >= 1);
    }

    private static Stream<Arguments> provideFishes() {
        return Stream.of(
                Arguments.of(new GoldFish("Browny", 1)),
                Arguments.of(new GoldFish("Greeny", 2))
        );
    }
}
