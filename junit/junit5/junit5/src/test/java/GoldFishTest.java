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

public class GoldFishTest {
    @Test
    public void testBooleanAssumption() {
        GoldFish goldFish = new GoldFish("Windows Jelly", 1);

        assumeTrue(System.getProperty("os.name").contains("Windows"));
        assertThat(goldFish.getName(), equalToIgnoringCase("Windows Jelly"));
    }

    @Test
    public void testBooleanAssert() {
        GoldFish goldFish = new GoldFish("Windows Jelly", 1);

        assert(System.getProperty("os.name").contains("Windows"));
        assertThat(goldFish.getName(), equalToIgnoringCase("Windows Jelly"));
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
