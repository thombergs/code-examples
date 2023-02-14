package source.assertj;

import java.util.function.Consumer;
import java.util.stream.Stream;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

public class AssertJTest {

	@ParameterizedTest
	@MethodSource("checkNumberArgs")
	void checkNumber(int number, Consumer<Integer> consumer) {

		consumer.accept(number);
	}

	static Stream<Arguments> checkNumberArgs() {

		Consumer<Integer> evenConsumer = i -> Assertions.assertThat(i % 2).isZero();
		Consumer<Integer> oddConsumer = i -> Assertions.assertThat(i % 2).isEqualTo(1);

		return Stream.of(Arguments.of(2, evenConsumer), Arguments.of(3, oddConsumer));
	}
}
